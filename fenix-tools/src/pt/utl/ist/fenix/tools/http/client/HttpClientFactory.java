package pt.utl.ist.fenix.tools.http.client;

import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;

public class HttpClientFactory {

    private final static int CONNECTION_TIMEOUT = 30000000;

    protected HttpClientFactory() {
    }

    public static HttpClient getHttpClient(final String host, final String port) {
        int serverPort = Integer.parseInt(port);

        final HttpClient client = new HttpClient();
        final Protocol protocol = getProtocol(host, serverPort);

        client.getHostConfiguration().setHost(host, serverPort, protocol);
        client.getState().setCookiePolicy(CookiePolicy.getDefaultPolicy());
        client.setConnectionTimeout(CONNECTION_TIMEOUT);
        client.setStrictMode(false);

        return client;
    }

    protected static Protocol getProtocol(final String serverHostname, final int serverPort) {
        return new Protocol(serverHostname, new DefaultProtocolSocketFactory(), serverPort);
    }

    public static GetMethod getGetMethod(final String path) {
        final GetMethod method = new GetMethod(path);

        DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
        retryhandler.setRequestSentRetryEnabled(false);
        retryhandler.setRetryCount(3);
        method.setMethodRetryHandler(retryhandler);

        return method;
    }

}
