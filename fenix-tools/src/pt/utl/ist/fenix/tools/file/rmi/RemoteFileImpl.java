package pt.utl.ist.fenix.tools.file.rmi;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;

public class RemoteFileImpl implements IRemoteFile {

	public File rootFile=null;
	public File delegate=null;
	public RemoteFileImpl(File rootFile) throws RemoteException
	{
		this.delegate=rootFile;
		this.rootFile=rootFile;
	}
	
	public String getName() throws RemoteException {
		return this.delegate.getName();
	}

	public String getPath() throws RemoteException {
		return makeRelativePath(delegate.getAbsolutePath(),getRootFile().getAbsolutePath());
	}

	private String makeRelativePath(String path,String pathRoot) {
		return path.replace(pathRoot, "/");
	}
	
	public File getRootFile()
	{
		return rootFile;
	}

	public String getAbsolutePath() throws RemoteException {
		return delegate.getAbsolutePath();
	}

	public String getParent() throws RemoteException {
		if(getRootFile()!=this.delegate)
			return makeRelativePath(delegate.getParent(),getRootFile().getPath());
		else
			return null;
	}

	public void changeToParentFile() throws RemoteException {
		if(rootFile==delegate)
			return;
		
		delegate=delegate.getParentFile();
	}

	public boolean canRead() throws RemoteException {
		return delegate.canRead();
	}

	public boolean canWrite() throws RemoteException {
		return delegate.canWrite();
	}

	public boolean exists() throws RemoteException {
		return delegate.exists();
	}

	public boolean isHidden() throws RemoteException {
		return delegate.isHidden();
	}

	public boolean isDirectory() throws RemoteException {
		return delegate.isDirectory();
	}

	public boolean isFile() throws RemoteException {
		return delegate.isFile();
	}

	public long lastModified() throws RemoteException {
		return delegate.lastModified();
	}

	public long length() throws RemoteException {
		return delegate.length();
	}

	public boolean createNewFile() throws RemoteException {
		try {
			return delegate.createNewFile();
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}

	public boolean delete() throws RemoteException {
		return delegate.delete();
	}

	public void deleteOnExit() throws RemoteException {
		delegate.deleteOnExit();
	}

	public void mkdir(String relativePath) throws RemoteException {
		File delegateFile=new File(this.rootFile,relativePath);
		delegateFile.mkdir();
		delegate=delegateFile;
	}

	public void mkdirs(String relativePath) throws RemoteException {
		File delegateFile=new File(this.rootFile,relativePath);
		delegateFile.mkdirs();
		delegate=delegateFile;
	}

	public void createFile(String relativePath) throws RemoteException {
		File delegateFile=new File(this.rootFile,relativePath);
		try {
			delegateFile.getParentFile().mkdirs();
			delegateFile.createNewFile();
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}
		delegate=delegateFile;
	}

	public void getFile(String relativePath) throws RemoteException {
		File delegateFile=new File(this.rootFile,relativePath);
		delegate=delegateFile;
	}

	public IRemoteOutputStream getOuputStream() throws RemoteException {
		RemoteOutputStreamImpl streamOut=new RemoteOutputStreamImpl(this,this.delegate);
		RemoteStreamHandlerFactory.manageStream(streamOut);
		return (IRemoteOutputStream)RMIConfig.getInstance().exportObject(streamOut);
	}

	public IRemoteInputStream getInputStream() throws RemoteException {
		RemoteInputStreamImpl streamIn=new RemoteInputStreamImpl(this,this.delegate);
		RemoteStreamHandlerFactory.manageStream(streamIn);
		return (IRemoteInputStream)RMIConfig.getInstance().exportObject(streamIn);
	}

	public String[] list() throws RemoteException {
		return delegate.list();
	}

	public String[] list(SerializableFilenameFilter filter) throws RemoteException {
		return delegate.list(filter);
	}

	public boolean setLastModified(long lastmodified) throws RemoteException {
		return delegate.setLastModified(lastmodified);
	}

	public boolean setReadOnly() throws RemoteException {
		return delegate.setReadOnly();
	}

	public boolean renameTo(String relativePath) throws RemoteException {
		File newFile=new File(rootFile,relativePath);
		boolean retVal=delegate.renameTo(newFile);
		delegate=newFile;
		return retVal;
	}

}
