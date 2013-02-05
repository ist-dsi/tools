package pt.utl.ist.fenix.tools.file.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.dspace.FileSetDeleteException;
import pt.utl.ist.fenix.tools.file.dspace.FileSetDeleteRequest;
import pt.utl.ist.fenix.tools.file.dspace.FileSetPermissionChangeException;
import pt.utl.ist.fenix.tools.file.dspace.FileSetPermissionChangeRequest;
import pt.utl.ist.fenix.tools.file.dspace.FileSetUploadException;
import pt.utl.ist.fenix.tools.file.dspace.FileSetUploadRequest;

public abstract class AbstractRemoteFileSetManager implements IRemoteFileSetManager {

    public AbstractRemoteFileSetManager() {
        super();
    }

    private Remote remoteRef = null;

    public Remote getRemoteRef() throws RemoteException {
        if (remoteRef == null) {
            remoteRef = RMIConfig.getInstance().exportObject(this);
        }

        return remoteRef;
    }

    public FileSetDescriptor uploadFileSet(IRemoteFile remoteBaseDir, FileSetUploadRequest request, String username,
            String password) throws FileSetUploadException, RemoteException {
        try {
            return uploadFileSet(new File(remoteBaseDir.getAbsolutePath()), request.getOriginalFilename(), request.getPath(),
                    request.getFileSet(), username, password);
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    public void changeFileSetPermissions(FileSetPermissionChangeRequest request, String username, String password)
            throws FileSetPermissionChangeException, RemoteException {
        try {
            changeFileSetPermissions(request.getFileSetDescriptor(), request.isPrivateFile().booleanValue(), username, password);
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    public void deleteFileSet(FileSetDeleteRequest request, String username, String password) throws FileSetDeleteException,
            RemoteException {

        try {
            deleteFileSet(request.getFileSetDescriptor(), username, password);
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public IRemoteFile getBaseRemoteDir(String username, String password) throws RemoteException {
        RemoteFileImpl remoteFileImpl;
        try {
            remoteFileImpl = new RemoteFileImpl(createTemporaryUploadDirectory(username, password));
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
        return (IRemoteFile) RMIConfig.getInstance().exportObject(remoteFileImpl);
    }

    @Override
    public IRemoteFile retrieveBaseRemoteDir(FileSetDescriptor descriptor, String username, String password)
            throws RemoteException {
        RemoteFileImpl remoteFileImpl;
        try {
            remoteFileImpl = new RemoteFileImpl(materializeFileSetToTemporaryDirectory(descriptor, username, password));
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
        return (IRemoteFile) RMIConfig.getInstance().exportObject(remoteFileImpl);
    }

    public abstract FileSetDescriptor uploadFileSet(File baseDir, String originalFileName, VirtualPath path, FileSet fs,
            String username, String password) throws IOException;

    public abstract File createTemporaryUploadDirectory(String username, String password) throws IOException;

    public abstract File materializeFileSetToTemporaryDirectory(FileSetDescriptor descriptor, String username, String password)
            throws IOException;
}
