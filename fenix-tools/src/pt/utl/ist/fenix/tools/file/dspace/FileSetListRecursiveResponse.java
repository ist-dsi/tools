package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetListRecursiveResponse implements Serializable,XMLSerializable{

    private String error;
    private FileSetDescriptor fileSetDescriptor;

    public FileSetListRecursiveResponse() {
    }

    public FileSetListRecursiveResponse(String error) {
        this.error = error;
    }

    public FileSetListRecursiveResponse(FileSetDescriptor fileSetDescriptor) {
        this.fileSetDescriptor=fileSetDescriptor;
    }

    public String getError() {
        return error;
    }

    public FileSetDescriptor getFileSetDescriptor() {
        return fileSetDescriptor;
    }

    public String toXMLString() {
        return toXML().asXML();
    	
    }

    public Element toXML()
    {
    	Element rootElement = new BaseElement("filesetlistrecursiveresponse");

        if (getError() != null) {
            rootElement.addElement("error").setText(getError());
        }
        else
        {
        	rootElement.add(fileSetDescriptor.toXML());
        }

        return rootElement;
    }
   
    public static FileSetListRecursiveResponse createFromXml(String xml) {
    	FileSetListRecursiveResponse retVal=new FileSetListRecursiveResponse();
    	retVal.fromXMLString(xml);
    	return retVal;
    }
    
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
        }
        else
        {
        	this.fileSetDescriptor=new FileSetDescriptor();
        	this.fileSetDescriptor.fromXML(xmlElement.element("filesetdescriptor"));
        }
    }

}
