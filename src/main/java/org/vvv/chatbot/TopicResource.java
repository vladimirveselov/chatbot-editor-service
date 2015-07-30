package org.vvv.chatbot;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.dao.Holder;
import org.vvv.chatbotdb.dao.TopicDBHelper;
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

/**
 * Root resource (exposed at "topics" path)
 */
@Path("topics")
public class TopicResource {

	private Holder holder;

	private static Log log = LogFactory.getLog(TopicResource.class);

	public TopicResource() {
		this.holder = new Holder();
		this.holder.init();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<Topic> getAllTopics() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		holder.getDbHelper().initConnection();
		TopicDBHelper helper = holder.getTopicDBHelper();
		List<Topic> response = helper.getAll();
		holder.getDbHelper().closeConnection();
		return response;
	}

	@POST
	@Path("topic")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Topic createTopic(Topic topic) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		log.info("Creating topic:" + topic.getTopicName());
		try {
			Topic response = this.holder.getTopicDBHelper().save(topic);
			return response;
		} catch (Exception e) {
			log.error("Cannot update topic: " + e, e);
			throw e;
		}
	}

	@PUT
	@Path("{topicName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(Topic topic) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		log.info("Updating topic:" + topic.getTopicName());
		try {
			this.holder.getTopicDBHelper().update(topic);
		} catch (Exception e) {
			log.error("Cannot update topic: " + e, e);
			throw e;
		}
		return Response.ok().entity(topic).build();
	}

	@DELETE
	@Path("{topicName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("topicName") String topicName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		holder.getDbHelper().initConnection();
		TopicDBHelper helper = holder.getTopicDBHelper();
		helper.delete(topicName);
		holder.getDbHelper().closeConnection();
		return Response.ok().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{topicName}/rules")
	public List<Rule> getRules(@PathParam("topicName") String topicName)
			throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
		return this.holder.getRuleDBHelper().getByTopic(topic);
	}

	@POST
	@Path("{topicName}/rule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Rule createRule(@PathParam("topicName") String topicName, Rule rule)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
		rule.setTopic(topic);
		return this.holder.getRuleDBHelper().save(rule);
	}

	@DELETE
	@Path("{topicName}/{ruleName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteRule(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Rule rule = this.holder.getRuleDBHelper().getByNameAndTopic(topicName,
				ruleName);
		if (rule != null) {
			this.holder.getRuleDBHelper().delete(rule);
		}
		return Response.ok().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{topicName}")
	public Topic getTopic(@PathParam("topicName") String topicName)
			throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return this.holder.getTopicDBHelper().getByName(topicName);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{topicName}/{ruleName}")
	public Rule getRule(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getRuleDBHelper().getByNameAndTopic(topicName,
				ruleName);
	}

	@PUT
	@Path("{topicName}/rule")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response updateRule(@PathParam("topicName") String topicName,
			Rule rule) throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		log.info("Updating rule:" + rule.getName());
		try {
			Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
			rule.setTopic(topic);
			this.holder.getRuleDBHelper().update(rule);
		} catch (Exception e) {
			log.error("Cannot update topic: " + e, e);
			throw e;
		}
		return Response.ok().entity(rule).build();
	}

}
