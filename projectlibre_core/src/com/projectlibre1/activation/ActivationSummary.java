package com.projectlibre1.activation;

import java.time.Instant;

public final class ActivationSummary {
	private final ActivationStatus status;
	private final String serialNumber;
	private final String installationId;
	private final Instant expiresAtUtc;
	private final String message;
	private final boolean expiringSoon;
	private final boolean developerModeEnabled;

	public ActivationSummary(
		ActivationStatus status,
		String serialNumber,
		String installationId,
		Instant expiresAtUtc,
		String message,
		boolean expiringSoon,
		boolean developerModeEnabled)
	{
		this.status = status;
		this.serialNumber = serialNumber;
		this.installationId = installationId;
		this.expiresAtUtc = expiresAtUtc;
		this.message = message;
		this.expiringSoon = expiringSoon;
		this.developerModeEnabled = developerModeEnabled;
	}

	public ActivationStatus getStatus() {
		return status;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getInstallationId() {
		return installationId;
	}

	public Instant getExpiresAtUtc() {
		return expiresAtUtc;
	}

	public String getMessage() {
		return message;
	}

	public boolean isExpiringSoon() {
		return expiringSoon;
	}

	public boolean isDeveloperModeEnabled() {
		return developerModeEnabled;
	}

	public boolean isActive() {
		return status.isActive();
	}

	public boolean isDeveloperOverride() {
		return status == ActivationStatus.DEVELOPER_OVERRIDE;
	}
}
