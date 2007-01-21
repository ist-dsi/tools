package pt.linkare.ant.propreaders;

import pt.linkare.ant.InvalidPropertySpecException;


public class PasswordPropertyReader extends StringPropertyReader{

	public PasswordPropertyReader() {
		super();
	}
	
	public String readProperty() throws InvalidPropertySpecException {
		return readPropertySimple();
	}
	
	private String readPropertySimple()
	{
		String message=buildDefaultMessage();
		
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readPrivateStringOrDefault(message, getProperty().getPropertyDefaultValue());
		else
		{
			int specMinLength=1;
			try
			{
				specMinLength=Integer.parseInt(getProperty().getMetaData("minLength"));
			}catch(Exception e)
			{
				//noop
			}
			return getInput().readPrivateString(message, getProperty().isPropertyRequired()?specMinLength:0);
		}
			
	}
}
