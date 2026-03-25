package com.projectlibre1.activation;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashMap;

import com.projectlibre1.preference.ConfigurationFile;

public final class ActivationStateStore {
	private static final String ACTIVATION_DIR_NAME = "activation";
	private static final String ACTIVATION_FILE_NAME = "activation.json";

	public StoredActivationState load() {
		try {
			File stateFile = getStateFile();
			if (!stateFile.isFile()) {
				return null;
			}
			String json = Files.readString(stateFile.toPath(), StandardCharsets.UTF_8);
			StoredActivationState state = new StoredActivationState();
			state.mode = ActivationJson.getString(json, "mode");
			state.serialNumber = ActivationJson.getString(json, "serialNumber");
			state.activationToken = ActivationJson.getString(json, "activationToken");
			state.developerSerial = ActivationJson.getString(json, "developerSerial");
			state.lastSeenUtc = parseInstant(ActivationJson.getString(json, "lastSeenUtc"));
			state.lastWarningDate = parseDate(ActivationJson.getString(json, "lastWarningDate"));
			state.activatedAtUtc = parseInstant(ActivationJson.getString(json, "activatedAtUtc"));
			return state;
		} catch (Exception e) {
			throw new IllegalStateException("Could not load activation state", e);
		}
	}

	public void save(StoredActivationState state) {
		try {
			File stateFile = getStateFile();
			File parent = stateFile.getParentFile();
			if ((parent != null) && !parent.isDirectory()) {
				parent.mkdirs();
			}
			LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
			values.put("schemaVersion", "1");
			values.put("mode", state.mode);
			values.put("serialNumber", state.serialNumber);
			values.put("activationToken", state.activationToken);
			values.put("developerSerial", state.developerSerial);
			values.put("lastSeenUtc", formatInstant(state.lastSeenUtc));
			values.put("lastWarningDate", formatDate(state.lastWarningDate));
			values.put("activatedAtUtc", formatInstant(state.activatedAtUtc));
			Files.writeString(stateFile.toPath(), ActivationJson.toJsonObject(values), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new IllegalStateException("Could not save activation state", e);
		}
	}

	public File getStateFile() {
		File confDir = ConfigurationFile.getConfDir();
		if (confDir == null) {
			String home = System.getProperty("user.home", ".");
			confDir = new File(home, ".projectlibre");
		}
		return new File(new File(confDir, ACTIVATION_DIR_NAME), ACTIVATION_FILE_NAME);
	}

	private static Instant parseInstant(String value) {
		if ((value == null) || (value.length() == 0)) {
			return null;
		}
		return Instant.parse(value);
	}

	private static LocalDate parseDate(String value) {
		if ((value == null) || (value.length() == 0)) {
			return null;
		}
		return LocalDate.parse(value);
	}

	private static String formatInstant(Instant value) {
		return value == null ? null : value.toString();
	}

	private static String formatDate(LocalDate value) {
		return value == null ? null : value.toString();
	}

	public static final class StoredActivationState {
		private static final String MODE_TOKEN = "TOKEN";
		private static final String MODE_DEVELOPER_OVERRIDE = "DEVELOPER_OVERRIDE";

		String mode;
		String serialNumber;
		String activationToken;
		String developerSerial;
		Instant lastSeenUtc;
		LocalDate lastWarningDate;
		Instant activatedAtUtc;

		public static StoredActivationState token(String serialNumber, String activationToken, Instant now) {
			StoredActivationState state = new StoredActivationState();
			state.mode = MODE_TOKEN;
			state.serialNumber = serialNumber;
			state.activationToken = activationToken;
			state.activatedAtUtc = now;
			state.lastSeenUtc = now;
			return state;
		}

		public static StoredActivationState developerOverride(String serialNumber, String developerSerial, Instant now) {
			StoredActivationState state = new StoredActivationState();
			state.mode = MODE_DEVELOPER_OVERRIDE;
			state.serialNumber = serialNumber;
			state.developerSerial = developerSerial;
			state.activatedAtUtc = now;
			state.lastSeenUtc = now;
			return state;
		}

		public boolean isDeveloperOverride() {
			return MODE_DEVELOPER_OVERRIDE.equals(mode);
		}
	}
}
