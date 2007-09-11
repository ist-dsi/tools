package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class MenuPropertyReader extends AbstractPropertyReader{

	public MenuPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		return readPropertyMenu();
	}
	
	
	private String readPropertyMenu() throws InvalidPropertySpecException, UnsupportedEncodingException
	{
		MenuMessage menuMessage=buildMenuMessage();
		
		if(getProperty().isPropertyRequired())
		{
			if(getProperty().getPropertyDefaultValue()!=null)
				return getInput().readMenuOptionOrDefault(menuMessage,getProperty().getPropertyDefaultValue());
			else
				return getInput().readMenuOption(menuMessage);
			
		}
		else
		{
			if(getProperty().getPropertyDefaultValue()!=null)
				return getInput().readMenuOptionOrQuitOrDefault(menuMessage, getProperty().getPropertyDefaultValue());
			else
				return getInput().readMenuOptionOrQuit(menuMessage);
			
		}
		
	}
	
}
