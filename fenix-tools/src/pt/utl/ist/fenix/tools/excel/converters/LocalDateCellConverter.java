package pt.utl.ist.fenix.tools.excel.converters;

import org.joda.time.LocalDate;

public class LocalDateCellConverter implements CellConverter {

    @Override
    public Object convert(Object source) {
	return (source != null) ? ((LocalDate) source).toDateTimeAtStartOfDay().toDate() : null;
    }

}
