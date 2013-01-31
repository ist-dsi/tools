package pt.utl.ist.fenix.tools.file.filters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.utils.FileUtils;

public class ZipFileSetFilter implements FileSetFilter {

	public ZipFileSetFilter() {
		super();
	}

	@Override
	public void handleFileSet(FileSet fs) throws FileSetFilterException {
		File dir;
		try {
			dir = unzipFile(fs.getContentFiles().iterator().next());
			repopulateFileSet(fs, dir);
		} catch (IOException e) {
			throw new IOFileSetFilterException(e);
		}
	}

	private File unzipFile(File file) throws IOException {

		return FileUtils.unzipFile(file);
	}

	protected void repopulateFileSet(FileSet fs, File zipDir) throws FileSetFilterException {
		Collection<File> innerFiles = FileUtils.recursiveListOnlyFiles(zipDir);
		for (File innerFile : innerFiles) {
			FileSet childSet = new FileSet(innerFile);
			fs.addChildSet(childSet);
		}

		File zipFile = fs.getContentFiles().iterator().next();
		//move zip file to the root of the zip directory
		String originalAbsoluteFilePath = zipFile.getAbsolutePath();
		File newLocation = new File(zipDir, zipFile.getName());
		FileInputStream fis;
		try {
			fis = new FileInputStream(zipFile);
			FileOutputStream fos = new FileOutputStream(newLocation);
			FileUtils.copyInputStreamToOutputStream(fis, fos);
			fos.flush();
			fos.close();
			fis.close();
			fs.replaceFileWithAbsolutePath(originalAbsoluteFilePath, newLocation);
		} catch (FileNotFoundException e) {
			throw new IOFileSetFilterException("Unable to move file " + zipFile.getAbsolutePath()
					+ " to the unzip temp directory " + zipDir.getAbsolutePath());
		} catch (IOException e) {
			throw new IOFileSetFilterException("Unable to move file " + zipFile.getAbsolutePath()
					+ " to the unzip temp directory " + zipDir.getAbsolutePath());
		}

	}

}
