package pt.utl.ist.fenix.tools.excel.converters;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalCellConverter implements CellConverter {

    private int scale;
    private RoundingMode mode;
    private boolean custom = false;

    public BigDecimalCellConverter() {
    }

    public BigDecimalCellConverter(final int scale, final RoundingMode mode) {
	this.custom = true;
	this.scale = scale;
	this.mode = mode;
    }

    @Override
    public Object convert(Object source) {
	if (source == null) {
	    return Double.valueOf(0d);
	}
	final BigDecimal value = (BigDecimal) source;
	return custom ? value.setScale(scale, mode).doubleValue() : value.doubleValue();
    }
}
