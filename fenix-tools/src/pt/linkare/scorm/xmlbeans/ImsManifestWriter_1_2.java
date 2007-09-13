/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.xml.namespace.QName;

import org.adlnet.xsd.adlcpRootv1P2.ScormtypeAttribute;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlOptions;
import org.imsglobal.xsd.imsmdRootv1P2P1.AggregationlevelType;
import org.imsglobal.xsd.imsmdRootv1P2P1.CatalogentryType;
import org.imsglobal.xsd.imsmdRootv1P2P1.CentityType;
import org.imsglobal.xsd.imsmdRootv1P2P1.ClassificationType;
import org.imsglobal.xsd.imsmdRootv1P2P1.ContextType;
import org.imsglobal.xsd.imsmdRootv1P2P1.ContributeType;
import org.imsglobal.xsd.imsmdRootv1P2P1.CopyrightandotherrestrictionsType;
import org.imsglobal.xsd.imsmdRootv1P2P1.CostType;
import org.imsglobal.xsd.imsmdRootv1P2P1.DateType;
import org.imsglobal.xsd.imsmdRootv1P2P1.DescriptionType;
import org.imsglobal.xsd.imsmdRootv1P2P1.DurationType;
import org.imsglobal.xsd.imsmdRootv1P2P1.EducationalType;
import org.imsglobal.xsd.imsmdRootv1P2P1.EntryType;
import org.imsglobal.xsd.imsmdRootv1P2P1.GeneralType;
import org.imsglobal.xsd.imsmdRootv1P2P1.InteractivitytypeType;
import org.imsglobal.xsd.imsmdRootv1P2P1.KeywordType;
import org.imsglobal.xsd.imsmdRootv1P2P1.LangstringType;
import org.imsglobal.xsd.imsmdRootv1P2P1.LearningresourcetypeType;
import org.imsglobal.xsd.imsmdRootv1P2P1.LifecycleType;
import org.imsglobal.xsd.imsmdRootv1P2P1.LocationType;
import org.imsglobal.xsd.imsmdRootv1P2P1.LomDocument;
import org.imsglobal.xsd.imsmdRootv1P2P1.LomType;
import org.imsglobal.xsd.imsmdRootv1P2P1.MetametadataType;
import org.imsglobal.xsd.imsmdRootv1P2P1.PurposeType;
import org.imsglobal.xsd.imsmdRootv1P2P1.RightsType;
import org.imsglobal.xsd.imsmdRootv1P2P1.RoleType;
import org.imsglobal.xsd.imsmdRootv1P2P1.SourceType;
import org.imsglobal.xsd.imsmdRootv1P2P1.StatusType;
import org.imsglobal.xsd.imsmdRootv1P2P1.TechnicalType;
import org.imsglobal.xsd.imsmdRootv1P2P1.TitleType;
import org.imsglobal.xsd.imsmdRootv1P2P1.ValueType;
import org.imsglobal.xsd.imsmdRootv1P2P1.VersionType;
import org.imsproject.xsd.imscpRootv1P1P2.FileType;
import org.imsproject.xsd.imscpRootv1P1P2.ItemType;
import org.imsproject.xsd.imscpRootv1P1P2.ManifestDocument;
import org.imsproject.xsd.imscpRootv1P1P2.ManifestType;
import org.imsproject.xsd.imscpRootv1P1P2.MetadataType;
import org.imsproject.xsd.imscpRootv1P1P2.OrganizationType;
import org.imsproject.xsd.imscpRootv1P1P2.OrganizationsType;
import org.imsproject.xsd.imscpRootv1P1P2.ResourceType;
import org.imsproject.xsd.imscpRootv1P1P2.ResourcesType;
import org.imsproject.xsd.imscpRootv1P1P2.SchemaDocument;
import org.imsproject.xsd.imscpRootv1P1P2.SchemaversionDocument;

import pt.linkare.scorm.utils.MultiStringKey;
import pt.linkare.scorm.utils.ScormException;
import pt.linkare.scorm.utils.ScormMetaData;
import pt.linkare.scorm.utils.ScormMetaDataHash;
import pt.linkare.scorm.utils.ScormMetaInfoEnum;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;
import javax.activation.MimetypesFileTypeMap;

/**
 * @author Oscar Ferreira - Linkare TI
 * 
 */
public class ImsManifestWriter_1_2 {

	private static final XmlOptions xmlOptions = new XmlOptions();

	// Manifest Document
	private ManifestDocument manDoc = null;

	// Utilizamos ScormMetaDataHash em vez de Collection<ScormMetaData>
	private ScormMetaDataHash scormMetadataHasher = null;

	private LomType rootLomType = null;

	// The Constructor of the Class must create the ManifestDocument
	private ImsManifestWriter_1_2() {
		try {
			HashMap<String, String> namespacePrefixes = new HashMap<String, String>();
			namespacePrefixes.put("http://www.imsproject.org/xsd/imscp_rootv1p1p2", "");
			namespacePrefixes.put("http://www.adlnet.org/xsd/adlcp_rootv1p2", "adlcp");
			namespacePrefixes.put("http://www.imsglobal.org/xsd/imsmd_rootv1p2p1", "imsmd");
			namespacePrefixes.put("http://ltsc.ieee.org/xsd/LOM", "lom");
			namespacePrefixes.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
			namespacePrefixes.put("http://www.w3.org/2001/XMLSchema", "xs");
			xmlOptions.setCharacterEncoding("UTF8");
			xmlOptions.setSavePrettyPrint();
			xmlOptions.setSavePrettyPrintIndent(4);
			xmlOptions.setSaveAggressiveNamespaces();
			xmlOptions.setSaveSuggestedPrefixes(namespacePrefixes);
			xmlOptions.setSaveNamespacesFirst();

			manDoc = ManifestDocument.Factory.newInstance(xmlOptions);
			manDoc.addNewManifest();
			XmlCursor cursor = manDoc.newCursor();
			if (cursor.toFirstChild()) {
				cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance",
						"schemaLocation"),
						"http://www.imsglobal.org/xsd/imsmd_rootv1p2p1 imsmd_rootv1p2p1.xsd "
								+ "http://www.imsproject.org/xsd/imscp_rootv1p1p2 imscp_rootv1p1p2.xsd "
								+ "http://www.adlnet.org/xsd/adlcp_rootv1p2 adlcp_rootv1p2.xsd ");
			}

		} catch (Exception e) {
			new ScormException("An error was thrown while creating a new instance of ManifestDocument!", e)
					.printStackTrace();
		}
	}

	// The Constructor of the Class must create the ManifestDocument
	public ImsManifestWriter_1_2(String manifestIdentifier, ScormMetaDataHash scormMetadataHash)
			throws Exception {
		this();
		ManifestType manType = this.getManDoc().getManifest();
		manType.setIdentifier(manifestIdentifier);
		setScormMetadataHasher(scormMetadataHash);
	}

	public ManifestDocument getManDoc() {
		return manDoc;
	}

	public void setManDoc(ManifestDocument manDoc) {
		this.manDoc = manDoc;
	}

	private void fillLangStringType(String language, String value, LangstringType lStrType)
			throws ScormException {
		if (language == null)
			language = "x-none";
		else if (!language.equals("x-none")) {
			String languageChangedToJava = language.replaceAll("\\-", "_");

			boolean foundValid = false;
			for (Locale l : Locale.getAvailableLocales()) {
				if (l.toString().equals(languageChangedToJava)) {
					foundValid = true;
					break;
				}
			}
			if (!foundValid)
				throw new ScormException("Language code " + language + " is an invalid code!");

		}

		lStrType.setLang(language);
		lStrType.setStringValue(value);
	}

	private void fillLOMSourceValue(String language, String value, SourceType sourceType, ValueType valueType)
			throws ScormException {
		fillLangStringType(null, ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION, sourceType
				.addNewLangstring());
		fillLangStringType(language, value, valueType.addNewLangstring());
	}

	private String encodeInVcard(String original) {
		if (original == null)
			return null;
		String transformed = original.trim();
		if (transformed.length() <= "BEGIN:VCARD".length()
				|| !transformed.substring(0, "BEGIN:VCARD".length()).equalsIgnoreCase("BEGIN:VCARD")) {// not
																										// a
																										// vCARD
																										// do
																										// it
			if (transformed.indexOf(":") != -1) {// just enclose it in vCard
				transformed = "BEGIN:VCARD " + transformed + " END:VCARD";
			} else {// assume it is a name
				transformed = "BEGIN:VCARD FN:" + transformed + " END:VCARD";
			}
			return transformed;
		} else
			return transformed;

	}

	public File createManifest(String baseDir) {
		try {
			// int count =-1;
			ManifestDocument manDoc = this.getManDoc();
			ManifestType manType = manDoc.getManifest();
			manType.addNewOrganizations();
			manType.addNewResources();
			MetadataType metaType = manType.addNewMetadata();
			SchemaversionDocument schemaVersionDoc = SchemaversionDocument.Factory.newInstance(xmlOptions);
			SchemaDocument schemaDoc = SchemaDocument.Factory.newInstance(xmlOptions);
			schemaDoc.setSchema("ADL SCORM");
			schemaVersionDoc.setSchemaversion("1.2");
			// Setting the Schemas for MetaData
			metaType.set(schemaDoc);
			metaType.set(schemaVersionDoc);
			// Preencher LOM from ItemMetaData collection
			LomDocument lomDoc = LomDocument.Factory.newInstance(xmlOptions);
			LomType lom = lomDoc.addNewLom();
			rootLomType = lom;

			// NOTE Sub-Tags of LOM
			GeneralType general = lom.addNewGeneral();
			LifecycleType lifecycle = lom.addNewLifecycle();
			TechnicalType techType = lom.addNewTechnical();
			EducationalType eduType = lom.addNewEducational();
			MetametadataType metaMetaData = lom.addNewMetametadata();
			RightsType rightsType = lom.addNewRights();
			// ClassificationType classType=lom.addNewClassification();
			// Variables used for Contributor/Date Insertions

			// NOTE Data added only once at most...
			TitleType tLom = general.addNewTitle();
			// Counters:Used for counting elements that should only have 0 or 1
			int sizeCounter = 0, statusCounter = 0, durationCounter = 0, versionCounter = 0, costCounter = 0, copyrightCounter = 0, aggregationCounter = 0, interactivityTypeCounter = 0, descriptionCounter = 0;

			for (ScormMetaData currentMeta : getScormMetadataHasher().listScormMetaData()) {
				// NIVEL 1
				if (currentMeta.getElement().equals("title")) {
					LangstringType lString = tLom.addNewLangstring();
					lString.setLang(currentMeta.getLang());
					lString.setStringValue(currentMeta.getValues()[0]);
				} else if (currentMeta.getElement().equals("identifier")) {
					for (String value : currentMeta.getValues()) {
						CatalogentryType catEntType = general.addNewCatalogentry();
						// May not be equal to the Value of Catalog on the
						// Reader
						// because if value was not a DSpace Metadata Qualifier
						// it will be the value "other"
						catEntType.setCatalog(currentMeta.getQualifier());
						EntryType entryType = catEntType.addNewEntry();
						fillLangStringType(currentMeta.getLang(), value, entryType.addNewLangstring());
					}

				} else if (currentMeta.getElement().equals("description")) {
					for (String value : currentMeta.getValues()) {
						DescriptionType dType = general.addNewDescription();
						fillLangStringType(currentMeta.getLang(), value, dType.addNewLangstring());
					}

				} else if (currentMeta.getElement().equals("subject")) {
					for (String value : currentMeta.getValues()) {
						KeywordType kType = general.addNewKeyword();
						fillLangStringType(currentMeta.getLang(), value, kType.addNewLangstring());
					}

				} else if (currentMeta.getElement().equals("version")) {
					if (versionCounter > 0)
						throw new ScormException(
								"There can be no more than one(1) Version element in the ImsManifest File");
					versionCounter++;
					VersionType versionType = lifecycle.addNewVersion();
					fillLangStringType(currentMeta.getLang(), currentMeta.getValues()[0], versionType
							.addNewLangstring());

				} else if (currentMeta.getElement().equals("status")) {
					if (statusCounter > 0)
						throw new ScormException(
								"There can be no more than one(1) Status element in the ImsManifest File");
					statusCounter++;
					StatusType statusType = lifecycle.addNewStatus();
					String value = currentMeta.getValues()[0];
					if (value == null || value.trim().equals(""))
						value = "Final";

					fillLOMSourceValue(null, value, statusType.addNewSource(), statusType.addNewValue());
				} else if (currentMeta.getElement().equals("metadatascheme")) {
					for (String value : currentMeta.getValues()) {
						metaMetaData.addMetadatascheme(value);
					}

				} else if (currentMeta.getElement().equals("format")) {
					for (String value : currentMeta.getValues()) {
						techType.addNewFormat().setStringValue(value);
					}
				} else if (currentMeta.getElement().equals("location")) {
					for (String value : currentMeta.getValues()) {
						LocationType locType = techType.addNewLocation();
						String type = currentMeta.getQualifier();
						if ("URI".equalsIgnoreCase(type)) {
							locType.setType(LocationType.Type.URI);
						} else if ("TEXT".equalsIgnoreCase(type)) {
							locType.setType(LocationType.Type.TEXT);
						} else
							throw new ScormException("Unknown location type " + type
									+ " - it should be one of TEXT,URI");
						locType.setStringValue(value);
					}

				} else if (currentMeta.getElement().equals("rights")) {
					if (currentMeta.getQualifier().equalsIgnoreCase("cost")) {
						// Here we create a Cost Item
						if (costCounter > 0)
							throw new ScormException(
									"There should one(1) and only one(1) Cost element in the ImsManifest File");
						costCounter++;
						CostType cType = rightsType.addNewCost();

						String value = currentMeta.getValues()[0];
						if (value == null || value.trim().length() == 0 || value.equalsIgnoreCase("no"))
							value = "no";
						else
							value = "yes";

						fillLOMSourceValue(null, value, cType.addNewSource(), cType.addNewValue());
					} else if (currentMeta.getQualifier().compareToIgnoreCase("copyright") == 0) {
						if (copyrightCounter > 0)
							throw new ScormException(
									"There should one(1) and only one(1) CopyrightandOtherRestriction element in the ImsManifest File");
						copyrightCounter++;

						CopyrightandotherrestrictionsType copyType = rightsType
								.addNewCopyrightandotherrestrictions();

						String value = currentMeta.getValues()[0];
						if (value == null || value.trim().length() == 0 || value.equalsIgnoreCase("no"))
							value = "no";
						else
							value = "yes";

						fillLOMSourceValue(null, value, copyType.addNewSource(), copyType.addNewValue());
					} else if (currentMeta.getQualifier().compareToIgnoreCase("description") == 0) {
						if (descriptionCounter > 0)
							throw new ScormException(
									"There should at most one(1) Description element in the ImsManifest File");
						descriptionCounter++;
						DescriptionType dType = rightsType.addNewDescription();
						fillLangStringType(currentMeta.getLang(), currentMeta.getValues()[0], dType
								.addNewLangstring());
					}

				}

				// NIVEL 2

				else if (currentMeta.getElement().equals("language")) {
					for (String value : currentMeta.getValues()) {
						general.addLanguage(value);
					}
				} else if (currentMeta.getElement().equals("aggregationLevel")) {
					if (aggregationCounter > 0)
						throw new ScormException(
								"There can only be at most one(1) AggregationLevel element in the ImsManifest File");
					aggregationCounter++;
					AggregationlevelType aggrLevelType = general.addNewAggregationlevel();
					fillLOMSourceValue(currentMeta.getLang(), currentMeta.getValues()[0], aggrLevelType
							.addNewSource(), aggrLevelType.addNewValue());
				}
				/**
				 * O ImsManifestReader classifica estes elementos do seguinte
				 * modo For the Contributor Element we must be carefull all date
				 * Elements that are from a specific contributor must have the
				 * Qualifier and lang according to the the dates qualifier and
				 * lang.This must be met so that contributor dates are placed in
				 * the correct contributor. Element | Qualifier|Lang | Value
				 * Example: ("contributor","author","en","Mr. Contributor 1")
				 * ("contributor","author","en","Mr. Contributor 2") the date
				 * must be: ("date","created","en",{"11-12-2006","Description
				 * for Contributor 1/-/Descri��o para o contribuidor 1 isto vem
				 * quando ao ler temos mais do que uma langstring na descri��o e
				 * neste caso perdemos o valor do seu lang colocando somente o
				 * lang do contributor" ,"12-12-2006","Description for
				 * Contrinutor 2"}) Why must we be carefull??For the reason that
				 * the Element,Qualifier e Lang are combined Key value to search
				 * the correct Values we need. The Qualifier for a Contributor
				 * may be "author","editor" or anything else("other") these
				 * values are matched up to the dates Element values sequencialy
				 * they are "created","copyright" or "other", here it is obvious
				 * that just the Qualifier serves to me a good filter except for
				 * when the Contributor is anything but an "author" or and
				 * "editor" because then they will be interpreted to be an
				 * "other" for dates and here if the lang is null or the same we
				 * will receive a date element with many values each one
				 * referring to Contributor in the order he was entered and here
				 * we see that order is a necessary factor when the filters do
				 * not help. The Order by which we add the contributores and the
				 * order we add the dates Example:
				 * ("contributor","publisher","","Mrs. Contributor Publisher")
				 * ("contributor","validator",null,"Mrs. Contributor Writer")
				 * the date must be: ("date","other","",{"24-12-2006","Descri��o
				 * desta data para Mrs. Contributor Publisher"
				 * ,"25-12-2006","Descri��o desta data para Mrs. Contributor
				 * Writer"}) NOTA1: Lang com null ou ""(vazio) vai acabar por
				 * ser "x-none" da� s�o considerados identicos na filtragem. *
				 * NOTA2: Terei de ter esses cuidados aqui quando procurando por
				 * pelo o date correspondente.
				 */
				else if (currentMeta.getElement().equals("contributor")) {
					int countVal = 0;
					String[] dateValues = getScormMetadataHasher()
							.getValues(
									"date",
									currentMeta.getQualifier().compareToIgnoreCase("author") == 0 ? "created"
											: (currentMeta.getQualifier().compareToIgnoreCase("editor") == 0 ? "copyright"
													: "other"), currentMeta.getLang());
					for (String contribValue : currentMeta.getValues()) {
						ContributeType cType = lifecycle.addNewContribute();
						RoleType rType = cType.addNewRole();
						String type = ScormMetaInfoEnum.translateFromDCToScorm(currentMeta.getQualifier(),
								ScormMetaInfoEnum.DUBLIN_CORE_QUALIFIER_CONTRIBUTOR,
								ScormMetaInfoEnum.DC2SCORM_QUALIFIER_CONTRIBUTOR_MAP);
						fillLOMSourceValue(currentMeta.getLang(), type, rType.addNewSource(), rType
								.addNewValue());
						CentityType centityType = cType.addNewCentity();
						centityType.setVcard(encodeInVcard(contribValue));
						if (dateValues != null && dateValues.length > 0) {
							DateType dType = cType.addNewDate();
							dType.setDatetime(dateValues[countVal]);
							fillLangStringType(currentMeta.getLang(), dateValues[countVal + 1], dType
									.addNewDescription().addNewLangstring());
						}
						countVal += 2;
					}
				} else if (currentMeta.getElement().equals("duration")) {
					if (durationCounter > 0)
						throw new ScormException(
								"There can only be at most one(1) Duration element in the ImsManifest File");
					durationCounter++;
					DurationType durationType = techType.addNewDuration();
					String[] durationVal = currentMeta.getValues();
					for (int i = 0; i < durationVal.length; i = +2) {
						durationType.setDatetime(durationVal[i]);
						DescriptionType descType = durationType.addNewDescription();
						fillLangStringType(currentMeta.getLang(), durationVal[++i], descType
								.addNewLangstring());
					}

				} else if (currentMeta.getElement().equals("interactivityType")) {
					if (interactivityTypeCounter > 0)
						throw new ScormException(
								"There can be no more than one(1) InteractivityType element in the ImsManifest File");
					interactivityTypeCounter++;
					InteractivitytypeType iType = eduType.addNewInteractivitytype();
					fillLOMSourceValue(currentMeta.getLang(), currentMeta.getValues()[0], iType
							.addNewSource(), iType.addNewValue());
				} else if (currentMeta.getElement().equals("type")) {
					for (String value : currentMeta.getValues()) {
						LearningresourcetypeType learnType = eduType.addNewLearningresourcetype();
						fillLOMSourceValue(currentMeta.getLang(), value, learnType.addNewSource(), learnType
								.addNewValue());
					}
				} else if (currentMeta.getElement().equals("context")) {
					for (String value : currentMeta.getValues()) {
						ContextType cType = eduType.addNewContext();
						fillLOMSourceValue(currentMeta.getLang(), value, cType.addNewSource(), cType
								.addNewValue());
					}
				} else if (currentMeta.getElement().equals("size")) {
					if (sizeCounter > 0)
						throw new ScormException(
								"There can be no more than one(1) Size element in the ImsManifest File");
					sizeCounter++;
					techType.setSize(Integer.parseInt(currentMeta.getValues()[0]));
				}
			}
			metaType.set(lomDoc);
			File manifestFile = new File(baseDir, "imsmanifest.xml");
			manDoc.save(manifestFile, xmlOptions);
			return manifestFile;
		} catch (Exception e) {
			new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e).printStackTrace();
		}
		return null;
	}

	// Add Resource file information to imsManifest file
	public File addResource2ImsManifest(File imsManifest, File resFile) throws ScormException {

		try {
			String resourceRelativeLocation = FileUtils.makeRelativePath(imsManifest.getAbsoluteFile()
					.getParentFile().getAbsolutePath(), resFile.getAbsoluteFile().getAbsolutePath());
			ManifestType manType = manDoc.getManifest();
			File tempDir = imsManifest.getAbsoluteFile().getParentFile();

			String resourceRelativeLocationStartupHtml = resourceRelativeLocation + "_startup_sco.html";
			String resourceRelativeLocationJScript = resourceRelativeLocation.substring(0,
					resourceRelativeLocation.indexOf(resFile.getName()))
					+ "APIWrapper.js";

			FileOutputStream outFileOS;
			InputStream inFileIS;
			try {
				ByteArrayOutputStream bosHTML = new ByteArrayOutputStream();

				outFileOS = new FileOutputStream(new File(tempDir, resourceRelativeLocationStartupHtml));
				inFileIS = getClass().getClassLoader().getResourceAsStream("scobase/sco_startup.html");
				FileUtils.copyInputStreamToOutputStream(inFileIS, bosHTML);
				bosHTML.flush();
				String htmlContent = bosHTML.toString();
				bosHTML.close();

				htmlContent = htmlContent.replaceAll("<!-- TITLE_COURSE_HERE -->", rootLomType.getGeneral()
						.getTitle().getLangstringArray()[0].getStringValue());
				htmlContent = htmlContent.replaceAll("<!-- TITLE_FILE_HERE -->", "Open Educational Resource");
				htmlContent = htmlContent.replaceAll("<!-- FILENAME_HERE -->", resourceRelativeLocation);

				PrintWriter pw = new PrintWriter(new OutputStreamWriter(outFileOS));
				pw.print(htmlContent);
				pw.flush();
				pw.close();

				outFileOS = new FileOutputStream(new File(tempDir, resourceRelativeLocationJScript));
				inFileIS = getClass().getClassLoader().getResourceAsStream("scobase/APIWrapper.js");
				FileUtils.copyInputStreamToOutputStream(inFileIS, outFileOS);
				outFileOS.close();

			} catch (FileNotFoundException e) {
				throw new ScormException("Unable to copy schema files to output dir", e);
			} catch (IOException e) {
				throw new ScormException("Unable to copy schema files to output dir", e);
			}

			ResourcesType resType = null;
			ResourceType rType = null;
			FileType fType = null;
			resType = manType.getResources();
			if (resType == null)
				resType = manType.addNewResources();

			rType = resType.addNewResource();
			// Setting Attributes for ResourceType
			ScormtypeAttribute scormType = ScormtypeAttribute.Factory.newInstance(xmlOptions);
			scormType.setScormtype(ScormtypeAttribute.Scormtype.SCO);
			rType.set(scormType);

			rType.setIdentifier(resourceRelativeLocation.replace('.', '_') + "_Resource_ID");
			rType.setType("webcontent");
			// Adding File
			fType = rType.addNewFile();
			fType.setHref(resourceRelativeLocation);

			fType = rType.addNewFile();
			fType.setHref(resourceRelativeLocationStartupHtml);

			fType = rType.addNewFile();
			fType.setHref(resourceRelativeLocationJScript);

			rType.setHref(resourceRelativeLocationStartupHtml);

			File lomFile = instrospectMetaForFile(resourceRelativeLocation, resFile, manDoc);
			String lomFileRelativePath = FileUtils.makeRelativePath(imsManifest.getAbsoluteFile()
					.getParentFile().getAbsolutePath(), lomFile.getAbsoluteFile().getAbsolutePath());

			org.adlnet.xsd.adlcpRootv1P2.LocationDocument adlCpLocationDoc = org.adlnet.xsd.adlcpRootv1P2.LocationDocument.Factory
					.newInstance(xmlOptions);
			adlCpLocationDoc.setLocation(lomFileRelativePath);

			MetadataType rMeta = rType.addNewMetadata();
			rMeta.set(adlCpLocationDoc);
			rMeta.setSchema("ADL SCORM");
			rMeta.setSchemaversion("1.2");

			OrganizationsType organizationsType = manType.getOrganizations();
			if (organizationsType == null) {
				organizationsType = manType.addNewOrganizations();
				organizationsType.setDefault(resourceRelativeLocation.replace('.', '_') + "_Org_ID");
			} else if (organizationsType.getOrganizationArray() == null
					|| organizationsType.getOrganizationArray().length == 0) {
				organizationsType.setDefault(resourceRelativeLocation.replace('.', '_') + "_Org_ID");
			}
			OrganizationType orgType = organizationsType.addNewOrganization();
			orgType.setIdentifier(resourceRelativeLocation.replace('.', '_') + "_Org_ID");
			orgType.setTitle(resFile.getName().substring(0, resFile.getName().lastIndexOf('.')));
			ItemType itemType = orgType.addNewItem();
			itemType.setIdentifier(resourceRelativeLocation.replace('.', '_') + "_Item_ID");
			itemType.setTitle(resFile.getName().substring(0, resFile.getName().lastIndexOf('.')));
			itemType.setIsvisible(true);
			itemType.setIdentifierref(resourceRelativeLocation.replace('.', '_') + "_Resource_ID");

			manDoc.save(imsManifest, xmlOptions);
		} catch (IOException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_IOEXCEPTION_PARSE, e);
		} catch (NullPointerException e) {
			throw new ScormException(ScormException.IMS_MANIFEST_NULLPOINTEREXCEPTION, e);
		} catch (Exception e) {
			throw new ScormException(ScormException.IMS_MANIFEST_EXCEPTION, e);
		}
		return null;
	}

	private File instrospectMetaForFile(String resourceRelativeLocation, File resFile, ManifestDocument manDoc)
			throws IOException, ScormException {

		
		String fileContentType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(resFile.getName());
		fileContentType = (fileContentType == null ? "application/octet-stream" : fileContentType);
		long fileSizeBytes = resFile.length();

		LomDocument lomDocument = LomDocument.Factory.newInstance(xmlOptions);
		LomType lom = lomDocument.addNewLom();

		XmlCursor cursor = lomDocument.newCursor();
		if (cursor.toFirstChild()) {
			cursor.setAttributeText(new QName("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation"),
					"http://www.imsglobal.org/xsd/imsmd_rootv1p2p1 imsmd_rootv1p2p1.xsd "
							+ "http://www.imsproject.org/xsd/imscp_rootv1p1p2 imscp_rootv1p1p2.xsd "
							+ "http://www.adlnet.org/xsd/adlcp_rootv1p2 adlcp_rootv1p2.xsd ");
		}

		lom.setGeneral((GeneralType) rootLomType.getGeneral().copy());
		lom.setLifecycle((LifecycleType) rootLomType.getLifecycle().copy());
		lom.setMetametadata((MetametadataType) rootLomType.getMetametadata().copy());
		lom.setEducational((EducationalType) rootLomType.getEducational().copy());
		lom.setRights((RightsType) rootLomType.getRights().copy());

		ClassificationType classificationType = lom.addNewClassification();
		PurposeType pType = classificationType.addNewPurpose();
		fillLOMSourceValue(null, "Educational Objective", pType.addNewSource(), pType.addNewValue());
		fillLangStringType(null, "automatically created by Fenix System", classificationType
				.addNewDescription().addNewLangstring());
		fillLangStringType(null, "fenix", classificationType.addNewKeyword().addNewLangstring());

		TechnicalType technical = lom.addNewTechnical();
		technical.addNewFormat().setStringValue(fileContentType);
		LocationType uriLocation = technical.addNewLocation();
		uriLocation.setType(LocationType.Type.URI);
		uriLocation.setStringValue(resourceRelativeLocation);
		technical.setSize((int) fileSizeBytes);

		String lomDocLocation = resFile.getName() + "_ScoMetaData.xml";
		File lomFile = new File(resFile.getAbsoluteFile().getParentFile(), lomDocLocation);
		lomDocument.save(lomFile, xmlOptions);

		return lomFile;
	}

	/**
	 * Receives the collection containing errors found during validation and
	 * print the errors to the console.
	 * 
	 * @param validationErrors
	 *            The validation errors.
	 */
	public void printErrors(ArrayList validationErrors) {
		System.out.println("Errors discovered during validation: \n");
		Iterator iter = validationErrors.iterator();
		while (iter.hasNext()) {
			System.out.println(">> " + iter.next() + "\n");
		}
	}

	public ScormMetaDataHash getScormMetadataHasher() {
		return scormMetadataHasher;
	}

	public void setScormMetadataHasher(ScormMetaDataHash scormMetadataHasher) {
		this.scormMetadataHasher = scormMetadataHasher;
		insertMissingFields();
	}

	/**
	 * 
	 */
	private void insertMissingFields() {
		createEmptyIfMissing("description");
		createDefaultIfMissing("metadatascheme", ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION);
		createDefaultIfMissing("format", "application/octet-stream");
		createDefaultIfMissing("location","http://fenix.ist.utl.pt/privado");
		createDefaultIfMissing("rights", "cost", "no");
		createDefaultIfMissing("rights", "copyright", "no");
		createDefaultIfMissing("status", "Final");
	}

	/**
	 * @param element
	 */
	private void createEmptyIfMissing(String element) {
		if (isMissing(element))
			scormMetadataHasher.put(element, null, null, "");
	}

	/**
	 * @param element
	 * @return true if the element is missing in the collection
	 */
	private boolean isMissing(String element) {
		Set<MultiStringKey> setOfScormElements = scormMetadataHasher.keySetWithElement(element);
		if (setOfScormElements != null && setOfScormElements.size() > 0)
			return false;
		return true;
	}

	/**
	 * @param element
	 * @param qualifier
	 */
	private void createEmptyIfMissing(String element, String qualifier) {
		if (isMissing(element, qualifier))
			scormMetadataHasher.put(element, qualifier, null, "");
	}

	/**
	 * @param element
	 * @param qualifier
	 * @return true if the element/qualifier is missing
	 */
	private boolean isMissing(String element, String qualifier) {
		Set<MultiStringKey> setOfScormElements = scormMetadataHasher.keySetWithElementAndQualifier(element,
				qualifier);
		if (setOfScormElements != null && setOfScormElements.size() > 0)
			return false;
		return true;
	}

	/**
	 * @param element
	 * @param value
	 */
	private void createDefaultIfMissing(String element, String value) {
		if (isMissing(element))
			scormMetadataHasher.put(element, null, null, value);
	}

	/**
	 * @param element
	 * @param qualifier
	 * @param value
	 */
	private void createDefaultIfMissing(String element, String qualifier, String value) {
		if (isMissing(element, qualifier))
			scormMetadataHasher.put(element, qualifier, null, value);
	}

	public static class ImsManifestWriter_1_2_Test {

		public static void main(String[] args) throws Exception {
			ScormMetaDataHash scormMetaHasher = new ScormMetaDataHash();
			ImsManifestWriter_1_2 manWriter = new ImsManifestWriter_1_2("LINKARE_TI_ID", scormMetaHasher);
			scormMetaHasher.put("title", null, "pt", "titalo 1");
			scormMetaHasher.put("title", null, "en", new String[] { "title 1", "tiiitllleee 2", "Le titre" });

			scormMetaHasher.put("subject", null, "pt", new String[] { "subject_value", "subject_value2",
					"palavra chave 1" });
			scormMetaHasher.put("description", null, "en",
					new String[] { "Description...", "Description...2" });
			scormMetaHasher.put("date", "created", null, new String[] { "27-07-2006", "66-07-2006" });
			scormMetaHasher.put("contributor", "author", "creator", "jose manuel das couves");
			scormMetaHasher.put("contributor", "author", "author", "joao das gaivotas");
			scormMetaHasher.put("contributor", "publisher", "en", "joao das gaivotas");
			scormMetaHasher.put("identifier", "citation", "br", new String[] {
					"Oscar J. Ferreira, Rua dos Pocos", "joaquin manel, em casa" });
			scormMetaHasher.put("identifier", "lalala", "br", "oscar J. Ferreira, Rua dos barcos");
			scormMetaHasher.put("version", null, "pt", new String[] { "v1.0", "v1.2" });
			scormMetaHasher.put("metadatascheme", null, null, new String[] { "metaschema1", "metaschema2",
					"metaschema3" });
			scormMetaHasher.put("location", "something", null, new String[] { "http://www.linux.com",
					"http://www.apache.com", "http://www.microsoft.crap" });
			scormMetaHasher.put("rights", "cost", "pt", new String[] { "credit", "transfer" });
			try {
				manWriter.createManifest("/home/pcma/Desktop");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
