package pt.linkare.ant.propreaders;

import pt.linkare.ant.InvalidPropertySpecException;

public class StringReplacePropertyReader extends AbstractPropertyReader{

	public StringReplacePropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		return readPropertyReplace();
	}
	
	private String readPropertyReplace() throws InvalidPropertySpecException
	{
		String message=buildDefaultMessage();
		String replaceString=getProperty().getMetaData("replaceString");
		if(replaceString==null)
			throw new InvalidPropertySpecException("replaceString configuration not defined...It is required!");
		
		String retVal=null;;
		if(getProperty().getPropertyDefaultValue()!=null)
			retVal=getInput().readStringOrDefault(message, getProperty().getPropertyDefaultValue());
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
			retVal=getInput().readString(message, getProperty().isPropertyRequired()?specMinLength:0);
		}
			
		return replaceString.replaceAll("${0}", retVal);
	}
	
}
