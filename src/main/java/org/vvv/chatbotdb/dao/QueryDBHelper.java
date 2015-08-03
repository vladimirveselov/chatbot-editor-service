package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Query;


public class QueryDBHelper extends DBObject {
    
    private static Log log = LogFactory.getLog(QueryDBHelper.class);
    
    public Query start(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO queries (query_text, session_id, chatbot_name, event_date, request) VALUES (?, ?, ?, ?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, query.getText());
            pstmt.setString(2, query.getSession_id());
            if (query.getChatbotName()!=null) {
                pstmt.setString(3, query.getChatbotName());
            } else {
                pstmt.setNull(3, java.sql.Types.VARCHAR);
            }
            pstmt.setDate(4, new java.sql.Date(query.getStartDate().getTime()));
            if (query.getRequest()!=null) {
                pstmt.setString(5, query.getRequest());
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }
            pstmt.executeUpdate();
            try(ResultSet keys = pstmt.getGeneratedKeys()) {
	            keys.next();  
	            Long key = keys.getLong(1);
	            query.setId(key);
            }
            super.getHolder().getSplitQueryDBHelper().saveQuery(query);
        } catch (SQLException e) {
            log.error("Error during save query: name: " + query.getText() + ", session: " + query.getSession_id(), e);
            throw e;
        }
        return query;
    }

    
    public void update(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "UPDATE queries SET response = ? , actions = ?, rule_id = ? WHERE id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, query.getResponse());
            if (query.getActions() != null) {
                pstmt.setString(2, query.getActions());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            pstmt.setLong(3, query.getRule_id());
            pstmt.setLong(4, query.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during update query: name: " + query.getText() + ", session: " + query.getSession_id(), e);
            throw e;
        } 
    }

    public void delete(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM queries WHERE id = ?";
        super.getHolder().getSplitQueryDBHelper().deleteByQueryId(query.getId());
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, query.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the query: text: " + query.getText() + ", session: " + query.getSession_id(), e);
            throw e;
        }
    }

    
    public List<Query> getBySession(String sessionId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, query_text, session_id, "
                + " chatbot_name, event_date, response, actions, rule_id, request "
                + " FROM queries "
                + " WHERE session_id = ?"
                + " ORDER BY id desc";
        List<Query> queries = new ArrayList<Query>();
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, sessionId);
            try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                Query query = new Query();
	                query.setId(rs.getLong("id"));
	                query.setText(rs.getString("query_text"));
	                query.setStartDate(new Date(rs.getDate("event_date").getTime()));
	                query.setSession_id(rs.getString("session_id"));
	                query.setResponse(rs.getString("response"));
	                query.setActions(rs.getString("actions"));
	                query.setRule_id(rs.getLong("rule_id"));
	                query.setRequest(rs.getString("request"));
	                query.setChatbotName(rs.getString("chatbot_name"));
	                queries.add(query);
	            }
            }
        } catch (SQLException e) {
            log.error("Error during selecting queries: session id - " + sessionId, e);
            throw e;
        }
        return queries;
    }
    
    public Query getById(Long id) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, query_text, session_id, "
                + " chatbot_name, event_date, response, actions, rule_id, request FROM queries WHERE id = ?";
        Query query = null;
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                query = new Query();
	                query.setId(rs.getLong("id"));
	                query.setText(rs.getString("query_text"));
	                query.setStartDate(new Date(rs.getDate("event_date").getTime()));
	                query.setSession_id(rs.getString("session_id"));
	                query.setResponse(rs.getString("response"));
	                query.setActions(rs.getString("actions"));
	                query.setRule_id(rs.getLong("rule_id"));
	                query.setRequest(rs.getString("request"));
	                query.setChatbotName(rs.getString("chatbot_name"));
	            }
            }
        } catch (SQLException e) {
            log.error("Error during selecting the query: id " + id, e);
            throw e;
        }
        return query;
    }
}
