package pt.utl.ist.fenix.tools.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.FileUtils;

public class RegisterSQLScript {

	public static void main(String[] args) {
		final File baseDir = new File(args[0]);
		if (!baseDir.exists()) {
			System.err.println("Base directory of sql scripts: " + args[0] + " does not exist!");
		} else if (baseDir.isDirectory()) {
			System.err.println("Base directory of sql scripts: " + args[0] + " is not a directory!");
		} else {
			final File file = new File(args[1]);
			if (!file.exists()) {
				System.err.println("File: " + args[1] + " does not exist!");
			} else if (!file.isFile()) {
				System.err.println("File: " + args[1] + " is not a file!");
			} else {
				final DateTime dateTime = new DateTime();
				final String destinationDirName = getDestinationDir(baseDir, dateTime);
				final File destinationDir = new File(destinationDirName);
				if (!destinationDir.exists()) {
					destinationDir.mkdirs();
				}
				final String destinationFileName = getDestinationFile(destinationDir, dateTime, file);
				final File destinationFile = new File(destinationFileName);
				if (destinationFile.exists()) {
					System.out.println("File " + destinationFileName + " already exists!");
				} else {
					try {
						final FileInputStream fileInputStream = new FileInputStream(file);
						final FileOutputStream fileOutputStream = new FileOutputStream(destinationFile);
						FileUtils.copy(fileInputStream, fileOutputStream);
					} catch (final IOException ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		System.exit(0);
	}

	private static String getDestinationFile(final File destinationDir, final DateTime dateTime, final File file) {
		return destinationDir.getAbsolutePath() + File.separatorChar + dateTime.toString("yyyyMMddHHmmss") + "_" + file.getName();
	}

	private static String getDestinationDir(final File baseDir, final DateTime dateTime) {
		final int year = dateTime.getYear();
		final int month = dateTime.getMonthOfYear();
		final int day = dateTime.getDayOfMonth();
		return baseDir.getAbsolutePath() + File.separatorChar + "R" + year + File.separatorChar + "R" + year + "-" + month
				+ File.separatorChar + "R" + year + "-" + month + "-" + day;
	}

}
