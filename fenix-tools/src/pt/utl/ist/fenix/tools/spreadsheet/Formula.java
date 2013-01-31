package pt.utl.ist.fenix.tools.spreadsheet;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.util.CellReference;

public class Formula {
	public static final Formula SUM = new Formula("sum(%row)");
	public static final Formula AVG = new Formula("average(%row)");

	public static final Formula SUM_FOOTER = new Formula("sum(%col)");
	public static final Formula AVG_FOOTER = new Formula("average(%col)");

	private final String formula;

	private final short formulaSpan;

	private final short[] formulaColumns;

	public Formula(String formula) {
		this(formula, (short) 0);
	}

	public Formula(String formula, short formulaSpan) {
		this.formula = formula;
		this.formulaSpan = formulaSpan;
		this.formulaColumns = null;
	}

	public Formula(String formula, short[] formulaColumns) {
		this.formula = formula;
		this.formulaSpan = 0;
		this.formulaColumns = formulaColumns;
	}

	public String getFormula(HSSFCell cell, int usefulAreaStart, int usefulAreaEnd) {
		String result = formula;
		if (result.contains("%col")) {
			CellReference start = new CellReference(usefulAreaStart, cell.getColumnIndex());
			CellReference end = new CellReference(usefulAreaEnd, cell.getColumnIndex());
			result = result.replaceAll("%col", Matcher.quoteReplacement(start.formatAsString() + ":" + end.formatAsString()));
		}
		if (result.contains("%row")) {
			short startColumn = 0;
			if (formulaColumns == null) {
				if (formulaSpan != 0) {
					startColumn = (short) (cell.getColumnIndex() - formulaSpan - 1);
				}
				CellReference start = new CellReference(cell.getRowIndex(), startColumn);
				CellReference end = new CellReference(cell.getRowIndex(), cell.getColumnIndex() - 1);
				result = result.replaceAll("%row", Matcher.quoteReplacement(start.formatAsString() + ":" + end.formatAsString()));
			} else {
				List<String> parts = new ArrayList<String>();
				for (short col : formulaColumns) {
					parts.add(new CellReference(cell.getRowIndex(), col).formatAsString());
				}
				result = result.replaceAll("%row", Matcher.quoteReplacement(StringUtils.join(parts, ", ")));
			}
		}
		return result;
	}
}
