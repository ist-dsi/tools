package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;

public class HostnameListPropertyReader extends HostnamePropertyReader{

	public HostnameListPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		
		String baseValue=null;
		boolean validated=false;
		while(!validated)
		{
			baseValue=readPropertySimple();
			String[] values=splitValues(baseValue);
			validated=true;
			if(parseBoolean(getProperty().getMetaData("validate"), false))
			{
				for(String value:values)
				{
					validated=validated & isValidHostname(value);
					if(!validated) 
					{
						System.out.println("Hostname '"+value+"' is not a valid hostname!");
						continue;
					}
				}
			}
		}
		return baseValue;
	}
}

