package pt.utl.ist.fenix.tools.excel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.RichTextString;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.excel.styles.CellDataFormat;
import pt.utl.ist.fenix.tools.excel.styles.CellStyle;
import pt.utl.ist.fenix.tools.excel.styles.ComposedCellStyle;

public abstract class AbstractSpreadsheetBuilder<Item> {
    protected static Map<Class<?>, CellConverter> BASE_CONVERTERS;

    /*
     * this is a fallback map used to convert any object build in a column with
     * no custom converter, it is applied by object type and there can be no
     * more that one converter for any given type.
     */
    static {
	BASE_CONVERTERS = new HashMap<Class<?>, CellConverter>();
	BASE_CONVERTERS.put(Integer.class, new IntegerCellConverter());
	BASE_CONVERTERS.put(DateTime.class, new DateTimeCellConverter());
	BASE_CONVERTERS.put(YearMonthDay.class, new YearMonthDayCellConverter());
	BASE_CONVERTERS.put(LocalDate.class, new LocalDateCellConverter());
	BASE_CONVERTERS.put(BigDecimal.class, new BigDecimalCellConverter());
    }

    protected Map<Class<?>, CellConverter> converters = new HashMap<Class<?>, CellConverter>();

    protected static CellStyle HEADER_STYLE = CellStyle.HEADER_STYLE;

    protected CellStyle headerStyle = HEADER_STYLE;

    protected static Map<Class<?>, CellStyle> TYPE_STYLES;

    static {
	TYPE_STYLES = new HashMap<Class<?>, CellStyle>();
	TYPE_STYLES.put(DateTime.class, new CellDataFormat());
	TYPE_STYLES.put(YearMonthDay.class, new CellDataFormat("dd/MM/yyyy"));
	TYPE_STYLES.put(LocalDate.class, new CellDataFormat("dd/MM/yyyy"));
    }

    protected Map<Class<?>, CellStyle> typeStyles = new HashMap<Class<?>, CellStyle>(TYPE_STYLES);

    protected static List<CellStyle> ROW_STYLES = Collections.emptyList();

    protected List<CellStyle> rowStyles = new ArrayList<CellStyle>(ROW_STYLES);

    protected Object convert(Object content) {
	if (converters.containsKey(content.getClass())) {
	    CellConverter converter = converters.get(content.getClass());
	    return converter.convert(content);
	}
	if (BASE_CONVERTERS.containsKey(content.getClass())) {
	    CellConverter converter = BASE_CONVERTERS.get(content.getClass());
	    return converter.convert(content);
	}
	return content;
    }

    protected void addConverter(Class<?> type, CellConverter converter) {
	converters.put(type, converter);
    }

    protected void setHeaderValue(HSSFWorkbook book, HSSFCell cell, Object value) {
	setValue(book, cell, value, headerStyle.getStyle(book));
    }

    protected void setValue(HSSFWorkbook book, HSSFCell cell, Object value) {
	ComposedCellStyle style = new ComposedCellStyle();
	if (!rowStyles.isEmpty()) {
	    style.merge(rowStyles.get(cell.getRowIndex() % rowStyles.size()));
	}
	if (typeStyles.containsKey(value.getClass())) {
	    style.merge(typeStyles.get(value.getClass()));
	}
	setValue(book, cell, value, style.getStyle(book));
    }

    private void setValue(HSSFWorkbook book, HSSFCell cell, Object value, HSSFCellStyle style) {
	if (value != null) {
	    Object content = convert(value);
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
	    } else if (content instanceof RichTextString) {
		cell.setCellValue((RichTextString) content);
	    } else {
		cell.setCellValue(content.toString());
	    }
	} else {
	    cell.setCellValue((String) null);
	}
	cell.setCellStyle(style);
    }

    protected void setHeaderStyle(CellStyle style) {
	headerStyle = style;
    }

    protected void addTypeStyle(Class<?> type, CellStyle style) {
	typeStyles.put(type, style);
    }

    protected void setRowStyle(CellStyle... styles) {
	rowStyles = Arrays.asList(styles);
    }

    abstract void build(WorkbookBuilder book);
}
