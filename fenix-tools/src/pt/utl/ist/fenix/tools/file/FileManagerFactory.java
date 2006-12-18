package pt.utl.ist.fenix.tools.file;

import java.util.Properties;

import pt.utl.ist.fenix.tools.util.PropertiesManager;

public class FileManagerFactory {
    private static final IFileManager fileManager;

    static {
        try {
            final Properties properties = PropertiesManager
                    .loadProperties("/FileManagerConfiguration.properties");
            final Class fileManagerImplementationClass = Class.forName(properties
            .getProperty("filemanager.implementation.class"));
            fileManager = (IFileManager) fileManagerImplementationClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Error loading file manager implementation", e);
        }

    }

    public static final IFileManager getFileManager() {
        return fileManager;
    }

}
