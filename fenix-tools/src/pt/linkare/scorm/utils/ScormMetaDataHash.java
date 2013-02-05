/**
 * 
 */
package pt.linkare.scorm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Óscar Ferreira - LINKARE TI
 * 
 */
public class ScormMetaDataHash {

    private HashMap<MultiStringKey, Collection<String>> theMap = new HashMap<MultiStringKey, Collection<String>>();

    public void put(String element, String qualifier, String lang, String value) {
        put(element, qualifier, lang, new String[] { value });
    }

    public void put(String element, String qualifier, String lang, String[] values) {
        MultiStringKey path = new MultiStringKey(element, qualifier, lang);

        if (theMap.get(path) != null) {
            theMap.get(path).addAll(new ArrayList<String>(Arrays.asList(values)));
        } else {
            theMap.put(path, new ArrayList<String>(Arrays.asList(values)));
        }

    }

    public Collection<ScormMetaData> listScormMetaData() {
        if (this.theMap.size() == 0) {
            return null;
        }

        ArrayList<ScormMetaData> retVal = new ArrayList<ScormMetaData>(this.theMap.size());
        for (MultiStringKey key : this.theMap.keySet()) {
            String element = key.getKey(0);
            String qualifier = key.getKey(1);
            String lang = key.getKey(2);
            String[] values = this.theMap.get(key).toArray(new String[0]);
            retVal.add(new ScormMetaData(element, qualifier, lang, values));
        }

        return retVal;
    }

    public void populateFromScormMetaData(Collection<ScormMetaData> metaDataCollection) {
        for (ScormMetaData current : metaDataCollection) {
            this.put(current.getElement(), current.getQualifier(), current.getLang(), current.getValues());
        }
    }

    public String[] getValues(String element, String qualifier, String lang) {
        return getValues(new MultiStringKey(element, qualifier, lang));
    }

    public String[] getValues(MultiStringKey key) {
        if (key != null) {
            final Collection<String> object = theMap.get(key);
            return object == null ? null : object.toArray(new String[0]);
        }
        return null;
    }

    public Set<MultiStringKey> keySet() {
        return theMap.keySet();
    }

    public Set<MultiStringKey> keySetWithElementAndQualifier(String element, String qualifier) {
        if (element != null && qualifier != null) {
            Set<MultiStringKey> all = keySet();
            HashSet<MultiStringKey> keysReturn = new HashSet<MultiStringKey>();
            for (MultiStringKey current : all) {
                if (element.equals(current.getKey(0)) && qualifier.equals(current.getKey(1))) {
                    keysReturn.add(current);
                }
            }

            return keysReturn;
        }
        return null;
    }

    public Set<MultiStringKey> keySetWithElement(String element) {
        if (element != null) {
            Set<MultiStringKey> all = keySet();
            HashSet<MultiStringKey> keysReturn = new HashSet<MultiStringKey>();
            for (MultiStringKey current : all) {
                if (element.equals(current.getKey(0))) {
                    keysReturn.add(current);
                }
            }

            return keysReturn;
        }
        return null;
    }

}
