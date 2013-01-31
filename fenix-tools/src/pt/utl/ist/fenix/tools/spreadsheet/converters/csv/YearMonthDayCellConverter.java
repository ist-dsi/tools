package pt.utl.ist.fenix.tools.spreadsheet.converters.csv;

import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.spreadsheet.converters.CellConverter;

public class YearMonthDayCellConverter implements CellConverter {
	@Override
	public Object convert(Object source) {
		return (source != null) ? ((YearMonthDay) source).toString("dd/MM/yyyy") : null;
	}
}
