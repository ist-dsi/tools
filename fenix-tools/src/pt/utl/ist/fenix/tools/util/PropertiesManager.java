package pt.utl.ist.fenix.tools.util;

import java.io.File;
import java.io.FileInputStream;
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

	public static Properties loadPropertiesFromFile(final String filePath) throws IOException {
		final Properties properties = new Properties();
		loadPropertiesFromFile(properties, filePath);
		return properties;
	}

	public static void loadProperties(final Properties properties, final String fileName) throws IOException {
		final InputStream inputStream = instance.getClass().getResourceAsStream(fileName);
		if (inputStream != null) {
			properties.load(inputStream);
		}
	}

	public static void loadPropertiesFromFile(final Properties properties, final String filePath) throws IOException {
		final File file = new File(filePath);
		final InputStream inputStream = new FileInputStream(file);
		if (inputStream != null) {
			properties.load(inputStream);
		}
	}
}