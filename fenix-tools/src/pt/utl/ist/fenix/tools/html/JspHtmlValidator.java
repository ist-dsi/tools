package pt.utl.ist.fenix.tools.html;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

public class JspHtmlValidator {

    private static int numFiles = 0;
    private static int numDirs = 0;
    private static int jspFiles = 0;
    private static int htmlFiles = 0;
    private static int cssFiles = 0;
    private static int imageFiles = 0;
    private static int jsFiles = 0;
    private static int textFiles = 0;
    private static int soundFiles = 0;
    private static int otherFiles = 0;

    public static void main(String[] args) {
        final String pathToVerify = "/home/marvin/workspace/fenix/jsp";
        try {
            long start = System.currentTimeMillis();
            validate(new File(pathToVerify));
            long end = System.currentTimeMillis();
            System.out.println("Process took:  " + (end - start) + " ms.");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            System.out.println("Processed " + numDirs + " dirs.");
            System.out.println("Processed " + numFiles + " files.");
            System.out.println("   jsp: " + jspFiles);
            System.out.println("   html: " + htmlFiles);
            System.out.println("   css: " + cssFiles);
            System.out.println("   image: " + imageFiles);
            System.out.println("   javascript: " + jsFiles);
            System.out.println("   text: " + textFiles);
            System.out.println("   sound: " + soundFiles);
            System.out.println("   other: " + otherFiles);
            System.exit(0);
        }
    }

    private static void validate(final File file) throws IOException {
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                numDirs++;
                for (final File subFile : file.listFiles()) {
                    validate(subFile);
                }
            } else if (file.isFile()) {
                numFiles++;
                validateFile(file);
            } else {
                throw new IllegalArgumentException("unknown.file.type: " + file);
            }
        }
    }

    private static void validateFile(final File file) throws IOException {
        final String filename = file.getName();
        final String extension = StringUtils.substringAfterLast(filename, ".");
        if (extension == null || extension.length() == 0) {
            otherFiles++;
        } else if (extension.equalsIgnoreCase("jsp")) {
            jspFiles++;
            validateJSPFile(file);
        } else if (extension.equalsIgnoreCase("css")) {
            cssFiles++;
        } else if (extension.equalsIgnoreCase("html") || extension.equalsIgnoreCase("htm")) {
            htmlFiles++;
        } else if (extension.equalsIgnoreCase("gif")
                || extension.equalsIgnoreCase("jpg")
                || extension.equalsIgnoreCase("png")
                || extension.equalsIgnoreCase("bmp")) {
            imageFiles++;
        } else if (extension.equalsIgnoreCase("js")) {
            jsFiles++;
        } else if (extension.equalsIgnoreCase("txt")
                || extension.equalsIgnoreCase("properties")) {
            textFiles++;
        } else if (extension.equalsIgnoreCase("mid")) {
            soundFiles++;
        } else {
            otherFiles++;
        }
    }

    private static void validateJSPFile(final File file) throws IOException {
        final String string = readFile(file);
        final StringBuilder stringBuilder = new StringBuilder();
    }

    public static String readFile(final File file) throws IOException {
        final FileReader fileReader = new FileReader(file);
        char[] buffer = new char[4096];
        final StringBuilder fileContents = new StringBuilder();
        for (int n = 0; (n = fileReader.read(buffer)) != -1; fileContents.append(buffer, 0, n));
        fileReader.close();
        return fileContents.toString();
    }

}
