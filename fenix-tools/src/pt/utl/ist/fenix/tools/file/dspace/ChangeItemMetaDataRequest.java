package pt.utl.ist.fenix.tools.file.dspace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.XMLSerializable;

public class ChangeItemMetaDataRequest implements Serializable, XMLSerializable {

	String itemHandler;
	Collection<FileSetMetaData> metaData;

	public ChangeItemMetaDataRequest() {
		super();
	}

	public ChangeItemMetaDataRequest(String itemHandler, Collection<FileSetMetaData> metaData) {
		this();
		this.itemHandler = itemHandler;
		this.metaData = metaData;
	}

	public String getItemHandler() {
		return itemHandler;
	}

	public void setItemHandler(String itemHandler) {
		this.itemHandler = itemHandler;
	}

	public Collection<FileSetMetaData> getMetaData() {
		return metaData;
	}

	private void addMetaDataElement(FileSetMetaData metaDataElement) {
		if (this.metaData == null) {
			this.metaData = new ArrayList<FileSetMetaData>();
		}
		this.metaData.add(metaDataElement);
	}

	public void setMetaData(Collection<FileSetMetaData> metaData) {
		this.metaData = metaData;
	}

	@Override
	public String toXMLString() {
		return toXML().asXML();
	}

	public Element toXML() {
		Element fileSetPermissionChangeElement = new BaseElement("itemMetaDataRequest");

		fileSetPermissionChangeElement.addElement("itemHandler").setText(getItemHandler());
		Element metaDataElement = fileSetPermissionChangeElement.addElement("metaData");
		for (FileSetMetaData metaData : getMetaData()) {
			metaDataElement.add(metaData.toXML());
		}

		return fileSetPermissionChangeElement;

	}

	public static ChangeItemMetaDataRequest createFromXml(String xml) {
		ChangeItemMetaDataRequest request = new ChangeItemMetaDataRequest();
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
		this.itemHandler = xmlElement.element("itemHandler").getText();
		for (Element element : (List<Element>) xmlElement.element("metaData").elements("filesetmetadata")) {
			FileSetMetaData metaDataElement = new FileSetMetaData();
			metaDataElement.fromXML(element);
			addMetaDataElement(metaDataElement);
		}
	}

}
