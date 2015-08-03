package org.vvv.chatbot;

import java.sql.SQLException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.dao.Holder;
import org.vvv.chatbotdb.model.Query;

/**
 * Root resource (exposed at "topics" path)
 */
@Path("talk")
public class TalkResource {

	private Holder holder;

	private static Log log = LogFactory.getLog(TalkResource.class);

	public TalkResource() {
		this.holder = new Holder();
		this.holder.init();
	}

	@POST
	@Path("query")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Query answer(Query query)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		log.info("Answering question:" + query.getText());
		return this.holder.getSearchAnswer().process(query);
	}

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("{query}")
	public Query answer(@PathParam("query") String queryText)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		log.info("Answering question:" + queryText);
		Query query = new Query();
		query.setText(queryText);
		return this.answer(query);
	}

}
