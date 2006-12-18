package pt.utl.ist.fenix.tools.file;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

public class VirtualPath implements Serializable,XMLSerializable{

    private List<VirtualPathNode> nodes;

    public VirtualPath() {
        this.nodes = new ArrayList<VirtualPathNode>();
    }

    public VirtualPath addNode(int index, VirtualPathNode node) {
        this.nodes.add(index, node);
        return this;
    }

    public VirtualPath addNode(VirtualPathNode node) {
        this.nodes.add(node);
        return this;
    }

    public List<VirtualPathNode> getNodes() {
        return this.nodes;
    }

    //Óscar Ferreira - Linkare TI
	public String toXMLString() {
		return toXML().asXML();
	}

	public void fromXMLString(String xml) {
		try {
			fromXML(DocumentHelper.parseText(xml).getRootElement());
		}
		catch (DocumentException e) {
			throw new RuntimeException("Error parsing xml string : "+xml,e);
		}		
	}

	public Element toXML()
	{
		Element xmlElement=new BaseElement("virtualpath");
        if(getNodes()!=null)
        {
            Element nodesElement=xmlElement.addElement("nodes");
            for(VirtualPathNode vNode:getNodes())
            {
                nodesElement.add(vNode.toXML());
            }           
        }
		return xmlElement;
	}
		
    @SuppressWarnings("unchecked")
	public void fromXML(Element xmlElement)
	{
		Element nodesElement=xmlElement.element("nodes");
        if(nodesElement!=null)
        {
            List<Element> vPathNodeElements=(List<Element>)nodesElement.elements("virtualpathnode");
            for(Element vPathNodeElement:vPathNodeElements)
            {
            	VirtualPathNode vPathNode=new VirtualPathNode();
            	vPathNode.fromXML(vPathNodeElement);
                this.addNode(vPathNode);
            }
        }
	}

}
