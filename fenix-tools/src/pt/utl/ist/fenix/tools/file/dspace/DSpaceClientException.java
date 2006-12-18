package pt.utl.ist.fenix.tools.file.dspace;

/**
 * This class is the base exception class for a DSpace Client implementation
 * @see net.sourceforge.fenixedu.integrationTier.dspace.DSpaceHTTPPostClient
 * 
 * 
 * @author Nadir
 * 
 * Alterado em 02/05/2006 por José Pedro Pereira - Linkare TI 
 *
 */
public class DSpaceClientException extends Exception {


	/**
	 * This exception should be serializable... 
	 */
	private static final long serialVersionUID = 8280689650812546887L;

	/**
	 * @see Exception#Exception()
	 */
	public DSpaceClientException() {
        super();
    }

	/**
	 * @see Exception#Exception(java.lang.String)
	 * @param message The message for this Exception
	 */
	public DSpaceClientException(String message) {
        super(message);
    }

	/**
	 * @see Exception#Exception(java.lang.String, java.lang.Throwable)
	 * @param message The message for this Exception
	 * @param cause The rootCause for this Exception
	 */
    public DSpaceClientException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see Exception#Exception(java.lang.Throwable)
     * @param cause The rootCause for this Exception
     */
    public DSpaceClientException(Throwable cause) {
        super(cause);
    }

}
