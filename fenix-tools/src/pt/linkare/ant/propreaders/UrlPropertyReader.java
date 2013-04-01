package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import pt.linkare.ant.InvalidPropertySpecException;

public class UrlPropertyReader extends AbstractPropertyReader {

    public UrlPropertyReader() {
        super();
    }

    @Override
    public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
        String url = null;
        while (url == null) {
            url = readPropertySimple();
            if (url != null && parseBoolean(getProperty().getMetaData("validate"), false)) {
                try {
                    new URL(url);
                } catch (MalformedURLException e) {
                    System.out.println(url + " is not a valid URL!");
                    url = null;
                }
            }
        }
        return url;
    }

    private String readPropertySimple() throws UnsupportedEncodingException {
        String message = buildDefaultMessage();

        if (getProperty().getPropertyDefaultValue() != null) {
            return getInput().readStringOrDefault(message, getProperty().getPropertyDefaultValue());
        } else {
            return getInput().readString(message, getProperty().isPropertyRequired() ? 1 : 0);
        }

    }

}
