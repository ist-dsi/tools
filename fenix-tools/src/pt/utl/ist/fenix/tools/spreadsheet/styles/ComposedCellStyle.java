package pt.utl.ist.fenix.tools.spreadsheet.styles;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ComposedCellStyle extends CellStyle {
	private final List<CellStyle> parts = new ArrayList<CellStyle>();

	@Override
	public HSSFCellStyle getStyle(HSSFWorkbook book) {
		HSSFCellStyle style = book.createCellStyle();
		HSSFFont font = book.createFont();
		for (CellStyle part : parts) {
			part.appendToStyle(book, style, font);
		}
		style.setFont(font);
		return style;
	}

	@Override
	protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
		// Nothing to do.
	}

	public CellStyle merge(CellStyle style) {
		parts.add(style);
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ComposedCellStyle) {
			ComposedCellStyle composedCellStyle = (ComposedCellStyle) obj;
			boolean equals = true;
			for (int i = 0; i < parts.size(); i++) {
				if (!parts.get(i).equals(composedCellStyle.parts.get(i))) {
					equals = false;
					break;
				}
			}
			return equals;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (CellStyle part : parts) {
			result += part.hashCode();
		}
		return result;
	}
}
