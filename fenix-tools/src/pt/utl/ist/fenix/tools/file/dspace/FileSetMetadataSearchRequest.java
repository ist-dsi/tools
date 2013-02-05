package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetMetadataSearchRequest implements Serializable, XMLSerializable {

    private FilesetMetadataQuery query;
    private VirtualPath optionalVirtualPath;

    //Serialization support
    public FileSetMetadataSearchRequest() {

    }

    public FileSetMetadataSearchRequest(FilesetMetadataQuery query) {
        this.query = query;
    }

    public FileSetMetadataSearchRequest(FilesetMetadataQuery query, VirtualPath optionalVirtualPath) {
        this.query = query;
        this.optionalVirtualPath = optionalVirtualPath;
    }

    /**
     * @return the optionalVirtualPath
     */
    public VirtualPath getOptionalVirtualPath() {
        return optionalVirtualPath;
    }

    public FilesetMetadataQuery getQuery() {
        return this.query;
    }

    @Override
    public String toXMLString() {
        return toXML().asXML();
    }

    public Element toXML() {
        Element fileSetMetaSearchElement = new BaseElement("filesetmetadatasearchrequest");

        fileSetMetaSearchElement.add(query.toXML());
        if (optionalVirtualPath != null) {
            fileSetMetaSearchElement.add(optionalVirtualPath.toXML());
        }

        return fileSetMetaSearchElement;

    }

    public static FileSetMetadataSearchRequest createFromXml(String xml) {
        FileSetMetadataSearchRequest retVal = new FileSetMetadataSearchRequest();
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
        this.query = new FilesetMetadataQuery();
        query.fromXML(xmlElement.element("filesetmetadataquery"));
        if (xmlElement.element("virtualpath") != null) {
            this.optionalVirtualPath = new VirtualPath();
            optionalVirtualPath.fromXML(xmlElement.element("virtualpath"));
        }
    }
}
