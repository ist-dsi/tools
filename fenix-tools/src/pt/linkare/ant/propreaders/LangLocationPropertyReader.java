package pt.linkare.ant.propreaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class LangLocationPropertyReader extends AbstractPropertyReader{

	public LangLocationPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		
		return getLocationString();
	}
	

	
	public String getLocationString() throws InvalidPropertySpecException
	{
		MenuMessage menuOptionsLocation=new MenuMessage();
		menuOptionsLocation.setMessage(buildDefaultMessage(false));
		List<String>[] optionsAndValuesLocation=null;
		if(getProperty().getMetaData("languageProperty")!=null)
		{
			optionsAndValuesLocation=buildLocationOptions(getProperty().getPropertyMap().get(getProperty().getMetaData("languageProperty")).getPropertyValue());
		}
		else
			optionsAndValuesLocation=buildLocationOptions(null);
		
		menuOptionsLocation.setOptions(optionsAndValuesLocation[0]);
		menuOptionsLocation.setOptionValues(optionsAndValuesLocation[1]);
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readMenuOptionOrDefault(menuOptionsLocation, getProperty().getPropertyDefaultValue());
		else
			return getInput().readMenuOption(menuOptionsLocation);
	}


	@SuppressWarnings("unchecked")
	private List<String>[] buildLocationOptions(String language) throws InvalidPropertySpecException {
		
		List<Locale> localesAvailable=Arrays.asList(Locale.getAvailableLocales());
		
		List<String>[] optionsValuesRetVal=(List<String>[])new List[]{new ArrayList<String>(localesAvailable.size()),new ArrayList<String>(localesAvailable.size())};
		for(Locale locale:localesAvailable)
		{
			if(language==null || (locale.getLanguage().equals(language) && !optionsValuesRetVal[1].contains(locale.getCountry())))
			{
				optionsValuesRetVal[0].add(locale.getDisplayCountry());
				optionsValuesRetVal[1].add(locale.getCountry());
			}
		}
		
		return optionsValuesRetVal;
		
	}

	
}
