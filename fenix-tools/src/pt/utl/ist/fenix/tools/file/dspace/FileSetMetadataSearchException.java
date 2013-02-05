/**
 * 
 */
package pt.utl.ist.fenix.tools.file.dspace;

import java.rmi.RemoteException;

/**
 * Signals a problem in a permission change request
 * 
 * @author jpereira - Linkare TI
 * 
 */
public class FileSetMetadataSearchException extends RemoteException {

    /**
     * Default constructor for a FilePermissionChangeException
     */
    public FileSetMetadataSearchException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param message The message to pass to the client side
     */
    public FileSetMetadataSearchException(String message) {
        super(message);
    }

    /**
     * @param message The message to pass to the client side
     * @param cause The root cause of this permission change exception
     */
    public FileSetMetadataSearchException(String message, Throwable cause) {
        super(message, cause);
    }

}
