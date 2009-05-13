package pt.utl.ist.fenix.tools.excel.example;

import java.io.IOException;
import java.util.ArrayList;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.excel.CellConverter;
import pt.utl.ist.fenix.tools.excel.SimplifiedSpreadsheetBuilder;
import pt.utl.ist.fenix.tools.excel.WorkbookExportFormat;
import pt.utl.ist.fenix.tools.excel.styles.CellDataFormat;

public class SimplifiedSpreadsheetExample {
    public static class SomeBean {
	public String getProperty1() {
	    return "some string";
	}

	public LocalDate getProperty2() {
	    return new LocalDate();
	}
    }

    public static void main(String[] args) throws IOException {
	ArrayList<SomeBean> elements = new ArrayList<SomeBean>();
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());
	elements.add(new SomeBean());

	SimplifiedSpreadsheetBuilder<SomeBean> builder = new SimplifiedSpreadsheetBuilder<SomeBean>(elements) {
	    {
		addConverter(LocalDate.class, new CellConverter() {
		    @Override
		    public Object convert(Object source) {
			return ((LocalDate) source).toDateTimeAtCurrentTime().toDate();
		    }
		});
		
		addTypeStyle(LocalDate.class, new CellDataFormat("dd/MM/yyyy"));
	    }

	    @Override
	    protected void makeLine(SomeBean item) {
		addColumn("Strings", item.getProperty1());
		addColumn("Dates", item.getProperty2());
	    }
	};
	builder.build(WorkbookExportFormat.EXCEL, "example.xls");
    }
}
