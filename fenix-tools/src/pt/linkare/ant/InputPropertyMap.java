package pt.linkare.ant;

import java.util.ArrayList;
import java.util.Collection;

public class InputPropertyMap extends ArrayList<InputProperty> {

    public InputPropertyMap(int initialCapacity) {
        super(initialCapacity);
    }

    public InputPropertyMap() {
        super();
    }

    public void put(InputProperty value) {
        super.add(value);
    }

    public InputProperty get(String propertyName) {
        for (InputProperty p : this) {
            if (p.getPropertyName().equals(propertyName)) {
                return p;
            }
        }

        return null;
    }

    public void putAll(Collection<InputProperty> propertiesList) {
        for (InputProperty prop : propertiesList) {
            put(prop);
        }
    }

}
