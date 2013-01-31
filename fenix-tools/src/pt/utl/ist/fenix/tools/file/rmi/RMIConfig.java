package pt.utl.ist.fenix.tools.file.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.AlreadyBoundException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.RMISocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

import pt.utl.ist.fenix.tools.file.utils.FileUtils;

public class RMIConfig {

	/**
	 * Servlet init parameter name The parameter that identifies whether this Servlet should start an RMI Registry or
	 * not
	 */
	public final static String START_RMI_REGISTRY_PARAM = "start.rmi.registry";

	/**
	 * Servlet init parameter name The parameter that identifies the port to use to start the RMI Registry if needed
	 */
	public final static String RMI_REGISTRY_PORT_PARAM = "rmi.registry.port";

	/**
	 * Servlet init parameter name The parameter that identifies the port to use to start the RMI server
	 */
	public static final String RMI_SERVER_PORT_PARAM = "rmi.port";

	/**
	 * Servlet init parameter name The parameter that identifies wether to start RMI Server over SSL or not
	 */
	public final static String RMI_SSL_PARAM = "rmi.ssl";

	/**
	 * Servlet init parameter name The parameter that identifies the location of the SSL_KEYSTORE to use in case of SSL
	 * support
	 */
	public final static String RMI_SSL_KEYSTORE_PARAM = "rmi.ssl.keystore";

	/**
	 * Servlet init parameter name The parameter that identifies the password for the SSL_KEYSTORE if in use support
	 */
	public final static String RMI_SSL_KEYSTORE_PASS_PARAM = "rmi.ssl.keystore.password";

	/**
	 * Servlet init parameter name The parameter that identifies the location of the SSL_KEYSTORE to use in case of SSL
	 * support
	 */
	public final static String RMI_SSL_TRUSTSTORE_PARAM = "rmi.ssl.truststore";

	/**
	 * Servlet init parameter name The parameter that identifies the password for the SSL_KEYSTORE if in use support
	 */
	public final static String RMI_SSL_TRUSTSTORE_PASS_PARAM = "rmi.ssl.truststore.password";

	/**
	 * Servlet init parameter name The parameter that identifies the jndi properties file to use on JNDI lookup and bind
	 */
	public final static String JNDI_PROPERTIES_FILE_PARAM = "jndi.properties.file";

	/**
	 * Servlet init parameter name The parameter that configures the maximum idle timeout for remote streams -
	 * milliseconds
	 */
	public static final String REMOTE_STREAMS_IDLE_TIMEOUT = "remote.streams.idle.timeout";

	/**
	 * Servlet init parameter name The parameter that configures the interval between successive checks of idle streams -
	 * milliseconds
	 */
	public static final String REMOTE_STREAMS_IDLE_CHECK_INTERVAL = "remote.streams.idle.check.interval";

	public static final String REMOTE_STREAM_BUFFER_MIN_PARAM = "rmi.stream.bytes.min";

	public static final String REMOTE_STREAM_BUFFER_MAX_PARAM = "rmi.stream.bytes.max";

	public static final String REMOTE_STREAM_BUFFER_BLOCK_PARAM = "rmi.stream.bytes.block";

	/**
	 * By default the servlet will start an rmi registry
	 */
	public final static boolean DEFAULT_START_RMI_REGISTRY_PARAM = true;

	/**
	 * By default the rmi registry will start on this port
	 */
	public final static int DEFAULT_RMI_REGISTRY_PORT_PARAM = 1091;

	/**
	 * By default the rmi servers will be started on this port
	 */
	public final static int DEFAULT_RMI_PORT_PARAM = 0;

	/**
	 * By default the rmi support will start with ssl support
	 */
	public final static boolean DEFAULT_RMI_SSL_PARAM = true;

	/**
	 * By default the rmi will use this keystore location
	 */
	public final static String DEFAULT_RMI_SSL_KEYSTORE_PARAM = "DSpaceRMIServer.keystore";

	/**
	 * By default the rmi will use this keystore passsword
	 */
	public final static String DEFAULT_RMI_SSL_KEYSTORE_PASS_PARAM = "dspace";

	/**
	 * By default the rmi will use this keystore location
	 */
	public final static String DEFAULT_RMI_SSL_TRUSTSTORE_PARAM = "DSpaceRMIClient.truststore";

	/**
	 * By default the rmi will use this keystore passsword
	 */
	public final static String DEFAULT_RMI_SSL_TRUSTSTORE_PASS_PARAM = "dspace";

	/**
	 * By default the jndi code will use this file from the classpath
	 */
	public final static String DEFAULT_JNDI_PROPERTIES_FILE_PARAM = "classpath://dspace.rmi.location.properties";

	public static final int DEFAULT_REMOTE_STREAM_BUFFER_MIN_PARAM = 1024;

	public static final int DEFAULT_REMOTE_STREAM_BUFFER_MAX_PARAM = 8192;

	public static final int DEFAULT_REMOTE_STREAM_BUFFER_BLOCK_PARAM = 512;

	// hide the constructor to enable the singleton pattern
	private RMIConfig() {

	}

	// keep a private reference to the one object
	private static RMIConfig instance = new RMIConfig();

	private RMIServerSocketFactory serverSocketFactory;

	private RMIClientSocketFactory clientSocketFactory;

	private int defaultPortNumber = DEFAULT_RMI_PORT_PARAM;

	private int registryPortNumber = DEFAULT_RMI_REGISTRY_PORT_PARAM;

	private boolean useSSL = DEFAULT_RMI_SSL_PARAM;

	private String sslKeyStore = DEFAULT_RMI_SSL_KEYSTORE_PARAM;

	private String sslKeyStorePass = DEFAULT_RMI_SSL_KEYSTORE_PASS_PARAM;

	private String sslTrustStore = DEFAULT_RMI_SSL_TRUSTSTORE_PARAM;

	private String sslTrustStorePass = DEFAULT_RMI_SSL_TRUSTSTORE_PASS_PARAM;

	private String jndiPropertiesFile = DEFAULT_JNDI_PROPERTIES_FILE_PARAM;

	private boolean startRMIRegistry = DEFAULT_START_RMI_REGISTRY_PARAM;

	/**
	 * @return the startRMIRegistry
	 */
	public boolean isStartRMIRegistry() {
		return startRMIRegistry;
	}

	/**
	 * @param startRMIRegistry
	 *            the startRMIRegistry to set
	 */
	public void setStartRMIRegistry(boolean startRMIRegistry) {
		this.startRMIRegistry = startRMIRegistry;
	}

	/**
	 * @return the jndiPropertiesFile
	 */
	public String getJndiPropertiesFile() {
		return jndiPropertiesFile;
	}

	/**
	 * @param jndiPropertiesFile
	 *            the jndiPropertiesFile to set
	 */
	public void setJndiPropertiesFile(String jndiPropertiesFile) {
		this.jndiPropertiesFile = jndiPropertiesFile;
	}

	public static RMIConfig getInstance() {
		return instance;
	}

	/**
	 * @return Returns the clientSocketFactory.
	 */
	public RMIClientSocketFactory getClientSocketFactory() {
		initializeSocketFactories();
		return clientSocketFactory;
	}

	/**
	 * @param clientSocketFactory
	 *            The clientSocketFactory to set.
	 */
	public void setClientSocketFactory(RMIClientSocketFactory clientSocketFactory) {
		this.clientSocketFactory = clientSocketFactory;
	}

	/**
	 * @return Returns the defaultPortNumber.
	 */
	public int getDefaultPortNumber() {
		return defaultPortNumber;
	}

	/**
	 * @param defaultPortNumber
	 *            The defaultPortNumber to set.
	 */
	public void setDefaultPortNumber(int defaultPortNumber) {
		this.defaultPortNumber = defaultPortNumber;
	}

	/**
	 * @return Returns the serverSocketFactory.
	 */
	public RMIServerSocketFactory getServerSocketFactory() {
		initializeSocketFactories();
		return serverSocketFactory;
	}

	/**
	 * @param serverSocketFactory
	 *            The serverSocketFactory to set.
	 */
	public void setServerSocketFactory(RMIServerSocketFactory serverSocketFactory) {
		this.serverSocketFactory = serverSocketFactory;
	}

	/**
	 * @return Returns the registryPortNumber.
	 */
	public int getRegistryPortNumber() {
		return registryPortNumber;
	}

	/**
	 * @param registryPortNumber
	 *            The registryPortNumber to set.
	 */
	public void setRegistryPortNumber(int registryPortNumber) {
		this.registryPortNumber = registryPortNumber;
	}

	/**
	 * @return Returns the useSSL.
	 */
	public boolean isUseSSL() {
		return useSSL;
	}

	/**
	 * @param useSSL
	 *            The useSSL to set.
	 */
	public void setUseSSL(boolean useSSL) {
		this.useSSL = useSSL;
	}

	public Remote exportObject(Remote remoteObject) throws RemoteException {
		return UnicastRemoteObject.exportObject(remoteObject, defaultPortNumber, getClientSocketFactory(),
				getServerSocketFactory());
	}

	public void bindObject(String name, Remote object) throws AlreadyBoundException, NamingException, IOException {
		if (name != null && name.trim().length() >= 0) {
			/*if(registry!=null)
			{
				System.out.println("RMI Registry Rebind object "+object+" as "+name);
				registry.rebind(name, object);
			}
			else
			{*/
			Context ctx = locateJNDI();
			ctx.rebind(name, object);
			/*}*/
		}

	}

	public Remote exportAndBindRemoteObject(String name, Remote object) throws AlreadyBoundException, NamingException,
			IOException {
		Remote retVal = exportObject(object);
		bindObject(name, retVal);
		return retVal;
	}

	public void unExportAndUnBindRemoteObject(String name, Remote object) throws AlreadyBoundException, NamingException,
			IOException, NotBoundException {
		unExportObject(object);
		if (name != null && name.trim().length() > 0) {
			unBindObject(name);
		}
	}

	public void unBindObject(String name) throws NotBoundException, NamingException, IOException {
		if (name.trim().length() >= 0) {
			if (registry != null) {
				registry.unbind(name);
			} else {
				locateJNDI().unbind(name);
			}
		}
	}

	public boolean isValidKeyStore(String sslKeyStore) {

		if (FileUtils.locateFilePath(sslKeyStore) != null) {
			return true;
		}

		return false;
	}

	public static SocketFactory loadClientSSLSocketFactory(String trustStoreLocation, String trustStorePassword)
			throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException,
			UnrecoverableKeyException, KeyManagementException {
		javax.net.ssl.KeyManagerFactory kmf;
		KeyStore ks;
		javax.net.ssl.TrustManagerFactory tmf;
		kmf = javax.net.ssl.KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(trustStoreLocation), trustStorePassword.toCharArray());
		kmf.init(ks, trustStorePassword.toCharArray());
		tmf = javax.net.ssl.TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		javax.net.ssl.SSLContext sslc = javax.net.ssl.SSLContext.getInstance("SSL");
		sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		return sslc.getSocketFactory();
	}

	public static ServerSocketFactory loadServerSSLSocketFactory(String keyStoreLocation, String keyStorePassword)
			throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException,
			UnrecoverableKeyException, KeyManagementException {
		javax.net.ssl.KeyManagerFactory kmf;
		KeyStore ks;
		javax.net.ssl.TrustManagerFactory tmf;
		kmf = javax.net.ssl.KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(new FileInputStream(keyStoreLocation), keyStorePassword.toCharArray());
		kmf.init(ks, keyStorePassword.toCharArray());
		tmf = javax.net.ssl.TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(ks);
		javax.net.ssl.SSLContext sslc = javax.net.ssl.SSLContext.getInstance("SSL");
		sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
		return sslc.getServerSocketFactory();
	}

	public void initializeSocketFactories() {
		if (serverSocketFactory != null && clientSocketFactory != null) {
			return;
		}

		if (useSSL) {
			serverSocketFactory = new SslRmiServerSocketFactory();
			clientSocketFactory = new SslRmiClientSocketFactory();
		} else {
			serverSocketFactory = RMISocketFactory.getDefaultSocketFactory();
			clientSocketFactory = RMISocketFactory.getDefaultSocketFactory();
		}
	}

	/**
	 * @return the sslKeyStore
	 */
	public String getSslKeyStore() {
		return sslKeyStore;
	}

	/**
	 * @param sslKeyStore
	 *            the sslKeyStore to set
	 */
	public void setSslKeyStore(String sslKeyStore) {
		this.sslKeyStore = sslKeyStore;
	}

	/**
	 * @return the sslKeyStorePass
	 */
	public String getSslKeyStorePass() {
		return sslKeyStorePass;
	}

	/**
	 * @param sslKeyStorePass
	 *            the sslKeyStorePass to set
	 */
	public void setSslKeyStorePass(String sslKeyStorePass) {
		this.sslKeyStorePass = sslKeyStorePass;
	}

	private Registry registry = null;

	public void initializeRegistry() throws RemoteException {
		if (startRMIRegistry && registry == null) {
			registry = LocateRegistry.createRegistry(registryPortNumber);
		}
	}

	private InitialContext ctx = null;

	public Context locateJNDI() throws NamingException, IOException {
		if (ctx == null) {
			ctx = new InitialContext(readJNDIProperties());
		}
		return ctx;
	}

	private Properties jndiProps = null;

	private Properties readJNDIProperties() throws IOException {
		if (jndiProps == null) {
			jndiProps = new Properties();
			InputStream is = null;
			if (jndiPropertiesFile.startsWith("classpath://")) {
				jndiPropertiesFile = jndiPropertiesFile.substring("classpath://".length());
				is = getClass().getClassLoader().getResourceAsStream(jndiPropertiesFile);
			} else if (jndiPropertiesFile.indexOf("://") != -1) {
				URL locationJNDIProps = new URL(jndiPropertiesFile);
				is = locationJNDIProps.openConnection().getInputStream();
			} else {
				File f = new File(jndiPropertiesFile);
				is = new FileInputStream(f);
			}
			jndiProps.load(is);
			is.close();
			if (useSSL) {
				//jndiProps.put("com.sun.jndi.rmi.factory.socket", getClientSocketFactory());
				//System.out.println("Setting property to access rmi jndi over ssl");
			}
		}

		return jndiProps;
	}

	public static void unExportObject(Remote obj) throws NoSuchObjectException {
		UnicastRemoteObject.unexportObject(obj, true);
	}

	/**
	 * @return the sslTrustStore
	 */
	public String getSslTrustStore() {
		return sslTrustStore;
	}

	/**
	 * @param sslTrustStore
	 *            the sslTrustStore to set
	 */
	public void setSslTrustStore(String sslTrustStore) {
		this.sslTrustStore = sslTrustStore;
	}

	/**
	 * @return the sslTrustStorePass
	 */
	public String getSslTrustStorePass() {
		return sslTrustStorePass;
	}

	/**
	 * @param sslTrustStorePass
	 *            the sslTrustStorePass to set
	 */
	public void setSslTrustStorePass(String sslTrustStorePass) {
		this.sslTrustStorePass = sslTrustStorePass;
	}

}
