package pt.linkare.ant;

import java.util.List;

public class MenuMessage {

	private String message;
	private List<String> options;
	private List<String> optionValues;

	public MenuMessage(String message, List<String> options, List<String> optionValues) {
		super();
		// TODO Auto-generated constructor stub
		this.message = message;
		this.options = options;
		this.optionValues = optionValues;
	}

	public MenuMessage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message The message to set.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return Returns the options.
	 */
	public List<String> getOptions() {
		return options;
	}

	/**
	 * @param options The options to set.
	 */
	public void setOptions(List<String> options) {
		this.options = options;
	}

	/**
	 * @return Returns the optionValues.
	 */
	public List<String> getOptionValues() {
		return optionValues;
	}

	/**
	 * @param optionValues The optionValues to set.
	 */
	public void setOptionValues(List<String> optionValues) {
		this.optionValues = optionValues;
	}

}
