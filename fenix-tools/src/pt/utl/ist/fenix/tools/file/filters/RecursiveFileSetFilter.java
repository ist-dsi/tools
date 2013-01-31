package pt.utl.ist.fenix.tools.file.filters;

import java.util.ArrayList;

import pt.utl.ist.fenix.tools.file.FileSet;

public abstract class RecursiveFileSetFilter implements FileSetFilter {

	public RecursiveFileSetFilter() {
		super();
	}

	@Override
	public void handleFileSet(FileSet fs) throws FileSetFilterException {
		recurseFileSetLevel(fs);
	}

	private ArrayList<FileSet> recursePath = new ArrayList<FileSet>();

	public void recurseFileSetLevel(FileSet fs) throws FileSetFilterException {
		if (fs != null) {
			if (recursePath.contains(fs)) {
				throw new RecursiveFileSetFilterException("Found a recursive path in the FileSet");
			}

			handleFileSetLevel(fs);
			recursePath.add(fs);
			if (fs.getChildSets() != null && fs.getChildSets().size() != 0) {
				for (FileSet current : fs.getChildSets()) {
					recurseFileSetLevel(current);
				}
			}
			recursePath.remove(fs);
		}

	}

	abstract public void handleFileSetLevel(FileSet leveledFs) throws FileSetFilterException;

}
