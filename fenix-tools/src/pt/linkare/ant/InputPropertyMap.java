package pt.linkare.ant;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class InputPropertyMap extends HashMap<String, InputProperty> {

	public InputPropertyMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	public InputPropertyMap(int initialCapacity) {
		super(initialCapacity);
	}

	public InputPropertyMap() {
		super();
	}

	public InputPropertyMap(Map<String,InputProperty> m) {
		super(m);
	}

	public InputProperty put(InputProperty value) {
		return super.put(value.getPropertyName(), value);
		
	}

	public InputProperty get(String propertyName)
	{
		return super.get(propertyName);
	}

	public void putAll(Collection<InputProperty> propertiesList) {
		for(InputProperty prop:propertiesList)
			put(prop);
	}

}
