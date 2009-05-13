package pt.utl.ist.fenix.tools.excel.converters;

/**
 * Converter from some domain entity that is to be set on an cell to a type that
 * is supported by the excel format.
 * 
 * Excel Supports the following: Boolean, Double, String, Calendar, Date,
 * RichTextString
 * 
 * @author Pedro Santos
 */
public interface CellConverter {
    public Object convert(Object source);
}
