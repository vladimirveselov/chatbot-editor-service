package org.vvv.chatbot.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.vvv.chatbotdb.dao.Chatbot;

public class ChatbotClientTest {
	
	private static Log log = LogFactory.getLog(ChatbotClientTest.class);

	@Test
	public void testGetAll() {
		ChatbotClient client = new ChatbotClient();
		List<Chatbot> chatbots = client.getAll();
		for (Chatbot chatbot : chatbots) {
			log.info("Chatbot id - " + chatbot.getId());
			log.info("Chatbot name - " + chatbot.getName());
		}
	}
	
	@Test
	public void testCreateDelete() throws UnsupportedEncodingException {
		ChatbotClient client = new ChatbotClient();
		
		String botName = new String("Пушкин".getBytes(), "UTF-8");
		try {
			client.delete(botName);
		} catch (Exception ex) {
			
		}
		
		Chatbot chatbot = new Chatbot();
		chatbot.setName(botName);
		chatbot = client.create(chatbot);
		
		assertNotNull(chatbot);
		
		List<Chatbot> chatbots = client.getAll();
		boolean exists = false;
		for (Chatbot cb : chatbots) {
			log.info("Chatbot id - " + cb.getId());
			log.info("Chatbot name - " + cb.getName());
			if (botName.equals(cb.getName())) {
				exists = true;
			}
		}
		assertTrue(exists);
		
		client.delete(botName);
		chatbots = client.getAll();
		exists = false;
		for (Chatbot cb : chatbots) {
			log.info("Chatbot id - " + cb.getId());
			log.info("Chatbot name - " + cb.getName());
			if (botName.equals(cb.getName())) {
				exists = true;
			}
		}
		assertFalse(exists);
	}

}
