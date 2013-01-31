package pt.utl.ist.fenix.tools.file;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import pt.utl.ist.fenix.tools.file.filters.FileSetFilter;
import pt.utl.ist.fenix.tools.file.filters.ScormFileSetFilter;
import pt.utl.ist.fenix.tools.file.filters.SimpleFileSetFilter;
import pt.utl.ist.fenix.tools.file.filters.SimpleImageFileSetFilter;
import pt.utl.ist.fenix.tools.file.filters.ZipFileSetFilter;

public enum FileSetType {
	SIMPLE(SimpleFileSetFilter.class), PACKAGE_SCORM_1_2(ScormFileSetFilter.class), UNPACKAGED_SCORM_1_2, APP_DOC, APP_EXCEL,
	APP_PDF, IMAGE_GIF(SimpleFileSetFilter.class, SimpleImageFileSetFilter.class), IMAGE_JPG(SimpleFileSetFilter.class,
			SimpleImageFileSetFilter.class), IMAGE_PNG(SimpleFileSetFilter.class, SimpleImageFileSetFilter.class),
	ZIP_JAR_SIMPLE(ZipFileSetFilter.class, SimpleFileSetFilter.class);

	private Collection<FileSetFilter> filters = new ArrayList<FileSetFilter>();

	private FileSetType(Class<? extends FileSetFilter>... fileSetFiltersClasses) {
		for (Class<? extends FileSetFilter> fileSetFilterClass : fileSetFiltersClasses) {
			try {
				this.filters.add(fileSetFilterClass.newInstance());
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Collection<FileSetFilter> getFileSetFilterChain() {
		return Collections.unmodifiableCollection(this.filters);
	}

}
