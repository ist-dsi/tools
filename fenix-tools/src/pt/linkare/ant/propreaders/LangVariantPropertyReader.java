package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class LangVariantPropertyReader extends AbstractPropertyReader {

	public LangVariantPropertyReader() {
		super();
	}

	@Override
	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {

		return getVariantString();
	}

	public String getVariantString() throws InvalidPropertySpecException, UnsupportedEncodingException {
		MenuMessage menuOptionsVariant = new MenuMessage();
		menuOptionsVariant.setMessage(buildDefaultMessage(false));
		List<String>[] optionsAndValuesVariant = null;

		InputProperty languageProp = getProperty().getPropertyMap().get(getProperty().getMetaData("languageProperty"));
		InputProperty locationProp = getProperty().getPropertyMap().get(getProperty().getMetaData("locationProperty"));

		String language = null;
		String location = null;

		if (languageProp != null) {
			language = languageProp.getPropertyValue();
		}

		if (locationProp != null) {
			location = locationProp.getPropertyValue();
		}

		optionsAndValuesVariant = buildVariantOptions(language, location);

		menuOptionsVariant.setOptions(optionsAndValuesVariant[0]);
		menuOptionsVariant.setOptionValues(optionsAndValuesVariant[1]);
		if (getProperty().getPropertyDefaultValue() != null) {
			return getInput().readMenuOptionOrDefault(menuOptionsVariant, getProperty().getPropertyDefaultValue());
		} else {
			return getInput().readMenuOption(menuOptionsVariant);
		}
	}

	@SuppressWarnings("unchecked")
	private List<String>[] buildVariantOptions(String language, String location) throws InvalidPropertySpecException {

		List<Locale> localesAvailable = Arrays.asList(Locale.getAvailableLocales());

		List<String>[] optionsValuesRetVal =
				new List[] { new ArrayList<String>(localesAvailable.size()), new ArrayList<String>(localesAvailable.size()) };
		for (Locale locale : localesAvailable) {
			if ((language == null || (locale.getLanguage().equals(language) && !optionsValuesRetVal[1].contains(locale
					.getVariant())))
					&& (location == null || (locale.getCountry().equals(location) && !optionsValuesRetVal[1].contains(locale
							.getVariant())))) {
				String variant = locale.getDisplayVariant();
				if (variant == null || variant.length() == 0) {
					continue;
				}
				String displayVariant = locale.getDisplayCountry();
				if (displayVariant == null || displayVariant.length() == 0) {
					displayVariant = "default";
				}

				optionsValuesRetVal[0].add(displayVariant);
				optionsValuesRetVal[1].add(variant);
			}
		}

		if (!getProperty().isPropertyRequired()) {
			optionsValuesRetVal[0].add("None");
			optionsValuesRetVal[1].add("");
		}

		return optionsValuesRetVal;

	}

}
