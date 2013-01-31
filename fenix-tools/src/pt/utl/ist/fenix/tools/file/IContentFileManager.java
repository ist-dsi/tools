package pt.utl.ist.fenix.tools.file;

import java.io.InputStream;
import java.util.Collection;

public interface IContentFileManager {

	/**
	 * Changes metaData relative for an Item
	 * 
	 */
	public void changeItemMetaData(String itemHandler, Collection<FileSetMetaData> metaData);

	public FileDescriptor createItemWithFile(VirtualPath path, String name, boolean privateFile,
			Collection<FileSetMetaData> metaData, InputStream inputStream);

	public FileDescriptor createItemWithFile(VirtualPath path, String name, boolean privateFile, String author, String title,
			InputStream inputStream);

	public FileDescriptor addFileToItem(VirtualPath path, String name, String itemId, boolean privateFile, InputStream inputStream);

	public void removeFileFromItem(String uniqueId);

	/**
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
