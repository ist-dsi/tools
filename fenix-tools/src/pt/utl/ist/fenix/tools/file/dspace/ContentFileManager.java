package pt.utl.ist.fenix.tools.file.dspace;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Properties;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.IContentFileManager;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;
import pt.utl.ist.fenix.tools.util.StringNormalizer;

public class ContentFileManager extends DSpaceFileManager implements IContentFileManager {

	public ContentFileManager() {
		super();
	}

	public ContentFileManager(final Properties properties) {
		super(properties);
	}

	@Override
	public FileDescriptor addFileToItem(VirtualPath path, String name, String itemId, boolean privateFile,
			InputStream fileInputStream) {
		File dirTemp;
		name = StringNormalizer.normalize(name);
		try {
			dirTemp = FileUtils.createTemporaryDir("filemanager_", "_temp_persisted_stream");
			File outFile = new File(dirTemp, name);
			FileOutputStream fOutStream = new FileOutputStream(outFile);
			FileUtils.copyInputStreamToOutputStream(fileInputStream, fOutStream);
			FileSet fs = new FileSet();
			fs.addContentFile(outFile);
			fs.setItemHandle(itemId);
			FileDescriptor descriptor = addFileToItem(path, name, itemId, privateFile, fs).getContentFileDescriptorWithName(name);
			FileUtils.deleteDirectory(dirTemp);
			return descriptor;

		} catch (IOException e) {
			throw new RuntimeException("Error occured saving file", e);
		}
	}

	@Override
	public void removeFileFromItem(String uniqueId) {
		try {
			dspaceClient.removeFileFromItem(uniqueId);
		} catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(), e);
		}

	}

	@Override
	public void changeItemMetaData(String itemHandler, Collection<FileSetMetaData> metaData) {
		try {
			dspaceClient.changeItemMetaData(itemHandler, metaData);
		} catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(), e);
		}
	}

	private FileSetDescriptor addFileToItem(VirtualPath path, String name, String itemId, boolean privateFile, FileSet fileSet) {
		try {
			return dspaceClient.addFileToItem(path, name, itemId, fileSet, privateFile);
		} catch (DSpaceClientException e) {
			throw new FileManagerException(e.getMessage(), e);
		}
	}

	@Override
	public FileDescriptor createItemWithFile(VirtualPath path, String name, boolean privateFile,
			Collection<FileSetMetaData> metaData, InputStream inputStream) {
		return super.saveFile(path, name, privateFile, metaData, inputStream);
	}

	@Override
	public FileDescriptor createItemWithFile(VirtualPath path, String name, boolean privateFile, String author, String title,
			InputStream inputStream) {
		return super.saveFile(path, name, privateFile, createMetaData(author, title), inputStream);
	}
}
