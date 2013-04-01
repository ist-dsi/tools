/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.File;
import java.util.Collection;

import pt.linkare.scorm.utils.ScormException;
import pt.linkare.scorm.utils.ScormMetaDataHash;

/**
 * @author Oscar Ferreira - Linkare TI
 * 
 */
public interface ScormHandler {

    public ScormData parseScormPifFile(File pifFile) throws Exception;

    public ScormData createScormPifFile(String manifestIdentifier, ScormMetaDataHash scormMetaDataMap,
            Collection<File> originalContentFiles) throws ScormException;

}
