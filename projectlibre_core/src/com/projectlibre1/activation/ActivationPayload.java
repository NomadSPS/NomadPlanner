package com.projectlibre1.activation;

import java.time.Instant;
import java.util.LinkedHashMap;

public final class ActivationPayload {
	private final String version;
	private final String product;
	private final String edition;
	private final String serialId;
	private final String installationHash;
	private final Instant issuedAtUtc;
	private final Instant expiresAtUtc;
	private final String nonce;
	private final String licensee;
	private final String features;

	public ActivationPayload(
		String version,
		String product,
		String edition,
		String serialId,
		String installationHash,
		Instant issuedAtUtc,
		Instant expiresAtUtc,
		String nonce,
		String licensee,
		String features)
	{
		this.version = version;
		this.product = product;
		this.edition = edition;
		this.serialId = serialId;
		this.installationHash = installationHash;
		this.issuedAtUtc = issuedAtUtc;
		this.expiresAtUtc = expiresAtUtc;
		this.nonce = nonce;
		this.licensee = licensee;
		this.features = features;
	}

	public String getVersion() {
		return version;
	}

	public String getProduct() {
		return product;
	}

	public String getEdition() {
		return edition;
	}

	public String getSerialId() {
		return serialId;
	}

	public String getInstallationHash() {
		return installationHash;
	}

	public Instant getIssuedAtUtc() {
		return issuedAtUtc;
	}

	public Instant getExpiresAtUtc() {
		return expiresAtUtc;
	}

	public String getNonce() {
		return nonce;
	}

	public String getLicensee() {
		return licensee;
	}

	public String getFeatures() {
		return features;
	}

	public String toJson() {
		LinkedHashMap<String, String> values = new LinkedHashMap<String, String>();
		values.put("version", version);
		values.put("product", product);
		values.put("edition", edition);
		values.put("serialId", serialId);
		values.put("installationHash", installationHash);
		values.put("issuedAtUtc", issuedAtUtc == null ? null : issuedAtUtc.toString());
		values.put("expiresAtUtc", expiresAtUtc == null ? null : expiresAtUtc.toString());
		values.put("nonce", nonce);
		values.put("licensee", licensee);
		values.put("features", features);
		return ActivationJson.toJsonObject(values);
	}

	public static ActivationPayload fromJson(String json) {
		return new ActivationPayload(
			ActivationJson.getString(json, "version"),
			ActivationJson.getString(json, "product"),
			ActivationJson.getString(json, "edition"),
			ActivationJson.getString(json, "serialId"),
			ActivationJson.getString(json, "installationHash"),
			parseInstant(ActivationJson.getString(json, "issuedAtUtc")),
			parseInstant(ActivationJson.getString(json, "expiresAtUtc")),
			ActivationJson.getString(json, "nonce"),
			ActivationJson.getString(json, "licensee"),
			ActivationJson.getString(json, "features"));
	}

	private static Instant parseInstant(String value) {
		if ((value == null) || (value.trim().length() == 0)) {
			return null;
		}
		return Instant.parse(value);
	}
}
