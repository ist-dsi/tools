package pt.utl.ist.fenix.tools.excel.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FontHeight extends CellStyle {

    private final short height;

    public FontHeight(short height) {
	this.height = height;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
	font.setFontHeightInPoints(height);
    }

    @Override
    public HSSFCellStyle getStyle(HSSFWorkbook book) {
	HSSFCellStyle style = book.createCellStyle();
	HSSFFont font = book.createFont();
	appendToStyle(book, style, font);
	style.setFont(font);
	return style;
    }

}
