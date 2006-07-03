package pt.linkare.ant.propreaders;

import java.util.Collection;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;

public interface PropertyReader {

	public void setProperty(InputProperty prop);
	public Collection<InputProperty> readPropertyValue(boolean fromDefault) throws InvalidPropertySpecException;
}
