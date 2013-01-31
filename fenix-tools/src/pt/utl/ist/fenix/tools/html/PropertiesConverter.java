package pt.utl.ist.fenix.tools.html;

import java.util.Map.Entry;
import java.util.Properties;

public abstract class PropertiesConverter {

	public Properties convert(final Properties originalProperties) {
		final Properties properties = new Properties();
		for (final Entry<Object, Object> entry : originalProperties.entrySet()) {
			final String key = (String) entry.getKey();
			final String value = (String) entry.getValue();
			properties.put(key, convert(value));
		}
		return properties;
	}

	public abstract String convert(final String value);

}
