package pt.utl.ist.fenix.tools.excel;

public class IntegerCellConverter implements CellConverter {
    @Override
    public Object convert(Object source) {
	return new Double(((Integer) source).doubleValue());
    }
}
