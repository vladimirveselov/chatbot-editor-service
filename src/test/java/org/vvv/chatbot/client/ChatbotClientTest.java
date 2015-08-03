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
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Query;
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
		
		Rule rule2 = client.getRule(topic.getTopicName(), rule.getName());
		log.info("Rule2:" + rule2.getId());
		log.info("Name:" + rule2.getName());
		log.info("rank:" + rule2.getRank());
		assertTrue(777l == rule2.getRank());

		Input input = new Input();
		input.setRule(rule);
		input.setText("Hahahaha");
		input = client.createInput(topic.getTopicName(), rule.getName(), input);
		log.info("Input created:" + input.getId());
		log.info("Text:" + input.getText());

		List<Input> inputs = client.getInputs(topic.getTopicName(), rule.getName());
		exists = false;
		for (Input in: inputs) {
			log.info("id - " + in.getId());
			log.info("name - " + in.getText());
			if (input.getText().equals(in.getText())) {
				exists = true;
			}
		}
		assertTrue(exists);
		
		input.setText("Hehehe");
		client.updateInput(input);
		
		Output output = new Output();
		output.setText("What?");
		output.setRequest("WHAT");
		output.setRule(rule2);
		output = client.createOutput(topic.getTopicName(), rule.getName(), output);
		log.info("output created:" + output.getId());
		
		List<Output> outputs = client.getOutputs(topic.getTopicName(), rule.getName());
		log.info("outputs received - " + outputs.size());
		exists = false;
		for (Output o: outputs) {
			log.info("id - " + o.getId());
			log.info("name - " + o.getText());
			if (o.getText().equals(output.getText())) {
				exists = true;
			}
		}
		assertTrue(exists);
		
		output.setText("Why?");
		output.setRequest("WHY");
		client.updateOutput(output);
		log.info("output updated:" + output.getText());
		
		Output output2 = client.getOutput(output.getId());
		log.info("output updated:" + output2.getText());
		
		assertEquals(output.getText(), output2.getText());
		assertEquals(output.getRequest(), output2.getRequest());
		
		Query query = new Query();
		query.setText("Hehehe");
		query = client.getAnswerGET(query);
		log.info(query.getResponse());
		log.info(query.getRule_id());
		
		client.deleteOutput(output.getId());
		log.info("output deleted:" + output.getText());
		
		outputs = client.getOutputs(topic.getTopicName(), rule.getName());
		log.info("outputs received - " + outputs.size());
		exists = false;
		for (Output o: outputs) {
			log.info("id - " + o.getId());
			log.info("name - " + o.getText());
			if (o.getText().equals(output.getText())) {
				exists = true;
			}
		}
		assertFalse(exists);
		
		Input input2 = client.getInput(input.getId());
		log.info("Input updated:" + input2.getId());
		log.info("Text:" + input2.getText());
		
		client.deleteInput(input2.getId());
		log.info("Input deleted:" + input2.getId());
		inputs = client.getInputs(topic.getTopicName(), rule.getName());
		log.info("Inputs received:" + inputs.size());
		exists = false;
		for (Input in: inputs) {
			log.info("id - " + in.getId());
			log.info("name - " + in.getText());
			if (input2.getText().equals(in.getText())) {
				exists = true;
			}
		}
		assertFalse(exists);
		
		
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
