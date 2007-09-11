package pt.linkare.ant;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.linkare.ant.propreaders.PropertyReaderManager;

/**
 * Represents an input property for ant 
 * 
 * @author jpereira - Linkare TI
 *
 */
public class InputProperty {

	private InputPropertyMap propertyMap=null;
	private String propertyName=null;
	private String propertyMessage=null;
	private String propertyDefaultValue=null;
	private boolean propertyRequired=true;
	private boolean propertyPersist=true;
	private boolean propertyPersistNull=true;
	private String propertyType=null;
	private HashMap<String, String> propertyMetaData=new HashMap<String, String>();
	private String encoding=null;
	
	private String propertyValue=null;
	
	private List<PropertyDependency> dependencies=new ArrayList<PropertyDependency>();
	
	private boolean read=false;
	
	
	public InputProperty(InputProperty other) {
		this.propertyMap=other.propertyMap;
		this.setPropertyMessage(other.getPropertyMessage());
		this.setPropertyDefaultValue(other.getPropertyDefaultValue());
		this.setPropertyName(other.getPropertyName());
		this.setPropertyType(other.getPropertyType());
		this.setPropertyPersist(other.isPropertyPersist());
		this.setPropertyRequired(other.isPropertyRequired());
		this.setPropertyValue(other.getPropertyValue());
		this.setRead(other.isRead());
		this.setDependencies(other.getDependencies());
		this.propertyMetaData=other.propertyMetaData;
		this.setEncoding(other.getEncoding());
	}
	
	public InputProperty(InputPropertyMap propertyMap,String enconding) {
		super();
		this.setEncoding(enconding);
		this.propertyMap=propertyMap;
	}

	/**
	 * @return Returns the dependencies.
	 */
	public List<PropertyDependency> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies The dependencies to set.
	 */
	public void setDependencies(List<PropertyDependency> dependencies) {
		this.dependencies = dependencies;
	}
	
	public boolean validateDependencies()
	{
		boolean valid=true;
		for(PropertyDependency propDep:getDependencies())
		{
			valid=valid & propDep.validateDependency();
			if(!valid)
				break;
		}
		return valid;
	}

	/**
	 * @return Returns the propertyValue.
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * @param propertyValue The propertyValue to set.
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	/**
	 * @return Returns the propertyDefaultValue.
	 */
	public String getPropertyDefaultValue() {
		return propertyDefaultValue;
	}

	/**
	 * @param propertyDefaultValue The propertyDefaultValue to set.
	 */
	public void setPropertyDefaultValue(String propertyDefaultValue) {
		this.propertyDefaultValue = propertyDefaultValue;
	}

	/**
	 * @return Returns the propertyName.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * @param propertyName The propertyName to set.
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * @return Returns the propertyRequired.
	 */
	public boolean isPropertyRequired() {
		return propertyRequired;
	}

	/**
	 * @param propertyRequired The propertyRequired to set.
	 */
	public void setPropertyRequired(boolean propertyRequired) {
		this.propertyRequired = propertyRequired;
	}

	/**
	 * @return Returns the propertyType.
	 */
	public String getPropertyType() {
		return propertyType;
	}

	/**
	 * @param propertyType The propertyType to set.
	 */
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	
	
	public void setMetaData(String key,String value)
	{
		this.propertyMetaData.put(key,value);
	}
	
	public void removeMetaData(String key)
	{
		this.propertyMetaData.remove(key);
	}

	public String getMetaData(String key)
	{
		return this.propertyMetaData.get(key);
	}
	
	public Map<String,String> getMetaData()
	{
		return this.propertyMetaData;
	}

	public void clearMetaData()
	{
		this.propertyMetaData.clear();
	}

	/**
	 * @return Returns the propertyMessage.
	 */
	public String getPropertyMessage() {
		return propertyMessage;
	}

	/**
	 * @param propertyMessage The propertyMessage to set.
	 */
	public void setPropertyMessage(String propertyMessage) {
		this.propertyMessage = propertyMessage;
	}

	/**
	 * @return Returns the propertyPersist.
	 */
	public boolean isPropertyPersist() {
		return propertyPersist;
	}

	/**
	 * @param propertyPersist The propertyPersist to set.
	 */
	public void setPropertyPersist(boolean propertyPersist) {
		this.propertyPersist = propertyPersist;
	}

	/**
	 * @return Returns the read.
	 */
	public boolean isRead() {
		return read;
	}

	/**
	 * @param read The read to set.
	 */
	public void setRead(boolean read) {
		this.read = read;
	}
	
	public Collection<InputProperty> readNow(boolean fromDefault) throws InvalidPropertySpecException,NoPropertyReaderException, UnsupportedEncodingException
	{
		ArrayList<InputProperty> generatedProperties=new ArrayList<InputProperty>();
		
		if(isRead()) return generatedProperties;
		
		//do not read the prop again in any case
		this.setRead(true);
		
		for(PropertyDependency dep:getDependencies())
		{
			if(dep.getParentProperty()!=null && !dep.getParentProperty().isRead())
			{
				generatedProperties.addAll(dep.getParentProperty().readNow(fromDefault));
			}
		}
		
		
		if(validateDependencies())
			generatedProperties.addAll(PropertyReaderManager.getInstance(getEncoding()).readProperty(this,fromDefault));
		else
			setPropertyValue(null);


		return generatedProperties;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InputProperty " + getPropertyName() + ", Type=" + getPropertyType()+", Default="+getPropertyDefaultValue()+", Required="+isPropertyRequired()+", Persistent="+isPropertyPersist()+", Message="+getPropertyMessage()+" Metadata="+metadataToString();
	}

	private String metadataToString() {
		StringBuffer metadataStr=new StringBuffer("");
		if(this.propertyMetaData!=null)
			for(Map.Entry<String, String> entry: this.propertyMetaData.entrySet())
			{
				if(metadataStr.length()>0) metadataStr.append(",");
				metadataStr.append(entry.getKey()).append("=").append(entry.getValue());
			}
		if(metadataStr.length()<0)
			metadataStr.append("}").reverse().append("{").reverse();
		
		return metadataStr.toString();
	}

	/**
	 * @return Returns the propertyMap.
	 */
	public InputPropertyMap getPropertyMap() {
		return propertyMap;
	}

	/**
	 * @return Returns the propertyPersistNull.
	 */
	public boolean isPropertyPersistNull() {
		return propertyPersistNull;
	}

	/**
	 * @param propertyPersistNull The propertyPersistNull to set.
	 */
	public void setPropertyPersistNull(boolean propertyPersistNull) {
		this.propertyPersistNull = propertyPersistNull;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}


}
