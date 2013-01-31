package pt.utl.ist.fenix.tools.file;

public class FileManagerException extends RuntimeException {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	private String key = null;

	private String[] args = null;

	public FileManagerException() {
		super();
	}

	public FileManagerException(Throwable throwable) {
		super(throwable);
	}

	public FileManagerException(final String key, final String... args) {
		super(key); // setting the message attribute
		this.key = key;
		this.args = args;
	}

	public FileManagerException(final String key, final Throwable cause, final String... args) {
		super(key, cause);
		this.key = key;
		this.args = args;
	}

	public String getKey() {
		return key;
	}

	public String[] getArgs() {
		return args;
	}

}
