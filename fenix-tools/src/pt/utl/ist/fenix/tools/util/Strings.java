package pt.utl.ist.fenix.tools.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
 
    public String exportAsString() {
	StringBuilder buffer = new StringBuilder("");
	for (String string : this) {
	    buffer.append(string.length());
	    buffer.append(":");
	    buffer.append(string);
	}
	return buffer.toString();
    }

    public static Strings importFromString(String string) {
	if (string == null) {
	    return null;
	}
	List<String> strings = new ArrayList<String>();

	int beginIndex = 0;
	int endIndex = find(beginIndex, ':', string);

	while (beginIndex >= 0 && endIndex > beginIndex) {
	    int size = Integer.valueOf(string.substring(beginIndex, endIndex++));
	    strings.add(string.substring(endIndex, endIndex + size));
	    beginIndex = endIndex + size;
	    endIndex = beginIndex + find(beginIndex, ':', string);
	}
	return new Strings(strings);
    }

    private static int find(int index, char c, String string) {
	return string.substring(index).indexOf(c);
    }

    public String getPresentationString() {
	final StringBuilder builder = new StringBuilder();
	for (final String string : this) {
	    if (builder.length() > 0) {
		builder.append(", ");
	    }
	    builder.append(string);
	}
	return builder.toString();
    }

}
