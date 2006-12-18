package pt.utl.ist.fenix.tools.file.filters;

public abstract class FileSetFilterException extends Exception {

	public FileSetFilterException() {
		super();
	}

	public FileSetFilterException(String message) {
		super(message);
	}

	public FileSetFilterException(String message, Throwable cause) {
		super(message, cause);
	}

	public FileSetFilterException(Throwable cause) {
		super(cause);
	}

}
