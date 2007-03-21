package pt.utl.ist.fenix.tools.util;

import sun.text.Normalizer;

public class StringNormalizer {

    	private static String normalize(String string, boolean toLowerCase) {
    	    String returnValue = Normalizer.normalize(string, Normalizer.DECOMP, Normalizer.DONE).replaceAll("[^\\p{ASCII}]",
		"");
    	    return (toLowerCase) ? returnValue.toLowerCase() : returnValue;
    	    
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
}
