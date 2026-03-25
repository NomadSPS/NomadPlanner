package com.projectlibre1.activation;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.UUID;

import com.projectlibre1.activation.ActivationStateStore.StoredActivationState;
import com.projectlibre1.configuration.Settings;
import com.projectlibre1.util.Environment;

public final class ActivationService {
	public static final String PRODUCT_NAME = "NomadPlan";
	public static final String TOKEN_PREFIX = "NMP1";
	private static final String TOKEN_VERSION = "1";
	private static final String PUBLIC_KEY_RESOURCE = "/com/projectlibre1/activation/keys/production-public.pem";
	private static final Duration CLOCK_ROLLBACK_TOLERANCE = Duration.ofMinutes(15);
	private static final long EXPIRY_WARNING_DAYS = 14L;

	private static final ActivationService INSTANCE = new ActivationService();

	private final ActivationStateStore stateStore = new ActivationStateStore();
	private final MachineFingerprintService machineFingerprintService = MachineFingerprintService.getInstance();
	private final DeveloperActivationPolicy developerActivationPolicy = new DeveloperActivationPolicy();
	private final PublicKey productionPublicKey;

	public static ActivationService getInstance() {
		return INSTANCE;
	}

	private ActivationService() {
		try {
			productionPublicKey = loadPublicKey(PUBLIC_KEY_RESOURCE);
		} catch (Exception e) {
			throw new IllegalStateException("Could not load production activation public key", e);
		}
	}

	public String getInstallationId() {
		return machineFingerprintService.getInstallationId();
	}

	public String getInstallationHash() {
		return machineFingerprintService.getInstallationHash();
	}

	public boolean isDeveloperModeEnabled() {
		return developerActivationPolicy.isDeveloperModeEnabled();
	}

	public ActivationSummary getCurrentSummary() {
		return validateStoredActivation(false);
	}

	public ActivationSummary validateForStartup() {
		return validateStoredActivation(true);
	}

	public ActivationSummary activate(String serialNumber, String activationCode) {
		String normalizedSerial = normalize(serialNumber);
		String normalizedToken = normalize(activationCode);
		if ((normalizedSerial == null) || (normalizedToken == null)) {
			return buildSummary(ActivationStatus.INVALID_TOKEN, normalizedSerial, null, "Serial number and activation code are required.");
		}

		ActivationPayload payload;
		try {
			payload = ActivationVerifier.parseAndVerifyToken(normalizedToken, TOKEN_PREFIX, productionPublicKey);
		} catch (IllegalArgumentException e) {
			return buildSummary(ActivationStatus.INVALID_SIGNATURE, normalizedSerial, null, e.getMessage());
		} catch (Exception e) {
			return buildSummary(ActivationStatus.INVALID_TOKEN, normalizedSerial, null, "Activation code could not be parsed.");
		}

		Instant now = Instant.now();
		ActivationSummary summary = validatePayload(normalizedSerial, payload, now, false);
		if (!summary.isActive()) {
			return summary;
		}

		StoredActivationState state = StoredActivationState.token(normalizedSerial, normalizedToken, now);
		saveState(state);
		setValidationProperty("active:" + payload.getExpiresAtUtc());
		return buildSummary(ActivationStatus.VALID, normalizedSerial, payload.getExpiresAtUtc(), "Activation successful.");
	}

	public ActivationSummary activateDeveloperOverride(String serialNumber) {
		if (!developerActivationPolicy.isDeveloperModeEnabled()) {
			return buildSummary(ActivationStatus.DEVELOPER_OVERRIDE_DISABLED, null, null, "Developer activation override is disabled.");
		}
		String normalizedSerial = normalize(serialNumber);
		String developerSerialId;
		try {
			developerSerialId = developerActivationPolicy.validateDeveloperSerial(normalizedSerial);
		} catch (IllegalArgumentException e) {
			return buildSummary(ActivationStatus.INVALID_DEVELOPER_SERIAL, null, null, e.getMessage());
		}
		Instant now = Instant.now();
		StoredActivationState state = StoredActivationState.developerOverride("DEV:" + developerSerialId, normalizedSerial, now);
		saveState(state);
		setValidationProperty("developer:" + developerSerialId);
		return buildSummary(ActivationStatus.DEVELOPER_OVERRIDE, "DEV:" + developerSerialId, null, "Developer activation override is active.");
	}

	public boolean shouldShowExpiryWarning(ActivationSummary summary) {
		if ((summary == null) || !summary.isActive() || summary.isDeveloperOverride() || !summary.isExpiringSoon()) {
			return false;
		}
		StoredActivationState state = stateStore.load();
		LocalDate today = LocalDate.now(ZoneOffset.UTC);
		return (state == null) || !today.equals(state.lastWarningDate);
	}

	public void markExpiryWarningShown() {
		StoredActivationState state = stateStore.load();
		if (state == null) {
			return;
		}
		state.lastWarningDate = LocalDate.now(ZoneOffset.UTC);
		saveState(state);
	}

	public ActivationPayload newPayload(
		String serialNumber,
		String installationHash,
		Instant expiresAtUtc,
		String licensee,
		String features)
	{
		return new ActivationPayload(
			TOKEN_VERSION,
			PRODUCT_NAME,
			getCurrentEdition(),
			serialNumber,
			installationHash,
			Instant.now(),
			expiresAtUtc,
			UUID.randomUUID().toString(),
			licensee,
			features);
	}

	private ActivationSummary validateStoredActivation(boolean updateLastSeen) {
		StoredActivationState state;
		try {
			state = stateStore.load();
		} catch (Exception e) {
			setValidationProperty("storage-error");
			return buildSummary(ActivationStatus.STORAGE_ERROR, null, null, "Activation data could not be loaded.");
		}
		if (state == null) {
			setValidationProperty("0");
			return buildSummary(ActivationStatus.NOT_ACTIVATED, null, null, "Activation is required.");
		}

		Instant now = Instant.now();
		if ((state.lastSeenUtc != null) && now.isBefore(state.lastSeenUtc.minus(CLOCK_ROLLBACK_TOLERANCE))) {
			setValidationProperty("clock-rollback");
			return buildSummary(ActivationStatus.CLOCK_ROLLBACK, state.serialNumber, null, "System clock rollback detected. Reactivation is required.");
		}

		if (state.isDeveloperOverride()) {
			if (!developerActivationPolicy.isDeveloperModeEnabled()) {
				setValidationProperty("developer-disabled");
				return buildSummary(ActivationStatus.DEVELOPER_OVERRIDE_DISABLED, state.serialNumber, null, "Developer override is only available in developer mode.");
			}
			try {
				String developerSerialId = developerActivationPolicy.validateDeveloperSerial(state.developerSerial);
				if (updateLastSeen) {
					state.lastSeenUtc = now;
					saveState(state);
				}
				setValidationProperty("developer:" + developerSerialId);
				return buildSummary(ActivationStatus.DEVELOPER_OVERRIDE, "DEV:" + developerSerialId, null, "Developer activation override is active.");
			} catch (IllegalArgumentException e) {
				setValidationProperty("invalid-developer");
				return buildSummary(ActivationStatus.INVALID_DEVELOPER_SERIAL, state.serialNumber, null, e.getMessage());
			}
		}

		ActivationPayload payload;
		try {
			payload = ActivationVerifier.parseAndVerifyToken(state.activationToken, TOKEN_PREFIX, productionPublicKey);
		} catch (IllegalArgumentException e) {
			setValidationProperty("invalid-signature");
			return buildSummary(ActivationStatus.INVALID_SIGNATURE, state.serialNumber, null, e.getMessage());
		} catch (Exception e) {
			setValidationProperty("invalid-token");
			return buildSummary(ActivationStatus.INVALID_TOKEN, state.serialNumber, null, "Activation token could not be read.");
		}

		ActivationSummary summary = validatePayload(state.serialNumber, payload, now, true);
		if (summary.isActive() && updateLastSeen) {
			state.lastSeenUtc = now;
			saveState(state);
		}
		if (summary.isActive()) {
			setValidationProperty("active:" + payload.getExpiresAtUtc());
		} else {
			setValidationProperty(summary.getStatus().name().toLowerCase());
		}
		return summary;
	}

	private ActivationSummary validatePayload(String serialNumber, ActivationPayload payload, Instant now, boolean fromStoredState) {
		if (payload == null) {
			return buildSummary(ActivationStatus.INVALID_TOKEN, serialNumber, null, "Activation payload is missing.");
		}
		if (!TOKEN_VERSION.equals(normalize(payload.getVersion()))) {
			return buildSummary(ActivationStatus.INVALID_TOKEN, serialNumber, null, "Activation payload version is invalid.");
		}
		if (!PRODUCT_NAME.equals(normalize(payload.getProduct()))) {
			return buildSummary(ActivationStatus.PRODUCT_MISMATCH, serialNumber, null, "Activation code is for a different product.");
		}
		if (!getCurrentEdition().equalsIgnoreCase(normalize(payload.getEdition()))) {
			return buildSummary(ActivationStatus.EDITION_MISMATCH, serialNumber, null, "Activation code is for a different edition.");
		}
		if (!normalize(serialNumber).equalsIgnoreCase(normalize(payload.getSerialId()))) {
			return buildSummary(ActivationStatus.SERIAL_MISMATCH, serialNumber, null, "Serial number does not match the activation code.");
		}
		if (!getInstallationHash().equalsIgnoreCase(normalize(payload.getInstallationHash()))) {
			return buildSummary(ActivationStatus.MACHINE_MISMATCH, serialNumber, payload.getExpiresAtUtc(), "Activation code is for a different machine.");
		}
		if (payload.getExpiresAtUtc() == null) {
			return buildSummary(ActivationStatus.INVALID_TOKEN, serialNumber, null, "Activation code is missing an expiry date.");
		}
		if (!payload.getExpiresAtUtc().isAfter(now)) {
			return buildSummary(ActivationStatus.EXPIRED, serialNumber, payload.getExpiresAtUtc(), "Activation has expired. Please reactivate.");
		}
		long daysRemaining = Duration.between(now, payload.getExpiresAtUtc()).toDays();
		boolean expiringSoon = daysRemaining <= EXPIRY_WARNING_DAYS;
		String message = fromStoredState ? "Activation is valid." : "Activation successful.";
		return new ActivationSummary(
			ActivationStatus.VALID,
			serialNumber,
			getInstallationId(),
			payload.getExpiresAtUtc(),
			message,
			expiringSoon,
			developerActivationPolicy.isDeveloperModeEnabled());
	}

	private ActivationSummary buildSummary(ActivationStatus status, String serialNumber, Instant expiresAtUtc, String message) {
		boolean expiringSoon = false;
		if ((expiresAtUtc != null) && expiresAtUtc.isAfter(Instant.now())) {
			expiringSoon = Duration.between(Instant.now(), expiresAtUtc).toDays() <= EXPIRY_WARNING_DAYS;
		}
		return new ActivationSummary(
			status,
			serialNumber,
			getInstallationId(),
			expiresAtUtc,
			message,
			expiringSoon,
			developerActivationPolicy.isDeveloperModeEnabled());
	}

	private void saveState(StoredActivationState state) {
		try {
			stateStore.save(state);
		} catch (Exception e) {
			throw new IllegalStateException("Could not persist activation state", e);
		}
	}

	private static String normalize(String value) {
		if (value == null) {
			return null;
		}
		String trimmed = value.trim();
		return trimmed.length() == 0 ? null : trimmed;
	}

	private static String getCurrentEdition() {
		return Environment.getStandAlone() ? Settings.VERSION_TYPE_STANDALONE : Settings.VERSION_TYPE_SERVER;
	}

	private static void setValidationProperty(String value) {
		System.setProperty("projectlibre.validation", value == null ? "0" : value);
	}

	private static PublicKey loadPublicKey(String resourcePath) throws Exception {
		InputStream stream = ActivationService.class.getResourceAsStream(resourcePath);
		if (stream == null) {
			throw new IllegalStateException("Missing activation public key resource");
		}
		String pem = new String(stream.readAllBytes(), StandardCharsets.US_ASCII);
		String base64 = pem
			.replace("-----BEGIN PUBLIC KEY-----", "")
			.replace("-----END PUBLIC KEY-----", "")
			.replaceAll("\\s+", "");
		byte[] encoded = Base64.getDecoder().decode(base64);
		return KeyFactory.getInstance("Ed25519").generatePublic(new X509EncodedKeySpec(encoded));
	}
}
