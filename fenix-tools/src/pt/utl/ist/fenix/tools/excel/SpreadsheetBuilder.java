package pt.utl.ist.fenix.tools.excel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;

public abstract class SpreadsheetBuilder<Item> {

    public abstract class ColumnGroup {
	private ColumnBuilder[] columns;

	public ColumnGroup(ColumnBuilder... columns) {
	    this.columns = columns;
	}

	public abstract void fillHeader(HSSFCell cell);

	@Override
	public String toString() {
	    StringBuilder builder = new StringBuilder();
	    builder.append("[");
	    for (ColumnBuilder column : columns) {
		builder.append(column.toString());
		builder.append(", ");
	    }
	    builder.append("]");
	    return builder.toString();
	}
    }

    public class BeanColumnGroupBuilder extends ColumnGroup {
	private final String header;

	public BeanColumnGroupBuilder(String headerKey, ResourceBundle headerBundle, ColumnBuilder... columns) {
	    super(columns);
	    this.header = headerBundle.getString(headerKey);
	}

	@Override
	public void fillHeader(HSSFCell cell) {
	    cell.setCellValue(header);
	}

	@Override
	public String toString() {
	    return header + " " + super.toString();
	}
    }

    public abstract class ColumnBuilder {
	private int headerColspan = 1;
	private int headerRowspan = 1;
	private int colspan = 1;
	private int rowspan = 1;

	public ColumnBuilder setHeaderColspan(int headerColspan) {
	    this.headerColspan = headerColspan;
	    return this;
	}

	public ColumnBuilder setHeaderRowspan(int headerRowspan) {
	    this.headerRowspan = headerRowspan;
	    return this;
	}

	public ColumnBuilder setColspan(int colspan) {
	    this.colspan = colspan;
	    this.headerColspan = colspan;
	    return this;
	}

	public ColumnBuilder setRowspan(int rowspan) {
	    this.rowspan = rowspan;
	    this.headerRowspan = rowspan;
	    return this;
	}

	public void fillHeader(HSSFCell cell) {
	    if (headerColspan > 1 || headerRowspan > 1) {
		CellRangeAddress range = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + headerRowspan - 1, cell
			.getColumnIndex(), cell.getColumnIndex() + headerColspan - 1);
		cell.getRow().getSheet().addMergedRegion(range);
	    }
	}

	public void fillCell(HSSFCell cell, Item item) {
	    if (colspan > 1 || rowspan > 1) {
		CellRangeAddress range = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + rowspan - 1, cell
			.getColumnIndex(), cell.getColumnIndex() + colspan - 1);
		cell.getRow().getSheet().addMergedRegion(range);
	    }
	}
    }

    public class BeanBuilder extends ColumnBuilder {
	private final String header;
	private final String property;

	public BeanBuilder(String headerKey, ResourceBundle headerBundle, String property) {
	    this.header = headerBundle.getString(headerKey);
	    this.property = property;
	}

	@Override
	public void fillHeader(HSSFCell cell) {
	    super.fillHeader(cell);
	    cell.setCellValue(header);
	}

	@Override
	public void fillCell(HSSFCell cell, Item item) {
	    super.fillCell(cell, item);
	    CellStyle cellStyle = book.createCellStyle();
	    CreationHelper helper = book.getCreationHelper();
	    try {
		Object content = PropertyUtils.getProperty(item, property);
		if (content instanceof Boolean) {
		    cell.setCellValue((Boolean) content);
		} else if (content instanceof Double) {
		    cell.setCellValue((Double) content);
		} else if (content instanceof String) {
		    cell.setCellValue((String) content);
		} else if (content instanceof Calendar) {
		    cell.setCellValue((Calendar) content);
		} else if (content instanceof Date) {
		    cell.setCellValue((Date) content);
		    cellStyle.setDataFormat(helper.createDataFormat().getFormat("dd/mm/yy hh:mm"));
		    cell.setCellStyle(cellStyle);
		} else if (content instanceof RichTextString) {
		    cell.setCellValue((RichTextString) content);
		} else {
		    cell.setCellValue((String) null);
		}
	    } catch (Exception e) {
		throw new RuntimeException("could not read property '" + property + "' from object " + item, e);
	    }
	}

	@Override
	public String toString() {
	    return "(" + header + " " + property + ")";
	}
    }

    protected final HSSFWorkbook book;

    private boolean hasHeader = true;

    private int startRow = 0;

    private int startColumn = 0;

    protected abstract List<ColumnBuilder> getColumns();

    protected abstract List<ColumnGroup> getColumnGroups();

    public SpreadsheetBuilder(HSSFWorkbook book) {
	this.book = book;
    }

    public SpreadsheetBuilder<Item> hideHeader() {
	hasHeader = false;
	return this;
    }

    public SpreadsheetBuilder<Item> setStartRow(int start) {
	startRow = start;
	return this;
    }

    public SpreadsheetBuilder<Item> setStartColumn(int start) {
	startColumn = start;
	return this;
    }

    public HSSFSheet build(String name, List<Item> items) {
	List<ColumnBuilder> columns = getColumns();
	Map<ColumnBuilder, ColumnGroup> groupMap = new HashMap<ColumnBuilder, ColumnGroup>();
	for (ColumnGroup group : getColumnGroups()) {
	    for (ColumnBuilder column : group.columns) {
		groupMap.put(column, group);
	    }
	}
	HSSFSheet sheet = book.createSheet();
	int rownum = startRow;
	int colnum = startColumn;
	if (hasHeader) {
	    if (!groupMap.isEmpty()) {
		List<ColumnGroup> used = new ArrayList<ColumnGroup>();
		HSSFRow header = sheet.createRow(rownum++);
		for (ColumnBuilder column : columns) {
		    if (groupMap.containsKey(column)) {
			ColumnGroup group = groupMap.get(column);
			if (!used.contains(group)) {
			    HSSFCell cell = header.createCell(colnum);
			    group.fillHeader(cell);
			    CellRangeAddress range = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell
				    .getColumnIndex(), cell.getColumnIndex() + group.columns.length - 1);
			    cell.getRow().getSheet().addMergedRegion(range);
			    colnum = header.getLastCellNum() + group.columns.length - 1;
			    used.add(group);
			}
		    } else {
			HSSFCell cell = header.createCell(colnum);
			column.fillHeader(cell);
			CellRangeAddress range = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex() + 1, cell
				.getColumnIndex(), cell.getColumnIndex());
			cell.getRow().getSheet().addMergedRegion(range);
			colnum = header.getLastCellNum();
		    }
		}
		colnum = startColumn;
	    }
	    HSSFRow header = sheet.createRow(rownum++);
	    for (ColumnBuilder column : columns) {
		column.fillHeader(header.createCell(colnum));
		colnum = header.getLastCellNum();
	    }
	}
	for (Item item : items) {
	    HSSFRow row = sheet.createRow(rownum++);
	    colnum = startColumn;
	    for (ColumnBuilder column : columns) {
		column.fillCell(row.createCell(colnum), item);
		colnum = row.getLastCellNum();
	    }
	}
	for (int i = 0; i < sheet.getLastRowNum(); i++) {
	    sheet.autoSizeColumn(i);
	}
	return sheet;
    }
}
