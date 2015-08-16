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
import org.vvv.chatbotdb.model.Action;
import org.vvv.chatbotdb.model.Rule;

/**
 * Root resource (exposed at "inputs" path)
 */
@Path("actions")
public class ActionResource {

	private Holder holder;

	private static Log log = LogFactory.getLog(ActionResource.class);

	public ActionResource() {
		this.holder = new Holder();
		this.holder.init();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{topicName}/{ruleName}")
	public List<Action> getActions(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getActionDBHelper().getByTopicNameAndRuleName(
				topicName, ruleName);
	}

	@POST
	@Path("{topicName}/{ruleName}/action")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Action createAction(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName, Action action)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, RuleDoesntExistException {
		Rule rule = this.holder.getRuleDBHelper().getByNameAndTopic(topicName,
				ruleName);
		if (rule == null) {
			throw new RuleDoesntExistException("Rule " + ruleName + " topic " + topicName + " doesnt exist");
		}
		action.setRule(rule);
		return this.holder.getActionDBHelper().save(action);
	}

	@DELETE
	@Path("{actionId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteAction(@PathParam("actionId") Long actionId)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Action action = this.holder.getActionDBHelper().getById(actionId);
		if (action != null) {
			this.holder.getActionDBHelper().delete(action);
		}
		return Response.ok().build();
	}

	@PUT
	@Path("action")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(Action action) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		log.info("Updating action:" + action.getId());
		try {
			this.holder.getActionDBHelper().update(action);
		} catch (Exception e) {
			log.error("Cannot update action: " + e, e);
			throw e;
		}
		return Response.ok().entity(action).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{actionId}")
	public Action getAction(@PathParam("actionId") Long actionId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getActionDBHelper().getById(actionId);
	}
}
