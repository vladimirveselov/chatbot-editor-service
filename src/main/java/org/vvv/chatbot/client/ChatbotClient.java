package org.vvv.chatbot.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.vvv.chatbotdb.dao.Chatbot;

public class ChatbotClient {

	private Client client;

	public ChatbotClient() {
		client = ClientBuilder.newClient();
	}

	public List<Chatbot> getAll() {
		WebTarget target = client
				.target("http://localhost:8080/chatbot-editor-service/webapi/");
		List<Chatbot> response = target.path("chatbots")
				.request(MediaType.APPLICATION_JSON)
				.get(new GenericType<List<Chatbot>>() {
				});
		return response;
	}

	public Chatbot create(Chatbot chatbot) {
		WebTarget target = client
				.target("http://localhost:8080/chatbot-editor-service/webapi/");
		Response response = target.path("chatbots/chatbot")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(chatbot, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Chatbot.class);

	}
	
	public void delete(String chatbotName) {
		WebTarget target = client
				.target("http://localhost:8080/chatbot-editor-service/webapi/");
		
		Response response = target.path("chatbots/" + chatbotName).request(MediaType.APPLICATION_JSON).delete();
		
		if(response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus() + ": there was an error on the server.");
		}
	}
}