/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import pt.linkare.scorm.utils.ScormMetaData;

/**
 * @author Oscar Ferreira - Linkare TI
 * 
 */

//Organizar os ficheiros e seus MetaDados num só Objecto(this)
public class ScormAsset {

	//File/s encountered in ImsManifest(and others) with common Metadata
	Collection<File> contentFiles = null;                 //not null
	//ScormMetaData created for contentFiles
	Collection<ScormMetaData> contentMetadataInfo = null;  //not null,size=0
	//XML MetaData file 
	File metadataFile = null;                             //maybe null

	public Collection<File> getContentFiles() {
		return contentFiles;
	}

	public void setContentFiles(Collection<File> contentFiles) {
		this.contentFiles = contentFiles;
	}

	public Collection<ScormMetaData> getContentMetadataInfo() {
		return contentMetadataInfo;
	}

	public void setContentMetadataInfo(Collection<ScormMetaData> contentMetadataInfo) {
		this.contentMetadataInfo = contentMetadataInfo;
	}

	public File getMetadataFile() {
		return metadataFile;
	}

	public void setMetadataFile(File metadataFile) {
		this.metadataFile = metadataFile;
	}

	@Override
	public String toString() {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);

		print(pw);

		pw.flush();
		String retVal = sw.getBuffer().toString();
		pw.close();

		return retVal;
	}

	public void print(PrintWriter out) {
		if (getMetadataFile() == null) {
			out.println("MetadataFile is the main imsmanifest file and not defined externally");
		} else {
			out.println("External MetadataFile: " + getMetadataFile());
		}

		out.println("Files that are referenced by the manifest: ");
		for (File f : getContentFiles()) {
			out.println("File: " + f.getAbsolutePath());
		}
		ScormMetaData.print(out, getContentMetadataInfo());

	}

}
