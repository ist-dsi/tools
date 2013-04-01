/**
 * 
 */
package pt.utl.ist.fenix.tools.file.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetQueryResults;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.dspace.FileSetDeleteException;
import pt.utl.ist.fenix.tools.file.dspace.FileSetPermissionChangeException;
import pt.utl.ist.fenix.tools.file.dspace.FileSetUploadException;

/**
 * The remote interface definition for the RMI Connection
 * to a FileSetManager
 * 
 * A client should first invoke getRemoteFile first and use its methods to upload the fileset...
 * After that it should emit an uploadFileSet request passing the fileSetUpload Object
 * that has just been updated to end the upload process...
 * 
 * 
 * @author jppereira
 * @see java.rmi.Remote
 * 
 */
public interface IRemoteFileSetManager extends Remote {
//The interface must extend Remote because of RMI

    public FileSetDescriptor uploadFileSet(IRemoteFile baseDir, VirtualPath path, String originalFileName, boolean privateFile,
            FileSet fs, String username, String password) throws FileSetUploadException, RemoteException;

    public void changeFileSetPermissions(FileSetDescriptor descriptor, boolean privateFile, String username, String password)
            throws FileSetPermissionChangeException, RemoteException;

    public void deleteFileSet(FileSetDescriptor descriptor, String username, String password) throws FileSetDeleteException,
            RemoteException;

    public FileSetDescriptor listRecursiveFromRoot(FileSetDescriptor descriptor, String username, String password)
            throws RemoteException;

    /**
     * @return A remote temporary directory handle
     */
    public IRemoteFile getBaseRemoteDir(String username, String password) throws RemoteException;

    public IRemoteFile retrieveBaseRemoteDir(FileSetDescriptor descriptor, String username, String password)
            throws RemoteException;

    public FileSetDescriptor getRootDescriptor(FileSetDescriptor innerChildDescriptor, String username, String password)
            throws RemoteException;

    public FileSetQueryResults searchFileSets(FilesetMetadataQuery query, VirtualPath optionalPathToRestrictSearch,
            String username, String password) throws RemoteException;

}
