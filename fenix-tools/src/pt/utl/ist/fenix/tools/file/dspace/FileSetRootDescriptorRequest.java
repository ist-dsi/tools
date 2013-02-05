package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetRootDescriptorRequest implements Serializable, XMLSerializable {

    private FileSetDescriptor fsDescriptor;

    //Serialization support
    public FileSetRootDescriptorRequest() {

    }

    public FileSetRootDescriptorRequest(FileSetDescriptor fsDescriptor) {
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
        Element fileSetDeleteElement = new BaseElement("filesetrootdescriptorrequest");

        fileSetDeleteElement.add(fsDescriptor.toXML());

        return fileSetDeleteElement;

    }

    public static FileSetRootDescriptorRequest createFromXml(String xml) {
        FileSetRootDescriptorRequest retVal = new FileSetRootDescriptorRequest();
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
