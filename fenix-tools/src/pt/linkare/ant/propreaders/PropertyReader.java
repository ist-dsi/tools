package pt.linkare.ant.propreaders;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import pt.linkare.ant.InputProperty;
import pt.linkare.ant.InvalidPropertySpecException;

public interface PropertyReader {

	public void setProperty(InputProperty prop);
	public Collection<InputProperty> readPropertyValue(boolean fromDefault) throws InvalidPropertySpecException, UnsupportedEncodingException;
	public boolean isDebug();
	public void setDebug(boolean debug);
	public void setEncoding(String encoding);

}
