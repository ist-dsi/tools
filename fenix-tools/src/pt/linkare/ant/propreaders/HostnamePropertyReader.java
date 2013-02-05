package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import pt.linkare.ant.InvalidPropertySpecException;

public class HostnamePropertyReader extends AbstractPropertyReader {

    public HostnamePropertyReader() {
        super();
    }

    @Override
    public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
        String hostname = null;
        while (hostname == null) {
            hostname = readPropertySimple();
            if (!parseBoolean(getProperty().getMetaData("validate"), false)) {
                return hostname;
            }

            if (parseBoolean(getProperty().getMetaData("validate"), false) && !isValidHostname(hostname)) {
                System.out.println("Host  " + hostname + " is not a valid address!");
                hostname = null;
            }
        }
        return hostname;
    }

    protected String readPropertySimple() throws UnsupportedEncodingException {
        String message = buildDefaultMessage();

        if (getProperty().getPropertyDefaultValue() != null) {
            return getInput().readStringOrDefault(message, getProperty().getPropertyDefaultValue());
        } else {
            return getInput().readString(message, getProperty().isPropertyRequired() ? 1 : 0);
        }

    }

    protected boolean isValidHostname(String hostname) {
        InetAddress[] addresses = null;
        try {
            addresses = InetAddress.getAllByName(hostname);
        } catch (UnknownHostException e) {
            return false;
        }

        if (addresses != null && addresses.length > 0) {
            return true;
        } else {
            return false;
        }
    }

}
