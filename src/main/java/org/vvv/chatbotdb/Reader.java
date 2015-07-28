package org.vvv.chatbotdb;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import org.vvv.chatbotdb.model.Input;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class Reader {

    private Console console;

    public void setConsole(Console console) {
        this.console = console;
    }

    public void readFile(String file) throws FileNotFoundException {
        BufferedReader br = null;
        Topic currentTopic = null;
        Rule currentRule = null;
        boolean questionsStarted = false;
        boolean responseStarted = false;
        StringBuffer sb = new StringBuffer();

        try {
            br = new BufferedReader(
                    new InputStreamReader(
                               new FileInputStream(file), "UTF8"));
          
            while (br.ready()) {
                String line = br.readLine();
                if (currentTopic != null && currentRule !=null) {
                    System.out.println("> topic: " + currentTopic.getTopicName() + " rule:" + currentRule.getName() + " " + line);
                } else {
                    System.out.println("> " + line);
                }
                if (questionsStarted) {
                    if (line.trim().length() == 0) {
                        questionsStarted = false;
                    } else {
                        if (currentRule == null || currentTopic == null) {
                            System.out.println("File - " + file
                                    + " Error: rule or topic is not defined");
                            return;
                        }
                        Input input = new Input();
                        input.setRule(currentRule);
                        input.setText(line);
                        currentRule.getInputs().add(input);
                    }
                    continue;
                }
                if (responseStarted) {
                    if (line.trim().length() == 0) {
                        responseStarted = false;
                        Output output = new Output();
                        output.setRule(currentRule);
                        output.setText(sb.toString());
                        currentRule.getOutputs().add(output);
                        sb = new StringBuffer();
                    } else {
                        if (currentRule == null || currentTopic == null) {
                            System.out.println("File - " + file
                                    + " Error: rule or topic is not defined");
                            return;
                        }
                        sb.append(line);
                        sb.append('\n');
                    }
                    continue;
                }
                if (line.startsWith("Question:")) {
                    questionsStarted = true;
                    continue;
                }
                if (line.startsWith("Response:")) {
                    responseStarted = true;
                    continue;
                }
                if (line.startsWith("Topic:")) {
                    String topicName = line.substring(6).trim();
                    if (!console.topicExists(topicName)) {
                        currentTopic = console.createTopic(topicName);
                    } else {
                        currentTopic = console.getTopic(topicName);
                    }
                    continue;
                }
                if (line.startsWith("Rule:")) {
                    if (currentRule != null) {
                        console.createRule(currentRule);
                    }
                    currentRule = new Rule();
                    currentRule.setTopic(currentTopic);
                    String ruleName = line.substring(5).trim();
                    currentRule.setName(ruleName);
                    System.out.println("Rule: " + currentRule.getName());
                    continue;
                }
            }
            if (responseStarted) {
                Output output = new Output();
                output.setRule(currentRule);
                output.setText(sb.toString());
                currentRule.getOutputs().add(output);
                console.createRule(currentRule);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                br.close();
            } catch (Exception ee) {

            }
        }
    }

}
