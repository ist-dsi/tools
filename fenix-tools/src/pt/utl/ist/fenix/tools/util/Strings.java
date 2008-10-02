package pt.utl.ist.fenix.tools.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class Strings extends ObjectList<String> implements Serializable {

    public Strings(Collection<String> strings) {
	super(strings);
    }
    
    public Strings(String[] strings) {
	this(Arrays.asList(strings));
    }
    
    public Strings(String string) {
	this(Collections.singleton(string));
    }

    public boolean hasString(String string) {
	return contains(string);
    }
    
    public boolean hasStringIgnoreCase(String string) {
	for (String stringInCollection : this) {
	    if (stringInCollection.equalsIgnoreCase(string)) {
		return true;
	    }
	}
	return false;
    }
    
}
