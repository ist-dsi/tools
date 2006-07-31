package pt.utl.ist.fenix.tools.html;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.DefaultProtocolSocketFactory;
import org.apache.commons.httpclient.protocol.Protocol;

public class PropertiesTranslator extends PropertiesConverter {

	public String convert(final String string) {
		try {
			final String translation = translate(string, "en", "pt");
			System.out.println(" translated from [" + string + "] to [" + translation + "].");
			return translation;
		} catch (Exception e) {
			return string;
		}
	}

	public static String translate(final String string, final String fromLang, final String toLang) throws Exception {
		final String response = queryHost(string, fromLang, toLang);
		final int indexOfTextArea = response.indexOf("textarea name=q");
		final int indexOfCloseTagMarker = response.indexOf(">", indexOfTextArea);
		final int indexOfCloseTag = response.indexOf("</textarea>", indexOfCloseTagMarker);
		return response.substring(indexOfCloseTagMarker + 1, indexOfCloseTag).trim();
	}

	private static String queryHost(final String string, final String fromLang, final String toLang) throws HttpException, IOException {
		final String host = "translate.google.com";
		final int port = 80;

		final HttpClient httpClient = new HttpClient();
        final Protocol protocol = new Protocol(host, new DefaultProtocolSocketFactory(), port);
        httpClient.getHostConfiguration().setHost(host, port, protocol);

		final PostMethod postMethod = new PostMethod();
		postMethod.setFollowRedirects(false);
		postMethod.setPath("http://translate.google.com/translate_t");
		postMethod.addParameter("langpair", fromLang + "|" + toLang);
		postMethod.addParameter("text", string);
		postMethod.addParameter("", "Translate");
		httpClient.executeMethod(postMethod);
		return postMethod.getResponseBodyAsString();
	}

}
