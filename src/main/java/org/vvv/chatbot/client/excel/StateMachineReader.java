package org.vvv.chatbot.client.excel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.dao.WrongFormatException;
import org.vvv.chatbotdb.model.SMAction;
import org.vvv.chatbotdb.model.SMCondition;
import org.vvv.chatbotdb.model.SMRule;
import org.vvv.chatbotdb.model.SMVariable;
import org.vvv.chatbotdb.model.StateMachine;

public class StateMachineReader {

    private static Log log = LogFactory.getLog(StateMachineReader.class);

    public List<StateMachine> readFromExcel(String fileName)
            throws FileNotFoundException, IOException, WrongFormatException {
        log.info("Reading from excel file: " + fileName);
        List<StateMachine> machines = new ArrayList<StateMachine>();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            ExcelUtils excelUtils = new ExcelUtils();
            List<ExcelSheet> data = excelUtils.readFromExcel(fis);
            for (ExcelSheet sheet : data) {
                machines.add(this.convert(sheet));
            }
        }
        return machines;
    }

    public StateMachine convert(ExcelSheet sheet)
            throws WrongFormatException {
        if (sheet.getData().size() < 3) {
            throw new WrongFormatException("Not enough rows");
        }
        StateMachine stateMachine = new StateMachine();
        stateMachine.setName(sheet.getName());
        Map<Integer, SMVariable> variables = this.getVariables(sheet.getData(),
                stateMachine);
        stateMachine.getVariables().addAll(variables.values());
        Map<Integer, SMAction> actions = this.getActions(sheet.getData(), stateMachine);
        stateMachine.getActions().addAll(actions.values());
        Map<Integer, SMRule> rules = this.getRules(sheet.getData(), stateMachine);
        stateMachine.getRules().addAll(rules.values());
        for (int i = 1; i < sheet.getData().size(); i++) {
            List<String> row = sheet.getData().get(i);
            for (int j = 1; j < row.size(); j++) {
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
                                condition.setStateMachineName(stateMachine.getName());
                                condition.setRuleName(rules.get(j).getName());
                                condition.setVariableName(variables.get(i).getName());
                                condition.setValue(value);
                                rules.get(j).getConditions().add(condition);
                            } else if (actions.containsKey(i)) {
                                rules.get(j).getActions().add(actions.get(i).getActionScript());
                                rules.get(j).getActionNames().add(actions.get(i).getName());
                            }
                        }
                    }
                }
            }
        }
        return stateMachine;
    }

    public Map<Integer, SMVariable> getVariables(List<List<String>> sheet,
            StateMachine sm) {
        Map<Integer, SMVariable> variables = new HashMap<Integer, SMVariable>();
        for (int i = 1; i < sheet.size(); i++) {
            List<String> row = sheet.get(i);
            if (row.size() > 0) {
                String val = row.get(0).toLowerCase().trim();
                if (val.length() > 0) {
                    if (val.matches("(действия|actions)")) {
                        break;
                    }
                    SMVariable var = new SMVariable();
                    var.setName(val);
                    var.setStateMachineName(sm.getName());
                    variables.put(i, var);
                }

            }
        }
        return variables;
    }

    public Map<Integer, SMRule> getRules(List<List<String>> sheet,
            StateMachine sm) {
        Map<Integer, SMRule> rules = new HashMap<Integer, SMRule>();
        List<String> firstRow = sheet.get(0);
        for (int i = 1; i < firstRow.size(); i++) {
            String val = firstRow.get(i).trim();
            if (val.length() > 0) {
                SMRule rule = new SMRule();
                rule.setName(val);
                rule.setStateMachineName(sm.getName());
                rules.put(i, rule);
            }
        }
        return rules;
    }

    public Map<Integer, SMAction> getActions(List<List<String>> sheet,
            StateMachine stateMachine) throws WrongFormatException {
        Map<Integer, SMAction> actions = new HashMap<Integer, SMAction>();
        if (sheet.size() < 3) {
            throw new WrongFormatException("Not enough rows");
        }
        boolean actionsStarted = false;
        String actionName = stateMachine.getName() + "_Action_";
        int actionNumber = 1;
        for (int i = 1; i < sheet.size(); i++) {
            List<String> row = sheet.get(i);
            if (row.size() > 0) {
                String val = row.get(0).toLowerCase().trim();
                if (val.length() > 0) {
                    if (val.matches("(действия|actions)")) {
                        actionsStarted = true;
                        continue;
                    }
                    if (actionsStarted) {
                        SMAction action = new SMAction();
                        action.setName(actionName + actionNumber);
                        actionNumber++;
                        action.setActionScript(val);
                        action.setStateMachineName(stateMachine.getName());
                        actions.put(i, action);
                    }
                }

            }
        }
        return actions;
    }


}
