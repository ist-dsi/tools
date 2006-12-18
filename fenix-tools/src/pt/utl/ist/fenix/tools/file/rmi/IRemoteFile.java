package pt.utl.ist.fenix.tools.file.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteFile  extends Remote {
	public String getName() throws RemoteException;
	public String getPath() throws RemoteException;
	public String getAbsolutePath() throws RemoteException;
	public String getParent() throws RemoteException;
	public boolean canRead() throws RemoteException;
	public boolean canWrite() throws RemoteException;
	public boolean exists() throws RemoteException;
	public boolean isHidden() throws RemoteException;
	public boolean isDirectory() throws RemoteException;
	public boolean isFile() throws RemoteException;
	public long lastModified() throws RemoteException;
	public long length() throws RemoteException;
	public boolean createNewFile() throws RemoteException;
	public boolean delete() throws RemoteException;
	public void deleteOnExit() throws RemoteException;
	public void mkdir(String relativePath) throws RemoteException;
	public void mkdirs(String relativePath) throws RemoteException;
	public void createFile(String relativePath) throws RemoteException;
	public void getFile(String relativePath) throws RemoteException;
	public IRemoteOutputStream getOuputStream() throws RemoteException;
	public IRemoteInputStream getInputStream() throws RemoteException;
	public String[] list() throws RemoteException;
	public String[] list(SerializableFilenameFilter filter) throws RemoteException;
	public boolean renameTo(String relativePath) throws RemoteException;
	public boolean setLastModified(long lastmodified) throws RemoteException;
	public boolean setReadOnly() throws RemoteException;
	public void changeToParentFile() throws RemoteException;
}
