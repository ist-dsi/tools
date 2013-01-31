package pt.utl.ist.fenix.tools.util;

import java.io.Serializable;

/**
 * 
 * @author Nuno Diegues
 * @author Anil Kassamali
 * @author Luis Cruz
 * 
 */
@SuppressWarnings("serial")
public class ByteArray implements Serializable {

	private byte[] bytes;

	public ByteArray() {
	}

	public ByteArray(byte[] value) {
		this.bytes = value;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

}
