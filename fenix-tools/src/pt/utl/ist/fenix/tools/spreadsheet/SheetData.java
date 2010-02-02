package pt.utl.ist.fenix.tools.spreadsheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representation of a sheet of data. Override this class (suggestion: use
 * anonymous classes) to define the contents of the sheet. Overriders must
 * implement the {@link #makeLine(Object)} method to populate a single line
 * given an Item object, a list of these is passed onto the constructor who in
 * turn calls the {@link #makeLine(Object)}.
 * 
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 * 
 * @param <Item>
 *            Type of object that will be used to populate cells.
 */
public abstract class SheetData<Item> {
    public static class Cell {
	Object value;
	short span;

	public Cell(Object value, short span) {
	    this.value = value;
	    this.span = span;
	}
    }

    final List<List<Cell>> headers = new ArrayList<List<Cell>>();
    final List<List<Cell>> matrix = new ArrayList<List<Cell>>();
    private boolean isHeader;
    private List<Cell> current;

    public SheetData(Iterable<Item> items) {
	isHeader = true;
	headers.add(new ArrayList<Cell>());
	for (final Item item : items) {
	    current = new ArrayList<Cell>();
	    makeLine(item);
	    matrix.add(current);
	    isHeader = false;
	}
	Collections.reverse(headers);
    }

    /**
     * Populates a single line of the sheet by calls on the addCell(...)
     * methods. Headers are optional, if you want them use the addCell methods
     * that accept headers.
     * 
     * @param item
     *            the object that will source the current sheet line.
     */
    protected abstract void makeLine(Item item);

    protected void addCell(String header, Object value) {
	if (isHeader) {
	    headers.get(0).add(new Cell(header, (short) 1));
	}
	addCell(value);
    }

    protected void addCell(String upHeader, short upSpan, String header, short span, Object value, short valueSpan) {
	addCell(new String[] { upHeader, header }, new short[] { upSpan, span }, value, valueSpan);
    }

    protected void addCell(String[] headers, short[] headerSpans, Object value, short valueSpan) {
	if (isHeader) {
	    int currentColumn = this.headers.get(0).size();
	    if (this.headers.size() < headers.length) {
		for (int i = headers.length - this.headers.size(); i > 0; i--) {
		    this.headers.add(new ArrayList<Cell>());
		}
	    }
	    for (int i = headers.length - 1; i >= 0; i--) {
		String header = headers[i];
		short span = headerSpans[i];
		List<Cell> headerRow = this.headers.get(headers.length - i - 1);
		int column = 0;
		for (Cell cell : headerRow) {
		    column += cell.span;
		}
		if (currentColumn - column > 0) {
		    headerRow.add(new Cell("", (short) (currentColumn - column)));
		}
		headerRow.add(new Cell(header, span));
	    }
	}
	addCell(value, valueSpan);
    }

    protected void addCell(Object value) {
	addCell(value, (short) 1);
    }

    protected void addCell(Object value, short hspan) {
	current.add(new Cell(value, hspan));
    }
}
