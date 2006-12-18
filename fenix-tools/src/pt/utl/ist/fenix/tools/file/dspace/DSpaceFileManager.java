package pt.utl.ist.fenix.tools.file.dspace;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.utl.ist.fenix.tools.file.AbstractFileManager;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchResult;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetQueryResults;
import pt.utl.ist.fenix.tools.file.FileSetType;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria.SearchField;

public class DSpaceFileManager extends AbstractFileManager{

	private Logger logger=Logger.getLogger(DSpaceFileManager.class.getName());
	
	private String downloadUrlFormat;

    private IDSpaceClient dspaceClient;
   
    public DSpaceFileManager()
    {
        super();
    	try {
            downloadUrlFormat = getProperty("dspace.serverUrl") + "/" + getProperty("dspace.downloadUriFormat");
            logger.log(Level.INFO,"Configured downloadUrlFormat "+downloadUrlFormat);
            final String dspaceClientClassName=getProperty("dspace.underlying.transport.class");
            logger.log(Level.INFO,"DSpace client class name "+dspaceClientClassName);
            dspaceClient=(IDSpaceClient) Class.forName(dspaceClientClassName).newInstance();
            logger.log(Level.INFO,"Initializing dspace client");
            dspaceClient.init(this);
        } 
		catch (InstantiationException e) {
            throw new RuntimeException("Error loading dspace file manager",e);
		}
		catch (IllegalAccessException e) {
            throw new RuntimeException("Error loading dspace file manager",e);
		}
		catch (ClassNotFoundException e) {
            throw new RuntimeException("Error loading dspace file manager",e);
		}
    }
    /*
    public FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean privateFile,
            FileMetadata fileMetadata, File file) {

        final VirtualPath path = buildPath(filePath);
        final Collection<FileSetMetaData> fsMetadata=new ArrayList<FileSetMetadata>(2);
        itemMetadata.add(ItemMetadata.createAuthorMeta(fileMetadata.getAuthor()));
        itemMetadata.add(ItemMetadata.createTitleMeta(fileMetadata.getTitle()));

        try {
            final FileUpload fileUpload = new FileUpload(path, originalFilename, privateFile,itemMetadata);
            final UploadedFileDescriptor uploadedFileDescriptor = dspaceClient.uploadFile(fileUpload,
                    file);

            return new FileDescriptor(uploadedFileDescriptor.getFilename(), uploadedFileDescriptor
                    .getMimeType(), uploadedFileDescriptor.getChecksum(), uploadedFileDescriptor
                    .getChecksumAlgorithm(), uploadedFileDescriptor.getSize(), uploadedFileDescriptor
                    .getBitstreamIdentification());

        } catch (DSpaceClientException e) {
            throw new FileManagerException("error.filemanager.unableToStoreFile", e, originalFilename);
        }
    }


    private VirtualPath buildPath(VirtualPath filePath) {
    	VirtualPath path = new VirtualPath();
        for (VirtualPathNode node : filePath.getNodes()) {
            path.addNode(new VirtualPathNode(node.getName(), node.getDescription()));
        }
        return path;
    }

    public void deleteFile(String uniqueId) {
        try {
            dspaceClient.deleteFile(uniqueId);
        } catch (DSpaceClientException e) {
            throw new FileManagerException("error.filemanager.unableDeleteFile", e);
        }

    }

    public void changeFilePermissions(String uniqueId, Boolean privateFile) {
        try {
            dspaceClient.changeFilePermissions(uniqueId, privateFile);
        } catch (DSpaceClientException e) {
            throw new FileManagerException("error.filemanager.unableToChangeFilePermissions", e);
        }

    }

    public byte[] retrieveFile(String uniqueId) {
        throw new UnsupportedOperationException(
                "Dspace file manager does not support file retrieve. Use direct download url instead");

    }

    
    
	@Override
	public FileSetDescriptor internalSaveFileSet(VirtualPath virtualPath, String originalFileName, boolean privateFile, FileSet fileSet, FileSetType fileSetType) {
		// TODO Auto-generated method stub
		return null;
	}

	public String formatDownloadUrl(String uniqueId, String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	public FileSet readFileSet(FileSetDescriptor fileSetDescriptor) {
		// TODO Auto-generated method stub
		return null;
	}

	public void changePermissions(FileSetDescriptor fileSetDescriptor, Boolean privateFileSet) {
		// TODO Auto-generated method stub
		
	}

	public void deleteFileSet(FileSetDescriptor fileSetDescriptor) {
		// TODO Auto-generated method stub
		
	}

	public FileSetDescriptor listAllDescriptorsFromRoot(FileSetDescriptor rootFileSetDescriptor) {
		// TODO Auto-generated method stub
		return null;
	}
	*/

	public FileSetDescriptor internalSaveFileSet(VirtualPath virtualPath, String originalFileName, boolean privateFile, FileSet fileSet, FileSetType fileSetType) {
		try {
			return dspaceClient.uploadFileSet(virtualPath, originalFileName, fileSet, privateFile);
		}
		catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}

	public String getDirectDownloadUrlFormat() {
		return downloadUrlFormat;
	}

	public String formatDownloadUrl(String uniqueId, String fileName) {
		String formatedDownloadUrl=downloadUrlFormat;
		return MessageFormat.format(formatedDownloadUrl, uniqueId,fileName);
	}

	public FileSet readFileSet(FileSetDescriptor fileSetDescriptor)  {
		try {
			return dspaceClient.retrieveFileSet(fileSetDescriptor);
		}
		catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}

	public void changePermissions(FileSetDescriptor fileSetDescriptor, Boolean privateFileSet) {
		try
		{
			dspaceClient.changeFileSetPermissions(fileSetDescriptor, privateFileSet.booleanValue());
		}catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}

	public void deleteFileSet(FileSetDescriptor fileSetDescriptor) {
		try
		{
			dspaceClient.deleteFileSet(fileSetDescriptor);
		}catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}

	public boolean isDirectDownloadURISupported() {
		return true;
	}

	public FileSetDescriptor listAllDescriptorsFromRoot(FileSetDescriptor rootFileSetDescriptor) {
		try {
			return dspaceClient.listAllDescriptorsFromRoot(rootFileSetDescriptor);
		}
		catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}
	
	public FileSetDescriptor getRootDescriptor(FileSetDescriptor innerChildDescriptor) {

		try {
			return dspaceClient.getRootDescriptor(innerChildDescriptor);
		}
		catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}

	public FileSetQueryResults searchFileSets(FilesetMetadataQuery query, VirtualPath optionalPathToRestrictSearch) {
		try {
			return dspaceClient.searchFileSets(query,optionalPathToRestrictSearch);
		}
		catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}
	
	public FileSearchResult searchFiles(FileSearchCriteria criteria) {
		try {
			return dspaceClient.searchFiles(criteria);
		}catch(DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}	
	
	public FileSearchResult searchFiles(FileSearchCriteria criteria, VirtualPath optionalPathToRestrictSearch) {
		try {
			return dspaceClient.searchFiles(criteria, optionalPathToRestrictSearch);
		}catch(DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(),e);
		}
	}	
	
}
