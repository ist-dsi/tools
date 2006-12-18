package pt.utl.ist.fenix.tools.file.filters;


import pt.utl.ist.fenix.tools.file.FileSet;

public interface FileSetFilter {
	public void handleFileSet(FileSet fs) throws FileSetFilterException;
}
