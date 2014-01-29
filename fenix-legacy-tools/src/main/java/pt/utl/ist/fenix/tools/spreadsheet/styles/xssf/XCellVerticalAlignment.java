package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellVerticalAlignment extends XCellStyle {

    private final short align;

    public XCellVerticalAlignment(short align) {
        this.align = align;
    }

    @Override
    protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
        style.setVerticalAlignment(align);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XCellVerticalAlignment) {
            XCellVerticalAlignment cellVerticalAlignment = (XCellVerticalAlignment) obj;
            return align == cellVerticalAlignment.align;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return align;
    }
}
