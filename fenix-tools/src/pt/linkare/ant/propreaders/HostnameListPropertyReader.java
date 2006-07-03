package pt.linkare.ant.propreaders;

import java.net.InetAddress;
import java.net.UnknownHostException;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.MenuMessage;

public class HostnameListPropertyReader extends HostnamePropertyReader{

	public HostnameListPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		
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
					validated=validated & isValidHostname(value);
			}
		}
		return baseValue;
	}
}

