package pt.linkare.scorm.xmlbeans.r1_3;
/*//Schemas Used: adlcp_v1p3.xsd;imscp_v1p1.xsd;lom.xsd;xml.xsd;unique/strict.xsd;
// vocab/loose.xsd;extend/strict.xsd;common/dataTypes.xsd;
// common/elementNames.xsd;common/elementTypes.xsd;common/rootElement.xsd
// common/vocabValues.xsd;common/vocabTypes.xsd           
*//**
 * 
 *//*
package pt.linkare.scorm.xmlBeans;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.ieee.ltsc.xsd.lom.Contribute;
import org.ieee.ltsc.xsd.lom.CopyrightAndOtherRestrictions;
import org.ieee.ltsc.xsd.lom.Coverage;
import org.ieee.ltsc.xsd.lom.Date;
import org.ieee.ltsc.xsd.lom.DateTimeValue;
import org.ieee.ltsc.xsd.lom.Description;
import org.ieee.ltsc.xsd.lom.Duration2;
import org.ieee.ltsc.xsd.lom.Educational;
import org.ieee.ltsc.xsd.lom.Entry;
import org.ieee.ltsc.xsd.lom.Format;
import org.ieee.ltsc.xsd.lom.General;
import org.ieee.ltsc.xsd.lom.Identifier;
import org.ieee.ltsc.xsd.lom.Keyword;
import org.ieee.ltsc.xsd.lom.Kind;
import org.ieee.ltsc.xsd.lom.KindValue;
import org.ieee.ltsc.xsd.lom.LangString;
import org.ieee.ltsc.xsd.lom.LangString2;
import org.ieee.ltsc.xsd.lom.LearningResourceType;
import org.ieee.ltsc.xsd.lom.LifeCycle;
import org.ieee.ltsc.xsd.lom.Location;
import org.ieee.ltsc.xsd.lom.Lom;
import org.ieee.ltsc.xsd.lom.LomDocument;
import org.ieee.ltsc.xsd.lom.Relation;
import org.ieee.ltsc.xsd.lom.Resource;
import org.ieee.ltsc.xsd.lom.Rights;
import org.ieee.ltsc.xsd.lom.Role;
import org.ieee.ltsc.xsd.lom.RoleValue;
import org.ieee.ltsc.xsd.lom.SourceValue;
import org.ieee.ltsc.xsd.lom.Technical;
import org.ieee.ltsc.xsd.lom.Title;
import org.imsglobal.xsd.imscpV1P1.FileType;
import org.imsglobal.xsd.imscpV1P1.ItemType;
import org.imsglobal.xsd.imscpV1P1.ManifestDocument;
import org.imsglobal.xsd.imscpV1P1.ManifestType;
import org.imsglobal.xsd.imscpV1P1.MetadataDocument;
import org.imsglobal.xsd.imscpV1P1.MetadataType;
import org.imsglobal.xsd.imscpV1P1.OrganizationType;
import org.imsglobal.xsd.imscpV1P1.OrganizationsType;
import org.imsglobal.xsd.imscpV1P1.ResourceType;
import org.imsglobal.xsd.imscpV1P1.ResourcesType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pt.linkare.scorm.utils.ScormMetaData;
import pt.linkare.scorm.utils.MainZipTester;
import pt.linkare.scorm.utils.ScormException;

*//**
 * @author Oscar Ferreira - Linkare TI
 *
 *//*
public class ImsManifestReader {

    //Some usefull Strings
    public static final String IMS_MANIFEST_READER_AUTH="author";
    public static final String IMS_MANIFEST_READER_PUB="publisher";
    public static final String IMS_MANIFEST_READER_LOM_VERSION="LOMv1.0";
    
    //SCORM_2004 Tags and there children
    public static final String[] SCORM_TAG_MANIFEST={"metadata","organizations","resources","manifest","imsss:sequencingCollection"};
    public static final String[] SCORM_TAG_METADATA={"schema","schemaversion","lom","adlcp:location"};
    public static final String[] SCORM_TAG_ORGANIZATIONS={"organization"};
    public static final String[] SCORM_TAG_ORGANIZATION={"title","item","metadata","imsss:sequencing"};
    public static final String[] SCORM_TAG_ITEM={"title","item","metadata","adlcp:timeLimitAction","adlcp:dataFromLMS","adlcp:completionThreshold","imsss:sequencing","adlnav:presentation"};
    public static final String[] SCORM_TAG_RESOURCES={"resource"};
    public static final String[] SCORM_TAG_RESOURCE={"metadata","file","dependency"};
    public static final String[] SCORM_TAG_FILE={"metadata"};
    
    //DSPACE...
    public static final String[] DUBLIN_CORE_QUALIFIER_CONTRIBUTOR={"advisor","author","editor","illustrator","other"};
    public static final String[] DUBLIN_CORE_QUALIFIER_COVERAGE={"spatial","temporal"};
    public static final String[] DUBLIN_CORE_QUALIFIER_DATE={"accessioned","available","copyright","created","issued","submitted"};
    public static final String[] DUBLIN_CORE_QUALIFIER_IDENTIFIER={"citation","govdoc","isbn","issn","sici","ismn","other","uri"};
    public static final String[] DUBLIN_CORE_QUALIFIER_DESCRIPTION={"abstract","provenance","sponsorship","statementofresponsibility","tableofcontents","uri"};
    public static final String[] DUBLIN_CORE_QUALIFIER_FORMAT={"extent","medium","mimetype"};
    public static final String[] DUBLIN_CORE_QUALIFIER_LANGUAGE={"iso"};
    public static final String[] DUBLIN_CORE_QUALIFIER_RELATION={"isformatof","ispartof","ispartofseries","haspart","isversionof","hasversionof","isbasedon","isreferencedby","requires","replaces","isreplacedby","uri"};
    public static final String[] DUBLIN_CORE_QUALIFIER_RIGHTS={"uri"};
    public static final String[] DUBLIN_CORE_QUALIFIER_SOURCE={"uri"};
    public static final String[] DUBLIN_CORE_QUALIFIER_SUBJECT={"classification","ddc","lcc","lcsh","mesh","other"};
    public static final String[] DUBLIN_CORE_QUALIFIER_TITLE={"alternative"};
    
  
    private Collection<ScormMetaData> itemDSpaceMetaData = null;
    
    //Valor de base xml:base pode ser geral pelo documento caso referencia do ficheiro
    //seja local e nao comece com "/" e caso nao tenha nenhum 
    private String xmlBase=null;
    private File basePath=null;
    
    

    public ImsManifestReader(File dirZipExtraido) {
        super();
        this.basePath=dirZipExtraido;
        this.xmlBase=dirZipExtraido.getAbsolutePath();
        if(!this.xmlBase.endsWith("/"))
            this.xmlBase=this.xmlBase.concat("/");
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

    //XXX:MAIN --> PARA TESTES...os "imsR" passam a ser "this"
    //args[0] name of Zip file Scorm compatible
    //args[1] name of output Directory extracted Zip File
    public static void main(String[] args) {
        
        try
        {              
            if(MainZipTester.getFileBoolean(args[0],"imsmanifest.xml"))
            {
                File unzippedDirFile=MainZipTester.extractZipFile(args[0],args[1]);
                //Já foi unzippado o ZIP File "test.zip"
                ImsManifestReader imsR=new ImsManifestReader(unzippedDirFile);
                Collection<ScormMetaData> colItemMetadata=null;                
                //Test existence of imsManifest file at root.                
                File manifestFile=imsR.getImsmanifest(imsR.getXmlBase());
                ManifestDocument mDoc = ManifestDocument.Factory.parse(manifestFile);
                if(!mDoc.validate())
                    throw new XmlException("ImsManifest File is Not Valid!");
                if(manifestFile!=null)
                {
                    Collection<File> schemas=imsR.getXsdFromXml(manifestFile);        
                    if(schemas!=null)
                    {
                        for(File schema:schemas)
                        {
                            if(schema!=null && schema.exists())
                            {
                                System.out.println("Schemas Absolute Path: "+schema.getAbsolutePath());
                            }
                            else
                            {   
                               throw new ScormException("A Schema is not available.");
                            }
                        }
                        Collection<String> colExternalFiles=imsR.getAllFileNamesRefManifest(manifestFile);
                        System.out.println();
                        for(String extFile:colExternalFiles)
                        {
                            System.out.println("External Files: "+extFile);
                        }
                        System.out.println();
                        String strAux=imsR.verifyFileCollectionExistence(colExternalFiles);
                        if(strAux!=null)
                        {
                            System.out.println("The File '"+strAux+"' was not found in Package!");                        
                        }
                        else
                        {
    //                      Todos os ficheiros existem
                            System.out.println("all went well,Package is SCORM Compatible");
                            //Maybe more testes...like all tags have all there 
                            //necessary attributes and necessary children tags
                            
                             Get DSpace MetaData 
                            colItemMetadata=imsR.readManifestData(manifestFile,colExternalFiles);
                            ScormMetaData.print(colItemMetadata);
                        }
                    }
                }
                else
                {
                    System.out.println("error");
                    throw new ScormException("Manifest File non-existent.");
                }
            }
            else
            {
                System.out.println("imsmanifest.xml File not found! Not SCORM Compliant.");
            }
        }
        catch(ScormException se)
        {
            se.printStackTrace();
        }
        catch(XmlException xe)
        {            
            xe.printStackTrace();
        }
        catch(IOException ioe)
        {
            System.out.println("Failed or Interrupted IO operation.");
            ioe.printStackTrace();
        }
        catch(Exception e)
        {
            System.out.println("Some other Exception occured.");
            e.printStackTrace();
        }
    }

    //will give a null value case imsmanifest does not exist..NOT PIF
    public File getImsmanifest(String AbsolutePathDirZipExtraido) throws NullPointerException
    {
        try
        {
            return new File(AbsolutePathDirZipExtraido,"imsmanifest.xml");
        }
        catch (NullPointerException npe) {
            throw npe;
        }
    }
    
    private String verifyFileCollectionExistence(Collection<String> colLocations)
    {
        for(String filePath:colLocations)
        {
            if(filePath.startsWith("http://"))
            {}
            else
            {
                if(new File(filePath).exists()==false)
                {
                    return filePath;
                }
            }
        }      
        return null;
    }
    
    //CALL THIS METHOD!!!!!!
    //To go through the imsmanifest.xml file and collect all external files except initial schema files
    public Collection<String> getAllFileNamesRefManifest(File xmlFile) throws XmlException,IOException,Exception
    {
        Collection<String> strCol= new HashSet<String>();
        ManifestDocument mDoc = null;
        //XmlObject xmlObjTemp=null;
        String strXmlBase=null;
        
        try{
            if(xmlFile!=null)
            {
                mDoc=ManifestDocument.Factory.parse(xmlFile);
                if(mDoc!=null)
                {
                    ManifestType mt=mDoc.getManifest();
                    if(mt!=null)
                    {
                        strXmlBase=getXmlBaseAttrib(mt);  
                        strCol=getManifestMetaDataRecursive(mt,strCol,strXmlBase); 
                    }
                }
            }            
        }
        catch(XmlException xmle)
        {
            throw xmle;
        }
        catch(IOException ioe)
        {
            throw ioe;
        }
        catch(Exception e)
        {
            throw e;
        }
        return strCol;
    }
   
    //Given an object gets its Attribute "xml:base" and adds another hierarchical
    //path to the 'this'.objects xmlBase Property
    private String getXmlBaseAttrib(XmlObject xmlObj)
    {
        String XmlBaseTemp=null;
        if(xmlObj!=null)
        {   
            Node node=xmlObj.getDomNode();
            node=node.getAttributes().getNamedItem("xml:base");
            if(node!=null)
            {
                String strBase=node.getNodeValue();                
                if(strBase.startsWith("/"))
                    strBase=strBase.substring(1);
                if(this.xmlBase!=null&&this.xmlBase.compareTo("")!=0&&!(strBase.startsWith("http://")))
                {
                    if(!this.xmlBase.endsWith("/"))
                        this.setXmlBase(this.xmlBase.concat("/"));
                    XmlBaseTemp=this.xmlBase.concat(strBase);
                }
                else
                    XmlBaseTemp=strBase;
            }
        }
        return XmlBaseTemp;
    }
    
//  Given an object gets its Attribute "xml:base" and adds another hierarchical
    //path to the 'this'.objects xmlBase Property
    private String getXmlBaseAttrib(XmlObject xmlObj,String otherXmlbase)
    {
        String XmlBaseTemp=null;
        if(otherXmlbase!=null&&otherXmlbase.compareTo("")!=0)
        {
            if(!otherXmlbase.endsWith("/"))
                otherXmlbase=otherXmlbase.concat("/");
            if(xmlObj!=null)
            {
                NamedNodeMap nnm=xmlObj.getDomNode().getAttributes();
                if(nnm!=null&&nnm.getLength()>0)
                {
                    Node node=nnm.getNamedItem("xml:base");
                    if(node!=null)
                    {
                        String strBase=node.getNodeValue();
                        if(strBase.startsWith("/"))
                            strBase=strBase.substring(1);
                        if(!(strBase.startsWith("http://")))
                            XmlBaseTemp=otherXmlbase.concat(strBase);
                        else
                            XmlBaseTemp=strBase;
                    }
                }
            }
        }
        else
            XmlBaseTemp= getXmlBaseAttrib(xmlObj);
        return XmlBaseTemp;
    }
    
    
//  Given an object gets its Attribute "xml:base" and adds another hierarchical
    //path to the 'this'.objects xmlBase Property
    private String getXmlBaseAttribForFileTypes(FileType fType)
    {
        String XmlBaseTemp=null;
        XmlObject xmlObjTemp=null;
        if(fType!=null)
        {
            xmlObjTemp=fType.selectAttribute(null,"href");
            if(xmlObjTemp!=null)
            {
                String strBase=xmlObjTemp.getDomNode().getNodeValue();
                if(strBase.startsWith("/"))
                    strBase=strBase.substring(1);
                if(this.xmlBase!=null&&this.xmlBase.compareTo("")!=0&&!(xmlObjTemp.getDomNode().getNodeValue().startsWith("http://")))
                {
                    if(!this.xmlBase.endsWith("/"))
                        this.setXmlBase(this.xmlBase.concat("/"));
                    XmlBaseTemp=this.xmlBase.concat(strBase);
                }
                else
                    XmlBaseTemp=strBase;
            }
        }
        return XmlBaseTemp;
    }
    
//  Given an object gets its Attribute "xml:base" and adds another hierarchical
    //path to the 'this'.objects xmlBase Property
    private String getXmlBaseAttribForFileTypes(FileType fType,String otherXmlbase)
    {
        String XmlBaseTemp=null;
        XmlObject xmlObjTemp=null;
        if(otherXmlbase!=null&&otherXmlbase.compareTo("")!=0)
        {

            if(!otherXmlbase.endsWith("/"))
                otherXmlbase=otherXmlbase.concat("/");
            if(fType!=null)
            {
                xmlObjTemp=fType.selectAttribute(null,"href");
                if(xmlObjTemp!=null)
                {             
                    String strBase=xmlObjTemp.getDomNode().getNodeValue();
                    if(strBase.startsWith("/"))
                        strBase=strBase.substring(1);
                    if(!(strBase.startsWith("http://")))
                        XmlBaseTemp=otherXmlbase.concat(strBase);
                    else
                        XmlBaseTemp=strBase;
                }
            }
        }
        else
            XmlBaseTemp= getXmlBaseAttribForFileTypes(fType);
        return XmlBaseTemp;
    }
    
    
    //Remove last substring from larger string
    //Ex: "Dir0/Dir1/hello.txt"->"Dir0/Dir1/"->"Dir0/",até ficar vazio
    public void removeLastHierarchicalXmlbase()
    {
        if(this.xmlBase!=null && this.xmlBase.compareToIgnoreCase("")!=0)
        {
            String strAux=this.xmlBase.substring(0,this.xmlBase.length()-1);
            int pos=strAux.lastIndexOf('/');
            this.xmlBase=this.xmlBase.substring(0,pos+1);
        }
        //else deixa-o como está
    }
    
    public void removeLastXmlbaseFromRealXmlbase(String lastXmlbaseAdded)
    {
        if(this.xmlBase!=null&&this.xmlBase.endsWith(lastXmlbaseAdded)&&this.xmlBase.compareTo("")!=0)
        {
            this.xmlBase=this.xmlBase.substring(0,this.xmlBase.length()-lastXmlbaseAdded.length());
        }
        //else deixo-o estar pq n foi afectado ou pq o q se queres remover n está no seu fim
    }
    
    //Principal Method to gather all the Absolute Path for files in the imsmanifest.xml file,Arg:xmlBase is the Manifests xml:base
    private Collection<String> getManifestMetaDataRecursive(ManifestType manType,Collection<String> colLocations,String xmlBase) throws XmlException,IOException,Exception
    {
        try{            
            if(manType!=null)       
            {                
                String xmlBaseAux=null;
                ResourcesType rsType=null;
                MetadataType mDataType=null;
                //Doing all the Manifest Nodes in manType
                if(manType.getManifestArray()!=null)
                {                    
                    for(ManifestType mType:manType.getManifestArray())
                    {
                        mDataType=mType.getMetadata();
                        colLocations=getLocationFromMetadata(mDataType,colLocations,xmlBase);
                        //Back to Manifest
                        OrganizationsType orgsType=mType.getOrganizations();
                        if(orgsType!=null)
                        {
                            OrganizationType[] orgArrayType=orgsType.getOrganizationArray();
                            if(orgArrayType!=null)
                            {
                                for(OrganizationType orgType:orgArrayType)
                                {
                                    mDataType=orgType.getMetadata();
                                    colLocations=getLocationFromMetadata(mDataType,colLocations,xmlBase);
                                    //Item in Organization may also have MetaData
                                    ItemType[] itemArrayType=orgType.getItemArray();
                                    //gets all locations from all items recursively
                                    colLocations=getItemsMetaDataRecursive(itemArrayType,colLocations,xmlBase);                                    
                                }
                            }
                        }//Locations:done for Organizations
                        //Locations for Resources...
                        rsType=mType.getResources();
                        if(rsType!=null)
                        {   
                            FileType[] fTypeArray=null;
                            xmlBaseAux=getXmlBaseAttrib(rsType,xmlBase);
                            for(ResourceType rType:rsType.getResourceArray())
                            {
                                xmlBaseAux=getXmlBaseAttrib(rType,xmlBaseAux);                            
                                colLocations=getLocationFromMetadata(rType.getMetadata(),colLocations,xmlBaseAux);
                                fTypeArray=rType.getFileArray();
                                if(fTypeArray!=null)
                                {
                                    for(FileType fType:fTypeArray)
                                    {
                                        colLocations.add(getXmlBaseAttribForFileTypes(fType,xmlBaseAux));
                                        colLocations=getLocationFromMetadata(fType.getMetadata(),colLocations,xmlBaseAux);
                                    }
                                }                           
                            }
                        }         
                        colLocations=getLomTechnicalLocation(mDataType,colLocations);
                        colLocations=getManifestMetaDataRecursive(mType,colLocations,xmlBase);                    
                    }
                }
                //Do This Manifest Node...
                mDataType=manType.getMetadata();
                colLocations=getLocationFromMetadata(mDataType,colLocations,xmlBase);                
                //Back to Manifest
                OrganizationsType orgsType=manType.getOrganizations();
                if(orgsType!=null)
                {
                    OrganizationType[] orgArrayType=orgsType.getOrganizationArray();
                    if(orgArrayType!=null)
                    {
                        for(OrganizationType orgType:orgArrayType)
                        {
                            mDataType=orgType.getMetadata();
                            colLocations=getLocationFromMetadata(mDataType,colLocations,xmlBase);
                            //Item in Organization may also have MetaData
                            ItemType[] itemArrayType=orgType.getItemArray();
                            //gets all locations from all items recursively  
                            colLocations=getItemsMetaDataRecursive(itemArrayType,colLocations,xmlBase);
                        }                
                    }
                }//Locations:done for Organizations
                //Locations for Resources...
                rsType=manType.getResources();
                if(rsType!=null)
                {   
                    FileType[] fTypeArray=null;
                    xmlBaseAux=getXmlBaseAttrib(rsType,xmlBase);
                    //xmlBaseAux=getXmlBaseAttrib(rsType,xmlBase);            
                    for(ResourceType rType:rsType.getResourceArray())
                    {
                        String xmlBaseAux1=getXmlBaseAttrib(rType,xmlBaseAux); 
                        colLocations=getLocationFromMetadata(rType.getMetadata(),colLocations,xmlBaseAux1);
                        fTypeArray=rType.getFileArray();
                        if(fTypeArray!=null)
                        {
                            for(FileType fType:fTypeArray)
                            {
                                colLocations.add(getXmlBaseAttribForFileTypes(fType,xmlBaseAux1));
                                colLocations=getLocationFromMetadata(fType.getMetadata(),colLocations,xmlBaseAux1);
                            }
                        }                           
                    }
                }         
                colLocations=getLomTechnicalLocation(mDataType,colLocations);               
            }
        }
        catch(XmlException xmle)
        {
            throw xmle;
        }
        catch(IOException ioe)
        {
            throw ioe;
        }
        catch(Exception e)
        {
            throw e;
        }
        return colLocations;
    }
    
    //Get Locations from all Metadatas in all ItemTypes found,adds to Collection of Strings
    //representing Locations and valid complete Hrefs for the SCORM Package
    private Collection<String> getItemsMetaDataRecursive(ItemType[] iTypeArray,Collection<String> colLocations,String xmlBase) throws Exception
    {
        try
        {
            if(iTypeArray!=null)
            {
                for(ItemType it:iTypeArray)
                {
                    colLocations=getLocationFromMetadata(it.getMetadata(),colLocations,xmlBase); 
                    colLocations=getItemsMetaDataRecursive(it.getItemArray(),colLocations,xmlBase);
                }
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        return colLocations;
    }
    
    //Loms Technical element has a Location, adding them
    private Collection<String> getLomTechnicalLocation(MetadataType metaType,Collection<String> colLocations) throws Exception
    {
        try{
            if(metaType!=null)
            {                    
                NodeList nodelist=metaType.getDomNode().getChildNodes();
                int pos =0;
                while(pos<nodelist.getLength()&&!nodelist.item(pos).getNodeName().startsWith("lom"))
                {
                    pos++;
                }
                LomDocument lomDocument=null;
        //      Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
                if(pos<nodelist.getLength())
                    lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));                
                if(lomDocument!=null)
                {
                    Lom lomType= lomDocument.getLom();
                    if(lomType!=null)
                    {
                        Technical[] tTypeArray=lomType.getTechnicalArray();
                        if(tTypeArray!=null)
                        {
                            for(Technical tType:tTypeArray)
                            {
                                if(tType!=null)
                                {
                                    Location[] locTypeArray= tType.getLocationArray();
                                    if(locTypeArray!=null)
                                    {
                                        for(Location locType:locTypeArray)
                                        {
                                            colLocations.add(locType.getStringValue());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            throw e;
        }        
        return colLocations;
    }

//  Given a MetaDataType gives it's adlcp:Location value
    private Collection<String> getLocationFromMetadata(MetadataType metaType,Collection<String> colLocations,String locXmlbase) throws Exception
    {
        NodeList nodeList=null;
        Location lType=null;
        String locXmlBaseAux=null;
        try
        {
            if(metaType!=null)
            {                
                nodeList=metaType.getDomNode().getChildNodes();
                if(nodeList!=null)
                {
                    for(int i= 0;i<nodeList.getLength();i++)
                    {             
                        if(nodeList.item(i).getNodeName().compareToIgnoreCase("adlcp:location")==0)
                        {                            
                            lType=Location.Factory.parse(nodeList.item(i));
                            if(lType!=null)
                            {     
                                locXmlBaseAux=lType.getStringValue();                           
                                if(locXmlbase!=null)
                                {
                                    if(locXmlBaseAux.startsWith("/"))
                                        locXmlBaseAux=locXmlBaseAux.substring(1);
                                    locXmlBaseAux=locXmlbase.concat(locXmlBaseAux);
                                }
                                else
                                {
                                    locXmlBaseAux=this.getXmlBase().concat(locXmlBaseAux);
                                }
                                colLocations.add(locXmlBaseAux);
                            }                           
                        }                        
                    }
                }                            
            }
        }
        catch(Exception e)
        {
            throw e;
        }
        return colLocations;
    }
  
    //Gets Xsd names at the manifest tags attributes
    public Collection<File> getXsdFromXml(File xmlFile) throws XmlException,IOException 
    {
        Collection<File> fileCol=new HashSet<File>();
        ManifestDocument mDoc = null;
        int count=0,countaux=0;
        
        try{
            if(xmlFile!=null)
            {
                mDoc= ManifestDocument.Factory.parse(xmlFile);
                if(mDoc!=null)
                {
                    ManifestType mt=mDoc.getManifest();
                    Node xsdNode=mt.getDomNode().getAttributes().getNamedItem("xsi:schemaLocation");          
                    String strXsds=xsdNode.getNodeValue();
                    String straux = null;
                    while((count=strXsds.indexOf(".xsd"))!=-1)
                    {                        
                        while(strXsds.charAt(count)!=' ')
                        {
                            count--;
                            countaux++;
                        }            
                        straux = strXsds.substring(count+1,count+countaux+4);
                        count+=countaux+1;
                        countaux=0;
                        strXsds=strXsds.substring(count);
                        fileCol.add(this.resolveUrl(this.xmlBase,straux));
                    } 
                }
            }
        }
        catch(XmlException xe)
        {
            throw xe;
        }
        catch(IOException ioe)
        {
            throw ioe;
        }
        return fileCol;
    }

    //Devolve o File com ao xmlBase vindo do tag metadata local ou do base geral
    //e com o location do ficheiro local.Para por fim ter o Absolute path.
    private File resolveUrl(String base, String location)
    {  
        if(base ==null)
        {
            if(this.xmlBase!=null)
            {
                base=this.xmlBase;                
            }
            else
                base="/";
        }      
        if(!base.endsWith("/"))
            base=base.concat("/");
        location=base.concat(location);
        if(location !=null)
        {
            File f=new File(location);                       
            return f;
        }
        return null;
    }
    
    //This method is called in another method
    //Just used by the bottom method,will give out a Collection of Nodes
    //that are not of the basic scormChildren named in the scormChildren
    private Collection<Node> calledBygetAnyLoms(NodeList nodeList,String[] scormChildren)
    {
        //System.out.print("SCORM Elements: ");
        for(int j=0;j<scormChildren.length;j++)
        {
            //System.out.print(""+scormChildren[j]+"; ");
        }
        //System.out.println();
        //System.out.print("This Nodes Elements: ");
        for(int x=0;x<nodeList.getLength();x++)
        {
            //System.out.print(""+nodeList.item(x).getNodeName()+"; ");
        }
        //System.out.println();
        Collection<Node> colNodes=new ArrayList<Node>();
        Collection<Node> colNodesAux=null;
        boolean isAnyNode=true;
        if(nodeList!=null)
        {
            for(int i=0;i<nodeList.getLength();i++)
            {
                isAnyNode=true;
                String strNodeNameTemp=nodeList.item(i).getNodeName();
                for(int j=0;j<scormChildren.length;j++)
                {
                    if(nodeList.item(i).getNodeValue()==null||!nodeList.item(i).hasChildNodes()|| strNodeNameTemp.startsWith("#"))
                        isAnyNode=false;
                    else
                    {
                        //if we enter here it's because we encountered a
                        //ChildNode of the Any value.Still we must verify that
                        //the node is a manifest or lom
                        if(strNodeNameTemp.compareToIgnoreCase(scormChildren[j])==0||strNodeNameTemp.compareToIgnoreCase("metadata")!=0||strNodeNameTemp.compareToIgnoreCase("lom")!=0)
                        {      
                            isAnyNode=false;                        
                        }
                    }
                }
                if(isAnyNode==true)
                {
                    //System.out.println("is an AnyNode "+strNodeNameTemp+" Value is: "+nodeList.item(i).getNodeValue());
                    colNodes.add(nodeList.item(i));
                }
                //System.out.println("Getting anyLoms for this node now: "+strNodeNameTemp+" Value is: "+nodeList.item(i).getNodeValue());
                colNodesAux=getAnyLoms(nodeList.item(i));
                if(colNodesAux!=null)
                    colNodes.addAll(colNodesAux);
            }
        }
        return colNodes;
    }
    //Call this method!In method that need to treat the manifest Tags
    //will return a Collection of Nodes being them Lom or Metadata
    //from a XmlObject that should be the Root Element for our ImsManifest.xml
    //manifest tag element
    private Collection<Node> getAnyLoms(XmlObject xmlObj)
    {       
        if(xmlObj!=null)
        {
            Node xmlObjNode = xmlObj.getDomNode();
            NodeList nodeList=xmlObjNode.getChildNodes();
            //System.out.println("Searching AnyLom in this Node: "+xmlObjNode.getNodeName());
            if(xmlObjNode.getNodeName().compareToIgnoreCase("manifest")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_MANIFEST);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("metadata")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_METADATA);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("organizations")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_ORGANIZATIONS);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("organization")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_ORGANIZATION);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("item")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_ITEM);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("resources")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_RESOURCES);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("resource")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_RESOURCE);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("file")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_FILE);
        }
        return null;
    }
    //This method is called in another method
    //will return a Collection of Nodes being them Lom or Metadata
    //from a XmlObject
    private Collection<Node> getAnyLoms(Node xmlObjNode)
    {
        Collection<Node> colNodes=null;
        if(xmlObjNode!=null)
        {   
            //Node xmlObjNode = xmlObj.getDomNode();
            NodeList nodeList=xmlObjNode.getChildNodes();
            if(xmlObjNode.getNodeName().compareToIgnoreCase("manifest")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_MANIFEST);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("metadata")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_METADATA);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("organizations")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_ORGANIZATIONS);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("organization")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_ORGANIZATION);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("item")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_ITEM);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("resources")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_RESOURCES);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("resource")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_RESOURCE);
            if(xmlObjNode.getNodeName().compareToIgnoreCase("file")==0)
                return calledBygetAnyLoms(nodeList,ImsManifestReader.SCORM_TAG_FILE);
        }
        return colNodes;
    }
    
    private Collection<ScormMetaData> readMetaData(File metaFile) throws IOException,XmlException
    {        
        try
        {          
            Collection<ScormMetaData> moreItemMetaData=null;
            MetadataDocument metaDoc=MetadataDocument.Factory.parse(metaFile);
            if(metaDoc!=null)
            {
                MetadataType metaType= metaDoc.getMetadata();
                if(metaType!=null)
                {
                    NodeList nodelist=metaType.getDomNode().getChildNodes();
                    int pos =0;
                    while(pos<nodelist.getLength()&&!nodelist.item(pos).getNodeName().startsWith("lom"))
                    {
                        pos++;
                    }
                    LomDocument lomDocument=null;
                    //Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
                    if(pos<nodelist.getLength())
                        lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));                
                    if(lomDocument!=null)
                    {
                        Lom lomType= lomDocument.getLom();
                        if(lomType!=null)
                        {
                            //HERE WE WORK WITH LOM TYPES!!
                            moreItemMetaData=ImsManifestReader.allDspaceMetaData(lomType);                             
                        }
                    }
                }
            }
            return moreItemMetaData;
        }
        catch(IOException e)
        {throw e;}
        catch(XmlException e)
        {throw e;}
    }
    
    private Collection<ScormMetaData> readMetaData(MetadataType metType) throws IOException,XmlException
    {        
        try
        {          
            Collection<ScormMetaData> moreItemMetaData=null;
            MetadataDocument metaDoc=MetadataDocument.Factory.parse(metType.getDomNode());
            if(metaDoc!=null)
            {
                MetadataType metaType= metaDoc.getMetadata();
                if(metaType!=null)
                {
                    NodeList nodelist=metaType.getDomNode().getChildNodes();
                    int pos =0;
                    while(pos<nodelist.getLength()&&!nodelist.item(pos).getNodeName().startsWith("lom"))
                    {
                        pos++;
                    }
                    LomDocument lomDocument=null;
                    //Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
                    if(pos<nodelist.getLength())
                        lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));                
                    if(lomDocument!=null)
                    {
                        Lom lomType= lomDocument.getLom();
                        if(lomType!=null)
                        {
                            //HERE WE WORK WITH LOM TYPES!!
                            moreItemMetaData=ImsManifestReader.allDspaceMetaData(lomType);                             
                        }
                    }
                }
            }
            return moreItemMetaData;
        }
        catch(XmlException e)
        {throw e;}
    }
    
    
    private Collection<ScormMetaData> readMetaData(Node metaNode) throws IOException,XmlException
    {        
        try
        {          
            Collection<ScormMetaData> moreItemMetaData=null;
            MetadataDocument metaDoc=MetadataDocument.Factory.parse(metaNode);
            if(metaDoc!=null)
            {
                MetadataType metaType= metaDoc.getMetadata();
                if(metaType!=null)
                {
                    NodeList nodelist=metaType.getDomNode().getChildNodes();
                    int pos =0;
                    while(pos<nodelist.getLength()&&!nodelist.item(pos).getNodeName().startsWith("lom"))
                    {
                        pos++;
                    }
                    LomDocument lomDocument=null;
                    //Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
                    if(pos<nodelist.getLength())
                        lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));                
                    if(lomDocument!=null)
                    {
                        Lom lomType= lomDocument.getLom();
                        if(lomType!=null)
                        {
                            //HERE WE WORK WITH LOM TYPES!!
                            moreItemMetaData=ImsManifestReader.allDspaceMetaData(lomType);                             
                        }
                    }
                }
            }
            return moreItemMetaData;
        }
        catch(XmlException e)
        {throw e;}
    }
    
    private Collection<ScormMetaData> readLomData(File lomFile) throws IOException,XmlException
    {        
        try
        {          
            Collection<ScormMetaData> moreItemMetaData=null;
            LomDocument lomDocument=null;
            //Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
            lomDocument = LomDocument.Factory.parse(lomFile);
            System.out.println("WE'RE PARSING AN EXTERNAL LOMFILE NAMED: "+lomFile.getAbsolutePath());
            if(lomDocument!=null)
            {                
                Lom lomType= lomDocument.getLom();
                if(lomType!=null)
                {
                    //HERE WE WORK WITH LOM TYPES!!
                    moreItemMetaData=ImsManifestReader.allDspaceMetaData(lomType);                             
                }
            }
            return moreItemMetaData;
        }
        catch(IOException e)
        {throw e;}
        catch(XmlException e)
        {throw e;}
    }
    
    private Collection<ScormMetaData> readLomData(Node lomNode) throws IOException,XmlException
    {        
        try
        {          
            Collection<ScormMetaData> moreItemMetaData=null;
            LomDocument lomDocument=null;
            //Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
            lomDocument = LomDocument.Factory.parse(lomNode);
            if(lomDocument!=null)
            {                
                Lom lomType= lomDocument.getLom();
                if(lomType!=null)
                {
                    //HERE WE WORK WITH LOM TYPES!!
                    moreItemMetaData=ImsManifestReader.allDspaceMetaData(lomType);                             
                }
            }
            return moreItemMetaData;
        }        
        catch(XmlException e)
        {throw e;}
    }
    
    private Collection<ScormMetaData> readManifestData(ManifestType manType) throws XmlException,IOException
    {
        try{
            Collection<ScormMetaData> moreItemMetaData=null;
            Collection<ScormMetaData> moreItemMetaDataAux=null;
            MetadataDocument metaDoc=MetadataDocument.Factory.parse(manType.getMetadata().getDomNode());
            if(metaDoc!=null)
            {
                //MetaData
                MetadataType metaType= metaDoc.getMetadata();
                if(metaType!=null)
                {
                    NodeList nodelist=metaType.getDomNode().getChildNodes();
                    int pos =0;
                    while(pos<nodelist.getLength()&&!nodelist.item(pos).getNodeName().startsWith("lom"))
                    {
                        pos++;
                    }
                    LomDocument lomDocument=null;
            //      Getting the Lom Node so we can get it's atributes,is length of metadata tag-2 (length of metadata tag-1 is text)
                    if(pos<nodelist.getLength())
                        lomDocument = LomDocument.Factory.parse(metaType.getDomNode().getChildNodes().item(pos));                
                    if(lomDocument!=null)
                    {
                        Lom lomType= lomDocument.getLom();
                        if(lomType!=null)
                        {
                            //HERE WE WORK WITH LOM TYPES!!
                            moreItemMetaData=ImsManifestReader.allDspaceMetaData(lomType);                             
                        }
                    }
                }
            }
            //Organizations
            OrganizationsType orgsType=manType.getOrganizations();
            if(orgsType!=null)
            {
                OrganizationType[] orgArray=orgsType.getOrganizationArray();
                for(OrganizationType org:orgArray)
                {
                    MetadataType metaT=org.getMetadata();
                    if(metaT!=null)
                        moreItemMetaDataAux=readMetaData(metaT);
                    if(moreItemMetaDataAux!=null)
                        moreItemMetaData.addAll(moreItemMetaDataAux);
                }
            }
            //Resources
            ResourcesType resType=manType.getResources();
            if(resType!=null)
            {
                ResourceType[] resArray=resType.getResourceArray();
                for(ResourceType res:resArray)
                {
                    MetadataType metaT=res.getMetadata();
                    if(metaT!=null)
                        moreItemMetaDataAux=readMetaData(metaT);
                    if(moreItemMetaDataAux!=null)
                        moreItemMetaData.addAll(moreItemMetaDataAux);
                }
            }
            return moreItemMetaData;
        }
        catch(XmlException e)
        {
            throw e;
        }
        catch(IOException e)
        {
            throw e;
        }
    }
    
    //Here we will be extracting the Content Aggregation Meta-data(context specific data describing the packaged course)
    //out of the imsmanifest.xml file
    public Collection<ScormMetaData> readManifestData(File imsManifestFile,Collection<String> externalMetadataFiles) throws IOException,XmlException
    {
        Collection<ScormMetaData> colItemMetaDataToReturn=new ArrayList<ScormMetaData>();
        Collection<ScormMetaData> colItemMetaDataAux=null;
        Collection<Node> colLomOrMetaNodes=null;
        ManifestDocument mDoc = null;
        try
        {
            //String straux =null;
            System.out.println("XML FILE about to be PARSED: "+imsManifestFile.getAbsolutePath());
            mDoc = ManifestDocument.Factory.parse(imsManifestFile);
            if(mDoc!=null)
            {
                ManifestType manType=mDoc.getManifest();
                colLomOrMetaNodes=getAnyLoms(manType);
                //Tratando dos Any Tags
                for(Node node:colLomOrMetaNodes)
                {
                    //System.out.println("Collection of Nodes that are ANY types: "+node.getNodeName());
                    try
                    {
                        colItemMetaDataAux=readMetaData(node);
                    }
                    catch (XmlException e) 
                    {
                        //e.printStackTrace();
                        colItemMetaDataAux=null;
                    }
                    if(colItemMetaDataAux!=null)
                        colItemMetaDataToReturn.addAll(colItemMetaDataAux);
                    else
                    {
                        try
                        {
                            colItemMetaDataAux=readLomData(node);
                        }
                        catch(XmlException e)
                        {
                            //e.printStackTrace();
                            colItemMetaDataAux=null;
                        }
                        if(colItemMetaDataAux!=null)
                            colItemMetaDataToReturn.addAll(colItemMetaDataAux);
                    }
                }
                
                if(manType!=null)
                {        
                    ManifestType[] manTypeArray=manType.getManifestArray();
                    for(ManifestType maniType:manTypeArray)
                    {
                        colItemMetaDataAux=readManifestData(maniType);
                        if(colItemMetaDataAux!=null)
                            colItemMetaDataToReturn.addAll(colItemMetaDataAux);
                    }
                    colItemMetaDataAux=readManifestData(manType);
                    if(colItemMetaDataAux!=null)
                        colItemMetaDataToReturn.addAll(colItemMetaDataAux);                   
                }
                //Agora vamos ler os Ficheiros todos .xml da nossa collection
                //são os referenciados pelo imsmanifest.xml
                if(externalMetadataFiles!=null)
                {
                    for(String fileURL:externalMetadataFiles)
                    {
                        if(fileURL.endsWith(".xml"))
                        {
                            System.out.println("Checking out this xmlFile: "+fileURL);
                            try
                            {
                                colItemMetaDataAux=readMetaData(new File(fileURL));
                            }
                            catch (XmlException e) 
                            {
                                //e.printStackTrace();
                                colItemMetaDataAux=null;
                            }
                            if(colItemMetaDataAux!=null)
                                colItemMetaDataToReturn.addAll(colItemMetaDataAux);
                            else
                            {
                                try
                                {
                                    //System.out.println("HERE!");
                                    colItemMetaDataAux=readLomData(new File(fileURL));
                                }
                                catch(XmlException e)
                                {
                                    //e.printStackTrace();
                                    colItemMetaDataAux=null;
                                }
                                if(colItemMetaDataAux!=null)
                                    colItemMetaDataToReturn.addAll(colItemMetaDataAux);
                            }
                        }
                    }
                }
            }
            this.setItemDSpaceMetaData(colItemMetaDataToReturn);
            return this.getItemDSpaceMetaData();
        }
        catch(IOException e)
        {throw e;}
        catch(XmlException e)
        {throw e;}
    }
    
    All the getXxxxQualifier methods below will except as an argument the 
    respective XxxxType instance,if the case has many sub-elements it will
    be necessary to go through all of them and compare with the second
    argument,a String.If the qualifier is found
    'i' is returned else '-1'
    
    
    All the getXxxxLang methods below will except as an argument the 
    respective XxxxType instance, and gather the attribute named "Lang"
    of the value LangstringType,and it's value will be returned.
    
    
    public static Collection<ScormMetaData> allDspaceMetaData(Lom lt)
    {
        if(lt !=null)
        {
            Collection<ScormMetaData> allMetaDataCol= new ArrayList<ScormMetaData>(); 
            ScormMetaData[] tempMetaData=null;
            tempMetaData=titleItemMetaData(lt);
            if(tempMetaData!=null)
            {                      
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=contributorItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=subjectItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=descriptionItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=dateItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=formatItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=identifierItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=typeItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=relationItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=languageItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=coverageItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            tempMetaData=rightsItemMetaData(lt);
            if(tempMetaData!=null)
            {
                for(int i=0;i<tempMetaData.length;i++)
                {
                    allMetaDataCol.add(tempMetaData[i]);
                }
            }
            return allMetaDataCol;
        }
        return null;
    }
    
    
    private static ScormMetaData[] titleItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        ScormMetaData[] imArray=new ScormMetaData[1];
        String str="title";
        //DSpace Element Stored
        im.setElement(str);
        String[] tempVal=null;
        
        General[] gtArray= lt.getGeneralArray();
        if(gtArray!=null)
        {
            for(General gt:gtArray)
            {
                Title[] ttArray=gt.getTitleArray();
                if(ttArray!=null)
                {
                    for(Title tt:ttArray)
                    {           
                        LangString2[] langTypeArray=tt.getStringArray();
                        if(langTypeArray.length!=0)
                            tempVal=new String[langTypeArray.length];
                        if(langTypeArray!=null)
                        {
                            for(int j= 0;j<langTypeArray.length;j++)
                            {
                                tempVal[j]=langTypeArray[j].getStringValue();                                
                            }
                            //DSpace Values Stored
                            im.setValues(tempVal);
                        } 
                        //DSpace Qualifier Stored
                        int pos =getTitleQualifier(tt);
                        if(pos!=-1)
                        {
                            im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_TITLE[pos]);
                        }
                        else
                        {
                            im.setQualifier(null);
                        }
                        //DSpace Lang Stored
                        im.setLang(getTitleLang(tt));
                        imArray[0]=im;
                    }
                }
            }
        }
        return imArray;
    }
    
    private static int getTitleQualifier(Title tt)
    {
        return -1;
    }
    
    private static String getTitleLang(Title tt)
    {
        String str=null;
        LangString2[] langAux=tt.getStringArray();
        if(langAux!=null)
        {
           for(LangString2 lstr:langAux)
            {
                str=lstr.getLanguage();
                if(str!=null)
                    return str;
            }          
        }
        return str;
    }
    //Trata de contributor com Qualifier igual a 'author','publisher' e afins
    private static ScormMetaData[] contributorItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        ScormMetaData[] imArray =null;
        String str="contributor";
        boolean isLOM = true;
        //DSpace Element Stored
        im.setElement(str);
        String[] tempVal=null;
        
        LifeCycle[] lifeCycleTypeArray =lt.getLifeCycleArray();
        if(lifeCycleTypeArray!=null)
        {
            for(LifeCycle lifeCycleType:lifeCycleTypeArray)
            {
                Contribute[] contribTypeArray=lifeCycleType.getContributeArray();
                if(contribTypeArray!=null)
                {
                    //Role rt=null;
                    RoleValue[] vtArray=null;
                    for(int i=0;i<contribTypeArray.length;i++)
                    {
                        imArray = new ScormMetaData[contribTypeArray.length];
                        Role[] roleArray =contribTypeArray[i].getRoleArray();
                        if(roleArray!=null)
                        {
                            for(Role role:roleArray)
                            {  
                                if(role!=null)
                                {
                                    SourceValue[] svArray=role.getSourceArray();
                                    if(svArray!=null)
                                    {
                                        for(SourceValue st:svArray)
                                        {
                                            String strLom;
                                            strLom=st.getStringValue();
                                            if(strLom !=null && strLom.trim().compareTo("")!=0)
                                            {
                                                isLOM=strLom.compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_LOM_VERSION)==0;
                                            }
                                            else
                                            {
                                                isLOM=true;
                                            }
                                        }
                                    }                       
                                    if((vtArray=role.getValueArray())!=null)
                                    {
                                        for(RoleValue vt:vtArray)
                                        {
                                            str=vt.getStringValue();
                                            String[] entTypeArray=contribTypeArray[i].getEntityArray();
                                            if(isLOM)
                                            {
                                                if(entTypeArray!=null)
                                                {                                
                                                    if(entTypeArray.length!=0)
                                                        tempVal = new String[entTypeArray.length];
                                                    for(int j=0;j<entTypeArray.length;j++)
                                                    {
                                                        tempVal[j]=entTypeArray[j];
                                                    }
                                                }
                                            }
                                            //DSpace Qualifier Stored
                                            if(str.compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_AUTH)!=0 && str.compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_PUB)!=0)
                                                im.setQualifier(null);
                                            else
                                            {
                                                if(getContributorQualifier(role)!=-1)
                                                    im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_CONTRIBUTOR[getContributorQualifier(role)]);
                                            }
                        //                  DSpace Lang Stored
                                            im.setLang(getContributorLang(role));
                        //                  DSpace Values Stored
                                            im.setValues(tempVal);
                                            imArray[i]=im;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }  
        return imArray;
    }
    
    //Estes Dois Servem para Qualifiers={author,publisher e null}
    private static int getContributorQualifier(Role rt)
    {
        for(int i=0;i<ImsManifestReader.DUBLIN_CORE_QUALIFIER_CONTRIBUTOR.length;i++)
        {
            RoleValue[] rvArray=rt.getValueArray();
            if(rvArray!=null)
            {
                for(RoleValue rv:rvArray)
                {
                    if(rv.getStringValue().compareToIgnoreCase(ImsManifestReader.DUBLIN_CORE_QUALIFIER_CONTRIBUTOR[i])==0)
                        return i;
                }
            }
        }
        
        return -1;
    }
    
    private static String getContributorLang(Role rt)
    {
        String str=null;
        RoleValue[] rvArray= rt.getValueArray();
        if(rvArray!=null)
        {
            for(RoleValue rv:rvArray)
            {    
                if((str=rv.selectAttribute(null,"lang").getDomNode().getNodeValue())!=null)
                    return str;
                else if((str=rv.selectAttribute(null,"Lang").getDomNode().getNodeValue())!=null)
                {
                    return str;
                }
            }
        }
        return str;
    }
 
    //Devolve todos o Subjects...
    private static ScormMetaData[] subjectItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        ScormMetaData[] imArray=null;
        String str="subject";
        //DSpace Element Stored
        im.setElement(str);
        String[] tempVal=null;
        
        General[] gtArray=lt.getGeneralArray();
        if(gtArray!=null)
        {
            for(General gt:gtArray)
            {  
                Keyword[] kwt=gt.getKeywordArray();
                if(kwt!=null)
                {
                    imArray=new ScormMetaData[kwt.length];
                    for(int i=0;i<kwt.length;i++)
                    {                    
                        LangString2[] langTypeArray=kwt[i].getStringArray();
                        if(langTypeArray!=null)
                        {                        
                            if(langTypeArray.length!=0)
                                tempVal=new String[langTypeArray.length];
                            for(int j=0;j<langTypeArray.length;j++)
                            {
                                tempVal[j]=langTypeArray[j].getStringValue();
                            }
    //                      DSpace Value Stored
                            im.setValues(tempVal);
                        }
    //                  DSpace Qualifier Stored
                        int pos =getSubjectQualifier(kwt[i]);
                        if(pos!=-1)
                        {
                            im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_SUBJECT[pos]);
                        }
                        else
                        {
                            im.setQualifier(null);
                        }
                        //DSpace Lang Stored
                        im.setLang(getSubjectLang(kwt[i]));
                        imArray[i]=im;
                    }
                    
                }
            }
        }
        return imArray;
    }
    
    private static int getSubjectQualifier(Keyword kt)
    {        
        return -1;
    }
    
    private static String getSubjectLang(Keyword kt)
    {
        String str=null;
        LangString2[] langAux= kt.getStringArray();
        for(int i=0;i<langAux.length;i++)
        {
            if(langAux[i]!=null)
            {
                return langAux[i].getLanguage();
                XmlObject xmlObj=langAux[i].selectAttribute(null,"lang");
                if(xmlObj!=null)
                {
                    if((str=xmlObj.getDomNode().getNodeValue())!=null)
                        return str;
                    else 
                    {
                        xmlObj=langAux[i].selectAttribute(null,"Lang");
                        if(xmlObj!=null)
                        {
                            if((str=xmlObj.getDomNode().getNodeValue())!=null)
                                return str;
                        }
                    }
                }
            }
        }
        return str;
    }
    
    private static ScormMetaData[] descriptionItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        ScormMetaData[] imArray=null;
        String str="description";
        //DSpace Element Stored
        im.setElement(str);
        String[] tempVal=null;
        
        General[] gtArray= lt.getGeneralArray();
        if(gtArray!=null)
        {
            for(General gt:gtArray)
            {
                LangString[] dt=gt.getDescriptionArray();
                if(dt!=null)
                {
                    for(int i= 0;i<dt.length;i++)
                        {
                            imArray = new ScormMetaData[dt.length];
                            LangString2[] langTypeArray=dt[i].getStringArray();
                            if(langTypeArray!=null)
                            {
                                if(langTypeArray.length!=0)
                                    tempVal = new String[langTypeArray.length];
                                for(int j= 0;j<langTypeArray.length;j++)
                                {
                                    tempVal[j]=langTypeArray[j].getStringValue();
                                }
        //                      DSpace Value Stored
                                im.setValues(tempVal);
                            }                    
                            //DSpace Qualifier Stored
                            int pos =getDescriptionQualifier(dt[i]);
                            if(pos!=-1)
                            {
                                im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_DESCRIPTION[pos]);
                            }
                            else
                            {
                                im.setQualifier(null);
                            }
                            //DSpace Lang Stored
                            im.setLang(getDescriptionLang(dt[i]));
                            imArray[i]=im;
                        }
                }
            }
        }
        return imArray;
    }
    
    
    private static int getDescriptionQualifier(LangString dt)
    {        
        return -1;
    }
    
    private static String getDescriptionLang(LangString dt)
    {
        String str=null;
        LangString2[] langAux= dt.getStringArray();
        for(int i=0;i<langAux.length;i++)
        {
            if(langAux[i]!=null)
            {
                return langAux[i].getLanguage();                
            }
        }
        return str;
    }
    
    private static ScormMetaData[] dateItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        ScormMetaData[] imArray=null;
        String str="date";
        //DSpace Element Stored
        im.setElement(str);
        String[] tempVal=null;
        boolean isLOM=true;
        
        LifeCycle[] lctArray= lt.getLifeCycleArray();
        if(lctArray!=null)
        {
            for(LifeCycle lct:lctArray)
            {    
                Contribute[] ctArray=lct.getContributeArray();            
                if(ctArray!=null)
                {
                   for(int i=0;i<ctArray.length;i++)
                        {
                            Role[] rtArray = ctArray[i].getRoleArray();
                            if(rtArray!=null)
                            {
                                for(Role rt:rtArray)
                                {
                                    SourceValue[] stArray=rt.getSourceArray();
                                    if(stArray!=null)
                                    {
                                        for(SourceValue st:stArray)
                                        {
                                            String strLom;
                                            strLom=st.getStringValue();
                                            if(strLom !=null && strLom.trim().compareTo("")!=0)
                                            {
                                                isLOM=strLom.compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_LOM_VERSION)==0;
                                            }
                                            else
                                            {
                                                isLOM=true;
                                            }
                                        }
                                    }   
                                    RoleValue[] rvArray=rt.getValueArray();
                                    if(rvArray!=null)
                                    {
                                        for(RoleValue rv:rvArray)
                                        {
                                            if(rv.getStringValue().compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_AUTH)==0)
                                            {
                                                imArray = new ScormMetaData[1];
                                                if(isLOM)
                                                {
                                                    Date[] dateArray=ctArray[i].getDateArray();
                                                    if(dateArray!=null)
                                                    {
                                                        tempVal = new String[dateArray.length];
                                                        for(int j=0;i<dateArray.length;i++)
                                                        {
                                                            if(dateArray[j]!=null)
                                                            {
                                                                DateTimeValue[] dtvArray=dateArray[j].getDateTimeArray();
                                                                for(DateTimeValue dtv:dtvArray)
                                                                {                                                                    
                                                                    tempVal[j]=dtv.getStringValue();
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                    //                          DSpace Value Stored
                                                im.setValues(tempVal);
                    //                          DSpace Qualifier Stored
                                                int pos =getDateQualifier(ctArray[i]);
                                                if(pos!=-1)
                                                {
                                                    im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_DATE[pos]);
                                                }
                                                else
                                                {
                                                    im.setQualifier(null);
                                                }
                                                //DSpace Lang Stored
                                                im.setLang(getDateLang(ctArray[i]));
                                                imArray[0]=im;
                                                return imArray;
                                            } 
                                        }
                                    }
                                }
                            }
                        }
                   
                }
            }
        }
        return imArray;
    }
    
    private static int getDateQualifier(Contribute ct)
    {   
        for(int i=0;i<ImsManifestReader.DUBLIN_CORE_QUALIFIER_DATE.length;i++)
        {
            if(ImsManifestReader.DUBLIN_CORE_QUALIFIER_DATE[i].compareToIgnoreCase("created")==0)
                return i;
        }
        
        return -1;
    }
    
    private static String getDateLang(Contribute ct)
    {
        String str=null;
        Role[] rtArray = ct.getRoleArray();
        if(rtArray!=null)
        {
           for(Role rt:rtArray)
           {
               RoleValue[] rtValue= rt.getValueArray();
               if(rtValue!=null)
               {  
                   for(RoleValue roleValue:rtValue)
                   {   
                       if(roleValue.getStringValue().compareToIgnoreCase("author")==0)
                       {
                           Date[] dateArray= ct.getDateArray();
                           if(dateArray!=null)
                           {
                               for(Date date:dateArray)
                               {
                                   Description[] descArray=date.getDescriptionArray();
                                   if(descArray!=null)
                                   {    
                                       for(Description desc:descArray)
                                       {
                                           LangString2[] langAux= desc.getStringArray();
                                           for(int i=0;i<langAux.length;i++)
                                           {
                                               if(langAux[i]!=null)
                                               {
                                                   return langAux[i].getLanguage();
                                                   XmlObject xmlObj=langAux[i].selectAttribute(null,"lang");
                                                   if(xmlObj!=null)
                                                   {
                                                       if((str=xmlObj.getDomNode().getNodeValue())!=null)
                                                           return str;
                                                       else 
                                                       {
                                                           xmlObj=langAux[i].selectAttribute(null,"Lang");
                                                           if(xmlObj!=null)
                                                           {
                                                               if((str=xmlObj.getDomNode().getNodeValue())!=null)
                                                                   return str;
                                                           }
                                                       }
                                                   }  
                                               }
                                           }
                                       }
                                   }
                               }
                           }
                       }
                   }
               }
            }
        }
        return str;
    }
    
    private static ScormMetaData[] formatItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        ScormMetaData[] imArray=null;
        String str="format";
        //DSpace Element Stored
        im.setElement(str);
        String[] tempVal=null;
        
        Technical[] tecArray=lt.getTechnicalArray();
        if(tecArray!=null)
        {   
            for(Technical techType:tecArray)
            {    
                if(techType!=null)
                {
                    Format[] formatArray=techType.getFormatArray();
                    if(formatArray!=null)
                    {
                        imArray = new ScormMetaData[1];
                        if(formatArray.length!=0)
                            tempVal = new String[formatArray.length];
                        for(int i= 0;i<formatArray.length;i++)
                        {
                            tempVal[i]=formatArray[i].getStringValue();
                        }
                        //DSpace Value Entered
                        im.setValues(tempVal);
        //              DSpace Qualifier Entered                   
                        int pos = getFormatQualifier(techType);
                        if(pos!=-1)
                        {
                            im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_FORMAT[pos]);
                        }
                        else
                        {
                            im.setQualifier(null);
                        }
                        //DSpace Lang Entered
                        im.setLang(getFormatLang(techType));
                        imArray[0]=im;
                    }
                }
            }
        } 
        return imArray;
    }
    
    private static int getFormatQualifier(Technical tt)
    {        
        return -1;
    }
    
    private static String getFormatLang(Technical tt)
    {
        String str=null;   
        Duration2[] dArray=tt.getDurationArray();
        //Requirement[] rtArray = tt.getRequirementArray();
        if(dArray!=null&&dArray.length>0)
        {      
            for(Duration2 dt:dArray)
            {
                Description[] descArray=dt.getDescriptionArray();
                for(Description desc:descArray)
                {
                    LangString2[] ls2Array=desc.getStringArray();                                       
                    for(LangString2 ls2:ls2Array)
                    {    
                        return ls2.getLanguage();
                        if(rt.getOrCompositeArray()[0].g!=null)
                        {
                            LangstringType langAux=rt[0].getType().getValue().getLangstring();
                            if(langAux!=null)
                            {
                                if((str=langAux.selectAttribute(null,"lang").getDomNode().getNodeValue())!=null)
                                    return str;
                                    else if((str=langAux.selectAttribute(null,"Lang").getDomNode().getNodeValue())!=null)
                                    {
                                        return str;
                                    }
                            }
                        }
                    }
                }
            }
        }
        return str;
    }
    
    private static ScormMetaData[] identifierItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        Collection<ScormMetaData> imArray = new ArrayList<ScormMetaData>();
        //ScormMetaData[] imArray=null;
        String str="identifier";
        //DSpace Element Stored
        im.setElement(str);
        //String[] tempVal=null;
        
        General[] gtArray= lt.getGeneralArray();
        Collection<String> tempVal=new ArrayList<String>();
        //tempVal = new String[gtArray.length];
        if(gtArray!=null)
        {
            int genCounter=0;
            //imArray = new ScormMetaData[gtArray.length];
            for(General g:gtArray)
            {
                Identifier[] iArray=g.getIdentifierArray();
                if(iArray!=null)
                {
                    for(Identifier ident:iArray)
                    {
                        Entry[] eArray=ident.getEntryArray();
                        if(eArray!=null)
                        {
                            for(Entry e:eArray)
                            {
                                tempVal.add(e.getStringValue());
                            }
                        } 
                        if(tempVal.size()==0)
                            tempVal=null;
                        //DSpace Value Stored
                        im.setValues((String[])tempVal.toArray(new String[0]));
                        
                        //DSpace Qualifier Stored
                        int pos =getIdentifierQualifier(g);
                        if(pos!=-1)
                        {
                            im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_IDENTIFIER[pos]);
                        }
                        else
                        {
                            im.setQualifier(null);
                        }
                        //DSpace Lang Stored
                        im.setLang(getIdentifierLang(g,genCounter));                
                        imArray.add(im);
                    }
                }
                genCounter++;
            }
        }                    
       
        return (ScormMetaData[])imArray.toArray(new ScormMetaData[0]);
    }
    
    private static int getIdentifierQualifier(General gt)
    {        
        return -1;
    }
    
    private static String getIdentifierLang(General gt,int pos)
    {
        String[] strArray=gt.getLanguageArray();
        if(strArray!=null)
        {
            if(strArray.length>=pos)
                return strArray[pos];
            if(strArray.length==1)
                return strArray[0];
        }
        return null;
    }
    
    private static ScormMetaData[] typeItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        Collection<ScormMetaData> imArray=new ArrayList<ScormMetaData>();
        String str="type";
        //DSpace Element Stored
        im.setElement(str);
        Collection<String> tempVal=new ArrayList<String>();
        boolean isLOM=true;
        
        Educational[] eduArray= lt.getEducationalArray();
        if(eduArray!=null)
        {
            for(Educational edu:eduArray)
            {
                LearningResourceType[] learnResourceTypeArray=edu.getLearningResourceTypeArray();
                if(learnResourceTypeArray!=null)
                {
                    int learnCounter=0;
                    for(LearningResourceType lr:learnResourceTypeArray)
                    {
                        SourceValue[] svArray=lr.getSourceArray();
                        if(svArray!=null)
                        {
                            String strLom=null;
                            for(int i=0;i<svArray.length;i++)
                            {
                                strLom=svArray[i].getStringValue();
                                if(strLom !=null && strLom.trim().compareTo("")!=0)
                                {
                                    isLOM=strLom.compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_LOM_VERSION)==0;
                                }
                                else
                                {
                                    isLOM=true;
                                }
                                if(isLOM)
                                {
                                    tempVal.add(lr.getValueArray()[i].getStringValue());
                                }         
                            }
                            if(tempVal.size()==0)
                                tempVal=null;
                        }                        
    //                  DSpace Qualifier Entered                   
                        int pos = getTypeQualifier(lr);
                        if(pos==-1)
                            im.setQualifier(null);                    
                        //DSpace Lang Entered
                        im.setLang(getTypeLang(edu,learnCounter));
                        //DSpace Value Entered
                        im.setValues((String[])tempVal.toArray(new String[0]));
                        imArray.add(im);
                    }
                    learnCounter++;
                }
            }
        }
        return (ScormMetaData[])imArray.toArray(new ScormMetaData[0]);
    }
    
    private static int getTypeQualifier(LearningResourceType lrt)
    {        
        return -1;
    }
    
    private static String getTypeLang(Educational edu,int pos)
    {
        String[] strArray=edu.getLanguageArray();
        if(strArray!=null)
        {
            if(strArray.length>=pos)
                return strArray[pos];
            if(strArray.length==1)
                return strArray[0];
        }
        return null;
    }
    
    //Source MISSING... Relation:Requires
    
    private static ScormMetaData[] languageItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        Collection<ScormMetaData> imArray=new ArrayList<ScormMetaData>();
        String str="language";
        //DSpace Element Stored
        im.setElement(str);
        Collection<String> tempVal=new ArrayList<String>();
        
        General[] genArray=lt.getGeneralArray();
        if(genArray!=null)
        {
            for(General gen:genArray)
            {  
                String[] strArray=gen.getLanguageArray();
                if(strArray!=null)
                {
                    int strCounter=0;
                    for(String str1:strArray)
                    {    
                        tempVal.add(str1);
//                      DSpace Lang Stored
                        im.setLang(getLanguageLang(gen,strCounter));
                        strCounter++;
                    }
                    //DSpace Value Stored
                    im.setValues((String[])tempVal.toArray(new String[0]));
                    //DSpace Qualifier Stored
                    int pos =getLanguageQualifier(gen);
                    if(pos!=-1)
                    {
                        im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_LANGUAGE[pos]);
                    }
                    else
                    {
                        im.setQualifier(null);
                    }
                    
                    imArray.add(im);
                }
            }
        }
        return (ScormMetaData[])imArray.toArray(new ScormMetaData[0]);
    }
    
    private static int getLanguageQualifier(General gt)
    {        
        return -1;
    }
    
    private static String getLanguageLang(General gt,int pos)
    {
        String[] strArray=gt.getLanguageArray();
        if(strArray!=null)
        {
            if(strArray.length>=pos)
                return strArray[pos];
            if(strArray.length==1)
                return strArray[0];
        }
        return null;
    }
    
    //Devolve todos os Relations (Relations&Source antigamente definidos)
    private static ScormMetaData[] relationItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        Collection<ScormMetaData> imArray=new ArrayList<ScormMetaData>();
        String str="relation";
        //DSpace Element Stored
        im.setElement(str);
        Collection<String> tempVal=new ArrayList<String>();
        
        Relation[] rtArray= lt.getRelationArray();
        if(rtArray!=null)
        {
            for(Relation rt:rtArray)
            {
                Resource[] rArray=rt.getResourceArray();
                if(rArray!=null)
                {
                    int resCounter=0;
                    for(Resource r:rArray)
                    {
                        Identifier[] iArray=r.getIdentifierArray();
                        if(iArray!=null)
                        {
                            for(Identifier id:iArray)
                            {
                                Entry[] entArray=id.getEntryArray();
                                if(entArray!=null)
                                {
                                    for(Entry entry:entArray)
                                    {
                                        tempVal.add(entry.getStringValue());
                                        
                                    }
                                }
                            }
                        }
//                      DSpace Qualifier Entered
                        int pos = getRelationQualifier(rt,resCounter);
                        if(pos!=-1)
                            im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_RELATION[pos]);
                        else
                        {
                            im.setQualifier(null);
                        }
                        //DSpace Lang Entered
                        im.setLang(getRelationLang(rt,resCounter));
                        resCounter++;
//                      DSpace Value Entered
                        im.setValues((String[])tempVal.toArray(new String[0]));
                        imArray.add(im);
                    }
                }
            }
        }
        return (ScormMetaData[])imArray.toArray(new ScormMetaData[0]);
    }
    
    private static int getRelationQualifier(Relation rt,int pos)
    {        
        Kind[] kt=rt.getKindArray();
        if(kt!=null)
        {
            if(kt.length>=pos)
            {
                for(int i=0;i<ImsManifestReader.DUBLIN_CORE_QUALIFIER_RELATION.length;i++)
                {
                    KindValue[] kvalArray=kt[pos].getValueArray();
                    for(KindValue kval:kvalArray)
                    {
                        if(ImsManifestReader.DUBLIN_CORE_QUALIFIER_RELATION[i].compareToIgnoreCase(kval.getStringValue())==0)
                            return i;
                    }
                    
                }
            }
        }
        return -1;
    }
    
    private static String getRelationLang(Relation rt,int pos)
    {
        String str=null;
        Resource[] rArray = rt.getResourceArray();
        if(rArray!=null&&rArray.length>=pos)
        {
            Description[] descArray=rArray[pos].getDescriptionArray();
            if(descArray!=null)
            {
                for(Description desc:descArray)
                {
                    LangString2[] lstrArray=desc.getStringArray();
                    if(lstrArray!=null)
                    {
                        for(LangString2 lstr:lstrArray)
                        {
                            str=lstr.getLanguage();
                            if(str!=null)
                                return str;
                        }
                    }
                }
            }           
        }
        return str;
    }
    
    //guarda todos o Coverages...
    private static ScormMetaData[] coverageItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        Collection<ScormMetaData> imArray=new ArrayList<ScormMetaData>();
        String str="coverage";
        //DSpace Element Stored
        im.setElement(str);
        Collection<String> tempVal=new ArrayList<String>();
        
        General[] genArray=lt.getGeneralArray();
        if(genArray!=null)
        {
            for(General gen:genArray)
            {
                Coverage[] covArray=gen.getCoverageArray();
                if(covArray!=null)
                {
                    for(Coverage cov:covArray)
                    {
                        LangString2[] lstrArray=cov.getStringArray();
                        if(lstrArray!=null)
                        {                           
                            for(LangString2 lstr:lstrArray)
                            {
                                tempVal.add(lstr.getStringValue());
                            }
//                          DSpace Value Entered
                            im.setValues((String[])tempVal.toArray(new String[0]));
//                          DSpace Qualifier Stored
                            int pos =getCoverageQualifier(cov);
                            if(pos!=-1)
                            {
                                im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_COVERAGE[pos]);
                            }
                            else
                            {
                                im.setQualifier(null);
                            }
//                          DSpace Lang Stored
                            im.setLang(getCoverageLang(cov));
                            imArray.add(im);
                        }
                    }
                }
            }            
        }
        return (ScormMetaData[])imArray.toArray(new ScormMetaData[0]);
    }
    
    
    private static int getCoverageQualifier(Coverage ct)
    {        
        return -1;
    }
    
    private static String getCoverageLang(Coverage ct)
    {
        String str=null;
        LangString2[] lstrArray=ct.getStringArray();
        if(lstrArray!=null)
        {
            for(LangString2 lstr:lstrArray)
            {
                str=lstr.getLanguage();
                if(str!=null)
                    return str;
            }
        }
        return str;
    }
    
    
    private static ScormMetaData[] rightsItemMetaData(Lom lt)
    {
        ScormMetaData im = new ScormMetaData();
        Collection<ScormMetaData> imArray=new ArrayList<ScormMetaData>();
        String str="title";
        //DSpace Element Stored
        im.setElement(str);
        Collection<String> tempVal=new ArrayList<String>();
        boolean isLOM=true;
               
        Rights[] rtArray=lt.getRightsArray();
        if(rtArray!=null)
        {
            for(Rights rt:rtArray)
            {
                CopyrightAndOtherRestrictions[] corvArray= rt.getCopyrightAndOtherRestrictionsArray();
                if(corvArray!=null)
                {
                    for(CopyrightAndOtherRestrictions corv:corvArray)
                    {
                        SourceValue[] svArray =corv.getSourceArray();
                        if(svArray!=null)
                        {
                            for(SourceValue sv:svArray)
                            {
                                str=sv.getStringValue();
                                if(str !=null && str.trim().compareTo("")!=0)
                                {
                                    isLOM=str.compareToIgnoreCase(ImsManifestReader.IMS_MANIFEST_READER_LOM_VERSION)==0;
                                }
                                else
                                {
                                    isLOM=true;
                                }
                                if(isLOM)
                                    tempVal.add(str);                                
                            }
                            //DSpace Values Stored
                            im.setValues((String[])tempVal.toArray(new String[0]));
//                          DSpace Qualifier Stored
                            int pos =getRightsQualifier(rt);
                            if(pos!=-1)
                            {
                                im.setQualifier(ImsManifestReader.DUBLIN_CORE_QUALIFIER_RIGHTS[pos]);
                            }
                            else
                            {
                                im.setQualifier(null);
                            }
//                          DSpace Lang Stored
                            im.setLang(getRightsLang(rt));
                            imArray.add(im);
                        }
                    }
                }
            }                        
        }
        return (ScormMetaData[])imArray.toArray(new ScormMetaData[0]);
    }
    
    
    
    private static int getRightsQualifier(Rights rt)
    {        
        return -1;
    }
    
    private static String getRightsLang(Rights rt)
    {
        String str=null;
        Description[] dtArray=rt.getDescriptionArray();
        if(dtArray!=null)
        {
            for(Description dt:dtArray)
            {
                LangString2[] lstrArray=dt.getStringArray();
                if(lstrArray!=null)
                {
                    for(LangString2 lstr:lstrArray)
                    {
                        str=lstr.getLanguage();
                        if(str!=null)
                            return str;
                    }
                }
            }            
        }
        return str;
    }

}
*/