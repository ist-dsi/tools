package pt.utl.ist.fenix.tools.spreadsheet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;

public class CsvReader {
    public static List<Map<String, String>> readCsvFile(File file, String separator, String encoding) throws IOException {
	List<String> lines = FileUtils.readLines(file, encoding);
	List<String> header = null;
	List<Map<String, String>> csvContent = new Vector<Map<String, String>>(lines.size());
	for (String line : lines) {
	    String[] parts = line.split(separator);
	    if (header == null) {
		header = new ArrayList<String>();
		for (String part : parts) {
		    header.add(cleanup(part));
		}
	    } else {
		int index = 0;
		Map<String, String> linemap = new HashMap<String, String>(header.size());
		for (String column : header) {
		    linemap.put(column, access(parts, index++));
		}
		csvContent.add(linemap);
	    }
	}
	return csvContent;
    }

    private static String access(String[] parts, int index) {
	if (parts.length <= index)
	    return null;
	return cleanup(parts[index]);
    }

    private static String cleanup(String part) {
	String value = part.trim();
	if (value.startsWith("\"")) {
	    value = value.replaceAll("\"", "");
	}
	return value.trim();
    }

    public static File getFile(String... parts) {
        File file = null;
        for (String part : parts) {
            if (file == null) {
        	file = new File(part);
            } else {
        	file = new File(file, part);
            }
        }
        return file;
    }
}
