package com.projectlibre1.activation;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

final class ActivationVerifier {
	private ActivationVerifier() {
	}

	static ActivationPayload parseAndVerifyToken(String token, String prefix, PublicKey publicKey) throws Exception {
		String[] parts = token.split("\\.");
		if ((parts.length != 3) || !prefix.equals(parts[0])) {
			throw new IllegalArgumentException("Invalid token format");
		}
		byte[] payloadBytes = Base64.getUrlDecoder().decode(parts[1]);
		byte[] signatureBytes = Base64.getUrlDecoder().decode(parts[2]);

		Signature verifier = Signature.getInstance("Ed25519");
		verifier.initVerify(publicKey);
		verifier.update(parts[0].getBytes(StandardCharsets.US_ASCII));
		verifier.update((byte) '.');
		verifier.update(parts[1].getBytes(StandardCharsets.US_ASCII));
		if (!verifier.verify(signatureBytes)) {
			throw new IllegalArgumentException("Invalid signature");
		}
		return ActivationPayload.fromJson(new String(payloadBytes, StandardCharsets.UTF_8));
	}
}
