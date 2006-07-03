package pt.linkare.ant.propreaders;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InstallerPropertiesReader;
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
	
	
	public Collection<InputProperty> buildGeneratedProperties() throws InvalidPropertySpecException
	{
		ArrayList<InputProperty> generatedProperties=new ArrayList<InputProperty>();
		String[] values=splitValues(getProperty().getPropertyValue());
		String propNameBase=getProperty().getMetaData("generated.key");
		String propTypeBase=getProperty().getMetaData("generated.type");
		String propMessageBase=getProperty().getMetaData("generated.message");
		String propDefaultValueBase=getProperty().getMetaData("generated.defaultValue");
		if(propNameBase==null || propTypeBase==null || values==null || values.length==0)
			return null;
		
		String generatedSpecBase=generateInputPropertySpec();
		InputProperty propertyBase=generateInputPropertyBase(propNameBase, propDefaultValueBase, generatedSpecBase);
	
		for(String currentValue:values)
		{
			InputProperty generatedCurrentProperty=new InputProperty(propertyBase);
			generatedCurrentProperty.setPropertyName(generateKey(propNameBase, currentValue));
			generatedCurrentProperty.setPropertyMessage(generateMessage(propMessageBase, currentValue));
			generatedProperties.add(generatedCurrentProperty);
		}
		
		return generatedProperties;
	}

	public String[] splitValues(String values)
	{
		if(values==null)
			return null;
		
		if(values.indexOf(',')>0)
			return values.split(",");
		else
			return new String[]{values};
	
	}
	
	private String generateKey(String name,String value)
	{
		return name.replaceAll("${value}", value);
	}
	
	private String generateMessage(String message,String value)
	{
		return message.replaceAll("${value}", value);
	}
	
	private final static String CRLF=System.getProperty("line.separator");
	private String generateInputPropertySpec()
	{
		StringWriter writerOut=new StringWriter();
		
		Map<String, String> metadata=getProperty().getMetaData();
		for(Map.Entry<String, String> metadataCurrent:metadata.entrySet())
		{
			if(metadataCurrent.getKey().startsWith("generated.") && !(metadataCurrent.getKey().equals("generated.key") || metadataCurrent.getKey().equals("generated.defaultValue"))) 
					writerOut.write("@"+metadataCurrent.getKey().substring("generated.".length()+1)+" = "+metadataCurrent.getValue()+CRLF);
		}
		
		return writerOut.getBuffer().toString();
	}
	
	private InputProperty generateInputPropertyBase(String propName,String defaultValue,String propSpec)
	{
		return InstallerPropertiesReader.getInstance().parseInputPropertyMetaInfo(propName, propSpec, defaultValue);
	}
	
	
	public Collection<InputProperty> readPropertyValue(boolean fromDefault) throws InvalidPropertySpecException{
	
		if(fromDefault)
			getProperty().setPropertyValue(InstallerPropertiesReader.getInstance().getDefaultValue(getProperty()));
		else
			getProperty().setPropertyDefaultValue(readProperty());
	
		return buildGeneratedProperties();
	}
	
	public abstract String readProperty() throws InvalidPropertySpecException;
	
}



