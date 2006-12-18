package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetRetrieveResponse implements Serializable,XMLSerializable{

    private String error;
    private FileSet fileSet;

    public FileSetRetrieveResponse() {
    }

    public FileSetRetrieveResponse(String error) {
        this.error = error;
    }

    public FileSetRetrieveResponse(FileSet fileSet) {
        this.fileSet=fileSet;
    }

    public String getError() {
        return error;
    }

    public FileSet getFileSet() {
        return fileSet;
    }

    public String toXMLString() {
        return toXML().asXML();
    	
    }

    public Element toXML()
    {
    	Element rootElement = new BaseElement("filesetretrieveresponse");

        if (getError() != null) {
            rootElement.addElement("error").setText(getError());
        }
        else
        {
        	rootElement.add(fileSet.toXML());
        }

        return rootElement;
    }
   
    public static FileSetRetrieveResponse createFromXml(String xml) {
    	FileSetRetrieveResponse retVal=new FileSetRetrieveResponse();
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
        	this.fileSet=new FileSet();
        	this.fileSet.fromXML(xmlElement.element("fileset"));
        }
    }

}
