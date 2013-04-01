package pt.linkare.scorm.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;

//A simple representation of the real ScormMetaData Class in the dSpaceIST Project
public class ScormMetaData {

    private static final String DEFAULT_LANG = "x-none";

    // Dublin Core qualification
    private String element = null;

    private String qualifier = null;

    private String lang = null;

    private String[] values = null;

    public ScormMetaData(String element, String qualifier, String lang, String[] values) {
        this.element = element;
        this.qualifier = qualifier;
        // NEW Putting the Lang of a Lanstring to a Default value
        // because an empty lang is considered and error
        if (lang == null || lang.trim().equalsIgnoreCase("")) {
            this.lang = ScormMetaData.DEFAULT_LANG;
        } else {
            this.lang = lang;
        }
        this.values = values;
    }

    public ScormMetaData(String element, String qualifier, String lang, String value) {
        this.element = element;
        this.qualifier = qualifier;
        // NEW Putting the Lang of a Lanstring to a Default value
        // because an empty lang is considered and error
        if (lang == null || lang.trim().equalsIgnoreCase("")) {
            this.lang = ScormMetaData.DEFAULT_LANG;
        } else {
            this.lang = lang;
        }
        this.values = new String[] { value };
    }

    // default public constructor for serialization
    public ScormMetaData() {

    }

    /**
     * @return Returns the element.
     */
    public String getElement() {
        return element;
    }

    /**
     * @param element
     *            The element to set.
     */
    public void setElement(String element) {
        this.element = element;
    }

    /**
     * @return Returns the lang.
     */
    public String getLang() {
        return lang;
    }

    /**
     * @param lang
     *            The lang to set.
     */
    public void setLang(String lang) {
        if (lang != null && !lang.trim().equalsIgnoreCase("")) {
            this.lang = lang;
        } else {
            this.lang = ScormMetaData.DEFAULT_LANG;
        }
    }

    /**
     * @return Returns the qualifier.
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * @param qualifier
     *            The qualifier to set.
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * @return Returns the values.
     */
    public String[] getValues() {
        return values;
    }

    /**
     * @param values
     *            The values to set.
     */
    public void setValues(String[] values) {
        this.values = values;
    }

    // TODO Verificr se este n dá erro!!
    public void setValue(String value) {
        Collection<String> colstr = new ArrayList<String>(1);
        colstr.add(value);
        this.values = colstr.toArray(new String[0]);
    }

    public static void print(PrintWriter out, Collection<ScormMetaData> colItemMetadata) {
        int colIndex = 0;
        out.println("HI!");
        if (colItemMetadata != null && colItemMetadata.size() > 0) {
            for (ScormMetaData itemMeta : colItemMetadata) {
                out.println("Metadata[" + (colIndex++) + "] :");
                itemMeta.print(out);
            }
        } else {
            out.println("No Dublin Core Metadata in collection");
        }
    }

    private void print(PrintWriter out) {
        out.print("Element: " + this.getElement());
        out.print(" Qualifier: " + this.getQualifier());
        out.println(" Language: " + this.getLang());
        String[] valueArray = this.getValues();
        int arrayIndex = 0;
        if (valueArray != null) {
            for (String value : valueArray) {
                out.println("   Value[" + (arrayIndex++) + "]: " + value);
            }
        } else {
            out.println("   No value defined");
        }
    }

    @Override
    public String toString() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        print(pw);

        pw.flush();
        String retVal = sw.getBuffer().toString();
        pw.close();

        return retVal;
    }
}
