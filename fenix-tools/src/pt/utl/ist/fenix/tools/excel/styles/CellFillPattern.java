package pt.utl.ist.fenix.tools.excel.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellFillPattern extends CellStyle {

    private final short pattern;

    public CellFillPattern(short pattern) {
	this.pattern = pattern;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
	style.setFillPattern(pattern);
    }

}
