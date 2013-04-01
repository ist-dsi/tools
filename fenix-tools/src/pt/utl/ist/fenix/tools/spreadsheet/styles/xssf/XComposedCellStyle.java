package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XComposedCellStyle extends XCellStyle {
    private final List<XCellStyle> parts = new ArrayList<XCellStyle>();

    @Override
    public XSSFCellStyle getStyle(XSSFWorkbook book) {
        XSSFCellStyle style = book.createCellStyle();
        XSSFFont font = book.createFont();
        for (XCellStyle part : parts) {
            part.appendToStyle(book, style, font);
        }
        style.setFont(font);
        return style;
    }

    @Override
    protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
        // Nothing to do.
    }

    public XCellStyle merge(XCellStyle style) {
        parts.add(style);
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XComposedCellStyle) {
            XComposedCellStyle composedCellStyle = (XComposedCellStyle) obj;
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
        for (XCellStyle part : parts) {
            result += part.hashCode();
        }
        return result;
    }
}
