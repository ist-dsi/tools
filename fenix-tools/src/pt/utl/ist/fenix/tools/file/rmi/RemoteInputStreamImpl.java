package pt.utl.ist.fenix.tools.file.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;

public class RemoteInputStreamImpl implements IRemoteInputStream, RemoteHandledStream {

	@SuppressWarnings("unused")
	private RemoteFileImpl remoteFileImpl;
	@SuppressWarnings("unused")
	private File underlyingFile;
	private FileInputStream delegate = null;

	public RemoteInputStreamImpl(RemoteFileImpl remoteFileImpl, File underlyingFile) throws RemoteException {
		this.remoteFileImpl = remoteFileImpl;
		this.underlyingFile = underlyingFile;
		updateLastAccessedTime();
		try {
			this.delegate = new FileInputStream(underlyingFile);
		} catch (FileNotFoundException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public int read() throws RemoteException {
		updateLastAccessedTime();
		try {
			return delegate.read();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public int available() throws RemoteException {
		updateLastAccessedTime();
		try {
			return delegate.available();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	@Override
	public void close() throws RemoteException {
		updateLastAccessedTime();
		this.hasEnded = true;
		try {
			delegate.close();
		} catch (IOException e) {
			throw new RemoteException(e.getMessage(), e);
		}
	}

	private long lastAccessTime = 0L;

	@Override
	public long getLastAccessTime() {
		return lastAccessTime;
	}

	@Override
	public void destroy() throws IOException {
		close();
	}

	private boolean hasEnded = false;

	@Override
	public boolean hasEnded() {
		return hasEnded;
	}

	private void updateLastAccessedTime() {
		this.lastAccessTime = System.currentTimeMillis();
	}

	@Override
	public String toString() {
		return getClass().getName() + " - File : " + underlyingFile.getAbsolutePath();
	}
}
