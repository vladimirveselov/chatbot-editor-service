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


public class RuleDBHelper extends DBObject {
    
    private static Log log = LogFactory.getLog(RuleDBHelper.class);
    
    public void save(Rule rule) throws SQLException {
        String sql = "INSERT INTO rules (topic_id, rule_name, rank, response) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet keys = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, rule.getTopic().getId());
            pstmt.setString(2, rule.getName());
            pstmt.setLong(3, rule.getRank());
            pstmt.setString(4, rule.getResponse());
            pstmt.executeUpdate();
            keys = pstmt.getGeneratedKeys();    
            keys.next();  
            Long key = keys.getLong(1);
            rule.setId(key);
            for (Input input : rule.getInputs()) {
                super.getHolder().getInputDBHelper().save(input);                
            }
            for (Output output : rule.getOutputs()) {
                super.getHolder().getOutputDBHelper().save(output);                
            }
        } catch (SQLException e) {
            log.error("Error during save rule: name: " + rule.getName() + ", rank: " + rule.getRank(), e);
            throw e;
        } finally {
            if (keys != null) {
                try {
                    keys.close();
                } catch (SQLException ex) {
                    // ignore
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
    }
    
    public void delete(Rule rule) throws SQLException {
        String sql = "DELETE FROM rules WHERE rule_name = ? AND topic_id = ?";
        PreparedStatement pstmt = null;
        try {
            super.getHolder().getInputDBHelper().deleteByRuleId(rule.getId());
            super.getHolder().getOutputDBHelper().deleteByRuleId(rule.getId());
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, rule.getName());
            pstmt.setLong(2, rule.getTopic().getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the rule: name: " + rule.getName() + ", topic: " + rule.getTopic().getTopicName(), e);
            throw e;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException ex) {
                    // ignore
                }
            }
        }
    }

    
    public Rule getById(Long id) throws SQLException {
        String sql = "SELECT id, rule_name, rank FROM rules WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Rule rule = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                rule = new Rule();
                rule.setId(rs.getLong("id"));
                rule.setName(rs.getString("rule_name"));
                rule.setRank(rs.getLong("rank"));
            } 
        } catch (SQLException e) {
            log.error("Error during selecting the rule: id " + id, e);
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
        return rule;
    }    
    
    public List<Rule> getByTopic(Topic topic) throws SQLException {
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
}
