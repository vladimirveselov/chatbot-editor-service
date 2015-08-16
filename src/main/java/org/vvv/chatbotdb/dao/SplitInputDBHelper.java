package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.SplitInput;
import org.vvv.chatbotdb.utils.StringUtils;

public class SplitInputDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(SplitInputDBHelper.class);
    
    private StringUtils stringUtils = new StringUtils();

    public void save(SplitInput splitInput) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO split_inputs (input_id, prev_word, word, next_word) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet keys = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, splitInput.getInput().getId());
            pstmt.setString(2, splitInput.getPrevWord());
            pstmt.setString(3, splitInput.getWord());
            pstmt.setString(4, splitInput.getNextWord());
            pstmt.executeUpdate();
            keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            splitInput.setId(key);
        } catch (SQLException e) {
            log.error("Error during save split_input: " + splitInput.getInput().getText()
                    + ", rule: " + splitInput.getInput().getRuleName(), e);
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

    public void delete(SplitInput splitInput) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM split_inputs WHERE id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, splitInput.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the split input: " + splitInput.getId(), e);
            throw e;
        } 
    }

    public void deleteByInputId(Long inputId) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM split_inputs WHERE input_id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, inputId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the split inputs by input id: " + inputId, e);
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
        String sql = "DELETE FROM split_inputs WHERE input_id in (SELECT id FROM inputs WHERE rule_id = ?)";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, ruleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the split inputs by rule id: " + ruleId, e);
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
    public SplitInput getById(Long id) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, input_id, prev_word, word, next_word FROM split_inputs WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SplitInput splitInput = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                splitInput = new SplitInput();
                splitInput.setId(rs.getLong("id"));
                splitInput.setPrevWord(rs.getString("prev_word"));
                splitInput.setWord(rs.getString("word"));
                splitInput.setNextWord(rs.getString("next_word"));
                Input input = new Input();
                input.setId(rs.getLong("input_id"));
                splitInput.setInput(input);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the split input: id " + id, e);
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
        return splitInput;
    }
    
    public Set<SplitInput> getByInput(Input input) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        String sql = "SELECT id, prev_word, word, next_word FROM split_inputs WHERE input_id = ?";
        Set<SplitInput> set = new HashSet<SplitInput>();
        Connection conn = super.getDbHelper().getConnection();
        try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, input.getId());
            try(ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SplitInput splitInput = new SplitInput();
                    splitInput.setId(rs.getLong("id"));
                    splitInput.setPrevWord(rs.getString("prev_word"));
                    splitInput.setWord(rs.getString("word"));
                    splitInput.setNextWord(rs.getString("next_word"));
                    splitInput.setInput(input);
                    set.add(splitInput);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting the split input: " + input.getText() + "| rule" + input.getRuleName(), e);
            throw e;
        }
        return set;
    }
    
    public void saveInput(Input input) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        List<String> words = this.stringUtils.split(input.getText());
        for (int i=0; i<words.size(); i++) {
            SplitInput splitInput = new SplitInput();
            splitInput.setInput(input);
            if (i>0) {
                splitInput.setPrevWord(words.get(i-1));
            }
            splitInput.setWord(words.get(i));
            if (i<words.size()-1) {
                splitInput.setNextWord(words.get(i+1));
            }
            this.save(splitInput);
        }
    }
    

}
