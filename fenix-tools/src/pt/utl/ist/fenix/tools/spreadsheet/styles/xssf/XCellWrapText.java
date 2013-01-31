package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellWrapText extends XCellStyle {

	private final boolean wrap;

	public XCellWrapText(boolean wrap) {
		this.wrap = wrap;
	}

	@Override
	protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
		style.setWrapText(wrap);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XCellWrapText) {
			XCellWrapText cellWrapText = (XCellWrapText) obj;
			return wrap == cellWrapText.wrap;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return wrap ? 1 : 0;
	}
}
