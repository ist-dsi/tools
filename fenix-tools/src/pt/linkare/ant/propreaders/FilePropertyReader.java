package pt.linkare.ant.propreaders;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;

public class FilePropertyReader extends AbstractPropertyReader{

	public FilePropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		return readPropertyFile();
	}
	
	
	private String readPropertyFile() throws InvalidPropertySpecException, UnsupportedEncodingException
	{
		boolean pathMustExist=parseBoolean(getProperty().getMetaData("validateFile"), false);
		boolean createPath=parseBoolean(getProperty().getMetaData("createFile"), false);
		boolean persistAbsolutePath=parseBoolean(getProperty().getMetaData("persistAbsolutePath"), false);
		createPath=pathMustExist?false:createPath;
		
		StringBuilder message=new StringBuilder();
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
		
		message.append(pathMustExist?" * File must exist!":"");
		
		String pathRetVal=null;
		if(getProperty().getPropertyDefaultValue()!=null)
			pathRetVal=getInput().readStringOrDefault(message.toString(), getProperty().getPropertyDefaultValue());
		else
			pathRetVal=getInput().readString(message.toString(), getProperty().isPropertyRequired()?1:0);
		
		if(pathMustExist)
		{
			File f=new File(pathRetVal);
			while(!f.exists() || !f.isFile())
			{
				System.out.println("File "+pathRetVal+" does not exist...");
				if(getProperty().getPropertyDefaultValue()!=null)
					pathRetVal=getInput().readStringOrDefault(message.toString(), getProperty().getPropertyDefaultValue());
				else
					pathRetVal=getInput().readString(message.toString(), getProperty().isPropertyRequired()?1:0);
				f=new File(pathRetVal);		
			}
		}
		if(createPath)
		{
			File f=new File(pathRetVal);
			File dirs=f.getParentFile();
			if(!dirs.exists())
			{
				if(!dirs.mkdirs())
					throw new InvalidPropertySpecException("Unable to create parent path for file "+f.getAbsolutePath());
			}
			if(!f.exists()) try {
				f.createNewFile();
			}
			catch (IOException e) {
				throw new InvalidPropertySpecException("Unable to create file "+f.getAbsolutePath());
			}
			
		}
		
		if(persistAbsolutePath)
		{
			File f=new File(pathRetVal);
			pathRetVal=f.getAbsolutePath();
		}

		return pathRetVal;
		
	}
}

