package pt.utl.ist.fenix.tools.scripts;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.FileUtils;
import pt.utl.ist.fenix.tools.util.StringAppender;

public class JoinSQLScripts {

	public static void main(String[] args) {
		final String dirName = args[0];
		final String fromDateString = args[1];
		final String outputFile = args[2];

		final File dir = new File(dirName);
		final SortedMap<String, File> directoryTree = new TreeMap<String, File>();

		loadDirectories(directoryTree, dir);

		System.out.println("Loaded " + directoryTree.size() + " directories, from " + directoryTree.firstKey() + " to "
				+ directoryTree.lastKey());
		System.out.println("Generate run file from " + fromDateString);
		directoryTree.put("Z_BaseDir", dir);

		try {
			final List<String> sqlScripts = loadSQLScripts(directoryTree, "R" + fromDateString);
			final PrintWriter printWriter = constructRunPrintWriter(outputFile);
			for (final String sqlFilename : sqlScripts) {
				printWriter.append("executeWithTempFile \"");
				printWriter.append(sqlFilename);
				printWriter.append("\" ");
				printWriter.append(sqlFilename);
				printWriter.append('\n');
			}
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.exit(0);
	}

	private static PrintWriter constructRunPrintWriter(String outputFile) throws FileNotFoundException {
		final PrintWriter printWriter = new PrintWriter(outputFile);

		printWriter.append("#!/bin/sh\n");
		printWriter.append("# -----------------------------------------------------------------------------\n");
		printWriter.append("#\n");
		printWriter.append("#   Runs sql scripts to update database from previous release.\n");
		printWriter.append("#\n");
		printWriter.append("# -----------------------------------------------------------------------------\n");
		printWriter.append("#\n");
		printWriter.append("\n");
		printWriter.append("DB=$1\n");
		printWriter.append("USER=$2\n");
		printWriter.append("DB_HOST=$3\n");
		printWriter.append("INSTITUTION=`grep \"institution=\" ../../build.properties | cut -d '=' -f2`\n");
		printWriter.append("\n");
		printWriter.append("echo Using host=$DB_HOST\n");
		printWriter.append("echo Using database: $DB\n");
		printWriter.append("echo Using user $USER:\n");
		printWriter.append("echo Institution: $INSTITUTION\n");
		printWriter.append("function execute() {\n");
		printWriter.append("\tif [ -z $3 ]\n");
		printWriter.append("\tthen\n");
		printWriter.append("\techo $1\n");
		printWriter.append("\tmysql -u$USER -f -h$DB_HOST --default-character-set=latin1 $DB < $2 \n");
		printWriter.append("\techo\n");
		printWriter.append("\telse\n");
		printWriter.append("\tif [ \"$INSTITUTION\" == \"$3\" ]\n");
		printWriter.append("\tthen\n");
		printWriter.append("\techo $1\n");
		printWriter.append("\tmysql -u$USER -f -h$DB_HOST --default-character-set=latin1 $DB < $2 \n");
		printWriter.append("\techo\n");
		printWriter.append("\tfi\n");
		printWriter.append("\tfi\n");
		printWriter.append("}\n");
		printWriter.append("\n");
		printWriter.append("function executeWithTempFile() {\n");
		printWriter.append("\techo $1\n");
		printWriter.append("\tmysql -u$USER -f -h$DB_HOST --default-character-set=latin1 $DB < $2 > tmp.sql\n");
		printWriter.append("\tmysql -u$USER -f -h$DB_HOST --default-character-set=latin1 $DB < tmp.sql\n");
		printWriter.append("\trm tmp.sql\n");
		printWriter.append("\techo\n");
		printWriter.append("}\n");
		printWriter.append("\n");
		printWriter.append("\n");

		return printWriter;
	}

	private static void loadDirectories(final SortedMap<String, File> directoryTree, final File dir) {
		for (final File file : dir.listFiles()) {
			if (file.isDirectory() && file.getName().startsWith("R")) {
				final String filename = file.getName();
				if (filename.length() == 11) {
					directoryTree.put(filename, file);
				} else {
					loadDirectories(directoryTree, file);
				}
			}
		}
	}

	private static List<String> loadSQLScripts(final SortedMap<String, File> directoryTree, final String fromDirName)
			throws IOException {
		final List<String> sqlScripts = new ArrayList<String>();
		for (final Entry<String, File> dirEntry : directoryTree.entrySet()) {
			//System.out.println("Processing dir: " + dirEntry.getKey());
			final File runFile = findFile(dirEntry.getValue(), "run");
			if (fromDirName.compareTo(dirEntry.getKey()) <= 0) {
				if (runFile == null) {
					System.out.println("No run file found in directory: " + dirEntry.getKey());
					for (final File file : dirEntry.getValue().listFiles()) {
						if (file.getName().endsWith(".sql")) {
							sqlScripts.add(file.getAbsolutePath());
						}
					}
				} else {
					addSpecifiedSQLScripts(sqlScripts, runFile);
				}
			}
		}
		return sqlScripts;
	}

	private static void addSpecifiedSQLScripts(final List<String> sqlScripts, final File runFile) throws IOException {
		final String dirName = runFile.getParentFile().getAbsolutePath();
		final String contents = FileUtils.readFile(runFile.getAbsolutePath());
		final String[] lines = contents.split("\n");
		for (final String line : lines) {
			final String sqlFilename = findSQLFilename(line);
			if (sqlFilename != null && sqlFilename.length() > 0) {
				final String absoluteFilename = StringAppender.append(dirName, "/", sqlFilename);
				final File file = new File(absoluteFilename);
				if (file.exists()) {
					sqlScripts.add(absoluteFilename);
				}
			}
		}
	}

	private static String findSQLFilename(String line) {
		final String trimmedLine = line.trim();
		if (line.startsWith("mysql")) {
			final String suffix = StringUtils.substringAfterLast(trimmedLine, "<").trim();
			final int posOfOutPipe = suffix.indexOf('>');
			return posOfOutPipe > 0 ? suffix.substring(0, posOfOutPipe).trim() : suffix;
		} else if (line.startsWith("execute")) {
			final String fullSuffix = StringUtils.substringAfterLast(trimmedLine, "\"").trim();
			final int posOfOutPipe = fullSuffix.indexOf('>');
			return posOfOutPipe > 0 ? fullSuffix.substring(0, posOfOutPipe).trim() : fullSuffix;
		}
		return null;
	}

	private static File findFile(File dir, String filename) {
		for (final File file : dir.listFiles()) {
			if (file.getName().equals(filename)) {
				return file;
			}
		}
		return null;
	}

}
