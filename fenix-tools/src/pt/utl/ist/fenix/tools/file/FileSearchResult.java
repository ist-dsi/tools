package pt.utl.ist.fenix.tools.file;

import java.util.List;

public class FileSearchResult {

	private List<FileDescriptor> results;
	private int pageSize;
	private int start;
	private int totalElements;

	public FileSearchResult(List<FileDescriptor> results, int start, int pageSize, Integer totalElements) {
		this.results = results;
		this.start = start;
		this.pageSize = pageSize;
		this.totalElements = totalElements;
	}

	public List<FileDescriptor> getSearchResults() {
		return results;
	}

	public Boolean hasMoreElements() {
		return (start + pageSize) < totalElements;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public Integer getPageSize() {
		return pageSize;
	}

}
