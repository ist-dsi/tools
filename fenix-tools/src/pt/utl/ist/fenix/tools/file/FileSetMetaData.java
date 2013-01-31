package pt.utl.ist.fenix.tools.file;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.linkare.scorm.utils.ScormMetaData;

/**
 * This class is an abstraction around the concept of metadata
 * It clearly uses the Dublin Core approach to MetaData which will
 * be enough for now
 * 
 * @author Jos� Pedro Pereira - Linkare TI
 * 
 */
@SuppressWarnings("serial")
public class FileSetMetaData implements Serializable, XMLSerializable {

	/**
	 * Dublin Core qualification "element"
	 */
	private String element = null;

	/**
	 * Dublin Core qualification "qualifier"
	 */
	private String qualifier = null;

	/**
	 * Dublin Core qualification "lang"
	 */
	private String lang = null;

	/**
	 * Dublin Core qualification "values"
	 */
	private String[] values = null;

	/**
	 * 
	 * @param element The Dublin Core element
	 * @param qualifier The Dublin Core qualifier
	 * @param lang The Language
	 * @param values The array of values
	 */
	public FileSetMetaData(String element, String qualifier, String lang, String[] values) {
		this.element = element;
		this.qualifier = qualifier;
		this.lang = lang;
		this.values = values;
	}

	/**
	 * Shortcut constructor @see FileSetMetaData#FileSetMetaData(String, String, String, String[])
	 * 
	 * @param element
	 * @param qualifier
	 * @param lang
	 * @param value
	 */
	public FileSetMetaData(String element, String qualifier, String lang, String value) {
		this.element = element;
		this.qualifier = qualifier;
		this.lang = lang;
		this.values = new String[] { value };
	}

	/**
	 * default public constructor for serialization
	 */
	public FileSetMetaData() {

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
		this.lang = lang;
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

	public static void print(PrintWriter out, Collection<FileSetMetaData> colItemMetadata) {
		int colIndex = 0;
		if (colItemMetadata != null && colItemMetadata.size() > 0) {
			for (FileSetMetaData itemMeta : colItemMetadata) {
				out.println("Metadata[" + (colIndex++) + "] :");
				itemMeta.print(out);
			}
		} else {
			out.println("No FileSet Metadata in collection");
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

	/**
	 * Factory method for commonly used "author" metainfo
	 * 
	 * @param name The name of the author
	 * @return A FileSetMetaData correctly specified for DublinCore classification
	 */
	public static FileSetMetaData createAuthorMeta(String name) {
		return new FileSetMetaData("contributor", "author", null, name);
	}

	/**
	 * Factory method for commonly used "author" metainfo
	 * 
	 * @param lang The language for the authorship
	 * @param name The name of the author
	 * @return A FileSetMetaData correctly specified for DublinCore classification
	 */
	public static FileSetMetaData createAuthorMeta(String lang, String name) {
		return new FileSetMetaData("contributor", "author", lang, name);
	}

	/**
	 * Factory method for commonly used "title" metainfo
	 * 
	 * @param lang The language for the authorship
	 * @param title The title for the content
	 * @return A FileSetMetaData correctly specified for DublinCore classification
	 */
	public static FileSetMetaData createTitleMeta(String lang, String title) {
		return new FileSetMetaData("title", null, lang, title);
	}

	/**
	 * Factory method for commonly used "title" metainfo
	 * 
	 * @param title The title for the content
	 * @return A FileSetMetaData correctly specified for DublinCore classification
	 */
	public static FileSetMetaData createTitleMeta(String title) {
		return new FileSetMetaData("title", null, null, title);
	}

	public static FileSetMetaData createWidthMeta(int width) {
		return new FileSetMetaData("width", null, null, "" + width);
	}

	public static FileSetMetaData createHeightMeta(int height) {
		return new FileSetMetaData("height", null, null, "" + height);
	}

	public static FileSetMetaData[] createWidthHeightMeta(int width, int height) {
		return new FileSetMetaData[] { createWidthMeta(width), createHeightMeta(height) };
	}

	public static Collection<FileSetMetaData> createFileSetMetaDatasFromScormMetaDatas(Collection<ScormMetaData> scormMetaDatas) {
		if (scormMetaDatas == null) {
			return null;
		}
		ArrayList<FileSetMetaData> retVal = new ArrayList<FileSetMetaData>(scormMetaDatas.size());
		for (ScormMetaData current : scormMetaDatas) {
			retVal.add(createFileSetMetaDataFromScormMetaData(current));
		}
		return retVal;
	}

	public static FileSetMetaData createFileSetMetaDataFromScormMetaData(ScormMetaData scormMetaData) {
		if (scormMetaData == null) {
			return null;
		}

		return new FileSetMetaData(scormMetaData.getElement(), scormMetaData.getQualifier(), scormMetaData.getLang(),
				scormMetaData.getValues());
	}

	/**
	 * Auto generated javadoc
	 * 
	 * @see pt.utl.ist.fenix.tools.file.XMLSerializable#toXMLString()
	 */
	@Override
	public String toXMLString() {
		return toXML().asXML();
	}

	public Element toXML() {
		Element xmlElement = new BaseElement("filesetmetadata");
		if (getElement() != null) {
			Element elementElement = xmlElement.addElement("element");
			elementElement.setText(getElement());
		}
		if (getQualifier() != null) {
			Element qualifierElement = xmlElement.addElement("qualifier");
			qualifierElement.setText(getQualifier());
		}
		if (getLang() != null) {
			Element langElement = xmlElement.addElement("lang");
			langElement.setText(getLang());
		}
		if (getValues() != null) {
			Element valuesElement = xmlElement.addElement("values");
			int count = 0;
			for (String value : getValues()) {
				if (value != null) {
					Element valueElement = valuesElement.addElement("value" + count++);
					valueElement.setText(value);
				}
			}
		}
		return xmlElement;
	}

	@SuppressWarnings("unchecked")
	public void fromXML(Element elem) {
		Element elementElement = elem.element("element");
		if (elementElement != null) {
			this.setElement(elementElement.getText());
		}
		Element qualifierElement = elem.element("qualifier");
		if (qualifierElement != null) {
			this.setQualifier(qualifierElement.getText());
		}
		Element langElement = elem.element("lang");
		if (langElement != null) {
			this.setLang(langElement.getText());
		}
		Element valuesElement = elem.element("values");
		if (valuesElement != null) {
			List<Element> valueElements = valuesElement.elements();
			String[] valuesAux = new String[valueElements.size()];
			int count = 0;
			for (Element valueElement : valueElements) {
				valuesAux[count++] = valueElement.getText();
			}
			this.setValues(valuesAux);
		}

	}

	/**
	 * Auto generated javadoc
	 * 
	 * @see pt.utl.ist.fenix.tools.file.XMLSerializable#fromXMLString(java.lang.String)
	 */
	@Override
	public void fromXMLString(String xml) {
		try {
			fromXML(DocumentHelper.parseText(xml).getRootElement());
		} catch (DocumentException e) {
			throw new RuntimeException("Error parsing xml string : " + xml, e);
		}
	}

	public static FileSetMetaData buildFromXMLString(String xmlString) {
		FileSetMetaData metadata = new FileSetMetaData();
		metadata.fromXMLString(xmlString);
		return metadata;
	}
}
