package org.vvv.chatbotdb.dao;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vvv.chatbotdb.model.Output;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class SearchAnswerDBHelperTest {
	
	private static Log log = LogFactory.getLog(SearchAnswerDBHelperTest.class);

	private Holder holder;
    @Before
    public void setUp() throws Exception {
        holder = new Holder();
        holder.init();
        holder.getDbHelper().initConnection();
        Topic topic1 = new Topic();
        topic1.setTopicName("topic1");
        holder.getTopicDBHelper().save(topic1);
        Rule rule1 = new Rule()
        	.withTopic(topic1)
        	.withName("rule1")
        	.withInputs(
        		"how are you",
        		"hi",
        		"hello",
        		"good afternoon")
        	.withOutputs(
        		"Hello!",
        		"I am very good");
        Rule rule2 = new Rule()
        	.withName("default")
        	.withTopic(topic1)
        	.withOutputs("catch all");
        Rule rule3 = new Rule()
	    	.withName("ruleRequest")
        	.withInputs("start request")
	    	.withTopic(topic1)
	    	.withOutputs(
	    		new Output()
	    		.withText("starting request")
	    		.withRequest("requestId"));
        Rule rule4 = new Rule()
	    	.withName("ruleResponseYes")
        	.withInputs("yes", "sure")
        	.withResponse("requestId")
	    	.withTopic(topic1)
	    	.withOutputs("responded to requestId yes");
        Rule rule5 = new Rule()
	    	.withName("ruleResponseNo")
        	.withInputs("not", "no", "never")
        	.withResponse("requestId")
	    	.withTopic(topic1)
	    	.withOutputs("responded to requestId no");
        holder.getRuleDBHelper().save(rule1);
        holder.getRuleDBHelper().save(rule2);
        holder.getRuleDBHelper().save(rule3);
        holder.getRuleDBHelper().save(rule4);
        holder.getRuleDBHelper().save(rule5);
    }

	@After
	public void tearDown() throws Exception {
		RuleDBHelper h = holder.getRuleDBHelper(); 
		h.delete(h.getByNameAndTopic("topic1", "rule1"));
		h.delete(h.getByNameAndTopic("topic1", "default"));
		h.delete(h.getByNameAndTopic("topic1", "ruleRequest"));
		h.delete(h.getByNameAndTopic("topic1", "ruleResponseYes"));
		h.delete(h.getByNameAndTopic("topic1", "ruleResponseNo"));
		TopicDBHelper t = holder.getTopicDBHelper();
		t.delete("topic1");
		holder.getDbHelper().closeConnection();
	}

	@Test
	public void test() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		SearchAnswerDBHelper sh = holder.getSearchAnswer();
		List<ChatbotTestCase> tc = new ArrayList<>();
		tc.add(new ChatbotTestCase("hi", "rule1"));
		tc.add(new ChatbotTestCase("test default", "default"));
		tc.add(new ChatbotTestCase("start request", "ruleRequest"));
		tc.add(new ChatbotTestCase("yes", "ruleResponseYes"));
		tc.add(new ChatbotTestCase("start request", "ruleRequest"));
		tc.add(new ChatbotTestCase("no", "ruleResponseNo"));
		Query q = new Query();
		for (ChatbotTestCase t : tc) {
			q.setText(t.getQuestion());
			q = sh.process(q);
			Rule rule = holder.getRuleDBHelper().getById(q.getRule_id());
			log.info(q.getText() + " === " + q.getResponse() 
					+ " rule = " + rule.getName() 
					+ " topic = " + rule.getTopic().getTopicName());
			assertEquals(t.getExpectedRuleName(), rule.getName());
		}
	}

}
