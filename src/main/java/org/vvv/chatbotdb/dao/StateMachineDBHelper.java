package org.vvv.chatbotdb.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.model.SMAction;
import org.vvv.chatbotdb.model.SMCondition;
import org.vvv.chatbotdb.model.SMRule;
import org.vvv.chatbotdb.model.SMVariable;
import org.vvv.chatbotdb.model.StateMachine;
import org.vvv.chatbotdb.utils.ExcelUtils;


public class StateMachineDBHelper extends DBObject {
    
    private static Log log = LogFactory.getLog(StateMachineDBHelper.class);

    public List<StateMachine> readFromExcel(String fileName) throws FileNotFoundException, IOException, WrongFormatException {
    	List<StateMachine> machines = new ArrayList<StateMachine>();
    	try(FileInputStream fis = new FileInputStream(fileName)) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<List<List<String>>> data = excelUtils.readFromExcel(fis);
    		for (List<List<String>> sheet : data) {
    			machines.add(this.convert(sheet));
    		}
    	}
    	return machines;
    }
    
    public StateMachine convert(List<List<String>> sheet) throws WrongFormatException {
    	if (sheet.size() < 3) {
    		throw new WrongFormatException("Not enough rows");
    	}
    	StateMachine stateMachine = new StateMachine();
    	Map<Integer, SMVariable> variables = this.getVariables(sheet, stateMachine);
		stateMachine.getVariables().addAll(variables.values());
    	Map<Integer, SMAction> actions = this.getActions(sheet, stateMachine);
    	stateMachine.getActions().addAll(actions.values());
    	Map<Integer, SMRule> rules = this.getRules(sheet, stateMachine);
    	stateMachine.getRules().addAll(rules.values());
    	for (int i=1; i<sheet.size(); i++) {
    		List<String> row = sheet.get(i);
    		for (int j = 1; j< row.size(); j++) {
    			String val = row.get(j).trim().toLowerCase();
    			if (val.length() > 0) {
    				Boolean value = null;
    				if (val.matches("(y|yes|да)")) {
    					value = Boolean.TRUE;
    				}
    				if (val.matches("(no|n|нет)")) {
    					value = Boolean.FALSE;
    				}
    				if (value != null) {
    					if (rules.containsKey(j)) {
    						if (variables.containsKey(i)) {
    							SMCondition condition = new SMCondition();
    							condition.setSmRule(rules.get(j));
    							condition.setSmVariable(variables.get(i));
    							condition.setValue(value);
    							rules.get(j).getConditions().add(condition);
    						} else if (actions.containsKey(i)) {
    							rules.get(j).getActions().add(actions.get(i));
    						}
    					}
    				}
    			}
    		}
    	}
    	return stateMachine;
    }
    
    public Map<Integer, SMVariable> getVariables(List<List<String>> sheet, StateMachine sm)  {
    	Map<Integer, SMVariable> variables = new HashMap<Integer, SMVariable>();
    	for (int i=1; i<sheet.size(); i++) {
    		List<String> row = sheet.get(i);
    		if (row.size() > 0) {
    			String val = row.get(0).trim();
    			if (val.length() > 0) {
    				if (val.matches("(действия|actions)")) {
    					break;
    				}
    				SMVariable var = new SMVariable();
    				var.setName(val);
    				var.setStateMachine(sm);
    				variables.put(i, var);
    			}
    			
    		}
    	}
    	return variables;
    }
    
    public Map<Integer, SMRule> getRules(List<List<String>> sheet, StateMachine sm)  {
    	Map<Integer, SMRule> rules= new HashMap<Integer, SMRule>();
    	List<String> firstRow = sheet.get(0);
    	for (int i=1; i<firstRow.size(); i++) {
			String val = firstRow.get(i).trim();
			if (val.length() > 0) {
				SMRule rule = new SMRule();
				rule.setName(val);
				rule.setStateMachine(sm);
				rules.put(i, rule);
			}
    	}
    	return rules;
    }
    public Map<Integer, SMAction> getActions(List<List<String>> sheet, StateMachine stateMachine) throws WrongFormatException {
    	Map<Integer, SMAction>  actions = new HashMap<Integer, SMAction>();
    	if (sheet.size() < 3) {
    		throw new WrongFormatException("Not enough rows");
    	}
    	boolean actionsStarted = false;
    	for (int i=1; i<sheet.size(); i++) {
    		List<String> row = sheet.get(i);
    		if (row.size() > 0) {
    			String val = row.get(0).trim();
    			if (val.length() > 0) {
    				if (val.matches("(действия|actions)")) {
    					actionsStarted = true;
    					continue;
    				}
    				if (actionsStarted) {
    					SMAction action = new SMAction();
    					action.setName(val);
    					action.setStateMachine(stateMachine);
    					actions.put(i, action);
    				}
    			}
    			
    		}
    	}
    	return actions;
    }
}
