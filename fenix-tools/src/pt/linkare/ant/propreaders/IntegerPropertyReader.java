package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;

public class IntegerPropertyReader extends AbstractPropertyReader{

	public IntegerPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		Integer valueRet=null;

		int min=Integer.MIN_VALUE;
		try
		{
			min=Integer.parseInt(getProperty().getMetaData("min"));
		}catch(Exception e)
		{
			//noop
		}
		int max=Integer.MAX_VALUE;
		try
		{
			max=Integer.parseInt(getProperty().getMetaData("max"));
		}catch(Exception e)
		{
			//noop
		}
		
		
		while(valueRet==null)
		{
			valueRet=readPropertyInteger();
			if(valueRet!=null && valueRet>max || valueRet<min)
			{
				System.out.println("Integer value must be between "+min+" and "+max+"!");
				valueRet=null;
			}
			else if(valueRet==null && !getProperty().isPropertyRequired())
			{
				return null;
			}
		}
		
		return valueRet.toString();
	}
	
	private Integer readPropertyInteger() throws UnsupportedEncodingException
	{
		String message=buildDefaultMessage();
		
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readIntegerOrDefault(message, getProperty().getPropertyDefaultValue());
		else
		{
			return getInput().readInteger(message);
		}
			
	}
	
}
