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
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Rule;

public class OutputDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(OutputDBHelper.class);
    
    public void save(Output output) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO outputs (rule_id, output_text, request) VALUES (?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet keys = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, output.getRule().getId());
            pstmt.setString(2, output.getText());
            pstmt.setString(3, output.getRequest());
            pstmt.executeUpdate();
            keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            output.setId(key);
        } catch (SQLException e) {
            log.error("Error during save output: " + output.getText()
                    + ", rule: " + output.getRule().getId() + "|"
                    + output.getRule().getName(), e);
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

    public void delete(Output output) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM outputs WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, output.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the output: " + output.getId(), e);
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

    public void deleteByRuleId(Long ruleId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM outputs WHERE rule_id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ruleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the outputs by role id: " + ruleId, e);
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

    public Output getById(Long id) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, output_text, request, rule_id FROM outputs WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Output output = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                output = new Output();
                output.setId(rs.getLong("id"));
                output.setText(rs.getString("output_text"));
                output.setRequest(rs.getString("request"));
                Rule rule = new Rule();
                rule.setId(rs.getLong("rule_id"));
                output.setRule(rule);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the output: id " + id, e);
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
        return output;
    }
    
    public List<Output> findByRuleId(Long ruleId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, output_text, request, rule_id FROM outputs WHERE rule_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Output> outputs = new ArrayList<Output>();
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ruleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Output output = new Output();
                output.setId(rs.getLong("id"));
                output.setText(rs.getString("output_text"));
                output.setRequest(rs.getString("request"));
                Rule rule = new Rule();
                rule.setId(rs.getLong("rule_id"));
                output.setRule(rule);
                outputs.add(output);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the output: rule_id " + ruleId, e);
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
        return outputs;
    }
}
