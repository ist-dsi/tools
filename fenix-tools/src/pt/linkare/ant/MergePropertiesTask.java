/**
 * 
 */
package pt.linkare.ant;

// java imports
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * MergeProperties provides functionality to merge two separate .properties configuration files into one while
 * preserving user comments.
 * 
 * @author jpereira - Linkare TI
 */
public class MergePropertiesTask extends Task {

	/**
	 * 
	 */
	public MergePropertiesTask() {
		super();
	}

	private String encoding = null;

	/** determines source .properties file */
	private File mergeFile;

	/** determines property over-ride file */
	private File importFile;

	/** determines where final properties are saved */
	private File destFile;

	/** stores a collection of properties added to merged file */
	private HashMap map = new HashMap();

	/** stores a log of the properties not found in the import file */
	private File logFile;

	/** stores a collection of properties non-existent in the merge File */
	private HashMap undefinedMergeMap = new HashMap();

	/**
	 * @return Returns the logFile.
	 */
	public File getLogFile() {
		return logFile;
	}

	/**
	 * @param logFile
	 *            The logFile to set.
	 */
	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	/**
	 * Configures the source input file to read the source properties.
	 * 
	 * @param file
	 *            A File object representing the source .properties file to read.
	 */
	public void setFile(final File file) {
		mergeFile = file;
	}

	/**
	 * Configures the destination file to overwrite the properties provided in the source file.
	 * 
	 * @param file
	 *            A File object representing the destination file to merge the combined properties into.
	 */
	public void setImportFile(final File file) {
		importFile = file;
	}

	/**
	 * Configures the destination file to write the combined properties.
	 * 
	 * @param file
	 *            A File object representing the destination file to merge the combined properties into.
	 */
	public void setToFile(final File file) {
		destFile = file;
	}

	/**
	 * Method invoked by the ant framework to execute the action associated with this task.
	 * 
	 * @throws BuildException
	 *             Any fatal exception that occurs while processing the task
	 */
	public void execute() throws BuildException {
		// validate provided parameters
		validate();

		// read source .properties
		List source = loadFile(mergeFile);
		List merge = loadFile(importFile);

		// get merged property collection
		Properties props = loadProperties();

		// iterate through source, and write to file with updated properties
		writeFile(source, merge, props);

		logUnexistent();
	}

	private void logUnexistent() {
		if (getLogFile() != null) {
			if (!getLogFile().exists()) try {
				getLogFile().createNewFile();
			}
			catch (IOException e) {
				throw new BuildException("Error creatng log file " + logFile.getPath(), e);
			}

			PrintWriter pw;
			try {
				pw = new PrintWriter(new FileWriter(getLogFile(), true),true);
			}
			catch (IOException e) {
				throw new BuildException("Unable to append to log file " + logFile.getPath(), e);
			}
			pw.println("********* " + mergeFile.getName() + " ***********");
			pw
					.println("Logging merge of file " + mergeFile.getPath() + " with properties from "
							+ importFile.getPath());
			pw.println("Merge was done into file " + destFile.getPath());
			pw.println("");
			pw.println("Properties not found on  import file: ");
			for (Object oEntry : undefinedMergeMap.entrySet()) {
				Map.Entry entry = (Map.Entry) oEntry;
				pw.println(entry.getKey() + "=" + entry.getValue());
			}
			pw.println("");
			pw.println("");
			pw.println("");
			pw.close();
		}

	}

	/**
	 * Validate that the task parameters are valid.
	 * 
	 * @throws BuildException
	 *             if parameters are invalid
	 */
	private void validate() throws BuildException {
		map.clear();
		undefinedMergeMap.clear();

		if (!importFile.canRead()) {
			final String message = "Unable to read from " + importFile + ".";

			throw new BuildException(message);
		}
		if (!mergeFile.canRead()) {
			final String message = "Unable to read from " + mergeFile + ".";
			throw new BuildException(message);
		}
		if (!destFile.exists()) {
			try {
				destFile.createNewFile();
			}
			catch (IOException e) {
				throw new BuildException("Could not create " + destFile + ".");
			}
		}
	}

	/**
	 * Reads the contents of the selected file and returns them in a List that contains String objects that represent
	 * each line of the file in the order that they were read.
	 * 
	 * @param file
	 *            The file to load the contents into a List.
	 * @return a List of the contents of the file where each line of the file is stored as an individual String object
	 *         in the List in the same physical order it appears in the file.
	 * @throws BuildException
	 *             An exception can occur if the version file is corrupted or the process is in someway interrupted
	 */
	private List loadFile(File file) throws BuildException {
		List list = new ArrayList();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			String record;
			try {
				while ((record = in.readLine()) != null) {
					record = record.trim();
					list.add(record);
				}
			}
			catch (Exception e) {
				throw new BuildException("Could not read file:" + file, e);
			}
			finally {
				in.close();
			}
		}
		catch (IOException IOe) {
			// had an exception trying to open the file
			throw new BuildException("Could not read file:" + file, IOe);
		}
		return list;
	}

	/**
	 * Loads the properties from the source .properties file specified and over-rides them with those found in the merge
	 * file.
	 * 
	 * @return a collection of merged properties from both specified .properties files
	 * @throws BuildException
	 *             If either of the .properties files cannot be loaded
	 */
	private Properties loadProperties() throws BuildException {
		Properties props = new Properties();
		Properties newProps = new Properties();
		try {
			InputStream in = new FileInputStream(mergeFile);
			InputStream in2 = new FileInputStream(importFile);

			try {
				props.load(in);
				newProps.load(in2);

				Enumeration enumPropsBase = props.propertyNames();
				while (enumPropsBase.hasMoreElements()) {
					String key = (String) enumPropsBase.nextElement();
					if (newProps.getProperty(key) == null && props.getProperty(key) != null)
						undefinedMergeMap.put(key, props.getProperty(key));
				}

				Enumeration enumProps = newProps.propertyNames();
				while (enumProps.hasMoreElements()) {
					String key = (String) enumProps.nextElement();
					props.setProperty(key, newProps.getProperty(key));
				}
				return props;
			}
			catch (Exception e) {
				throw new BuildException("Could not read file.", e);
			}
			finally {
				in.close();
				in2.close();
			}
		}
		catch (IOException IOe) {
			throw new BuildException("Could not read file.", IOe);
		}
	}

	/**
	 * Writes the merged properties to a single file while preserving any comments.
	 * 
	 * @param source
	 *            A list containing the contents of the original source file
	 * @param merge
	 *            A list containing the contents of the file to merge
	 * @param props
	 *            A collection of properties with their values merged from both files
	 * @throws BuildException
	 *             if the destination file can't be created
	 */
	private void writeFile(List source, List merge, Properties props) throws BuildException {
		Iterator iterate = source.iterator();
		try {
			FileOutputStream out = new FileOutputStream(destFile);
			PrintStream p = new PrintStream(out, true, getEncoding());
			try {
				// write original file with updated values
				while (iterate.hasNext()) {
					String line = (String) iterate.next();
					p.println(updateProperty(line, props));
				}
				// add new properties to file
				Properties newProps = getNewProperties(props);
				writeNewProperties(merge, newProps, p);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new BuildException("Could not write file: " + destFile,e);
			}
			finally {
				out.close();
			}
		}
		catch (IOException IOe) {
			IOe.printStackTrace();
			throw new BuildException("Could not write file: " + destFile, IOe);
		}
	}

	/**
	 * Determines whether the specified line has a corresponding property in the props collection and updates the value
	 * accordingly.
	 * 
	 * @param line
	 *            The original content of the line
	 * @param props
	 *            A collection of merged property values
	 * @return an updated string matching the correct property value
	 */
	private String updateProperty(String line, Properties props) {
		if (!line.startsWith("#")) {
			if (line.indexOf("=") > 0) {
				int index = line.indexOf("=");
				String propName = line.substring(0, index).trim();
				String propValue = props.getProperty(propName);
				if (propValue != null) {
					propValue=propValue.trim();
					map.put(propName, propValue);
					return (propName + "=" + propValue);
				}
			}
		}
		return line;
	}

	/**
	 * Returns a collection of properties that have not yet been added to the destination .properties file.
	 * 
	 * @param allProps
	 *            A collection of all properties found between the source and merge property files.
	 * @return a collection of properties that have not yet been added to the destination .properties files including
	 *         their final values
	 */
	private Properties getNewProperties(Properties allProps) {
		Properties newProps = new Properties();
		Enumeration enumProps = allProps.propertyNames();
		while (enumProps.hasMoreElements()) {
			String key = (String) enumProps.nextElement();
			if (!map.containsKey(key)) {
				newProps.setProperty(key, allProps.getProperty(key));
			}
		}
		return newProps;
	}

	private Properties unexistentProperties(Properties allProps) {
		Properties newProps = new Properties();
		Enumeration enumProps = allProps.propertyNames();
		while (enumProps.hasMoreElements()) {
			String key = (String) enumProps.nextElement();
			if (!map.containsKey(key)) {
				newProps.setProperty(key, allProps.getProperty(key));
			}
		}
		return newProps;
	}

	/**
	 * Writes the elements from the list that have not yet been written.
	 * 
	 * @param list
	 *            A list containing each line of the file to merge
	 * @param props
	 *            A collection of the new properties to add to the file
	 * @param p
	 *            A stream to print to the destination .properties file
	 * @throws IOException
	 *             can occur if an exception occurs while writing to the file
	 */
	private void writeNewProperties(List list, Properties props, PrintStream p) throws IOException {
		List tempList = new ArrayList();
		Enumeration keys = props.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			// find the key in the merge list and add to p.out()
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				String line = (String) iterator.next();
				if (line.startsWith("#")) {
					tempList.add(line);
				}
				else {
					int index = line.indexOf("=");
					if (index > 0) {
						String newKey = line.substring(0, index).trim();
						String newValue = props.getProperty(newKey);
						if (newValue != null) {
							newValue=newValue.trim();
							// if it matches, then dump to p.out
							Iterator iterateTemp = tempList.iterator();
							while (iterateTemp.hasNext()) {
								p.println((String) iterateTemp.next());
							}
							p.println(newKey + "=" + newValue);
							break;
						}
					}
					tempList.clear();
				}
			}
		}
	}

	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding
	 *            The encoding to set.
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
