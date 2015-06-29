package org.vvv.chatbotdb.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleDBHelperTest {

    private static Log log = LogFactory.getLog(RuleDBHelperTest.class);

    private Holder holder;

    @Before
    public void setUp() throws Exception {
        CreateTestDB c = new CreateTestDB();
        c.initDB();
        holder = new Holder();
        holder.init();
        holder.getDbHelper()
                .setConnectionString(
                        "jdbc:mysql://localhost:3306/chatbot_test?user=chatbot_test&password=sql");
        holder.getDbHelper().initConnection();

    }

    @After
    public void tearDown() throws Exception {
        holder.getDbHelper().closeConnection();
    }

    @Test
    public void testCRUD() throws SQLException {
        RuleDBHelper helper = this.holder.getRuleDBHelper();
        Topic topic = new Topic();
        topic.setTopicName("testtopic2");
        topic.setRank(100L);
        this.holder.getTopicDBHelper().save(topic);
        log.info("Saved topic: " + topic.getId());

        Rule rule = new Rule();
        rule.setTopic(topic);
        rule.setName("testRuleName");
        rule.setRank(100L);

        helper.save(rule);
        log.info("Saved rule: " + rule.getId());

        Rule rule2 = helper.getById(rule.getId());
        assertEquals(rule2.getName(), rule.getName());

        helper.delete(rule);
        Rule rule3 = helper.getById(rule.getId());
        assertTrue(rule3 == null);

        this.holder.getTopicDBHelper().delete(topic);
        Topic top3 = this.holder.getTopicDBHelper().getById(topic.getId());
        assertTrue(top3 == null);
    }

    @Test
    public void testFullRule() throws SQLException {
        Topic topic = new Topic();
        topic.setTopicName("начало");
        this.holder.getTopicDBHelper().save(topic);

        Rule rule = new Rule();
        rule.setTopic(topic);

        Set<Input> inputs = new HashSet<Input>();
        for (int i = 0; i < 5; i++) {
            Input input = new Input();
            input.setRule(rule);
            input.setText("a b c d e f g абырвалг " + i);
            inputs.add(input);
        }
        rule.setInputs(inputs);

        rule.setName("пушкин пушкин");

        Set<Output> outputs = new HashSet<Output>();
        for (int i = 0; i < 5; i++) {
            Output output = new Output();
            output.setRule(rule);
            output.setText("ответ " + i);
            outputs.add(output);
        }

        rule.setOutputs(outputs);

        this.holder.getRuleDBHelper().save(rule);

        this.holder.getRuleDBHelper().getById(rule.getId());

        this.holder.getRuleDBHelper().delete(rule);

        this.holder.getTopicDBHelper().delete(topic);
    }

}