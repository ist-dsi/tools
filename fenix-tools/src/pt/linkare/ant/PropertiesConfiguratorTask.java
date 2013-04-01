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

    private boolean debug = false;
    private File specFile = null;
    private File file = null;
    private String additionalPackageForPropertyReaders = null;
    private String encoding = "ISO-8859-1";
    private String propertyCryptPassword = null;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.tools.ant.Task#execute()
     */
    @Override
    public void execute() throws BuildException {

        debug("Project is null? " + getProject() == null ? " yes" : " no");
        if (getProject() == null) {
            throw new BuildException("The project is not yet specified... This task requires the project to be set...");
        }

        //initialize stdin in ant way
        debug("Creating standard input wrapper on project");
        StdIn.getInstance(getProject(), getEncoding());
        debug("Setting additional package for property readers to " + getAdditionalPackageForPropertyReaders());
        PropertyReaderManager manager = PropertyReaderManager.getInstance(this.isDebug(), this.getEncoding());
        manager.setAdditionalPackageForPropertyReaders(getAdditionalPackageForPropertyReaders());
        manager.setPropertyCryptPassword(getPropertyCryptPassword());
        try {
            debug("Reading properties spec from file " + getSpecFile().getName() + " and current properties from "
                    + getFile().getName());
            Properties props =
                    InstallerPropertiesReader.readProperties(getSpecFile(), getFile(), this.isDebug(), this.getEncoding());
            //Use addProperties of the base PropertyTask as it resolves and replaces values
            debug("Add the properties to the project...");
            addProperties(props);

            for (Task t : subTasks) {
                debug("Performing subtask " + t.getTaskName()
                        + (getDescription() != null ? " with description " + getDescription() : ""));
                t.perform();
            }
        } catch (Throwable e) {
            if (isDebug()) {
                e.printStackTrace();
            }
            throw new BuildException(e);
        }
    }

    private ArrayList<Task> subTasks = new ArrayList<Task>();

    @Override
    public void addTask(Task subTask) {
        subTasks.add(subTask);
    }

    /**
     * @return Returns the file.
     */
    @Override
    public File getFile() {
        return file;
    }

    /**
     * @param file The file to set.
     */
    @Override
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

    /**
     * @return Returns true if this task is to debug information
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug Set to true if you want to view debugging info from this task
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void debug(String message) {
        if (debug) {
            System.out.println(getClass().getName() + ":" + message);
        }

    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getPropertyCryptPassword() {
        return propertyCryptPassword;
    }

    public void setPropertyCryptPassword(String propertyCryptPassword) {
        if (propertyCryptPassword != null && propertyCryptPassword.length() > 0) {
            this.propertyCryptPassword = propertyCryptPassword;
        } else {
            this.propertyCryptPassword = null;
        }
    }

}
