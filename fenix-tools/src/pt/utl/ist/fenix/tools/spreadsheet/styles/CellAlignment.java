package pt.utl.ist.fenix.tools.spreadsheet.styles;

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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CellAlignment) {
            CellAlignment cellAlignment = (CellAlignment) obj;
            return cellAlignment.align == align;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return align;
    }
}
