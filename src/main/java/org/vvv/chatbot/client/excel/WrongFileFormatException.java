package org.vvv.chatbot.client.excel;

import org.vvv.chatbot.ChatBotException;

public class WrongFileFormatException extends ChatBotException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20122687489250981L;

	public WrongFileFormatException() {
		super();
	}

	public WrongFileFormatException(String message) {
		super(message);
	}
}
