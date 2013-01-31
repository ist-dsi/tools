package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellFillForegroundColor extends XCellStyle {

	private final IndexedColors color;

	public XCellFillForegroundColor(IndexedColors color) {
		this.color = color;
	}

	@Override
	protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
		style.setFillForegroundColor(color.getIndex());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XCellFillForegroundColor) {
			XCellFillForegroundColor cellFillForegroundColor = (XCellFillForegroundColor) obj;
			return color.equals(cellFillForegroundColor.color);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}
}
