package pt.utl.ist.fenix.tools.file.rmi;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.RemoteException;

public class RemoteFileOutputStream extends OutputStream {

    private IRemoteOutputStream remoteOutputStream = null;

    public RemoteFileOutputStream(IRemoteFile remoteFile) throws IOException {
        try {
            remoteOutputStream = remoteFile.getOuputStream();
        } catch (RemoteException e) {
            throw new IOException(e.getMessage());
        }
    }

    @Override
    public void write(int b) throws IOException {
        remoteOutputStream.write(b);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#close()
     */
    @Override
    public void close() throws IOException {
        remoteOutputStream.close();
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#flush()
     */
    @Override
    public void flush() throws IOException {
        remoteOutputStream.flush();
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[], int, int)
     */
    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        remoteOutputStream.write(b, off, len);
    }

    /* (non-Javadoc)
     * @see java.io.OutputStream#write(byte[])
     */
    @Override
    public void write(byte[] b) throws IOException {
        remoteOutputStream.write(b);
    }

}
