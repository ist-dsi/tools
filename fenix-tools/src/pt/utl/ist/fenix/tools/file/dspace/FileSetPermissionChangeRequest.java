package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class FileSetPermissionChangeRequest implements Serializable, XMLSerializable {

    // (<handle authority prefix>/<item handle>/<bitstream sequence id>)
    private FileSetDescriptor fsDescriptor;

    private Boolean privateFile;

    //Serialization support
    public FileSetPermissionChangeRequest() {

    }

    public FileSetPermissionChangeRequest(FileSetDescriptor fsDescriptor, Boolean privateFile) {
        this.fsDescriptor = fsDescriptor;
        this.privateFile = privateFile;
    }

    public FileSetDescriptor getFileSetDescriptor() {
        return this.fsDescriptor;
    }

    public Boolean isPrivateFile() {
        return this.privateFile;
    }

    @Override
    public String toXMLString() {
        return toXML().asXML();
    }

    public Element toXML() {
        Element fileSetPermissionChangeElement = new BaseElement("filesetpermissionchangerequest");

        fileSetPermissionChangeElement.addElement("privatefile").setText("" + isPrivateFile());
        fileSetPermissionChangeElement.add(fsDescriptor.toXML());

        return fileSetPermissionChangeElement;

    }

    public static FileSetPermissionChangeRequest createFromXml(String xml) {
        FileSetPermissionChangeRequest retVal = new FileSetPermissionChangeRequest();
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
        this.privateFile = Boolean.parseBoolean(xmlElement.element("privatefile").getText());
    }

}
