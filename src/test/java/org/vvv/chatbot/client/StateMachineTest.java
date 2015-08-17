package org.vvv.chatbot.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.vvv.chatbot.client.excel.ExcelSheet;
import org.vvv.chatbot.client.excel.ExcelUtils;
import org.vvv.chatbot.client.excel.StateMachineReader;
import org.vvv.chatbot.client.excel.TopicReader;
import org.vvv.chatbot.client.excel.WrongFileFormatException;
import org.vvv.chatbotdb.dao.WrongFormatException;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.StateMachine;
import org.vvv.chatbotdb.model.Topic;
import org.vvv.chatbotdb.test.IntegrationTest;

@Category(IntegrationTest.class)
public class StateMachineTest {
	
	private static Log log = LogFactory.getLog(StateMachineTest.class);
	
	private String fileName = "docs/RulesExample.xlsx";

	@Before
    public void tearUp() {
        ChatbotClient client = new ChatbotClient();
        client.deleteTopic("Привет");
        client.deleteStateMachine("ПравилаПриветствия");
    }
	
	@Test
	public void testStateMachines() throws WrongFormatException, WrongFileFormatException, IOException {
	    Topic topic = null;
	    StateMachine sm = null;
	    try(FileInputStream fis = new FileInputStream(fileName)) {
            ExcelUtils excelUtils = new ExcelUtils();
            List<ExcelSheet> data = excelUtils.readFromExcel(fis);
            TopicReader topicReader  = new TopicReader();
            topic = topicReader.convert(data.get(0));
            StateMachineReader reader = new StateMachineReader ();
            sm = reader.convert(data.get(1));
        }
	    log.info("topic - " + topic.toString());
	    log.info("state machine - " + sm.toString());
	    ChatbotClient client = new ChatbotClient();
	    client.createTopic(topic);
	    client.createStateMachine(sm);
	    
	    Query query = new Query();
	    query.setText("Привет");
	    query = client.getAnswerPOST(query);
	    log.info("answer:" + query);
	    
	    query.setText("Привет");
        query = client.getAnswerPOST(query);
        log.info("answer:" + query);
        
        query.setText("Пока");
        query = client.getAnswerPOST(query);
        log.info("answer:" + query);

        query.setText("Пока");
        query = client.getAnswerPOST(query);
        log.info("answer:" + query);

//	    client.deleteTopic(topic.getTopicName());
//	    client.deleteStateMachine(sm.getName());
	}
	
}
