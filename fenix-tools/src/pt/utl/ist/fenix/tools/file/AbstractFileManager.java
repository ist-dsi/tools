package pt.utl.ist.fenix.tools.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import pt.utl.ist.fenix.tools.util.FileUtils;

public abstract class AbstractFileManager implements IFileManager {

    protected File copyToTemporaryFile(InputStream inputStream) throws IOException {
        return FileUtils.copyToTemporaryFile(inputStream);
    }

}
