package com.projectlibre1.activation;

public enum ActivationStatus {
	VALID,
	DEVELOPER_OVERRIDE,
	NOT_ACTIVATED,
	INVALID_TOKEN,
	INVALID_SIGNATURE,
	SERIAL_MISMATCH,
	PRODUCT_MISMATCH,
	EDITION_MISMATCH,
	MACHINE_MISMATCH,
	EXPIRED,
	CLOCK_ROLLBACK,
	DEVELOPER_OVERRIDE_DISABLED,
	INVALID_DEVELOPER_SERIAL,
	STORAGE_ERROR;

	public boolean isActive() {
		return (this == VALID) || (this == DEVELOPER_OVERRIDE);
	}

	public boolean blocksStartup() {
		return !isActive();
	}
}
