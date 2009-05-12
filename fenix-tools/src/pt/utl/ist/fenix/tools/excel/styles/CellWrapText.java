package pt.utl.ist.fenix.tools.excel.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellWrapText extends CellStyle {

    private final boolean wrap;

    public CellWrapText(boolean wrap) {
	this.wrap = wrap;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
	style.setWrapText(wrap);
    }

}
