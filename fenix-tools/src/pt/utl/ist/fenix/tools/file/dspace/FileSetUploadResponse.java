package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetUploadResponse implements Serializable, XMLSerializable {

    private String error;

    private FileSetDescriptor fileSetDescriptor;

    public FileSetUploadResponse() {
    }

    public FileSetUploadResponse(String error) {
        this.error = error;
        this.fileSetDescriptor = null;
    }

    public FileSetUploadResponse(FileSetDescriptor fileSetDescriptor) {
        this.error = null;
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
        Element rootElement = new BaseElement("filesetuploadresponse");

        if (this.error != null) {
            rootElement.addElement("error").setText(this.error);
        } else {
            rootElement.add(getFileSetDescriptor().toXML());
        }

        return rootElement;
    }

    public static FileSetUploadResponse createFromXml(String xml) {
        FileSetUploadResponse retVal = new FileSetUploadResponse();
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
            fileSetDescriptor.fromXML(xmlElement.element("filesetdescriptor"));
        }
    }

}
