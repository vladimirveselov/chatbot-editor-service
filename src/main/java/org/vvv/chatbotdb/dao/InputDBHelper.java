package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class InputDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(InputDBHelper.class);
    
    public void save(Input input) throws SQLException {
        String sql = "INSERT INTO inputs (rule_id, input_text) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        ResultSet keys = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, input.getRule().getId());
            pstmt.setString(2, input.getText());
            pstmt.executeUpdate();
            keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            input.setId(key);
            super.getHolder().getSplitInputDBHelper().saveInput(input);
        } catch (SQLException e) {
            log.error("Error during save input: " + input.getText()
                    + ", rule: " + input.getRule().getId() + "|"
                    + input.getRule().getName(), e);
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

    public void delete(Input input) throws SQLException {
        String sql = "DELETE FROM inputs WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            super.getHolder().getSplitInputDBHelper().deleteByInputId(input.getId());
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, input.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the input: " + input.getId(), e);
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

    public void deleteByRuleId(Long ruleId) throws SQLException {
        String sql = "DELETE FROM inputs WHERE rule_id = ?";
        PreparedStatement pstmt = null;
        try {
            super.getHolder().getSplitInputDBHelper().deleteByRuleId(ruleId);
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ruleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the inputs by role id: " + ruleId, e);
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

    public Input getById(Long id) throws SQLException {
        String sql = "SELECT id, input_text, rule_id FROM inputs WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Input input = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                input = new Input();
                input.setId(rs.getLong("id"));
                input.setText(rs.getString("input_text"));
                Rule rule = new Rule();
                rule.setId(rs.getLong("rule_id"));
                input.setRule(rule);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the input: id " + id, e);
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
        return input;
    }
    
    public Set<Input> getByRule(Rule rule) throws SQLException {
        String sql = "SELECT id, input_text, rule_id FROM inputs WHERE rule_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Set<Input> inputs = new HashSet<Input>();
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, rule.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Input input = new Input();
                input.setId(rs.getLong("id"));
                input.setText(rs.getString("input_text"));
                input.setRule(rule);
                inputs.add(input);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the inputs: rule id " + rule.getId(), e);
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
        return inputs;
    }

}
