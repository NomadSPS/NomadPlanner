package com.projectlibre1.activation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class ActivationJson {
	private ActivationJson() {
	}

	static String toJsonObject(LinkedHashMap<String, String> values) {
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

	static String getString(String json, String key) {
		Pattern pattern = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(null|\"((?:\\\\.|[^\\\\\"])*)\")");
		Matcher matcher = pattern.matcher(json);
		if (!matcher.find()) {
			return null;
		}
		if ("null".equals(matcher.group(1))) {
			return null;
		}
		return unescape(matcher.group(2));
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

	private static String unescape(String value) {
		StringBuilder builder = new StringBuilder(value.length());
		boolean escaped = false;
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (escaped) {
				switch (ch) {
					case 'n':
						builder.append('\n');
						break;
					case 'r':
						builder.append('\r');
						break;
					case 't':
						builder.append('\t');
						break;
					case '\\':
					case '"':
						builder.append(ch);
						break;
					default:
						builder.append(ch);
				}
				escaped = false;
			} else if (ch == '\\') {
				escaped = true;
			} else {
				builder.append(ch);
			}
		}
		if (escaped) {
			builder.append('\\');
		}
		return builder.toString();
	}
}
