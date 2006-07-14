package pt.utl.ist.fenix.tools.html;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Stack;

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
    private static int writtenFiles = 0;

    public static void main(String[] args) {
        final String pathToVerify = "/home/marvin/workspace/fenix_head/jsp";
        try {
            long start = System.currentTimeMillis();
            validate(new File(pathToVerify));
            long end = System.currentTimeMillis();
            System.out.println("Process took:  " + (end - start) + " ms.");
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            System.out.println("\nProcessed " + numDirs + " dirs.");
            System.out.println("Processed " + numFiles + " files.");
            System.out.println("   jsp: " + jspFiles);
            System.out.println("   html: " + htmlFiles);
            System.out.println("   css: " + cssFiles);
            System.out.println("   image: " + imageFiles);
            System.out.println("   javascript: " + jsFiles);
            System.out.println("   text: " + textFiles);
            System.out.println("   sound: " + soundFiles);
            System.out.println("   other: " + otherFiles);
            System.out.println("Wrote " + writtenFiles + " files.");
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
                try {
                	validateFile(file);
                } catch (StringIndexOutOfBoundsException ex) {
                	System.out.println("Error processing file: " + file.getAbsolutePath() + "\n\n");
                	throw ex;
                }
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
        final String originalFileContents = readFile(file);
        final StringBuilder stringBuilder = new StringBuilder();
        validateJSPFile(stringBuilder, originalFileContents, 0);
        final String processedFileContents = stringBuilder.toString();
        if (!processedFileContents.equals(originalFileContents)) {
            writtenFiles++;
//            writeFile(file, processedFileContents);
//            System.out.println("file: " + file.getAbsolutePath() + " " + originalFileContents.length() + " --> " + processedFileContents.length());
        }
        //System.out.println("file: " + file.getAbsolutePath());
    }

    private final static String[] TAGS = new String[] {
    	"html:text",
    	"html:button",
    	"html:cancel",
    	"html:checkbox",
    	"html:file",
    	"html:hidden",
    	"html:image",
    	"html:img",
    	"html:multibox",
    	"html:password",
    	"html:radio",
    	"html:reset",
    	"html:select",
    	"html:submit",
    	"html:text",
    	"html:textarea"
    };

    private static int findToken(final String contents, final int offset) {
    	final int[] tagPositions = new int[TAGS.length];
    	for (int i = 0; i < TAGS.length; i++) {
    		tagPositions[i] = findIndexOf(contents, offset, TAGS[i]);
    	}
        return min(tagPositions);
    }

    private static String determineTag(final String contents, final int offset) {
    	for (final String tag : TAGS) {
//    		System.out.println("tag: " + tag);
    		if (isTag(contents, offset, tag)) {
    			return tag;
    		}
    	}
    	return null;
    }

    private static void validateJSPFile(final StringBuilder buffer, final String contents, final int offset) {
        final int tokenIndex = findToken(contents, offset);
        if (tokenIndex >= 0) {
        	buffer.append(contents.substring(offset, tokenIndex));
            final int nextOffSet;
            final String tag = determineTag(contents, tokenIndex);
            if (tag == null) {
                nextOffSet = tokenIndex + 1;
                buffer.append(contents.substring(tokenIndex, nextOffSet));            	
            } else {
            	nextOffSet = processTag(buffer, contents, tokenIndex, tag);
            }
            validateJSPFile(buffer, contents, nextOffSet);
        } else {
        	buffer.append(contents.substring(offset));
        }
    }

    private static boolean isTag(final String contents, final int offset, final String prefix) {
    	final int nextCharPos = offset + prefix.length();
    	if (contents.length() >= nextCharPos + 1) {
    		char nextChar = contents.charAt(nextCharPos);
    		return contents.startsWith(prefix, offset) && (nextChar == ' ' || nextChar == '\t' || nextChar == '\n');
    	}
    	return false;
    }

    private static int processTag(final StringBuilder buffer, final String contents, final int offset, final String tag) {
        final int tagTerminationIndex = findTagTermination(contents, offset, tag);
        final int nextIndex;
        if (tagTerminationIndex > offset) {
        	nextIndex = tagTerminationIndex;
            if (containsAlternative(contents, offset, tagTerminationIndex)) {
                buffer.append(contents.substring(offset, tagTerminationIndex));
            } else {
                final String propertyName = findPropertyName(contents, offset, tagTerminationIndex);
                buffer.append(tag);
                buffer.append(" bundle=\"ALT_RESOURCES\" altKey=\"alt.");
                buffer.append(propertyName);
                buffer.append('"');
                buffer.append(contents.substring(offset + tag.length(), tagTerminationIndex));
            }
        } else {
        	nextIndex = offset + 1;
        	buffer.append(contents.substring(offset, nextIndex));
        }
        return nextIndex;
    }

    private static String findPropertyName(final String contents, final int offset, final int tagTerminationIndex) {
        final int propertyPos = contents.indexOf("property=", offset);
        final int startIndex = propertyPos + 10;
        int endIndex;
        for (endIndex = startIndex; contents.charAt(endIndex) != '"' && contents.charAt(endIndex) != '\''; endIndex++);
        return contents.substring(startIndex, endIndex);
    }

    private static boolean containsAlternative(final String contents, final int offset, final int tagTerminationIndex) {
        final int altPos = findIndexOf(contents, offset, "alt=");
        final int altKeyPos = findIndexOf(contents, offset, "altKey=");
        final int pos = min(altPos, altKeyPos);
        return pos >= 0 && pos < tagTerminationIndex;
    }

    private static int findTagTermination(final String contents, final int offset, final String tag) {
        Stack<Character> charStack = new Stack<Character>(); 
        for (int i = offset + tag.length(); i < contents.length(); i++) {
            char c = contents.charAt(i);
            if (isControleCharacter(c)) {
                if (charStack.isEmpty() && c == '>') {
                    return i + 1;
                } else {
                    if (charStack.isEmpty()) {
                        charStack.push(Character.valueOf(c));
                    } else {
                        char firstElement = charStack.firstElement().charValue();
                        if (matchingControlCharacters(firstElement, c)) {
                            charStack.pop();
                        } else {
                            charStack.push(Character.valueOf(c));
                        }
                    }
                }
            }
        }
        return -1;
    }

    private static boolean matchingControlCharacters(final char fc, final char lc) {
        return (fc == '"' && lc == '"') || (fc == '\'' && lc == '\'') || (fc == '<' && lc == '>');
    }

    private static boolean isControleCharacter(final char c) {
        return c == '"' || c == '\'' || c == '<' || c == '>';
    }

    private static int findIndexOf(final String contents, final int offset, final String token) {
        final int index = contents.indexOf(token, offset);
        return index == -1 ? Integer.MAX_VALUE : index;
    }

    private static int min(int... is) {
        if (is.length == 0) {
            throw new IllegalArgumentException("");
        } else if (is.length == 1) {
            return normalize(is[0]);
        } else {
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < is.length; i++) {
                min = Math.min(min, is[i]);
            }
            return normalize(min);
        }
    }

    private static int normalize(final int min) {
        return min == Integer.MAX_VALUE ? -1 : min;
    }

    public static String readFile(final File file) throws IOException {
        final FileReader fileReader = new FileReader(file);
        char[] buffer = new char[4096];
        final StringBuilder fileContents = new StringBuilder();
        for (int n = 0; (n = fileReader.read(buffer)) != -1; fileContents.append(buffer, 0, n));
        fileReader.close();
        return fileContents.toString();
    }

    private static void writeFile(final File file, final String content) throws IOException {
        final FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.write(content);
        fileWriter.close();
    }

}
