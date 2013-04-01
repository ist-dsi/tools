package pt.linkare.ant;


import java.io.File;

import pt.linkare.ant.MergeTextFilesTask;

import junit.framework.TestCase;

public class MergeTextFilesTaskTest extends TestCase {

    
    MergeTextFilesTask task=null;
    MergeTextFilesTask propertiesMergeTask=null;
    
    protected void setUp() throws Exception {
	super.setUp();
	task=new MergeTextFilesTask();
	task.setEncoding("ISO-8859-1");
	task.setOriginalFile(new File("test/source.css"));
	task.setImportFile(new File("test/source_specific.css"));
	task.setDestinationFile(new File("test/destination.css"));
	task.setLogFile(new File("test/logCSSMerge.txt"));

	propertiesMergeTask=new MergeTextFilesTask();
	propertiesMergeTask.setEncoding("ISO-8859-1");
	propertiesMergeTask.setOriginalFile(new File("test/source.properties"));
	propertiesMergeTask.setImportFile(new File("test/source_specific.properties"));
	propertiesMergeTask.setDestinationFile(new File("test/destination.properties"));
	propertiesMergeTask.setLogFile(new File("test/logPropertiesMerge.txt"));

    }

    protected void tearDown() throws Exception {
	super.tearDown();
    }

    public void testExecute() {
	try
	{
	    task.execute();
	}catch (Exception e) {
	    e.printStackTrace();
	    fail("Failed : "+e.getMessage());
	}
	
	try
	{
	    propertiesMergeTask.execute();
	}catch (Exception e) {
	    e.printStackTrace();
	    fail("Failed : "+e.getMessage());
	}
    }

}
