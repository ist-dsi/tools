package pt.utl.ist.fenix.tools.file;

import pt.utl.ist.fenix.tools.file.dspace.ContentFileManager;
import pt.utl.ist.fenix.tools.file.dspace.DSpaceFileManager;
import pt.utl.ist.fenix.tools.file.dspace.ScormFileManager;

public class DSpaceFileManagerFactory extends FileManagerFactory {
	private static final IFileManager fileManager;
    private static final IScormFileManager scormFileManager;
    private static final IContentFileManager contentFileManager;
    
    static {
    	fileManager = new DSpaceFileManager();
    	scormFileManager = new ScormFileManager();
    	contentFileManager = new ContentFileManager();
    }

	@Override
	public final IContentFileManager getContentFileManager() {
		return contentFileManager;
	}

	@Override
	public final IFileManager getFileManager() {
		return getSimpleFileManager();
	}

	@Override
	public final IScormFileManager getScormFileManager() {
		return scormFileManager;
	}

	@Override
	public final IFileManager getSimpleFileManager() {
		return fileManager;
	}

}
