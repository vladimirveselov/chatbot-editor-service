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


public class ChatbotDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(ChatbotDBHelper.class);
    
    public Chatbot save(Chatbot chatbot) throws SQLException {
        String sql = "INSERT INTO chatbots(chatbot_name) VALUES (?)";
        PreparedStatement pstmt = null;
        ResultSet keys = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, chatbot.getName());
            pstmt.executeUpdate();
            keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            chatbot.setId(key);
        } catch (SQLException e) {
            log.error("Error during save chabot: " + chatbot.getName(), e);
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
        return chatbot;
    }

    public void delete(Chatbot chatbot) throws SQLException {
        String sql = "DELETE FROM chatbots WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatbot.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the chatbot: " + chatbot.getId(), e);
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
    
    public void delete(String chatbotName) throws SQLException {
    	this.unlinkTopics(chatbotName);
        String sql = "DELETE FROM chatbots WHERE chatbot_name = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, chatbotName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the chatbot: " + chatbotName, e);
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

    public void unlinkTopics(String chatbotName) throws SQLException {
        String sql = "DELETE FROM topics_chatbots WHERE chatbot_id = (SELECT id FROM chatbots "
        		+ "WHERE chatbot_name = ?)";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, chatbotName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting the chatbot topics links: " + chatbotName, e);
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
    
    public Chatbot getById(Long id) throws SQLException {
        String sql = "SELECT id, chatbot_name FROM chatbots WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Chatbot chatbot = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                chatbot = new Chatbot();
                chatbot.setId(rs.getLong("id"));
                chatbot.setName(rs.getString("chatbot_name"));
            }
        } catch (SQLException e) {
            log.error("Error during selecting the chatbot: id " + id, e);
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
        return chatbot;
    }
    
    public void link(Chatbot chatbot, Topic topic) throws SQLException {
        String sql = "INSERT INTO topics_chatbots(chatbot_id, topic_id) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatbot.getId());
            pstmt.setLong(2, topic.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during link chatbot and topic: " + chatbot.getId() + " topic:" + topic.getId(), e);
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
    
    public void unlink(Chatbot chatbot, Topic topic) throws SQLException {
        String sql = "DELETE FROM topics_chabots WHERE chatbot_id = ? and topic_id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatbot.getId());
            pstmt.setLong(2, topic.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during unlink the chabot: " + chatbot.getId() + " topic:" + topic.getId(), e);
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
    
    public List<Chatbot> list() throws SQLException {
    	List<Chatbot> chatbots = new ArrayList<Chatbot>();
    	String sql = "SELECT id, chatbot_name FROM chatbots";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Chatbot chatbot = new Chatbot();
                chatbot.setId(rs.getLong("id"));
                chatbot.setName(rs.getString("chatbot_name"));
                chatbots.add(chatbot);
            }
        } catch (SQLException e) {
            log.error("Error list chatbots", e);
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
        return chatbots;
    }
    
}
