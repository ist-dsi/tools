package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetUploadRequest implements Serializable, XMLSerializable {

	private VirtualPath path;

	private String originalFilename;

	private Boolean privateFile;

	private FileSet fileSet;

	//Serialization support
	public FileSetUploadRequest() {
	}

	public FileSetUploadRequest(VirtualPath path, String originalFilename, boolean privateFile, FileSet fileSet) {
		this.path = path;
		this.originalFilename = originalFilename;
		this.privateFile = privateFile;
		this.fileSet = fileSet;
	}

	public VirtualPath getPath() {
		return this.path;
	}

	public String getOriginalFilename() {
		return this.originalFilename;
	}

	public FileSet getFileSet() {
		return fileSet;
	}

	public boolean isPrivateFile() {
		return privateFile;
	}

	@Override
	public String toXMLString() {
		return toXML().asXML();
	}

	public static FileSetUploadRequest createFromXml(String xml) {
		FileSetUploadRequest retVal = new FileSetUploadRequest();
		retVal.fromXMLString(xml);
		return retVal;
	}

	@Override
	public void fromXMLString(String xml) {
		try {
			fromXML(DocumentHelper.parseText(xml).getRootElement());
		} catch (DocumentException e) {
			throw new RuntimeException("Error parsing xml string : " + xml, e);
		}
	}

	public Element toXML() {
		Element rootElement = new BaseElement("filesetuploadrequest");
		rootElement.add(getPath().toXML());
		if (getFileSet().getItemHandle() != null) {
			rootElement.addElement("itemHandle").setText(getFileSet().getItemHandle());
		}
		rootElement.addElement("filename").setText(getOriginalFilename());
		rootElement.addElement("privatefile").setText("" + isPrivateFile());
		rootElement.add(getFileSet().toXML());
		return rootElement;
	}

	public void fromXML(Element xmlElement) {
		this.path = new VirtualPath();
		this.path.fromXML(xmlElement.element("virtualpath"));
		this.fileSet = new FileSet();
		if (xmlElement.elementText("itemHandle") != null) {
			this.fileSet.setItemHandle(xmlElement.elementText("itemHandle"));
		}
		this.fileSet.fromXML(xmlElement.element("fileset"));
		this.originalFilename = xmlElement.element("filename").getText();
		this.privateFile = Boolean.parseBoolean(xmlElement.element("privatefile").getText());
	}

}
