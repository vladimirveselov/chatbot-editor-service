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
import org.vvv.chatbotdb.model.Action;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class ActionDBHelper extends DBObject {

	private static Log log = LogFactory.getLog(ActionDBHelper.class);

	public Action save(Action action) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String sql = "INSERT INTO actions (rule_id, action_body, priority) VALUES (?, ?, ?)";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setLong(1, action.getRule().getId());
			pstmt.setString(2, action.getActionBody());
			pstmt.setLong(3, action.getPriority());
			pstmt.executeUpdate();
			try (ResultSet keys = pstmt.getGeneratedKeys()) {
				keys.next();
				Long key = keys.getLong(1);
				action.setId(key);
			}
		} catch (SQLException e) {
			log.error("Error during save action: " + action.getActionBody()
					+ ", rule: " + action.getRule().getId() + "|"
					+ action.getRule().getName(), e);
			throw e;
		}
		return action;
	}

	public void delete(Action action) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "DELETE FROM actions WHERE id = ?";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, action.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the action: " + action.getId(), e);
			throw e;
		}
	}

	public void update(Action action) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "UPDATE actions SET "
		        + " action_body = ?,"
		        + " priority = ? "
		        + " WHERE id = ?";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, action.getActionBody());
			pstmt.setLong(2, action.getPriority());
			pstmt.setLong(3, action.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during updating the action: " + action.getId(), e);
			throw e;
		}
	}

	public void deleteByRuleId(Long ruleId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "DELETE FROM actions WHERE rule_id = ?";
        Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setLong(1, ruleId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the actions by role id: " + ruleId,e);
			throw e;
		}
	}

	public Action getById(Long id) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String sql = "SELECT id, rule_id, action_body, priority FROM actions WHERE id = ?";
		Connection conn = super.getDbHelper().getConnection();
		Action action = null;
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, id);
			try(ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					action = new Action();
					action.setId(rs.getLong("id"));
					action.setActionBody(rs.getString("action_body"));
					action.setPriority(rs.getLong("priority"));
					Rule rule = new Rule();
					rule.setId(rs.getLong("rule_id"));
					action.setRule(rule);
				}
			}
		} catch (SQLException e) {
			log.error("Error during selecting the action: id " + id, e);
			throw e;
		} 
		return action;
	}

	public void getByRule(Rule rule) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "SELECT id, rule_id, action_body, priority FROM actions WHERE rule_id = ?";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, rule.getId());
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Action action = new Action();
					action.setId(rs.getLong("id"));
					action.setActionBody(rs.getString("action_body"));
					action.setPriority(rs.getLong("priority"));
					action.setRule(rule);
					rule.getActions().add(action);
				}
			}
		} catch (SQLException e) {
			log.error(
					"Error during selecting the inputs: rule id "
							+ rule.getId(), e);
			throw e;
		}
	}

	public List<Action> getByTopicNameAndRuleName(String topicName,
			String ruleName) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String sql = "SELECT " 
			+ " a.id id, " 
		    + " a.action_body action_body,"
		    + " a.priority priority,"
			+ " a.rule_id rule_id," 
		    + " r.rule_name rule_name,"
			+ " r.topic_id topic_id," 
			+ " t.topic_name topic_name"
			+ " FROM " 
			+ " actions a," 
			+ " rules r," 
			+ " topics t"
			+ " WHERE " 
			+ " a.rule_id = r.id AND "
			+ " r.topic_id = t.id AND" 
			+ " r.rule_name = ? AND"
			+ " t.topic_name = ?";
		List<Action> actions = new ArrayList<Action>();
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, ruleName);
			pstmt.setString(2, topicName);
			Rule rule = null;
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Action action = new Action();
					action.setId(rs.getLong("id"));
					action.setActionBody(rs.getString("action_body"));
					action.setPriority(rs.getLong("priority"));
					if (rule == null) {
						rule = new Rule();
						rule.setId(rs.getLong("rule_id"));
						rule.setName(rs.getString("rule_name"));
						Topic topic = new Topic();
						topic.setId(rs.getLong("topic_id"));
						topic.setTopicName(rs.getString("topic_name"));
						rule.setTopicName(topic.getTopicName());
					}
					action.setRule(rule);
					actions.add(action);
				}
			}
		} catch (SQLException e) {
			log.error("Error during selecting the inputs: rule name "
					+ ruleName + " topic name -" + topicName, e);
			throw e;
		}
		return actions;
	}

}
