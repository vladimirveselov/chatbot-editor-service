package org.vvv.chatbot.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.vvv.chatbotdb.model.Chatbot;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;
import org.vvv.chatbotdb.test.IntegrationTest;

@Category(IntegrationTest.class)
public class ChatbotClientTest {
	
	private static Log log = LogFactory.getLog(ChatbotClientTest.class);

	@Test
	public void testGetAllChatbots() {
		ChatbotClient client = new ChatbotClient();
		List<Chatbot> chatbots = client.getAllChatbots();
		for (Chatbot chatbot : chatbots) {
			log.info("Chatbot id - " + chatbot.getId());
			log.info("Chatbot name - " + chatbot.getName());
		}
	}
	
	@Test
	public void testCreateDelete() throws UnsupportedEncodingException {
		ChatbotClient client = new ChatbotClient();
		
		String botName = new String("Pushkin".getBytes(), "UTF-8");
		try {
			client.deleteChatbot(botName);
		} catch (Exception ex) {
			
		}
		
		Chatbot chatbot = new Chatbot();
		chatbot.setName(botName);
		chatbot = client.createChatbot(chatbot);
		
		assertNotNull(chatbot);
		
		List<Chatbot> chatbots = client.getAllChatbots();
		boolean exists = false;
		for (Chatbot cb : chatbots) {
			log.info("Chatbot id - " + cb.getId());
			log.info("Chatbot name - " + cb.getName());
			if (botName.equals(cb.getName())) {
				exists = true;
			}
		}
		assertTrue(exists);
		
		client.deleteChatbot(botName);
		chatbots = client.getAllChatbots();
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
	
	
	@Test
	public void testCreateDeleteTopic() throws UnsupportedEncodingException {
		ChatbotClient client = new ChatbotClient();
		
		String topicName = new String("Hello".getBytes(), "UTF-8");
		String ruleName = new String("rule1".getBytes(), "UTF-8");
		try {
			client.deleteRule(topicName, ruleName);
		} catch (Exception ex) {
			
		}		
		try {
			client.deleteTopic(topicName);
		} catch (Exception ex) {
			
		}
		
		Topic topic = new Topic();
		topic.setTopicName(topicName);
		topic = client.createTopic(topic);
		
		assertNotNull(topic);
		
		List<Topic> topics = client.getAllTopics();
		boolean exists = false;
		for (Topic top : topics) {
			log.info("id - " + top.getId());
			log.info("name - " + top.getTopicName());
			if (topicName.equals(top.getTopicName())) {
				exists = true;
			}
		}
		assertTrue(exists);
		
		topic.setRank(999l);
		client.updateTopic(topic);
		
		Topic topic2 = client.getTopic(topic.getTopicName());
		assertEquals(topic.getRank(), topic2.getRank());
		
		Rule rule = new Rule();
		rule.setName(ruleName);
		rule = client.createRule(rule, topic.getTopicName());
		log.info("Created rule: " + rule.getId());
		List<Rule> rules = client.getRules(topic.getTopicName());
		for (Rule r : rules) {
			log.info("Rule:" + r.getId());
			log.info("Name:" + r.getName());
			log.info("rank:" + r.getRank());
		}
		rule.setRank(777l);
		client.updateRule(topic.getTopicName(), rule);
		log.info("updated rule: " + rule.getId());
		rules = client.getRules(topic.getTopicName());
		for (Rule r : rules) {
			log.info("Rule:" + r.getId());
			log.info("Name:" + r.getName());
			log.info("rank:" + r.getRank());
		}
		
		client.deleteRule(topic.getTopicName(), rule.getName());
		log.info("deleted rule: " + rule.getId());
		
		client.deleteTopic(topicName);
		topics = client.getAllTopics();
		exists = false;
		for (Topic top : topics) {
			log.info("id - " + top.getId());
			log.info("name - " + top.getTopicName());
			if (topicName.equals(top.getTopicName())) {
				exists = true;
			}
		}
		assertFalse(exists);
	}
	
	@Test
	public void testGetAllTopics() {
		ChatbotClient client = new ChatbotClient();
		List<Topic> topics = client.getAllTopics();
		for (Topic top : topics) {
			log.info("id - " + top.getId());
			log.info("name - " + top.getTopicName());
		}
	}
}
