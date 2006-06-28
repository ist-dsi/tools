package pt.linkare.ant.propreaders;

import java.io.File;

import pt.linkare.ant.InvalidPropertySpecException;
import pt.linkare.ant.StdIn;

public class PathPropertyReader extends AbstractPropertyReader{

	public PathPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		return readPropertyPath();
	}
	
	
	private String readPropertyPath() throws InvalidPropertySpecException
	{
		boolean pathMustExist=parseBoolean(getProperty().getMetaData("validatePath"), false);
		boolean createPath=parseBoolean(getProperty().getMetaData("createPath"), false);
		createPath=pathMustExist?false:createPath;
		
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
		
		message.append(pathMustExist?" * Path must exist!":"");
		
		message.append(StdIn.CRLF);
		
		String pathRetVal=null;
		if(getProperty().getPropertyDefaultValue()!=null)
			pathRetVal=getInput().readStringOrDefault(message.toString(), getProperty().getPropertyDefaultValue());
		else
			pathRetVal=getInput().readString(message.toString(), getProperty().isPropertyRequired()?1:0);
		
		System.out.println("path must exist? "+pathMustExist +" and required? "+getProperty().isPropertyRequired());
		if(pathMustExist && getProperty().isPropertyRequired())
		{
			File f=new File(pathRetVal);
			System.out.println("path ret val? "+pathRetVal + " exists? "+f.exists());
			while(!f.exists())
			{
				System.out.println("Path "+pathRetVal+" does not exist...");
				if(getProperty().getPropertyDefaultValue()!=null)
					pathRetVal=getInput().readStringOrDefault(message.toString(), getProperty().getPropertyDefaultValue());
				else
					pathRetVal=getInput().readString(message.toString(), getProperty().isPropertyRequired()?1:0);
				
				f=new File(pathRetVal);
			}
		}
		if(createPath && pathRetVal!=null)
		{
			File f=new File(pathRetVal);
			if(!f.exists())
			{
				if(!f.mkdirs())
					throw new InvalidPropertySpecException("Unable to create path "+f.getAbsolutePath());
			}
		}
		return pathRetVal;
		
	}
}

