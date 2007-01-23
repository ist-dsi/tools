/**
 * 
 */
package pt.linkare.ant;

// java imports
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * MergeProperties provides functionality to merge two separate .properties
 * configuration files into one while preserving user comments.
 * 
 * @author jpereira - Linkare TI
 */
public class MergeTextFilesTask extends Task {

    /**
         * 
         */
    public MergeTextFilesTask() {
	super();
    }

    private String encoding = null;

    /** determines source .properties file */
    private File originalFile;

    /** determines property over-ride file */
    private File mergeFile;

    /** determines where final properties are saved */
    private File destFile;

    /** stores a log of the properties not found in the import file */
    private File logFile;

    /**
         * @return Returns the logFile.
         */
    public File getLogFile() {
	return logFile;
    }

    /**
         * @param logFile
         *                The logFile to set.
         */
    public void setLogFile(File logFile) {
	this.logFile = logFile;
    }

    /**
         * Configures the source input file to read the source properties.
         * 
         * @param file
         *                A File object representing the source .properties file
         *                to read.
         */
    public void setOriginalFile(final File file) {
	originalFile = file;
    }

    /**
         * Configures the destination file to overwrite the properties provided
         * in the source file.
         * 
         * @param file
         *                A File object representing the destination file to
         *                merge the combined properties into.
         */
    public void setImportFile(final File file) {
	mergeFile = file;
    }

    /**
         * Configures the destination file to write the combined properties.
         * 
         * @param file
         *                A File object representing the destination file to
         *                merge the combined properties into.
         */
    public void setDestinationFile(final File file) {
	destFile = file;
    }

    /**
         * Method invoked by the ant framework to execute the action associated
         * with this task.
         * 
         * @throws BuildException
         *                 Any fatal exception that occurs while processing the
         *                 task
         */
    public void execute() throws BuildException {
	// validate provided parameters
	validate();

	// read source .properties
	List<TextBlock> source = loadFile(originalFile);
	List<TextBlock> merge = loadFile(mergeFile);
	List<TextBlock> undefinedOnImport=new ArrayList<TextBlock>();
	
	mergeTextBlock(source, merge,undefinedOnImport);

	// iterate through source, and write to file with updated properties
	writeOutFile(source,merge);

	//log all unexistent properties to the log file
	log(merge,undefinedOnImport);
    }

    
    private void log(List<TextBlock> merge,List<TextBlock> undefinedOnImport) {
	if (getLogFile() != null) {
	    if (!getLogFile().exists())
		try {
		    getLogFile().createNewFile();
		} catch (IOException e) {
		    throw new BuildException("Error creatng log file " + logFile.getPath(), e);
		}

	    PrintWriter pw;
	    try {
		pw = new PrintWriter(new FileWriter(getLogFile(), true), true);
	    } catch (IOException e) {
		throw new BuildException("Unable to append to log file " + logFile.getPath(), e);
	    }
	    pw.println("********* " + originalFile.getName() + " ***********");
	    pw.println("Logging merge of file " + originalFile.getPath() + " with properties from "
		    + mergeFile.getPath());
	    pw.println("Merge was done into file " + destFile.getPath());
	    pw.println("");
	    pw.println("Additional blocks encountered on import file: ");
	    for (TextBlock oEntry : merge) {
		pw.println(oEntry.toString());
	    }
	    pw.println("");
	    pw.println("New blocks on source file only: ");
	    for (TextBlock oEntry : undefinedOnImport) {
		pw.println(oEntry.toString());
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
         *                 if parameters are invalid
         */
    private void validate() throws BuildException {

	if (!mergeFile.canRead()) {
	    final String message = "Unable to read from " + mergeFile + ".";

	    throw new BuildException(message);
	}
	if (!originalFile.canRead()) {
	    final String message = "Unable to read from " + originalFile + ".";
	    throw new BuildException(message);
	}
	if (!destFile.exists()) {
	    try {
		destFile.createNewFile();
	    } catch (IOException e) {
		throw new BuildException("Could not create " + destFile + ".");
	    }
	}
    }

    /**
         * Reads the contents of the selected file and returns them in a List
         * that contains String objects that represent each line of the file in
         * the order that they were read.
         * 
         * @param file
         *                The file to load the contents into a List.
         * @return a List of the contents of the file where each line of the
         *         file is stored as an individual String object in the List in
         *         the same physical order it appears in the file.
         * @throws BuildException
         *                 An exception can occur if the version file is
         *                 corrupted or the process is in someway interrupted
         */
    private List<TextBlock> loadFile(File file) throws BuildException {
	try {
	    return TextBlockParser.TextBlockParserManager.getBlockParserByFileName(file.getName())
		    .readBlocks(new InputStreamReader(new FileInputStream(file),getEncoding()));
	} catch (IOException IOe) {
	    // had an exception trying to open the file
	    throw new BuildException("Could not read file:" + file, IOe);
	}
    }

    private TextBlock getBlockAndRemoveFromMerge(List<TextBlock> merge, TextBlock current,List<TextBlock> undefinedOnImport) {
	for (TextBlock block : merge) {
	    if (block.equals(current)) {
		merge.remove(block);
		return block;
	    }
	}

	undefinedOnImport.add(current);
	return current;
    }

    /**
         * Loads the properties from the source .properties file specified and
         * over-rides them with those found in the merge file.
         * 
         * @throws BuildException
         *                 If either of the .properties files cannot be loaded
         */
    private void mergeTextBlock(List<TextBlock> source, List<TextBlock> merge,List<TextBlock> undefinedOnImport) throws BuildException {

	try {
	    // Replace all source properties with merge existing properties
	    for (int i = 0; i < source.size(); i++) {
		source.set(i, getBlockAndRemoveFromMerge(merge, source.get(i),undefinedOnImport));
	    }

	} catch (Exception e) {
	    throw new BuildException("Could not read file.", e);
	}
    }

    /**
         * Writes the merged properties to a single file while preserving any
         * comments.
         * 
         * @param source
         *                A list containing the contents of the original source
         *                file
         * @param merge
         *                A list containing the contents of the file to merge
         * @param props
         *                A collection of properties with their values merged
         *                from both files
         * @throws BuildException
         *                 if the destination file can't be created
         */
    private void writeOutFile(List<TextBlock> source,List<TextBlock> merge)
	    throws BuildException {

	source.addAll(merge);

	try {
	    FileOutputStream out = new FileOutputStream(destFile);
	    PrintStream p = new PrintStream(out, true, getEncoding());
	    try {
		for (TextBlock block : source) {
		    p.print(block.toString());
		}
	    } catch (Exception e) {
		e.printStackTrace();
		throw new BuildException("Could not write file: " + destFile, e);
	    } finally {
		out.close();
	    }
	} catch (IOException IOe) {
	    IOe.printStackTrace();
	    throw new BuildException("Could not write file: " + destFile, IOe);
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
         *                The encoding to set.
         */
    public void setEncoding(String encoding) {
	this.encoding = encoding;
    }
}
