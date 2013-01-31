package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XFontColor extends XCellStyle {

	private final IndexedColors color;

	public XFontColor(IndexedColors color) {
		this.color = color;
	}

	@Override
	protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
		font.setColor(color.getIndex());
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
		if (obj instanceof XFontColor) {
			XFontColor fontColor = (XFontColor) obj;
			return color.equals(fontColor.color);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}
}
