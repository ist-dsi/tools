package pt.utl.ist.fenix.tools.file.rmi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;

public class RemoteOutputStreamImpl implements IRemoteOutputStream,RemoteHandledStream {

	
	@SuppressWarnings("unused")
	private RemoteFileImpl remoteFileImpl;
	@SuppressWarnings("unused")
	private File underlyingFile;
	private FileOutputStream delegate=null;
	
	public RemoteOutputStreamImpl(RemoteFileImpl remoteFileImpl,File underlyingFile) throws RemoteException
	{
		this.remoteFileImpl=remoteFileImpl;
		this.underlyingFile=underlyingFile;
		updateLastAccessedTime();
		try {
			this.delegate=new FileOutputStream(underlyingFile);
		}
		catch (FileNotFoundException e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}
	
	public void flush() throws RemoteException {
		updateLastAccessedTime();
		try {
			delegate.flush();
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}

	public void write(byte[] b, int off, int len) throws RemoteException {
		updateLastAccessedTime();
		try {
			delegate.write(b,off,len);
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}

	public void write(int b) throws RemoteException {
		updateLastAccessedTime();
		try {
			delegate.write(b);
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}

	public void close() throws RemoteException {
		updateLastAccessedTime();
		try {
			delegate.close();
			hasEnded=true;
			RMIConfig.unExportObject(this);
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}

	}

	public void write(byte[] b) throws RemoteException {
		updateLastAccessedTime();
		try {
			delegate.write(b);
		}
		catch (IOException e) {
			throw new RemoteException(e.getMessage(),e);
		}
	}

	private long lastAccessTime=0L;
	public long getLastAccessTime() {
		return lastAccessTime;
	}

	public void destroy() throws IOException {
			delegate.close();
	}
	
	private boolean hasEnded=false;
	public boolean hasEnded()
	{
		return hasEnded;
	}

	
	private void updateLastAccessedTime() {
		this.lastAccessTime=System.currentTimeMillis();
	}
	
	public String toString()
	{
		return getClass().getName()+" - File: "+underlyingFile.getAbsolutePath();
	}

}
