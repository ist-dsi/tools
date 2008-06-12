package pt.utl.ist.fenix.tools.file;

import java.util.Properties;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

public abstract class FileManagerFactory {
    private static final FileManagerFactory concreteFactory;
    
	    static {
	    	 try {
	             final Properties properties = PropertiesManager
	                     .loadProperties("/FileManagerConfiguration.properties");
	             final Class fileManagerImplementationClass = Class.forName(properties
	             .getProperty("file.manager.factory.implementation.class"));
	             concreteFactory = (FileManagerFactory) fileManagerImplementationClass.newInstance();
	         } catch (Exception e) {
	             throw new RuntimeException("Error loading file manager implementation", e);
	         }
    }

	public static FileManagerFactory getFactoryInstance() {
		return concreteFactory;
	}
	
	public abstract IFileManager getFileManager();
    public abstract IFileManager getSimpleFileManager();
    public abstract IScormFileManager getScormFileManager();
    public abstract IContentFileManager getContentFileManager();
    
}
