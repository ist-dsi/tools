/**
 * 
 */
package pt.utl.ist.fenix.tools.loaders;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public interface IFileLine {

	public boolean fillWithFileLineData(String dataLine);

	public String getUniqueKey();

}
