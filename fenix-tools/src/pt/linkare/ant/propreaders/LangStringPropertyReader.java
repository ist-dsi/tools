package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class LangStringPropertyReader extends AbstractPropertyReader {

	public LangStringPropertyReader() {
		super();
	}

	@Override
	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {

		return getLangString();
	}

	public String getLangString() throws InvalidPropertySpecException, UnsupportedEncodingException {
		MenuMessage menuOptionsLang = new MenuMessage();
		menuOptionsLang.setMessage(buildDefaultMessage(false));
		List<String>[] optionsAndValuesLang = null;
		if (getProperty().getMetaData("langs") != null) {
			optionsAndValuesLang = buildLangOptions(getProperty().getMetaData("langs"));
		} else {
			optionsAndValuesLang = buildLangOptions();
		}
		menuOptionsLang.setOptions(optionsAndValuesLang[0]);
		menuOptionsLang.setOptionValues(optionsAndValuesLang[1]);
		if (getProperty().getPropertyDefaultValue() != null) {
			return getInput().readMenuOptionOrDefault(menuOptionsLang, getProperty().getPropertyDefaultValue());
		} else {
			return getInput().readMenuOption(menuOptionsLang);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String>[] buildLangOptions(String metaData) throws InvalidPropertySpecException {
		List<String> langs = parseOptions(metaData);
		List<String>[] optionsValuesRetVal =
				new List[] { new ArrayList<String>(langs.size()), new ArrayList<String>(langs.size()) };
		List<Locale> locales = new ArrayList<Locale>(langs.size());
		for (String lang : langs) {
			locales.add(new Locale(lang));
		}

		for (Locale locale : locales) {
			optionsValuesRetVal[0].add(locale.getDisplayLanguage());
			optionsValuesRetVal[1].add(locale.getLanguage());
		}
		return optionsValuesRetVal;

	}

	@SuppressWarnings("unchecked")
	private List<String>[] buildLangOptions() {
		Locale[] locales = Locale.getAvailableLocales();
		List<String>[] optionsValuesRetVal =
				new List[] { new ArrayList<String>(locales.length), new ArrayList<String>(locales.length) };
		HashSet<String> languagesDone = new HashSet<String>();
		optionsValuesRetVal[0] = new ArrayList<String>();
		optionsValuesRetVal[1] = new ArrayList<String>(locales.length);
		for (Locale locale : locales) {
			if (!languagesDone.contains(locale.getLanguage())) {
				optionsValuesRetVal[0].add(locale.getDisplayLanguage());
				optionsValuesRetVal[1].add(locale.getLanguage());
				languagesDone.add(locale.getLanguage());
			}
		}
		return optionsValuesRetVal;
	}

}
