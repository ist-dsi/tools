package pt.utl.ist.fenix.tools.excel.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellAlignment extends CellStyle {

    private final short align;

    public CellAlignment(short align) {
	this.align = align;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
	style.setAlignment(align);
    }

}
