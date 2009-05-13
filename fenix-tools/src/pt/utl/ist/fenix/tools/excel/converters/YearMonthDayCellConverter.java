package pt.utl.ist.fenix.tools.excel.converters;

import org.joda.time.YearMonthDay;

public class YearMonthDayCellConverter implements CellConverter {

    @Override
    public Object convert(Object source) {
	return (source != null) ? ((YearMonthDay) source).toDateTimeAtMidnight().toDate() : null;
    }

}
