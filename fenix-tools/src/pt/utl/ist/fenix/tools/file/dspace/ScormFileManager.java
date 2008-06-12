package pt.utl.ist.fenix.tools.file.dspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.UUID;

import pt.linkare.scorm.utils.ScormMetaDataHash;
import pt.linkare.scorm.xmlbeans.ScormData;
import pt.linkare.scorm.xmlbeans.ScormHandlerFactory;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.FileSetType;
import pt.utl.ist.fenix.tools.file.IScormFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class ScormFileManager extends DSpaceFileManager implements IScormFileManager {

    public ScormFileManager() {
	super();
    }

    public ScormFileManager(Properties properties) {
	super(properties);
    }

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
	    String title, InputStream fileInputStream, FileSetType type) {
	return saveScormFile(filePath, originalFilename, privateFile, createMetaData(author, title), fileInputStream, type);

    }

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
	    Collection<FileSetMetaData> metaData, InputStream fileInputStream, FileSetType type) {

	FileSet fs = new FileSet();
	File dirTemp;
	originalFilename = StringNormalizer.normalize(originalFilename);
	try {
	    dirTemp = FileUtils.createTemporaryDir("ScormPackageTmpUpload", "tmp");
	    File originalFile = new File(dirTemp, originalFilename);
	    FileOutputStream fos = new FileOutputStream(originalFile);
	    FileUtils.copyInputStreamToOutputStream(fileInputStream, fos);

	    fs.addContentFile(originalFile);
	    fs.addMetaInfo(metaData);
	    FileSetDescriptor descriptor = saveFileSet(filePath, originalFilename, privateFile, fs, type);
	    return getScormFileDescriptor(descriptor.getContentFilesDescriptors());

	} catch (IOException e) {
	    throw new FileManagerException(e);
	}

    }

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
	    String title, File fileToSave, FileSetType type) {
	FileSet fs = new FileSet();
	fs.addMetaInfo(createMetaData(author, title));
	fs.addContentFile(fileToSave);
	FileSetDescriptor fsDescriptor = saveFileSet(filePath, originalFilename, privateFile, fs, type);
	return fsDescriptor.getContentFileDescriptor(0);
    }

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
	    String title, File fileToSave, ScormMetaDataHash scormParameters) {
	try {
	    FileInputStream inputStream = new FileInputStream(fileToSave);
	    return saveScormFile(filePath, originalFilename, privateFile, author, title, inputStream, scormParameters);
	} catch (IOException e) {
	    throw new FileManagerException(e);
	}
    }

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile, String author,
	    String title, InputStream inputStream, ScormMetaDataHash scormParameters) {
	return saveScormFile(filePath, originalFilename, privateFile, createMetaData(author, title), inputStream, scormParameters);
    }

    public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
	    Collection<FileSetMetaData> metaData, InputStream fileInputStream, ScormMetaDataHash scormParameters) {
	FileSetDescriptor descriptor = null;
	originalFilename = StringNormalizer.normalize(originalFilename);

	try {
	    File dirTemp = FileUtils.createTemporaryDir("ScormPackageTmpUpload", "tmp");
	    File originalFile = new File(dirTemp, originalFilename);
	    FileOutputStream fos = new FileOutputStream(originalFile);
	    FileUtils.copyInputStreamToOutputStream(fileInputStream, fos);
	    fos.close();

	    Collection<File> originalFileList = Collections.singletonList(originalFile);

	    String identifier = "FENIX_MANIFEST_" + UUID.randomUUID().toString();

	    ScormData data = ScormHandlerFactory.getScormHandler().createScormPifFile(identifier, scormParameters,
		    originalFileList);
	    FileSet fileset = FileSet.createFileSetFromScormData(data);
	    fileset.getMetaInfo().addAll(metaData);
	    descriptor = saveFileSet(filePath, originalFilename, privateFile, fileset, FileSetType.UNPACKAGED_SCORM_1_2);

	} catch (Exception e) {
	    throw new FileManagerException(e);
	}

	return getScormFileDescriptor(descriptor.getContentFilesDescriptors());

    }

    private FileDescriptor getScormFileDescriptor(Collection<FileDescriptor> contentFilesDescriptors) {
	for (FileDescriptor descriptor : contentFilesDescriptors) {
	    if (descriptor.getFilename().contains(".zip")) {
		return descriptor;
	    }
	}
	return (FileDescriptor) contentFilesDescriptors.toArray()[0];

    }

}
