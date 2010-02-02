package pt.utl.ist.fenix.tools.spreadsheet.converters.csv;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;

public class LocalDateCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
	return (source != null) ? ((LocalDate) source).toString("dd/MM/yyyy") : null;
    }
}
