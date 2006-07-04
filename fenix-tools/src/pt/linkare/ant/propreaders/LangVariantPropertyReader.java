package pt.linkare.ant.propreaders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class LangVariantPropertyReader extends AbstractPropertyReader{

	public LangVariantPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		
		return getVariantString();
	}
	

	
	public String getVariantString() throws InvalidPropertySpecException
	{
		MenuMessage menuOptionsVariant=new MenuMessage();
		menuOptionsVariant.setMessage(buildDefaultMessage(false));
		List<String>[] optionsAndValuesVariant=null;
		String language=getProperty().getPropertyMap().get(getProperty().getMetaData("languageProperty")).getPropertyValue();
		String location=getProperty().getPropertyMap().get(getProperty().getMetaData("locationProperty")).getPropertyValue();
		optionsAndValuesVariant=buildVariantOptions(language,location);
		
		menuOptionsVariant.setOptions(optionsAndValuesVariant[0]);
		menuOptionsVariant.setOptionValues(optionsAndValuesVariant[1]);
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readMenuOptionOrDefault(menuOptionsVariant, getProperty().getPropertyDefaultValue());
		else
			return getInput().readMenuOption(menuOptionsVariant);
	}


	@SuppressWarnings("unchecked")
	private List<String>[] buildVariantOptions(String language,String location) throws InvalidPropertySpecException {
		
		List<Locale> localesAvailable=Arrays.asList(Locale.getAvailableLocales());
		
		List<String>[] optionsValuesRetVal=(List<String>[])new List[]{new ArrayList<String>(localesAvailable.size()),new ArrayList<String>(localesAvailable.size())};
		for(Locale locale:localesAvailable)
		{
			if((language==null || (locale.getLanguage().equals(language) && !optionsValuesRetVal[1].contains(locale.getVariant()))) 
					&& (location==null || (locale.getCountry().equals(location) && !optionsValuesRetVal[1].contains(locale.getVariant()))))
			{
				optionsValuesRetVal[0].add(locale.getDisplayVariant());
				optionsValuesRetVal[1].add(locale.getVariant());
			}
		}
		
		return optionsValuesRetVal;
		
	}

	
}
