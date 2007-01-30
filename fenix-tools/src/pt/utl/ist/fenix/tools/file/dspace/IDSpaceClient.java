package pt.utl.ist.fenix.tools.file.dspace;

import java.util.Collection;

import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchResult;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.FileSetQueryResults;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery;
import pt.utl.ist.fenix.tools.file.VirtualPath;


/**
 * This is the interface that any DSpaceClient should implement...
 * The DSpaceClient general class will use this interface to
 * dispatch calls to the specific implementation
 * 
 * 
 * @author jpereira - Linkare TI
 * 
 */
public interface IDSpaceClient {

	/**
	 * This method is called before any other method to enable
	 * the IDSpaceClient access to its context
	 * @param ctx The IDSpaceContext access reference
	 */
	public void init(DSpaceFileManager filemanager);
	
	/**
	 * This method should implement the upload of a file to the server
	 * 
	 * @param fileUpload the FileUpload object from DSpace that describes the upload file
	 * @param file The file to be uploaded to the server side
	 * @return a UploadedFileDescriptor the process on the client
	 * @throws DSpaceClientException if any error occurs during the upload process
	 */
	public FileSetDescriptor uploadFileSet(VirtualPath path,String originalFilename,FileSet fs,boolean privateFile) throws DSpaceClientException;

	/**
	 * This method implements the upload of a file to an existing item
	 * 
	 * 
	 */
	public FileSetDescriptor addFileToItem(VirtualPath path, String name, String itemHandle, FileSet fileSet, boolean privateFile) throws DSpaceClientException;
	
	
	/**
	 * Removes a bitstream from the associated item 
	 */
	public void removeFileFromItem(String uniqueId) throws DSpaceClientException;
	
	/**
	 * This method enables deletion of a previously uploaded file in
	 * case of an error or if it is deleted from the client space
	 * 
	 * @param bitstreamIdentification The name of the bitStream in DSpace
	 * @throws DSpaceClientException if any error occurs during the delete process
	 */
	public void deleteFileSet(FileSetDescriptor descriptor) throws DSpaceClientException;
	
	
	/**
	 * Changes the metaData of a given file. 
	 */
	
	public void changeItemMetaData(String itemHandler, Collection<FileSetMetaData> metaData) throws DSpaceClientException;
	
	/**
	 * This method permits changing the permissions on a previously uploaded file
	 * @param bitstreamIdentification The bitStreamIdentification to change permissions on
	 * @param privateFile Wether this file should be made private or public(private=false)
	 */
	public void changeFileSetPermissions(FileSetDescriptor descriptor, boolean privateFile) throws DSpaceClientException;

	public FileSet retrieveFileSet(FileSetDescriptor descriptor) throws DSpaceClientException;

	public FileSetDescriptor listAllDescriptorsFromRoot(FileSetDescriptor rootFileSetDescriptor) throws DSpaceClientException;

	public FileSetDescriptor getRootDescriptor(FileSetDescriptor innerChildDescriptor) throws DSpaceClientException;

	public FileSetQueryResults searchFileSets(FilesetMetadataQuery query, VirtualPath optionalPathToRestrictSearch) throws DSpaceClientException;
	
	public FileSearchResult searchFiles(FileSearchCriteria field,  VirtualPath optionalPathToRestrictSearch) throws DSpaceClientException;
	
	public FileSearchResult searchFiles(FileSearchCriteria field) throws DSpaceClientException;
}