package pt.utl.ist.fenix.tools.spreadsheet;

/**
 * Spreadsheet formats supported by {@link SpreadsheetBuilder}.
 * 
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
public enum WorkbookExportFormat {
    EXCEL, CSV(","), TSV("\t");

    private String separator;

    private WorkbookExportFormat() {
    }

    private WorkbookExportFormat(String separator) {
	this.separator = separator;
    }

    public String getSeparator() {
	return separator;
    }
}
