package pt.linkare.ant.propreaders;

import java.io.File;
import java.io.UnsupportedEncodingException;

import pt.linkare.ant.InvalidPropertySpecException;

public class PathPropertyReader extends AbstractPropertyReader {

	public PathPropertyReader() {
		super();
	}

	@Override
	public String readProperty() throws InvalidPropertySpecException, UnsupportedEncodingException {
		return readPropertyPath();
	}

	private String readPropertyPath() throws InvalidPropertySpecException, UnsupportedEncodingException {
		boolean pathMustExist = parseBoolean(getProperty().getMetaData("validatePath"), false);
		boolean createPath = parseBoolean(getProperty().getMetaData("createPath"), false);
		boolean persistAbsolutePath = parseBoolean(getProperty().getMetaData("persistAbsolutePath"), false);
		createPath = pathMustExist ? false : createPath;

		StringBuilder message = new StringBuilder();
		if (getProperty().getPropertyMessage() == null) {
			message.append("Please provide the value for property " + getProperty().getPropertyName());
			if (getProperty().getPropertyType() != null) {
				message.append(" (This property is of type " + getProperty().getPropertyType() + ")");
			}

		} else {
			message.append(getProperty().getPropertyMessage());
		}

		if (getProperty().getPropertyDefaultValue() != null) {
			message.append(" [" + getProperty().getPropertyDefaultValue() + "]");
		}

		message.append(pathMustExist ? " * Path must exist!" : "");

		String pathRetVal = null;
		if (getProperty().getPropertyDefaultValue() != null) {
			pathRetVal = getInput().readStringOrDefault(message.toString(), getProperty().getPropertyDefaultValue());
		} else {
			pathRetVal = getInput().readString(message.toString(), getProperty().isPropertyRequired() ? 1 : 0);
		}

		if (pathMustExist && getProperty().isPropertyRequired()) {
			File f = new File(pathRetVal);
			while (!f.exists()) {
				System.out.println("Path " + pathRetVal + " does not exist...");
				if (getProperty().getPropertyDefaultValue() != null) {
					pathRetVal = getInput().readStringOrDefault(message.toString(), getProperty().getPropertyDefaultValue());
				} else {
					pathRetVal = getInput().readString(message.toString(), getProperty().isPropertyRequired() ? 1 : 0);
				}

				f = new File(pathRetVal);
			}
		}
		if (createPath && pathRetVal != null) {
			File f = new File(pathRetVal);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					throw new InvalidPropertySpecException("Unable to create path " + f.getAbsolutePath());
				}
			}
		}

		if (persistAbsolutePath) {
			File f = new File(pathRetVal);
			pathRetVal = f.getAbsolutePath();
		}

		return pathRetVal;

	}
}
