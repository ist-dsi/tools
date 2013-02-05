package pt.utl.ist.fenix.tools.file.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class RemoteFileImpl implements IRemoteFile {

    public File rootFile = null;
    public File delegate = null;

    public RemoteFileImpl(File rootFile) throws RemoteException {
        this.delegate = rootFile;
        this.rootFile = rootFile;
    }

    @Override
    public String getName() throws RemoteException {
        return this.delegate.getName();
    }

    @Override
    public String getPath() throws RemoteException {
        return makeRelativePath(delegate.getAbsolutePath(), getRootFile().getAbsolutePath());
    }

    private String makeRelativePath(String path, String pathRoot) {
        return path.replace(pathRoot, "/");
    }

    public File getRootFile() {
        return rootFile;
    }

    @Override
    public String getAbsolutePath() throws RemoteException {
        return delegate.getAbsolutePath();
    }

    @Override
    public String getParent() throws RemoteException {
        if (getRootFile() != this.delegate) {
            return makeRelativePath(delegate.getParent(), getRootFile().getPath());
        } else {
            return null;
        }
    }

    @Override
    public void changeToParentFile() throws RemoteException {
        if (rootFile == delegate) {
            return;
        }

        delegate = delegate.getParentFile();
    }

    @Override
    public boolean canRead() throws RemoteException {
        return delegate.canRead();
    }

    @Override
    public boolean canWrite() throws RemoteException {
        return delegate.canWrite();
    }

    @Override
    public boolean exists() throws RemoteException {
        return delegate.exists();
    }

    @Override
    public boolean isHidden() throws RemoteException {
        return delegate.isHidden();
    }

    @Override
    public boolean isDirectory() throws RemoteException {
        return delegate.isDirectory();
    }

    @Override
    public boolean isFile() throws RemoteException {
        return delegate.isFile();
    }

    @Override
    public long lastModified() throws RemoteException {
        return delegate.lastModified();
    }

    @Override
    public long length() throws RemoteException {
        return delegate.length();
    }

    @Override
    public boolean createNewFile() throws RemoteException {
        try {
            return delegate.createNewFile();
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public boolean delete() throws RemoteException {
        return delegate.delete();
    }

    @Override
    public void deleteOnExit() throws RemoteException {
        delegate.deleteOnExit();
    }

    @Override
    public void mkdir(String relativePath) throws RemoteException {
        File delegateFile = new File(this.rootFile, relativePath);
        delegateFile.mkdir();
        delegate = delegateFile;
    }

    @Override
    public void mkdirs(String relativePath) throws RemoteException {
        File delegateFile = new File(this.rootFile, relativePath);
        delegateFile.mkdirs();
        delegate = delegateFile;
    }

    @Override
    public void createFile(String relativePath) throws RemoteException {
        File delegateFile = new File(this.rootFile, relativePath);
        try {
            delegateFile.getParentFile().mkdirs();
            delegateFile.createNewFile();
        } catch (IOException e) {
            throw new RemoteException(e.getMessage(), e);
        }
        delegate = delegateFile;
    }

    @Override
    public void getFile(String relativePath) throws RemoteException {
        File delegateFile = new File(this.rootFile, relativePath);
        delegate = delegateFile;
    }

    @Override
    public IRemoteOutputStream getOuputStream() throws RemoteException {
        RemoteOutputStreamImpl streamOut = new RemoteOutputStreamImpl(this, this.delegate);
        RemoteStreamHandlerFactory.manageStream(streamOut);
        return (IRemoteOutputStream) RMIConfig.getInstance().exportObject(streamOut);
    }

    @Override
    public IRemoteInputStream getInputStream() throws RemoteException {
        RemoteInputStreamImpl streamIn = new RemoteInputStreamImpl(this, this.delegate);
        RemoteStreamHandlerFactory.manageStream(streamIn);
        return (IRemoteInputStream) RMIConfig.getInstance().exportObject(streamIn);
    }

    @Override
    public String[] list() throws RemoteException {
        return delegate.list();
    }

    @Override
    public String[] list(SerializableFilenameFilter filter) throws RemoteException {
        return delegate.list(filter);
    }

    @Override
    public boolean setLastModified(long lastmodified) throws RemoteException {
        return delegate.setLastModified(lastmodified);
    }

    @Override
    public boolean setReadOnly() throws RemoteException {
        return delegate.setReadOnly();
    }

    @Override
    public boolean renameTo(String relativePath) throws RemoteException {
        File newFile = new File(rootFile, relativePath);
        boolean retVal = delegate.renameTo(newFile);
        delegate = newFile;
        return retVal;
    }

}
