package pt.utl.ist.fenix.tools.http.client;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.joda.time.DateTime;

public class DeployRegister {

	public static void main(String[] args) {
		final String wikiHost = args[0];
		final String wikiPort = args[1];
		final String wikiUsername = args[2];
		final String wikiPassword = args[3];
		final String tagName = args[4];
		final String comment = args[5];

		try {
			registerDeploy(wikiHost, wikiPort, wikiUsername, wikiPassword, tagName, comment);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		System.exit(0);
	}

	private static void registerDeploy(final String wikiHost, final String wikiPort, final String wikiUsername,
			final String wikiPassword, final String tagName, final String comment) {
		final DateTime dateTime = new DateTime();
		final String wikiPage = "DeployLogs/" + dateTime.toString("yyyy-MM-dd");
		final StringBuilder stringBuilder = new StringBuilder();
		appendExistingPage(wikiHost, wikiPort, wikiPage, wikiUsername, wikiPassword, stringBuilder);

		stringBuilder.append("||");
		stringBuilder.append(wikiUsername);
		stringBuilder.append("||");
		stringBuilder.append(dateTime.toString("HH"));
		stringBuilder.append("h");
		stringBuilder.append(dateTime.toString("mm"));

		stringBuilder.append("||fenix / www.apl");

		stringBuilder.append("||HEAD");

		stringBuilder.append("||");
		stringBuilder.append(tagName);
		stringBuilder.append("");

		stringBuilder.append("||");
		stringBuilder.append(comment);
		stringBuilder.append("");

		stringBuilder.append("||");
		final String fileContents = stringBuilder.toString();
		postPage(wikiHost, wikiPort, wikiPage, fileContents, wikiUsername, wikiPassword);
	}

	private static void appendExistingPage(final String wikiHost, final String wikiPort, final String wikiPage,
			final String wikiUsername, final String wikiPassword, final StringBuilder stringBuilder) {
		try {
			final HttpClient httpClient = HttpClientFactory.getHttpClient(wikiHost, wikiPort);

			final GetMethod method = HttpClientFactory.getGetMethod("/UserPreferences");
			executeMethod(httpClient, method);

			method.setPath("/UserPreferences" + "#preview");

			final NameValuePair[] loginNameValuePairs = new NameValuePair[5];
			loginNameValuePairs[0] = new NameValuePair("action", "userform");
			loginNameValuePairs[1] = new NameValuePair("username", wikiUsername);
			loginNameValuePairs[2] = new NameValuePair("password", wikiPassword);
			loginNameValuePairs[3] = new NameValuePair("login", "Login");
			loginNameValuePairs[4] = new NameValuePair("method", "post");
			method.setQueryString(loginNameValuePairs);
			executeMethod(httpClient, method);

			method.setPath("/" + wikiPage + "?action=raw");
			final String response = executeMethod(httpClient, method);
			System.out.println("response: " + response);

			if (response == null || response.length() == 0) {
				stringBuilder
						.append("||'''Programmer'''||'''Hour'''||'''Application(s)'''||'''CVS Branch'''||'''CVS Tag'''||'''Motive for deploy'''||\n");
			} else {
				stringBuilder.append(response);
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void postPage(final String wikiHost, final String wikiPort, final String wikiPage, final String fileContents,
			final String wikiUsername, final String wikiPassword) {
		try {
			final HttpClient httpClient = HttpClientFactory.getHttpClient(wikiHost, wikiPort);

			final GetMethod method = HttpClientFactory.getGetMethod("/UserPreferences");
			executeMethod(httpClient, method);

			method.setPath("/UserPreferences" + "#preview");

			final NameValuePair[] loginNameValuePairs = new NameValuePair[5];
			loginNameValuePairs[0] = new NameValuePair("action", "userform");
			loginNameValuePairs[1] = new NameValuePair("username", wikiUsername);
			loginNameValuePairs[2] = new NameValuePair("password", wikiPassword);
			loginNameValuePairs[3] = new NameValuePair("login", "Login");
			loginNameValuePairs[4] = new NameValuePair("method", "post");
			method.setQueryString(loginNameValuePairs);
			executeMethod(httpClient, method);

			method.setPath("/" + wikiPage);
			executeMethod(httpClient, method);

			method.setPath("/" + wikiPage + "?action=edit");
			executeMethod(httpClient, method);

			method.setPath("/" + wikiPage);

			final NameValuePair[] nameValuePairs = new NameValuePair[4];
			nameValuePairs[0] = new NameValuePair("action", "savepage");
			nameValuePairs[1] = new NameValuePair("button_save", "Save Changes");
			nameValuePairs[2] = new NameValuePair("savetext", fileContents);
			nameValuePairs[3] = new NameValuePair("method", "post");
			method.setQueryString(nameValuePairs);
			executeMethod(httpClient, method);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected static String executeMethod(final HttpClient httpClient, final GetMethod method) throws IOException, HttpException {
		method.setFollowRedirects(true);
		httpClient.executeMethod(method);
		final String response = method.getResponseBodyAsString();
		method.recycle();
		return response;
	}

}