package pt.utl.ist.fenix.tools.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class StringNormalizer {

	public static Method normalizeMethod = null;

	public static Object[] allArgsFilledToFillIn = null;

	static {
		Class normalizerClazz = null;
		try {
			// sun.text.Normalizer.normalize(string, sun.text.Normalizer.DECOMP,
			// sun.text.Normalizer.DONE);
			normalizerClazz = Class.forName("sun.text.Normalizer");
			normalizeMethod = normalizerClazz.getMethod("normalize",
					new Class[] { String.class,
							Class.forName("sun.text.Normalizer$Mode"),
							int.class });
			allArgsFilledToFillIn = new Object[] { null,
					normalizerClazz.getField("DECOMP").get(null),
					normalizerClazz.getField("DONE").get(null) };
		} catch (Exception ignored) {
			try {
				// java.text.Normalizer
				// normalizer=java.text.Normalizer.normalize(CharSequence,
				// java.text.Normalizer.Form.NFD);
				normalizerClazz = Class.forName("java.text.Normalizer");
				normalizeMethod = normalizerClazz.getMethod("normalize",
						new Class[] { CharSequence.class,
								Class.forName("java.text.Normalizer$Form") });
				allArgsFilledToFillIn = new Object[] {
						null,
						Class.forName("java.text.Normalizer$Form").getField(
								"NFD").get(null) };
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private static String normalize(String string, boolean toLowerCase) {
		Object[] objectArgs = new Object[allArgsFilledToFillIn.length];
		System.arraycopy(allArgsFilledToFillIn, 0, objectArgs, 0,
				allArgsFilledToFillIn.length);
		objectArgs[0] = string;

		String returnValue;
		try {
			returnValue = normalizeMethod.invoke(null, objectArgs).toString()
					.replaceAll("[^\\p{ASCII}]", "");
			return (toLowerCase) ? returnValue.toLowerCase() : returnValue;
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
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
	
	public static void main(String[] args) {
		String stringToNormalize="·¡È…Á« Áı∫™ Í ‚¬";
		String expectedNormalize="aaeecc co eeaa";
		System.out.println("Normalizing string "+stringToNormalize);
		String normalized=StringNormalizer.normalize(stringToNormalize);
		System.out.println("Normalized  string "+normalized);
		System.out.println("Is this the expected value ? "+expectedNormalize.equals(normalized));
	}
}
