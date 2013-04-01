/**
 * 
 */
package pt.utl.ist.fenix.tools.file;

/**
 * @author jpereira
 * 
 */
public interface XMLSerializable {

    public String toXMLString();

    public void fromXMLString(String xml);
}
