package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;

public class CellDateFormat extends CellStyle {

	private String format = "dd/MM/yyyy hh:mm";

	public CellDateFormat() {
	}

	public CellDateFormat(String format) {
		this.format = format;
	}

	@Override
	protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
		CreationHelper helper = book.getCreationHelper();
		style.setDataFormat(helper.createDataFormat().getFormat(format));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CellDateFormat) {
			CellDateFormat cellDataFormat = (CellDateFormat) obj;
			return format.equals(cellDataFormat.format);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return format.hashCode();
	}
}
