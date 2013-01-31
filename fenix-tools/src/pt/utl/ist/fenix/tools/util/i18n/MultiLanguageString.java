package pt.utl.ist.fenix.tools.util.i18n;

import java.io.Serializable;
import java.text.Collator;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class MultiLanguageString implements Serializable, Comparable<MultiLanguageString> {

	private final Map<Language, String> contentsMap;

	public MultiLanguageString() {
		this.contentsMap = new HashMap<Language, String>();
	}

	public MultiLanguageString(final String content) {
		final Language userLanguage = Language.getUserLanguage();
		if (userLanguage == null) {
			throw new IllegalArgumentException("no user language is set");
		}
		Map<Language, String> contents = new HashMap<Language, String>();
		contents.put(userLanguage, content == null ? StringUtils.EMPTY : content);
		this.contentsMap = contents;
	}

	public MultiLanguageString(final Language language, final String content) {
		if (language == null) {
			throw new IllegalArgumentException("language cannot be null");
		}
		Map<Language, String> contents = new HashMap<Language, String>();
		contents.put(language, content == null ? StringUtils.EMPTY : content);
		this.contentsMap = contents;
	}

	private MultiLanguageString(Map<Language, String> contentsMap) {
		this.contentsMap = contentsMap;
	}

	/**
	 * 
	 * @param language
	 *            the language of the content
	 * @param content
	 *            the String with the content in the specified language
	 * @return a <b>new</b> {@link MultiLanguageString} with the given content
	 *         in the given language added to the already existing content NOTE:
	 *         it does not change the content of this instance
	 */
	public MultiLanguageString with(final Language language, final String content) {
		if (language == null) {
			throw new IllegalArgumentException("language cannot be null");
		}
		Map<Language, String> contents = new HashMap<Language, String>();
		contents.putAll(contentsMap);
		contents.put(language, content == null ? StringUtils.EMPTY : content);
		return new MultiLanguageString(contents);
	}

	/**
	 * @see MultiLanguageString#with(Language, String)
	 * @param content
	 * @return
	 */
	public MultiLanguageString withDefault(final String content) {
		final Language userLanguage = Language.getUserLanguage();
		if (userLanguage == null) {
			throw new IllegalArgumentException("no user language is set");
		}
		return with(userLanguage, content);
	}

	public MultiLanguageString without(Language language) {
		if (language != null) {
			Map<Language, String> contents = new HashMap<Language, String>();
			contents.putAll(contentsMap);
			contents.remove(language);
			return new MultiLanguageString(contents);
		}
		return this;
	}

	public Collection<String> getAllContents() {
		return contentsMap.values();
	}

	public Collection<Language> getAllLanguages() {
		return contentsMap.keySet();
	}

	public boolean isRequestedLanguage() {
		Language userLanguage = Language.getUserLanguage();
		return userLanguage != null && userLanguage.equals(getContentLanguage());
	}

	public Language getContentLanguage() {
		Language userLanguage = Language.getUserLanguage();
		if (userLanguage != null && hasLanguage(userLanguage)) {
			return userLanguage;
		}

		Language systemLanguage = Language.getDefaultLanguage();
		if (systemLanguage != null && hasLanguage(systemLanguage)) {
			return systemLanguage;
		}

		return contentsMap.isEmpty() ? null : contentsMap.keySet().iterator().next();
	}

	/**
	 * @deprecated use {@link MultiLanguageString#withDefault(String)} instead
	 */
	@Deprecated
	public void setContent(String text) {
		final Language userLanguage = Language.getUserLanguage();
		if (userLanguage != null) {
			setContent(userLanguage, text);
		}
		final Language systemLanguage = Language.getDefaultLanguage();
		if (userLanguage != systemLanguage && !hasLanguage(systemLanguage)) {
			setContent(systemLanguage, text);
		}
	}

	/**
	 * @deprecated use {@link #with(Language, String)}
	 */
	@Deprecated
	public void setContent(Language language, String content) {
		if (language == null) {
			throw new IllegalArgumentException("language cannot be null");
		}
		contentsMap.put(language, content == null ? "" : content);
	}

	public String getContent() {
		return getContent(getContentLanguage());
	}

	public String getContent(Language language) {
		return contentsMap.get(language);
	}

	public String getPreferedContent() {
		return hasLanguage(Language.getDefaultLanguage()) ? getContent(Language.getDefaultLanguage()) : getContent();
	}

	/**
	 * @deprecated use {@link #without(Language)}
	 */
	@Deprecated
	public String removeContent(Language language) {
		return contentsMap.remove(language);
	}

	public String toUpperCase() {
		return hasContent() ? getContent().toUpperCase() : null;
	}

	public boolean hasContent() {
		// return getContent() != null;
		return !isEmpty();
	}

	public boolean hasContent(Language language) {
		return !StringUtils.isEmpty(getContent(language));
	}

	public boolean hasLanguage(Language language) {
		return contentsMap.containsKey(language);
	}

	public String exportAsString() {
		final StringBuilder result = new StringBuilder();
		for (final Entry<Language, String> entry : contentsMap.entrySet()) {
			final Language key = entry.getKey();
			final String value = entry.getValue();
			result.append(key);
			result.append(value.length());
			result.append(':');
			result.append(value);
		}
		return result.toString();
	}

	public MultiLanguageString append(MultiLanguageString string) {
		Map<Language, String> contents = new HashMap<Language, String>();
		Set<Language> allLanguages = new HashSet<Language>();
		allLanguages.addAll(string.getAllLanguages());
		allLanguages.addAll(getAllLanguages());
		for (Language language : allLanguages) {
			contents.put(language,
					StringUtils.defaultString(getContent(language)) + StringUtils.defaultString(string.getContent(language)));
		}
		return new MultiLanguageString(contents);
	}

	public MultiLanguageString append(String string) {
		Map<Language, String> contents = new HashMap<Language, String>();
		for (Language language : getAllLanguages()) {
			contents.put(language, StringUtils.defaultString(getContent(language)) + StringUtils.defaultString(string));
		}
		return new MultiLanguageString(contents);
	}

	/**
	 * @return true if this multi language string contains no languages
	 */
	public boolean isEmpty() {
		// return this.getAllLanguages().isEmpty();
		return contentsMap.isEmpty();
	}

	public static MultiLanguageString importFromString(String string) {
		if (string == null) {
			return null;
		}

		Map<Language, String> contents = new HashMap<Language, String>();
		String nullContent = StringUtils.EMPTY;

		for (int i = 0; i < string.length();) {

			int length = 0;
			int collonPosition = string.indexOf(':', i + 2);

			if (!StringUtils.isNumeric(string.substring(i + 2, collonPosition))) {
				length = Integer.parseInt(string.substring(i + 4, collonPosition));
				nullContent = string.substring(collonPosition + 1, collonPosition + 1 + length);

			} else {
				length = Integer.parseInt(string.substring(i + 2, collonPosition));
				String language = string.substring(i, i + 2);
				String content = string.substring(collonPosition + 1, collonPosition + 1 + length);
				contents.put(Language.valueOf(language), content);
			}

			i = collonPosition + 1 + length;
		}

		// HACK: MultiLanguageString should not allow null values as language
		if (contents.isEmpty()) {
			contents.put(Language.getDefaultLanguage(), nullContent);
		}

		return new MultiLanguageString(contents);
	}

	@Override
	public String toString() {
		final String content = getContent();
		return content == null ? StringUtils.EMPTY : content;
	}

	@Override
	public int compareTo(MultiLanguageString languageString) {
		if (!hasContent() && !languageString.hasContent()) {
			return 0;
		}

		if (!hasContent() && languageString.hasContent()) {
			return -1;
		}

		if (hasContent() && !languageString.hasContent()) {
			return 1;
		}

		return Collator.getInstance().compare(getContent(), languageString.getContent());
	}

	public boolean equalInAnyLanguage(Object obj) {
		if (obj instanceof MultiLanguageString) {
			MultiLanguageString multiLanguageString = (MultiLanguageString) obj;
			Set<Language> languages = new HashSet<Language>();
			languages.addAll(this.getAllLanguages());
			languages.addAll(multiLanguageString.getAllLanguages());
			for (Language language : languages) {
				if (this.getContent(language) != null
						&& this.getContent(language).equalsIgnoreCase(multiLanguageString.getContent(language))) {
					return true;
				}
			}
		} else if (obj instanceof String) {
			for (final String string : getAllContents()) {
				if (string.equals(obj)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MultiLanguageString) {
			MultiLanguageString multiLanguageString = (MultiLanguageString) obj;
			if (this.getAllContents().size() != multiLanguageString.getAllContents().size()) {
				return false;
			}
			for (Language language : this.getAllLanguages()) {
				if (!getContent(language).equalsIgnoreCase(multiLanguageString.getContent(language))) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int sum = 0;
		for (String content : getAllContents()) {
			sum += content.hashCode();
		}
		return sum;
	}
}
