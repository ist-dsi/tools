package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetQueryResults;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetMetadataSearchResponse implements Serializable, XMLSerializable {

    private String error;
    private FileSetQueryResults results;

    public FileSetMetadataSearchResponse() {
    }

    public FileSetMetadataSearchResponse(String error) {
        this.error = error;
    }

    public FileSetMetadataSearchResponse(FileSetQueryResults results) {
        this.results = results;
    }

    public String getError() {
        return error;
    }

    /**
     * @return the results
     */
    public FileSetQueryResults getResults() {
        return results;
    }

    @Override
    public String toXMLString() {
        return toXML().asXML();

    }

    public Element toXML() {
        Element rootElement = new BaseElement("filesetmetadatasearchresponse");

        if (getError() != null) {
            rootElement.addElement("error").setText(getError());
        } else {
            rootElement.add(results.toXML());
        }

        return rootElement;
    }

    public static FileSetMetadataSearchResponse createFromXml(String xml) {
        FileSetMetadataSearchResponse retVal = new FileSetMetadataSearchResponse();
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
            this.results = new FileSetQueryResults();
            this.results.fromXML(xmlElement.element("filesetqueryresults"));
        }
    }

}
