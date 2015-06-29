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

public class TopicDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(TopicDBHelper.class);

    public void save(Topic topic) throws SQLException {
        String sql = "INSERT INTO topics (topic_name, rank) VALUES (?, ?)";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, topic.getTopicName());
            pstmt.setLong(2, topic.getRank());
            pstmt.executeUpdate();
            ResultSet keys = pstmt.getGeneratedKeys();
            keys.next();
            Long key = keys.getLong(1);
            topic.setId(key);
        } catch (SQLException e) {
            log.error("Error during save topic: name: " + topic.getTopicName()
                    + ", rank: " + topic.getRank(), e);
            throw e;
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
    }

    public void update(Topic topic) throws SQLException {
        String sql = "UPDATE topics SET topic_name = ?, rank = ? WHERE id = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, topic.getTopicName());
            pstmt.setLong(2, topic.getRank());
            pstmt.setLong(3, topic.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(
                    "Error during save updating topic: name: "
                            + topic.getTopicName() + ", rank: "
                            + topic.getRank() + " id:" + topic.getId(), e);
            throw e;
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
    }

    public void delete(Topic topic) throws SQLException {
        String sql = "DELETE FROM topics WHERE topic_name = ?";
        PreparedStatement pstmt = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, topic.getTopicName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(
                    "Error during deleting the topic: name: "
                            + topic.getTopicName() + ", rank: "
                            + topic.getRank(), e);
            throw e;
        } finally {
            try {
                pstmt.close();
            } catch (Exception ex) {

            }
        }
    }

    public Topic getById(Long id) throws SQLException {
        String sql = "SELECT id, topic_name, rank FROM TOPICS WHERE id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Topic topic = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                topic = new Topic();
                topic.setId(rs.getLong("id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setRank(rs.getLong("rank"));
            }
        } catch (SQLException e) {
            log.error("Error during get topic by id: " + id, e);
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
        return topic;
    }

    public Topic getByName(String name) throws SQLException {
        String sql = "SELECT id, topic_name, rank FROM TOPICS WHERE topic_name = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Topic topic = null;
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                topic = new Topic();
                topic.setId(rs.getLong("id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setRank(rs.getLong("rank"));
            }
        } catch (SQLException e) {
            log.error("Error get the topic by name: " + name, e);
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
        return topic;
    }

    public List<Topic> getAll() throws SQLException {
        String sql = "SELECT id, topic_name, rank FROM topics";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Topic> topics = new ArrayList<Topic>();
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getLong("id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setRank(rs.getLong("rank"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            log.error("Error list topics", e);
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
        return topics;
    }

    public List<Topic> getByChatbotId(Long chatbotId) throws SQLException {
        String sql = "SELECT t.id id, "
                + " t.topic_name topic_name, "
                + " t.rank rank FROM "
                + " topics t, topics_chatbots tc"
                + " WHERE "
                + " t.id = tc.topic_id AND"
                + " tc.chatbot_id = ?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Topic> topics = new ArrayList<Topic>();
        try {
            Connection conn = super.getDbHelper().getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, chatbotId);            
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Topic topic = new Topic();
                topic.setId(rs.getLong("id"));
                topic.setTopicName(rs.getString("topic_name"));
                topic.setRank(rs.getLong("rank"));
                topics.add(topic);
            }
        } catch (SQLException e) {
            log.error("Error list topics. Chatbot id:" + chatbotId, e);
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
        return topics;
    }

}
