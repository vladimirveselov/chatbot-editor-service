package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.SearchAnswerResult;

@XmlRootElement
public class SearchAnswerDBHelper extends DBObject {
    
    public static final String REQUEST_PREFIX = "request=";
    
    public static final Long REQUEST_TIME_OUT = 60 * 60 * 1000l; // 1 hr

	private static Log log = LogFactory.getLog(SearchAnswerDBHelper.class);
    
    private Random random = new Random();
    
    public Query process(Query query) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		QueryDBHelper qh = super.getHolder().getQueryDBHelper();
		query.setStartDate(new Date());
		if (query.getSession_id() == null) {
			query.setSession_id(UUID.randomUUID().toString());
		} else {
			List<Query> queries = qh.getBySession(query.getSession_id());
			if (queries.size()>0) {
				Query prev = queries.get(0);
				if (prev.getActions() != null
						&& prev.getActions().startsWith(REQUEST_PREFIX)
						&& (query.getStartDate().getTime() < 
								prev.getStartDate().getTime() + REQUEST_TIME_OUT)) {
					String request = prev.getActions().substring(REQUEST_PREFIX.length()).trim();
					if (request.length() > 0) {
						query.setRequest(request);
					}
				}
			}
		}
		query = qh.start(query);
		return this.search(query);
    }
    
    public Query search(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        SearchAnswerResult result = this.searchResponse(query);
        if (result == null) {
        	result = this.searchAll(query);
        }
        if (result == null) {
        	result = this.searchDefault(query);
        }
        if (result != null) {
	        Rule rule = super.getHolder().getRuleDBHelper().getById(result.getRuleId());
	        log.debug("Rule:" + rule.getName());
	        log.debug("Answer:");
	        List<Output> outputs = super.getHolder().getOutputDBHelper().findByRuleId(rule.getId());
	        for (Output o : outputs) {
	            log.debug(o.getText());
	        }
	        if (outputs.size()>0) {
	            Output output = outputs.get(random.nextInt(outputs.size()));
	            query.setResponse(output.getText());
	            query.setRule_id(rule.getId());
	            if (output.getRequest() != null) {
	            	query.setActions(REQUEST_PREFIX + output.getRequest());	           
	            }
	            super.getHolder().getQueryDBHelper().update(query);
	        }
        }
        return query;
    }
    
    public SearchAnswerResult searchResponse(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
    	if (query.getRequest() == null) {
    		return null;
    	}
        String sql = "SELECT r.id rule_id, b.id input_id, "
                + " sum(d.weight)*r.rank*t.rank score, c.query_id query_id FROM "
                + " split_inputs a, "
                + " inputs b, "
                + " split_queries c, "
                + " (select word, (1.0/count(id)) weight from split_inputs group by word) d, "
                + " rules r,"
                + " topics t"
                + "   WHERE "
                + " (( b.id = a.input_id AND "
                + "    a.word = d.word AND "
                + "    a.word = c.word)"
                + "  OR ("
                + "    b.id = a.input_id AND"
                + "    a.word = d.word AND"
                + "    a.prev_word = c.prev_word AND"
                + "    a.word = c.word AND"
                + "    a.next_word = c.next_word"
                + " ) OR ("
                + "    b.id = a.input_id AND"
                + "    a.word = d.word AND"
                + "    a.word = c.word AND"
                + "    a.next_word = c.next_word"
                + " ) OR ("
                + "    b.id = a.input_id AND"
                + "    a.word = d.word AND"
                + "    a.prev_word = c.prev_word AND"
                + "    a.word = c.word"
                + " )) AND"
                + "    b.rule_id = r.id AND"
                + "    b.response = ?"
                + "    r.topic_id = r.topic_id AND"
                + "    c.query_id = ?"
                + "    group by b.id"
                + "    order by score desc"
                + " limit 1";
        SearchAnswerResult result = null;
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
        	pstmt.setString(1, query.getRequest());
            pstmt.setLong(2, query.getId());
            try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                result = new SearchAnswerResult();
	                result.setQueryId(rs.getLong("query_id"));
	                result.setInputd(rs.getLong("input_id"));
	                result.setRuleId(rs.getLong("rule_id"));
	                result.setScore(rs.getDouble("score"));
	            } 
            }
        } catch (SQLException e) {
            log.error("Error during selecting the query: id " + query.getId(), e);
            throw e;
        }
        return result;
    }

    public SearchAnswerResult searchAll(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT r.id rule_id, b.id input_id, "
                + " sum(d.weight)*r.rank*t.rank score, c.query_id query_id FROM "
                + " split_inputs a, "
                + " inputs b, "
                + " split_queries c, "
                + " (select word, (1.0/count(id)) weight from split_inputs group by word) d, "
                + " rules r,"
                + " topics t"
                + "   WHERE "
                + " (( b.id = a.input_id AND "
                + "    a.word = d.word AND "
                + "    a.word = c.word)"
                + "  OR ("
                + "    b.id = a.input_id AND"
                + "    a.word = d.word AND"
                + "    a.prev_word = c.prev_word AND"
                + "    a.word = c.word AND"
                + "    a.next_word = c.next_word"
                + " ) OR ("
                + "    b.id = a.input_id AND"
                + "    a.word = d.word AND"
                + "    a.word = c.word AND"
                + "    a.next_word = c.next_word"
                + " ) OR ("
                + "    b.id = a.input_id AND"
                + "    a.word = d.word AND"
                + "    a.prev_word = c.prev_word AND"
                + "    a.word = c.word"
                + " )) AND"
                + "    b.rule_id = r.id AND"
                + "    r.topic_id = r.topic_id AND"
                + "    c.query_id = ?"
                + "    group by b.id"
                + "    order by score desc"
                + " limit 1";
        SearchAnswerResult result = null;
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, query.getId());
            try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                result = new SearchAnswerResult();
	                result.setQueryId(rs.getLong("query_id"));
	                result.setInputd(rs.getLong("input_id"));
	                result.setRuleId(rs.getLong("rule_id"));
	                result.setScore(rs.getDouble("score"));
	            } 
            }
        } catch (SQLException e) {
            log.error("Error during selecting the query: id " + query.getId(), e);
            throw e;
        }
        return result;
    }

    public SearchAnswerResult searchDefault(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT r.id rule_id , r.rank*t.rank score "
                + " FROM "
                + "   rules r,"
                + "   topics t"
                + " WHERE "
                + "   r.topic_id = t.id "
                + "   AND "
                + "   ( r.rule_name = \'default\' "
                + "       OR "
                + "     t.topic_name = \'default\'"
                + "   ) "
                + "   ORDER BY score DESC"
                + " limit 1";
        SearchAnswerResult result = null;
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                result = new SearchAnswerResult();
	                result.setQueryId(query.getId());
	                result.setRuleId(rs.getLong("rule_id"));
	                result.setScore(rs.getDouble("score"));
	            } 
            }
        } catch (SQLException e) {
            log.error("Error during selecting the query: id " + query.getId(), e);
            throw e;
        }
        return result;
    }

}
