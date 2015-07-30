package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.SearchAnswerResult;

@XmlRootElement
public class SearchAnswer extends DBObject {
    
    private static Log log = LogFactory.getLog(SearchAnswer.class);
    
    public void process(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        super.getHolder().getQueryDBHelper().start(query);
        
    }
    
    public SearchAnswerResult search(Long queryId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
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
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SearchAnswerResult result = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, queryId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                result = new SearchAnswerResult();
                result.setQueryId(rs.getLong("query_id"));
                result.setInputd(rs.getLong("input_id"));
                result.setRuleId(rs.getLong("rule_id"));
                result.setScore(rs.getDouble("score"));
            } 
        } catch (SQLException e) {
            log.error("Error during selecting the query: id " + queryId, e);
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception ex) {
                    
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    // ignore
                }
            }
        }
        return result;
    }    
    

}
