package org.vvv.chatbot.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.vvv.chatbot.security.LoginFilter;
import org.vvv.chatbotdb.model.Action;
import org.vvv.chatbotdb.model.Chatbot;
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.StateMachine;
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
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(new GenericType<List<Chatbot>>() {
				});
		return response;
	}

	public Chatbot createChatbot(Chatbot chatbot) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("chatbots/chatbot")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
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
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public List<Topic> getAllTopics() {
		WebTarget target = client.target(this.endPoint);
		List<Topic> response = target.path("topics")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(new GenericType<List<Topic>>() {
				});
		return response;
	}

	public List<Rule> getRules(String topicName) {
		WebTarget target = client.target(this.endPoint);
		List<Rule> response = target.path("topics/" + topicName + "/rules")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(new GenericType<List<Rule>>() {
				});
		return response;
	}

	public Rule getRule(String topicName, String ruleName) {
		WebTarget target = client.target(this.endPoint);
		Rule response = target.path("topics/" + topicName + "/" + ruleName)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(new GenericType<Rule>() {
				});
		return response;
	}

	public Topic createTopic(Topic topic) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/topic")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
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
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public void updateTopic(Topic topic) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/" + topic.getTopicName())
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
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
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
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
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
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
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public Topic getTopic(String topicName) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("topics/" + topicName)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(Response.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Topic.class);
	}

	public List<Input> getInputs(String topicName, String ruleName) {
		WebTarget target = client.target(this.endPoint);
		List<Input> response = target
				.path("inputs/" + topicName + "/" + ruleName)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(new GenericType<List<Input>>() {
				});
		return response;
	}

	public void deleteInput(Long inputId) {
		WebTarget target = client.target(this.endPoint);

		Response response = target.path("inputs/" + inputId)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public Input createInput(String topicName, String ruleName, Input input) {
		WebTarget target = client.target(this.endPoint);
		Response response = target
				.path("inputs/" + topicName + "/" + ruleName + "/input")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.post(Entity.entity(input, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Input.class);
	}

	public void updateInput(Input input) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("inputs/input")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.put(Entity.entity(input, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public Input getInput(Long inputId) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("inputs/" + inputId)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(Response.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Input.class);
	}

	public List<Output> getOutputs(String topicName, String ruleName) {
		WebTarget target = client.target(this.endPoint);
		List<Output> response = target
				.path("outputs/" + topicName + "/" + ruleName)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(new GenericType<List<Output>>() {
				});
		return response;
	}

	public void deleteOutput(Long outputId) {
		WebTarget target = client.target(this.endPoint);

		Response response = target.path("outputs/" + outputId)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.delete();

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public Output createOutput(String topicName, String ruleName, Output output) {
		WebTarget target = client.target(this.endPoint);
		Response response = target
				.path("outputs/" + topicName + "/" + ruleName + "/output")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.post(Entity.entity(output, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Output.class);
	}

	public void updateOutput(Output output) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("outputs/output")
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.put(Entity.entity(output, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}
	}

	public Output getOutput(Long outputId) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("outputs/" + outputId)
				.request(MediaType.APPLICATION_JSON)
				.header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
				.get(Response.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Output.class);
	}

	public Query getAnswerGET(Query query) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("talk/" + query.getText())
				.request(MediaType.APPLICATION_JSON).get(Response.class);

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Query.class);
	}

	public Query getAnswerPOST(Query query) {
		WebTarget target = client.target(this.endPoint);
		Response response = target.path("talk/query")
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.entity(query, MediaType.APPLICATION_JSON));

		if (response.getStatus() != 200) {
			throw new RuntimeException(response.getStatus()
					+ ": there was an error on the server.");
		}

		return response.readEntity(Query.class);
	}

    public List<Action> getActions(String topicName, String ruleName) {
        WebTarget target = client.target(this.endPoint);
        List<Action> response = target
                .path("actions/" + topicName + "/" + ruleName)
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .get(new GenericType<List<Action>>() {
                });
        return response;
    }

    public void deleteAction(Long actionId) {
        WebTarget target = client.target(this.endPoint);

        Response response = target.path("actions/" + actionId)
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .delete();

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }
    }

    public Action createAction(String topicName, String ruleName, Action action) {
        WebTarget target = client.target(this.endPoint);
        Response response = target
                .path("actions/" + topicName + "/" + ruleName + "/action")
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .post(Entity.entity(action, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }

        return response.readEntity(Action.class);
    }

    public void updateAction(Action action) {
        WebTarget target = client.target(this.endPoint);
        Response response = target.path("actions/action")
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .put(Entity.entity(action, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }
    }

    public Action getAction(Long actionId) {
        WebTarget target = client.target(this.endPoint);
        Response response = target.path("actions/" + actionId)
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .get(Response.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }

        return response.readEntity(Action.class);
    }


    public List<StateMachine> getStateMachines() {
        WebTarget target = client.target(this.endPoint);
        List<StateMachine> response = target
                .path("statemachines")
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .get(new GenericType<List<StateMachine>>() {
                });
        return response;
    }

    public void deleteStateMachine(String stateMachineName) {
        WebTarget target = client.target(this.endPoint);

        Response response = target.path("statemachines/" + stateMachineName)
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .delete();

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }
    }

    public StateMachine createStateMachine(StateMachine stateMachine) {
        WebTarget target = client.target(this.endPoint);
        Response response = target
                .path("statemachines/statemachine")
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .post(Entity.entity(stateMachine, MediaType.APPLICATION_JSON));

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }

        return response.readEntity(StateMachine.class);
    }

    public StateMachine getStateMachine(String name) {
        WebTarget target = client.target(this.endPoint);
        Response response = target.path("statemachines/" + name)
                .request(MediaType.APPLICATION_JSON)
                .header(LoginFilter.HEADER_KEY, LoginFilter.SECRET)
                .get(Response.class);

        if (response.getStatus() != 200) {
            throw new RuntimeException(response.getStatus()
                    + ": there was an error on the server.");
        }

        return response.readEntity(StateMachine.class);
    }

}