package pt.utl.ist.fenix.tools.spreadsheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;
import pt.utl.ist.fenix.tools.spreadsheet.styles.CellStyle;
import pt.utl.ist.fenix.tools.spreadsheet.styles.ICellStyle;
import pt.utl.ist.fenix.tools.spreadsheet.styles.xssf.XCellStyle;

/**
 * Builder for all kinds of Spreadsheets (currently supports excel, csv, and
 * tsv). Basically, given a format, and a set of {@link SheetData}s it
 * constructs a spreadsheet in the specified {@link OutputStream}.
 * 
 * It can be further customized with {@link CellConverter}s, and
 * {@link CellStyle}s (that are only useful in the excel format). A startup set
 * of converters and styles were already created, if you need more consider
 * extending these sets in the inner classes instead of just passing as a custom
 * one. If its not domain specific it should be in the tools jar.
 * 
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
public class SpreadsheetBuilder {
    private Map<String, SheetData<?>> sheets = new HashMap<String, SheetData<?>>();
    private final Map<Class<?>, CellConverter> converters = new HashMap<Class<?>, CellConverter>();
    private ICellStyle headerStyle = null;
    private ICellStyle mergeHeaderStyle = null;
    private final Map<Class<?>, ICellStyle> typeStyles = new HashMap<Class<?>, ICellStyle>();
    private List<ICellStyle> rowStyles = new ArrayList<ICellStyle>();

    public SpreadsheetBuilder() {
    }

    /**
     * Adds a sheet to the resulting work. You need at least one to have
     * something useful.
     * 
     * @param name
     *            The name of the sheet.
     * @param sheet
     *            the sheet data.
     * @return this.
     */
    public SpreadsheetBuilder addSheet(String name, SheetData<?> sheet) {
	sheets.put(name, sheet);
	return this;
    }

    /**
     * Adds a custom type converter.
     * 
     * @param type
     *            The type of object to be converted
     * @param converter
     *            The converter class
     * @return this.
     */
    public SpreadsheetBuilder addConverter(Class<?> type, CellConverter converter) {
	converters.put(type, converter);
	return this;
    }

    /**
     * Overrides the header style.
     * 
     * @param style
     *            The style specification
     * @return this.
     */
    public SpreadsheetBuilder setHeaderStyle(CellStyle style) {
	headerStyle = style;
	return this;
    }

    /**
     * Merges the specified style the the existing header style.
     * 
     * @param style
     *            The style specification
     * @return this.
     */
    protected SpreadsheetBuilder appendHeaderStyle(ICellStyle style) {
	mergeHeaderStyle = style;
	return this;
    }

    /**
     * Adds a new style by object type.
     * 
     * @param type
     *            The type of object (before conversion) on which all cells of
     *            that object have the specified style applied.
     * @param style
     *            The style specification
     * @return this.
     */
    protected SpreadsheetBuilder addTypeStyle(Class<?> type, ICellStyle style) {
	typeStyles.put(type, style);
	return this;
    }

    /**
     * Adds a set of row styles. If more than one is specified they are applied
     * alternated on the lines, this can be used to achieve that grey/white line
     * background alternation.
     * 
     * @param styles
     *            A set of style specifications
     * @return this.
     */
    protected SpreadsheetBuilder setRowStyle(ICellStyle... styles) {
	rowStyles = Arrays.asList(styles);
	return this;
    }

    /**
     * Writes the data sets in the specified file.
     * 
     * @param format
     *            type of spreadsheet
     * @param filename
     *            the output file
     * @throws IOException
     *             if and error occurs while writing.
     */
    public void build(WorkbookExportFormat format, String filename) throws IOException {
	build(format, new File(filename));
    }

    /**
     * Writes the data sets in the specified file.
     * 
     * @param format
     *            type of spreadsheet
     * @param file
     *            the output file
     * @throws IOException
     *             if and error occurs while writing.
     */
    public void build(WorkbookExportFormat format, File file) throws IOException {
	build(format, new FileOutputStream(file));
    }

    /**
     * Writes the data sets in the specified stream.
     * 
     * @param format
     *            type of spreadsheet
     * @param output
     *            the output stream
     * @throws IOException
     *             if and error occurs while writing.
     */
    public void build(WorkbookExportFormat format, OutputStream output) throws IOException {
	switch (format) {
	case EXCEL: {
	    ExcelBuilder builder = new ExcelBuilder();
	    for (Entry<Class<?>, CellConverter> entry : converters.entrySet()) {
		builder.addConverter(entry.getKey(), entry.getValue());
	    }
	    if (headerStyle != null) {
		builder.setHeaderStyle((CellStyle) headerStyle);
	    }
	    if (mergeHeaderStyle != null) {
		builder.appendHeaderStyle((CellStyle) mergeHeaderStyle);
	    }
	    for (Entry<Class<?>, ICellStyle> entry : typeStyles.entrySet()) {
		builder.addTypeStyle(entry.getKey(), (CellStyle) entry.getValue());
	    }
	    builder.setRowStyle(rowStyles.toArray(new CellStyle[0]));
	    builder.build(sheets, output);
	    break;
	}
	case CSV:
	case TSV: {
	    CsvBuilder builder = new CsvBuilder();
	    for (Entry<Class<?>, CellConverter> entry : converters.entrySet()) {
		builder.addConverter(entry.getKey(), entry.getValue());
	    }
	    builder.build(sheets, output, format.getSeparator());
	    break;
	}
	case DOCX:
	    DocxBuilder builder = new DocxBuilder();
	    for (Entry<Class<?>, CellConverter> entry : converters.entrySet()) {
		builder.addConverter(entry.getKey(), entry.getValue());
	    }
	    if (headerStyle != null) {
		builder.setHeaderStyle((XCellStyle) headerStyle);
	    }
	    if (mergeHeaderStyle != null) {
		builder.appendHeaderStyle((XCellStyle) mergeHeaderStyle);
	    }
	    for (Entry<Class<?>, ICellStyle> entry : typeStyles.entrySet()) {
		builder.addTypeStyle(entry.getKey(), (XCellStyle) entry.getValue());
	    }
	    builder.setRowStyle(rowStyles.toArray(new XCellStyle[0]));
	    builder.build(sheets, output);
	    break;

	default:
	    break;
	}
    }
}
