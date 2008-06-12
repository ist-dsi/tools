package pt.utl.ist.fenix.tools.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

    private static final PropertiesManager instance = new PropertiesManager();

    public static Properties loadProperties(final String fileName) throws IOException {
	final Properties properties = new Properties();
	loadProperties(properties, fileName);
	return properties;
    }

    public static void loadProperties(final Properties properties, final String fileName) throws IOException {
	final InputStream inputStream = instance.getClass().getResourceAsStream(fileName);
	if (inputStream != null) {
	    properties.load(inputStream);
	}
    }

}