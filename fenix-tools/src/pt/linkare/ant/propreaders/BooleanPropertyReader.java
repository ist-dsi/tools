package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.StdIn;

public class BooleanPropertyReader extends AbstractPropertyReader{

	
	public BooleanPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		return readPropertyBoolean();
	}
	
	private String readPropertyBoolean() throws UnsupportedEncodingException
	{
		String yesOption=getProperty().getMetaData("yesOption");
		String noOption=getProperty().getMetaData("noOption");
		String yesOptionValue=getProperty().getMetaData("yesOptionValue");
		String noOptionValue=getProperty().getMetaData("noOptionValue");
		if(yesOption==null) yesOption="y";
		if(noOption==null) noOption="n";
		if(yesOptionValue==null) yesOptionValue="1";
		if(noOptionValue==null) noOptionValue="0";
		
		StringBuffer message=new StringBuffer();
		if(getProperty().getPropertyMessage()==null)
		{
			message.append("Please provide the value for property "+getProperty().getPropertyName());
			if(getProperty().getPropertyType()!=null)
				message.append(" (This property is of type "+getProperty().getPropertyType()+")");
		}
		else
			message.append(getProperty().getPropertyMessage());
		
		message.append(" ("+yesOption+","+noOption+")");
		
		boolean defaultSelected=false;
		if(getProperty().getPropertyDefaultValue()!=null && getProperty().getPropertyDefaultValue().equals(yesOptionValue))
			defaultSelected=true;
		
		if(getProperty().getPropertyDefaultValue()!=null)
			message.append(" ["+(defaultSelected?yesOption:noOption)+"]");
		
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readBooleanOptionOrDefault(message.toString(), yesOption, noOption, defaultSelected)?yesOptionValue:noOptionValue;
		else
			return getInput().readBooleanOption(message.toString(), yesOption, noOption)?yesOptionValue:noOptionValue;
		
	}
	
	
	
}
