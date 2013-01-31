package pt.utl.ist.fenix.tools.resources;

public interface IMessageResourceProvider {

	public String getMessage(String bundle, String key, String... args);

}
