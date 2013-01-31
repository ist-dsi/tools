/**
 * 
 */
package pt.utl.ist.fenix.tools.file.dspace;

import java.rmi.RemoteException;

/**
 * Signals a problem in a file delete request
 * 
 * @author jpereira - Linkare TI
 * 
 */
public class FileSetDeleteException extends RemoteException {

	/**
	 * Default constructor for a FileDeleteException
	 */
	public FileSetDeleteException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message The message to pass to the client side
	 */
	public FileSetDeleteException(String message) {
		super(message);
	}

	/**
	 * @param message The message to pass to the client side
	 * @param cause The root cause of this delete request exception
	 */
	public FileSetDeleteException(String message, Throwable cause) {
		super(message, cause);
	}

}
