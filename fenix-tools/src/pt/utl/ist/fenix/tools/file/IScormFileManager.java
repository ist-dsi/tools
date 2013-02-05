package pt.utl.ist.fenix.tools.file;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;

import pt.linkare.scorm.utils.ScormMetaDataHash;

public interface IScormFileManager {

    /**
     * Saves the file for a given type, this is used for saving SCORM packages
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     */
    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
            String title, InputStream fileInputStream, FileSetType type);

    /**
     * Saves the file for a given type, this is used for saving SCORM packages
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     */

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
            Collection<FileSetMetaData> metadata, InputStream inputStream, FileSetType type);

    /**
     * Saves the file for a given type, this is used for saving SCORM packages
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     */
    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
            String title, File fileToSave, FileSetType type);

    /**
     * Saves the normal with a set of scorm attributes, this is used to create a
     * SCORM package on the fly for a given file
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     */
    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
            String title, File fileToSave, ScormMetaDataHash scormParameters);

    /**
     * Saves the normal with a set of scorm attributes, this is used to create a
     * SCORM package on the fly for a given file
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     */
    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
            String title, InputStream fileInputStream, ScormMetaDataHash scormParameters);

    /**
     * Saves the normal with a set of scorm attributes, this is used to create a
     * SCORM package on the fly for a given file
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     */
    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
            Collection<FileSetMetaData> metaData, InputStream fileInputStream, ScormMetaDataHash scormParameters);

    /**
     * /**
     * Deletes an existing file
     * 
     * @param uniqueId
     * @throws FileManagerException
     */
    public void deleteFile(String uniqueId);

    /**
     * Changes the file permissions. In the file is private, its assumed that
     * the file storage, will ask fenix who are the persons authorized to access
     * file
     * 
     * @param uniqueId
     * @param privateFile
     */
    public void changeFilePermissions(String uniqueId, Boolean privateFile);

    /**
     * Returns the format of the download url. The format should receive only a
     * parameter with file unique Id. Example:
     * http://dspacehost/bitstream/{0}/{1} where {0} should be replaced by the
     * Id and {1} by the filename
     * 
     * @return An url schema for direct download of the file
     */
    public String formatDownloadUrl(String uniqueId, String fileName);

    /**
     * Retrieves the file content
     * 
     * @return the file InputStream for the File
     */

    public InputStream retrieveFile(String uniqueId);

    public FileSearchResult searchFiles(FileSearchCriteria critera);

    public FileSearchResult searchFiles(FileSearchCriteria critera, VirtualPath optionalPathToRestrictSearch);
}
