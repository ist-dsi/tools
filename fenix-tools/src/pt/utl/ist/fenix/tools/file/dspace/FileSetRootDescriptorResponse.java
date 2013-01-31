package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetRootDescriptorResponse implements Serializable, XMLSerializable {

	private String error;
	private FileSetDescriptor fileSetDescriptor;

	public FileSetRootDescriptorResponse() {
	}

	public FileSetRootDescriptorResponse(String error) {
		this.error = error;
	}

	public FileSetRootDescriptorResponse(FileSetDescriptor fileSetDescriptor) {
		this.fileSetDescriptor = fileSetDescriptor;
	}

	public String getError() {
		return error;
	}

	public FileSetDescriptor getFileSetDescriptor() {
		return fileSetDescriptor;
	}

	@Override
	public String toXMLString() {
		return toXML().asXML();

	}

	public Element toXML() {
		Element rootElement = new BaseElement("filesetrootdescriptorresponse");

		if (getError() != null) {
			rootElement.addElement("error").setText(getError());
		} else {
			rootElement.add(fileSetDescriptor.toXML());
		}

		return rootElement;
	}

	public static FileSetRootDescriptorResponse createFromXml(String xml) {
		FileSetRootDescriptorResponse retVal = new FileSetRootDescriptorResponse();
		retVal.fromXMLString(xml);
		return retVal;
	}

	@Override
	public void fromXMLString(String xml) {
		try {
			fromXML(DocumentHelper.parseText(xml).getRootElement());
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	public void fromXML(Element xmlElement) {

		Element errorElement = xmlElement.element("error");
		if (errorElement != null) {
			this.error = errorElement.getText();
		} else {
			this.fileSetDescriptor = new FileSetDescriptor();
			this.fileSetDescriptor.fromXML(xmlElement.element("filesetdescriptor"));
		}
	}

}
