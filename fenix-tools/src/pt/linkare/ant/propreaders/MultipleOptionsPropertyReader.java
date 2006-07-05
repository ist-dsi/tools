package pt.linkare.ant.propreaders;

import java.util.List;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class MultipleOptionsPropertyReader extends AbstractPropertyReader{

	public MultipleOptionsPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		return readPropertyMultiple();
	}
	
	
	private String readPropertyMultiple() throws InvalidPropertySpecException
	{
		MenuMessage menuMessage=buildMenuMessage();
		if("*".equals(getProperty().getPropertyDefaultValue()))
		{
			StringBuffer defaultValue=new StringBuffer("");
			List<String> optionsValues=menuMessage.getOptionValues();
			for(String optionValue:optionsValues)
			{
				defaultValue.append(","+optionValue);
			}
			//remove the first comma in the default Value
			getProperty().setPropertyDefaultValue(defaultValue.toString().substring(1));
		}
		
		if(getProperty().isPropertyRequired())
		{
			if(getProperty().getPropertyDefaultValue()!=null)
				return getInput().readMultipleOptionOrDefault(menuMessage,getProperty().getPropertyDefaultValue());
			else
				return getInput().readMultipleOption(menuMessage);
			
		}
		else
		{
			if(getProperty().getPropertyDefaultValue()!=null)
				return getInput().readMultipleOptionOrQuitOrDefault(menuMessage, getProperty().getPropertyDefaultValue());
			else
				return getInput().readMultipleOptionOrQuit(menuMessage);
			
		}
		
	}
	
}
