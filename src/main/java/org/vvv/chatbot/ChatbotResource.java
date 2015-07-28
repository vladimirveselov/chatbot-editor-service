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

import org.vvv.chatbotdb.dao.ChatbotDBHelper;
import org.vvv.chatbotdb.dao.Holder;
import org.vvv.chatbotdb.model.Chatbot;


/**
 * Root resource (exposed at "chatbot" path)
 */
@Path("chatbots")
public class ChatbotResource {

	private Holder holder;

	public ChatbotResource() {
		this.holder = new Holder();
		this.holder.init();
	}
	

	@GET
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Chatbot> getAllChatbots() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        holder.getDbHelper().initConnection();
        ChatbotDBHelper helper = holder.getChatbotDBHelper();
        List<Chatbot> response = helper.list();
        holder.getDbHelper().closeConnection();
        return response;
    }
	
	

	@POST
	@Path("chatbot")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Chatbot createActivityParams(Chatbot chatbot) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        holder.getDbHelper().initConnection();
        ChatbotDBHelper helper = holder.getChatbotDBHelper();
        Chatbot response = helper.save(chatbot);
        holder.getDbHelper().closeConnection();
        return response;
	}
	
	@DELETE
	@Path("{chatbotName}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete (@PathParam ("chatbotName") String chatbotName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        holder.getDbHelper().initConnection();
        ChatbotDBHelper helper = holder.getChatbotDBHelper();
        helper.delete(chatbotName);
        holder.getDbHelper().closeConnection();
		return Response.ok().build();
	}
	
}
	
