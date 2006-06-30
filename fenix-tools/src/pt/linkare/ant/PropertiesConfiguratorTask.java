package pt.linkare.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.taskdefs.Property;

public class PropertiesConfiguratorTask extends Property implements TaskContainer {

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
			//Use addProperties of the base PropertyTask as it resolves and replaces values
			addProperties(props);
			
			for(Task t:subTasks)
			{
				t.perform();
			}
		}
		catch(Exception e)
		{
			throw new BuildException(e);
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
