/**
 * 
 */
package pt.linkare.scorm.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;

/**
 * @author Óscar Ferreira - LINKARE TI
 * 
 */
/*
 * This Enum will permit connection between both Form MetaInfo Input Variables
 * and the Element names for Scorm FileSetMetaData.By using it's objects
 * Key=Value of the Forms Property;Value=ItemMetadat.Elements Value
 */
public enum ScormMetaInfoEnum {

	/*
	     * //Nunca deve ser utilizado IDENTIFIER ("identifier", new
	     * FileSetMetaData("identifier",null,null,new String[0])),
	     * GENERAL_STRUCTURE ("generalStructure", new
	     * FileSetMetaData("general_structure",null,null,new String[0])),
	     * METAMETADATA_IDENTIFIER ("metametadataIdentifier", new
	     * FileSetMetaData("metametadata_identifier",null,null,new String[0])),
	     * METAMETADATA_CATALOG ("metametadataCatalog", new
	     * FileSetMetaData("metametadata_catalog",null,null,new String[0])),
	     * METAMETADATA_ENTRY ("metametadataEntry", new
	     * FileSetMetaData("metametadata_entry",null,null,new String[0])),
	     * METAMETADATA_CONTRIBUTO_ROLE ("metametadataContributorRole", new
	     * FileSetMetaData("metametadata_contributorRole",null,null,new
	     * String[0])), METAMETADATA_CONTRIBUTOR_VCARD
	     * ("metametadataContributorvcard", new
	     * FileSetMetaData("metametadata_contributorvcard",null,null,new
	     * String[0])), METAMETADATA_CONTRIBUTOR_DATE
	     * ("metametadataContributordate", new
	     * FileSetMetaData("metametadata_contributordate",null,null,new
	     * String[0])), METAMETADATA_LANGUAGE ("metametadataLanguage", new
	     * FileSetMetaData("metametadata_language",null,null,new String[0])),
	     * TECHNICAL_SIZE ("technicalSize", new
	     * FileSetMetaData("technical_size",null,null,new String[0])),
	     * TECHNICAL_TYPE ("technicalType", new
	     * FileSetMetaData("technical_type",null,null,new String[0])),
	     * TECHNICAL_NAME ("technicalName", new
	     * FileSetMetaData("technical_name",null,null,new String[0])),
	     * TECHNICAL_MINIMUM_VERSION ("technicalMinimumversion", new
	     * FileSetMetaData("technical_minimumversion",null,null,new String[0])),
	     * TECHNICAL_MAXIMUM_VERSION ("technicalMaximumversion", new
	     * FileSetMetaData("technical_maximumversion",null,null,new String[0])),
	     * TECHNICAL_INSTALLATION ("technicalInstallation", new
	     * FileSetMetaData("technical_installation",null,null,new String[0])),
	     * TECHNICAL_OTHER_PLATFORM_REQUIREMENTS
	     * ("technicalOtherplatformrequirements", new
	     * FileSetMetaData("technical_otherplatformrequirements",null,null,new
	     * String[0])), EDUCATIONAL_INTERACTIVITY_LEVEL
	     * ("educationalInteractivitylevel", new
	     * FileSetMetaData("educational_interactivitylevel",null,null,new
	     * String[0])), EDUCATIONAL_SEMANTIC_DENSITY
	     * ("educationalSemanticdensity", new
	     * FileSetMetaData("educational_semanticdensity",null,null,new
	     * String[0])), EDUCATIONAL_INTENDED_END_USER_ROLE
	     * ("educationalIntendedenduserrole", new
	     * FileSetMetaData("educational_intendedenduserrole",null,null,new
	     * String[0])), EDUCATIONAL_TYPICAL_AGE_RANGE
	     * ("educationalTypicalagerange", new
	     * FileSetMetaData("educational_typicalagerange",null,null,new
	     * String[0])), EDUCATIONAL_DIFICULTY ("educationalDifficulty", new
	     * FileSetMetaData("educational_difficulty",null,null,new String[0])),
	     * EDUCATIONAL_TYPICAL_LEARNING_TIME ("educationalTypicallearningtime",
	     * new FileSetMetaData("educational_typicallearningtime",null,null,new
	     * String[0])), EDUCATIONAL_DESCRIPTION ("educationalDescription", new
	     * FileSetMetaData("educational_description",null,null,new String[0])),
	     * EDUCATIONAL_LANGUAGE ("educationalLanguage", new
	     * FileSetMetaData("educational_language",null,null,new String[0])),
	     * RELATIONAL_RESOURCE_CATALOG_ENTRY_CATALOG
	     * ("relationResourceCatalogEntryCatalog", new
	     * FileSetMetaData("relationResourceCatalogEntryCatalog",null,null,new
	     * String[0])), RELATIONAL_RESOURCE_DESCRIPTION
	     * ("relationResourceDescription", new
	     * FileSetMetaData("relationResourceDescription",null,null,new
	     * String[0])), ANNOTATION_PERSON ("annotationPerson", new
	     * FileSetMetaData("annotation_person",null,null,new String[0])),
	     * ANNOTATION_DATE ("annotationDate", new
	     * FileSetMetaData("annotation_date",null,null,new String[0])),
	     * ANNOTATION_DESCRIPTION ("annotationDescription", new
	     * FileSetMetaData("annotation_description",null,null,new String[0])),
	     * CLASSIFICATION_TAXONPATH_SOURCE ("classificationTaxonpathSource", new
	     * FileSetMetaData("classification_taxonpath_source",null,null,new
	     * String[0])), CLASSIFICATION_TAXON_ID ("classificationTaxonId", new
	     * FileSetMetaData("classification_taxon_id",null,null,new String[0])),
	     * CLASSIFICATION_DESCRIPTION ("classificationDescription", new
	     * FileSetMetaData("classification_description",null,null,new
	     * String[0])), CLASSIFICATION_KEYWORD ("classificationKeyword", new
	     * FileSetMetaData("classification_keyword",null,null,new String[0])),
	     * COVERAGE ("coverage", new FileSetMetaData("coverage",null,null,new
	     * String[0])), RESOURCE_FILE_CREATION_DATE ("resourceFileCreationDate",
	     * new FileSetMetaData("resourceFileCreationDate",null,null,new
	     * String[0])), RELATION_KIND ("relationKind", new
	     * FileSetMetaData("relationKind",null,null,new String[0])),
	     * RELATION_RESOURCE_CATALOG_ENTRY_ENTRY
	     * ("relationResourceCatalogEntryEntry", new
	     * FileSetMetaData("relation",null,null,new String[0])),
	     */

	TITLE("title", new FileSetMetaData("title", null, null, new String[0])), KEYWORD("keyword", new FileSetMetaData("subject",
			null, null, new String[0])), GENERAL_LANGUAGE("generalLanguage", new FileSetMetaData("language", null, null,
			new String[0])), GENERAL_CATALOG("generalCatalog", new FileSetMetaData("identifier", null, null, new String[0])),
	GENERAL_ENTRY("generalEntry", new FileSetMetaData("generalEntry", null, null, new String[0])), GENERAL_AGGREGATIONLEVEL(
			"generalAggregationLevel", new FileSetMetaData("aggregationLevel", null, null, new String[0])), LIFECYCLE_VERSION(
			"lifecycleVersion", new FileSetMetaData("version", null, null, new String[0])), LIFECYCLE_STATUS("lifecycleStatus",
			new FileSetMetaData("status", null, null, new String[0])),

	METAMETADATA_METADATASCHEMA("metametadataMetadataschema", new FileSetMetaData("metadatascheme", null, null, new String[0])),

	TECHNICAL_LOCATION("technicalLocation", new FileSetMetaData("location", "uri", null, new String[0])), TECHNICAL_SIZE(
			"technicalSize", new FileSetMetaData("size", null, null, new String[0])), TECHNICAL_DURATION("technicalDuration",
			new FileSetMetaData("duration", null, null, new String[0])), EDUCATIONAL_INTERACTIVITY_TYPE(
			"educationalInteractivitytype", new FileSetMetaData("interactivityType", null, null, new String[0])),

	EDUCATIONAL_CONTEXT("educationalContext", new FileSetMetaData("context", null, null, new String[0])),

	RIGHTS_COST("rightsCost", new FileSetMetaData("rights", "cost", null, new String[0])), RIGHTS_DESCRIPTION(
			"rightsDescription", new FileSetMetaData("rights", "description", null, new String[0])),

	DESCRIPTION("description", new FileSetMetaData("description", null, null, new String[0])),

	CONTRIBUTE_ROLE("contributeRole", new FileSetMetaData("contributor", null, null, new String[0])), VCARD("vcard",
			new FileSetMetaData("vcard", null, null, new String[0])), DATE("date", new FileSetMetaData("date", "created", null,
			new String[0])),

	FORMAT("format", new FileSetMetaData("format", "mimetype", null, new String[0])), LEARNING_RESOURCE("learningResource",
			new FileSetMetaData("type", null, null, new String[0])),

	RIGHTS_COPYRIGHT_OTHER_RESTRICTIONS("rightsCopyRightOtherRestrictions", new FileSetMetaData("rights", "copyright", null,
			new String[0]));

	public static final String[] DUBLIN_CORE_QUALIFIER_CONTRIBUTOR = { "advisor", "author", "editor", "illustrator", "other" };

	public static final String[] DC2SCORM_QUALIFIER_CONTRIBUTOR_MAP = { "Validator", "Author", "Editor", "Graphical Designer",
			"Unknown" };

	public static String translateFromDCToScorm(String value, String[] dcValues, String[] scormValues) {
		if (dcValues == null || scormValues == null || value == null) {
			throw new NullPointerException("dcValues,scormValues or the current value is null");
		}
		if (dcValues.length != scormValues.length) {
			throw new ArrayIndexOutOfBoundsException("dcValues and scormValues map are not same size...");
		}

		for (int i = 0; i < dcValues.length; i++) {
			if (value.equals(dcValues[i])) {
				return scormValues[i];
			}
		}

		throw new NullPointerException("Unable to convert value " + value + " from Dublin Core to Scorm dcValues="
				+ Arrays.deepToString(dcValues));
	}

	// TODO identifier e relationKind vem do Fenix,tratar disto mais logo
	public static final String[] METADATA_INFO_KEYS = { "title", "keyword", "generalLanguage", "generalCatalog", "generalEntry",
			"generalStructure", "generalAggregationLevel", "lifecycleVersion", "lifecycleStatus", "metametadataIdentifier",
			"metametadataCatalog", "metametadataEntry", "metametadataContributorRole", "metametadataContributorvcard",
			"metametadataContributordate", "metametadataMetadataschema", "metametadataLanguage", "technicalSize",
			"technicalLocation", "technicalType", "technicalName", "technicalMinimumversion", "technicalMaximumversion",
			"technicalInstallation", "technicalOtherplatformrequirements", "technicalDuration", "educationalInteractivitytype",
			"educationalInteractivitylevel", "educationalSemanticdensity", "educationalIntendedenduserrole",
			"educationalContext", "educationalTypicalagerange", "educationalDifficulty", "educationalTypicallearningtime",
			"educationalDescription", "educationalLanguage", "rightsCost", "rightsDescription",
			"relationResourceCatalogEntryCatalog", "relationResourceDescription", "annotationPerson", "annotationDate",
			"annotationDescription", "classificationTaxonpathSource", "classificationTaxonId", "classificationTaxonEntry",
			"classificationDescription", "classificationKeyword", "description", "coverage", "contributeRole", "vcard", "date",
			"resourceFileCreationDate", "format", "learningResource", "relationKind", "relationResourceCatalogEntryEntry",
			"rightsCopyRightOtherRestrictions", "radiochoice" };

	public static final String[] METADATA_INFO_KEYS_MANDATORY = { "title", "generalCatalog", "generalEntry", "keyword",
			"generalLanguage", "description", "contributeRole", "vcard", "lifecycleVersion", "date", "resourceFileCreationDate",
			"lifecycleStatus", "rightsCopyRightOtherRestrictions", "radiochoice", "metametadataMetadataSchema", "format",
			"technicalLocation", "rightsCost", "generalAggregationLevel", "technicalDuration", "educationalInteractivitytype",
			"learningResource", "educationalContext", "rightsDescription" };

	private String metadataSimpleName = null;

	// private FileSetMetaData itemMetaData=null;
	private FileSetMetaData scormMetaData = null;

	private ScormMetaInfoEnum(String metadataSimpleName, FileSetMetaData scormMetaData) {
		this.metadataSimpleName = metadataSimpleName;
		this.scormMetaData = scormMetaData;
	}

	static public String[] getMetadataSimpleName() {
		String[] retVal = new String[ScormMetaInfoEnum.values().length];
		int i = 0;
		for (ScormMetaInfoEnum current : ScormMetaInfoEnum.values()) {
			retVal[i++] = current.metadataSimpleName;
		}
		return retVal;
	}

	public String getMetadataSimpleName1() {
		return this.metadataSimpleName;
	}

	public FileSetMetaData createFileSetMetaDataFromHash(HashMap<String, Object> metaInfoFromForm) {
		FileSetMetaData retVal = null;
		String enumKey = this.getMetadataSimpleName1();
		String formValue = String.valueOf(metaInfoFromForm.get(enumKey));
		if (enumKey.compareToIgnoreCase(GENERAL_CATALOG.metadataSimpleName) == 0) {
			retVal = this.scormMetaData;
			retVal.setQualifier(formValue);
			retVal.setValues(new String[] { (String) metaInfoFromForm.get(GENERAL_ENTRY.metadataSimpleName) });
		}
		// else
		// if(enumKey.compareToIgnoreCase(GENERAL_ENTRY.metadataSimpleName)==0)
		// { }
		else if (enumKey.compareToIgnoreCase(CONTRIBUTE_ROLE.metadataSimpleName) == 0) {
			retVal = this.scormMetaData;
			retVal.setQualifier(getContributorScormMetadataQualifier(formValue));
			retVal.setValues(new String[] { (String) metaInfoFromForm.get(VCARD.metadataSimpleName) });
		}
		// else if(enumKey.compareToIgnoreCase(VCARD.metadataSimpleName)==0)
		// {}
		else if (enumKey.compareToIgnoreCase(DATE.metadataSimpleName) == 0) {
			retVal = this.scormMetaData;
			retVal.setQualifier(getDatesQualifierValueFromRoleValue((String) metaInfoFromForm
					.get(CONTRIBUTE_ROLE.metadataSimpleName)));
			retVal.setValues(new String[] { formValue, "" });
		} else if (enumKey.compareToIgnoreCase(TECHNICAL_DURATION.metadataSimpleName) == 0) {
			retVal = this.scormMetaData;
			retVal.setValues(new String[] { formValue, "" });
		} else// All other ScormData that have no specific variation on
				// getting it's ScorMetadata
		{
			retVal = this.scormMetaData;
			retVal.setValues(new String[] { formValue });
		}

		return retVal;
	}

	private static String getContributorScormMetadataQualifier(String roleValue) {
		String retVal = "other";
		for (String str : DUBLIN_CORE_QUALIFIER_CONTRIBUTOR) {
			if (str.compareToIgnoreCase(roleValue) == 0) {
				retVal = roleValue;
				break;
			}
		}
		return retVal;
	}

	private static String getDatesQualifierValueFromRoleValue(String roleValue) {
		String retVal = getContributorScormMetadataQualifier(roleValue);
		if (retVal.compareToIgnoreCase("author") == 0) {
			retVal = "created";
		} else if (retVal.compareToIgnoreCase("editor") == 0) {
			retVal = "copyright";
		}
		return retVal;
	}

	public static Collection<FileSetMetaData> createFileSetMetaDataColFromHash(HashMap<String, Object> metaInfoFromForm) {
		Collection<FileSetMetaData> retval = new ArrayList<FileSetMetaData>(ScormMetaInfoEnum.values().length);
		for (ScormMetaInfoEnum current : ScormMetaInfoEnum.values()) {
			retval.add(current.createFileSetMetaDataFromHash(metaInfoFromForm));
		}

		return retval;
	}

	public static ScormMetaDataHash createScormMetaDataHashFromFileSetMetaData(Collection<FileSetMetaData> fsMetaData) {
		ScormMetaDataHash retVal = new ScormMetaDataHash();
		if (fsMetaData != null) {
			for (FileSetMetaData current : fsMetaData) {
				populateScormMetaDataHashFromFileSetMetaData(current, retVal);
			}
		}

		return retVal;
	}

	public static void populateScormMetaDataHashFromFileSetMetaData(FileSetMetaData fsMetaData, ScormMetaDataHash hash) {
		hash.put(fsMetaData.getElement(), fsMetaData.getQualifier(), fsMetaData.getLang(), fsMetaData.getValues());
	}

	/**
	 * @param hashmapScormParam
	 * @return
	 */
	public static ScormMetaDataHash createScormMetaDataHashFromUIFormData(HashMap<String, Object> hashmapScormParam) {
		return createScormMetaDataHashFromFileSetMetaData(createFileSetMetaDataColFromHash(hashmapScormParam));
	}

}
