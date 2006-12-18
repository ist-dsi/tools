package pt.utl.ist.fenix.tools.file.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import pt.linkare.scorm.xmlbeans.ScormData;
import pt.linkare.scorm.xmlbeans.ScormHandlerFactory;
import pt.utl.ist.fenix.tools.file.FileSet;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;

public class ScormFileSetFilter implements FileSetFilter {

	public ScormFileSetFilter() {
		super();
	}

	public void handleFileSet(FileSet fs) throws FileSetFilterException {

		parseMetaInfo(fs, true);

	}

	protected void parseMetaInfo(FileSet fs, boolean preserveMetaInfo) throws FileSetFilterException {
		try {
			if (fs != null) {
				File pifFile = fs.getContentFile(0);
				ScormData scormData = ScormHandlerFactory.getScormHandler().parseScormPifFile(pifFile);

				if (preserveMetaInfo) {
					Collection<FileSetMetaData> previousData = new ArrayList<FileSetMetaData>(fs
							.getMetaInfo());
					fs.doCleanCopyFromFileSet(FileSet.createFileSetFromScormData(scormData));
					fs.addMetaInfo(previousData);
				} else {
					fs.doCleanCopyFromFileSet(FileSet.createFileSetFromScormData(scormData));
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
