package org.vvv.chatbot;

public class RuleDoesntExistException extends ChatBotException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20122687489250981L;

	public RuleDoesntExistException() {
		super();
	}

	public RuleDoesntExistException(String message) {
		super(message);
	}
}
