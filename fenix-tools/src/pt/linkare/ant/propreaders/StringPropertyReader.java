package pt.linkare.ant.propreaders;

import pt.linkare.ant.InvalidPropertySpecException;

public class StringPropertyReader extends AbstractPropertyReader{

	public StringPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		return readPropertySimple();
	}
	
	private String readPropertySimple()
	{
		String message=buildDefaultMessage();
		
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readStringOrDefault(message, getProperty().getPropertyDefaultValue());
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
			return getInput().readString(message, getProperty().isPropertyRequired()?specMinLength:0);
		}
			
	}
	
}
