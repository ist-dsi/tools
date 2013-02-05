package pt.utl.ist.fenix.tools.resources;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EnResourceFileCharCountForTranslations {

    private static final boolean WRITES_ALLOWED = false;

    private static final String RESOURCES_FOLDER = "./resources";
    private static final String RESOURCES_TRANSLATED_FOLDER = "./resourcesTranslated";
    private static final String RESOURCES_FILE_EXTENSION = ".properties";

    private static final String RESOURCES_PT_MARKER = "_pt";
    private static final String RESOURCES_EN_MARKER = "_en";

    private static final Map<String, String> PT_RESOURCE_FILES = new HashMap<String, String>();
    private static final Map<String, String> EN_RESOURCE_FILES = new HashMap<String, String>();
    private static final Map<String, String> EN_TRANSLATED_RESOURCE_FILES = new HashMap<String, String>();

    private static int totalEnglishCharCount = 0;
    private static int totalPortugueseCharCount = 0;
    private static int totalTranslatedEnglishCharCount = 0;

    public static void main(String[] args) {
        execute();
    }

    public static void execute() {
        countEnAndPtChars();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("#################################################################################################");
        System.out.println();
        System.out.println("#################################################################################################");
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        countPreviouslyEnTranslatedChars();
    }

    private static void countEnAndPtChars() {
        File file = new File(RESOURCES_FOLDER);
        FilenameFilter ptFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(RESOURCES_PT_MARKER + RESOURCES_FILE_EXTENSION);
            }
        };
        FilenameFilter enFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(RESOURCES_EN_MARKER + RESOURCES_FILE_EXTENSION);
            }
        };

        for (String resourceFilename : file.list(ptFilter)) {
            PT_RESOURCE_FILES.put(getResourceFileSimpleName(resourceFilename), RESOURCES_FOLDER + "/" + resourceFilename);
        }
        for (String resourceFilename : file.list(enFilter)) {
            EN_RESOURCE_FILES.put(getResourceFileSimpleName(resourceFilename), RESOURCES_FOLDER + "/" + resourceFilename);
        }
        System.out.println("Found " + EN_RESOURCE_FILES.size() + " _en files.");
        System.out.println("Found " + PT_RESOURCE_FILES.size() + " _pt files.");

        int fileCount = 400;
        for (String enFilename : EN_RESOURCE_FILES.values()) {
            try {
                final InputStreamReader inputStreamReader = new FileReader(enFilename);
                final String contents = readFile(inputStreamReader);
                inputStreamReader.close();

                parseResourceContents(getResourceFileSimpleName(enFilename), contents);
                System.out.println("partialEnglishCharCount: " + totalEnglishCharCount);
                System.out.println("partialPortugueseCharCount: " + totalPortugueseCharCount);
                if (--fileCount == 0) {
                    break;
                }
            } catch (final IOException e) {
                e.printStackTrace();
                return;
            }
        }

        System.out.println();
        System.out.println("totalEnglishCharCount: " + totalEnglishCharCount);
        System.out.println("totalPortugueseCharCount: " + totalPortugueseCharCount);
    }

    private static void countPreviouslyEnTranslatedChars() {
        File file = new File(RESOURCES_TRANSLATED_FOLDER);
        FilenameFilter enFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(RESOURCES_EN_MARKER + RESOURCES_FILE_EXTENSION);
            }
        };

        for (String resourceFilename : file.list(enFilter)) {
            EN_TRANSLATED_RESOURCE_FILES.put(getResourceFileSimpleName(resourceFilename), RESOURCES_TRANSLATED_FOLDER + "/"
                    + resourceFilename);
        }
        System.out.println("Found " + EN_TRANSLATED_RESOURCE_FILES.size() + " _en previously translated files.");

        int fileCount = 400;
        for (String enFilename : EN_TRANSLATED_RESOURCE_FILES.values()) {
            try {
                final InputStreamReader inputStreamReader = new FileReader(enFilename);
                final String contents = readFile(inputStreamReader);
                inputStreamReader.close();

                parseTranslatedResourceContents(getResourceFileSimpleName(enFilename), contents);
                System.out.println("partialTranslatedEnglishCharCount: " + totalTranslatedEnglishCharCount);
                if (--fileCount == 0) {
                    break;
                }
            } catch (final IOException e) {
                e.printStackTrace();
                return;
            }
        }

        System.out.println();
        System.out.println("totalTranslatedEnglishCharCount: " + totalTranslatedEnglishCharCount);
    }

    private static void parseResourceContents(String resourceFileSimpleName, String contents) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Parsing file: " + resourceFileSimpleName);
        String[] lines = contents.split("\n");
        System.out.println("Found " + lines.length + " lines");
        int lineNumber = 0;
        while (lineNumber < lines.length) {
            String line = lines[lineNumber];
            if (shouldIgnoreLine(line, true)) {
                lineNumber++;
                continue;
            }

            String[] messageSplit = line.split("=", 2);
            if (messageSplit.length == 1) {
                messageSplit = line.split(":", 2);
            }
            String label = messageSplit[0].trim();

            String text = messageSplit[1];
            while (text.endsWith("\\")) {
                Integer numOfSlashes = countNumOfEndingSlashes(text);
                if ((numOfSlashes % 2) == 0) {
                    // Pair number of slashes means all the slashes are escaped
                    // so the message does not continue to the next line.
                    break;
                }
                text = text.trim();
                text = text.substring(0, text.length() - 1);
                line = lines[++lineNumber];

                text += line.trim();
            }

            if (isInPortuguese(resourceFileSimpleName, label, text)) {
                totalPortugueseCharCount += text.length();
            } else {
                totalEnglishCharCount += text.length();
            }
            lineNumber++;
        }
    }

    private static void parseTranslatedResourceContents(String resourceFileSimpleName, String contents) {
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println("Parsing file: " + resourceFileSimpleName);
        String[] lines = contents.split("\n");
        System.out.println("Found " + lines.length + " lines");
        int lineNumber = 0;
        while (lineNumber < lines.length) {
            String line = lines[lineNumber];
            if (shouldIgnoreLine(line, true)) {
                lineNumber++;
                continue;
            }

            String[] messageSplit = line.split("=", 2);
            if (messageSplit.length == 1) {
                messageSplit = line.split(":", 2);
            }
            String label = messageSplit[0].trim();

            String text = messageSplit[1];
            while (text.endsWith("\\")) {
                Integer numOfSlashes = countNumOfEndingSlashes(text);
                if ((numOfSlashes % 2) == 0) {
                    // Pair number of slashes means all the slashes are escaped
                    // so the message does not continue to the next line.
                    break;
                }
                text = text.trim();
                text = text.substring(0, text.length() - 1);
                line = lines[++lineNumber];

                text += line.trim();
            }

            totalTranslatedEnglishCharCount += text.length();

            lineNumber++;
        }
    }

    private static boolean isInPortuguese(String resourceFileSimpleName, String label, String text) {
        String ptResourceFilename = PT_RESOURCE_FILES.get(resourceFileSimpleName);
        try {
            final InputStreamReader inputStreamReader = new FileReader(ptResourceFilename);
            final String contents = readFile(inputStreamReader);
            inputStreamReader.close();

            String ptText = findTextForLabel(ptResourceFilename, contents, label, text);
            return ptText.equals(text);
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    private static String findTextForLabel(String ptResourceFilename, String contents, String labelToFind, String textInEnglish) {
        String[] lines = contents.split("\n");
        int lineNumber = 0;
        while (lineNumber < lines.length) {
            String line = lines[lineNumber];
            if (shouldIgnoreLine(line, false)) {
                lineNumber++;
                continue;
            }

            String[] messageSplit = line.split("=", 2);
            if (messageSplit.length == 1) {
                messageSplit = line.split(":", 2);
            }
            String label = messageSplit[0].trim();

            if (label.equals(labelToFind)) {
                String text = messageSplit[1];
                while (text.endsWith("\\")) {
                    Integer numOfSlashes = countNumOfEndingSlashes(text);
                    if ((numOfSlashes % 2) == 0) {
                        // Pair number of slashes means all the slashes are escaped
                        // so the message does not continue to the next line.
                        break;
                    }
                    text = text.trim();
                    text = text.substring(0, text.length() - 1);
                    line = lines[++lineNumber];

                    text += line.trim();
                }
                return text;
            }
            lineNumber++;
        }

        System.out.println("WARNING! THIS LABEL WAS NOT FOUND IN THE PT FILE: " + labelToFind);
        if (WRITES_ALLOWED) {
            try {
                final FileWriter writer = new FileWriter(ptResourceFilename, true);
                writer.write('\n' + labelToFind + " = " + textInEnglish);
                writer.close();
                System.out.println("LABEL WRITTEN BACK IN THE PT FILE FROM THE EN FILE: " + labelToFind);
            } catch (final IOException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }

        return "";
    }

    private static int countNumOfEndingSlashes(String text) {
        int numOfSlahes = 0;
        int charNum = text.length() - 1;
        while (charNum >= 0) {
            char c = text.charAt(charNum);
            if (c == '\\') {
                numOfSlahes++;
            } else {
                break;
            }
            charNum--;
        }
        return numOfSlahes;
    }

    private static String getResourceFileSimpleName(String resourceFilename) {
        String[] directorySplit = resourceFilename.split("/");
        return directorySplit[directorySplit.length - 1].split("_")[0];
    }

    private static boolean shouldIgnoreLine(String line, boolean printDebugInfo) {
        if (line.trim().length() == 0) {
            return true;
        }
        if (line.trim().startsWith("#")) {
            return true;
        }
        if ((line.indexOf("#") != -1) && (line.indexOf("#") < line.indexOf("="))) {
            if (printDebugInfo) {
                System.out.println("Found a malformed comment, ignoring. Line: " + line);
            }
            return true;
        }
        String[] messageSplit = line.split("=", 2);
        if (messageSplit.length == 1) {
            messageSplit = line.split(":", 2);
            if (messageSplit.length == 1) {
                if (printDebugInfo) {
                    System.out.println("Found a malformed line, ignoring. Line: " + line);
                }
            }
            return true;
        }
        return false;
    }

    private static String readFile(final InputStream inputStream) throws IOException {
        return readFile(new InputStreamReader(inputStream));
    }

    private static String readFile(final InputStreamReader fileReader) throws IOException {
        try {
            char[] buffer = new char[4096];
            final StringBuilder fileContents = new StringBuilder();
            for (int n = 0; (n = fileReader.read(buffer)) != -1; fileContents.append(buffer, 0, n)) {
                ;
            }
            return fileContents.toString();
        } finally {
            fileReader.close();
        }
    }
}
