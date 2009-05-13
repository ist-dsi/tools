package pt.utl.ist.fenix.tools.excel.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CreationHelper;

public class CellDataFormat extends CellStyle {

    private String format = "dd/MM/yyyy hh:mm";

    public CellDataFormat() {
    }

    public CellDataFormat(String format) {
	this.format = format;
    }

    @Override
    protected void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font) {
	CreationHelper helper = book.getCreationHelper();
	style.setDataFormat(helper.createDataFormat().getFormat(format));
    }

}
