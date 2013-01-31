package pt.utl.ist.fenix.tools.spreadsheet.styles;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class StyleCache {
	private HSSFWorkbook book;

	private Map<CellStyle, HSSFCellStyle> cache = new HashMap<CellStyle, HSSFCellStyle>();

	public StyleCache(HSSFWorkbook book) {
		this.book = book;
	}

	public HSSFCellStyle getStyle(CellStyle style) {
		if (!cache.containsKey(style)) {
			cache.put(style, style.getStyle(book));
		}
		return cache.get(style);
	}

	public int getSize() {
		return cache.size();
	}
}
