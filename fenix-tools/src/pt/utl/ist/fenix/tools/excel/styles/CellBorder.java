package pt.utl.ist.fenix.tools.excel.styles;

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
}
