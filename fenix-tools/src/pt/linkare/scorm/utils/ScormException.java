/**
 * 
 */
package pt.linkare.scorm.utils;

/**
 * @author oferreira
 * 
 */
public class ScormException extends Exception {

//  Default messages for diferent type of Exceptions
	public static final String IMS_MANIFEST_XMLEXCEPTION_PARSE = "xml.parse.problem";
	public static final String IMS_MANIFEST_XMLEXCEPTION = "xml.problem";
	public static final String IMS_MANIFEST_IOEXCEPTION_PARSE = "io.parse.problem";
	public static final String IMS_MANIFEST_IOEXCEPTION = "io.problem";
	public static final String IMS_MANIFEST_NULLPOINTEREXCEPTION = "null.pointer.problem";
	public static final String IMS_MANIFEST_EXCEPTION = "general.problem";
	public static final String IMS_MANIFEST_FILE_DIRECTORY_FAILED_CREATE = "io.file.creation.problem";

	/**
	 * Field serialVersionUID
	 */
	private static final long serialVersionUID = 2361719304619112275L;

	/**
     * 
     */
	public ScormException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ScormException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ScormException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public ScormException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
