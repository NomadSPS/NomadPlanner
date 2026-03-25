package com.projectlibre1.dialog;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.projectlibre1.activation.ActivationSummary;
import com.projectlibre1.activation.ActivationStatus;
import com.projectlibre1.strings.Messages;

public final class ActivationUiSupport {
	private static final DateTimeFormatter EXPIRY_FORMAT = DateTimeFormatter.ofPattern("dd MMM uuuu HH:mm z")
		.withZone(ZoneId.systemDefault());

	private ActivationUiSupport() {
	}

	public static String formatSummary(ActivationSummary summary) {
		if (summary == null) {
			return Messages.getString("ActivationDialog.NotActivated");
		}
		if (summary.isDeveloperOverride()) {
			return Messages.getStringWithParam("ActivationDialog.DeveloperOverrideStatus", summary.getSerialNumber());
		}
		if (summary.getStatus() == ActivationStatus.VALID) {
			return Messages.getStringWithParam("ActivationDialog.ActiveUntil", new Object[] { EXPIRY_FORMAT.format(summary.getExpiresAtUtc()) });
		}
		return Messages.getString("ActivationDialog.NotActivated");
	}

	public static String formatExpiryWarning(ActivationSummary summary) {
		if ((summary == null) || (summary.getExpiresAtUtc() == null)) {
			return Messages.getString("ActivationDialog.NotActivated");
		}
		return Messages.getStringWithParam("ActivationDialog.ExpiringSoonWarning", new Object[] { EXPIRY_FORMAT.format(summary.getExpiresAtUtc()) });
	}
}
