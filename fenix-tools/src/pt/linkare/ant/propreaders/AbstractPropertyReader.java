package pt.linkare.ant.propreaders;

import java.util.ArrayList;
import java.util.List;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;
import pt.linkare.ant.StdIn;


public abstract class AbstractPropertyReader implements PropertyReader{

	private InputProperty prop=null;
	
	public AbstractPropertyReader() {
		super();
	}


	public void setProperty(InputProperty prop) {
		this.prop=prop;
		
	}

	public InputProperty getProperty()
	{
		return prop;
	}

	public StdIn getInput()
	{
		return StdIn.getInstance();
	}


	public java.lang.String buildDefaultMessage() {
		StringBuffer message=new StringBuffer();
		if(getProperty().getPropertyMessage()==null)
		{
			message.append("Please provide the value for property "+getProperty().getPropertyName());
			if(getProperty().getPropertyType()!=null)
				message.append(" (This property is of type "+getProperty().getPropertyType()+")");
		}
		else
			message.append(getProperty().getPropertyMessage());
		
		if(getProperty().getPropertyDefaultValue()!=null)
			message.append(" ["+getProperty().getPropertyDefaultValue()+"]");
		
		message.append(StdIn.CRLF);
		return message.toString();
	}
	
	public MenuMessage buildMenuMessage() throws InvalidPropertySpecException
	{
		if(getProperty().getMetaData("options")==null)
		{
			throw new InvalidPropertySpecException("Please specifiy the options list for this menu based property - "+getProperty().getPropertyName());
		}
		List<String> options=parseOptions(getProperty().getMetaData("options"));
		List<String> optionValues=new ArrayList<String>(options.size());
		if(getProperty().getMetaData("optionsValues")==null)
		{
			for(int i=0;i<optionValues.size();i++)
			{
				optionValues.set(i, Integer.valueOf(i).toString());
			}
		}
		else
		{
			optionValues=parseOptions(getProperty().getMetaData("optionsValues"));
			if(optionValues.size()!=options.size())
				throw new InvalidPropertySpecException("Options Values for property "+getProperty().getPropertyName()+" are not the same size as options ("+optionValues.size()+" != "+options.size()+")");
		}
		
		if(getProperty().getPropertyDefaultValue()!=null)
		{
			//check if the default value is in one of the optionsValues
			if(!optionValues.contains(getProperty().getPropertyDefaultValue()))
				throw new InvalidPropertySpecException("The property default value specified is not in the allowed list...");
			
		}
		
		StringBuffer message=new StringBuffer();
		if(getProperty().getPropertyMessage()==null)
		{
			message.append("Please choose one of the values for property "+getProperty().getPropertyName());
		}
		else
			message.append(getProperty().getPropertyMessage());
				
		message.append(StdIn.CRLF);
		
		return new MenuMessage(message.toString(),options,optionValues);
	}
	
	
	/**
	 * optionsStr should be in format {"Option1","Option2","Option3","Option4"}
	 * 
	 * @param optionsStr
	 * @return The parsed options as a list of strings
	 */
	public List<java.lang.String> parseOptions(java.lang.String optionsStr) throws InvalidPropertySpecException
	{
		if(optionsStr==null) return null;
		if(! optionsStr.startsWith("{\"") || !optionsStr.endsWith("\"}"))
			throw new InvalidPropertySpecException("Array is not correctly formatted : format should be {\"value 1\",\"value 2\",\"value n\"}. Current value is : "+optionsStr);

		//now split the string by the "," tokene in full (which means the first token and the last one 
		//will have an extra quote at the beggining and the end
		//take first {" and last "} to obliviate this problem
		java.lang.String optionsStrParse=optionsStr.substring(2,optionsStr.length()-2);
		ArrayList<java.lang.String> options=new ArrayList<java.lang.String>();
		String[] vals=optionsStrParse.split("\",\"");
		for(String matched:vals)
			options.add(matched);
		
		if(options.size()==0)
			throw new InvalidPropertySpecException("Unable to parse array format: "+optionsStr);
		
		return options;
	}

	public boolean parseBoolean(String value,boolean defaultValue)
	{
		return value==null?defaultValue:(value.equalsIgnoreCase("y") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("true") || value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("on"));
	}
	
	
}



