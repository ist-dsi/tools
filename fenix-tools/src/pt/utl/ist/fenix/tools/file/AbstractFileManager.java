package pt.utl.ist.fenix.tools.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.linkare.scorm.utils.ScormMetaDataHash;
import pt.linkare.scorm.utils.ScormMetaInfoEnum;
import pt.linkare.scorm.xmlbeans.ImsManifestWriter_1_2;
import pt.linkare.scorm.xmlbeans.ScormData;
import pt.linkare.scorm.xmlbeans.ScormHandlerFactory;
import pt.utl.ist.fenix.tools.file.filters.FileSetFilter;
import pt.utl.ist.fenix.tools.file.filters.FileSetFilterException;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;
import pt.utl.ist.fenix.tools.tree.TreeUtilities;
import pt.utl.ist.fenix.tools.tree.TreeUtilities.TreeRecurseException;
import pt.utl.ist.fenix.tools.util.PropertiesManager;

public abstract class AbstractFileManager implements IFileManager {

	private static Logger logger = Logger.getLogger(AbstractFileManager.class.getName());

	private Properties properties = null;

	public AbstractFileManager() {
		try {
			logger.log(Level.INFO, "Loding properties from file /FileManagerConfiguration.properties");
			properties = PropertiesManager.loadProperties("/FileManagerConfiguration.properties");
		} catch (IOException e) {
			throw new RuntimeException("Unable to read FileManager configuration properties", e);
		}
	}

	public String getProperty(String propKey) {
		return properties.getProperty(propKey);
	}

	/**
	 * This method will rec-descend the FileSet passed in and invoke the method
	 * denoted by m, existing in this class (or extension) for each found
	 * fileset... It keeps track of the path in the tree, to avoid infinite
	 * loops
	 * 
	 * @param fileSet
	 * @param m
	 */
	public void recurseFileSetCallMethod(FileSet fileSet, Method m) {
		try {
			TreeUtilities.createTreeUtilities().recurseTreeCallMethod(fileSet, "getChildSets", this, m);
		} catch (TreeRecurseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This method will rec-descend the FileSetDescriptor passed in and invoke
	 * the method denoted by m, existing in this class (or extension) for each
	 * found filesetdescriptor... It keeps track of the path in the tree, to
	 * avoid infinite loops
	 * 
	 * @param fileSetDescriptor
	 * @param m
	 */
	public void recurseFileSetDescriptorCallMethod(FileSetDescriptor fileSetDescriptor, Method m) {
		try {
			TreeUtilities.createTreeUtilities().recurseTreeCallMethod(fileSetDescriptor, "getChildSets",
					this, m);
		} catch (TreeRecurseException e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#changeFilePermissions(java.lang.String,
	 *      java.lang.Boolean)
	 */
	public void changeFilePermissions(String uniqueId, Boolean privateFile) throws FileManagerException {
		FileSetDescriptor setDescriptor = new FileSetDescriptor();
		FileDescriptor fileDescriptor = new FileDescriptor();
		fileDescriptor.setUniqueId(uniqueId);
		setDescriptor.addContentFileDescriptor(fileDescriptor);
		setDescriptor = listAllDescriptorsFromRoot(setDescriptor);
		if (setDescriptor != null)
			changePermissions(setDescriptor, privateFile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#deleteFile(java.lang.String)
	 */
	public void deleteFile(String uniqueId) throws FileManagerException {
		FileSetDescriptor setDescriptor = new FileSetDescriptor();
		FileDescriptor fileDescriptor = new FileDescriptor();
		fileDescriptor.setUniqueId(uniqueId);
		setDescriptor.addContentFileDescriptor(fileDescriptor);
		setDescriptor = listAllDescriptorsFromRoot(setDescriptor);
		if (setDescriptor != null)
			deleteFileSet(setDescriptor);
	}

	public void formatDownloadUrls(FileSetDescriptor fileSetDescriptor) {
		try {
			recurseFileSetDescriptorCallMethod(fileSetDescriptor, this.getClass().getMethod(
					"formatDownloadUrlAtLevel", new Class[] { FileSetDescriptor.class }));
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}

	public void formatDownloadUrlAtLevel(FileSetDescriptor fileSetDescriptor) throws FileManagerException {
		for (FileDescriptor desc : fileSetDescriptor.getAllFileDescriptors()) {
			desc.setDirectDownloadUrl(formatDownloadUrl(desc.getUniqueId(), desc.getFilename()));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#retrieveFile(java.lang.String)
	 */
	public byte[] retrieveFile(String uniqueId) throws FileManagerException {
		FileDescriptor descriptor = new FileDescriptor();
		descriptor.setUniqueId(uniqueId);
		FileSetDescriptor fileSetDescriptor = new FileSetDescriptor(descriptor);
		FileSet readFileSet = this.readFileSet(this.listAllDescriptorsFromRoot(fileSetDescriptor));
		
		try {
			return FileUtils.readByteArray(readFileSet.getContentFile(0));
		} catch (FileNotFoundException e) {
			throw new FileManagerException(e);
		} catch (IOException e) {
			throw new FileManagerException(e);
		}
	}

	private Collection<FileSetMetaData> createMetaData(String author, String title) {
		Collection<FileSetMetaData> metadata = new ArrayList<FileSetMetaData>();
		metadata.add(FileSetMetaData.createAuthorMeta(author));
		metadata.add(FileSetMetaData.createTitleMeta(title));
		return metadata;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#saveFile(pt.utl.ist.fenix.tools.file.FilePath,
	 *      java.lang.String, boolean, pt.utl.ist.fenix.tools.file.FileMetadata,
	 *      java.io.File)
	 */
	public FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			String author, String title, File fileToSave) {
		Collection<FileSetMetaData> metadata = createMetaData(author, title);
		return saveFile(filePath, originalFilename, privateFile, metadata, fileToSave);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#saveFile(pt.utl.ist.fenix.tools.file.FilePath,
	 *      java.lang.String, boolean, pt.utl.ist.fenix.tools.file.FileMetadata,
	 *      java.io.File)
	 */

	public FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			Collection<FileSetMetaData> fileMetadata, File fileToSave) {
		FileSet fs = new FileSet();
		fs.addMetaInfo(fileMetadata);
		fs.addContentFile(fileToSave);
		FileSetDescriptor fsDescriptor = saveFileSet(filePath, originalFilename, privateFile, fs,
				FileSetType.SIMPLE);
		return fsDescriptor.getContentFileDescriptor(0);
	}

	public FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			Collection<FileSetMetaData> fileMetadata, InputStream fileInputStream) {
		File dirTemp;
		try {
			dirTemp = FileUtils.createTemporaryDir("filemanager_", "_temp_persisted_stream");
			File outFile = new File(dirTemp, originalFilename);
			FileOutputStream fOutStream = new FileOutputStream(outFile);
			FileUtils.copyInputStreamToOutputStream(fileInputStream, fOutStream);
			return saveFile(filePath, originalFilename, privateFile, fileMetadata, outFile);
		} catch (IOException e) {
			throw new RuntimeException("Error occured saving file", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#saveFile(pt.utl.ist.fenix.tools.file.FilePath,
	 *      java.lang.String, boolean, pt.utl.ist.fenix.tools.file.FileMetadata,
	 *      java.io.InputStream)
	 */
	public FileDescriptor saveFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			String author, String title, InputStream fileInputStream) {
		return saveFile(filePath, originalFilename, privateFile, createMetaData(author, title),
				fileInputStream);
	}

	public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			String author, String title, InputStream fileInputStream, FileSetType type) {
		FileSet fs = new FileSet();
		File dirTemp;

		try {
			dirTemp = FileUtils.createTemporaryDir("ScormPackageTmpUpload", "tmp");
			File originalFile = new File(dirTemp, originalFilename);
			FileOutputStream fos = new FileOutputStream(originalFile);
			FileUtils.copyInputStreamToOutputStream(fileInputStream, fos);

			fs.addContentFile(originalFile);
			fs.addMetaInfo(createMetaData(author, title));
			FileSetDescriptor descriptor = saveFileSet(filePath, originalFilename, privateFile, fs, type);
			return getScormFileDescriptor(descriptor.getContentFilesDescriptors());

		} catch (IOException e) {
			throw new FileManagerException(e);
		}

	}

	public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			String author, String title, File fileToSave, FileSetType type) {
		FileSet fs = new FileSet();
		fs.addMetaInfo(createMetaData(author, title));
		fs.addContentFile(fileToSave);
		FileSetDescriptor fsDescriptor = saveFileSet(filePath, originalFilename, privateFile, fs, type);
		return fsDescriptor.getContentFileDescriptor(0);
	}

	public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			String author, String title, File fileToSave, ScormMetaDataHash scormParameters) {
		try {
			FileInputStream inputStream = new FileInputStream(fileToSave);
			return saveScormFile(filePath, originalFilename, privateFile, author, title, inputStream,
					scormParameters);
		} catch (IOException e) {
			throw new FileManagerException(e);
		}
	}

	public FileDescriptor saveScormFile(VirtualPath filePath, String originalFilename, boolean privateFile,
			String author, String title, InputStream inputStream, ScormMetaDataHash scormParameters) {
		FileSetDescriptor descriptor = null;

		try {
			File dirTemp = FileUtils.createTemporaryDir("ScormPackageTmpUpload", "tmp");
			File originalFile = new File(dirTemp, originalFilename);
			FileOutputStream fos = new FileOutputStream(originalFile);
			FileUtils.copyInputStreamToOutputStream(inputStream, fos);
			fos.close();

			Collection<File> originalFileList = Collections.singletonList(originalFile);

			String identifier = "FENIX_MANIFEST_" + UUID.randomUUID().toString();

			ScormData data = ScormHandlerFactory.getScormHandler().createScormPifFile(identifier,
					scormParameters, originalFileList);
			FileSet fileset = FileSet.createFileSetFromScormData(data);
			fileset.getMetaInfo().addAll(createMetaData(author, title));
			descriptor = saveFileSet(filePath, originalFilename, privateFile, fileset,
					FileSetType.UNPACKAGED_SCORM_1_2);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see pt.utl.ist.fenix.tools.file.IFileManager#saveFileSet(pt.utl.ist.fenix.tools.file.VirtualPath,
	 *      java.lang.String, boolean, pt.utl.ist.fenix.tools.file.FileSet,
	 *      pt.utl.ist.fenix.tools.file.FileSetType)
	 */
	public FileSetDescriptor saveFileSet(VirtualPath virtualPath, String originalFileName,
			boolean privateFile, FileSet fileSet, FileSetType fileSetType) {

		if (fileSetType != null) {
			Collection<FileSetFilter> filterChain = fileSetType.getFileSetFilterChain();
			for (FileSetFilter filter : filterChain)
				try {
					filter.handleFileSet(fileSet);
				} catch (FileSetFilterException e) {
					throw new RuntimeException("Unable to run FileSetFilter " + filter.getClass().getName(),
							e);
				}
		}

		return internalSaveFileSet(virtualPath, originalFileName, privateFile, fileSet, fileSetType);
	}

	public abstract FileSetDescriptor internalSaveFileSet(VirtualPath virtualPath, String originalFileName,
			boolean privateFile, FileSet fileSet, FileSetType fileSetType);

	public abstract FileSet readFileSet(FileSetDescriptor fileSetDescriptor);

	public abstract void deleteFileSet(FileSetDescriptor fileSetDescriptor);

	public abstract FileSetDescriptor listAllDescriptorsFromRoot(FileSetDescriptor descriptor);

	public abstract FileSetDescriptor getRootDescriptor(FileSetDescriptor innerChildDescriptor);

	public abstract void changePermissions(FileSetDescriptor descriptor, Boolean isPrivateFile);
}
