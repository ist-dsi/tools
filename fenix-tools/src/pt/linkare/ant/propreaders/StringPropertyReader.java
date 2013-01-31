package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;

public class StringPropertyReader extends AbstractPropertyReader {

	public StringPropertyReader() {
		super();
	}

	@Override
	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		return readPropertySimple();
	}

	private String readPropertySimple() throws UnsupportedEncodingException {
		String message = buildDefaultMessage();

		if (getProperty().getPropertyDefaultValue() != null) {
			return getInput().readStringOrDefault(message, getProperty().getPropertyDefaultValue());
		} else {
			int specMinLength = 1;
			try {
				specMinLength = Integer.parseInt(getProperty().getMetaData("minLength"));
			} catch (Exception e) {
				//noop
			}
			return getInput().readString(message, getProperty().isPropertyRequired() ? specMinLength : 0);
		}

	}

}
