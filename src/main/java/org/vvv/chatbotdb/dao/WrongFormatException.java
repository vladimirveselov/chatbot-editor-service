package org.vvv.chatbotdb.dao;

import org.vvv.chatbot.ChatBotException;

public class WrongFormatException extends ChatBotException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7400809294104253490L;

	public WrongFormatException() {
		super();
	}

	public WrongFormatException(String message) {
		super(message);
	}
}
