package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellBorder extends CellStyle {

    private final short borderBottom;
    private final short borderLeft;
    private final short borderRight;
    private final short borderTop;

    public CellBorder(short border) {
	this.borderBottom = border;
	this.borderLeft = border;
	this.borderRight = border;
	this.borderTop = border;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
	style.setBorderBottom(borderBottom);
	style.setBorderLeft(borderLeft);
	style.setBorderRight(borderRight);
	style.setBorderTop(borderTop);
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof CellBorder) {
	    CellBorder cellBorder = (CellBorder) obj;
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
