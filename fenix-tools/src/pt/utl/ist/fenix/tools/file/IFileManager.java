package pt.utl.ist.fenix.tools.file;

import java.io.File;
import java.io.InputStream;

public interface IFileManager {

    /**
     * Saves the file and returns the descriptor
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     * @throws FileManagerException
     */
    public FileDescriptor saveFile(FilePath filePath, String originalFilename, boolean privateFile,
            FileMetadata fileMetadata, File fileToSave);

    /**
     * Saves the file and returns the descriptor
     * 
     * @return A FileDescriptor instance that enables access to the saved file
     * @throws FileManagerException
     */
    public FileDescriptor saveFile(FilePath filePath, String originalFilename, boolean privateFile,
            FileMetadata fileMetadata, InputStream fileInputStream);

    /**
     * Deletes an existing file
     * 
     * @param uniqueId
     * @throws FileManagerException
     */
    public void deleteFile(String uniqueId) throws FileManagerException;

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
    public String getDirectDownloadUrlFormat();

    /**
     * Retrieves the file content
     * 
     * @return the file contents as a byte[]
     */
    public byte[] retrieveFile(String uniqueId);

}
