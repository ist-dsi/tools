package pt.linkare.ant.propreaders;

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
