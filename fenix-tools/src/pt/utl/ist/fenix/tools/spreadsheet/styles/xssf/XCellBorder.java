package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellBorder extends XCellStyle {

    private final short borderBottom;
    private final short borderLeft;
    private final short borderRight;
    private final short borderTop;

    public XCellBorder(short border) {
        this.borderBottom = border;
        this.borderLeft = border;
        this.borderRight = border;
        this.borderTop = border;
    }

    @Override
    protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
        style.setBorderBottom(borderBottom);
        style.setBorderLeft(borderLeft);
        style.setBorderRight(borderRight);
        style.setBorderTop(borderTop);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XCellBorder) {
            XCellBorder cellBorder = (XCellBorder) obj;
            return borderBottom == cellBorder.borderBottom && borderTop == cellBorder.borderTop
                    && borderLeft == cellBorder.borderLeft && borderRight == cellBorder.borderRight;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (borderBottom << 24) & (borderTop << 16) & (borderLeft << 8) & borderRight;
    }
}
