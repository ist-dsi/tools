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

import pt.linkare.scorm.xmlbeans.ScormAsset;
import pt.linkare.scorm.xmlbeans.ScormData;

/**
 * @author Jos� Pedro Pereira - Linkare TI
 * 
 *         This class represents an abstraction over the file system to enable the use
 *         of related files, metadata associated to files, files containing metadata and
 *         groups of related files...
 */
@SuppressWarnings("serial")
public class FileSet implements Serializable, XMLSerializable {

	/**
	 * Item ID, used when you want to append a file to an existing item.
	 */

	private String itemHandle;

	/**
	 * The related child FileSet's
	 */
	private Collection<FileSet> childSets = new ArrayList<FileSet>(0);

	/**
	 * The metadata associated to the content files, either implicitly inferred or read from the metainfo files
	 */
	private Collection<FileSetMetaData> metaInfo = new ArrayList<FileSetMetaData>(0);

	/**
	 * The references to the content files
	 */
	private Collection<File> contentFiles = new ArrayList<File>(0);

	/**
	 * The references to the metadata files
	 */
	private Collection<File> metaFiles = new ArrayList<File>(0);

	/**
	 * @return Returns the child FileSets.
	 */
	public Collection<FileSet> getChildSets() {
		return childSets;
	}

	/**
	 * @param childSets
	 *            Sets the childs FileSets
	 */
	public void setChildSets(Collection<FileSet> childSets) {
		this.childSets = childSets;
	}

	/**
	 * @param childSet
	 *            adds one FileSet as a child to the childs Collection
	 */
	public void addChildSet(FileSet childSet) {
		if (this.childSets == null) {
			this.childSets = new ArrayList<FileSet>();
		}

		this.childSets.add(childSet);

	}

	/**
	 * @param childSet
	 *            removes the FileSet passed from the childs Collection
	 */
	public void removeChildSet(FileSet childSet) {
		if (this.childSets == null) {
			this.childSets = new ArrayList<FileSet>();
		}

		this.childSets.remove(childSet);
	}

	/**
	 * @return Returns the contentFiles.
	 */
	public Collection<File> getContentFiles() {
		return contentFiles;
	}

	public File getContentFile(int index) {
		return contentFiles.toArray(new File[0])[index];
	}

	/**
	 * @param contentFiles
	 *            The contentFiles to set.
	 */
	public void setContentFiles(Collection<File> contentFiles) {
		this.contentFiles = contentFiles;
	}

	public void addContentFile(File f) {
		if (this.contentFiles == null) {
			this.contentFiles = new ArrayList<File>();
		}

		this.contentFiles.add(f);
	}

	public void addContentFile(Collection<File> f) {
		if (this.contentFiles == null) {
			this.contentFiles = new ArrayList<File>();
		}

		this.contentFiles.addAll(f);
	}

	public void removeContentFile(File f) {
		if (this.contentFiles == null) {
			this.contentFiles = new ArrayList<File>();
		}

		this.contentFiles.remove(f);
	}

	public void removeContentFile(Collection<File> f) {
		if (this.contentFiles == null) {
			this.contentFiles = new ArrayList<File>();
		}

		this.contentFiles.removeAll(f);
	}

	/**
	 * @return Returns the metaFiles.
	 */
	public Collection<File> getMetaFiles() {
		return metaFiles;
	}

	/**
	 * @param metaFiles
	 *            The metaFiles to set.
	 */
	public void setMetaFiles(Collection<File> metaFiles) {
		this.metaFiles = metaFiles;
	}

	public void addMetaFile(File f) {
		if (this.metaFiles == null) {
			this.metaFiles = new ArrayList<File>();
		}

		this.metaFiles.add(f);
	}

	public void addMetaFile(Collection<File> f) {
		if (this.metaFiles == null) {
			this.metaFiles = new ArrayList<File>();
		}

		this.metaFiles.addAll(f);
	}

	public void removeMetaFile(File f) {
		if (this.metaFiles == null) {
			this.metaFiles = new ArrayList<File>();
		}

		this.metaFiles.add(f);
	}

	public void removeMetaFile(Collection<File> f) {
		if (this.metaFiles == null) {
			this.metaFiles = new ArrayList<File>();
		}

		this.metaFiles.removeAll(f);
	}

	public Collection<File> getAllFiles() {
		Collection<File> allFiles =
				new ArrayList<File>((getContentFiles() == null ? 0 : getContentFiles().size())
						+ (getMetaFiles() == null ? 0 : getMetaFiles().size()));
		allFiles.addAll(getContentFiles());
		allFiles.addAll(getMetaFiles());
		return allFiles;
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

		if (metaInfo != null) {
			this.metaInfo.addAll(metaInfo);
		}
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
	public FileSet() {
		super();
	}

	/**
	 * The simplest constructor
	 * 
	 * @param f
	 *            One content file only
	 */
	public FileSet(File f) {
		this.addContentFile(f);
	}

	/**
	 * @param f
	 *            One content file only
	 * @param metaInfo
	 *            One or more FileSetMetaData
	 */
	public FileSet(File f, FileSetMetaData... metaInfo) {
		this.addContentFile(f);
		this.addMetaInfo(Arrays.asList(metaInfo));
	}

	/**
	 * @param f
	 *            One content file only
	 * @param metaInfo
	 *            A collection of FileSetMetaData
	 */
	public FileSet(File f, Collection<FileSetMetaData> metaInfo) {
		this.addContentFile(f);
		this.addMetaInfo(metaInfo);
	}

	/**
	 * @param f
	 *            One content file
	 * @param metaFile
	 *            One metadata file
	 */
	public FileSet(File f, File metaFile) {
		this.addContentFile(f);
		this.addMetaFile(metaFile);
	}

	/**
	 * @param f
	 *            One content file only
	 * @param metaFile
	 *            One meta file only
	 * @param metaInfo
	 *            One or more FileSetMetaData
	 */
	public FileSet(File f, File metaFile, FileSetMetaData... metaInfo) {
		this.addContentFile(f);
		this.addMetaFile(metaFile);
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
	public FileSet(File f, File metaFile, Collection<FileSetMetaData> metaInfo) {
		this.addContentFile(f);
		this.addMetaFile(metaFile);
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
	public FileSet(Collection<File> f, File metaFile, Collection<FileSetMetaData> metaInfo) {
		this.addContentFile(f);
		this.addMetaFile(metaFile);
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
	public FileSet(Collection<File> f, File metaFile, FileSetMetaData... metaInfo) {
		this.addContentFile(f);
		this.addMetaFile(metaFile);
		this.addMetaInfo(Arrays.asList(metaInfo));
	}

	/**
	 * @param f
	 *            A collection of content files
	 * @param metaInfo
	 *            A collection of FileSetMetaData
	 */
	public FileSet(Collection<File> f, Collection<FileSetMetaData> metaInfo) {
		this.addContentFile(f);
		this.addMetaInfo(metaInfo);
	}

	/**
	 * @param f
	 *            A collection of content files
	 * @param metaInfo
	 *            One or more FileSetMetaData
	 */
	public FileSet(Collection<File> f, FileSetMetaData... metaInfo) {
		this.addContentFile(f);
		this.addMetaInfo(Arrays.asList(metaInfo));
	}

	/**
	 * @param f
	 *            A collection of content files
	 */
	public FileSet(Collection<File> f) {
		this.addContentFile(f);
	}

	/**
	 * @param f
	 *            One or more content files
	 */
	public FileSet(File... f) {
		this.addContentFile(Arrays.asList(f));
	}

	public static FileSet createFileSetFromScormData(ScormData scormData) {

		FileSet retVal = new FileSet();
		retVal.addContentFile(scormData.getOriginalFile());
		retVal.addMetaFile(scormData.getMetaDataFile());
		retVal.addMetaInfo(FileSetMetaData.createFileSetMetaDatasFromScormMetaDatas(scormData.getPackageMetaInfo()));

		for (ScormAsset asset : scormData.getAssets()) {
			FileSet assetFileSet = new FileSet();
			assetFileSet.addContentFile(asset.getContentFiles());
			assetFileSet.addMetaFile(asset.getMetadataFile());
			assetFileSet.addMetaInfo(FileSetMetaData.createFileSetMetaDatasFromScormMetaDatas(asset.getContentMetadataInfo()));
		}

		return retVal;
	}

	public void doCleanCopyFromFileSet(FileSet other) {
		this.childSets.clear();
		this.contentFiles.clear();
		this.metaFiles.clear();
		this.metaInfo.clear();

		this.childSets.addAll(other.getChildSets());
		this.contentFiles.addAll(other.getContentFiles());
		this.metaFiles.addAll(other.getMetaFiles());
		this.metaInfo.addAll(other.getMetaInfo());
	}

	public boolean existsFileRecursively(File checkFile) {
		return searchFileSetByFile(checkFile) != null;
	}

	public FileSet searchFileSetByFile(File searchFile) {
		for (File f : getContentFiles()) {
			if (f.equals(searchFile)) {
				return this;
			}
		}
		for (File f : getMetaFiles()) {
			if (f.equals(searchFile)) {
				return this;
			}
		}
		for (FileSet childFileSet : getChildSets()) {
			FileSet foundInChildFileSet = childFileSet.searchFileSetByFile(searchFile);
			if (foundInChildFileSet != null) {
				return foundInChildFileSet;
			}
		}
		return null;
	}

	public FileSet searchFileSetByFile(String originalAbsoluteFilePath) {
		for (File f : getContentFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				return this;
			}
		}
		for (File f : getMetaFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				return this;
			}
		}
		for (FileSet childFileSet : getChildSets()) {
			FileSet foundInChildFileSet = childFileSet.searchFileSetByFile(originalAbsoluteFilePath);
			if (foundInChildFileSet != null) {
				return foundInChildFileSet;
			}
		}
		return null;
	}

	public File searchFileByAbsolutePath(String originalAbsoluteFilePath) {
		for (File f : getContentFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				return f;
			}
		}
		for (File f : getMetaFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				return f;
			}
		}
		for (FileSet childFileSet : getChildSets()) {
			File foundInChildFileSet = childFileSet.searchFileByAbsolutePath(originalAbsoluteFilePath);
			if (foundInChildFileSet != null) {
				return foundInChildFileSet;
			}
		}
		return null;
	}

	public boolean replaceFileWithAbsolutePath(String originalAbsoluteFilePath, File newFile) {
		for (File f : getContentFiles()) {
			if (f.getPath().equals(originalAbsoluteFilePath)) {
				this.getContentFiles().remove(f);
				this.getContentFiles().add(newFile);
				return true;
			}

		}
		for (File f : getMetaFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				this.getMetaFiles().remove(f);
				this.getMetaFiles().add(newFile);
				return true;
			}
		}
		for (FileSet childFileSet : getChildSets()) {
			boolean foundInChildFileSet = childFileSet.replaceFileWithAbsolutePath(originalAbsoluteFilePath, newFile);
			if (foundInChildFileSet) {
				return true;
			}
		}
		return false;
	}

	public boolean replaceFileWithAbsolutePath(String originalAbsoluteFilePath, String replacementAbsoluteFilePath) {
		for (File f : getContentFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				this.getContentFiles().remove(f);
				this.getContentFiles().add(new File(replacementAbsoluteFilePath));
				return true;
			}

		}
		for (File f : getMetaFiles()) {
			if (f.getAbsolutePath().equals(originalAbsoluteFilePath)) {
				this.getMetaFiles().remove(f);
				this.getMetaFiles().add(new File(replacementAbsoluteFilePath));
				return true;
			}
		}
		for (FileSet childFileSet : getChildSets()) {
			boolean foundInChildFileSet =
					childFileSet.replaceFileWithAbsolutePath(originalAbsoluteFilePath, replacementAbsoluteFilePath);
			if (foundInChildFileSet) {
				return true;
			}
		}
		return false;
	}

	public Collection<File> recursiveListContentFiles() {
		ArrayList<File> retVal = new ArrayList<File>(getContentFiles().size());
		retVal.addAll(getContentFiles());
		for (FileSet childFileSet : getChildSets()) {
			retVal.addAll(childFileSet.recursiveListContentFiles());
		}

		return retVal;
	}

	public Collection<File> recursiveListMetaFiles() {
		ArrayList<File> retVal = new ArrayList<File>(getMetaFiles().size());
		retVal.addAll(getMetaFiles());
		for (FileSet childFileSet : getChildSets()) {
			retVal.addAll(childFileSet.recursiveListMetaFiles());
		}

		return retVal;
	}

	public Collection<File> recursiveListAllFiles() {
		Collection<File> allFiles = recursiveListContentFiles();
		allFiles.addAll(recursiveListMetaFiles());
		return allFiles;
	}

	@Override
	public String toXMLString() {

		return toXML().asXML();
	}

	public Element toXML() {
		Element xmlElement = new BaseElement("fileset");
		if (getItemHandle() != null) {
			xmlElement.addElement("itemHandle").addText(getItemHandle());
		}
		if (getChildSets() != null) {
			Element childsElement = xmlElement.addElement("childsets");
			for (FileSet child : getChildSets()) {
				childsElement.add(child.toXML());
			}
		}
		if (getContentFiles() != null) {
			Element contentFileElement = xmlElement.addElement("contentfiles");
			for (File current : getContentFiles()) {
				contentFileElement.addElement("contentfile").setText(current.getAbsolutePath());
			}
		}
		if (getMetaFiles() != null) {
			Element metaFileElement = xmlElement.addElement("metafiles");
			for (File current : getMetaFiles()) {
				metaFileElement.addElement("metafile").setText(current.getAbsolutePath());
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
	public void fromXMLString(String xml) {
		try {
			fromXML(DocumentHelper.parseText(xml).getRootElement());
		} catch (DocumentException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public void fromXML(Element xmlElement) {
		if (xmlElement.element("itemHandle") != null) {
			Element id = xmlElement.element("itemHandle");
			this.setItemHandle(id.getText());
		}
		if (xmlElement.element("childsets") != null) {
			Element childSetsElement = xmlElement.element("childsets");
			List<Element> childSetsList = childSetsElement.elements("fileset");
			for (Element filesetElement : childSetsList) {
				FileSet child = new FileSet();
				child.fromXML(filesetElement);
				this.addChildSet(child);
			}
		}
		if (xmlElement.element("contentfiles") != null) {
			Element contentFilesElement = xmlElement.element("contentfiles");
			List<Element> contentFileList = contentFilesElement.elements("contentfile");
			for (Element contentFileElement : contentFileList) {
				File file = new File(contentFileElement.getText());
				this.addContentFile(file);
			}
		}
		if (xmlElement.element("metafiles") != null) {
			Element metaFilesElement = xmlElement.element("metafiles");
			List<Element> metaFileList = metaFilesElement.elements();
			for (Element metaFileElement : metaFileList) {
				File metaFile = new File(metaFileElement.getText());
				this.addMetaFile(metaFile);
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

	public FileSetDescriptor createBasicFileSetDescriptor() {
		FileSetDescriptor fsDescriptor = new FileSetDescriptor();
		fsDescriptor.addMetaInfo(this.getMetaInfo());
		for (File f : getContentFiles()) {
			fsDescriptor.addContentFileDescriptor(new FileDescriptor(f.getAbsolutePath(), f.getName(), null, null, null, (int) f
					.length(), null));
		}
		for (File f : getMetaFiles()) {
			fsDescriptor.addMetaFileDescriptors(new FileDescriptor(f.getAbsolutePath(), f.getName(), null, null, null, (int) f
					.length(), null));
		}

		return fsDescriptor;
	}

	public FileSetDescriptor recursiveCreateBasicFileSetDescriptor() {
		FileSetDescriptor fsDescriptor = createBasicFileSetDescriptor();
		for (FileSet child : getChildSets()) {
			fsDescriptor.addChildSet(child.recursiveCreateBasicFileSetDescriptor());
		}

		return fsDescriptor;
	}

	public String getItemHandle() {
		return itemHandle;
	}

	public void setItemHandle(String itemHandle) {
		this.itemHandle = itemHandle;
	}

}
