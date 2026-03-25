package com.projectlibre1.activation;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public final class MachineFingerprintService {
	private static final MachineFingerprintService INSTANCE = new MachineFingerprintService();

	private String installationHash;
	private String installationId;

	public static MachineFingerprintService getInstance() {
		return INSTANCE;
	}

	private MachineFingerprintService() {
	}

	public synchronized String getInstallationHash() {
		if (installationHash == null) {
			installationHash = computeInstallationHash();
		}
		return installationHash;
	}

	public synchronized String getInstallationId() {
		if (installationId == null) {
			installationId = formatInstallationId(getInstallationHash());
		}
		return installationId;
	}

	private String computeInstallationHash() {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			digest.update(buildNormalizedFingerprint().getBytes("UTF-8"));
			return toHex(digest.digest());
		} catch (Exception e) {
			throw new IllegalStateException("Could not compute installation fingerprint", e);
		}
	}

	private String buildNormalizedFingerprint() {
		List<String> macs = new ArrayList<String>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while ((interfaces != null) && interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = interfaces.nextElement();
				try {
					if (!networkInterface.isUp() || networkInterface.isLoopback()) {
						continue;
					}
				} catch (Exception e) {
					continue;
				}
				byte[] hardwareAddress = networkInterface.getHardwareAddress();
				if ((hardwareAddress == null) || (hardwareAddress.length == 0)) {
					continue;
				}
				StringBuilder builder = new StringBuilder();
				for (byte b : hardwareAddress) {
					builder.append(String.format("%02X", b));
				}
				macs.add(builder.toString());
			}
		} catch (Exception ignored) {
		}
		Collections.sort(macs);
		StringBuilder fingerprint = new StringBuilder();
		fingerprint.append("os.name=").append(System.getProperty("os.name", "")).append('\n');
		fingerprint.append("os.arch=").append(System.getProperty("os.arch", "")).append('\n');
		if (macs.isEmpty()) {
			try {
				fingerprint.append("host=").append(InetAddress.getLocalHost().getHostName());
			} catch (Exception e) {
				fingerprint.append("host=unknown");
			}
		} else {
			for (String mac : macs) {
				fingerprint.append("mac=").append(mac).append('\n');
			}
		}
		return fingerprint.toString();
	}

	private static String formatInstallationId(String hash) {
		StringBuilder builder = new StringBuilder(hash.length() + (hash.length() / 4));
		for (int i = 0; i < hash.length(); i++) {
			if ((i > 0) && ((i % 4) == 0)) {
				builder.append('-');
			}
			builder.append(hash.charAt(i));
		}
		return builder.toString();
	}

	private static String toHex(byte[] value) {
		StringBuilder builder = new StringBuilder(value.length * 2);
		for (byte b : value) {
			builder.append(String.format("%02X", b));
		}
		return builder.toString();
	}
}
