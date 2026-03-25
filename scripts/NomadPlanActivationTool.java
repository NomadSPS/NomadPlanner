import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public final class NomadPlanActivationTool {
	private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			printUsage();
			System.exit(1);
		}

		String command = args[0];
		Map<String, String> options = parseOptions(args, 1);
		if ("generate-token".equals(command)) {
			System.out.println(generateToken(options));
			return;
		}
		if ("generate-dev-serial".equals(command)) {
			System.out.println(generateDeveloperSerial(options));
			return;
		}
		printUsage();
		System.exit(1);
	}

	private static String generateToken(Map<String, String> options) throws Exception {
		String privateKeyPath = required(options, "--private-key");
		String serial = required(options, "--serial");
		String installationHash = normalizeInstallationHash(requiredOneOf(options, "--installation-hash", "--installation-id"));
		Instant expiresAtUtc = Instant.parse(required(options, "--expires"));
		String edition = option(options, "--edition", "standalone");
		String licensee = options.get("--licensee");
		String features = options.get("--features");

		LinkedHashMap<String, String> payload = new LinkedHashMap<String, String>();
		payload.put("version", "1");
		payload.put("product", "NomadPlan");
		payload.put("edition", edition);
		payload.put("serialId", serial);
		payload.put("installationHash", installationHash);
		payload.put("issuedAtUtc", Instant.now().toString());
		payload.put("expiresAtUtc", expiresAtUtc.toString());
		payload.put("nonce", UUID.randomUUID().toString());
		payload.put("licensee", licensee);
		payload.put("features", features);

		return signPayload("NMP1", payload, loadPrivateKey(Path.of(privateKeyPath)));
	}

	private static String generateDeveloperSerial(Map<String, String> options) throws Exception {
		String privateKeyPath = required(options, "--private-key");
		String serialId = requiredOneOf(options, "--serial-id", "--serial");
		String prefix = "DEV1";
		PrivateKey privateKey = loadPrivateKey(Path.of(privateKeyPath));

		Signature signature = Signature.getInstance("Ed25519");
		signature.initSign(privateKey);
		signature.update(prefix.getBytes(StandardCharsets.US_ASCII));
		signature.update((byte) '.');
		signature.update(serialId.getBytes(StandardCharsets.US_ASCII));

		return prefix + "." + serialId + "." + URL_ENCODER.encodeToString(signature.sign());
	}

	private static String signPayload(String prefix, LinkedHashMap<String, String> payload, PrivateKey privateKey) throws Exception {
		String payloadJson = toJsonObject(payload);
		String payloadBase64 = URL_ENCODER.encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

		Signature signature = Signature.getInstance("Ed25519");
		signature.initSign(privateKey);
		signature.update(prefix.getBytes(StandardCharsets.US_ASCII));
		signature.update((byte) '.');
		signature.update(payloadBase64.getBytes(StandardCharsets.US_ASCII));

		return prefix + "." + payloadBase64 + "." + URL_ENCODER.encodeToString(signature.sign());
	}

	private static PrivateKey loadPrivateKey(Path path) throws Exception {
		String pem = Files.readString(path, StandardCharsets.US_ASCII);
		String base64 = pem
			.replace("-----BEGIN PRIVATE KEY-----", "")
			.replace("-----END PRIVATE KEY-----", "")
			.replaceAll("\\s+", "");
		byte[] encoded = Base64.getDecoder().decode(base64);
		return KeyFactory.getInstance("Ed25519").generatePrivate(new PKCS8EncodedKeySpec(encoded));
	}

	private static String normalizeInstallationHash(String value) {
		return value.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
	}

	private static Map<String, String> parseOptions(String[] args, int startIndex) {
		LinkedHashMap<String, String> options = new LinkedHashMap<String, String>();
		for (int i = startIndex; i < args.length; i++) {
			String key = args[i];
			if (!key.startsWith("--")) {
				throw new IllegalArgumentException("Unexpected argument: " + key);
			}
			if ((i + 1) >= args.length) {
				throw new IllegalArgumentException("Missing value for " + key);
			}
			options.put(key, args[++i]);
		}
		return options;
	}

	private static String required(Map<String, String> options, String key) {
		String value = options.get(key);
		if ((value == null) || (value.trim().length() == 0)) {
			throw new IllegalArgumentException("Missing required option " + key);
		}
		return value.trim();
	}

	private static String requiredOneOf(Map<String, String> options, String primaryKey, String alternateKey) {
		String value = options.get(primaryKey);
		if ((value != null) && (value.trim().length() > 0)) {
			return value.trim();
		}
		return required(options, alternateKey);
	}

	private static String option(Map<String, String> options, String key, String defaultValue) {
		String value = options.get(key);
		return ((value == null) || (value.trim().length() == 0)) ? defaultValue : value.trim();
	}

	private static String toJsonObject(LinkedHashMap<String, String> values) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		boolean first = true;
		for (Map.Entry<String, String> entry : values.entrySet()) {
			if (!first) {
				builder.append(",\n");
			}
			first = false;
			builder.append("  \"")
				.append(escape(entry.getKey()))
				.append("\": ");
			if (entry.getValue() == null) {
				builder.append("null");
			} else {
				builder.append('"')
					.append(escape(entry.getValue()))
					.append('"');
			}
		}
		builder.append("\n}\n");
		return builder.toString();
	}

	private static String escape(String value) {
		StringBuilder builder = new StringBuilder(value.length() + 8);
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			switch (ch) {
				case '\\':
					builder.append("\\\\");
					break;
				case '"':
					builder.append("\\\"");
					break;
				case '\n':
					builder.append("\\n");
					break;
				case '\r':
					builder.append("\\r");
					break;
				case '\t':
					builder.append("\\t");
					break;
				default:
					builder.append(ch);
			}
		}
		return builder.toString();
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("  java scripts/NomadPlanActivationTool.java generate-token --private-key <pem> --serial <serial> --installation-id <id>|--installation-hash <hash> --expires <utc-iso> [--edition standalone|server] [--licensee <name>] [--features <csv>]");
		System.out.println("  java scripts/NomadPlanActivationTool.java generate-dev-serial --private-key <pem> --serial-id <id>");
	}
}
