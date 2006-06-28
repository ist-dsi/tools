package pt.linkare.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;

public class PropertiesConfiguratorTask extends Task implements TaskContainer {

	private File specFile=null;
	private File file=null;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		//initialize stdin in ant way
		StdIn.getInstance(getProject());
		try
		{
			Properties props=InstallerPropertiesReader.readProperties(getSpecFile(),getFile());
			
			//The project properties are now set up on the project
			for(Map.Entry<Object, Object> prop:props.entrySet())
				getProject().setProperty(prop.getKey().toString(),prop.getValue().toString());
			
			for(Task t:subTasks)
			{
				t.perform();
			}
		}
		catch(Exception e)
		{
			log(getTaskName()+" Problem occurred - "+e.getMessage());
			e.printStackTrace();
		}
	}


	private ArrayList<Task> subTasks=new ArrayList<Task>();
	
	public void addTask(Task subTask) {
		subTasks.add(subTask);
	}

	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return Returns the specFile.
	 */
	public File getSpecFile() {
		return specFile;
	}

	/**
	 * @param specFile The specFile to set.
	 */
	public void setSpecFile(File specFile) {
		this.specFile = specFile;
	}

	

}
