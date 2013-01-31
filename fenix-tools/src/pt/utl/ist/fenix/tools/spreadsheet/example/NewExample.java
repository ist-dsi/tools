package pt.utl.ist.fenix.tools.spreadsheet.example;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.spreadsheet.SheetData;
import pt.utl.ist.fenix.tools.spreadsheet.SpreadsheetBuilder;
import pt.utl.ist.fenix.tools.spreadsheet.WorkbookExportFormat;

public class NewExample {
	public static class SomeBean {
		public BigDecimal getBigDecimal() {
			return new BigDecimal(new Random().nextDouble());
		}

		public DateTime getDateTime() {
			return new DateTime();
		}

		public Integer getInteger() {
			return new Random().nextInt();
		}

		public LocalDate getLocalDate() {
			return new LocalDate();
		}

		public Boolean getBoolean() {
			return new Random().nextBoolean();
		}

		public Double getDouble() {
			return new Random().nextDouble();
		}

		public String getString() {
			return "hello";
		}

		public Calendar getCalendar() {
			return Calendar.getInstance();
		}

		public Date getDate() {
			return new Date();
		}
	}

	public static void main(String[] args) throws IOException {
		List<SomeBean> beans = new ArrayList<SomeBean>();
		for (int i = 0; i < 500; i++) {
			beans.add(new SomeBean());
		}

		SheetData<SomeBean> data = new SheetData<SomeBean>(beans) {
			@Override
			protected void makeLine(SomeBean item) {
				addCell(new String[] { "Number Stuff", "BigDecimal" }, new short[] { 3, 1 }, item.getBigDecimal(), (short) 1);
				addCell("Integer", item.getInteger());
				addCell("Double", item.getDouble());
				addCell(new String[] { "Date Stuff", "DateTime" }, new short[] { 4, 1 }, item.getDateTime(), (short) 1);
				addCell("LocalDate", item.getLocalDate());
				addCell("Calendar", item.getCalendar());
				addCell("Date", item.getDate());
				addCell(new String[] { "Other Stuff", "Boolean" }, new short[] { 2, 1 }, item.getBoolean(), (short) 1);
				addCell("String", item.getString());
			}
		};
		new SpreadsheetBuilder().addSheet("test", data).build(WorkbookExportFormat.EXCEL, "test.xls");
		new SpreadsheetBuilder().addSheet("test", data).build(WorkbookExportFormat.CSV, "test.csv");
	}
}
