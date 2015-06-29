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
import org.vvv.chatbotdb.utils.StringUtils;

public class SplitQueryDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(SplitQueryDBHelper.class);
    
    private StringUtils stringUtils = new StringUtils();

    public void save(SplitQuery splitQuery) throws SQLException {
        String sql = "INSERT INTO split_queries (query_id, prev_word, word, next_word) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        ResultSet keys = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, splitQuery.getQuery().getId());
            pstmt.setString(2, splitQuery.getPrevWord());
            pstmt.setString(3, splitQuery.getWord());
            pstmt.setString(4, splitQuery.getNextWord());
            pstmt.executeUpdate();
            keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            splitQuery.setId(key);
        } catch (SQLException e) {
            log.error("Error during save split query: " + splitQuery.getQuery().getText(), e);
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

    public void delete(SplitQuery splitQuery) throws SQLException {
        String sql = "DELETE FROM split_queries WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, splitQuery.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the split query: " + splitQuery.getId(), e);
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

    public void deleteByQueryId(Long queryId) throws SQLException {
        String sql = "DELETE FROM split_queries WHERE query_id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, queryId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the split queries by query_id: " + queryId, e);
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

    public SplitQuery getById(Long id) throws SQLException {
        String sql = "SELECT id, query_id, prev_word, word, next_word FROM split_queries WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        SplitQuery splitQuery = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                splitQuery = new SplitQuery();
                splitQuery.setId(rs.getLong("id"));
                splitQuery.setPrevWord(rs.getString("prev_word"));
                splitQuery.setWord(rs.getString("word"));
                splitQuery.setNextWord(rs.getString("next_word"));
                Query query = new Query();
                query.setId(rs.getLong("query_id"));
                splitQuery.setQuery(query);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the split query: id " + id, e);
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
        return splitQuery;
    }
    
    public Set<SplitQuery> getByQuery(Query query) throws SQLException {
        String sql = "SELECT id, prev_word, word, next_word FROM split_queries WHERE query_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Set<SplitQuery> set = new HashSet<SplitQuery>();
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, query.getId());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                SplitQuery splitQuery = new SplitQuery();
                splitQuery.setId(rs.getLong("id"));
                splitQuery.setPrevWord(rs.getString("prev_word"));
                splitQuery.setWord(rs.getString("word"));
                splitQuery.setNextWord(rs.getString("next_word"));
                splitQuery.setQuery(query);
                set.add(splitQuery);
            }
        } catch (SQLException e) {
            log.error("Error during selecting the split query: " + query.getText(), e);
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
        return set;
    }
    
    public void saveQuery(Query query) throws SQLException {
        List<String> words = this.stringUtils.split(query.getText().toLowerCase());
        for (int i=0; i<words.size(); i++) {
            SplitQuery splitQuery = new SplitQuery();
            splitQuery.setQuery(query);
            if (i>0) {
                splitQuery.setPrevWord(words.get(i-1));
            }
            splitQuery.setWord(words.get(i));
            if (i<words.size()-1) {
                splitQuery.setNextWord(words.get(i+1));
            }
            this.save(splitQuery);
        }
    }
    

}
