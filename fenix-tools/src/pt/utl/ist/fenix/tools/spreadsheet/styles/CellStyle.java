package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public abstract class CellStyle {
    public HSSFCellStyle getStyle(HSSFWorkbook book) {
	HSSFCellStyle style = book.createCellStyle();
	appendToStyle(book, style, null);
	return style;
    }

    protected abstract void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font);
}
