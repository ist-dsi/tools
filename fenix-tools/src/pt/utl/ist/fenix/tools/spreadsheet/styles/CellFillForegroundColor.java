package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public class CellFillForegroundColor extends CellStyle {

    private final HSSFColor color;

    public CellFillForegroundColor(HSSFColor color) {
        this.color = color;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
        style.setFillForegroundColor(color.getIndex());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CellFillForegroundColor) {
            CellFillForegroundColor cellFillForegroundColor = (CellFillForegroundColor) obj;
            return color.equals(cellFillForegroundColor.color);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }
}
