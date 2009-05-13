package pt.utl.ist.fenix.tools.excel.converters;

public class IntegerCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
	final Integer value = (Integer) source;
	return (value != null) ? new Double(value.doubleValue()) : Double.valueOf(0d);
    }
}
