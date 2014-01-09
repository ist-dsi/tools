package pt.utl.ist.fenix.tools.spreadsheet.converters.csv;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;

public class DateTimeCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
        return (source != null) ? ((DateTime) source).toString("dd/MM/yyyy hh:mm") : null;
    }
}
