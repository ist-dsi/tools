package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XFontWeight extends XCellStyle {

    private final short boldweight;

    public XFontWeight(short boldweight) {
	this.boldweight = boldweight;
    }

    @Override
    protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
	font.setBoldweight(boldweight);
    }

    @Override
    public XSSFCellStyle getStyle(XSSFWorkbook book) {
	XSSFCellStyle style = book.createCellStyle();
	XSSFFont font = book.createFont();
	appendToStyle(book, style, font);
	style.setFont(font);
	return style;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof XFontWeight) {
	    XFontWeight fontWeight = (XFontWeight) obj;
	    return boldweight == fontWeight.boldweight;
	}
	return false;
    }

    @Override
    public int hashCode() {
	return boldweight;
    }
}
