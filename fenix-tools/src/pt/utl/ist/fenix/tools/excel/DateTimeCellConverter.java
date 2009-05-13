package pt.utl.ist.fenix.tools.excel;

import org.joda.time.DateTime;

public class DateTimeCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
	return (source != null) ? ((DateTime) source).toDate() : null;
    }
}
