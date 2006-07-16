package pt.utl.ist.fenix.tools.html;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringAppender;

public class JspHtmlValidator {

	private static final Logger logger = Logger.getLogger(JspHtmlValidator.class.getName());

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
    private static int fixedTags = 0;
    private static int skippedTags = 0;
    private static int alreadyCorrectTags = 0;

    private static File cf = null;

    private static Map<String, String> altKeys = new HashMap<String, String>();

    public static void main(String[] args) {
    	final String projectDir = "/home/marvin/workspace/fenix_head";
        try {
            long start = System.currentTimeMillis();
            validate(new File(projectDir + "/jsp"));
            long end = System.currentTimeMillis();
            generateAltKeyProperties(projectDir + "/config/resources", "HtmlAltResources");
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
            System.out.println("Initially " + alreadyCorrectTags + " correct tags.");
            System.out.println("Wrote " + writtenFiles + " files.");
            System.out.println("Created " + altKeys.size() + " distinct keys");            
            System.out.println("Fixed " + fixedTags + " tags.");
            System.out.println("Skipped " + skippedTags + " tags.");            
            System.exit(0);
        }
    }

    private static void generateAltKeyProperties(final String destinationDirPath, final String bundleName) throws IOException {
    	generateAltKeyProperties(destinationDirPath, bundleName, "pt");
    	generateAltKeyProperties(destinationDirPath, bundleName, "en");
	}

	private static void generateAltKeyProperties(final String destinationDirPath, final String bundleName, final String language) throws IOException {
		final String filename = StringAppender.append(destinationDirPath, "/", bundleName, "_", language, ".properties");
		final File file = new File(filename);
		final Properties properties = new Properties();
		if (file.exists()) {
			final FileInputStream fileInputStream = new FileInputStream(file);
			properties.load(fileInputStream);
			fileInputStream.close();
		} else {
			file.createNewFile();
		}
		for (final Entry<String, String> altKeyEntry : altKeys.entrySet()) {
			final String key = altKeyEntry.getKey();
			if (!properties.containsKey(key)) {
				final String value = altKeyEntry.getValue();
				properties.put(key, value);
			}
		}
		final FileOutputStream fileOutputStream = new FileOutputStream(file, false);
		properties.store(fileOutputStream, null);
		fileOutputStream.close();
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
                	cf = file;
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
        }
    }

    private final static String[] TAGS = new String[] {
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
    	"html:textarea",
    	"input"
    };

    private static int findToken(final String contents, final int offset) {
    	final int[] tagPositions = new int[TAGS.length];
    	for (int i = 0; i < TAGS.length; i++) {
    		tagPositions[i] = findIndexOf(contents, offset, "<" + TAGS[i]);
    	}
        return min(tagPositions);
    }

    private static String determineTag(final String contents, final int offset) {
    	for (final String tag : TAGS) {
    		if (isTag(contents, offset, tag)) {
    			return tag;
    		}
    	}
    	return null;
    }

    private static void validateJSPFile(final StringBuilder buffer, final String contents, final int offset) {
        final int tokenIndex = findToken(contents, offset);
        if (tokenIndex >= 0) {
        	buffer.append(contents.substring(offset, tokenIndex + 1));
            final int nextOffSet;
            final String tag = determineTag(contents, tokenIndex + 1);
            if (tag == null) {
                nextOffSet = tokenIndex + 2;
                buffer.append(contents.substring(tokenIndex + 1, nextOffSet));            	
            } else {
            	nextOffSet = processTag(buffer, contents, tokenIndex + 1, tag);
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
        final int tagTerminationIndex = findTagTermination(contents, offset + tag.length(), '>');
        final int nextIndex;
        if (tagTerminationIndex > offset) {
        	nextIndex = tagTerminationIndex;
            if (containsAlternative(contents, offset, tagTerminationIndex)) {
            	alreadyCorrectTags++;
                buffer.append(contents.substring(offset, tagTerminationIndex));
            } else {
            	final String whereToLookForLabel = tag.equals("input") ? "name=" : "property=";
                final String propertyName = findPropertyName(contents, offset, whereToLookForLabel, tagTerminationIndex);
                final String tagPropertyNamePrefix = getTagPropertyNamePrefix(tag);
                final String normalizedPropertyValue = getPropertyName(propertyName, tag);
                	
                buffer.append(tag);
                if (isCalculatedValue(normalizedPropertyValue)) {
                	buffer.append(" alt=\"");
                	buffer.append(normalizedPropertyValue);
                } else {
                	if (tag.equals("input")) {
                		buffer.append(" alt=\"");
                	} else {
                		buffer.append(" bundle=\"HTMLALT_RESOURCES\" altKey=\"");
                	}
                	final String efectivePropertyName = StringAppender.append(tagPropertyNamePrefix, ".", normalizedPropertyValue);
                	altKeys.put(efectivePropertyName, normalizedPropertyValue);
                	buffer.append(efectivePropertyName);
                }
                	
                buffer.append('"');
                buffer.append(contents.substring(offset + tag.length(), tagTerminationIndex));
                fixedTags++;
//                	if (isCalculatedValue(normalizedPropertyValue)) {
//                	if (cf.getAbsolutePath().equals("/home/marvin/workspace/fenix_head/jsp/student/onlineTests/showStudentTest_bd.jsp")) {
//                		System.out.println("\n" + cf.getAbsolutePath());
//                		System.out.println("o: [" + contents.substring(offset, nextIndex) + "]");
//                		if (isCalculatedValue(normalizedPropertyValue)) {
//                			System.out.println("r: [" + tag + " alt=\"" + normalizedPropertyValue + '"' + contents.substring(offset + tag.length(), tagTerminationIndex) + "]");                			
//                		} else if (tag.equals("input")) {
//                    		System.out.println("r: [" + tag + " alt=\"" + tagPropertyNamePrefix + "." + normalizedPropertyValue + '"' + contents.substring(offset + tag.length(), tagTerminationIndex) + "]");
//                    	} else {
//                    		System.out.println("r: [" + tag + " bundle=\"HTMLALT_RESOURCES\" altKey=\"" + tagPropertyNamePrefix + "." + normalizedPropertyValue + '"' + contents.substring(offset + tag.length(), tagTerminationIndex) + "]");
//                    	}
//                	}
            }
        } else {
        	nextIndex = offset + 1;
        	buffer.append(contents.substring(offset, nextIndex));
        }
        return nextIndex;
    }

    private static boolean isCalculatedValue(final String normalizedPropertyValue) {
    	return normalizedPropertyValue.indexOf("<%=") >= 0;
	}

	private static String getPropertyName(final String propertyName, final String tag) {
    	return propertyName == null ? getTagPropertyNamePrefix(tag) : propertyName;
	}

    private static String getTagPropertyNamePrefix(final String tag) {
		final int colinPos = tag.indexOf(':');
		return colinPos > 0 ? tag.substring(colinPos + 1) : tag;
    }

	private static String findPropertyName(final String contents, final int offset, final String whereToLookForLabel, final int tagTerminationIndex) {
        final int propertyPos = contents.indexOf(whereToLookForLabel, offset);
        if (propertyPos >= 0 && propertyPos < tagTerminationIndex) {
        	final int startIndex = propertyPos + whereToLookForLabel.length() + 1;
        	final int propertyTerminationIndex = findTagTermination(contents, startIndex, contents.charAt(propertyPos + whereToLookForLabel.length()));
        	if (propertyPos < propertyTerminationIndex && propertyTerminationIndex < tagTerminationIndex) {
        		return contents.substring(startIndex, propertyTerminationIndex - 1);
        	}
        }
    	return null;
    }

    private static boolean containsAlternative(final String contents, final int offset, final int tagTerminationIndex) {
        final int altPos = findIndexOf(contents, offset, "alt=");
        final int altKeyPos = findIndexOf(contents, offset, "altKey=");
        final int pos = min(altPos, altKeyPos);
        return pos >= 0 && pos < tagTerminationIndex;
    }

    private static int findTagTermination(final String contents, final int offset, final char terminationChar) {
        Stack<Character> charStack = new Stack<Character>();
        for (int i = offset; i < contents.length(); i++) {
            char c = contents.charAt(i);
            if (isControleCharacter(c)) {
                if (charStack.isEmpty() && c == terminationChar) {
                	return i + 1;
                } else {
                    if (charStack.isEmpty()) {
                        charStack.push(Character.valueOf(c));
                    } else {
                        char firstElement = charStack.peek().charValue();
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
        for (int i = 0; i < buffer.length; buffer[i++] = 0);
        final StringBuilder fileContents = new StringBuilder();
        for (int n = 0; (n = fileReader.read(buffer)) != -1; ) {
        	fileContents.append(buffer, 0, n);
        	for (int i = 0; i < buffer.length; buffer[i++] = 0);
        }
        fileReader.close();
        return fileContents.toString();
    }

    private static void writeFile(final File file, final String content) throws IOException {
        final FileWriter fileWriter = new FileWriter(file, false);
        fileWriter.write(content);
        fileWriter.close();
    }

}
