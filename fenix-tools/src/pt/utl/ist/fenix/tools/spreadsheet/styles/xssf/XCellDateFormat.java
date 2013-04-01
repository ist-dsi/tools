package pt.utl.ist.fenix.tools.spreadsheet.styles.xssf;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class XCellDateFormat extends XCellStyle {

    private String format = "dd/MM/yyyy hh:mm";

    public XCellDateFormat() {
    }

    public XCellDateFormat(String format) {
        this.format = format;
    }

    @Override
    protected void appendToStyle(XSSFWorkbook book, XSSFCellStyle style, XSSFFont font) {
        CreationHelper helper = book.getCreationHelper();
        style.setDataFormat(helper.createDataFormat().getFormat(format));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XCellDateFormat) {
            XCellDateFormat cellDataFormat = (XCellDateFormat) obj;
            return format.equals(cellDataFormat.format);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return format.hashCode();
    }
}
