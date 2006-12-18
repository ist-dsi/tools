package pt.utl.ist.fenix.tools.file.rmi;

import java.io.IOException;
import java.io.InputStream;

public class RemoteFileInputStream extends InputStream {

	IRemoteInputStream delegate=null;
	
	public RemoteFileInputStream(IRemoteFile remoteFile) throws IOException {
			delegate=remoteFile.getInputStream();
	}
	/* (non-Javadoc)
	 * @see pt.utl.ist.fenix.tools.file.rmi.IRemoteInputStream#available()
	 */
	public int available() throws IOException {
		return delegate.available();
	}
	/* (non-Javadoc)
	 * @see pt.utl.ist.fenix.tools.file.rmi.IRemoteInputStream#close()
	 */
	public void close() throws IOException {
		delegate.close();
	}
	/* (non-Javadoc)
	 * @see pt.utl.ist.fenix.tools.file.rmi.IRemoteInputStream#read()
	 */
	public int read() throws IOException {
		return delegate.read();
	}

}
