package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class FontColor extends CellStyle {

	private final HSSFColor color;

	public FontColor(HSSFColor color) {
		this.color = color;
	}

	@Override
	protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
		font.setColor(color.getIndex());
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
		if (obj instanceof FontColor) {
			FontColor fontColor = (FontColor) obj;
			return color.equals(fontColor.color);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return color.hashCode();
	}
}
