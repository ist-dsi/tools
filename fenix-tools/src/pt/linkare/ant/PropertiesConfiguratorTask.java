package pt.linkare.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.TaskContainer;
import org.apache.tools.ant.taskdefs.Property;

import pt.linkare.ant.propreaders.PropertyReaderManager;

public class PropertiesConfiguratorTask extends Property implements TaskContainer {

	private File specFile=null;
	private File file=null;
	private String additionalPackageForPropertyReaders=null;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		
		if(getProject()==null)
			throw new BuildException("The project is not yet specified... This task requires the project to be set...");
		
		//initialize stdin in ant way
		StdIn.getInstance(getProject());
		PropertyReaderManager.getInstance().setAdditionalPackageForPropertyReaders(getAdditionalPackageForPropertyReaders());
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

	/**
	 * @return Returns the additionalPackageForPropertyReaders.
	 */
	public String getAdditionalPackageForPropertyReaders() {
		return additionalPackageForPropertyReaders;
	}

	/**
	 * @param additionalPackageForPropertyReaders The additionalPackageForPropertyReaders to set.
	 */
	public void setAdditionalPackageForPropertyReaders(String additionalPackageForPropertyReaders) {
		this.additionalPackageForPropertyReaders = additionalPackageForPropertyReaders;
	}

	

}
