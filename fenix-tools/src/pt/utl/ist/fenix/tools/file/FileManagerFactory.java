package pt.utl.ist.fenix.tools.file;

import java.util.Properties;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

public abstract class FileManagerFactory {

    private static FileManagerFactory concreteFactory;

    public static void init() {
	final Properties properties;
	try {
	    properties = PropertiesManager.loadProperties("/FileManagerConfiguration.properties");
	} catch (Exception e) {
	    throw new RuntimeException("Error loading file manager implementation", e);
	}
	init(properties);
    }

    public static void init(final Properties properties) {
	final String classname = properties.getProperty("file.manager.factory.implementation.class");
	init(classname);
    }

    public synchronized static void init(final String classname) {
	if (concreteFactory != null) {
	    throw new Error(FileManagerFactory.class.getName() + " already initialized.");
	}
	try {
	    final Class fileManagerImplementationClass = Class.forName(classname);
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
