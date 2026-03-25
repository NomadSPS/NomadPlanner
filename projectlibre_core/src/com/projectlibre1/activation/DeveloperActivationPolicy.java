package com.projectlibre1.activation;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public final class DeveloperActivationPolicy {
	public static final String DEVELOPER_MODE_PROPERTY = "nomadplan.devActivation";
	private static final String DEVELOPER_PREFIX = "DEV1";
	private static final String PUBLIC_KEY_RESOURCE = "/com/projectlibre1/activation/keys/developer-public.pem";

	private final PublicKey developerPublicKey;

	public DeveloperActivationPolicy() {
		try {
			this.developerPublicKey = loadPublicKey();
		} catch (Exception e) {
			throw new IllegalStateException("Could not load developer activation public key", e);
		}
	}

	public boolean isDeveloperModeEnabled() {
		return Boolean.parseBoolean(System.getProperty(DEVELOPER_MODE_PROPERTY, "false"));
	}

	public String validateDeveloperSerial(String serial) {
		if ((serial == null) || (serial.trim().length() == 0)) {
			throw new IllegalArgumentException("Developer serial is required");
		}
		String[] parts = serial.trim().split("\\.");
		if ((parts.length != 3) || !DEVELOPER_PREFIX.equals(parts[0])) {
			throw new IllegalArgumentException("Developer serial format is invalid");
		}
		try {
			byte[] signatureBytes = Base64.getUrlDecoder().decode(parts[2]);
			Signature verifier = Signature.getInstance("Ed25519");
			verifier.initVerify(developerPublicKey);
			verifier.update(parts[0].getBytes(StandardCharsets.US_ASCII));
			verifier.update((byte) '.');
			verifier.update(parts[1].getBytes(StandardCharsets.US_ASCII));
			if (!verifier.verify(signatureBytes)) {
				throw new IllegalArgumentException("Developer serial signature is invalid");
			}
			return parts[1];
		} catch (IllegalArgumentException e) {
			throw e;
		} catch (Exception e) {
			throw new IllegalArgumentException("Developer serial could not be verified", e);
		}
	}

	private static PublicKey loadPublicKey() throws Exception {
		InputStream stream = DeveloperActivationPolicy.class.getResourceAsStream(PUBLIC_KEY_RESOURCE);
		if (stream == null) {
			throw new IllegalStateException("Missing developer activation public key resource");
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
