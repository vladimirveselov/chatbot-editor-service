package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class InputDBHelper extends DBObject {

	private static Log log = LogFactory.getLog(InputDBHelper.class);

	public Input save(Input input) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String sql = "INSERT INTO inputs (rule_id, input_text) VALUES (?, ?)";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setLong(1, input.getRule().getId());
			pstmt.setString(2, input.getText());
			pstmt.executeUpdate();
			try (ResultSet keys = pstmt.getGeneratedKeys()) {
				keys.next();
				Long key = keys.getLong(1);
				input.setId(key);
			}
			super.getHolder().getSplitInputDBHelper().saveInput(input);
		} catch (SQLException e) {
			log.error("Error during save input: " + input.getText()
					+ ", rule: " + input.getRule().getId() + "|"
					+ input.getRule().getName(), e);
			throw e;
		}
		return input;
	}

	public void delete(Input input) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "DELETE FROM inputs WHERE id = ?";
		super.getHolder().getSplitInputDBHelper()
				.deleteByInputId(input.getId());
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, input.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the input: " + input.getId(), e);
			throw e;
		}
	}

	public void update(Input input) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "UPDATE inputs SET input_text = ? WHERE id = ?";
		super.getHolder().getSplitInputDBHelper()
				.deleteByInputId(input.getId());
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, input.getText());
			pstmt.setLong(2, input.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the input: " + input.getId(), e);
			throw e;
		}
		super.getHolder().getSplitInputDBHelper().saveInput(input);
	}

	public void deleteByRuleId(Long ruleId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "DELETE FROM inputs WHERE rule_id = ?";
		PreparedStatement pstmt = null;
		try {
			super.getHolder().getSplitInputDBHelper().deleteByRuleId(ruleId);
			Connection conn = super.getDbHelper().getConnection();
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, ruleId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the inputs by role id: " + ruleId,
					e);
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

	public Input getById(Long id) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
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

	public Set<Input> getByRule(Rule rule) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "SELECT id, input_text, rule_id FROM inputs WHERE rule_id = ?";
		Set<Input> inputs = new HashSet<Input>();
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, rule.getId());
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Input input = new Input();
					input.setId(rs.getLong("id"));
					input.setText(rs.getString("input_text"));
					input.setRule(rule);
					inputs.add(input);
				}
			}
		} catch (SQLException e) {
			log.error(
					"Error during selecting the inputs: rule id "
							+ rule.getId(), e);
			throw e;
		}
		return inputs;
	}

	public List<Input> getByTopicNameAndRuleName(String topicName,
			String ruleName) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String sql = "SELECT " + " i.id id, " + " i.input_text input_text,"
				+ " i.rule_id rule_id," + " r.rule_name rule_name,"
				+ " r.topic_id topic_id," + " t.topic_name topic_name"
				+ " FROM " + " inputs i," + " rules r," + " topics t"
				+ " WHERE " + " i.rule_id = r.id AND "
				+ " r.topic_id = t.id AND" + " r.rule_name = ? AND"
				+ " t.topic_name = ?";
		List<Input> inputs = new ArrayList<Input>();
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, ruleName);
			pstmt.setString(2, topicName);
			Rule rule = null;
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Input input = new Input();
					input.setId(rs.getLong("id"));
					input.setText(rs.getString("input_text"));
					if (rule == null) {
						rule = new Rule();
						rule.setId(rs.getLong("rule_id"));
						rule.setName(rs.getString("rule_name"));
						Topic topic = new Topic();
						topic.setId(rs.getLong("topic_id"));
						topic.setTopicName(rs.getString("topic_name"));
						rule.setTopic(topic);
					}
					input.setRule(rule);
					inputs.add(input);
				}
			}
		} catch (SQLException e) {
			log.error("Error during selecting the inputs: rule name "
					+ ruleName + " topic name -" + topicName, e);
			throw e;
		}
		return inputs;
	}

}
