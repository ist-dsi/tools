package pt.utl.ist.fenix.tools.util;

import java.text.Normalizer;
import java.text.Normalizer.Form;

public class StringNormalizer {

	private static String normalize(String string, boolean toLowerCase) {
		final String normalized = Normalizer.normalize(string, Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		return (toLowerCase) ? normalized.toLowerCase() : normalized;
	}

	public static String normalize(String string) {
		return normalize(string, true);
	}

	public static String normalizePreservingCapitalizedLetters(String string) {
		return normalize(string, false);
	}

	public static void normalize(String[] words) {
		for (int i = 0; i < words.length; i++) {
			words[i] = normalize(words[i]);
		}
	}

	public static String normalizeAndRemoveMinorChars(String string) {
		final String normalizedString = normalize(string);
		final StringBuilder stringBuilder = new StringBuilder();
		for (final char c : normalizedString.toCharArray()) {
			final int i = c;
			if (i >= 32 || i == 10 || i == 13) {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

	public static String normalizeAndRemoveNonAlphaNumeric(String string) {
		return normalizeAndReplaceNonAlphaNumeric(string, "");
	}

	public static String normalizeAndReplaceNonAlphaNumeric(String string, String replacement) {
		string = normalize(string);
		string = string.replaceAll("[^0-9a-zA-Z]", replacement);
		return string.trim();
	}
}
