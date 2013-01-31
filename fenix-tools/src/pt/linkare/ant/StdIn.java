package pt.linkare.ant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.input.InputHandler;
import org.apache.tools.ant.input.InputRequest;

import pt.linkare.ant.propreaders.AbstractPropertyReader;

public class StdIn {

	private boolean graphicsAvailable = !java.awt.GraphicsEnvironment.isHeadless();

	private BufferedReader bis = null;

	private Project proj = null;

	public static final String CRLF = System.getProperty("line.separator");

	private String encoding = "iso-8859-1";

	private StdIn(String encoding) throws UnsupportedEncodingException {
		this.encoding = encoding;
		bis = new BufferedReader(new InputStreamReader(System.in, encoding));
	}

	private StdIn(Project proj, String encoding) {
		this.proj = proj;
		this.encoding = encoding;
	}

	private static StdIn instance = null;

	public static synchronized StdIn getInstance(String encoding) throws UnsupportedEncodingException {
		if (instance == null) {
			instance = new StdIn(encoding);
		}
		return instance;
	}

	public static synchronized StdIn getInstance(Project p, String encoding) {
		if (instance == null) {
			instance = new StdIn(p, encoding);
		}
		return instance;
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readBooleanOption(java.lang.String,
	     *      java.lang.String, java.lang.String)
	     */
	public boolean readBooleanOption(String message, String yesOption, String noOption) {

		StringBuilder fullMessage = new StringBuilder(message);
		fullMessage.append(" [" + yesOption + "/" + noOption + "] :");

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine != null) {
					if (readLine.equals(yesOption)) {
						return true;
					} else if (readLine.equals(noOption)) {
						return false;
					} else {
						System.out.println("Invalid value. Valid Options are: [" + yesOption + "," + noOption + "]");
					}
				}
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readString(java.lang.String, int)
	     */
	public String readString(String message, int minLength) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if ((readLine == null && minLength > 0) || readLine.length() < minLength) {
					System.out.println(readLine == null ? "You must specify a value!" : "The specified value must have at least "
							+ minLength + " characters!");
					continue;
				}
				return readLine;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readPrivateString(java.lang.String, int)
	     */
	public String readPrivateString(String message, int minLength) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readPrivateStringFromInput(fullMessage.toString());
				if ((readLine == null && minLength > 0) || readLine.length() < minLength) {
					System.out.println(readLine == null ? "You must specify a value!" : "The specified value must have at least "
							+ minLength + " characters!");
					continue;
				}
				return readLine;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOption(java.lang.String,
	     *      java.lang.String[])
	     */
	public int readMenuOption(String message, String[] optionsMessages) {

		StringBuilder fullMessage = new StringBuilder(message);
		int currentOptionCount = 0;
		for (String currentOptionMessage : optionsMessages) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(currentOptionCount).append(" - ").append(currentOptionMessage);
		}

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					System.out.println(readLine == null ? "You must specify an option!" : "Valid options are between 1 and "
							+ currentOptionCount);
					continue;
				}
				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount);
					continue;
				}

				return valueSelected - 1;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOptionOrQuit(java.lang.String,
	     *      java.lang.String[], java.lang.String)
	     */
	public int readMenuOptionOrQuit(String message, String[] optionsMessages, String quitOption) {

		StringBuilder fullMessage = new StringBuilder(message);
		int currentOptionCount = 0;
		for (String currentOptionMessage : optionsMessages) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(currentOptionCount).append(" - ").append(currentOptionMessage);
		}
		fullMessage.append(CRLF).append(quitOption).append(" - Quit!");

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());

				if (readLine == null || readLine.length() <= 0) {
					System.out.println(readLine == null ? "You must specify an option!" : "Valid options are between 1 and "
							+ currentOptionCount);
					continue;
				}
				if (readLine != null && quitOption.equalsIgnoreCase(readLine)) {
					return -1;
				}

				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
					System.out.println("selected value read is=" + valueSelected);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount + " or " + quitOption);
					continue;
				}

				return valueSelected - 1;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readInteger(java.lang.String)
	     */
	public int readInteger(String message) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					System.out.println(readLine == null ? "You must specify a value!" : "You must specify an integer value!");
					continue;
				}
				int valueRead = 0;
				try {
					valueRead = Integer.parseInt(readLine);
					return valueRead;
				} catch (Exception e) {
					System.out.println("You must specify an integer value!");
				}
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readIntegerOrDefault(java.lang.String,
	     *      int)
	     */
	public int readIntegerOrDefault(String message, int defaultValue) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					return defaultValue;
				}
				int valueRead = 0;
				try {
					valueRead = Integer.parseInt(readLine);
					return valueRead;
				} catch (Exception e) {
					System.out.println("You must specify an integer value!");
				}
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readIntegerOrDefault(java.lang.String,
	     *      java.lang.String)
	     */
	public int readIntegerOrDefault(String message, String defaultValue) {
		return readIntegerOrDefault(message, Integer.parseInt(defaultValue));
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readStringOrDefault(java.lang.String,
	     *      java.lang.String)
	     */
	public String readStringOrDefault(String message, String defaultValue) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() == 0) {
					return defaultValue;
				}
				return readLine;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readPrivateStringOrDefault(java.lang.String,
	     *      java.lang.String)
	     */
	public String readPrivateStringOrDefault(String message, String defaultValue) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readPrivateStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() == 0) {
					return defaultValue;
				}
				return readLine;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readStringOrDefault(java.lang.String,
	     *      java.lang.String, int)
	     */
	public String readStringOrDefault(String message, String defaultValue, int minLength) {

		StringBuilder fullMessage = new StringBuilder(message);
		fullMessage.append((minLength > 0 ? (" " + "min - " + minLength + " chars") : "") + ": ");

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() == 0) {
					return defaultValue;
				} else if (minLength > 0 && readLine.length() < minLength) {
					System.out.println("The specified value must have at least " + minLength + " characters!");
					continue;
				}
				return readLine;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readBooleanOptionOrDefault(java.lang.String,
	     *      java.lang.String, java.lang.String, boolean)
	     */
	public boolean readBooleanOptionOrDefault(String message, String yesOption, String noOption, boolean defaultValue) {

		StringBuilder fullMessage = new StringBuilder(message);

		while (true) {
			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() == 0) {
					return defaultValue;
				}
				if (readLine != null) {
					if (readLine.equals(yesOption)) {
						return true;
					} else if (readLine.equals(noOption)) {
						return false;
					} else {
						System.out.println("Invalid value. Valid Options are:[" + noOption + "," + yesOption + "]");
					}
				}
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOptionOrQuitOrDefault(java.lang.String,
	     *      java.lang.String[], java.lang.String, int)
	     */
	public int readMenuOptionOrQuitOrDefault(String message, String[] optionsMessages, String quitOption, int defaultOption) {

		StringBuilder fullMessage = new StringBuilder(message);
		fullMessage.append(" [").append((defaultOption + 1)).append("]");
		int currentOptionCount = 0;
		for (String currentOptionMessage : optionsMessages) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(defaultOption == currentOptionCount ? "[" : " ").append(currentOptionCount)
					.append(" - ").append(currentOptionMessage).append(defaultOption == currentOptionCount ? "]" : "");
		}
		fullMessage.append(CRLF).append(quitOption).append(" - Quit!");

		while (true) {

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					return defaultOption;
				}
				if (readLine != null && quitOption.equalsIgnoreCase(readLine)) {
					return -1;
				}

				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount + " or " + quitOption);
					continue;
				}

				return valueSelected - 1;
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOption(pt.linkare.ant.MenuMessage)
	     */
	public String readMenuOption(MenuMessage menuMessage) {
		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(currentOptionCount).append(" - ").append(currentOptionMessage);
		}

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					System.out.println("You must select a value between 1 and " + currentOptionCount);
				}
				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount);
					continue;
				}

				return menuMessage.getOptionValues().get(valueSelected - 1);
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMultipleOptionOrDefault(pt.linkare.ant.MenuMessage,
	     *      java.lang.String)
	     */
	public String readMultipleOptionOrDefault(MenuMessage menuMessage, String propertyDefaultValue) {
		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		List<String> defaultValuesList = Arrays.asList(AbstractPropertyReader.splitValues(propertyDefaultValue));

		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			boolean isSelected = defaultValuesList.contains(menuMessage.getOptionValues().get(currentOptionCount - 1));
			fullMessage.append(CRLF).append(isSelected ? "[" : "").append(currentOptionCount).append(" - ")
					.append(currentOptionMessage).append(isSelected ? "]" : "");
		}

		fullMessage.append(CRLF)
				.append("You may select many options by choosing their number in comma separated values e.g: 1,2,3").append(CRLF);

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					return propertyDefaultValue;
				}
				String[] values = readLine.split(",");

				int[] valuesSelected = new int[values.length];
				for (int i = 0; i < values.length; i++) {
					try {
						valuesSelected[i] = Integer.parseInt(values[i]);
					} catch (Exception ignored) {

					}
					if (valuesSelected[i] == -1 || valuesSelected[i] < 1 || valuesSelected[i] > currentOptionCount) {
						System.out.println("Valid options are between 1 and " + currentOptionCount);
						continue;
					}
				}

				StringBuilder retVal = new StringBuilder();
				for (int i = 0; i < valuesSelected.length; i++) {
					if (i > 0) {
						retVal.append(",");
					}
					retVal.append(menuMessage.getOptionValues().get(valuesSelected[i] - 1));
				}

				return retVal.toString();
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOptionOrDefault(pt.linkare.ant.MenuMessage,
	     *      java.lang.String)
	     */
	public String readMenuOptionOrDefault(MenuMessage menuMessage, String propertyDefaultValue) {

		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			fullMessage.append(CRLF)
					.append(propertyDefaultValue.equals(menuMessage.getOptionValues().get(currentOptionCount - 1)) ? "[" : " ")
					.append(currentOptionCount).append(" - ").append(currentOptionMessage)
					.append(propertyDefaultValue.equals(menuMessage.getOptionValues().get(currentOptionCount - 1)) ? "]" : "");
		}

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					return propertyDefaultValue;
				}
				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount);
					continue;
				}

				return menuMessage.getOptionValues().get(valueSelected - 1);
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMultipleOption(pt.linkare.ant.MenuMessage)
	     */
	public String readMultipleOption(MenuMessage menuMessage) {
		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(currentOptionCount).append(" - ").append(currentOptionMessage);
		}
		fullMessage.append(CRLF)
				.append("You may select many options by choosing their number in comma separated values e.g: 1,2,3").append(CRLF);

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					System.out.println("You must choose at least one value between 1 and " + currentOptionCount
							+ " or q (quit with no selection)!");
					continue;
				}
				String[] values = readLine.split(",");

				int[] valuesSelected = new int[values.length];
				for (int i = 0; i < values.length; i++) {
					try {
						valuesSelected[i] = Integer.parseInt(values[i]);
					} catch (Exception ignored) {

					}
					if (valuesSelected[i] == -1 || valuesSelected[i] < 1 || valuesSelected[i] > currentOptionCount) {
						System.out.println("Valid options are between 1 and " + currentOptionCount);
						continue;
					}
				}

				StringBuilder retVal = new StringBuilder();
				for (int i = 0; i < valuesSelected.length; i++) {
					if (i > 0) {
						retVal.append(", ");
					}
					retVal.append(menuMessage.getOptionValues().get(valuesSelected[i] - 1));
				}

				return retVal.toString();
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMultipleOptionOrQuitOrDefault(pt.linkare.ant.MenuMessage,
	     *      java.lang.String)
	     */
	public String readMultipleOptionOrQuitOrDefault(MenuMessage menuMessage, String propertyDefaultValue) {
		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		List<String> defaultValuesList = Arrays.asList(AbstractPropertyReader.splitValues(propertyDefaultValue));

		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			boolean isSelected = defaultValuesList.contains(menuMessage.getOptionValues().get(currentOptionCount - 1));
			fullMessage.append(CRLF).append(isSelected ? "[" : "").append(currentOptionCount).append(" - ")
					.append(currentOptionMessage).append(isSelected ? "]" : "");
		}
		fullMessage.append(CRLF).append("q").append(" - ").append("Quit");
		fullMessage.append(CRLF)
				.append("You may select many options by choosing their number in comma separated values e.g: 1,2,3").append(CRLF);

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					return propertyDefaultValue;
				}
				if (readLine.equalsIgnoreCase("q")) {
					return null;
				}
				String[] values = readLine.split(",");

				int[] valuesSelected = new int[values.length];
				for (int i = 0; i < values.length; i++) {
					try {
						valuesSelected[i] = Integer.parseInt(values[i]);
					} catch (Exception ignored) {

					}
					if (valuesSelected[i] == -1 || valuesSelected[i] < 1 || valuesSelected[i] > currentOptionCount) {
						System.out.println("Valid options are between 1 and " + currentOptionCount);
						continue;
					}
				}

				StringBuilder retVal = new StringBuilder();
				for (int i = 0; i < valuesSelected.length; i++) {
					if (i > 0) {
						retVal.append(", ");
					}
					retVal.append(menuMessage.getOptionValues().get(valuesSelected[i] - 1));
				}

				return retVal.toString();
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMultipleOptionOrQuit(pt.linkare.ant.MenuMessage)
	     */
	public String readMultipleOptionOrQuit(MenuMessage menuMessage) {
		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(currentOptionCount).append(" - ").append(currentOptionMessage);
		}
		fullMessage.append(CRLF).append("q").append(" - ").append("Quit");
		fullMessage.append(CRLF)
				.append("You may select many options by choosing their number in comma separated values e.g: 1,2,3").append(CRLF);

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					System.out.println("You must choose at least one value between 1 and " + currentOptionCount
							+ " or q (quit with no selection)!");
					continue;
				}
				if (readLine.equalsIgnoreCase("q")) {
					return null;
				}
				String[] values = readLine.split(",");

				int[] valuesSelected = new int[values.length];
				for (int i = 0; i < values.length; i++) {
					try {
						valuesSelected[i] = Integer.parseInt(values[i]);
					} catch (Exception ignored) {

					}
					if (valuesSelected[i] == -1 || valuesSelected[i] < 1 || valuesSelected[i] > currentOptionCount) {
						System.out.println("Valid options are between 1 and " + currentOptionCount);
						continue;
					}
				}

				StringBuilder retVal = new StringBuilder();
				for (int i = 0; i < valuesSelected.length; i++) {
					if (i > 0) {
						retVal.append(", ");
					}
					retVal.append(menuMessage.getOptionValues().get(valuesSelected[i] - 1));
				}

				return retVal.toString();
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOptionOrQuitOrDefault(pt.linkare.ant.MenuMessage,
	     *      java.lang.String)
	     */
	public String readMenuOptionOrQuitOrDefault(MenuMessage menuMessage, String propertyDefaultValue) {

		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			fullMessage.append(CRLF)
					.append(propertyDefaultValue.equals(menuMessage.getOptionValues().get(currentOptionCount - 1)) ? "[" : " ")
					.append(currentOptionCount).append(" - ").append(currentOptionMessage)
					.append(propertyDefaultValue.equals(menuMessage.getOptionValues().get(currentOptionCount - 1)) ? "]" : "");
		}
		fullMessage.append(CRLF).append("q").append(" - ").append("Quit");

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					return propertyDefaultValue;
				}
				if (readLine.equalsIgnoreCase("q")) {
					return null;
				}
				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount);
					continue;
				}

				return menuMessage.getOptionValues().get(valueSelected - 1);
			} catch (Exception e) {
				// ignored
			}
		}
	}

	/*
	     * (non-Javadoc)
	     * 
	     * @see pt.linkare.ant.IStdIn#readMenuOptionOrQuit(pt.linkare.ant.MenuMessage)
	     */
	public String readMenuOptionOrQuit(MenuMessage menuMessage) {

		StringBuilder fullMessage = new StringBuilder(menuMessage.getMessage());
		int currentOptionCount = 0;
		for (String currentOptionMessage : menuMessage.getOptions()) {
			++currentOptionCount;
			fullMessage.append(CRLF).append(currentOptionCount).append(" - ").append(currentOptionMessage);
		}
		fullMessage.append(CRLF).append("q").append(" - ").append("Quit");

		while (true) {
			// by the time we get here... the user may select any option
			// between 1 and currentOptionCount (inclusive)

			try {
				String readLine = readStringFromInput(fullMessage.toString());
				if (readLine == null || readLine.length() <= 0) {
					System.out.println("You must choose a value between 1 and " + currentOptionCount
							+ " or q (quit with no selection)!");
					continue;
				}
				if (readLine.equalsIgnoreCase("q")) {
					return null;
				}
				int valueSelected = -1;
				try {
					valueSelected = Integer.parseInt(readLine);
				} catch (Exception ignored) {

				}
				if (valueSelected == -1 || valueSelected < 1 || valueSelected > currentOptionCount) {
					System.out.println("Valid options are between 1 and " + currentOptionCount);
					continue;
				}

				return menuMessage.getOptionValues().get(valueSelected - 1);
			} catch (Exception e) {
				// ignored
			}
		}
	}

	private String readStringFromInput(String message) throws Exception {
		if (proj != null) {
			InputHandler handler = proj.getInputHandler();
			InputRequest request = new InputRequest(message);
			handler.handleInput(request);
			String readLine = request.getInput();
			readLine = (readLine == null ? readLine : readLine.trim());
			return readLine;
		} else {
			System.out.println(message);
			String readLine = bis.readLine();
			readLine = (readLine == null ? readLine : readLine.trim());
			return readLine;
		}

	}

	private String readPrivateStringFromInput(String message) throws Exception {

		if (proj != null) {
			InputHandler handler = proj.getInputHandler();
			InputRequest request = new InputRequest(message);
			handler.handleInput(request);
			String readLine = request.getInput();
			readLine = (readLine == null ? readLine : readLine.trim());
			return readLine;
		} else {
			System.out.println(message);
			String readLine = bis.readLine();
			readLine = (readLine == null ? readLine : readLine.trim());
			return readLine;

			/*System.out.println(message);
			ConsoleReader reader = new jline.ConsoleReader();
			String readLine = reader.readLine('*');
			readLine = (readLine == null ? readLine : readLine.trim());
			return readLine;*/
		}

	}

}
