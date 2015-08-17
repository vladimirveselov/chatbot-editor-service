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

	public Output save(Output output, Rule rule) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "INSERT INTO outputs (rule_id, output_text, request) VALUES (?, ?, ?)";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql,
				Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setLong(1, rule.getId());
			pstmt.setString(2, output.getText());
			pstmt.setString(3, output.getRequest());
			pstmt.executeUpdate();
			try (ResultSet keys = pstmt.getGeneratedKeys()) {
				keys.next();
				Long key = keys.getLong(1);
				output.setId(key);
			}
		} catch (SQLException e) {
			log.error("Error during save output: " + output.getText()
					+ ", rule: " + rule.getId() + "|"
					+ rule.getName(), e);
			throw e;
		}
		return output;
	}

	public void update(Output output) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "UPDATE "
				+ " outputs SET "
				+ " output_text = ?,"
				+ " request = ?"
				+ " WHERE id = ?";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, output.getText());
			pstmt.setString(2, output.getRequest());
			pstmt.setLong(3, output.getId());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the input: " + output.getId(), e);
			throw e;
		}
	}

	public void delete(Long outputId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "DELETE FROM outputs WHERE id = ?";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, outputId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error("Error during deleting the output: " + outputId, e);
			throw e;
		}
	}

	public void deleteByRuleId(Long ruleId) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "DELETE FROM outputs WHERE rule_id = ?";
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, ruleId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			log.error(
					"Error during deleting the outputs by role id: " + ruleId,
					e);
			throw e;
		}
	}

	public Output getById(Long id) throws SQLException, InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		String sql = "SELECT "
		        + "  o.id id, "
		        + "  o.output_text output_text,"
		        + "  o.request request, "
		        + "  o.rule_id rule_id,"
		        + "  r.rule_name rule_name,"
		        + "  t.topic_name "
		        + " FROM "
		        + "  outputs o,"
		        + "  rules r,"
		        + "  topics t"
		        + " WHERE "
		        + "  r.topic_id = t.id AND"
		        + "  o.rule_id = r.id AND"
		        + "  o.id = ?";
		Output output = null;
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, id);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					output = new Output();
					output.setId(rs.getLong("id"));
					output.setText(rs.getString("output_text"));
					output.setRequest(rs.getString("request"));
                    output.setRuleName(rs.getString("rule_name"));
                    output.setTopicName(rs.getString("topic_name"));
				}
			}
		} catch (SQLException e) {
			log.error("Error during selecting the output: id " + id, e);
			throw e;
		}
		return output;
	}

	public List<Output> findByRule(Rule rule) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		String sql = "SELECT id, output_text, request, rule_id FROM outputs WHERE rule_id = ?";
		List<Output> outputs = new ArrayList<Output>();
		Connection conn = super.getDbHelper().getConnection();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setLong(1, rule.getId());
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Output output = new Output();
					output.setId(rs.getLong("id"));
					output.setText(rs.getString("output_text"));
					output.setRequest(rs.getString("request"));
					output.setRuleName(rule.getName());
					output.setTopicName(rule.getTopicName());
					outputs.add(output);
				}
			}
		} catch (SQLException e) {
			log.error("Error during selecting the output: rule_name " + rule.getName(), e);
			throw e;
		}
		return outputs;
	}

	public List<Output> getByTopicNameAndRuleName(String topicName,
			String ruleName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		String sql = "SELECT" 
				+ " o.id id, " 
				+ " o.output_text output_text,"
				+ " o.request request," 
				+ " o.rule_id rule_id,"
				+ " r.rule_name rule_name" 
				+ " FROM " 
				+ " outputs o,"
				+ " rules r," 
				+ " topics t" 
				+ " WHERE "
				+ " o.rule_id = r.id AND" 
				+ " r.topic_id = t.id AND"
				+ " t.topic_name = ? AND" 
				+ " r.rule_name = ?";
		Connection conn = super.getDbHelper().getConnection();
		List<Output> outputs = new ArrayList<Output>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, topicName);
			pstmt.setString(2, ruleName);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					Output output = new Output();
					output.setId(rs.getLong("id"));
					output.setText(rs.getString("output_text"));
					output.setRequest(rs.getString("request"));
					output.setRuleName(ruleName);
					output.setTopicName(topicName);
					outputs.add(output);
				}
			}
		} catch (SQLException e) {
			log.error("Error during selecting the outputs: topic " + topicName
					+ " rule " + ruleName, e);
			throw e;
		}
		return outputs;

	}
}
