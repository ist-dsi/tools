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
public class FileSetRootDescriptorException extends RemoteException {

    /**
     * Default constructor for a FilePermissionChangeException
     */
    public FileSetRootDescriptorException() {
        super();
    }

    /**
     * Constructor
     * 
     * @param message The message to pass to the client side
     */
    public FileSetRootDescriptorException(String message) {
        super(message);
    }

    /**
     * @param message The message to pass to the client side
     * @param cause The root cause of this permission change exception
     */
    public FileSetRootDescriptorException(String message, Throwable cause) {
        super(message, cause);
    }

}
