package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XStyleCache {
    private XSSFWorkbook book;

    private Map<XCellStyle, XSSFCellStyle> cache = new HashMap<XCellStyle, XSSFCellStyle>();

    public XStyleCache(XSSFWorkbook book) {
        this.book = book;
    }

    public XSSFCellStyle getStyle(XCellStyle style) {
        if (!cache.containsKey(style)) {
            cache.put(style, style.getStyle(book));
        }
        return cache.get(style);
    }

    public int getSize() {
        return cache.size();
    }
}
