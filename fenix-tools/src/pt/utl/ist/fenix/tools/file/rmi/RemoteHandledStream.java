package pt.utl.ist.fenix.tools.file.rmi;

import java.io.IOException;

public interface RemoteHandledStream {
	public long getLastAccessTime();
	public void destroy() throws IOException;
	public boolean hasEnded();
}
