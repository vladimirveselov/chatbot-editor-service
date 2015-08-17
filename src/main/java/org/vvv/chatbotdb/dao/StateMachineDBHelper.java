package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.Action;
import org.vvv.chatbotdb.model.ActionResult;
import org.vvv.chatbotdb.model.SMAction;
import org.vvv.chatbotdb.model.SMCondition;
import org.vvv.chatbotdb.model.SMMemory;
import org.vvv.chatbotdb.model.SMRule;
import org.vvv.chatbotdb.model.SMVariable;
import org.vvv.chatbotdb.model.StateMachine;

public class StateMachineDBHelper extends DBObject {

    private static Log log = LogFactory.getLog(StateMachineDBHelper.class);

    public SMVariable saveSMVariable(SMVariable var, StateMachine stateMachine)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO sm_variables (state_machine_id, sm_variable_name) VALUES (?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, stateMachine.getId());
            pstmt.setString(2, var.getName());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                Long key = keys.getLong(1);
                var.setId(key);
            }
        } catch (SQLException e) {
            log.error("Error during save SMVariable: " + var.getName()
                    + ", state machine: " + stateMachine.getName(), e);
            throw e;
        }
        return var;
    }

    public void deleteSMVariableByStateMachineId(Long stateMachineId)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM sm_variables WHERE state_machine_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, stateMachineId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error(
                    "Error during deleting sm_variable for state machine id: "
                            + stateMachineId, e);
            throw e;
        }
    }

    public SMCondition saveSMCondition(SMCondition condition, SMRule rule,
            SMVariable var) throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO sm_conditions " + "( sm_rule_id,"
                + "  sm_variable_id," + "  sm_variable_value) VALUES (?, ?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, rule.getId());
            pstmt.setLong(2, var.getId());
            pstmt.setBoolean(3, condition.getValue());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                Long key = keys.getLong(1);
                condition.setId(key);
            }
        } catch (SQLException e) {
            log.error("Error during save SMCondition: " + condition.toString(),
                    e);
            throw e;
        }
        return condition;
    }

    public void deleteSMConditionByRuleId(Long ruleId) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "DELETE FROM sm_conditions WHERE " + " sm_rule_id = ? ";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ruleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting sm conditions for rule id: "
                    + ruleId, e);
            throw e;
        }
    }

    public SMRule saveSMRule(SMRule smRule,
            Map<String, SMVariable> variablesMap,
            Map<String, SMAction> actionsMap, StateMachine stateMachine)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO sm_rules " + "( state_machine_id,"
                + "  sm_rule_name ) VALUES (?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, stateMachine.getId());
            pstmt.setString(2, smRule.getName());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                Long key = keys.getLong(1);
                smRule.setId(key);
            }
        } catch (SQLException e) {
            log.error("Error during save SMRule: " + smRule.toString(), e);
            throw e;
        }
        for (SMCondition condition : smRule.getConditions()) {
            this.saveSMCondition(condition, smRule,
                    variablesMap.get(condition.getVariableName()));
        }
        for (String actionName : smRule.getActionNames()) {
            this.attachSMActionToSMRule(actionsMap.get(actionName), smRule);
        }
        return smRule;
    }

    public void deleteSMRulesByStateMachineId(Long stateMachineId)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM sm_rules WHERE state_machine_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, stateMachineId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting sm_rule for state machine id: "
                    + stateMachineId, e);
            throw e;
        }
    }

    public SMAction saveSMAction(SMAction smAction, StateMachine stateMachine) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "INSERT INTO sm_actions " + "( state_machine_id,"
                + "  sm_action_name , action_text ) VALUES (?, ?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, stateMachine.getId());
            pstmt.setString(2, smAction.getName());
            pstmt.setString(3, smAction.getActionScript());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                Long key = keys.getLong(1);
                smAction.setId(key);
            }
        } catch (SQLException e) {
            log.error("Error during save SMAction: " + smAction.toString(), e);
            throw e;
        }
        return smAction;
    }

    public void deleteSMActionsByStateMachineId(Long stateMachineId)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM sm_actions WHERE state_machine_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, stateMachineId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting sm_action for state machine id: "
                    + stateMachineId, e);
            throw e;
        }
    }

    public void attachSMActionToSMRule(SMAction smAction, SMRule smRule)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "INSERT INTO sm_actions_rules "
                + "( sm_rule_id, sm_action_id ) VALUES (?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, smRule.getId());
            pstmt.setLong(2, smAction.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during save SMAction: " + smAction.toString(), e);
            throw e;
        }
    }

    public void detachSMActionFromSMRule(SMAction smAction, SMRule smRule)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM sm_actions_rules WHERE sm_rule_id = ? and sm_action_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, smRule.getId());
            pstmt.setLong(2, smAction.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting sm_action_rule for rule : "
                    + smRule.toString() + " action " + smAction.toString(), e);
            throw e;
        }
    }

    public void detachAllSMActionsFromSMRule(SMRule smRule)
            throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        String sql = "DELETE FROM sm_actions_rules WHERE sm_rule_id = ? ";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, smRule.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting sm_action_rule for rule : "
                    + smRule.toString(), e);
            throw e;
        }
    }

    public StateMachine saveStateMachine(StateMachine sm) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "INSERT INTO state_machines (state_machine_name) VALUES (?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, sm.getName());
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                Long key = keys.getLong(1);
                sm.setId(key);
            }
        } catch (SQLException e) {
            log.error("Error during save state machine: " + sm.getName(), e);
            throw e;
        }
        Map<String, SMVariable> variablesMap = new HashMap<String, SMVariable>();
        for (SMVariable variable : sm.getVariables()) {
            this.saveSMVariable(variable, sm);
            variablesMap.put(variable.getName(), variable);
        }
        Map<String, SMAction> actionsMap = new HashMap<String, SMAction>();
        for (SMAction action : sm.getActions()) {
            this.saveSMAction(action, sm);
            actionsMap.put(action.getName(), action);
        }
        for (SMRule rule : sm.getRules()) {
            this.saveSMRule(rule, variablesMap, actionsMap, sm);
        }
        return sm;
    }

    public void deleteStateMachine(StateMachine sm) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        log.info("deleting state machine: " + sm.getName());
        if (!this.stateMachineExistis(sm.getName())) {
            log.info("state machine doesn't exist : " + sm.getName());
            return;
        }
        for (SMRule rule : sm.getRules()) {
            this.deleteSMConditionByRuleId(rule.getId());
            this.detachAllSMActionsFromSMRule(rule);
        }
        this.deleteSMRulesByStateMachineId(sm.getId());
        this.deleteSMActionsByStateMachineId(sm.getId());
        this.deleteSMVariableByStateMachineId(sm.getId());

        String sql = "DELETE FROM state_machines WHERE id = ? ";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sm.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during deleting state machine : " + sm.toString(),
                    e);
            throw e;
        }
    }

    public void deleteStateMachine(String stateMachine) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        StateMachine sm = this.getStateMachine(stateMachine);
        if (sm == null) {
            return;
        }
        this.deleteStateMachine(sm);
    }

    public SMMemory saveSMMemory(SMMemory memory) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        log.info("Save memory:" + memory);
        String sql = "INSERT INTO sm_memory ("
                + " session_id, "
                + " sm_variable_name, "
                + " sm_variable_value, " 
                + " short_string_value, "
                + " long_string_value, "
                + " sm_last_modified) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, memory.getSessionId());
            pstmt.setString(2, memory.getSmVariableName());
            if (memory.getValue() == null) {
                pstmt.setNull(3, Types.BOOLEAN);
            } else {
                pstmt.setBoolean(3, memory.getValue());
            }
            if (memory.getShortStringValue() == null) {
                pstmt.setNull(4, Types.VARCHAR);
            } else {
                pstmt.setString(4, memory.getShortStringValue());
            }
            if (memory.getLongStringValue() == null) {
                pstmt.setNull(5, Types.VARCHAR);
            } else {
                pstmt.setString(5, memory.getLongStringValue());
            }
            pstmt.setDate(6,  new java.sql.Date(System.currentTimeMillis()));
            pstmt.executeUpdate();
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                keys.next();
                Long key = keys.getLong(1);
                memory.setId(key);
            }
        } catch (SQLException e) {
            log.error("Error during save SMMemory: " + memory + " sql:" + sql, e);
            throw e;
        }
        return memory;
    }

    public SMMemory getMemory(String sessionId, String variableName) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "SELECT " 
                + "   m.id id," 
                + "   m.session_id session_id,"
                + "   m.state_machine_id state_machine_id,"
                + "   m.sm_variable_name sm_variable_name,"
                + "   m.sm_variable_value sm_variable_value,"
                + "   m.short_string_value short_string_value,"
                + "   m.long_string_value long_string_value,"
                + "   m.sm_last_modified sm_last_modified" 
                + " FROM "
                + "   sm_memory m" 
                + " WHERE"
                + "   m.session_id = ? AND" 
                + "   m.sm_variable_name = ? ";
        Connection conn = super.getDbHelper().getConnection();
        SMMemory memory = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sessionId);
            pstmt.setString(2, variableName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    memory = new SMMemory();
                    memory.setId(rs.getLong("id"));
                    memory.setSessionId(rs.getString("session_id"));
                    memory.setSmVariableName(variableName);
                    memory.setValue(rs.getBoolean("sm_variable_value"));
                    memory.setShortStringValue(rs
                            .getString("short_string_value"));
                    memory.setLongStringValue(rs.getString("long_string_value"));
                    memory.setLastModified(rs.getDate("sm_last_modified"));
                }
            }
        } catch (SQLException e) {
            log.error("Error during save SMMemory: " + memory, e);
            throw e;
        }
        return memory;
    }

    public void updateSMMemory(SMMemory memory) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        log.info("Update memory:" + memory);
        String sql = "UPDATE sm_memory " + " SET sm_variable_value = ?,"
                + " short_string_value = ?," + " long_string_value = ?, "
                + " sm_last_modified = ? WHERE id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            if (memory.getValue() != null) {
                pstmt.setBoolean(1, memory.getValue());
            } else {
                pstmt.setNull(1, java.sql.Types.BOOLEAN);
            }
            if (memory.getShortStringValue() != null) {
                pstmt.setString(2, memory.getShortStringValue());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            if (memory.getLongStringValue() != null) {
                pstmt.setString(3, memory.getLongStringValue());
            } else {
                pstmt.setNull(3, java.sql.Types.CLOB);
            }
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            pstmt.setLong(5, memory.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("Error during updating sm_memory: " + memory, e);
            throw e;
        }
    }

    public List<String> listStateMachines() throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        String sql = "SELECT state_machine_name FROM state_machines";
        List<String> stateMachines = new ArrayList<String>();
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stateMachines.add(rs.getString("state_machine_name"));
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
        return stateMachines;
    }

    public StateMachine getStateMachine(String name)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " + " id," + " state_machine_name"
                + " FROM state_machines " + " WHERE state_machine_name = ?";
        StateMachine sm = null;
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sm = new StateMachine();
                    sm.setId(rs.getLong("id"));
                    sm.setName(rs.getString("state_machine_name"));
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
        if (sm == null) {
            log.info("state machine not found: " + name);
            return null;
        } else {
            log.info("state machine found: " + name + " id: " + sm.getId());
        }
        Map<Long, SMVariable> variables = new HashMap<Long, SMVariable>();
        this.getSMVariables(sm, variables);
        Map<Long, SMAction> actions = new HashMap<Long, SMAction>();
        this.getSMActions(sm, actions);
        this.getSMRules(sm, variables, actions);
        return sm;
    }

    public boolean stateMachineExistis(String name)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        log.info("checking if state machine " + name + " exists");
        String sql = "SELECT " + " id," + " state_machine_name"
                + " FROM state_machines " + " WHERE state_machine_name = ?";
        StateMachine sm = null;
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sm = new StateMachine();
                    sm.setId(rs.getLong("id"));
                    sm.setName(rs.getString("state_machine_name"));
                    log.info("found state machine " + name + " id:"
                            + sm.getId());
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
        return (sm != null);
    }

    public void getSMVariables(StateMachine sm, Map<Long, SMVariable> variables)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " + " id," + " sm_variable_name "
                + " FROM sm_variables" + " WHERE state_machine_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sm.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMVariable var = new SMVariable();
                    var.setId(rs.getLong("id"));
                    var.setName(rs.getString("sm_variable_name"));
                    var.setStateMachineName(sm.getName());
                    sm.getVariables().add(var);
                    variables.put(var.getId(), var);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
    }

    public List<SMVariable> findSMVariables(String variableName)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        log.info("searching variables by name:" + variableName);
        String sql = "SELECT " 
                + "  v.id id," 
                + "  v.sm_variable_name sm_variable_name,"
                + "  sm.state_machine_name state_machine_name" 
                + " FROM "
                + "  sm_variables v,"
                + "  state_machines sm"
                + " WHERE "
                + "  v.state_machine_id = sm.id AND"
                + "  v.sm_variable_name = ?";
        log.info("sql:" + sql);
        Connection conn = super.getDbHelper().getConnection();
        List<SMVariable> variables = new ArrayList<SMVariable>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, variableName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMVariable var = new SMVariable();
                    var.setId(rs.getLong("id"));
                    var.setName(rs.getString("sm_variable_name"));
                    var.setStateMachineName(rs.getString("state_machine_name"));
                    variables.add(var);
                    log.info("found:" + var.getId() + " " + var.getName());
                }
            }
        } catch (SQLException e) {
            log.error("Error during getting list of vairables by name " + variableName, e);
            throw e;
        }
        return variables;
    }


    public List<SMVariable> findSMVariablesByStateMachineName(String stateMachineName)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " 
                + "  v.id id," 
                + "  v.sm_variable_name sm_variable_name,"
                + "  sm.state_machine_name state_machine_name" 
                + " FROM "
                + "  sm_variables v,"
                + "  state_machines sm"
                + " WHERE "
                + "  v.state_machine_id = sm.id AND"
                + "  sm.state_machine_name = ?";
        Connection conn = super.getDbHelper().getConnection();
        List<SMVariable> variables = new ArrayList<SMVariable>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stateMachineName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMVariable var = new SMVariable();
                    var.setId(rs.getLong("id"));
                    var.setName(rs.getString("sm_variable_name"));
                    var.setStateMachineName(rs.getString("state_machine_name"));
                    variables.add(var);
                }
            }
        } catch (SQLException e) {
            log.error("Error during getting list of vairables by state machine name " + stateMachineName, e);
            throw e;
        }
        return variables;
    }


    
    public void getSMActions(StateMachine sm, Map<Long, SMAction> actions)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " + " id," + " sm_action_name, " + " action_text "
                + " FROM sm_actions " + " WHERE state_machine_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sm.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMAction action = new SMAction();
                    action.setId(rs.getLong("id"));
                    action.setName(rs.getString("sm_action_name"));
                    action.setActionScript(rs.getString("action_text"));
                    action.setStateMachineName(sm.getName());
                    sm.getActions().add(action);
                    actions.put(action.getId(), action);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
    }

    public void getSMRules(StateMachine sm, Map<Long, SMVariable> variables,
            Map<Long, SMAction> actions) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        String sql = "SELECT " + " id," + " sm_rule_name " + " FROM sm_rules "
                + " WHERE state_machine_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, sm.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMRule rule = new SMRule();
                    rule.setId(rs.getLong("id"));
                    rule.setName(rs.getString("sm_rule_name"));
                    rule.setStateMachineName(sm.getName());
                    sm.getRules().add(rule);
                    this.getSMConditions(rule);
                    this.getSMActionsForRule(rule, actions);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }

    }

    public void getSMConditions(SMRule rule) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        String sql = "SELECT  " 
            + "   c.id id, "
            + "   r.sm_rule_name sm_rule_name, "
            + "   v.sm_variable_name sm_variable_name,"
            + "   c.sm_variable_value sm_variable_value" 
            + " FROM "
            + "   sm_conditions c, "
            + "   sm_rules r,"
            + "   sm_variables v" 
            + " WHERE"
            + "   c.sm_rule_id = r.id AND"
            + "   c.sm_variable_id = v.id AND" 
            + "   r.id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, rule.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMCondition condition = new SMCondition();
                    condition.setId(rs.getLong("id"));
                    condition.setValue(rs.getBoolean("sm_variable_value"));
                    condition.setRuleName(rule.getName());
                    condition.setVariableName(rs.getString("sm_variable_name"));
                    condition.setStateMachineName(rule.getStateMachineName());
                    rule.getConditions().add(condition);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }

    }

    public void getSMActionsForRule(SMRule rule, Map<Long, SMAction> actions)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " + " sm_action_id " + " FROM sm_actions_rules "
                + " WHERE sm_rule_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, rule.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rule.getActions().add(
                            actions.get(rs.getLong("sm_action_id"))
                                    .getActionScript());
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }

    }

    public List<SMRule> fireRules(String sessionId)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        this.merge(sessionId);
        String sql = "SELECT " 
                + "   A.rule_id," 
                + "   A.rule_name,"
                + "   A.session_id,"
                + "   B.state_machine_name" 
                + " FROM " 
                + " (SELECT"
                + "   count(m.id) cond_count," 
                + "   r.sm_rule_name rule_name,"
                + "   r.id rule_id," 
                + "   m.session_id session_id" 
                + "  FROM"
                + "   sm_memory m," 
                + "   sm_rules r," 
                + "   sm_conditions c,"
                + "   sm_variables v"
                + "  WHERE"
                + "   c.sm_rule_id = r.id AND"
                + "   v.id  = c.sm_variable_id AND"
                + "   v.sm_variable_name = m.sm_variable_name AND"
                + "   c.sm_variable_value = m.sm_variable_value AND"
                + "   m.session_id = ?"
                + "  GROUP BY c.sm_rule_id, m.session_id"
                + "  ) A," 
                + " (SELECT" 
                + "   count(c.id) var_count,"
                + "   c.sm_rule_id rule_id," 
                + "   r.sm_rule_name rule_name,"
                + "   sm.state_machine_name state_machine_name"
                + "  FROM "
                + "   sm_conditions c, "
                + "   sm_rules r,"
                + "   state_machines sm"
                + "  WHERE"
                + "   c.sm_rule_id = r.id AND"
                + "   sm.id = r.state_machine_id"
                + "  GROUP BY c.sm_rule_id) B"
                + " WHERE "
                + "  A.cond_count = B.var_count AND"
                + "  A.rule_id = B.rule_id";
        List<SMRule> rules = new ArrayList<SMRule>();
        Connection conn = super.getDbHelper().getConnection();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sessionId);
            log.info("executing :" + sql);
            log.info("executing session id:" + sessionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMRule rule = new SMRule();
                    rule.setId(rs.getLong("rule_id"));
                    rule.setName(rs.getString("rule_name"));
                    rule.setStateMachineName(rs.getString("state_machine_name"));
                    rule.getActions().addAll(
                            this.getSMActionsScriptsByRule(rule.getId()));
                    rules.add(rule);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
        return rules;
    }

    public List<SMAction> getSMActionsByRule(Long ruleId)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " 
                + "  a.id id,"
                + "  a.state_machine_id state_machine_id,"
                + "  sm.state_machine_name state_machine_name,"
                + "  a.sm_action_name sm_action_name,"
                + "  a.action_text action_text" 
                + " FROM " 
                + "  sm_actions a,"
                + "  sm_actions_rules ar" 
                + "  state_machines sm"
                + " WHERE "
                + "  a.state_machine_id = sm.id AND"
                + "  a.id = ar.sm_action_id AND" 
                + "  ar.sm_rule_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        List<SMAction> actions = new ArrayList<SMAction>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ruleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMAction action = new SMAction();
                    action.setId(rs.getLong("id"));
                    action.setName(rs.getString("sm_action_name"));
                    action.setActionScript(rs.getString("action_text"));
                    action.setStateMachineName(rs.getString("state_machine_name"));
                    actions.add(action);
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
        return actions;
    }

    public List<String> getSMActionsScriptsByRule(Long ruleId)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException, SQLException {
        String sql = "SELECT " + "  a.action_text action_text" + " FROM "
                + "  sm_actions a," + "  sm_actions_rules ar" + " WHERE "
                + "  a.id = ar.sm_action_id AND" + "  ar.sm_rule_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        List<String> actions = new ArrayList<String>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, ruleId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    actions.add(rs.getString("action_text"));
                }
            }
        } catch (SQLException e) {
            log.error("Error during selecting state machines ", e);
            throw e;
        }
        return actions;
    }

    public void execute(Collection<String> actions, String sessionId,
            ActionResult result) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        for (String action : actions) {
            String[] commands = action.split(";");
            for (String command : commands) {
                this.executeAction(command, sessionId, result);
            }
        }
    }

    public void executeActions(Collection<Action> actions, String sessionId,
            ActionResult result) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        for (Action action : actions) {
            String[] commands = action.getActionBody().split(";");
            for (String command : commands) {
                this.executeAction(command, sessionId, result);
            }
        }
    }

    public boolean executeSMRules(Collection<SMRule> rules, String sessionId,
            ActionResult result) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        boolean changed = false;
        for (SMRule rule : rules) {
            for (String action : rule.getActions()) {
                String[] commands = action.split(";");
                for (String command : commands) {
                    log.info("executing command:" + command);
                    if (this.executeAction(command, sessionId, result)) {
                        changed = true;
                    }
                }
            }
        }
        return changed;
    }

    public boolean executeAction(String action, String sessionId,
            ActionResult result) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        boolean changed = false;
        result.append(action);
        if (action.indexOf('=') > 0) {
            String[] parts = action.split("=");
            if (parts.length < 2) {
                return false;
            }
            String variable = parts[0].trim();
            String stringValue = parts[1].trim().toLowerCase();
            log.info("set " + variable + "=" + stringValue);
            result.getMemory().put(variable, stringValue);
            if (variable.toLowerCase().matches("(ответ|output|response|выход)")) {
                log.info("Processing output:" + stringValue);
                if (stringValue.indexOf('|') > 0) {
                    String[] responses = stringValue.split("\\|");
                    for (int i=0; i<responses.length; i++) {
                        log.info("responses["+i+"]=" + responses[i]);                        
                    }
                    int idx = new Random().nextInt(responses.length);
                    String randomResponse = responses[idx];
                    log.info("Processing output, response:" + randomResponse);
                    result.setResponse(randomResponse);
                } else {
                    result.setResponse(stringValue);
                }
            }
            if (variable.toLowerCase().matches(
                    "(материал|content|страница|page)")) {
                result.setContent(stringValue);
            }
            if (variable.toLowerCase().matches("(запрос|вопрос|request)")) {
                result.setRequest(stringValue);
            }
            Boolean val = null;
            if (stringValue.matches("(true|y|yes|да|1)")) {
                val = Boolean.TRUE;
            }
            if (stringValue.matches("(false|n|no|нет|0)")) {
                val = Boolean.FALSE;
            }
            if (this.safeOrUpdateMemory(sessionId, variable, val, stringValue)) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean safeOrUpdateMemory(String sessionId, String variable,
            Boolean value, String stringValue) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        boolean changed = false;
        log.info("save or update memory:" + sessionId + " " + variable + " " + value + " " + stringValue);
        SMMemory memory = this.getMemory(sessionId, variable);
        if (memory == null) {
            memory = new SMMemory();
            memory.setSessionId(sessionId);
            memory.setSmVariableName(variable);
            memory.setValue(value);
            if (stringValue.length() > 250) {
                memory.setLongStringValue(stringValue);
            } else {
                memory.setShortStringValue(stringValue);
            }
            this.saveSMMemory(memory);
            changed = true;
        } else {
            if (memory.getValue() != value) {
                memory.setValue(value);
                if (stringValue.length() > 250) {
                    memory.setLongStringValue(stringValue);
                } else {
                    memory.setShortStringValue(stringValue);
                }
                this.updateSMMemory(memory);
                changed = true;
            }
        }
        return changed;
    }

    public boolean safeOrUpdateMemory(String sessionId, SMVariable variable,
            String stringValue) throws InstantiationException,
            IllegalAccessException, ClassNotFoundException, SQLException {
        boolean changed = false;
        SMMemory memory = this.getMemory(sessionId, variable.getName());
        if (memory == null) {
            memory = new SMMemory();
            memory.setSessionId(sessionId);
            memory.setSmVariableName(variable.getName());
            memory.setStateMachineName(variable.getStateMachineName());
            memory.setShortStringValue(stringValue);
            this.saveSMMemory(memory);
            changed = true;
        } else {
            if (!memory.getShortStringValue().equals(stringValue)) {
                memory.setShortStringValue(stringValue);
                this.updateSMMemory(memory);
                changed = true;
            }
        }
        return changed;
    }

    public List<SMMemory> listMemory(String sessionId) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "SELECT " 
                + "   id," 
                + "   session_id,"
                + "   sm_variable_name,"
                + "   sm_variable_value,"
                + "   short_string_value,"
                + "   long_string_value,"
                + "   sm_last_modified" 
                + " FROM "
                + "   sm_memory "
                + " WHERE"
                + "   session_id = ?";
        Connection conn = super.getDbHelper().getConnection();
        List<SMMemory> sessionMemory = new ArrayList<SMMemory>();
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, sessionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SMMemory memory = new SMMemory();
                    memory.setId(rs.getLong("id"));
                    memory.setSessionId(rs.getString("session_id"));
                    memory.setSmVariableName(rs.getString("sm_variable_name"));
                    memory.setValue(rs.getBoolean("sm_variable_value"));
                    memory.setShortStringValue(rs
                            .getString("short_string_value"));
                    memory.setLongStringValue(rs.getString("long_string_value"));
                    memory.setLastModified(rs.getDate("sm_last_modified"));
                    sessionMemory.add(memory);
                }
            }
        } catch (SQLException e) {
            log.error("Error during list session memory: " + sessionId, e);
            throw e;
        }
        return sessionMemory;
    }

    public Long getStateMachineId(String stateMachineName) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "SELECT id FROM state_machines WHERE  state_machine_name = ?";
        Connection conn = super.getDbHelper().getConnection();
        Long stateMachineId = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stateMachineName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stateMachineId = rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            log.error("Error during get state machine id by name: "
                    + stateMachineName, e);
            throw e;
        }
        return stateMachineId;
    }

    public Long getSMVariableId(String stateMachineName, String variableName) throws SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        String sql = "SELECT "
                + "     v.id "
                + "  FROM "
                + "     state_machines sm,"
                + "     sm_variables v"
                + "  WHERE"
                + "     sm.id = v.state_machine_id AND"
                + "     sm.state_machine_name = ? AND"
                + "     v.sm_variable_name = ?";
        Connection conn = super.getDbHelper().getConnection();
        Long id = null;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stateMachineName);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    id = rs.getLong("id");
                }
            }
        } catch (SQLException e) {
            log.error("Error during get state machine variable id by state machine name: "
                    + stateMachineName + " and variable name: " + variableName, e);
            throw e;
        }
        return id;
    }
    
    public void merge(String sessionId) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        List<SMMemory> memory = this.listMemory(sessionId);
        Map<String, SMMemory> memoryMap = new HashMap<String, SMMemory>();
        for (SMMemory memoryRecord : memory) {
            log.info("found memory:" + memoryRecord);
            memoryMap.put(memoryRecord.getSmVariableName(), memoryRecord);
        }
        List<String> stateMachineNames = this.listStateMachines();
        for (String stateMachineName : stateMachineNames) {
            List<SMVariable> variables = this.findSMVariablesByStateMachineName(stateMachineName);
            for (SMVariable var : variables) {
                if (!memoryMap.containsKey(var.getName())) {
                    SMMemory mem = new SMMemory();
                    mem.setSmVariableName(var.getName());
                    mem.setValue(Boolean.FALSE);
                    mem.setSessionId(sessionId);
                    log.info("saving memory: " + mem);
                    this.saveSMMemory(mem);
                    memoryMap.put(mem.getSmVariableName(), mem);
                }
            }
        }
    }
}
