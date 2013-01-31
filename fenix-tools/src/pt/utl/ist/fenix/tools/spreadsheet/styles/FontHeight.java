package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FontHeight extends CellStyle {

	private final short height;

	public FontHeight(short height) {
		this.height = height;
	}

	@Override
	protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
		font.setFontHeightInPoints(height);
	}

	@Override
	public HSSFCellStyle getStyle(HSSFWorkbook book) {
		HSSFCellStyle style = book.createCellStyle();
		HSSFFont font = book.createFont();
		appendToStyle(book, style, font);
		style.setFont(font);
		return style;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FontHeight) {
			FontHeight fontHeight = (FontHeight) obj;
			return height == fontHeight.height;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return height;
	}
}
