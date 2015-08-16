package org.vvv.chatbotdb.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vvv.chatbot.client.excel.ExcelSheet;
import org.vvv.chatbot.client.excel.ExcelUtils;
import org.vvv.chatbot.client.excel.StateMachineReader;
import org.vvv.chatbotdb.model.SMAction;
import org.vvv.chatbotdb.model.SMMemory;
import org.vvv.chatbotdb.model.SMVariable;
import org.vvv.chatbotdb.model.StateMachine;

public class StateMachineDBHelperTest {
	
	public static final String TEST_STATE_MACHINE_NAME = "Example1";

    private static Log log = LogFactory.getLog(StateMachineDBHelperTest.class);
	
	private Holder holder;
	
	private StateMachineDBHelper helper;

	@Before
	public void setUp() throws Exception {
        holder = new Holder();
        holder.init();
        helper = holder.getStateMachineDBHelper();
	}

	@After
	public void tearDown() throws Exception {
	    try {
	        StateMachine sm = helper.getStateMachine(TEST_STATE_MACHINE_NAME);
	        if (sm != null) {
	            log.info("deleting state machine :" + sm.getName());
	            helper.deleteStateMachine(sm);
	            log.info("deleted sm");
	        }
	    } catch (Exception e) {
	        log.error("error during cleaning", e);
	    }
	    holder.getDbHelper().closeConnection();
	}

	@Test
	public void testGetVariableNames() throws FileNotFoundException, IOException, WrongFormatException {
		try(FileInputStream fis = new FileInputStream("docs/StateMachineExample.xlsx")) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<ExcelSheet> sheets = excelUtils.readFromExcel(fis);
    		StateMachine sm = new StateMachine();
    		sm.setName(sheets.get(0).getName());
    		StateMachineReader reader = new StateMachineReader ();
    		Map<Integer, SMVariable> variables = reader.getVariables(sheets.get(0).getData(), sm);
    		log.info(variables.toString());
    	}
	}
	
	@Test
	public void testGetActionNames() throws FileNotFoundException, IOException, WrongFormatException {
		try(FileInputStream fis = new FileInputStream("docs/StateMachineExample.xlsx")) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<ExcelSheet> sheets = excelUtils.readFromExcel(fis);
            StateMachineReader reader = new StateMachineReader ();
            StateMachine sm = new StateMachine();
            sm.setName(sheets.get(0).getName());
    		Map<Integer, SMAction> actions= reader.getActions(sheets.get(0).getData(), sm);
    		log.info(actions.toString());
    	}
	}
	
	@Test
	public void testStateMachine() throws FileNotFoundException, IOException, WrongFormatException, InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		try(FileInputStream fis = new FileInputStream("docs/StateMachineExample.xlsx")) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<ExcelSheet> data = excelUtils.readFromExcel(fis);
    		StateMachineReader reader = new StateMachineReader ();
    		StateMachine m = reader.convert(data.get(0));
    		m.setName(TEST_STATE_MACHINE_NAME);
    		log.info(m.getRules().toString());
    		m = helper.saveStateMachine(m);
    		log.info("saved sm");
    		for(SMVariable var : m.getVariables()) {
    			SMMemory memory = new SMMemory();
    			memory.setSessionId("1");
    			memory.setSmVariableName(var.getName());
    			memory.setStateMachineName(m.getName());
    			memory.setValue(Boolean.TRUE);
    			helper.saveSMMemory(memory);
    		}
    		
    		helper.deleteStateMachine(m);
    		log.info("deleted sm");
    		helper.getDbHelper().closeConnection();
    	}
	}

}
