package pt.utl.ist.fenix.tools.spreadsheet.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class FontWeight extends CellStyle {

    private final short boldweight;

    public FontWeight(short boldweight) {
        this.boldweight = boldweight;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
        font.setBoldweight(boldweight);
    }

    @Override
    public HSSFCellStyle getStyle(HSSFWorkbook book) {
        HSSFCellStyle style = book.createCellStyle();
        HSSFFont font = book.createFont();
        appendToStyle(book, style, font);
        style.setFont(font);
        return style;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FontWeight) {
            FontWeight fontWeight = (FontWeight) obj;
            return boldweight == fontWeight.boldweight;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return boldweight;
    }
}
