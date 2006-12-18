/**
 * 
 */
package pt.linkare.scorm.xmlbeans;

import org.imsglobal.xsd.imsmdRootv1P2P1.AggregationlevelType;
import org.imsglobal.xsd.imsmdRootv1P2P1.CatalogentryType;
import org.imsglobal.xsd.imsmdRootv1P2P1.CentityType;
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
import org.imsglobal.xsd.imsmdRootv1P2P1.LomType;
import org.imsglobal.xsd.imsmdRootv1P2P1.MetametadataType;
import org.imsglobal.xsd.imsmdRootv1P2P1.RightsType;
import org.imsglobal.xsd.imsmdRootv1P2P1.RoleType;
import org.imsglobal.xsd.imsmdRootv1P2P1.SourceType;
import org.imsglobal.xsd.imsmdRootv1P2P1.StatusType;
import org.imsglobal.xsd.imsmdRootv1P2P1.TechnicalType;
import org.imsglobal.xsd.imsmdRootv1P2P1.TitleType;
import org.imsglobal.xsd.imsmdRootv1P2P1.ValueType;
import org.imsglobal.xsd.imsmdRootv1P2P1.VersionType;

import pt.linkare.scorm.utils.ScormException;
import pt.linkare.scorm.utils.ScormMetaDataHash;


/**
 * @author Oscar Ferreira - Linkare TI
 *
 */
public class ScormMetadataCollector {
    
    /*
     All the getXxxxQualifier methods below will except as an argument the 
     respective XxxxType instance,if the case has many sub-elements it will
     be necessary to go through all of them and compare with the second
     argument,a String.If the qualifier is found
     'i' is returned else '-1'
     */
    /*
     All the getXxxxLang methods below will except as an argument the 
     respective XxxxType instance, and gather the attribute named "Lang"
     of the value LangstringType,and it's value will be returned.
     */
    
    public static ScormMetaDataHash allScormItemMetaData(LomType lt)
    {
        try
        {
            if(lt !=null)
            {
                ScormMetaDataHash itemmetaHasher=new ScormMetaDataHash();
                titleItemMetaData(lt,itemmetaHasher);                
                identifierItemMetaData(lt,itemmetaHasher);
                descriptionItemMetaData(lt,itemmetaHasher);
                subjectItemMetaData(lt,itemmetaHasher);
                lifecycleVersionItemMetaData(lt,itemmetaHasher);
                lifecycleStatusItemMetaData(lt,itemmetaHasher);
                metametadataMetadataschemaItemMetaData(lt,itemmetaHasher);
                formatItemMetaData(lt,itemmetaHasher);            
                technicalLocationItemMetaData(lt,itemmetaHasher);
                rightsCostItemMetaData(lt,itemmetaHasher);
                rightsItemMetaData(lt,itemmetaHasher);
                languageItemMetaData(lt,itemmetaHasher);
                generalAggregationLevelItemMetaData(lt,itemmetaHasher);
                contributorItemMetaData(lt,itemmetaHasher);
                technicalDurationItemMetaData(lt,itemmetaHasher);
                educationalInteractivitytypeItemMetaData(lt,itemmetaHasher);
                typeItemMetaData(lt,itemmetaHasher);
                educationalContextItemMetaData(lt,itemmetaHasher);                 
                rightsDescriptionItemMetaData(lt,itemmetaHasher);
                technicalSizeItemMetaData(lt,itemmetaHasher);
                
                return itemmetaHasher;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
   /**
     * @param lt
     * @param itemmetaHasher
     */
    private static void rightsCostItemMetaData(LomType lt, ScormMetaDataHash itemHasher) throws ScormException
    {
        String qualifier=null,lang=null,value=null;
        RightsType rType=lt.getRights();
        if(rType!=null)
        {
            CostType cType=rType.getCost();
            if(cType!=null)
            {
                SourceType sType=cType.getSource();
                if(sType!=null)
                {
                    LangstringType langStrType=sType.getLangstring();
                    if(langStrType!=null)
                    {
                        if(langStrType.getStringValue().compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                        {
                            ValueType vType=cType.getValue();
                            if(vType!=null)
                            {
                                LangstringType lType=vType.getLangstring();
                                if(lType!=null)
                                {
                                    lang=lType.getLang();
                                    value=lType.getStringValue();
                                    qualifier="cost";
                                    itemHasher.put("rights",qualifier,lang,value);
                                }
                            }
                        }
                        else
                        {
                            throw new ScormException("Rights->Cost was not LOMv1.0.");
                        }
                    }
                }
            }
        }
    }
    
    
    /**
     * @param lt
     */
    private static void rightsDescriptionItemMetaData(LomType lt,ScormMetaDataHash itemhasher) 
    {
        String qualifier="description",lang=null,value=null;
        RightsType rType=lt.getRights();
        if(rType!=null)
        {
            DescriptionType dType=rType.getDescription();
            LangstringType[] lTypeArray=dType.getLangstringArray();
            if(lTypeArray!=null)
            {
                for(LangstringType lType:lTypeArray)
                {
                    lang=lType.getLang();
                    value=lType.getStringValue();
                    itemhasher.put("rights",qualifier,lang,value);
                }
            }            
        }        
    }
    
    
     
    /**
     * @param lt
     */
    private static void educationalContextItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException
    {
        String qualifier=null,lang=null,value=null;
        EducationalType eduType=lt.getEducational();
        if(eduType!=null)
        {
            ContextType[] cTypeArray=eduType.getContextArray();
            if(cTypeArray!=null)
            {
                for(ContextType cType:cTypeArray)
                {
                    SourceType sType=cType.getSource();
                    if(sType!=null)
                    {
                        LangstringType langStrType=sType.getLangstring();
                        if(langStrType!=null)
                        {
                            String str=langStrType.getStringValue();
                            if(str!=null)
                            {
                                if(str.compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                                {
                                    ValueType vType=cType.getValue();
                                    if(vType!=null)
                                    {
                                        LangstringType lStrType=vType.getLangstring();
                                        if(lStrType!=null)
                                        {
                                            lang=lStrType.getLang();
                                            value=lStrType.getStringValue();
                                            itemHasher.put("context",qualifier,lang,value);
                                        }
                                    }
                                }
                                else
                                    throw new ScormException("Educational->Context was not LOMv1.0");
                            }
                        }
                    }
                }
            }
        }       
    }
    
    
    /**
     * @param lt
     */
    private static void educationalInteractivitytypeItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException
    {
        String qualifier=null,lang=null,value=null;
        EducationalType eType=lt.getEducational();
        if(eType!=null)
        {
            InteractivitytypeType iType=eType.getInteractivitytype();
            if(iType!=null)
            {
                SourceType sType=iType.getSource();
                if(sType!=null)
                {
                    LangstringType lStrType=sType.getLangstring();
                    if(lStrType!=null)
                    {
                        String str=lStrType.getStringValue();
                        if(str!=null)
                        {
                            if(str.compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                            {
                                ValueType vType=iType.getValue();
                                if(vType!=null)
                                {
                                    LangstringType lType=vType.getLangstring();
                                    if(lType!=null)
                                    {
                                        lang=lType.getLang();
                                        value=lType.getStringValue();
                                        itemHasher.put("interactivityType",qualifier,lang,value);
                                    }
                                }
                            }
                            else
                            {
                                throw new ScormException("Educational->InteractivityType was not LOMv1.0.");
                            }
                        }
                    }
                }
            }
        }               
    }
    
    
    
    /**
     * @param lt
     */
    private static void technicalDurationItemMetaData(LomType lt,ScormMetaDataHash itemHasher) 
    {
        String qualifier=null,lang=null,durationDate=null,durationDescription=null;
        TechnicalType tType=lt.getTechnical();
        if(tType!=null)
        {
            DurationType dType=tType.getDuration();
            if(dType!=null)
            {
                durationDate=dType.getDatetime();                
                DescriptionType descType=dType.getDescription();
                if(descType!=null)
                {
                    LangstringType[] langTypeArray=descType.getLangstringArray();
                    if(langTypeArray!=null)
                    {
                        for(LangstringType langType:langTypeArray)
                        {
                            lang=langType.getLang();
                            String str=langType.getStringValue();
                            if(str!=null)
                            {
                                if(durationDescription==null)
                                    durationDescription=str;
                                else
                                {
                                    durationDescription.concat("/-/"+str);
                                }
                            }
                            itemHasher.put("duration",qualifier,lang,new String[]{durationDate,durationDescription});
                        }
                    }
                }                
            }
        }        
    }
    
    
    /**
     * @param lt
     */
    private static void technicalLocationItemMetaData(LomType lt,ScormMetaDataHash itemHasher) 
    {
        String qualifier=null,lang=null,value=null;
        TechnicalType tType=lt.getTechnical();
        if(tType!=null)
        {
            LocationType[] lTypeArray=tType.getLocationArray();
            if(lTypeArray!=null)
            {
                for(LocationType lType:lTypeArray)
                {
                    value=lType.getStringValue();                    
                    if(lType.getType().toString().equalsIgnoreCase("uri"))
                        qualifier="uri";
                    else
                        qualifier="other";
                    itemHasher.put("location",qualifier,lang,value);                        
                }
            }
        }
    }
    
    
    /**
     * @param lt
     */
    private static void technicalSizeItemMetaData(LomType lt,ScormMetaDataHash itemHasher) 
    {
        String qualifier=null,lang=null,value=null;
        TechnicalType tType=lt.getTechnical();
        if(tType!=null)
        {
            value=String.valueOf(tType.getSize());
            itemHasher.put("size",qualifier,lang,value);
        }
    }        
    
    /**
     * @param lt
     */
    private static void metametadataMetadataschemaItemMetaData(LomType lt,ScormMetaDataHash itemHasher) 
    {
        String qualifier=null,lang=null,value=null;
        MetametadataType mmType=lt.getMetametadata();
        if(mmType!=null)
        {
            String[] msTypeArray=mmType.getMetadataschemeArray();
            if(msTypeArray!=null)
            {
                for(String msType:msTypeArray)
                {
                    value=msType;
                    itemHasher.put("metadatascheme",qualifier,lang,value);
                }
            }
        }       
    }
    
    
    /**
     * @param lt
     */
    private static void lifecycleStatusItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException
    {
        String qualifier=null,lang=null,value=null;
        LifecycleType lType=lt.getLifecycle();
        if(lType!=null)
        {
            StatusType sType=lType.getStatus();
            if(sType!=null)
            {
                SourceType srcType=sType.getSource();
                if(srcType!=null)
                {
                    LangstringType langStrType=srcType.getLangstring();
                    if(langStrType!=null)
                    {
                        if(langStrType.getStringValue().compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                        {                    
                            ValueType vType=sType.getValue();
                            if(vType!=null)
                            {
                                LangstringType lStrtype=vType.getLangstring();
                                if(lStrtype!=null)
                                {
                                    lang=lStrtype.getLang();
                                    value=lStrtype.getStringValue();
                                    itemHasher.put("status",qualifier,lang,value);
                                }
                            }
                        }
                        else
                        {
                            throw new ScormException("LifeCycle->Status was not LOMv1.0.");
                        }
                    }
                }
            }
        }        
    }
    
    
    /**
     * @param lt
     */
    private static void lifecycleVersionItemMetaData(LomType lt,ScormMetaDataHash itemHasher) 
    {
        String value=null,lang=null,qualifier=null;
        LifecycleType lType=lt.getLifecycle();
        if(lType!=null)
        {
            VersionType vType=lType.getVersion();
            if(vType!=null)
            {
                LangstringType[] lStrTypeArray=vType.getLangstringArray();
                if(lStrTypeArray!=null)
                {
                    for(LangstringType lStrType:lStrTypeArray)
                    {
                        lang=lStrType.getLang();
                        value=lStrType.getStringValue();
                        itemHasher.put("version",qualifier,lang,value);
                    }
                }
            }
        }
    }
    
    
    /**
     * @param lt
     */
    private static void generalAggregationLevelItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException
    {
        String qualifier=null,lang=null,value=null;
        GeneralType gType=lt.getGeneral();
        if(gType!=null)
        {
            AggregationlevelType aType=gType.getAggregationlevel();
            if(aType!=null)
            {
                SourceType sType=aType.getSource();
                if(sType!=null)
                {
                    LangstringType lType=sType.getLangstring();
                    if(lType!=null)
                    {
                        String str=lType.getStringValue();
                        if(str!=null)
                        {
                            if(str.compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                            {
                                ValueType vType=aType.getValue();
                                if(vType!=null)
                                {
                                    LangstringType lStrType=vType.getLangstring();
                                    if(lStrType!=null)
                                    {
                                        lang=lStrType.getLang();
                                        value=lStrType.getStringValue();
                                        itemHasher.put("aggregationLevel",qualifier,lang,value);
                                    }
                                }
                            }
                            else
                            {
                                throw new ScormException("General->Aggregation_Level was not LOMv1.0.");
                            }
                        }
                    }
                }
                
            }
        }                
    }
    
    
    
    private static void titleItemMetaData(LomType lt,ScormMetaDataHash itemHasher)
    {    
        GeneralType gType=lt.getGeneral();
        if(gType!=null)
        {
            TitleType tType=gType.getTitle();
            if(tType!=null)
            {
                LangstringType[] lTypeArray=tType.getLangstringArray();
                if(lTypeArray!=null)
                {
                    for(LangstringType lType:lTypeArray)
                    {
                        String value=lType.getStringValue();
                        String lang=lType.getLang();
                        itemHasher.put("title",null,lang,value);
                    }
                }
            }
        }
        
    }
    
    //Trata de contributor com Qualifier igual a 'author','publisher' e afins
    private static void contributorItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException
    {
        String dateTime=null,dateDescription=null,qualifier="other",lang=null,value=null;
        LifecycleType lifeType=lt.getLifecycle();
        if(lifeType!=null)
        {
            ContributeType[] cTypeArray=lifeType.getContributeArray();
            if(cTypeArray!=null)
            {
                for(ContributeType cType:cTypeArray)
                {
                    DateType dType=cType.getDate();
                    if(dType!=null)
                    {
                        dateTime=dType.getDatetime();
                        DescriptionType dDescType=dType.getDescription();
                        LangstringType[] lTypeArray=dDescType.getLangstringArray();
                        if(lTypeArray!=null)
                        {
                            for(LangstringType lStrType:lTypeArray)
                            {
                                if(dateDescription==null)
                                    dateDescription=lStrType.getStringValue();
                                else
                                {
                                    dateDescription=dateDescription.concat("/-/"+lStrType.getStringValue());
                                }                                
                            }
                        }
                    }
                    RoleType rType=cType.getRole();
                    if(rType!=null)
                    {
                        SourceType srcType=rType.getSource();
                        if(srcType!=null)
                        {
                            LangstringType langStrType=srcType.getLangstring();
                            if(langStrType!=null)
                            {
                                String str=langStrType.getStringValue();
                                if(str!=null)
                                {
                                    if(str.compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                                    {
                                        ValueType valType=rType.getValue();
                                        if(valType!=null)
                                        {
                                            LangstringType lStrType=valType.getLangstring();
                                            if(lStrType!=null)
                                            {
                                                String strRole=lStrType.getStringValue();
                                                for(String strVal:ImsManifestReader_1_2.DUBLIN_CORE_QUALIFIER_CONTRIBUTOR)
                                                {
                                                    if(strRole.compareToIgnoreCase(strVal)==0)
                                                    {
                                                        if(strVal.compareToIgnoreCase("author")==0 || strVal.compareToIgnoreCase("editor")==0)
                                                        {
                                                            qualifier=strRole;
                                                        }                                                        
                                                        break;
                                                    }
                                                }
                                                lang=lStrType.getLang();
                                            }
                                        }
                                    }
                                    else
                                    {
                                        throw new ScormException("Lifecycle->Contribute->Role was not LOMv1.0.");
                                    }
                                }    
                            }
                        }
                    }                    
                    CentityType[] centTypeArray=cType.getCentityArray();
                    if(centTypeArray!=null)
                    {                        
                        for(CentityType centType:centTypeArray)
                        {
                            value=centType.getVcard();
                            itemHasher.put("contributor",qualifier,lang,value);
                            if(qualifier.compareToIgnoreCase("author")==0)
                                itemHasher.put("date","created",lang,new String[]{dateTime,dateDescription});
                            else if(qualifier.compareToIgnoreCase("editor")==0)
                                itemHasher.put("date","copyright",lang,new String[]{dateTime,dateDescription});
                            else
                                itemHasher.put("date",qualifier,lang,new String[]{dateTime,dateDescription});
                        }
                    }
                }
            }
            
        }
    }
    
//  Devolve todos o Subjects...
    private static void subjectItemMetaData(LomType lt,ScormMetaDataHash itemHasher)
    {
        GeneralType gType=lt.getGeneral();
        if(gType!=null)
        {
            KeywordType[] kTypeArray=gType.getKeywordArray();
            if(kTypeArray!=null)
            {
                for(KeywordType kType:kTypeArray)
                {
                    LangstringType[] lTypeArray=kType.getLangstringArray();
                    if(lTypeArray!=null)
                    {
                        for(LangstringType lType:lTypeArray)
                        {
                            String lang=lType.getLang();
                            String value=lType.getStringValue();
                            itemHasher.put("subject",null,lang,value);
                        }
                    }
                }
            }
        }
    }
    
    
    
    
    private static void descriptionItemMetaData(LomType lt,ScormMetaDataHash itemHasher)
    {
        GeneralType gType=lt.getGeneral();
        if(gType!=null)
        {
            DescriptionType[] dTypeArray= gType.getDescriptionArray();
            if(dTypeArray!=null)
            {
                for(DescriptionType dType:dTypeArray)
                {
                    LangstringType[] lTypeArray=dType.getLangstringArray();
                    if(lTypeArray!=null)
                    {
                        for(LangstringType lType:lTypeArray)
                        {
                            String lang=lType.getLang();
                            String value=lType.getStringValue();
                            itemHasher.put("description",null,lang,value);
                        }
                    }
                }
            }
        }
        
    }
           
    private static void formatItemMetaData(LomType lt,ScormMetaDataHash itemHasher)
    {
        String qualifier=null,lang=null,value=null;
        TechnicalType tType=lt.getTechnical();
        if(tType!=null)
        {
            String[] fTypeArray=tType.getFormatArray();
            if(fTypeArray!=null)
            {
                for(String fType:fTypeArray)
                {
                    value=fType;
                    qualifier="mimetype";
                    itemHasher.put("format",qualifier,lang,value);
                }
            }
        }        
    }
    
    private static void identifierItemMetaData(LomType lt,ScormMetaDataHash itemHasher)
    {
        GeneralType gType=lt.getGeneral();
        if(gType!=null)
        {
            CatalogentryType[] cTypeArray=gType.getCatalogentryArray();
            for(CatalogentryType cType:cTypeArray)
            {
                String catType=cType.getCatalog();
                if(catType!=null)
                { 
                    String qualifier="other";
                    String lang=null; 
                    for(String val:ImsManifestReader_1_2.DUBLIN_CORE_QUALIFIER_IDENTIFIER)
                    {
                        if(catType.equalsIgnoreCase(val))
                            qualifier=val;
                    }
                    EntryType eType=cType.getEntry();
                    LangstringType[] lTypeArray=eType.getLangstringArray();
                    if(lTypeArray!=null)
                    {
                        for(LangstringType lType:lTypeArray)
                        {
                            lang=lType.getLang();
                            String value=lType.getStringValue();
                            itemHasher.put("identifier",qualifier,lang,value);                            
                        }
                    }
                }
                
            }
        }
    }
    
    private static void typeItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException
    {
        String qualifier=null,lang=null,value=null;
        EducationalType eduType=lt.getEducational();
        if(eduType!=null)
        {
            LearningresourcetypeType[] learnTypeArray=eduType.getLearningresourcetypeArray();
            if(learnTypeArray!=null)
            {
                for(LearningresourcetypeType learnType:learnTypeArray)
                {
                    SourceType sType=learnType.getSource();
                    if(sType!=null)
                    {
                        LangstringType lStrType=sType.getLangstring();
                        if(lStrType!=null)
                        {
                            String str=lStrType.getStringValue();
                            if(str.compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                            {
                                ValueType vType=learnType.getValue();
                                if(vType!=null)
                                {
                                    LangstringType lType=vType.getLangstring();
                                    if(lType!=null)
                                    {
                                        lang=lType.getLang();
                                        value=lType.getStringValue();
                                        itemHasher.put("type",qualifier,lang,value);
                                    }
                                }
                            }
                            else
                            {
                                throw new ScormException("Educational->LearningResourceType was not LOMv1.0.");
                            }
                        }
                    }
                }
            }           
        }        
    }
    
    private static void languageItemMetaData(LomType lt,ScormMetaDataHash itemHasher)
    {
        String qualifier=null,lang=null;
        GeneralType gType=lt.getGeneral();
        if(gType!=null)
        {
            String[] lArray=gType.getLanguageArray();
            if(lArray!=null)
            {
                qualifier="iso";
                itemHasher.put("language",qualifier,lang,lArray);
            }
        }
    }
    
    private static void rightsItemMetaData(LomType lt,ScormMetaDataHash itemHasher) throws ScormException    
    {
        String qualifier=null,lang=null,value=null;
        RightsType rType=lt.getRights();
        if(rType!=null)
        {
            CopyrightandotherrestrictionsType copyType=rType.getCopyrightandotherrestrictions();
            if(copyType!=null)
            {
                SourceType sType=copyType.getSource();
                if(sType!=null)
                {
                    LangstringType lType=sType.getLangstring();
                    if(lType!=null)
                    {
                        if(lType.getStringValue().compareToIgnoreCase(ImsManifestReader_1_2.IMS_MANIFEST_READER_LOM_VERSION)==0)
                        {
                            ValueType vType=copyType.getValue();
                            if(vType!=null)
                            {
                                LangstringType langStrType=vType.getLangstring();                            
                                if(langStrType!=null)
                                {
                                    lang=langStrType.getLang();
                                    value=langStrType.getStringValue();
                                    qualifier="copyright";
                                    itemHasher.put("rights",qualifier,lang,value);
                                }
                            }
                        }
                        else
                        {
                            throw new ScormException("Rights->Copyright was not LOMv1.0.");
                        }
                    }
                }
            }
        }      
    }  
    
}
