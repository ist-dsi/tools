package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellAlignment extends XCellStyle {

    private final short align;

    public XCellAlignment(short align) {
        this.align = align;
    }

    @Override
    protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
        style.setAlignment(align);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XCellAlignment) {
            XCellAlignment cellAlignment = (XCellAlignment) obj;
            return cellAlignment.align == align;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return align;
    }
}
