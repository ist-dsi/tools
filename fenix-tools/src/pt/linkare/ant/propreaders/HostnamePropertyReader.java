package pt.linkare.ant.propreaders;

import java.net.InetAddress;
import java.net.UnknownHostException;

import pt.linkare.ant.InvalidPropertySpecException;

public class HostnamePropertyReader extends AbstractPropertyReader{

	public HostnamePropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		String hostname=null;
		while(hostname==null)
		{
			hostname=readPropertySimple();
			InetAddress[] addresses=null;
			try {
				addresses = InetAddress.getAllByName(hostname);
			}
			catch (UnknownHostException e) {
				hostname=null;			
			}
			if(parseBoolean(getProperty().getMetaData("validate"),false) && (addresses==null || addresses.length==0))
			{
				System.out.println("Host  "+hostname+" is not a valid address!");
				hostname=null;			
			}
		}
		return hostname;
	}
	
	private String readPropertySimple()
	{
		String message=buildDefaultMessage();
		
		if(getProperty().getPropertyDefaultValue()!=null)
			return getInput().readStringOrDefault(message, getProperty().getPropertyDefaultValue());
		else
		{
			return getInput().readString(message, getProperty().isPropertyRequired()?1:0);
		}
			
	}
	
}
