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
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Rule;

/**
 * Root resource (exposed at "outputs" path)
 */
@Path("outputs")
public class OtputResource {

	private Holder holder;

	private static Log log = LogFactory.getLog(OtputResource.class);

	public OtputResource() {
		this.holder = new Holder();
		this.holder.init();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{topicName}/{ruleName}")
	public List<Output> getOutputs(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getOutputDBHelper().getByTopicNameAndRuleName(
				topicName, ruleName);
	}

	@POST
	@Path("{topicName}/{ruleName}/output")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Output createOutput(@PathParam("topicName") String topicName,
			@PathParam("ruleName") String ruleName, Output output)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException, RuleDoesntExistException {
		Rule rule = this.holder.getRuleDBHelper().getByNameAndTopic(topicName,
				ruleName);
		if (rule == null) {
			throw new RuleDoesntExistException("Rule " + ruleName + " topic " + topicName + " doesnt exist");
		}
		output.setRule(rule);
		return this.holder.getOutputDBHelper().save(output);
	}

	@DELETE
	@Path("{outputId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response deleteOutput(@PathParam("outputId") Long outputId)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		Output output= this.holder.getOutputDBHelper().getById(outputId);
		if (output != null) {
			this.holder.getOutputDBHelper().delete(output.getId());
		}
		return Response.ok().build();
	}

	@PUT
	@Path("output")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response update(Output output) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		log.info("Updating output:" + output.getId());
		try {
			this.holder.getOutputDBHelper().update(output);
		} catch (Exception e) {
			log.error("Cannot update output: " + e, e);
			throw e;
		}
		return Response.ok().entity(output).build();
	}
	
	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{outputId}")
	public Output getOutput(@PathParam("outputId") Long outputId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		return this.holder.getOutputDBHelper().getById(outputId);
	}
}
