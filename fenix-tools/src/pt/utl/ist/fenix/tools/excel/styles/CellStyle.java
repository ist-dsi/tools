package pt.utl.ist.fenix.tools.excel.styles;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

public abstract class CellStyle {
    public static CellStyle HEADER_STYLE = new ComposedCellStyle() {
	{
	    merge(new FontColor(new HSSFColor.BLACK()));
	    merge(new FontWeight(HSSFFont.BOLDWEIGHT_BOLD));
	    merge(new FontHeight((short) 8));
	    merge(new CellAlignment(HSSFCellStyle.ALIGN_CENTER));
	    merge(new CellFillForegroundColor(new HSSFColor.GREY_25_PERCENT()));
	    merge(new CellFillPattern(HSSFCellStyle.SOLID_FOREGROUND));
	    merge(new CellBorder(HSSFCellStyle.BORDER_THIN));
	    merge(new CellVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER));
	    merge(new CellWrapText(true));
	}
    };

    public HSSFCellStyle getStyle(HSSFWorkbook book) {
	HSSFCellStyle style = book.createCellStyle();
	appendToStyle(book, style, null);
	return style;
    }

    protected abstract void appendToStyle(HSSFWorkbook book, HSSFCellStyle style, HSSFFont font);
}
