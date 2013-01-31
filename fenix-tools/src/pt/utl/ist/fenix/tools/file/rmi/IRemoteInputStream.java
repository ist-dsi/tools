package pt.utl.ist.fenix.tools.file.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteInputStream extends Remote {

	public int read() throws RemoteException;

	public int available() throws RemoteException;

	public void close() throws RemoteException;

}
