package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetRetrieveRequest implements Serializable, XMLSerializable {

    // (<handle authority prefix>/<item handle>/<bitstream sequence id>)
    private FileSetDescriptor fsDescriptor;

    //Serialization support
    public FileSetRetrieveRequest() {

    }

    public FileSetRetrieveRequest(FileSetDescriptor fsDescriptor) {
        this.fsDescriptor = fsDescriptor;
    }

    public FileSetDescriptor getFileSetDescriptor() {
        return this.fsDescriptor;
    }

    @Override
    public String toXMLString() {
        return toXML().asXML();
    }

    public Element toXML() {
        Element fileSetPermissionChangeElement = new BaseElement("filesetretrieverequest");

        fileSetPermissionChangeElement.add(fsDescriptor.toXML());

        return fileSetPermissionChangeElement;

    }

    public static FileSetRetrieveRequest createFromXml(String xml) {
        FileSetRetrieveRequest retVal = new FileSetRetrieveRequest();
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
        this.fsDescriptor = new FileSetDescriptor();
        fsDescriptor.fromXML(xmlElement.element("filesetdescriptor"));
    }

}
