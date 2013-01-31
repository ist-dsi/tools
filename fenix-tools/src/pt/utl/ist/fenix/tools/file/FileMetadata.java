package pt.utl.ist.fenix.tools.file;

import java.util.ArrayList;
import java.util.Collection;

@Deprecated
public class FileMetadata {

	private String title;

	private String author;

	public FileMetadata(String title, String author) {
		this.title = title;
		this.author = author;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Collection<FileSetMetaData> createFileSetMetaData() {
		ArrayList<FileSetMetaData> retVal = new ArrayList<FileSetMetaData>(2);

		retVal.add(FileSetMetaData.createAuthorMeta(getAuthor()));
		retVal.add(FileSetMetaData.createTitleMeta(getTitle()));
		return retVal;
	}
}
