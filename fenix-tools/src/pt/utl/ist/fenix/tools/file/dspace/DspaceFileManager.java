package pt.utl.ist.fenix.tools.file.dspace;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.dspace.external.interfaces.remoteManager.objects.FileUpload;
import org.dspace.external.interfaces.remoteManager.objects.ItemMetadata;
import org.dspace.external.interfaces.remoteManager.objects.Path;
import org.dspace.external.interfaces.remoteManager.objects.PathComponent;
import org.dspace.external.interfaces.remoteManager.objects.UploadedFileDescriptor;

import pt.utl.ist.fenix.tools.file.AbstractFileManager;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileMetadata;
import pt.utl.ist.fenix.tools.file.FilePath;
import pt.utl.ist.fenix.tools.file.Node;
import pt.utl.ist.fenix.tools.util.PropertiesManager;

public class DspaceFileManager extends AbstractFileManager {

    private static final DspaceFileManager instance = new DspaceFileManager();

    private static final String downloadUrlFormat;

    private static final DspaceClient dspaceClient;

    static {
        try {
            final Properties properties = PropertiesManager
                    .loadProperties("/FileManagerConfiguration.properties");
            final String serverUrl = properties.getProperty("dspace.serverUrl");
            dspaceClient = new DspaceClient(serverUrl, properties.getProperty("dspace.username"),
                    properties.getProperty("dspace.password"));
            downloadUrlFormat = serverUrl + "/" + properties.getProperty("dspace.downloadUriFormat");
        } catch (IOException e) {
            throw new RuntimeException("Error loading dspace file manager");
        }
    }

    public static final DspaceFileManager getInstance() {
        return instance;
    }

    public FileDescriptor saveFile(FilePath filePath, String originalFilename, boolean privateFile,
            FileMetadata fileMetadata, File file) {

        final Path path = buildPath(filePath);
        final ItemMetadata itemMetadata = new ItemMetadata(fileMetadata.getTitle(), fileMetadata
                .getAuthor());

        try {
            final FileUpload fileUpload = new FileUpload(path, originalFilename, privateFile,
                    itemMetadata);
            final UploadedFileDescriptor uploadedFileDescriptor = dspaceClient.uploadFile(fileUpload,
                    file);

            return new FileDescriptor(uploadedFileDescriptor.getFilename(), uploadedFileDescriptor
                    .getMimeType(), uploadedFileDescriptor.getChecksum(), uploadedFileDescriptor
                    .getChecksumAlgorithm(), uploadedFileDescriptor.getSize(), uploadedFileDescriptor
                    .getBitstreamIdentification());

        } catch (DspaceClientException e) {
            throw new FileManagerException("error.filemanager.unableToStoreFile", e, originalFilename);
        }
    }

    public FileDescriptor saveFile(FilePath filePath, String originalFilename, boolean privateFile,
            FileMetadata fileMetadata, InputStream fileInputStream) {
        File temporaryFile = null;
        try {
            temporaryFile = copyToTemporaryFile(fileInputStream);

            return saveFile(filePath, originalFilename, privateFile, fileMetadata, temporaryFile);
        } catch (IOException e) {
            throw new FileManagerException("error.filemanager.unableToStoreFile", e, originalFilename);
        } finally {
            if (temporaryFile != null) {
                temporaryFile.delete();
            }
        }

    }

    private Path buildPath(FilePath filePath) {
        Path path = new Path();
        for (Node node : filePath.getNodes()) {
            path.addPathComponent(new PathComponent(node.getName(), node.getDescription()));
        }
        return path;
    }

    public void deleteFile(String uniqueId) {
        try {
            dspaceClient.deleteFile(uniqueId);
        } catch (DspaceClientException e) {
            throw new FileManagerException("error.filemanager.unableDeleteFile", e);
        }

    }

    public void changeFilePermissions(String uniqueId, Boolean privateFile) {
        try {
            dspaceClient.changeFilePermissions(uniqueId, privateFile);
        } catch (DspaceClientException e) {
            throw new FileManagerException("error.filemanager.unableToChangeFilePermissions", e);
        }

    }

    public String getDirectDownloadUrlFormat() {
        return downloadUrlFormat;
    }

    public byte[] retrieveFile(String uniqueId) {
        throw new UnsupportedOperationException(
                "Dspace file manager does not support file retrieve. Use direct download url instead");

    }

}
