package org.vvv.chatbot;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.dao.Holder;
import org.vvv.chatbotdb.model.StateMachine;

/**
 * Root resource (exposed at "topics" path)
 */
@Path("statemachines")
public class StateMachineResource {

	private Holder holder;

	private static Log log = LogFactory.getLog(StateMachineResource.class);

	public StateMachineResource() {
		this.holder = new Holder();
		this.holder.init();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<String> listStateMachines() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
	    return this.holder.getStateMachineDBHelper().listStateMachines();
	}

	@POST
	@Path("statemachine")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public StateMachine createStateMachine(StateMachine stateMachine) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		log.info("Creating state machine:" + stateMachine.getName());
		try {
			StateMachine response = this.holder.getStateMachineDBHelper().saveStateMachine(stateMachine);
			return response;
		} catch (Exception e) {
			log.error("Cannot save state machine: " + e, e);
			throw e;
		}
	}

	@DELETE
	@Path("{stateMachineName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("stateMachineName") String stateMachineName)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
	    this.holder.getStateMachineDBHelper().deleteStateMachine(stateMachineName);
		return Response.ok().build();
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{stateMachineName}")
	public StateMachine getStateMachine(@PathParam("stateMachineName") String stateMachineName)
			throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		return this.holder.getStateMachineDBHelper().getStateMachine(stateMachineName);
	}

}
