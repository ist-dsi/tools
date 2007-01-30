package pt.utl.ist.fenix.tools.file;

import java.io.Serializable;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

public class FileDescriptor implements Serializable,XMLSerializable {

    private String filename;
    
    private String originalAbsoluteFilePath;

    private String mimeType;

    private String checksum;

    private String checksumAlgorithm;

    private int size;

    private String uniqueId;
    
    private String directDownloadUrl=null;

    
    /**
     * @return Returns the item Handler
     * 
     */
    public String getItemStorageId() {
    	return uniqueId.substring(0,uniqueId.lastIndexOf("/"));
    }
    
    /**
	 * @return Returns the directDownloadUrl.
	 */
	public String getDirectDownloadUrl() {
		return directDownloadUrl;
	}

	/**
	 * @param directDownloadUrl The directDownloadUrl to set.
	 */
	public void setDirectDownloadUrl(String directDownloadUrl) {
		this.directDownloadUrl = directDownloadUrl;
	}

	//default constructor for serialization
	public FileDescriptor()
	{
		
	}
	public FileDescriptor(String originalAbsoluteFilePath,String filename, String mimeType, String checksum, String checksumAlgorithm,
            Integer size, String uniqueId) {
		this.originalAbsoluteFilePath=originalAbsoluteFilePath;
        this.filename = filename;
        this.mimeType = mimeType;
        this.checksum = checksum;
        this.checksumAlgorithm = checksumAlgorithm;
        this.size = size;
        this.uniqueId = uniqueId;
    }

    /**
     * The file checksum
     * 
     * @return The checksum for the file
     */
    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    /**
     * The algorithm used to calculate the checksum
     * 
     * @return The algorithm for checksum calculation
     */
    public String getChecksumAlgorithm() {
        return checksumAlgorithm;
    }

    public void setChecksumAlgorithm(String checksumAlgorithm) {
        this.checksumAlgorithm = checksumAlgorithm;
    }

    /**
     * The filename only
     * 
     * @return The name of the file
     */
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * The mime type
     * 
     * @return The mimetype of the file
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * The file size
     * 
     * @return the size of the file in bytes
     */
    public int getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    /**
     * An unique Id, i.e., an identification that allows direct file access. On
     * local disk can be the full system path to file (/a/b/c/d/e/theFile.jpg).
     * On other content repositories will typically be the path (or equivalent)
     * to the file.
     * 
     * @return the unique id for file access
     */
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

	/**
	 * @return Returns the originalAbsoluteFilePath.
	 */
	public String getOriginalAbsoluteFilePath() {
		return originalAbsoluteFilePath;
	}

	/**
	 * @param originalAbsoluteFilePath The originalAbsoluteFilePath to set.
	 */
	public void setOriginalAbsoluteFilePath(String originalAbsoluteFilePath) {
		this.originalAbsoluteFilePath = originalAbsoluteFilePath;
	}

	
	
	public Element toXML() {
		Element xmlElement=new BaseElement("filedescriptor");
		if(getOriginalAbsoluteFilePath()!=null)
			xmlElement.addElement("originalabsolutefilepath").setText(getUniqueId());
		if(getUniqueId()!=null)
			xmlElement.addElement("uniqueid").setText(getUniqueId());
		if(getChecksum()!=null)
			xmlElement.addElement("checksum").setText(getChecksum());
		if(getChecksumAlgorithm()!=null)
			xmlElement.addElement("checksumAlg").setText(getChecksumAlgorithm());
		if(getDirectDownloadUrl()!=null)
			xmlElement.addElement("directdownloadurl").setText(getDirectDownloadUrl());
		if(getFilename()!=null)
			xmlElement.addElement("filename").setText(getFilename());
		if(getMimeType()!=null)
			xmlElement.addElement("mimetype").setText(getMimeType());
		
		xmlElement.addElement("size").setText(Long.valueOf(getSize()).toString());
		
		return xmlElement;
	}
	public String toXMLString()
	{
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
	
	public void fromXML(Element xmlElement)
	{
		if(xmlElement.element("originalabsolutefilepath")!=null)
			this.setOriginalAbsoluteFilePath(xmlElement.element("originalabsolutefilepath").getText());
		if(xmlElement.element("uniqueid")!=null)
			this.setUniqueId(xmlElement.element("uniqueid").getText());
		if(xmlElement.element("filename")!=null)
			this.setFilename(xmlElement.element("filename").getText());
		if(xmlElement.element("checksum")!=null)
			this.setChecksum(xmlElement.element("checksum").getText());
		if(xmlElement.element("checksumAlg")!=null)
			this.setChecksumAlgorithm(xmlElement.element("checksumAlg").getText());
		if(xmlElement.element("directdownloadurl")!=null)
			this.setDirectDownloadUrl(xmlElement.element("directdownloadurl").getText());
		if(xmlElement.element("mimetype")!=null)
			this.setMimeType(xmlElement.element("mimetype").getText());
		this.setSize(Integer.valueOf(xmlElement.element("size").getText()).intValue());
	}
	
	public static FileDescriptor createFromXMLString(String xml)
	{
		FileDescriptor retVal=new FileDescriptor();
		retVal.fromXMLString(xml);
		return retVal;
	}


}
