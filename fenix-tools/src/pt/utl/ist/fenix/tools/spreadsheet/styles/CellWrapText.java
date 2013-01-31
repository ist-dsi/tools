package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellWrapText extends CellStyle {

	private final boolean wrap;

	public CellWrapText(boolean wrap) {
		this.wrap = wrap;
	}

	@Override
	protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
		style.setWrapText(wrap);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellWrapText) {
			CellWrapText cellWrapText = (CellWrapText) obj;
			return wrap == cellWrapText.wrap;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return wrap ? 1 : 0;
	}
}
