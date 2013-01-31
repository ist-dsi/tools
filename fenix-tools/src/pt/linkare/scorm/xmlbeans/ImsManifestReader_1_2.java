/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.imsglobal.xsd.imsmdRootv1P2P1.LocationType;
import org.imsglobal.xsd.imsmdRootv1P2P1.LomDocument;
import org.imsglobal.xsd.imsmdRootv1P2P1.LomType;
import org.imsglobal.xsd.imsmdRootv1P2P1.TechnicalType;
import org.imsproject.xsd.imscpRootv1P1P2.FileType;
import org.imsproject.xsd.imscpRootv1P1P2.ItemType;
import org.imsproject.xsd.imscpRootv1P1P2.ManifestDocument;
import org.imsproject.xsd.imscpRootv1P1P2.ManifestType;
import org.imsproject.xsd.imscpRootv1P1P2.MetadataDocument;
import org.imsproject.xsd.imscpRootv1P1P2.MetadataType;
import org.imsproject.xsd.imscpRootv1P1P2.OrganizationType;
import org.imsproject.xsd.imscpRootv1P1P2.OrganizationsType;
import org.imsproject.xsd.imscpRootv1P1P2.ResourceType;
import org.imsproject.xsd.imscpRootv1P1P2.ResourcesType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pt.linkare.scorm.utils.ScormException;
import pt.linkare.scorm.utils.ScormMetaData;

/**
 * @author Oscar Ferreira - Linkare TI
 * 
 */
public class ImsManifestReader_1_2 {

	//Some usefull Strings
	public static final String IMS_MANIFEST_READER_AUTH = "author";
	public static final String IMS_MANIFEST_READER_PUB = "publisher";
	public static final String IMS_MANIFEST_READER_LOM_VERSION = "LOMv1.0";

	//SCORM_2004 Tags and there children
	public static final String[] SCORM_TAG_MANIFEST = { "metadata", "organizations", "resources", "manifest",
			"imsss:sequencingCollection" };
	public static final String[] SCORM_TAG_METADATA = { "schema", "schemaversion", "lom", "adlcp:location" };
	public static final String[] SCORM_TAG_ORGANIZATIONS = { "organization" };
	public static final String[] SCORM_TAG_ORGANIZATION = { "title", "item", "metadata", "imsss:sequencing" };
	public static final String[] SCORM_TAG_ITEM = { "title", "item", "metadata", "adlcp:timeLimitAction", "adlcp:dataFromLMS",
			"adlcp:completionThreshold", "imsss:sequencing", "adlnav:presentation" };
	public static final String[] SCORM_TAG_RESOURCES = { "resource" };
	public static final String[] SCORM_TAG_RESOURCE = { "metadata", "file", "dependency" };
	public static final String[] SCORM_TAG_FILE = { "metadata" };

	//Dublin Core
	public static final String[] DUBLIN_CORE_QUALIFIER_CONTRIBUTOR = { "advisor", "author", "editor", "illustrator", "other" };
	public static final String[] DUBLIN_CORE_QUALIFIER_COVERAGE = { "spatial", "temporal" };
	public static final String[] DUBLIN_CORE_QUALIFIER_DATE = { "accessioned", "available", "copyright", "created", "issued",
			"submitted" };
	public static final String[] DUBLIN_CORE_QUALIFIER_IDENTIFIER = { "citation", "govdoc", "isbn", "issn", "sici", "ismn",
			"other", "uri" };
	public static final String[] DUBLIN_CORE_QUALIFIER_DESCRIPTION = { "abstract", "provenance", "sponsorship",
			"statementofresponsibility", "tableofcontents", "uri" };
	public static final String[] DUBLIN_CORE_QUALIFIER_FORMAT = { "extent", "medium", "mimetype" };
	public static final String[] DUBLIN_CORE_QUALIFIER_LANGUAGE = { "iso" };
	public static final String[] DUBLIN_CORE_QUALIFIER_RELATION = { "isformatof", "ispartof", "ispartofseries", "haspart",
			"hasformat", "isversionof", "hasversionof", "hasversion", "isbasedon", "isbasisfor", "isreferencedby",
			"isrequiredby", "requires", "references", "replaces", "isreplacedby", "uri" };
	public static final String[] DUBLIN_CORE_QUALIFIER_RIGHTS = { "uri" };
	public static final String[] DUBLIN_CORE_QUALIFIER_SOURCE = { "uri" };
	public static final String[] DUBLIN_CORE_QUALIFIER_SUBJECT = { "classification", "ddc", "lcc", "lcsh", "mesh", "other" };
	public static final String[] DUBLIN_CORE_QUALIFIER_TITLE = { "alternative" };

	private Collection<ScormMetaData> itemDSpaceMetaData = null;

	//Valor de base xml:base pode ser geral pelo documento caso referencia do ficheiro
	//seja local e nao comece com "/" e caso nao tenha nenhum 
	private String xmlBase = null;
	private File basePath = null;

	public ImsManifestReader_1_2(File dirZipExtraido) {
		super();
		this.basePath = dirZipExtraido;
		this.xmlBase = dirZipExtraido.getAbsolutePath();
		if (!this.xmlBase.endsWith("/")) {
			this.xmlBase = this.xmlBase.concat("/");
		}
	}

	public File getBasePath() {
		return basePath;
	}

	public void setBasePath(File basePath) {
		this.basePath = basePath;
	}

	public Collection<ScormMetaData> getItemDSpaceMetaData() {
		return itemDSpaceMetaData;
	}

	public void setItemDSpaceMetaData(Collection<ScormMetaData> itemDSpaceMetaData) {
		this.itemDSpaceMetaData = itemDSpaceMetaData;
	}

	public String getXmlBase() {
		return xmlBase;
	}

	public void setXmlBase(String xmlBase) {
		this.xmlBase = xmlBase;
	}

	public Collection<ScormAsset> getColScormAssets(File dirPifPackageExtracted) throws ScormException //IOException, XmlException, Exception
	{
		try {
			if (dirPifPackageExtracted != null) {
				Collection<ScormAsset> colScormAssets = new ArrayList<ScormAsset>();
				ImsManifestReader_1_2 imsR = new ImsManifestReader_1_2(dirPifPackageExtracted);
				File manifestFile = imsR.getImsmanifest(imsR.getXmlBase());
				ManifestDocument mDoc = ManifestDocument.Factory.parse(manifestFile);
				String resourceHref = null;
				if (mDoc != null) {
					ManifestType manType = mDoc.getManifest();
					String xmlBaseAux = getXmlBaseAttrib(manType, imsR.getXmlBase());
					ResourcesType resourcesType = manType.getResources();
					String xmlBaseAux1 = getXmlBaseAttrib(resourcesType, xmlBaseAux);
					ResourceType[] resourceTypeArray = resourcesType.getResourceArray();
					for (ResourceType resourceType : resourceTypeArray) {
						ScormAsset scormAssetToAdd0 = new ScormAsset();
						String xmlBaseAux2 = getXmlBaseAttrib(resourceType, xmlBaseAux1);
						resourceHref = getHrefAttrib(resourceType, xmlBaseAux);
						MetadataType metaDType = resourceType.getMetadata();
						if (metaDType != null) {
							String metaDataXmlFileLocation = getLocationFromMetadata(metaDType, xmlBaseAux2);
							//We're going to get ItemMetaData for this resource from another imsmanifest file
							if (metaDataXmlFileLocation != null && metaDataXmlFileLocation.length() > 0) {
								scormAssetToAdd0.setMetadataFile(new File(metaDataXmlFileLocation));
								scormAssetToAdd0.setContentMetadataInfo(imsR.getMetaDataFromMetaOrLomXMLFile(scormAssetToAdd0
										.getMetadataFile()));
								FileType[] fileTypeArray = resourceType.getFileArray();
								if (fileTypeArray != null && fileTypeArray.length > 0) {
									Collection<File> colResourceFiles0 = new ArrayList<File>();
									for (FileType fileType : fileTypeArray) {
										ScormAssetCreationForFileTypes(fileType, scormAssetToAdd0, metaDataXmlFileLocation,
												xmlBaseAux2, colScormAssets, colResourceFiles0, imsR);
									}
									if (colResourceFiles0.size() > 0) {
										scormAssetToAdd0.setContentFiles(colResourceFiles0);
										colScormAssets.add(scormAssetToAdd0);
									}
								}
								//here we have metaData but no files types except the one 
								//that must be metioned in the resourceType's href
								else {
									if (resourceHref != null) {
										Collection<File> colResourceFiles0 = new ArrayList<File>(1);
										colResourceFiles0.add(new File(resourceHref));
										scormAssetToAdd0.setContentFiles(colResourceFiles0);
										colScormAssets.add(scormAssetToAdd0);
									} else {
										throw new ScormException(
												"Resource File location not encountered.ResourceType href attribute not found,nor are any FileTypes present.");
									}
								}
							} else {
								//XXX here we have internal metadata but the file that contains this metadata
								//continues to be the principal imsmanifest.xml but the metadata is particular 
								//to the resource we are in.
								//XXX put the 1st argument from manifestFile -> null
								scormAssetToAdd0.setMetadataFile(null);
								scormAssetToAdd0.setContentMetadataInfo(imsR.getMetaDataFromMetaOrLomXMLFile(metaDType));
								FileType[] fileTypeArray = resourceType.getFileArray();
								//going through all the resources files if a file has it's own metadata we must
								//treat it as a new ScormAsset and add it to the collection else we add
								//the file to the collection of files belonging to the general ScormAsset of the resource 
								if (fileTypeArray != null && fileTypeArray.length > 0) {
									Collection<File> colResourceFiles0 = new ArrayList<File>();
									for (FileType fileType : fileTypeArray) {
										ScormAssetCreationForFileTypes(fileType, scormAssetToAdd0, metaDataXmlFileLocation,
												xmlBaseAux2, colScormAssets, colResourceFiles0, imsR);
									}
									if (colResourceFiles0.size() > 0) {
										scormAssetToAdd0.setContentFiles(colResourceFiles0);
										colScormAssets.add(scormAssetToAdd0);
									}
								} else {//estamos perante um resource com metadata mas onde o File é representado no
										//attributo "href" do resourceType  
									if (resourceHref != null) {
										Collection<File> colResourceFiles0 = new ArrayList<File>(1);
										colResourceFiles0.add(new File(resourceHref));
										scormAssetToAdd0.setContentFiles(colResourceFiles0);
										colScormAssets.add(scormAssetToAdd0);
									} else {
										throw new ScormException(
												"Resource File location not encountered.ResourceType href attribute not found,nor are any FileTypes present.");
									}
								}
							}
						} else {
							FileType[] fileTypeArray = resourceType.getFileArray();
							//Resource has no metaData but the file might have...
							if (fileTypeArray != null && fileTypeArray.length > 0) {
								String metaDataXmlFileLocation = null;
								Collection<File> colResourceFiles0 = new ArrayList<File>();
								for (FileType fileType : fileTypeArray) {
									ScormAssetCreationForFileTypes(fileType, scormAssetToAdd0, metaDataXmlFileLocation,
											xmlBaseAux2, colScormAssets, colResourceFiles0, imsR);
								}
								if (colResourceFiles0.size() > 0) {
									scormAssetToAdd0.setContentMetadataInfo(imsR.getMetaDataFromMetaOrLomXMLFile(manType
											.getMetadata()));
									scormAssetToAdd0.setContentFiles(colResourceFiles0);
									colScormAssets.add(scormAssetToAdd0);
								}
							}
						}

					}

					return colScormAssets;
				} else {
					throw new ScormException("ImsManifest has no Manifest Tag...ImsManifest INVALID!");
				}
			}
		} catch (ScormException e) {
			throw e;
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION, e);
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return null;
	}

	private void ScormAssetCreationForFileTypes(FileType fileType, ScormAsset scormAssetToAdd0, String metaDataXmlFileLocation,
			String xmlBaseAux2, Collection<ScormAsset> colScormAssets, Collection<File> colResourceFiles0,
			ImsManifestReader_1_2 imsR) throws Exception {
		try {
			MetadataType metaDataType = fileType.getMetadata();
			if (metaDataType != null) {
				ScormAsset scormAssetToAdd1 = new ScormAsset();
				metaDataXmlFileLocation = getLocationFromMetadata(metaDataType, xmlBaseAux2);
				if (metaDataXmlFileLocation != null && metaDataXmlFileLocation.length() > 0) {
					ScormAssetCreationWithExternalMetaData(colScormAssets, imsR, scormAssetToAdd1, fileType, xmlBaseAux2,
							metaDataXmlFileLocation);
				} else {
					//XXX put the 1º argument from manifestFile -> null
					ScormAssetCreationWithInternalMetaData(null, colScormAssets, imsR, scormAssetToAdd1, fileType, xmlBaseAux2,
							metaDataType);
				}
			} else {
				String fileTypeURL = imsR.getXmlBaseAttribForFileTypes(fileType, xmlBaseAux2);
				colResourceFiles0.add(new File(fileTypeURL));
			}
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	//ScormAssetCreationWithExternalMetaDataFileLocation
	private void ScormAssetCreationWithExternalMetaData(Collection<ScormAsset> colScormAssets, ImsManifestReader_1_2 imsR,
			ScormAsset scormAssetToAdd1, FileType fileType, String xmlBaseAux2, String metaDataXmlFileLocation) throws Exception {
		try {
			Collection<File> colResourceFiles1 = new ArrayList<File>();
			scormAssetToAdd1.setMetadataFile(new File(metaDataXmlFileLocation));
			scormAssetToAdd1.setContentMetadataInfo(imsR.getMetaDataFromMetaOrLomXMLFile(scormAssetToAdd1.getMetadataFile()));
			String fileTypeURL = imsR.getXmlBaseAttribForFileTypes(fileType, xmlBaseAux2);
			colResourceFiles1.add(new File(fileTypeURL));
			scormAssetToAdd1.setContentFiles(colResourceFiles1);
			colScormAssets.add(scormAssetToAdd1);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	private void ScormAssetCreationWithInternalMetaData(File manifestFile, Collection<ScormAsset> colScormAssets,
			ImsManifestReader_1_2 imsR, ScormAsset scormAssetToAdd1, FileType fileType, String xmlBaseAux2,
			MetadataType metaDataType) throws ScormException {
		try {
			Collection<File> colResourceFiles1 = new ArrayList<File>();
			//XXX Talvez este manifestFile q adiciono devia ser null,pq assim penso que estou a dizer q o imsmanifest
			//file dele � o principal do pacote
			scormAssetToAdd1.setMetadataFile(manifestFile);
			scormAssetToAdd1.setContentMetadataInfo(imsR.getMetaDataFromMetaOrLomXMLFile(metaDataType));
			String fileTypeURL = imsR.getXmlBaseAttribForFileTypes(fileType, xmlBaseAux2);
			colResourceFiles1.add(new File(fileTypeURL));
			scormAssetToAdd1.setContentFiles(colResourceFiles1);
			colScormAssets.add(scormAssetToAdd1);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	//will give a null value case imsmanifest does not exist..NOT PIF
	public File getImsmanifest(String AbsolutePathDirZipExtraido) throws ScormException //NullPointerException
	{
		try {
			return new File(AbsolutePathDirZipExtraido, "imsmanifest.xml");
		} catch (NullPointerException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_NULLPOINTEREXCEPTION, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	@SuppressWarnings("unused")
	private String verifyFileCollectionExistence(Collection<String> colLocations) {
		for (String filePath : colLocations) {
			if (filePath.startsWith("http://")) {
			} else {
				if (new File(filePath).exists() == false) {
					return filePath;
				}
			}
		}
		return null;
	}

	//CALL THIS METHOD!!!!!!
	//To go through the imsmanifest.xml file and collect all external files except initial schema files
	public Collection<String> getAllFileNamesRefManifest(File xmlFile) throws ScormException//XmlException,IOException,Exception
	{
		Collection<String> strCol = new HashSet<String>();
		ManifestDocument mDoc = null;
		String strXmlBase = null;

		try {
			if (xmlFile != null) {
				mDoc = ManifestDocument.Factory.parse(xmlFile);
				if (mDoc != null) {
					ManifestType mt = mDoc.getManifest();
					if (mt != null) {
						strXmlBase = getXmlBaseAttrib(mt);
						strCol = getManifestMetaDataRecursive(mt, strCol, strXmlBase);
					}
				}
			}
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION, e);
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return strCol;
	}

	//Given an object gets its Attribute "xml:base" and adds another hierarchical
	//path to the 'this'.objects xmlBase Property
	private String getXmlBaseAttrib(XmlObject xmlObj) {
		String XmlBaseTemp = null;
		if (xmlObj != null) {
			Node node = xmlObj.getDomNode();

			if (node.getAttributes().item(0) != null && node.getAttributes().item(0).getNodeName().equalsIgnoreCase("xml:base")) {
				node = node.getAttributes().item(0);
			} else {
				node = node.getAttributes().getNamedItem("xml:base");
			}
			if (node != null) {
				String strBase = node.getNodeValue();
				if (strBase.startsWith("/")) {
					strBase = strBase.substring(1);
				}
				if (this.xmlBase != null && this.xmlBase.compareTo("") != 0 && !(strBase.startsWith("http://"))) {
					if (!this.xmlBase.endsWith("/")) {
						this.setXmlBase(this.xmlBase.concat("/"));
					}
					XmlBaseTemp = this.xmlBase.concat(strBase);
				} else {
					XmlBaseTemp = strBase;
				}
			}
		}
		return XmlBaseTemp;
	}

	//Given an object gets its Attribute "xml:base" and adds another hierarchical
	//path to the 'this'.objects xmlBase Property
	private String getXmlBaseAttrib(XmlObject xmlObj, String otherXmlbase) {
		String XmlBaseTemp = null;
		if (otherXmlbase != null && otherXmlbase.compareTo("") != 0) {
			if (!otherXmlbase.endsWith("/")) {
				otherXmlbase = otherXmlbase.concat("/");
			}
			if (xmlObj != null) {
				NamedNodeMap nnm = xmlObj.getDomNode().getAttributes();
				if (nnm != null && nnm.getLength() > 0) {
					Node node = nnm.getNamedItem("xml:base");
					if (node != null) {
						String strBase = node.getNodeValue();
						if (strBase.startsWith("/")) {
							strBase = strBase.substring(1);
						}
						if (!(strBase.startsWith("http://"))) {
							XmlBaseTemp = otherXmlbase.concat(strBase);
						} else {
							XmlBaseTemp = strBase;
						}
					} else {
						XmlBaseTemp = otherXmlbase;
					}
				} else {
					XmlBaseTemp = otherXmlbase;
				}
			}
		} else {
			XmlBaseTemp = getXmlBaseAttrib(xmlObj);
		}

		return XmlBaseTemp;
	}

	//Given an object gets its Attribute "xml:base" and adds another hierarchical
	//path to the 'this'.objects xmlBase Property
	private String getHrefAttrib(XmlObject xmlObj, String otherXmlbase) {
		String XmlBaseTemp = null;
		if (otherXmlbase != null && otherXmlbase.compareTo("") != 0) {
			if (!otherXmlbase.endsWith("/")) {
				otherXmlbase = otherXmlbase.concat("/");
			}
			if (xmlObj != null) {
				NamedNodeMap nnm = xmlObj.getDomNode().getAttributes();
				if (nnm != null && nnm.getLength() > 0) {
					Node node = nnm.getNamedItem("href");
					if (node != null) {
						String strBase = node.getNodeValue();
						if (strBase.startsWith("/")) {
							strBase = strBase.substring(1);
						}
						if (!(strBase.startsWith("http://"))) {
							XmlBaseTemp = otherXmlbase.concat(strBase);
						} else {
							XmlBaseTemp = strBase;
						}
					} else {
						XmlBaseTemp = otherXmlbase;
					}
				} else {
					XmlBaseTemp = otherXmlbase;
				}
			}
		} else {
			XmlBaseTemp = getXmlBaseAttrib(xmlObj);
		}

		return XmlBaseTemp;
	}

	//Given an object gets its Attribute "xml:base" and adds another hierarchical
	//path to the 'this'.objects xmlBase Property
	private String getXmlBaseAttribForFileTypes(FileType fType) {
		String XmlBaseTemp = null;
		XmlObject xmlObjTemp = null;
		if (fType != null) {
			xmlObjTemp = fType.selectAttribute(null, "href");
			if (xmlObjTemp != null) {
				String strBase = xmlObjTemp.getDomNode().getNodeValue();
				if (strBase.startsWith("/")) {
					strBase = strBase.substring(1);
				}
				if (this.xmlBase != null && this.xmlBase.compareTo("") != 0
						&& !(xmlObjTemp.getDomNode().getNodeValue().startsWith("http://"))) {
					if (!this.xmlBase.endsWith("/")) {
						this.setXmlBase(this.xmlBase.concat("/"));
					}
					XmlBaseTemp = this.xmlBase.concat(strBase);
				} else {
					XmlBaseTemp = strBase;
				}
			}
		}
		return XmlBaseTemp;
	}

	//Given an object gets its Attribute "xml:base" and adds another hierarchical
	//path to the 'this'.objects xmlBase Property
	private String getXmlBaseAttribForFileTypes(FileType fType, String otherXmlbase) {
		String XmlBaseTemp = null;
		XmlObject xmlObjTemp = null;
		if (otherXmlbase != null && otherXmlbase.compareTo("") != 0) {

			if (!otherXmlbase.endsWith("/")) {
				otherXmlbase = otherXmlbase.concat("/");
			}
			if (fType != null) {
				xmlObjTemp = fType.selectAttribute(null, "href");
				if (xmlObjTemp != null) {
					String strBase = xmlObjTemp.getDomNode().getNodeValue();
					if (strBase.startsWith("/")) {
						strBase = strBase.substring(1);
					}
					if (!(strBase.startsWith("http://"))) {
						XmlBaseTemp = otherXmlbase.concat(strBase);
					} else {
						XmlBaseTemp = strBase;
					}
				}
			}
		} else {
			XmlBaseTemp = getXmlBaseAttribForFileTypes(fType);
		}
		return XmlBaseTemp;
	}

	//Remove last substring from larger string
	//Ex: "Dir0/Dir1/hello.txt"->"Dir0/Dir1/"->"Dir0/",até ficar vazio
	public void removeLastHierarchicalXmlbase() {
		if (this.xmlBase != null && this.xmlBase.compareToIgnoreCase("") != 0) {
			String strAux = this.xmlBase.substring(0, this.xmlBase.length() - 1);
			int pos = strAux.lastIndexOf('/');
			this.xmlBase = this.xmlBase.substring(0, pos + 1);
		}
		//else deixa-o como está
	}

	public void removeLastXmlbaseFromRealXmlbase(String lastXmlbaseAdded) {
		if (this.xmlBase != null && this.xmlBase.endsWith(lastXmlbaseAdded) && this.xmlBase.compareTo("") != 0) {
			this.xmlBase = this.xmlBase.substring(0, this.xmlBase.length() - lastXmlbaseAdded.length());
		}
		//else deixo-o estar pq n foi afectado ou pq o q se queres remover n está no seu fim
	}

	//Principal Method to gather all the Absolute Path for files in the imsmanifest.xml file,Arg:xmlBase is the Manifests xml:base
	private Collection<String> getManifestMetaDataRecursive(ManifestType manType, Collection<String> colLocations, String xmlBase)
			throws ScormException//XmlException,IOException,Exception
	{
		if (manType != null) {
			String xmlBaseAux = null;
			ResourcesType rsType = null;
			MetadataType mDataType = null;
			//Doing all the Manifest Nodes in manType
			if (manType.getManifestArray() != null) {
				for (ManifestType mType : manType.getManifestArray()) {
					mDataType = mType.getMetadata();
					colLocations = getLocationFromMetadata(mDataType, colLocations, xmlBase);
					//Back to Manifest
					OrganizationsType orgsType = mType.getOrganizations();
					if (orgsType != null) {
						OrganizationType[] orgArrayType = orgsType.getOrganizationArray();
						if (orgArrayType != null) {
							for (OrganizationType orgType : orgArrayType) {
								mDataType = orgType.getMetadata();
								colLocations = getLocationFromMetadata(mDataType, colLocations, xmlBase);
								//Item in Organization may also have MetaData
								ItemType[] itemArrayType = orgType.getItemArray();
								//gets all locations from all items recursively
								colLocations = getItemsMetaDataRecursive(itemArrayType, colLocations, xmlBase);
							}
						}
					}//Locations:done for Organizations
						//Locations for Resources...
					rsType = mType.getResources();
					if (rsType != null) {
						FileType[] fTypeArray = null;
						xmlBaseAux = getXmlBaseAttrib(rsType, xmlBase);
						for (ResourceType rType : rsType.getResourceArray()) {
							xmlBaseAux = getXmlBaseAttrib(rType, xmlBaseAux);
							colLocations = getLocationFromMetadata(rType.getMetadata(), colLocations, xmlBaseAux);
							fTypeArray = rType.getFileArray();
							if (fTypeArray != null) {
								for (FileType fType : fTypeArray) {
									colLocations.add(getXmlBaseAttribForFileTypes(fType, xmlBaseAux));
									colLocations = getLocationFromMetadata(fType.getMetadata(), colLocations, xmlBaseAux);
								}
							}
						}
					}
					colLocations = getLomTechnicalLocation(mDataType, colLocations);
					colLocations = getManifestMetaDataRecursive(mType, colLocations, xmlBase);
				}
			}
			//Do This Manifest Node...
			mDataType = manType.getMetadata();
			colLocations = getLocationFromMetadata(mDataType, colLocations, xmlBase);
			//Back to Manifest
			OrganizationsType orgsType = manType.getOrganizations();
			if (orgsType != null) {
				OrganizationType[] orgArrayType = orgsType.getOrganizationArray();
				if (orgArrayType != null) {
					for (OrganizationType orgType : orgArrayType) {
						mDataType = orgType.getMetadata();
						colLocations = getLocationFromMetadata(mDataType, colLocations, xmlBase);
						//Item in Organization may also have MetaData
						ItemType[] itemArrayType = orgType.getItemArray();
						//gets all locations from all items recursively  
						colLocations = getItemsMetaDataRecursive(itemArrayType, colLocations, xmlBase);
					}
				}
			}//Locations:done for Organizations
				//Locations for Resources...
			rsType = manType.getResources();
			if (rsType != null) {
				FileType[] fTypeArray = null;
				xmlBaseAux = getXmlBaseAttrib(rsType, xmlBase);
				for (ResourceType rType : rsType.getResourceArray()) {
					String xmlBaseAux1 = getXmlBaseAttrib(rType, xmlBaseAux);
					colLocations = getLocationFromMetadata(rType.getMetadata(), colLocations, xmlBaseAux1);
					fTypeArray = rType.getFileArray();
					if (fTypeArray != null) {
						for (FileType fType : fTypeArray) {
							colLocations.add(getXmlBaseAttribForFileTypes(fType, xmlBaseAux1));
							colLocations = getLocationFromMetadata(fType.getMetadata(), colLocations, xmlBaseAux1);
						}
					}
				}
			}
			colLocations = getLomTechnicalLocation(mDataType, colLocations);
		}
		return colLocations;
	}

	//Get Locations from all Metadatas in all ItemTypes found,adds to Collection of Strings
	//representing Locations and valid complete Hrefs for the SCORM Package
	private Collection<String> getItemsMetaDataRecursive(ItemType[] iTypeArray, Collection<String> colLocations, String xmlBase)
			throws ScormException//Exception
	{
		try {
			if (iTypeArray != null) {
				for (ItemType it : iTypeArray) {
					colLocations = getLocationFromMetadata(it.getMetadata(), colLocations, xmlBase);
					colLocations = getItemsMetaDataRecursive(it.getItemArray(), colLocations, xmlBase);
				}
			}
		} catch (ScormException e) {
			throw e;
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return colLocations;
	}

	//Loms Technical element has a Location, adding them
	private Collection<String> getLomTechnicalLocation(MetadataType metaType, Collection<String> colLocations)
			throws ScormException {
		try {
			if (metaType != null) {
				NodeList nodelist = metaType.getDomNode().getChildNodes();
				int pos = 0;
				while (pos < nodelist.getLength() && !nodelist.item(pos).getNodeName().startsWith("lom")) {
					pos++;
				}
				LomDocument lomDocument = null;
				//      Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
				if (pos < nodelist.getLength()) {
					lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));
				}
				if (lomDocument != null) {
					LomType lomType = lomDocument.getLom();
					if (lomType != null) {
						TechnicalType tType = lomType.getTechnical();
						if (tType != null) {
							LocationType[] locTypeArray = tType.getLocationArray();
							if (locTypeArray != null) {
								for (LocationType locType : locTypeArray) {
									colLocations.add(locType.getStringValue());
								}
							}
						}
					}
				}
			}
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return colLocations;
	}

//  Given a MetaDataType gives it's adlcp:Location value
	private String getLocationFromMetadata(MetadataType metaType, String locXmlbase) throws ScormException//Exception
	{
		NodeList nodeList = null;
		LocationType lType = null;
		String locXmlBaseAux = null;
		try {
			if (metaType != null) {
				nodeList = metaType.getDomNode().getChildNodes();
				if (nodeList != null) {
					for (int i = 0; i < nodeList.getLength(); i++) {
						if (nodeList.item(i).getNodeName().compareToIgnoreCase("adlcp:location") == 0) {
							lType = LocationType.Factory.parse(nodeList.item(i));
							if (lType != null) {
								locXmlBaseAux = lType.getStringValue();
								if (locXmlbase != null) {
									if (locXmlBaseAux.startsWith("/")) {
										locXmlBaseAux = locXmlBaseAux.substring(1);
									}
									locXmlBaseAux = locXmlbase.concat(locXmlBaseAux);
								} else {
									locXmlBaseAux = this.getXmlBase().concat(locXmlBaseAux);
								}
							}
						}
					}
				}
			}
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return locXmlBaseAux;
	}

//  Given a MetaDataType gives it's adlcp:Location value
	private Collection<String> getLocationFromMetadata(MetadataType metaType, Collection<String> colLocations, String locXmlbase)
			throws ScormException//Exception
	{
		NodeList nodeList = null;
		LocationType lType = null;
		String locXmlBaseAux = null;
		try {
			if (metaType != null) {
				nodeList = metaType.getDomNode().getChildNodes();
				if (nodeList != null) {
					for (int i = 0; i < nodeList.getLength(); i++) {
						if (nodeList.item(i).getNodeName().compareToIgnoreCase("adlcp:location") == 0) {
							lType = LocationType.Factory.parse(nodeList.item(i));
							if (lType != null) {
								locXmlBaseAux = lType.getStringValue();
								if (locXmlbase != null) {
									if (locXmlBaseAux.startsWith("/")) {
										locXmlBaseAux = locXmlBaseAux.substring(1);
									}
									locXmlBaseAux = locXmlbase.concat(locXmlBaseAux);
								} else {
									locXmlBaseAux = this.getXmlBase().concat(locXmlBaseAux);
								}
								colLocations.add(locXmlBaseAux);
							}
						}
					}
				}
			}
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return colLocations;
	}

	//Gets Xsd names at the manifest tags attributes
	public Collection<File> getXsdFromXml(File xmlFile) throws ScormException//XmlException,IOException 
	{
		Collection<File> fileCol = new HashSet<File>();
		ManifestDocument mDoc = null;
		int count = 0, countaux = 0;

		try {
			if (xmlFile != null) {
				mDoc = ManifestDocument.Factory.parse(xmlFile);
				if (mDoc != null) {
					ManifestType mt = mDoc.getManifest();
					Node xsdNode = mt.getDomNode().getAttributes().getNamedItem("xsi:schemaLocation");
					if (xsdNode != null) {
						String strXsds = xsdNode.getNodeValue();
						String straux = null;
						while ((count = strXsds.indexOf(".xsd")) != -1) {
							while (strXsds.charAt(count) != ' ') {
								count--;
								countaux++;
							}
							straux = strXsds.substring(count + 1, count + countaux + 4);
							count += countaux + 1;
							countaux = 0;
							strXsds = strXsds.substring(count);
							fileCol.add(this.resolveUrl(this.xmlBase, straux));
						}
					}
				}
			}
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return fileCol;
	}

	//Devolve o File com ao xmlBase vindo do tag metadata local ou do base geral
	//e com o location do ficheiro local.Para por fim ter o Absolute path.
	private File resolveUrl(String base, String location) {
		if (base == null) {
			if (this.xmlBase != null) {
				base = this.xmlBase;
			} else {
				base = "/";
			}
		}
		if (!base.endsWith("/")) {
			base = base.concat("/");
		}
		location = base.concat(location);
		if (location != null) {
			File f = new File(location);
			return f;
		}
		return null;
	}

	//This method is called in another method
	//Just used by the getAnyLoms(XmlObject xmlObj) method,will give out a Collection of Nodes
	//that are not of the basic scormChildren named in the scormChildren
	private Collection<Node> calledBygetAnyLoms(NodeList nodeList, String[] scormChildren) {
		Collection<Node> colNodes = new ArrayList<Node>();
		Collection<Node> colNodesAux = null;
		boolean isAnyNode = true;
		if (nodeList != null) {
			for (int i = 0; i < nodeList.getLength(); i++) {
				isAnyNode = true;
				String strNodeNameTemp = nodeList.item(i).getNodeName();
				for (String element : scormChildren) {
					if (nodeList.item(i).getNodeValue() == null || !nodeList.item(i).hasChildNodes()
							|| strNodeNameTemp.startsWith("#")) {
						isAnyNode = false;
					} else {
						//if we enter here it's because we encountered a
						//ChildNode of the Any value.Still we must verify that
						//the node is a manifest or lom
						if (strNodeNameTemp.compareToIgnoreCase(element) == 0
								|| strNodeNameTemp.compareToIgnoreCase("metadata") != 0
								|| strNodeNameTemp.compareToIgnoreCase("lom") != 0) {
							isAnyNode = false;
						}
					}
				}
				if (isAnyNode == true) {
					colNodes.add(nodeList.item(i));
				}
				colNodesAux = getAnyLoms(nodeList.item(i));
				if (colNodesAux != null) {
					colNodes.addAll(colNodesAux);
				}
			}
		}
		return colNodes;
	}

	//Call this method!In method that need to treat the manifest Tags
	//will return a Collection of Nodes being them Lom or Metadata
	//from a XmlObject that should be the Root Element for our ImsManifest.xml
	//manifest tag element
	private Collection<Node> getAnyLoms(XmlObject xmlObj) {
		//XXX Aqui mudei de xmlObjNode.getNodeName().compareToIgnoreCase("organizations")==0
		//-> xmlObjNode.getNodeName().endsWith("organizations")
		//pela a razao que alguns elementos podem vir com o nome do namespace
		//em 1º lugar depois o nome do elemento ex:"ims:organizations"
		if (xmlObj != null) {
			Node xmlObjNode = xmlObj.getDomNode();
			NodeList nodeList = xmlObjNode.getChildNodes();
			if (xmlObjNode.getNodeName().endsWith("manifest")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_MANIFEST);
			}
			if (xmlObjNode.getNodeName().endsWith("metadata")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_METADATA);
			}
			if (xmlObjNode.getNodeName().endsWith("organizations")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_ORGANIZATIONS);
			}
			if (xmlObjNode.getNodeName().endsWith("organization")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_ORGANIZATION);
			}
			if (xmlObjNode.getNodeName().endsWith("item")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_ITEM);
			}
			if (xmlObjNode.getNodeName().endsWith("resources")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_RESOURCES);
			}
			if (xmlObjNode.getNodeName().endsWith("resource")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_RESOURCE);
			}
			if (xmlObjNode.getNodeName().endsWith("file")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_FILE);
			}
		}
		return null;
	}

	//This method is called in another method
	//will return a Collection of Nodes being them Lom or Metadata
	//from a XmlObject
	private Collection<Node> getAnyLoms(Node xmlObjNode) {
//      XXX Aqui mudei de xmlObjNode.getNodeName().compareToIgnoreCase("organizations")==0
		//-> xmlObjNode.getNodeName().endsWith("organizations")
		//pela a razao que alguns elementos podem vir com o nome do namespace
		//em 1º lugar depois o nome do elemento ex:"ims:organizations"
		Collection<Node> colNodes = null;
		if (xmlObjNode != null) {
			//Node xmlObjNode = xmlObj.getDomNode();
			NodeList nodeList = xmlObjNode.getChildNodes();
			if (xmlObjNode.getNodeName().endsWith("manifest")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_MANIFEST);
			}
			if (xmlObjNode.getNodeName().endsWith("metadata")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_METADATA);
			}
			if (xmlObjNode.getNodeName().endsWith("organizations")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_ORGANIZATIONS);
			}
			if (xmlObjNode.getNodeName().endsWith("organization")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_ORGANIZATION);
			}
			if (xmlObjNode.getNodeName().endsWith("item")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_ITEM);
			}
			if (xmlObjNode.getNodeName().endsWith("resources")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_RESOURCES);
			}
			if (xmlObjNode.getNodeName().endsWith("resource")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_RESOURCE);
			}
			if (xmlObjNode.getNodeName().endsWith("file")) {
				return calledBygetAnyLoms(nodeList, ImsManifestReader_1_2.SCORM_TAG_FILE);
			}
		}
		return colNodes;
	}

	private Collection<ScormMetaData> readMetaData(File metaFile) throws ScormException//
	{
		Collection<ScormMetaData> moreItemMetaData = null;
		try {
			MetadataDocument metaDoc = MetadataDocument.Factory.parse(metaFile);
			if (metaDoc != null) {
				MetadataType metaType = metaDoc.getMetadata();
				if (metaType != null) {
					NodeList nodelist = metaType.getDomNode().getChildNodes();
					int pos = 0;                                          //XXX Added && Xxx.endswith(":lom")
					while (pos < nodelist.getLength()
							&& (!nodelist.item(pos).getNodeName().startsWith("lom") && !nodelist.item(pos).getNodeName()
									.endsWith(":lom"))) {
						pos++;
					}
					LomDocument lomDocument = null;
					//Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
					if (pos < nodelist.getLength()) {
						lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));
					}
					if (lomDocument != null) {
						LomType lomType = lomDocument.getLom();
						if (lomType != null) {
							//HERE WE WORK WITH LOM TYPES!!
							moreItemMetaData = ScormMetadataCollector.allScormItemMetaData(lomType).listScormMetaData();
						}
					}
				}
			}
			return moreItemMetaData;
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION_PARSE, e);
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	protected Collection<ScormMetaData> readMetaData(MetadataType metType) throws ScormException//IOException,XmlException
	{
		try {
			Collection<ScormMetaData> moreItemMetaData = null;
			MetadataDocument metaDoc = MetadataDocument.Factory.parse(metType.getDomNode());
			if (metaDoc != null) {
				MetadataType metaType = metaDoc.getMetadata();
				if (metaType != null) {
					NodeList nodelist = metaType.getDomNode().getChildNodes();
					int pos = 0;                                  //XXX Added && Xxx.endswith(":lom")
					while (pos < nodelist.getLength()
							&& (!nodelist.item(pos).getNodeName().startsWith("lom") && !nodelist.item(pos).getNodeName()
									.endsWith(":lom"))) {
						pos++;
					}
					LomDocument lomDocument = null;
					if (pos < nodelist.getLength()) {
						lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));
					}
					if (lomDocument != null) {
						LomType lomType = lomDocument.getLom();
						if (lomType != null) {
							//HERE WE WORK WITH LOM TYPES!!
							moreItemMetaData = ScormMetadataCollector.allScormItemMetaData(lomType).listScormMetaData();
						}
					}
				}
			}
			return moreItemMetaData;
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	private Collection<ScormMetaData> readMetaData(Node metaNode) throws ScormException//IOException,XmlException
	{
		try {
			Collection<ScormMetaData> moreItemMetaData = null;
			MetadataDocument metaDoc = MetadataDocument.Factory.parse(metaNode);
			if (metaDoc != null) {
				MetadataType metaType = metaDoc.getMetadata();
				if (metaType != null) {
					NodeList nodelist = metaType.getDomNode().getChildNodes();
					int pos = 0;
					while (pos < nodelist.getLength()
							&& (!nodelist.item(pos).getNodeName().startsWith("lom") && !nodelist.item(pos).getNodeName()
									.endsWith(":lom"))) {
						pos++;
					}
					LomDocument lomDocument = null;
					//Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
					if (pos < nodelist.getLength()) {
						lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));
					}
					if (lomDocument != null) {
						LomType lomType = lomDocument.getLom();
						if (lomType != null) {
							//HERE WE WORK WITH LOM TYPES!!
							moreItemMetaData = ScormMetadataCollector.allScormItemMetaData(lomType).listScormMetaData();
						}
					}
				}
			}
			return moreItemMetaData;
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	private Collection<ScormMetaData> readLomData(File lomFile) throws ScormException//IOException,XmlException
	{
		try {
			Collection<ScormMetaData> moreItemMetaData = null;
			LomDocument lomDocument = null;
			//Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
			lomDocument = LomDocument.Factory.parse(lomFile);
			if (lomDocument != null) {
				LomType lomType = lomDocument.getLom();
				if (lomType != null) {
					//HERE WE WORK WITH LOM TYPES!!
					moreItemMetaData = ScormMetadataCollector.allScormItemMetaData(lomType).listScormMetaData();
				}
			}
			return moreItemMetaData;
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION_PARSE, e);
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	private Collection<ScormMetaData> readLomData(Node lomNode) throws ScormException//IOException,XmlException
	{
		try {
			Collection<ScormMetaData> moreItemMetaData = null;
			LomDocument lomDocument = null;
			//Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
			lomDocument = LomDocument.Factory.parse(lomNode);
			if (lomDocument != null) {
				LomType lomType = lomDocument.getLom();
				if (lomType != null) {
					//HERE WE WORK WITH LOM TYPES!!
					moreItemMetaData = ScormMetadataCollector.allScormItemMetaData(lomType).listScormMetaData();
				}
			}
			return moreItemMetaData;
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	protected Collection<ScormMetaData> readManifestData(ManifestType manType) throws ScormException//XmlException,IOException
	{
		try {
			Collection<ScormMetaData> moreItemMetaData = null;
			Collection<ScormMetaData> moreItemMetaDataAux = null;
			MetadataType metaDType = manType.getMetadata();
			if (metaDType != null) {
				MetadataDocument metaDoc = MetadataDocument.Factory.parse(metaDType.getDomNode());
				if (metaDoc != null) {
					//MetaData
					MetadataType metaType = metaDoc.getMetadata();
					if (metaType != null) {
						NodeList nodelist = metaType.getDomNode().getChildNodes();
						int pos = 0;
						while (pos < nodelist.getLength() && !nodelist.item(pos).getNodeName().startsWith("lom")
								&& !nodelist.item(pos).getNodeName().endsWith(":lom")) {
							pos++;
						}
						LomDocument lomDocument = null;
						//      Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
						if (pos < nodelist.getLength()) {
							lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));
						}
						if (lomDocument != null) {
							LomType lomType = lomDocument.getLom();
							if (lomType != null) {
								//HERE WE WORK WITH LOM TYPES!!
								moreItemMetaData = ScormMetadataCollector.allScormItemMetaData(lomType).listScormMetaData();
							}
						}
					}
				}
			}
			//Organizations
			OrganizationsType orgsType = manType.getOrganizations();
			if (orgsType != null) {
				OrganizationType[] orgArray = orgsType.getOrganizationArray();
				for (OrganizationType org : orgArray) {
					MetadataType metaT = org.getMetadata();
					if (metaT != null) {
						moreItemMetaDataAux = readMetaData(metaT);
					}
					if (moreItemMetaDataAux != null) {
						moreItemMetaData.addAll(moreItemMetaDataAux);
					}
					ItemType[] itemTypeArray = org.getItemArray();
					for (ItemType it : itemTypeArray) {
						moreItemMetaDataAux = null;
						MetadataType mt = it.getMetadata();
						if (mt != null) {
							moreItemMetaDataAux = readMetaData(mt);
						}
						if (moreItemMetaDataAux != null) {
							moreItemMetaData.addAll(moreItemMetaDataAux);
						}
					}
				}
			}
			//Resources
			ResourcesType resType = manType.getResources();
			if (resType != null) {
				ResourceType[] resArray = resType.getResourceArray();
				for (ResourceType res : resArray) {
					MetadataType metaT = res.getMetadata();
					if (metaT != null) {
						moreItemMetaDataAux = readMetaData(metaT);
					}
					if (moreItemMetaDataAux != null) {
						moreItemMetaData.addAll(moreItemMetaDataAux);
					}
					FileType[] fileTypeArray = res.getFileArray();
					for (FileType ft : fileTypeArray) {
						moreItemMetaDataAux = null;
						MetadataType mt = ft.getMetadata();
						if (mt != null) {
							moreItemMetaDataAux = readMetaData(mt);
						}
						if (moreItemMetaDataAux != null) {
							moreItemMetaData.addAll(moreItemMetaDataAux);
						}
					}
				}
			}
			return moreItemMetaData;
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

	public Collection<ScormMetaData> getMetaDataFromMetaOrLomXMLFile(MetadataType metaDType) throws ScormException {
		Collection<ScormMetaData> colItemMetaDataToReturn = new ArrayList<ScormMetaData>();
		Collection<ScormMetaData> colItemMetaDataAux = null;
		try {
			colItemMetaDataAux = readMetaData(metaDType);
		} catch (ScormException e) {
			colItemMetaDataAux = null;
		}
		if (colItemMetaDataAux != null) {
			colItemMetaDataToReturn.addAll(colItemMetaDataAux);
		}

		return colItemMetaDataToReturn;
	}

	public Collection<ScormMetaData> getMetaDataFromMetaOrLomXMLFile(File externalFile) throws ScormException//IOException
	{
		try {
			Collection<ScormMetaData> colItemMetaDataToReturn = new ArrayList<ScormMetaData>();
			Collection<ScormMetaData> colItemMetaDataAux = null;
			String fileURL = externalFile.getAbsolutePath();
			if (fileURL.endsWith(".xml")) {
				System.out.println("Checking out this xmlFile: " + fileURL);
				try {
					colItemMetaDataAux = readMetaData(externalFile);
				} catch (ScormException e) {
					//e.printStackTrace();
					colItemMetaDataAux = null;
				}
				if (colItemMetaDataAux != null) {
					colItemMetaDataToReturn.addAll(colItemMetaDataAux);
				} else {
					try {
						colItemMetaDataAux = readLomData(new File(fileURL));
					} catch (ScormException e) {
						colItemMetaDataAux = null;
						throw e;
					}
					if (colItemMetaDataAux != null) {
						colItemMetaDataToReturn.addAll(colItemMetaDataAux);
					}
				}
			}
			return colItemMetaDataToReturn;
		} catch (ScormException e) {
			throw e;
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

//  Here we will be extracting the Content Aggregation Meta-data(context specific data describing the packaged course)
//  out of the imsmanifest.xml file
	public Collection<ScormMetaData> readManifestData(File imsManifestFile, Collection<String> externalMetadataFiles)
			throws ScormException//IOException,XmlException
	{
		Collection<ScormMetaData> colItemMetaDataToReturn = new ArrayList<ScormMetaData>();
		Collection<ScormMetaData> colItemMetaDataAux = null;
		Collection<Node> colLomOrMetaNodes = null;
		ManifestDocument mDoc = null;
		try {
			System.out.println("XML FILE about to be PARSED: " + imsManifestFile.getAbsolutePath());
			mDoc = ManifestDocument.Factory.parse(imsManifestFile);
			if (mDoc != null) {
				ManifestType manType = mDoc.getManifest();
				colLomOrMetaNodes = getAnyLoms(manType);
				//Tratando dos Any Tags
				for (Node node : colLomOrMetaNodes) {
					try {
						colItemMetaDataAux = readMetaData(node);
					} catch (ScormException e) {
						//e.printStackTrace();
						colItemMetaDataAux = null;
					}
					if (colItemMetaDataAux != null) {
						colItemMetaDataToReturn.addAll(colItemMetaDataAux);
					} else {
						try {
							colItemMetaDataAux = readLomData(node);
						} catch (ScormException e) {
							colItemMetaDataAux = null;
							throw e;
						}
						if (colItemMetaDataAux != null) {
							colItemMetaDataToReturn.addAll(colItemMetaDataAux);
						}
					}
				}
				if (manType != null) {
					ManifestType[] manTypeArray = manType.getManifestArray();
					for (ManifestType maniType : manTypeArray) {
						colItemMetaDataAux = readManifestData(maniType);
						if (colItemMetaDataAux != null) {
							colItemMetaDataToReturn.addAll(colItemMetaDataAux);
						}
					}
					colItemMetaDataAux = readManifestData(manType);
					if (colItemMetaDataAux != null) {
						colItemMetaDataToReturn.addAll(colItemMetaDataAux);
					}
				}
				//Agora vamos ler os Ficheiros todos .xml da nossa collection
				//são os referenciados pelo imsmanifest.xml
				if (externalMetadataFiles != null) {
					for (String fileURL : externalMetadataFiles) {
						if (fileURL.endsWith(".xml")) {
							System.out.println("Checking out this xmlFile: " + fileURL);
							try {
								colItemMetaDataAux = readMetaData(new File(fileURL));
							} catch (ScormException e) {
								//e.printStackTrace();
								colItemMetaDataAux = null;
							}
							if (colItemMetaDataAux != null) {
								colItemMetaDataToReturn.addAll(colItemMetaDataAux);
							} else {
								try {
									colItemMetaDataAux = readLomData(new File(fileURL));
								} catch (ScormException e) {
									colItemMetaDataAux = null;
									throw e;
								}
								if (colItemMetaDataAux != null) {
									colItemMetaDataToReturn.addAll(colItemMetaDataAux);
								}
							}
						}
					}
				}
			}
			this.setItemDSpaceMetaData(colItemMetaDataToReturn);
			return this.getItemDSpaceMetaData();
		} catch (ScormException e) {
			throw e;
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION_PARSE, e);
		} catch (XmlException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_XMLEXCEPTION_PARSE, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
	}

}
