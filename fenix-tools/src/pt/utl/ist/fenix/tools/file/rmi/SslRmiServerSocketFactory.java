package pt.utl.ist.fenix.tools.file.rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;

import javax.net.ServerSocketFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;

import pt.utl.ist.fenix.tools.file.utils.FileUtils;

public class SslRmiServerSocketFactory implements RMIServerSocketFactory {
    transient ServerSocketFactory delegate = null;
    transient SslRMIServerSocketFactory defaultDispatch = null;

    public SslRmiServerSocketFactory() {
    }

    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        initDelegate();
        if (delegate != null) {
            return delegate.createServerSocket(port);
        } else {
            return defaultDispatch.createServerSocket(port);
        }
    }

    private void initDelegate() {
        if (delegate == null && defaultDispatch == null) {
            if (RMIConfig.getInstance().isValidKeyStore(RMIConfig.getInstance().getSslKeyStore())) {
                String sslKeyStoreLocation = FileUtils.locateFilePath(RMIConfig.getInstance().getSslKeyStore());
                String sslKeyStorePass = RMIConfig.getInstance().getSslKeyStorePass();
                try {
                    delegate = RMIConfig.loadServerSSLSocketFactory(sslKeyStoreLocation, sslKeyStorePass);
                } catch (Exception e) {
                    defaultDispatch = new SslRMIServerSocketFactory();
                }
            } else {
                defaultDispatch = new SslRMIServerSocketFactory();
            }
        }

    }

}