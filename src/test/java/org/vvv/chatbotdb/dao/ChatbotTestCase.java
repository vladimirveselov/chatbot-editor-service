package org.vvv.chatbotdb.dao;

public class ChatbotTestCase {
	
	@Override
	public String toString() {
		return "ChatbotTestCase [question=" + question + ", expectedRuleId="
				+ expectedRuleName + "]";
	}

	public ChatbotTestCase(String question, String expectedRuleName) {
		super();
		this.question = question;
		this.expectedRuleName = expectedRuleName;
	}

	private String question;
	
	private String expectedRuleName;

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getExpectedRuleName() {
		return expectedRuleName;
	}

	public void setExpectedRuleName(String expectedRuleName) {
		this.expectedRuleName = expectedRuleName;
	}

}
