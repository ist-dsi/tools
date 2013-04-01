package pt.utl.ist.fenix.tools.file.dspace;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileSearchCriteria;
import pt.utl.ist.fenix.tools.file.FileSearchResult;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetDescriptor;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.FileSetQueryResults;
import pt.utl.ist.fenix.tools.file.FilesetMetadataQuery;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.rmi.IRemoteFile;
import pt.utl.ist.fenix.tools.file.rmi.IRemoteFileSetManager;
import pt.utl.ist.fenix.tools.file.rmi.RMIConfig;
import pt.utl.ist.fenix.tools.file.rmi.RemoteFileInputStream;
import pt.utl.ist.fenix.tools.file.rmi.RemoteFileOutputStream;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;

/**
 * This is the first RMI Client made for RMI transport conection to the DSpace Server Some properties must be defined in
 * order to enable this Client to be used: 1. dspace.initial.context.properties - The name of the properties file to
 * load the InitialContext JNDI properties from... It should be accessible from
 * ClassLoader.getResouceAsInputStream(path) 5. dspace.use.ssl - Should be 1, yes or true if you want to use ssl... In
 * that case, additional props should be defined 5.1 dspace.ssl.client.keystore - Should be the relative path to the
 * client keystore file 6.
 * 
 * @author jpereira - Linkare TI
 */
public class DSpaceRmiClient implements IDSpaceClient {

    private static Logger logger = Logger.getLogger(DSpaceRmiClient.class.getName());

    private String username = null;

    private String password = null;

    private int bytesStartLength = RMIConfig.DEFAULT_REMOTE_STREAM_BUFFER_MIN_PARAM;

    private int bytesIncreaseLength = RMIConfig.DEFAULT_REMOTE_STREAM_BUFFER_BLOCK_PARAM;

    private int maxBytesLength = RMIConfig.DEFAULT_REMOTE_STREAM_BUFFER_MAX_PARAM;

    private String dspaceManagerJndiName = null;

    /*
     * public FileSetDescriptor uploadFileSet(FileSet fs) throws DSpaceClientException { try { IDSpaceRMIOutputStream
     * outputStream = findRemote().getRemoteFile(username, password); BufferedInputStream bis = new
     * BufferedInputStream(new FileInputStream(file)); int currentBytesRead = 0; int currentBufferSize =
     * bytesStartLength; double timeTakenBeforePerByte = 0.; double timeTakenNowPerByte = 0.; long timeBefore = 0;
     * byte[] data = new byte[maxBytesLength]; while ((currentBytesRead = bis.read(data, 0, currentBufferSize)) != -1) {
     * timeBefore = System.currentTimeMillis(); outputStream.write(data, 0, currentBytesRead); timeTakenNowPerByte =
     * ((double) (System.currentTimeMillis() - timeBefore)) / (double) currentBufferSize; if (timeTakenNowPerByte > 1.10 *
     * timeTakenBeforePerByte) {// If the time per byte is 10% higher then // before - decrease the buffer size until //
     * it reaches the minimum defined currentBufferSize = (currentBufferSize - bytesIncreaseLength < bytesStartLength) ?
     * currentBufferSize : currentBufferSize - bytesIncreaseLength; } else if (timeTakenNowPerByte < 0.90 *
     * timeTakenBeforePerByte) {// If the time per byte is 10% lower // then the timeTakenBeforePerByte - try // to
     * increase the buffer size for best // performance currentBufferSize = (currentBufferSize + bytesIncreaseLength >
     * maxBytesLength) ? currentBufferSize : currentBufferSize + bytesIncreaseLength; } // prepare for next round...
     * timeTakenBeforePerByte = timeTakenNowPerByte; } // ok... all bytes written String uploadedFilePath =
     * outputStream.getUploadedFilePath(); // flush, close and then enable gc (because it is really a remote gc
     * outputStream.flush(); outputStream.close(); outputStream = null; return findRemote().uploadFile(uploadedFilePath,
     * fileUpload, username, password); } catch (RemoteException e) { throw new DSpaceClientException("DSpace Server
     * Communication problem", e); } catch (FileNotFoundException e) { throw new DSpaceClientException("File not found " +
     * file.getAbsolutePath(), e); } catch (IOException e) { throw new DSpaceClientException("Input/Ouput problem
     * reading file: " + file.getAbsolutePath(), e); } }
     */
    /*
     * public void deleteFile(String bitstreamIdentification) throws DSpaceClientException { try {
     * findRemote().deleteFile(bitstreamIdentification, username, password); } catch (Exception e) { throw new
     * DSpaceClientException("Error deleting file " + bitstreamIdentification + " in DSpace", e); } } public void
     * changeFilePermissions(String bitstreamIdentification, boolean privateFile) throws DSpaceClientException { try {
     * findRemote().changeFilePermissions(bitstreamIdentification, privateFile, username, password); } catch (Exception
     * e) { throw new DSpaceClientException("Error changing file permissions on file " + bitstreamIdentification + " in
     * DSpace", e); } }
     */

    public DSpaceRmiClient() throws DSpaceClientException {

    }

    private IRemoteFileSetManager remoteDSpaceServer;

    private IRemoteFileSetManager findRemote() throws DSpaceClientException {

        if (remoteDSpaceServer == null) {
            Object oRemote;
            try {
                oRemote = RMIConfig.getInstance().locateJNDI().lookup(dspaceManagerJndiName);
                remoteDSpaceServer = (IRemoteFileSetManager) PortableRemoteObject.narrow(oRemote, IRemoteFileSetManager.class);
            } catch (NamingException e) {
                throw new DSpaceClientException("Error finding remote RMI endpoint...", e);
            } catch (IOException e) {
                throw new DSpaceClientException("Error finding remote RMI endpoint...", e);
            }
        }
        return remoteDSpaceServer;
    }

    @Override
    public void init(DSpaceFileManager ctx) {
        try {
            username = ctx.getProperty("dspace.username");
            password = ctx.getProperty("dspace.password");
            // bytesStartLength = Integer.parseInt(ctx.getProperty(DSPACE_RMI_STREAM_BYTES_MIN));
            // bytesIncreaseLength = Integer.parseInt(ctx.getProperty(DSPACE_RMI_STREAM_BYTES_BLOCK));
            // maxBytesLength = Integer.parseInt(ctx.getProperty(DSPACE_RMI_STREAM_BYTES_MAX));

            RMIConfig.getInstance().setJndiPropertiesFile(ctx.getProperty(RMIConfig.JNDI_PROPERTIES_FILE_PARAM));
            RMIConfig.getInstance().setUseSSL(Integer.parseInt(ctx.getProperty(RMIConfig.RMI_SSL_PARAM)) != 0);
            RMIConfig.getInstance().setRegistryPortNumber(Integer.parseInt(ctx.getProperty(RMIConfig.RMI_REGISTRY_PORT_PARAM)));
            RMIConfig.getInstance().setDefaultPortNumber(Integer.parseInt(ctx.getProperty(RMIConfig.RMI_SERVER_PORT_PARAM)));
            RMIConfig.getInstance().setSslTrustStore(ctx.getProperty(RMIConfig.RMI_SSL_TRUSTSTORE_PARAM));
            RMIConfig.getInstance().setSslTrustStorePass(ctx.getProperty(RMIConfig.RMI_SSL_TRUSTSTORE_PASS_PARAM));
            RMIConfig.getInstance().initializeSocketFactories();

            dspaceManagerJndiName = ctx.getProperty("dspace.rmi.server.name");
            try {
                bytesStartLength = Integer.parseInt(ctx.getProperty(RMIConfig.REMOTE_STREAM_BUFFER_MIN_PARAM));
                bytesIncreaseLength = Integer.parseInt(ctx.getProperty(RMIConfig.REMOTE_STREAM_BUFFER_BLOCK_PARAM));
                maxBytesLength = Integer.parseInt(ctx.getProperty(RMIConfig.REMOTE_STREAM_BUFFER_MAX_PARAM));

            } catch (Exception e) {
                logger.log(Level.INFO, "Unable to parse buffer block sizes... Assuming defaults!");
            }

        } catch (Exception e) {
            throw new RuntimeException("Unable to read configuration properties for " + getClass().getName(), e);
        }
        try {
            findRemote();
        } catch (DSpaceClientException e) {
            throw new RuntimeException("Error finding remote DSpaceServer implementation...", e);
        }

    }

    @Override
    public FileSetDescriptor uploadFileSet(VirtualPath path, String originalFilename, FileSet fs, boolean privateFile)
            throws DSpaceClientException {
        try {
            String localBaseDir = fs.getContentFile(0).getParent();
            IRemoteFile remoteDir = findRemote().getBaseRemoteDir(username, password);
            long timeStart = System.currentTimeMillis();
            uploadTransferFileSetRecursive(localBaseDir, remoteDir, fs);
            FileSetDescriptor retVal =
                    findRemote().uploadFileSet(remoteDir, path, originalFilename, privateFile, fs, username, password);
            System.out.println("RMI: uploadFileSet took " + (System.currentTimeMillis() - timeStart) + " ms");
            return retVal;
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        } catch (IOException e) {
            throw new DSpaceClientException(e);
        }
    }

    public void uploadTransferFileSetRecursive(String localBaseDir, IRemoteFile remoteDir, FileSet fs) throws IOException,
            RemoteException {
        HashMap<String, String> pathReplacements = new HashMap<String, String>();

        for (File f : fs.getAllFiles()) {
            String relativePath = FileUtils.makeRelativePath(localBaseDir, f.getAbsolutePath());
            remoteDir.createFile(relativePath);
            RemoteFileOutputStream rfos = new RemoteFileOutputStream(remoteDir);
            FileUtils.adaptativeCopyInputStreamToOutputStream(new FileInputStream(f), rfos, bytesStartLength, maxBytesLength,
                    bytesIncreaseLength);
            rfos.close();
            pathReplacements.put(f.getAbsolutePath(), remoteDir.getAbsolutePath());
        }

        for (Entry<String, String> pathReplacement : pathReplacements.entrySet()) {
            fs.replaceFileWithAbsolutePath(pathReplacement.getKey(), pathReplacement.getValue());
        }
        for (FileSet childFs : fs.getChildSets()) {
            uploadTransferFileSetRecursive(localBaseDir, remoteDir, childFs);
        }
    }

    @Override
    public FileSet retrieveFileSet(FileSetDescriptor descriptor) throws DSpaceClientException {
        try {
            IRemoteFile remoteBaseDir = findRemote().retrieveBaseRemoteDir(descriptor, username, password);
            String remoteBaseDirAbsolutePath = remoteBaseDir.getAbsolutePath();
            File localBaseDir = FileUtils.createTemporaryDir("DSpaceTempDownload", ".tmp");
            FileSet remoteFileSet = descriptor.createRecursiveFileSet();
            downloadTransferRecursiveFileSet(localBaseDir, remoteBaseDirAbsolutePath, remoteBaseDir, remoteFileSet);
            return remoteFileSet;
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        } catch (IOException e) {
            throw new DSpaceClientException(e);
        }
    }

    public void downloadTransferRecursiveFileSet(File localBaseDir, String remoteBaseDirAbsolutePath, IRemoteFile remoteBaseDir,
            FileSet remoteFileSet) throws IOException, RemoteException {

        HashMap<String, String> pathReplacements = new HashMap<String, String>();

        for (File f : remoteFileSet.getAllFiles()) {
            String relativePath = FileUtils.makeRelativePath(remoteBaseDirAbsolutePath, f.getAbsolutePath());
            File localFile = new File(localBaseDir, relativePath);
            localFile.getParentFile().mkdirs();
            localFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(localFile);
            remoteBaseDir.getFile(relativePath);
            RemoteFileInputStream rfis = new RemoteFileInputStream(remoteBaseDir);
            FileUtils.adaptativeCopyInputStreamToOutputStream(rfis, fos, bytesStartLength, maxBytesLength, bytesIncreaseLength);
            pathReplacements.put(remoteBaseDir.getAbsolutePath(), localFile.getAbsolutePath());
        }

        for (Entry<String, String> pathReplacement : pathReplacements.entrySet()) {
            remoteFileSet.replaceFileWithAbsolutePath(pathReplacement.getKey(), pathReplacement.getValue());
        }

        for (FileSet childFs : remoteFileSet.getChildSets()) {
            downloadTransferRecursiveFileSet(localBaseDir, remoteBaseDirAbsolutePath, remoteBaseDir, childFs);
        }
    }

    @Override
    public void deleteFileSet(FileSetDescriptor descriptor) throws DSpaceClientException {
        try {
            findRemote().deleteFileSet(descriptor, username, password);
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        }
    }

    @Override
    public void changeFileSetPermissions(FileSetDescriptor descriptor, boolean privateFile) throws DSpaceClientException {
        try {
            findRemote().changeFileSetPermissions(descriptor, privateFile, username, password);
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        }

    }

    @Override
    public FileSetDescriptor listAllDescriptorsFromRoot(FileSetDescriptor rootFileSetDescriptor) throws DSpaceClientException {
        try {
            return findRemote().listRecursiveFromRoot(rootFileSetDescriptor, username, password);
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        }

    }

    @Override
    public FileSetDescriptor getRootDescriptor(FileSetDescriptor innerChildDescriptor) throws DSpaceClientException {
        try {
            return findRemote().getRootDescriptor(innerChildDescriptor, username, password);
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        }
    }

    @Override
    public FileSetQueryResults searchFileSets(FilesetMetadataQuery query, VirtualPath optionalPathToRestrictSearch)
            throws DSpaceClientException {
        try {
            return findRemote().searchFileSets(query, optionalPathToRestrictSearch, username, password);
        } catch (RemoteException e) {
            throw new DSpaceClientException(e);
        }
    }

    @Override
    public FileSearchResult searchFiles(FileSearchCriteria criteria, VirtualPath optionalPathToRestrictSearch)
            throws DSpaceClientException {
        List<FileDescriptor> descriptors = new ArrayList<FileDescriptor>();
        FilesetMetadataQuery query = criteria.getQuery();

        FileSetQueryResults results = searchFileSets(query, optionalPathToRestrictSearch);
        for (FileSetDescriptor descriptor : results.getResults()) {
            descriptors.add(getRootDescriptor(descriptor).getContentFileDescriptor(0));
        }

        return new FileSearchResult(descriptors, query.getStart(), query.getPageSize(), results.getHitsCount());
    }

    @Override
    public FileSearchResult searchFiles(FileSearchCriteria criteria) throws DSpaceClientException {
        return searchFiles(criteria, null);
    }

    @Override
    public FileSetDescriptor addFileToItem(VirtualPath path, String name, String itemId, FileSet fileSet, boolean privateFile)
            throws DSpaceClientException {
        /**
         * Should be implemented
         */
        return null;
    }

    @Override
    public void changeItemMetaData(String itemHandler, Collection<FileSetMetaData> metaData) {
        /**
         * Should be implemented
         */
    }

    @Override
    public void removeFileFromItem(String uniqueId) throws DSpaceClientException {
        /**
         * Should be implemented
         */
    }

    @Override
    public InputStream retrieveStreamForFile(String uniqueIdentifier) throws DSpaceClientException {
        /**
         * Should be implemented
         */
        return null;
    }

}
