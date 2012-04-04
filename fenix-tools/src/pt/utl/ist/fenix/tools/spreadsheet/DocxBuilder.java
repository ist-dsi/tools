package pt.utl.ist.fenix.tools.spreadsheet;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.spreadsheet.SheetData.Cell;
import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.converters.excel.BigDecimalCellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.converters.excel.DateTimeCellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.converters.excel.IntegerCellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.converters.excel.LocalDateCellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.converters.excel.MultiLanguageStringCellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.converters.excel.YearMonthDayCellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellAlignment;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellBorder;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellDateFormat;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellFillForegroundColor;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellFillPattern;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellStyle;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellVerticalAlignment;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellWrapText;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XComposedCellStyle;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XFontColor;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XFontHeight;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XFontWeight;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XStyleCache;

class DocxBuilder extends AbstractSheetBuilder {
    static Map<Class<?>, CellConverter> BASE_CONVERTERS;

    static {
	// TODO: grow this list to all common basic types.
	BASE_CONVERTERS = new HashMap<Class<?>, CellConverter>();
	BASE_CONVERTERS.put(Integer.class, new IntegerCellConverter());
	BASE_CONVERTERS.put(DateTime.class, new DateTimeCellConverter());
	BASE_CONVERTERS.put(YearMonthDay.class, new YearMonthDayCellConverter());
	BASE_CONVERTERS.put(LocalDate.class, new LocalDateCellConverter());
	BASE_CONVERTERS.put(BigDecimal.class, new BigDecimalCellConverter());
	BASE_CONVERTERS.put(MultiLanguageStringCellConverter.class, new MultiLanguageStringCellConverter());
    }

    private static Map<Class<?>, XCellStyle> TYPE_STYLES;

    static {
	TYPE_STYLES = new HashMap<Class<?>, XCellStyle>();
	TYPE_STYLES.put(DateTime.class, new XCellDateFormat());
	TYPE_STYLES.put(YearMonthDay.class, new XCellDateFormat("dd/MM/yyyy"));
	TYPE_STYLES.put(LocalDate.class, new XCellDateFormat("dd/MM/yyyy"));
	TYPE_STYLES.put(GregorianCalendar.class, new XCellDateFormat());
	TYPE_STYLES.put(Date.class, new XCellDateFormat());
    }

    private static List<XCellStyle> ROW_STYLES = Collections.emptyList();
    
    private static XCellStyle HEADER_STYLE = new XComposedCellStyle() {
	{
	    IndexedColors black = IndexedColors.BLACK;
	    IndexedColors gray = IndexedColors.GREY_25_PERCENT;

	    merge(new XFontColor(black));
	    merge(new XFontWeight(XSSFFont.BOLDWEIGHT_BOLD));
	    merge(new XFontHeight((short) 8));
	    merge(new XCellAlignment(XSSFCellStyle.ALIGN_CENTER));
	    merge(new XCellFillForegroundColor(gray));
	    merge(new XCellFillPattern(XSSFCellStyle.SOLID_FOREGROUND));
	    merge(new XCellBorder(XSSFCellStyle.BORDER_THIN));
	    merge(new XCellVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER));
	    merge(new XCellWrapText(true));
	}
    };

    {
	converters.putAll(BASE_CONVERTERS);
    }

    private XCellStyle headerStyle = HEADER_STYLE;

    private final Map<Class<?>, XCellStyle> typeStyles = new HashMap<Class<?>, XCellStyle>(TYPE_STYLES);

    private List<XCellStyle> rowStyles = new ArrayList<XCellStyle>(ROW_STYLES);

    private XStyleCache styleCache;

    int usefulAreaStart;

    int usefulAreaEnd;

    protected void setHeaderStyle(XCellStyle style) {
	headerStyle = style;
    }

    protected void appendHeaderStyle(XCellStyle style) {
	XComposedCellStyle composed = new XComposedCellStyle();
	composed.merge(headerStyle);
	composed.merge(style);
	headerStyle = composed;
    }

    protected void addTypeStyle(Class<?> type, XCellStyle style) {
	typeStyles.put(type, style);
    }

    protected void setRowStyle(XCellStyle... styles) {
	rowStyles = Arrays.asList(styles);
    }

    protected void setValue(XSSFWorkbook book, XSSFCell cell, Object value, short span) {
	XComposedCellStyle style = new XComposedCellStyle();
	if (!rowStyles.isEmpty()) {
	    style.merge(rowStyles.get(cell.getRowIndex() % rowStyles.size()));
	}
	if (value != null && typeStyles.containsKey(value.getClass())) {
	    style.merge(typeStyles.get(value.getClass()));
	}
	setValue(book, cell, value, span, styleCache.getStyle(style));
    }

    private void setValue(XSSFWorkbook book, XSSFCell cell, Object value, short span, XSSFCellStyle style) {
	if (value != null) {
	    Object content = convert(value);
	    if (content instanceof Boolean) {
		cell.setCellValue((Boolean) content);
	    } else if (content instanceof Double) {
		cell.setCellValue((Double) content);
	    } else if (content instanceof String) {
		cell.setCellValue((String) content);
	    } else if (content instanceof GregorianCalendar) {
		cell.setCellValue((GregorianCalendar) content);
	    } else if (content instanceof Date) {
		cell.setCellValue((Date) content);
	    } else if (content instanceof RichTextString) {
		cell.setCellValue((RichTextString) content);
	    } else if (content instanceof Formula) {
		// cell.setCellFormula(((Formula) content).getFormula(cell,
		// usefulAreaStart, usefulAreaEnd));
	    } else {
		cell.setCellValue(content.toString());
	    }
	} else {
	    cell.setCellValue((String) null);
	}
	if (span > 1) {
	    CellRangeAddress region = new CellRangeAddress(cell.getRowIndex(), cell.getRowIndex(), cell.getColumnIndex(),
		    cell.getColumnIndex() + span - 1);
	    cell.getSheet().addMergedRegion(region);
	}
	cell.setCellStyle(style);
    }

    public void build(Map<String, SheetData<?>> sheets, OutputStream output) throws IOException {
	try {
	    XSSFWorkbook book = new XSSFWorkbook();
	    styleCache = new XStyleCache(book);
	    for (Entry<String, SheetData<?>> entry : sheets.entrySet()) {
		final XSSFSheet sheet = book.createSheet(entry.getKey());
		int rownum = 0;
		int colnum = 0;

		SheetData<?> data = entry.getValue();
		if (!data.headers.get(0).isEmpty()) {
		    for (List<Cell> headerRow : data.headers) {
			colnum = 0;
			final XSSFRow row = sheet.createRow(rownum++);
			for (Cell cell : headerRow) {
			    setValue(book, row.createCell(colnum++), cell.value, cell.span, styleCache.getStyle(headerStyle));
			    colnum = colnum + cell.span - 1;
			}
		    }
		}
		usefulAreaStart = rownum;
		for (final List<Cell> line : data.matrix) {
		    colnum = 0;
		    final XSSFRow row = sheet.createRow(rownum++);
		    for (Cell cell : line) {
			setValue(book, row.createCell(colnum++), cell.value, cell.span);
			colnum = colnum + cell.span - 1;
		    }
		}
		usefulAreaEnd = rownum - 1;
		if (data.hasFooter()) {
		    colnum = 0;
		    final XSSFRow row = sheet.createRow(rownum++);
		    for (Cell cell : data.footer) {
			setValue(book, row.createCell(colnum++), cell.value, cell.span);
			colnum = colnum + cell.span - 1;
		    }
		}
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
		    sheet.autoSizeColumn(i);
		}
	    }
	    book.write(output);
	} finally {
	    output.flush();
	    output.close();
	}
    }
}
