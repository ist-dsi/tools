package pt.utl.ist.fenix.tools.spreadsheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
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
	final List<Cell> footer = new ArrayList<Cell>();
	private boolean isHeader;
	private boolean isFooter;
	private List<Cell> current;

	public SheetData(Iterable<Item> items) {
		isHeader = true;
		headers.add(new ArrayList<Cell>());
		Iterator<Item> iterator = items.iterator();
		while (iterator.hasNext()) {
			Item item = iterator.next();
			current = new ArrayList<Cell>();
			isFooter = !iterator.hasNext();
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

	protected void addCell(Object header, Object value) {
		if (isHeader) {
			addHeader(new Object[] { header }, new short[] { 1 });
		}
		addCell(value);
		if (isFooter) {
			addFooter(null);
		}
	}

	protected void addCell(Object upHeader, short upSpan, Object header, short span, Object value, short valueSpan) {
		addCell(new Object[] { upHeader, header }, new short[] { upSpan, span }, value, valueSpan);
	}

	protected void addCell(Object[] headers, short[] headerSpans, Object value, short valueSpan) {
		if (isHeader) {
			addHeader(headers, headerSpans);
		}
		addCell(value, valueSpan);
		if (isFooter) {
			addFooter(null);
		}
	}

	protected void addCell(Object header, Object value, Object footer) {
		if (isHeader) {
			addHeader(new Object[] { header }, new short[] { 1 });
		}
		addCell(value);
		if (isFooter) {
			addFooter(footer, (short) 1);
		}
	}

	protected void addCell(Object upHeader, short upSpan, Object header, short span, Object value, short valueSpan,
			Object footer, short footerSpan) {
		addCell(new Object[] { upHeader, header }, new short[] { upSpan, span }, value, valueSpan, footer, footerSpan);
	}

	protected void addCell(Object[] headers, short[] headerSpans, Object value, short valueSpan, Object footer, short footerSpan) {
		if (isHeader) {
			addHeader(headers, headerSpans);
		}
		addCell(value, valueSpan);
		if (isFooter) {
			addFooter(footer, footerSpan);
		}
	}

	private void addHeader(Object[] headers, short[] spans) {
		int currentColumn = this.headers.get(0).size();
		if (this.headers.size() < headers.length) {
			for (int i = headers.length - this.headers.size(); i > 0; i--) {
				this.headers.add(new ArrayList<Cell>());
			}
		}
		for (int i = headers.length - 1; i >= 0; i--) {
			Object header = headers[i];
			short span = spans[i];
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

	private void addFooter(Object footer) {
		addFooter(footer, (short) 1);
	}

	private void addFooter(Object footer, short hspan) {
		this.footer.add(new Cell(footer, hspan));
	}

	protected void addCell(Object value) {
		addCell(value, (short) 1);
	}

	protected void addCell(Object value, short hspan) {
		this.current.add(new Cell(value, hspan));
	}

	public boolean hasFooter() {
		if (!footer.isEmpty()) {
			for (Cell cell : footer) {
				if (cell.value != null) {
					return true;
				}
			}
		}
		return false;
	}
}
