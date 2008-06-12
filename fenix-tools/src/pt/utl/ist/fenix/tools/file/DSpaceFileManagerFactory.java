package pt.utl.ist.fenix.tools.file;

import java.util.Properties;

import pt.utl.ist.fenix.tools.file.dspace.ContentFileManager;
import pt.utl.ist.fenix.tools.file.dspace.DSpaceFileManager;
import pt.utl.ist.fenix.tools.file.dspace.ScormFileManager;

public class DSpaceFileManagerFactory extends FileManagerFactory {

    private static IFileManager fileManager = null;

    private static IScormFileManager scormFileManager = null;

    private static IContentFileManager contentFileManager = null;

    public static void init() {
	synchronized (fileManager) {
	    if (fileManager == null || scormFileManager == null || contentFileManager == null) {
		throw new Error(DSpaceFileManager.class.getName() + " has already been initialized.");
	    }
	    fileManager = new DSpaceFileManager();
	    scormFileManager = new ScormFileManager();
	    contentFileManager = new ContentFileManager();
	}
    }

    public static void init(final Properties properties) {
	synchronized (fileManager) {
	    if (fileManager == null || scormFileManager == null || contentFileManager == null) {
		throw new Error(DSpaceFileManager.class.getName() + " has already been initialized.");
	    }
	    fileManager = new DSpaceFileManager(properties);
	    scormFileManager = new ScormFileManager(properties);
	    contentFileManager = new ContentFileManager(properties);
	}
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
