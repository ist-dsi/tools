package pt.utl.ist.fenix.tools.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyBeutifier {

	public static void main(String[] args) {
		final String resourceDir = "/home/marvin/workspace/fenix_head/config/resources";
		final String enResource = resourceDir + "/HtmlAltResources_en.properties";
		final String ptResource = resourceDir + "/HtmlAltResources_pt.properties";

		try {
			final Properties originalProperties = loadProperties(enResource);
			final Properties enProperties = beutify(originalProperties);
			final Properties ptProperties = translate(enProperties);
			writeProperties("/tmp/HtmlAltResources_en.properties", enProperties);
			writeProperties("/tmp/HtmlAltResources_pt.properties", ptProperties);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	private static Properties loadProperties(final String filename) throws IOException {
		final File file = new File(filename);
		final Properties properties = new Properties();
		if (file.exists()) {
			final FileInputStream fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
			fileInputStream.close();
		}
		return properties;
	}

	private static void writeProperties(String filename, Properties properties) throws IOException {
		final File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		final FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		properties.store(fileOutputStream, null);
		fileOutputStream.close();
	}

	private static Properties beutify(final Properties originalProperties) {
		final PropertiesBeutifier propertiesBeutifier = new PropertiesBeutifier();
		return propertiesBeutifier.convert(originalProperties);
	}

	private static Properties translate(Properties originalProperties) {
		final PropertiesTranslator propertiesTranslator = new PropertiesTranslator();
		return propertiesTranslator.convert(originalProperties);
	}

}
