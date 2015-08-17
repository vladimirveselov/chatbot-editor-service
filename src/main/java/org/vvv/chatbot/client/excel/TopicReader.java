package org.vvv.chatbot.client.excel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.dao.WrongFormatException;
import org.vvv.chatbotdb.model.Action;
import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class TopicReader {
    
    private static Log log = LogFactory.getLog(TopicReader.class);
    
    public static final String RULE_NAME_HEADER_PATTERN = "(название правила|правило|rule|rule name)";
    
    public static final String RANK_HEADER_PATTERN = "(ранк|rank|приоритет)";
    
    public static final String RESPONSE_HEADER_PATTERN = "(ответ|response)";
    
    public static final String INPUTS_HEADER_PATTERN = "(входы|вход|inputs)";
    
    public static final String OUTPUTS_HEADER_PATTERN = "(выходы|ответы|outputs)";
    
    public static final String REQUEST_HEADER_PATTERN = "(запрос|request)";
    
    public static final String ACTIONS_HEADER_PATTERN = "(действия|actions|script)";
            
    public Topic convert(ExcelSheet sheet)
            throws WrongFormatException, WrongFileFormatException {
        if (sheet.getData().size() < 2) {
            throw new WrongFormatException("Not enough rows");
        }
        Topic topic = new Topic();
        String topicName = sheet.getName();
        if (topicName.contains("=")) {
            String [] topicNameAndRank = topicName.split("=");
            topic.setTopicName(topicNameAndRank[0].trim());
            topic.setRank(Long.parseLong(topicNameAndRank[1].trim()));
            log.info("reading topic: " + topic.getTopicName() + " rank is custom - " + topic.getRank());
        } else {
            topic.setTopicName(topicName);
            log.info("reading topic: " + topic.getTopicName() + " rank is default - " + topic.getRank());
        }
        Map<TopicHeader, Integer> headersMap = new HashMap<TopicHeader, Integer>();
        List<String> firstRow = sheet.getData().get(0);
        for (int i = 0; i<firstRow.size(); i++) {
            String s = firstRow.get(i).toLowerCase().trim();
            if (s.matches(RULE_NAME_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.RULE, i);
            }
            if (s.matches(RANK_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.RANK, i);
            }
            if (s.matches(RESPONSE_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.RESPONSE, i);
            }
            if (s.matches(INPUTS_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.INPUTS, i);
            }
            if (s.matches(OUTPUTS_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.OUTPUTS, i);
            }
            if (s.matches(REQUEST_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.REQUEST, i);
            }
            if (s.matches(ACTIONS_HEADER_PATTERN)) {
                headersMap.put(TopicHeader.ACTIONS, i);
            }
        }
        if (!headersMap.containsKey(TopicHeader.RULE)) {
            throw new WrongFileFormatException("Rules column not found");
        }
        log.info("Headers:" + headersMap);
        this.scanForRules(topic, sheet.getData(), headersMap.get(TopicHeader.RULE), headersMap);
        return topic;
    }

    public void scanForRules(Topic topic, List<List<String>> data, int columnNumber, Map<TopicHeader, Integer> headersMap) {
        String ruleName = null;
        int startRow = 1;
        for (int i=1; i<data.size(); i++) {
            List<String> row = data.get(i);
            if (row.size() > columnNumber) {
                String val = row.get(columnNumber).trim();
                if (val.length() > 0) {
                    if (ruleName == null) {
                        ruleName = val;
                        startRow = i;
                        continue;
                    }
                    if (!val.equalsIgnoreCase(ruleName)) {
                        Rule rule = this.getRule(topic, data, ruleName, startRow, i, headersMap);
                        topic.getRules().add(rule);
                        ruleName = val;
                        startRow = i;
                    }
                }
            }
        }
        Rule rule = this.getRule(topic, data, ruleName, startRow, data.size()-1, headersMap);
        topic.getRules().add(rule);
    }

    public Rule getRule(
            Topic topic, 
            List<List<String>> data, 
            String ruleName, 
            int startRow, 
            int currentRow, 
            Map<TopicHeader, Integer> headersMap) {
        log.info("reading rule, data:" + data);
        log.info("rule name:" + ruleName);
        log.info("start row:" + startRow);
        log.info("current row:" + currentRow);
        Rule rule = new Rule();
        rule.setName(ruleName);
        rule.setTopicName(topic.getTopicName());
        if (headersMap.containsKey(TopicHeader.RANK)) {
            String rankValue = data.get(startRow).get(headersMap.get(TopicHeader.RANK)).trim();
            if (rankValue.length() > 0 && isNumber(rankValue)) {
                rule.setRank(Long.parseLong(rankValue));
            }
        }
        if (headersMap.containsKey(TopicHeader.RESPONSE)) {
            String responseValue = data.get(startRow).get(headersMap.get(TopicHeader.RESPONSE)).trim();
            if (responseValue.length() > 0) {
                rule.setResponse(responseValue);
            }
        }
        this.getInputs(rule, data, startRow, currentRow, headersMap);
        this.getOutputs(rule, data, startRow, currentRow, headersMap);
        this.getActions(rule, data, startRow, currentRow, headersMap);
        return rule;
    }
   
    private boolean isNumber(String value) {
        for (int i=0; i<value.length(); i++) {
            char ch = value.charAt(i);
            if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return true;
    }

    public void getInputs(
            Rule rule,
            List<List<String>> data, 
            int startRow, 
            int currentRow, 
            Map<TopicHeader, Integer> headersMap) {
        if (!headersMap.containsKey(TopicHeader.INPUTS)) {
            return;
        }
        for (int i=startRow; i<currentRow; i++) {
            String val = data.get(i).get(headersMap.get(TopicHeader.INPUTS)).trim();
            if (val.length() > 0) {
                Input input = new Input();
                input.setRuleName(rule.getName());
                input.setTopicName(rule.getTopicName());
                input.setText(val);
                rule.getInputs().add(input);
            }
        }
    }
    
    public void getOutputs(
            Rule rule,
            List<List<String>> data, 
            int startRow, 
            int currentRow, 
            Map<TopicHeader, Integer> headersMap) {
        if (!headersMap.containsKey(TopicHeader.OUTPUTS)) {
            return;
        }
        boolean requests = headersMap.containsKey(TopicHeader.REQUEST);
        for (int i=startRow; i<currentRow; i++) {
            if (data.get(i).size() < headersMap.get(TopicHeader.OUTPUTS)) {
                String val = data.get(i).get(headersMap.get(TopicHeader.OUTPUTS)).trim();
                if (val.length() > 0) {
                    Output output = new Output();
                    output.setRuleName(rule.getName());
                    output.setTopicName(rule.getTopicName());
                    output.setText(val);
                    rule.getOutputs().add(output);
                    if (requests) {
                        String requestValue = data.get(i).get(headersMap.get(TopicHeader.REQUEST)).trim();
                        if (requestValue.length()>0) {
                            output.setRequest(requestValue);
                        }
                    }
                }
            }
        }
    }
    
    public void getActions(
            Rule rule,
            List<List<String>> data, 
            int startRow, 
            int currentRow, 
            Map<TopicHeader, Integer> headersMap) {
        if (!headersMap.containsKey(TopicHeader.ACTIONS)) {
            return;
        }
        for (int i=startRow; i<currentRow; i++) {
            if (data.get(i).size() > headersMap.get(TopicHeader.ACTIONS)) {
                String val = data.get(i).get(headersMap.get(TopicHeader.ACTIONS)).trim();
                if (val.length() > 0) {
                    Action action = new Action();
                    action.setRuleName(rule.getName());
                    action.setActionBody(val);
                    action.setPriority(100l);
                    action.setTopicName(rule.getTopicName());
                    rule.getActions().add(action);
                }
            }
        }
    }

}
