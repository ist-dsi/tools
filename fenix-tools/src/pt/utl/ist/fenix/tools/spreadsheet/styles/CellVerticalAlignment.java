package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class CellVerticalAlignment extends CellStyle {

    private final short align;

    public CellVerticalAlignment(short align) {
        this.align = align;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
        style.setVerticalAlignment(align);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CellVerticalAlignment) {
            CellVerticalAlignment cellVerticalAlignment = (CellVerticalAlignment) obj;
            return align == cellVerticalAlignment.align;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return align;
    }
}
