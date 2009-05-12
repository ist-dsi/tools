package pt.utl.ist.fenix.tools.excel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class WorkbookBuilder {

    private final List<AbstractSpreadsheetBuilder<?>> builders = new ArrayList<AbstractSpreadsheetBuilder<?>>();

    private HSSFWorkbook book;

    public WorkbookBuilder add(AbstractSpreadsheetBuilder<?> sheet) {
	builders.add(sheet);
	return this;
    }

    HSSFWorkbook getExcelBook() {
	if (book == null) {
	    book = new HSSFWorkbook();
	}
	return book;
    }

    public void build(WorkbookExportFormat format, String filename) throws IOException {
	build(format, new File(filename));
    }

    public void build(WorkbookExportFormat format, File file) throws IOException {
	build(format, new FileOutputStream(file));
    }

    public void build(WorkbookExportFormat format, OutputStream output) throws IOException {
	for (AbstractSpreadsheetBuilder<?> sheet : builders) {
	    sheet.build(this);
	}
	switch (format) {
	case EXCEL:
	    getExcelBook().write(output);
	    break;
	default:
	    break;
	}
    }
}
