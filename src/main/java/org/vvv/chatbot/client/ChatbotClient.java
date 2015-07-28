package org.vvv.chatbot.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.vvv.chatbotdb.model.Chatbot;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class ChatbotClient {

	private Client client;

	private String endPoint = "http://localhost:5080/chatbot-editor-service/webapi/";

	public ChatbotClient() {
		client = ClientBuilder.newClient();
	}

	public ChatbotClient(String endPoint) {
		client = ClientBuilder.newClient();
		this.endPoint = endPoint;
	}

	public List<Chatbot> getAllChatbots() {
		WebTarget target = client.target(this.endPoint);
		List<Chatbot> response = target.path("chatbots")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Chatbot>>() {
				});
		return response;
	}

	public Chatbot createChatbot(Chatbot chatbot) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("chatbots/chatbot")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(chatbot, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Chatbot.class);

	}

	public void deleteChatbot(String chatbotName) {
		WebTarget target = client.target(this.endPoint);

		Response response = target.path("chatbots/" + chatbotName)
				.request(MediaType.APPLICATION_JSON).delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public List<Topic> getAllTopics() {
		WebTarget target = client.target(this.endPoint);
		List<Topic> response = target.path("topics")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Topic>>() {
				});
		return response;
	}
	
	public List<Rule> getRules(String topicName) {
		WebTarget target = client.target(this.endPoint);
		List<Rule> response = target.path("topics/" + topicName + "/rules")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Rule>>() {
				});
		return response;
	}

	public Topic createTopic(Topic topic) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/topic")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(topic, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Topic.class);
	}
	
	public void deleteTopic(String topicName) {
		WebTarget target = client.target(this.endPoint);

		Response response = target.path("topics/" + topicName)
				.request(MediaType.APPLICATION_JSON).delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}
	
	public void updateTopic(Topic topic) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/" + topic.getTopicName())
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(topic, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}
	
	public void updateRule(String topicName, Rule rule) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/" + topicName + "/rule")
				.request(MediaType.APPLICATION_JSON)
				.put(Entity.entity(rule, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}
	
	public Rule createRule(Rule rule, String topicName) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/" + topicName + "/rule")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(rule, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Rule.class);
	}
	
	public void deleteRule(String topicName, String ruleName) {
		WebTarget target = client.target(this.endPoint);

		Response response = target.path("topics/" + topicName + "/" + ruleName)
				.request(MediaType.APPLICATION_JSON).delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}
	
	public Topic getTopic(String topicName) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/" + topicName)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Topic.class);
	}
}