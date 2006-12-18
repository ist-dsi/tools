package pt.utl.ist.fenix.tools.file;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

/**
 * 
 * @author naat
 * 
 */

public class VirtualPathNode implements Serializable,XMLSerializable{
    
	private String name;

    private String description;

    public VirtualPathNode() {
    }

    public VirtualPathNode(String name, String description) {
        super();
        this.name = name;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    //XXX Óscar Ferreira - Linkare TI
	public String toXMLString() {
		return toXML().asXML();
	}

	public Element toXML() {		
        Element xmlElement=new BaseElement("virtualpathnode");
        xmlElement.addElement("name").setText(getName());
        xmlElement.addElement("description").setText(getDescription());
        
		return xmlElement;
	}

	public void fromXMLString(String xml) {
		try {
			fromXML(DocumentHelper.parseText(xml).getRootElement());
		}
		catch (DocumentException e) {
			throw new RuntimeException("Error parsing xml string : "+xml,e);
		}		
	}

	public void fromXML(Element xmlElement) {
		Element nameElement=xmlElement.element("name");
        if(nameElement!=null)
        {
            this.name=(String)nameElement.getData();
        }
        Element descriptionElement=xmlElement.element("description");
        if(descriptionElement!=null)
        {
            this.description=(String)descriptionElement.getData();
        }
	}


}
