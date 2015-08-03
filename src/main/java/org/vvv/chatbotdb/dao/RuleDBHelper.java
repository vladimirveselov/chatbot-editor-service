package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;


public class RuleDBHelper extends DBObject {
    
    private static Log log = LogFactory.getLog(RuleDBHelper.class);
    
    public Rule save(Rule rule) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO rules (topic_id, rule_name, rank, response) VALUES (?, ?, ?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, rule.getTopic().getId());
            pstmt.setString(2, rule.getName());
            pstmt.setLong(3, rule.getRank());
            pstmt.setString(4, rule.getResponse());
            pstmt.executeUpdate();
            try(ResultSet keys = pstmt.getGeneratedKeys()) {    
	            keys.next();  
	            Long key = keys.getLong(1);
	            rule.setId(key);
            }            
            for (Input input : rule.getInputs()) {
                super.getHolder().getInputDBHelper().save(input);                
            }
            for (Output output : rule.getOutputs()) {
                super.getHolder().getOutputDBHelper().save(output);                
            }
        } catch (SQLException e) {
            log.error("Error during save rule: name: " + rule.getName() + ", rank: " + rule.getRank(), e);
            throw e;
        }
        return rule;
    }
    
    public void update(Rule rule) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "UPDATE rules "
        		+ " SET "
        		+ "	rank = ? ,"
        		+ " response = ?"
        		+ " WHERE id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, rule.getRank());
            pstmt.setString(2, rule.getResponse());
            pstmt.setLong(3, rule.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during updating rule: name: " + rule.getName() + ", rank: " + rule.getRank(), e);
            throw e;
        }
    }
    public void delete(Rule rule) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        super.getHolder().getInputDBHelper().deleteByRuleId(rule.getId());
        super.getHolder().getOutputDBHelper().deleteByRuleId(rule.getId());
        String sql = "DELETE FROM rules WHERE id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, rule.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the rule: name: " + rule.getName() + ", topic: " + rule.getTopic().getTopicName(), e);
            throw e;
        } 
    }

    
    public Rule getById(Long id) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT "
        		+ " r.id id, "
        		+ " r.rule_name rule_name, "
        		+ " r.rank rank,"
        		+ " r.topic_id topic_id,"
        		+ " t.topic_name topic_name,"
        		+ " t.rank topic_rank "
        		+ " FROM rules r, topics t"
        		+ " WHERE r.topic_id = t.id"
        		+ " AND r.id = ?";
        Rule rule = null;
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            try(ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                rule = new Rule();
	                rule.setId(rs.getLong("id"));
	                rule.setName(rs.getString("rule_name"));
	                rule.setRank(rs.getLong("rank"));
	                Topic topic = new Topic();
	                topic.setId(rs.getLong("topic_id"));
	                topic.setTopicName(rs.getString("topic_name"));
	                topic.setRank(rs.getLong("topic_rank"));
	                rule.setTopic(topic);
	            }
            }
        } catch (SQLException e) {
            log.error("Error during selecting the rule: id " + id, e);
            throw e;
        }
        return rule;
    }    
    
    public Rule getByNameAndTopic(String topicName, String ruleName) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT r.id, r.rule_name, r.rank FROM rules r, topics t"
        		+ " WHERE r.topic_id = t.id"
        		+ " AND t.topic_name = ?"
        		+ " AND r.rule_name = ?";
        Rule rule = null;
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, topicName);
            pstmt.setString(2, ruleName);
            try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                rule = new Rule();
	                rule.setId(rs.getLong("id"));
	                rule.setName(rs.getString("rule_name"));
	                rule.setRank(rs.getLong("rank"));
	            } 
            }
        } catch (SQLException e) {
            log.error("Error during selecting the rule: name " 
            	+ ruleName + " topic name:" + topicName, e);
            throw e;
        } 
        return rule;
    } 
    
    public List<Rule> getByTopic(Topic topic) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, rule_name, rank FROM rules WHERE topic_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Rule> rules = new ArrayList<Rule>();
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, topic.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Rule rule = new Rule();
                rule.setId(rs.getLong("id"));
                rule.setName(rs.getString("rule_name"));
                rule.setRank(rs.getLong("rank"));
                rule.setTopic(topic);
                rules.add(rule);
            } 
        } catch (SQLException e) {
            log.error("Error during selecting the rule: by topic, name - " + topic.getTopicName(), e);
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
        return rules;
    }
    
    public List<Rule> getByTopicName(String topicName) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, rule_name, rank FROM rules r, topics t WHERE "
        		+ "r.topic_id = t.id "
        		+ "and t.topic_name = ?";
        List<Rule> rules = new ArrayList<Rule>();
        try(Connection conn = super.getDbHelper().getConnection()) {
            try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            	pstmt.setString(1, topicName);
            	try(ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Rule rule = new Rule();
                        rule.setId(rs.getLong("id"));
                        rule.setName(rs.getString("rule_name"));
                        rule.setRank(rs.getLong("rank"));
                        rules.add(rule);
                    } 
            	}
            }
        } catch (SQLException e) {
            log.error("Error during selecting the rule: by topic name - " + topicName, e);
            throw e;
        } 
        return rules;
    }
}
