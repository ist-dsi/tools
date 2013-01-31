/**
 * 
 */
package pt.utl.ist.fenix.tools.file.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the remote interface of DSpace to
 * do file transfers... It shoud behave more or less like @see java.io.BufferedOutputStream
 * except for RMI implementation details
 * 
 * The underlying implementation should actually use
 * a BufferedOutputStream underneath
 * 
 * @author jpereira - Linkare TI
 * 
 */
public interface IRemoteOutputStream extends Remote {

	/**
	 * @see java.io.BufferedOutputStream#flush()
	 * @throws RemoteException
	 */
	public void flush() throws RemoteException;

	/**
	 * @see java.io.BufferedOutputStream#write(byte[], int, int)
	 * @param b The byte[] from which to write data
	 * @param off The offset from which to start from
	 * @param len The number of bytes to write from b
	 * @throws RemoteException
	 */
	public void write(byte[] b, int off, int len) throws RemoteException;

	/**
	 * @see java.io.BufferedOutputStream#write(int)
	 * @param b The byte value to write
	 * @throws RemoteException If a comm exception or an underlying IOException occurs
	 */
	public void write(int b) throws RemoteException;

	/**
	 * @see java.io.FilterOutputStream#close()
	 * @throws RemoteException
	 */
	public void close() throws RemoteException;

	/**
	 * @see java.io.FilterOutputStream#write(byte[])
	 * @param b The byte[] from which to write
	 * @throws RemoteException
	 */
	public void write(byte[] b) throws RemoteException;

}
