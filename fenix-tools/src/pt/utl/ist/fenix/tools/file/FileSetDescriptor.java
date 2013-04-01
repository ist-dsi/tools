package pt.utl.ist.fenix.tools.file;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.BaseElement;

/**
 * @author Jos� Pedro Pereira - Linkare TI
 *         This class represents an abstraction over the file system to enable the use
 *         of related files, metadata associated to files, files containing metadata and groups of related files...
 */

@SuppressWarnings("serial")
public class FileSetDescriptor implements Serializable, XMLSerializable {

    /**
     * The related child FileSet's
     */
    private Collection<FileSetDescriptor> childSets = new ArrayList<FileSetDescriptor>(0);

    /**
     * The metadata associated to the content files, either implicitly inferred or read from the metainfo files
     */
    private Collection<FileSetMetaData> metaInfo = new ArrayList<FileSetMetaData>(0);

    /**
     * The references to the content files
     */
    private Collection<FileDescriptor> contentFilesDescriptors = new ArrayList<FileDescriptor>(0);

    /**
     * The references to the metadata files
     */
    private Collection<FileDescriptor> metaFilesDescriptors = new ArrayList<FileDescriptor>(0);

    /**
     * @return Returns the child FileSets.
     */
    public Collection<FileSetDescriptor> getChildSets() {
        return childSets;
    }

    /**
     * @param childSets
     *            Sets the childs FileSets
     */
    public void setChildSets(Collection<FileSetDescriptor> childSets) {
        this.childSets = childSets;
    }

    /**
     * @param childSet
     *            adds one FileSet as a child to the childs Collection
     */
    public void addChildSet(FileSetDescriptor childSet) {
        if (this.childSets == null) {
            this.childSets = new ArrayList<FileSetDescriptor>();
        }

        this.childSets.add(childSet);

    }

    /**
     * @param childSet
     *            removes the FileSet passed from the childs Collection
     */
    public void removeChildSet(FileSetDescriptor childSet) {
        if (this.childSets == null) {
            this.childSets = new ArrayList<FileSetDescriptor>();
        }

        this.childSets.remove(childSet);
    }

    /**
     * @return Returns the contentFiles.
     */
    public Collection<FileDescriptor> getContentFilesDescriptors() {
        return contentFilesDescriptors;
    }

    public FileDescriptor getContentFileDescriptor(int index) {
        return contentFilesDescriptors.toArray(new FileDescriptor[0])[index];
    }

    /**
     * @param contentFiles
     *            The contentFiles to set.
     */
    public void setContentFilesDescriptors(Collection<FileDescriptor> contentFiles) {
        this.contentFilesDescriptors = contentFiles;
    }

    public void addContentFileDescriptor(FileDescriptor f) {
        if (this.contentFilesDescriptors == null) {
            this.contentFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.contentFilesDescriptors.add(f);
    }

    public void addContentFileDescriptor(Collection<FileDescriptor> f) {
        if (this.contentFilesDescriptors == null) {
            this.contentFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.contentFilesDescriptors.addAll(f);
    }

    public void removeContentFileDescriptor(FileDescriptor f) {
        if (this.contentFilesDescriptors == null) {
            this.contentFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.contentFilesDescriptors.remove(f);
    }

    public void removeContentFileDescriptor(Collection<FileDescriptor> f) {
        if (this.contentFilesDescriptors == null) {
            this.contentFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.contentFilesDescriptors.removeAll(f);
    }

    /**
     * @return Returns the metaFiles.
     */
    public Collection<FileDescriptor> getMetaFilesDescriptors() {
        return metaFilesDescriptors;
    }

    /**
     * @param metaFiles
     *            The metaFiles to set.
     */
    public void setMetaFilesDescriptors(Collection<FileDescriptor> metaFiles) {
        this.metaFilesDescriptors = metaFiles;
    }

    public void addMetaFileDescriptors(FileDescriptor f) {
        if (this.metaFilesDescriptors == null) {
            this.metaFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.metaFilesDescriptors.add(f);
    }

    public void addMetaFileDescriptors(Collection<FileDescriptor> f) {
        if (this.metaFilesDescriptors == null) {
            this.metaFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.metaFilesDescriptors.addAll(f);
    }

    public void removeMetaFileDescriptor(FileDescriptor f) {
        if (this.metaFilesDescriptors == null) {
            this.metaFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.metaFilesDescriptors.add(f);
    }

    public void removeMetaFileDescriptors(Collection<FileDescriptor> f) {
        if (this.metaFilesDescriptors == null) {
            this.metaFilesDescriptors = new ArrayList<FileDescriptor>();
        }

        this.metaFilesDescriptors.removeAll(f);
    }

    public Collection<FileDescriptor> getAllFileDescriptors() {
        ArrayList<FileDescriptor> allDescriptors =
                new ArrayList<FileDescriptor>((getContentFilesDescriptors() != null ? getContentFilesDescriptors().size() : 0)
                        + (getMetaFilesDescriptors() != null ? getMetaFilesDescriptors().size() : 0));
        allDescriptors.addAll(getContentFilesDescriptors());
        allDescriptors.addAll(getMetaFilesDescriptors());
        return allDescriptors;
    }

    /**
     * @return Returns the metaInfo.
     */
    public Collection<FileSetMetaData> getMetaInfo() {
        return metaInfo;
    }

    /**
     * @param metaInfo
     *            The metaInfo to set.
     */
    public void setMetaInfo(Collection<FileSetMetaData> metaInfo) {
        this.metaInfo = metaInfo;
    }

    public void addMetaInfo(FileSetMetaData metaInfo) {
        if (this.metaInfo == null) {
            this.metaInfo = new ArrayList<FileSetMetaData>();
        }

        this.metaInfo.add(metaInfo);
    }

    public void addMetaInfo(Collection<FileSetMetaData> metaInfo) {
        if (this.metaInfo == null) {
            this.metaInfo = new ArrayList<FileSetMetaData>();
        }

        this.metaInfo.addAll(metaInfo);
    }

    public void removeMetaInfo(FileSetMetaData metaInfo) {
        if (this.metaInfo == null) {
            this.metaInfo = new ArrayList<FileSetMetaData>();
        }

        this.metaInfo.remove(metaInfo);
    }

    public void removeMetaInfo(Collection<FileSetMetaData> metaInfo) {
        if (this.metaInfo == null) {
            this.metaInfo = new ArrayList<FileSetMetaData>();
        }

        this.metaInfo.removeAll(metaInfo);
    }

    /**
     * Default Empty constructor required for Serializatio
     */

    public FileSetDescriptor() {
        super();
    }

    /**
     * The simplest constructor
     * 
     * @param f
     *            One content file only
     */
    public FileSetDescriptor(FileDescriptor f) {
        this.addContentFileDescriptor(f);
    }

    /**
     * @param f
     *            One content file only
     * @param metaInfo
     *            One or more FileSetMetaData
     */
    public FileSetDescriptor(FileDescriptor f, FileSetMetaData... metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaInfo(Arrays.asList(metaInfo));
    }

    /**
     * @param f
     *            One content file only
     * @param metaInfo
     *            A collection of FileSetMetaData
     */
    public FileSetDescriptor(FileDescriptor f, Collection<FileSetMetaData> metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaInfo(metaInfo);
    }

    /**
     * @param f
     *            One content file
     * @param metaFile
     *            One metadata file
     */
    public FileSetDescriptor(FileDescriptor f, FileDescriptor metaFile) {
        this.addContentFileDescriptor(f);
        this.addMetaFileDescriptors(metaFile);
    }

    /**
     * @param f
     *            One content file only
     * @param metaFile
     *            One meta file only
     * @param metaInfo
     *            One or more FileSetMetaData
     */
    public FileSetDescriptor(FileDescriptor f, FileDescriptor metaFile, FileSetMetaData... metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaFileDescriptors(metaFile);
        this.addMetaInfo(Arrays.asList(metaInfo));
    }

    /**
     * @param f
     *            One content file only
     * @param metaFile
     *            One meta file only
     * @param metaInfo
     *            A collection of FileSetMetaData
     */
    public FileSetDescriptor(FileDescriptor f, FileDescriptor metaFile, Collection<FileSetMetaData> metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaFileDescriptors(metaFile);
        this.addMetaInfo(metaInfo);
    }

    /**
     * @param f
     *            A collection of content files
     * @param metaFile
     *            Only one metadata file
     * @param metaInfo
     *            A collection of FileSetMetaData
     */
    public FileSetDescriptor(Collection<FileDescriptor> f, FileDescriptor metaFile, Collection<FileSetMetaData> metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaFileDescriptors(metaFile);
        this.addMetaInfo(metaInfo);
    }

    /**
     * @param f
     *            A collection of content files
     * @param metaFile
     *            Only one metadata file
     * @param metaInfo
     *            One or more FileSetMetaData
     */
    public FileSetDescriptor(Collection<FileDescriptor> f, FileDescriptor metaFile, FileSetMetaData... metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaFileDescriptors(metaFile);
        this.addMetaInfo(Arrays.asList(metaInfo));
    }

    /**
     * @param f
     *            A collection of content files
     * @param metaInfo
     *            A collection of FileSetMetaData
     */
    public FileSetDescriptor(Collection<FileDescriptor> f, Collection<FileSetMetaData> metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaInfo(metaInfo);
    }

    /**
     * @param f
     *            A collection of content files
     * @param metaInfo
     *            One or more FileSetMetaData
     */
    public FileSetDescriptor(Collection<FileDescriptor> f, FileSetMetaData... metaInfo) {
        this.addContentFileDescriptor(f);
        this.addMetaInfo(Arrays.asList(metaInfo));
    }

    /**
     * @param f
     *            A collection of content files
     */
    public FileSetDescriptor(Collection<FileDescriptor> f) {
        this.addContentFileDescriptor(f);
    }

    /**
     * @param f
     *            One or more content files
     */
    public FileSetDescriptor(FileDescriptor... f) {
        this.addContentFileDescriptor(Arrays.asList(f));
    }

    public Element toXML() {
        Element xmlElement = new BaseElement("filesetdescriptor");
        if (getChildSets() != null) {
            Element childsElement = xmlElement.addElement("childsets");
            for (FileSetDescriptor childDescriptor : getChildSets()) {
                childsElement.add(childDescriptor.toXML());
            }
        }
        if (getContentFilesDescriptors() != null) {
            Element contentFileDescElement = xmlElement.addElement("contentfiledescriptors");
            for (FileDescriptor current : getContentFilesDescriptors()) {
                contentFileDescElement.add(current.toXML());
            }
        }
        if (getMetaFilesDescriptors() != null) {
            Element metaFileDescElement = xmlElement.addElement("metafiledescriptors");
            for (FileDescriptor current : getMetaFilesDescriptors()) {
                metaFileDescElement.add(current.toXML());
            }
        }
        if (getMetaInfo() != null) {
            Element metaInfoDescElement = xmlElement.addElement("metainfo");
            for (FileSetMetaData current : getMetaInfo()) {
                metaInfoDescElement.add(current.toXML());
            }

        }

        return xmlElement;
    }

    @Override
    public String toXMLString() {
        return toXML().asXML();
    }

    @Override
    public void fromXMLString(String xml) {
        try {
            fromXML(DocumentHelper.parseText(xml).getRootElement());
        } catch (DocumentException e) {
            throw new RuntimeException("Error parsing xml string : " + xml, e);
        }
    }

    @SuppressWarnings("unchecked")
    public void fromXML(Element xmlElement) {
        if (xmlElement.element("childsets") != null) {
            Element childSetsElement = xmlElement.element("childsets");
            List<Element> childSetsList = childSetsElement.elements("filesetdescriptor");
            for (Element filesetDescriptorElement : childSetsList) {
                FileSetDescriptor child = new FileSetDescriptor();
                child.fromXML(filesetDescriptorElement);
                this.addChildSet(child);
            }
        }
        if (xmlElement.element("contentfiledescriptors") != null) {
            Element contentFileDescriptorsElement = xmlElement.element("contentfiledescriptors");
            List<Element> contentFileDescriptorsList = contentFileDescriptorsElement.elements("filedescriptor");
            for (Element fileDescriptorElement : contentFileDescriptorsList) {
                FileDescriptor fileDesc = new FileDescriptor();
                fileDesc.fromXML(fileDescriptorElement);
                this.addContentFileDescriptor(fileDesc);
            }
        }
        if (xmlElement.element("metafiledescriptors") != null) {
            Element metaFileDescriptorsElement = xmlElement.element("metafiledescriptors");
            List<Element> metaFileDescriptorsList = metaFileDescriptorsElement.elements("filedescriptor");
            for (Element metaFileDescriptorElement : metaFileDescriptorsList) {
                FileDescriptor metaFileDescriptor = new FileDescriptor();
                metaFileDescriptor.fromXML(metaFileDescriptorElement);
                this.addMetaFileDescriptors(metaFileDescriptor);
            }
        }
        if (xmlElement.element("metainfo") != null) {
            Element filesetMetaDatasElement = xmlElement.element("metainfo");
            List<Element> filesetMetaDatasList = filesetMetaDatasElement.elements("filesetmetadata");
            for (Element filesetMetaDataElement : filesetMetaDatasList) {
                FileSetMetaData metaData = new FileSetMetaData();
                metaData.fromXML(filesetMetaDataElement);
                this.addMetaInfo(metaData);
            }
        }
    }

    public static FileSetDescriptor createFromXMLString(String xml) {
        FileSetDescriptor retVal = new FileSetDescriptor();
        retVal.fromXMLString(xml);
        return retVal;
    }

    public FileSet createBasicFileSet() {
        FileSet fs = new FileSet();
        fs.addMetaInfo(this.getMetaInfo());
        for (FileDescriptor f : getContentFilesDescriptors()) {
            String originalPath = f.getOriginalAbsoluteFilePath();
            if (originalPath != null) {
                fs.addContentFile(new File(f.getOriginalAbsoluteFilePath()));
            }
        }
        for (FileDescriptor f : getMetaFilesDescriptors()) {
            String originalPath = f.getOriginalAbsoluteFilePath();
            if (originalPath != null) {
                fs.addMetaFile(new File(f.getOriginalAbsoluteFilePath()));
            }
        }
        return fs;
    }

    public FileSet createRecursiveFileSet() {
        FileSet baseFileSet = createBasicFileSet();
        for (FileSetDescriptor child : getChildSets()) {
            baseFileSet.addChildSet(child.createRecursiveFileSet());
        }

        return baseFileSet;
    }

    public Collection<FileDescriptor> recursiveListContentFilesDescriptors() {
        ArrayList<FileDescriptor> retVal = new ArrayList<FileDescriptor>(getContentFilesDescriptors().size());
        retVal.addAll(getContentFilesDescriptors());
        for (FileSetDescriptor childFileSetDescriptor : getChildSets()) {
            retVal.addAll(childFileSetDescriptor.recursiveListContentFilesDescriptors());
        }

        return retVal;
    }

    public Collection<FileDescriptor> recursiveListMetaFilesDescriptors() {
        ArrayList<FileDescriptor> retVal = new ArrayList<FileDescriptor>(getMetaFilesDescriptors().size());
        retVal.addAll(getMetaFilesDescriptors());
        for (FileSetDescriptor childFileSet : getChildSets()) {
            retVal.addAll(childFileSet.recursiveListMetaFilesDescriptors());
        }

        return retVal;
    }

    public Collection<FileDescriptor> recursiveListAllFileDescriptors() {
        Collection<FileDescriptor> allFilesDescriptors = recursiveListContentFilesDescriptors();
        allFilesDescriptors.addAll(recursiveListMetaFilesDescriptors());
        return allFilesDescriptors;
    }

    public void doCleanCopyFromFileSetDescriptor(FileSetDescriptor other) {
        this.childSets.clear();
        this.contentFilesDescriptors.clear();
        this.metaFilesDescriptors.clear();
        this.metaInfo.clear();

        this.childSets.addAll(other.getChildSets());
        this.contentFilesDescriptors.addAll(other.getContentFilesDescriptors());
        this.metaFilesDescriptors.addAll(other.getMetaFilesDescriptors());
        this.metaInfo.addAll(other.getMetaInfo());
    }

    public boolean existsFileRecursively(String originalAbsoluteFilePath) {
        return searchFileSetDescriptorByFile(originalAbsoluteFilePath) != null;
    }

    public FileSetDescriptor searchFileSetDescriptorByFile(String originalAbsoluteFilePath) {
        for (FileDescriptor f : getContentFilesDescriptors()) {
            if (f.getOriginalAbsoluteFilePath().equals(originalAbsoluteFilePath)) {
                return this;
            }
        }
        for (FileDescriptor f : getMetaFilesDescriptors()) {
            if (f.getOriginalAbsoluteFilePath().equals(originalAbsoluteFilePath)) {
                return this;
            }
        }
        for (FileSetDescriptor childFileSet : getChildSets()) {
            FileSetDescriptor foundInChildFileSet = childFileSet.searchFileSetDescriptorByFile(originalAbsoluteFilePath);
            if (foundInChildFileSet != null) {
                return foundInChildFileSet;
            }
        }
        return null;
    }

    public FileDescriptor getContentFileDescriptorWithName(String name) {
        for (FileDescriptor descriptor : getContentFilesDescriptors()) {
            if (descriptor.getFilename().equals(name)) {
                return descriptor;
            }
        }
        return null;
    }

}
