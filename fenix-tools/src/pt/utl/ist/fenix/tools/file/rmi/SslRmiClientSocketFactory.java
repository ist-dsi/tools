package pt.utl.ist.fenix.tools.file.rmi;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;

import javax.net.SocketFactory;
import javax.rmi.ssl.SslRMIClientSocketFactory;

import pt.utl.ist.fenix.tools.file.utils.FileUtils;

public class SslRmiClientSocketFactory implements RMIClientSocketFactory, Serializable {
    transient SocketFactory delegate = null;

    transient SslRMIClientSocketFactory defaultDispatch = null;

    public SslRmiClientSocketFactory() {
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        initDelegate();
        if (delegate != null) {
            return delegate.createSocket(host, port);
        } else {
            return defaultDispatch.createSocket(host, port);
        }
    }

    private void initDelegate() {
        if (delegate == null && defaultDispatch == null) {
            if (RMIConfig.getInstance().isValidKeyStore(RMIConfig.getInstance().getSslTrustStore())) {
                String sslTrustStoreLocation = FileUtils.locateFilePath(RMIConfig.getInstance().getSslTrustStore());
                String sslTrustStorePass = RMIConfig.getInstance().getSslTrustStorePass();
                try {
                    delegate = RMIConfig.loadClientSSLSocketFactory(sslTrustStoreLocation, sslTrustStorePass);
                } catch (Exception e) {
                    defaultDispatch = new SslRMIClientSocketFactory();
                }
            } else {
                defaultDispatch = new SslRMIClientSocketFactory();
            }
        }

    }

}
