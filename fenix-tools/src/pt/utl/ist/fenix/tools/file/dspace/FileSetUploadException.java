/**
 * 
 */
package pt.utl.ist.fenix.tools.file.dspace;

import java.rmi.RemoteException;

/**
 * Signals a problem in a file uploadrequest
 * 
 * @author jpereira - Linkare TI
 * 
 */
public class FileSetUploadException extends RemoteException {

	/**
	 * Default constructor for a FileUploadException
	 */
	public FileSetUploadException() {
		super();
	}

	/**
	 * Constructor
	 * 
	 * @param message The message to pass to the client side
	 */
	public FileSetUploadException(String message) {
		super(message);
	}

	/**
	 * @param message The message to pass to the client side
	 * @param cause The root cause of this upload request exception
	 */
	public FileSetUploadException(String message, Throwable cause) {
		super(message, cause);
	}

}
