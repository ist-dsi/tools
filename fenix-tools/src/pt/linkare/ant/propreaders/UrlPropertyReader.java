package pt.linkare.ant.propreaders;

import java.net.MalformedURLException;
import java.net.URL;

import pt.linkare.ant.InvalidPropertySpecException;

public class UrlPropertyReader extends AbstractPropertyReader{

	public UrlPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		String url=null;
		while(url==null)
		{
			url=readPropertySimple();
			URL urlLocation=null;
			try {
				urlLocation = new URL(url);
			}
			catch (MalformedURLException e) {
				url=null;
			}
			if(parseBoolean(getProperty().getMetaData("validate"),false) && (urlLocation==null))
			{
				System.out.println(url+" is not a valid URL!");
				url=null;			
			}
		}
		return url;
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
