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
 * Root resource (exposed at "inputs" path)
 */
@Path("inputs")
public class InputsResource {

	private Holder holder;

	private static Log log = LogFactory.getLog(InputsResource.class);

	public InputsResource() {
		this.holder = new Holder();
		this.holder.init();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{topicName}/{ruleName}")
	public List<Input> getInputs(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getInputDBHelper().getByTopicNameAndRuleName(
				topicName, ruleName);
	}

	@POST
	@Path("{topicName}/{ruleName}/input")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Input createInput(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName, Input input)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, RuleDoesntExistException {
		Rule rule = this.holder.getRuleDBHelper().getByNameAndTopic(topicName,
				ruleName);
		if (rule == null) {
			throw new RuleDoesntExistException("Rule " + ruleName + " topic " + topicName + " doesnt exist");
		}
		input.setRule(rule);
		return this.holder.getInputDBHelper().save(input);
	}

	@DELETE
	@Path("{inputId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteinput(@PathParam("inputId") Long inputId)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Input input = this.holder.getInputDBHelper().getById(inputId);
		if (input != null) {
			this.holder.getInputDBHelper().delete(input);
		}
		return Response.ok().build();
	}

	@PUT
	@Path("input")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(Input input) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		log.info("Updating input:" + input.getId());
		try {
			this.holder.getInputDBHelper().update(input);
		} catch (Exception e) {
			log.error("Cannot update topic: " + e, e);
			throw e;
		}
		return Response.ok().entity(input).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{inputId}")
	public Input getInput(@PathParam("inputId") Long inputId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getInputDBHelper().getById(inputId);
	}
}
