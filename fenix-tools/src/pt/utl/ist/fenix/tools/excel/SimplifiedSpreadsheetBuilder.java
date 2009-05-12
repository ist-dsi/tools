package pt.utl.ist.fenix.tools.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class SimplifiedSpreadsheetBuilder<Item> extends AbstractSpreadsheetBuilder<Item> {
    private final Collection<Item> items;

    private boolean isHeader;

    private final List<String> headers = new ArrayList<String>();

    private final List<List<Object>> matrix = new ArrayList<List<Object>>();

    private List<Object> current;

    public SimplifiedSpreadsheetBuilder(Collection<Item> items) {
	this.items = items;
    }

    protected void addColumn(String header, Object value) {
	if (isHeader) {
	    headers.add(header);
	}
	current.add(value);
    }

    protected abstract void makeLine(Item item);

    @Override
    void build(WorkbookBuilder bookBuilder) {
	isHeader = true;
	for (Item item : items) {
	    current = new ArrayList<Object>();
	    makeLine(item);
	    matrix.add(current);
	    isHeader = false;
	}
	HSSFWorkbook book = bookBuilder.getExcelBook();
	HSSFSheet sheet = book.createSheet();
	int rownum = 0;
	int colnum = 0;
	HSSFRow headerRow = sheet.createRow(rownum++);
	for (String headerString : headers) {
	    HSSFCell cell = headerRow.createCell(colnum++);
	    setHeaderValue(book, cell, headerString);
	}
	for (List<Object> line : matrix) {
	    colnum = 0;
	    HSSFRow row = sheet.createRow(rownum++);
	    for (Object value : line) {
		HSSFCell cell = row.createCell(colnum++);
		setValue(book, cell, value);
	    }
	}
    }

    public void build(WorkbookExportFormat format, String filename) throws IOException {
	new WorkbookBuilder().add(this).build(format, filename);
    }

    public void build(WorkbookExportFormat format, File file) throws IOException {
	new WorkbookBuilder().add(this).build(format, file);
    }

    public void build(WorkbookExportFormat format, OutputStream output) throws IOException {
	new WorkbookBuilder().add(this).build(format, output);
    }
}
