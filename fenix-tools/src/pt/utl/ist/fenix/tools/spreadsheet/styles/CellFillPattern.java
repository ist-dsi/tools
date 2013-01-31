package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellFillPattern extends CellStyle {

	private final short pattern;

	public CellFillPattern(short pattern) {
		this.pattern = pattern;
	}

	@Override
	protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
		style.setFillPattern(pattern);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellFillPattern) {
			CellFillPattern cellFillPattern = (CellFillPattern) obj;
			return pattern == cellFillPattern.pattern;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return pattern;
	}
}
