package pt.utl.ist.fenix.tools.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public abstract class AbstractFileManager implements IFileManager {

    // Cluster safe global unique temporary filename
    private static final String TEMPORARY_FILE_GLOBAL_UNIQUE_NAME_PREFIX = UUID.randomUUID().toString();

    private static final int BUFFER_SIZE = 1024 * 1024;

    protected File copyToTemporaryFile(InputStream inputStream) throws IOException {
        File temporaryFile = File.createTempFile(TEMPORARY_FILE_GLOBAL_UNIQUE_NAME_PREFIX, "");
        FileOutputStream targetFileOutputStream = null;
        try {
            targetFileOutputStream = new FileOutputStream(temporaryFile);
            final byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                final int numberOfBytesRead = inputStream.read(buffer, 0, BUFFER_SIZE);

                if (numberOfBytesRead == -1) {
                    break;
                }

                // write out those same bytes
                targetFileOutputStream.write(buffer, 0, numberOfBytesRead);
            }
        } finally {
            if (targetFileOutputStream != null) {
                targetFileOutputStream.close();
            }
        }

        return temporaryFile;
    }

}
