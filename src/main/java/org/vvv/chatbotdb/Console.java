package org.vvv.chatbotdb;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.vvv.chatbotdb.dao.Holder;
import org.vvv.chatbotdb.dao.SearchAnswerResult;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class Console {
    
//    private static Log log = LogFactory.getLog(Console.class);
    
    private Holder holder;
    
    private Reader reader;
    
    public Console() {
        this.holder = new Holder();
        this.holder.init();
        this.holder.getDbHelper().setConnectionString("jdbc:mysql://localhost:2306/chatbot?user=chatbot&password=sql2015");
        this.reader = new Reader();
        this.reader.setConsole(this);
    }
    
    public Topic createTopic(String topicName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        Topic topic = new Topic();
        topic.setTopicName(topicName);
        this.holder.getTopicDBHelper().save(topic);
        System.out.println("Topic " + topicName + " created");
        this.holder.getDbHelper().closeConnection();
        return topic;
    }

    public boolean topicExists(String topicName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
        System.out.println("Topic " + topicName + " created");
        this.holder.getDbHelper().closeConnection();
        return topic!=null;
    }

    public Topic getTopic(String topicName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
        System.out.println("Topic " + topicName + " created");
        this.holder.getDbHelper().closeConnection();
        return topic;
    }
    
    public void deleteTopic(String topicName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        Topic topic = new Topic();
        topic.setTopicName(topicName);
        this.holder.getTopicDBHelper().delete(topicName);
        System.out.println("Topic " + topicName + " deleted");
        this.holder.getDbHelper().closeConnection();
    }

    public void updateTopic(String topicName, Long rank) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        
        Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
        topic.setId(rank);
        this.holder.getTopicDBHelper().update(topic);
        
        System.out.println("Topic " + topicName + " updated");
        this.holder.getDbHelper().closeConnection();
    }
    
    public void listTopics() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        List<Topic> topics = this.holder.getTopicDBHelper().getAll();
        System.out.println("Topics:");
        for (Topic topic : topics) {
            System.out.println("\t" + topic.getTopicName() + " rank:" + topic.getRank());
        }
        this.holder.getDbHelper().closeConnection();
    }
    
    public void createRule(Rule rule) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        this.holder.getDbHelper().initConnection();
        this.holder.getRuleDBHelper().save(rule);
        System.out.println("Rule created:" + rule.getName() + " id=" + rule.getId());
        this.holder.getDbHelper().closeConnection();
    }
    
    public void readFile(String file) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, FileNotFoundException {
        this.holder.getDbHelper().initConnection();
        this.reader.readFile(file);
        this.holder.getDbHelper().getConnection();
    }
    
    public Query startQuery(String text) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Query query = new Query();
        query.setText(text);
        query.setSession_id("1");
        query.setStartDate(new Date());
        this.holder.getDbHelper().initConnection();
        this.holder.getQueryDBHelper().start(query);
        this.holder.getDbHelper().closeConnection();
        System.out.println("Query created:" + query.getId());
        return query;
    }

    public SearchAnswerResult searchAnswer(Query query) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        this.holder.getDbHelper().initConnection();
        SearchAnswerResult result = this.holder.getSearchAnswer().search(query.getId());
        if (result != null) {
            System.out.println("Answer found:" + result.getRuleId() + " score:" + result.getScore());
            Rule rule = this.holder.getRuleDBHelper().getById(result.getRuleId());
            System.out.println("Rule:" + rule.getName());
            System.out.println("Answer:");
            List<Output> outputs = this.holder.getOutputDBHelper().findByRuleId(rule.getId());
            for (Output o : outputs) {
                System.out.println(o.getText());
            }
            if (outputs.size()>0) {
                Output output = outputs.get(0);
                query.setResponse(output.getText());
                query.setRule_id(rule.getId());
                query.setRequest(output.getRequest());
                this.holder.getQueryDBHelper().update(query);
            }
        } else {
            System.out.println("Answer not found");
        }
        this.holder.getDbHelper().closeConnection();
        return result;
    }
    
    public void ask(String text) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        Query q = this.startQuery(text);
        this.searchAnswer(q);
    }
    
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, FileNotFoundException {
        Console console = new Console();
        if (args.length <1) {
            System.out.println("Wrong argument");
        }
        if (args[0].equals("list-topics")) {
            console.listTopics();
        }
        if (args[0].equals("add-topic")) {
            if (args.length < 2) {
                System.out.println("Wrong argument");
            }
            console.createTopic(args[1]);
        }
        if (args[0].equals("delete-topic")) {
            if (args.length < 2) {
                System.out.println("Wrong argument");
            }
            console.deleteTopic(args[1]);
        }

        if (args[0].equals("update-topic")) {
            if (args.length < 2) {
                System.out.println("Wrong argument");
            }
            console.deleteTopic(args[1]);
        }
        
        if (args[0].equals("load")) {
            if (args.length < 2) {
                System.out.println("Wrong argument");
            }
            console.readFile(args[1]);
        }
        
        if (args[0].equals("start-query")) {
            if (args.length < 2) {
                System.out.println("Wrong argument");
            }
            console.startQuery(args[1]);
        }

        if (args[0].equals("search")) {
            if (args.length < 2) {
                System.out.println("Wrong argument");
            }
            console.ask(args[1]);
        }
    }

}
