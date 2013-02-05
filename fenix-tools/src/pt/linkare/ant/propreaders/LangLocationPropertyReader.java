package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class LangLocationPropertyReader extends AbstractPropertyReader {

    public LangLocationPropertyReader() {
        super();
    }

    @Override
    public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {

        return getLocationString();
    }

    public String getLocationString() throws InvalidPropertySpecException, UnsupportedEncodingException {
        MenuMessage menuOptionsLocation = new MenuMessage();
        menuOptionsLocation.setMessage(buildDefaultMessage(false));
        List<String>[] optionsAndValuesLocation = null;
        if (getProperty().getMetaData("languageProperty") != null) {
            InputProperty propRelated = getProperty().getPropertyMap().get(getProperty().getMetaData("languageProperty"));
            if (propRelated != null) {
                optionsAndValuesLocation =
                        buildLocationOptions(getProperty().getPropertyMap().get(getProperty().getMetaData("languageProperty"))
                                .getPropertyValue());
            } else {
                optionsAndValuesLocation = buildLocationOptions(null);
            }
        } else {
            optionsAndValuesLocation = buildLocationOptions(null);
        }

        menuOptionsLocation.setOptions(optionsAndValuesLocation[0]);
        menuOptionsLocation.setOptionValues(optionsAndValuesLocation[1]);
        if (getProperty().getPropertyDefaultValue() != null) {
            return getInput().readMenuOptionOrDefault(menuOptionsLocation, getProperty().getPropertyDefaultValue());
        } else {
            return getInput().readMenuOption(menuOptionsLocation);
        }
    }

    @SuppressWarnings("unchecked")
    private List<String>[] buildLocationOptions(String language) throws InvalidPropertySpecException {

        List<Locale> localesAvailable = Arrays.asList(Locale.getAvailableLocales());

        List<String>[] optionsValuesRetVal =
                new List[] { new ArrayList<String>(localesAvailable.size()), new ArrayList<String>(localesAvailable.size()) };
        for (Locale locale : localesAvailable) {
            if (language == null
                    || (locale.getLanguage().equals(language) && !optionsValuesRetVal[1].contains(locale.getCountry()))) {
                String country = locale.getCountry();
                if (country == null || country.length() == 0) {
                    continue;
                }
                String displayCountry = locale.getDisplayCountry();
                if (displayCountry == null || displayCountry.length() == 0) {
                    displayCountry = "default";
                }
                optionsValuesRetVal[0].add(displayCountry);
                optionsValuesRetVal[1].add(country);
            }
        }

        if (!getProperty().isPropertyRequired()) {
            optionsValuesRetVal[0].add("None");
            optionsValuesRetVal[1].add("");
        }

        return optionsValuesRetVal;

    }

}
