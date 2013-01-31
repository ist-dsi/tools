package pt.utl.ist.fenix.tools.http.client;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodRetryHandler;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
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
		//The following is deprecated... should use Method.setCookiePolicy (done in getGetMethod)
		//client.getState().setCookiePolicy(CookiePolicy.getDefaultPolicy());

		//This one is also deprecated... see below the new way
		//client.setConnectionTimeout(CONNECTION_TIMEOUT);
		client.getHttpConnectionManager().getParams().setConnectionTimeout(CONNECTION_TIMEOUT);

		//This one is also deprecated... see below the new way
		//client.setStrictMode(false);
		client.getParams().makeLenient();

		return client;
	}

	protected static Protocol getProtocol(final String serverHostname, final int serverPort) {
		return new Protocol(serverHostname, new DefaultProtocolSocketFactory(), serverPort);
	}

	public static GetMethod getGetMethod(final String path) {
		final GetMethod method = new GetMethod(path);

		//this is the not deprecated way of keeping cookie policies
		method.getParams().setCookiePolicy(CookiePolicy.DEFAULT);

		//This one is deprecated...
		//DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler();
		//retryhandler.setRequestSentRetryEnabled(false);
		//retryhandler.setRetryCount(3);
		//method.setMethodRetryHandler(retryhandler);

		//Here it is the right way
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new HttpMethodRetryHandler() {
			private int retryCount = 3;
			private boolean requestSentRetryEnabled = false;

			@Override
			public boolean retryMethod(HttpMethod method, IOException exception, int executionCount) {

				return ((!method.isRequestSent() || requestSentRetryEnabled) && (executionCount <= retryCount));
			}
		});

		return method;
	}

}
