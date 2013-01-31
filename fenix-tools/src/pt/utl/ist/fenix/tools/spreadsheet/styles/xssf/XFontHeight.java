package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XFontHeight extends XCellStyle {

	private final short height;

	public XFontHeight(short height) {
		this.height = height;
	}

	@Override
	protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
		font.setFontHeightInPoints(height);
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
		if (obj instanceof XFontHeight) {
			XFontHeight fontHeight = (XFontHeight) obj;
			return height == fontHeight.height;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return height;
	}
}
