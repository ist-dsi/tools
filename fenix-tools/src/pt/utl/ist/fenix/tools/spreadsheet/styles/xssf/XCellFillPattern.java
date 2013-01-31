package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellFillPattern extends XCellStyle {

	private final short pattern;

	public XCellFillPattern(short pattern) {
		this.pattern = pattern;
	}

	@Override
	protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
		style.setFillPattern(pattern);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XCellFillPattern) {
			XCellFillPattern cellFillPattern = (XCellFillPattern) obj;
			return pattern == cellFillPattern.pattern;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return pattern;
	}
}
