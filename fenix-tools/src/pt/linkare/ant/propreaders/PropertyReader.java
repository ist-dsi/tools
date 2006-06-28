package pt.linkare.ant.propreaders;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;

public interface PropertyReader {

	public void setProperty(InputProperty prop);
	public String readProperty() throws InvalidPropertySpecException;
	
}
