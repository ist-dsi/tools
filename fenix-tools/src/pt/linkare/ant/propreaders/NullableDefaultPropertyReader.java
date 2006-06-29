package pt.linkare.ant.propreaders;

import pt.linkare.ant.InvalidPropertySpecException;

public class NullableDefaultPropertyReader extends AbstractPropertyReader{

	public NullableDefaultPropertyReader() {
		super();
	}

	public String readProperty() throws InvalidPropertySpecException {
		return getProperty().getPropertyDefaultValue();
	}
	
	
}
