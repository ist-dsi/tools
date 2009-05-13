package pt.utl.ist.fenix.tools.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 * 
 * @param <Item>
 *            The type of object that is used to fill the lines.
 */
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
    final void build(WorkbookBuilder bookBuilder) {
	isHeader = true;
	for (final Item item : items) {
	    current = new ArrayList<Object>();
	    makeLine(item);
	    matrix.add(current);
	    isHeader = false;
	}
	final HSSFWorkbook book = bookBuilder.getExcelBook();
	final HSSFSheet sheet = book.createSheet();
	int rownum = 0;
	int colnum = 0;

	final HSSFRow headerRow = sheet.createRow(rownum++);
	for (final String headerString : headers) {
	    setHeaderValue(book, headerRow.createCell(colnum++), headerString);
	}

	for (final List<Object> line : matrix) {
	    colnum = 0;
	    final HSSFRow row = sheet.createRow(rownum++);
	    for (final Object value : line) {
		setValue(book, row.createCell(colnum++), value);
	    }
	}
	for (int i = 0; i < sheet.getLastRowNum(); i++) {
	    sheet.autoSizeColumn(i);
	}
    }
}
