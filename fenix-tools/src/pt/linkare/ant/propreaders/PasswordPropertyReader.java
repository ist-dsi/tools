package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;

public class PasswordPropertyReader extends StringPropertyReader {

    public PasswordPropertyReader() {
        super();
    }

    @Override
    public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
        return readPropertySimple();
    }

    private String readPropertySimple() throws UnsupportedEncodingException {
        String message = buildDefaultMessage();

        if (getProperty().getPropertyDefaultValue() != null) {
            return getInput().readPrivateStringOrDefault(message, getProperty().getPropertyDefaultValue());
        } else {
            int specMinLength = 1;
            try {
                specMinLength = Integer.parseInt(getProperty().getMetaData("minLength"));
            } catch (Exception e) {
                //noop
            }
            return getInput().readPrivateString(message, getProperty().isPropertyRequired() ? specMinLength : 0);
        }

    }
}
