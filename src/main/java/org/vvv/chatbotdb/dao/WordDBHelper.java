package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WordDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(WordDBHelper.class);

    public void save(Word word) throws SQLException {
        String sql = "INSERT INTO words (word, syn_to_id) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, word.getWord());
            if (word.getSynonymTo() != null) {
                pstmt.setLong(2, word.getSynonymTo());
            } else {
                pstmt.setNull(2, java.sql.Types.BIGINT);
            }
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            word.setId(key);
        } catch (SQLException e) {
            log.error("Error during save word: " + word.getWord()
                    + ", synonym to: " + word.getSynonymTo(), e);
            throw e;
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
    }

    public void update(Word word) throws SQLException {
        String sql = "UPDATE words SET syn_to_id = ? WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            if (word.getSynonymTo() != null) {
                pstmt.setLong(1, word.getSynonymTo());
            } else {
                pstmt.setNull(1, java.sql.Types.BIGINT);
            }
            pstmt.setLong(2, word.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(
                    "Error during save updating word: "
                            + word.getWord() + ", syn to: "
                            + word.getSynonymTo() + " id:" + word.getId(), e);
            throw e;
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
    }

    public void delete(Word word) throws SQLException {
        String sql = "DELETE FROM words WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, word.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(
                    "Error during deleting the word: "
                            + word.getId(), e);
            throw e;
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
    }

    public Word getById(Long id) throws SQLException {
        String sql = "SELECT id, word, syn_to_id FROM words WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Word word = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                word = new Word();
                word.setId(rs.getLong("id"));
                word.setWord(rs.getString("word"));
                word.setSynonymTo(rs.getLong("syn_to_id"));
            }
        } catch (SQLException e) {
            log.error("Error during get word by id: " + id, e);
            throw e;
        } finally {
            try {
                rs.close();
            } catch (Exception ex) {

            }
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
        return word;
    }

}
