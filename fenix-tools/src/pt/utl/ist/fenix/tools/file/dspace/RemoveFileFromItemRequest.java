package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class RemoveFileFromItemRequest implements Serializable, XMLSerializable {

    String uniqueId;

    public RemoveFileFromItemRequest() {
        super();
    }

    public RemoveFileFromItemRequest(String uniqueId) {
        this();
        this.uniqueId = uniqueId;
    }

    public String getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String toXMLString() {
        return toXML().asXML();
    }

    public Element toXML() {
        Element removeFileFromItemRequest = new BaseElement("removeFileFromItemRequest");
        removeFileFromItemRequest.addElement("uniqueId").setText(getUniqueId());
        return removeFileFromItemRequest;
    }

    public static RemoveFileFromItemRequest createFromXml(String xml) {
        RemoveFileFromItemRequest request = new RemoveFileFromItemRequest();
        request.fromXMLString(xml);
        return request;
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
        this.uniqueId = xmlElement.element("uniqueId").getText();
    }

}
