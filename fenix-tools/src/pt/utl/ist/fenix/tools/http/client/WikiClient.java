package pt.utl.ist.fenix.tools.http.client;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;

public class WikiClient {

    public static void main(String[] args) {
        final String wikiHost = args[0];
        final String wikiPort = args[1];
        final String wikiUsername = args[2];
        final String wikiPassword = args[3];
        final String wikiPage = args[4];
        final String outputFilename = args[5];

        try {
            processRequest(wikiHost, wikiPort, wikiUsername, wikiPassword, wikiPage, outputFilename);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.exit(0);
    }

    private static void processRequest(final String wikiHost, final String wikiPort,
            final String wikiUsername, final String wikiPassword, final String wikiPage,
            final String outputFilename) {
        try {
            final HttpClient httpClient = HttpClientFactory.getHttpClient(
                    wikiHost, wikiPort);

            final GetMethod method = HttpClientFactory.getGetMethod("/UserPreferences");
            executeMethod(httpClient, method);

            method.setPath("/UserPreferences" + "#preview");

            final NameValuePair[] loginNameValuePairs = new NameValuePair[5];
            loginNameValuePairs[0] = new NameValuePair("action", "userform");
            loginNameValuePairs[1] = new NameValuePair("username", wikiUsername);
            loginNameValuePairs[2] = new NameValuePair("password", wikiPassword);
            loginNameValuePairs[3] = new NameValuePair("login", "Login");
            loginNameValuePairs[4] = new NameValuePair("method", "POST");
            method.setQueryString(loginNameValuePairs);
            executeMethod(httpClient, method);
            method.recycle();

            method.setPath(wikiPage);
            executeMethod(httpClient, method);

            final FileOutputStream fileOutputStream = new FileOutputStream(outputFilename);
            fileOutputStream.write(method.getResponseBody());
            fileOutputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected static void executeMethod(final HttpClient httpClient,
            final GetMethod method) throws IOException, HttpException {
        method.setFollowRedirects(false);
        httpClient.executeMethod(method);
    }

}
