package org.vvv.chatbotdb.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vvv.chatbotdb.model.SMAction;
import org.vvv.chatbotdb.model.SMVariable;
import org.vvv.chatbotdb.model.StateMachine;
import org.vvv.chatbotdb.utils.ExcelUtils;

public class StateMachineDBHelperTest {
	
	private static Log log = LogFactory.getLog(StateMachineDBHelperTest.class);

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetVariableNames() throws FileNotFoundException, IOException, WrongFormatException {
		try(FileInputStream fis = new FileInputStream("docs/StateMachineExample.xlsx")) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<List<List<String>>> data = excelUtils.readFromExcel(fis);
    		StateMachineDBHelper helper = new StateMachineDBHelper();
    		Map<Integer, SMVariable> variables = helper.getVariables(data.get(0), null);
    		log.info(variables.toString());
    	}
	}
	
	@Test
	public void testGetActionNames() throws FileNotFoundException, IOException, WrongFormatException {
		try(FileInputStream fis = new FileInputStream("docs/StateMachineExample.xlsx")) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<List<List<String>>> data = excelUtils.readFromExcel(fis);
    		StateMachineDBHelper helper = new StateMachineDBHelper();
    		Map<Integer, SMAction> actions= helper.getActions(data.get(0), null);
    		log.info(actions.toString());
    	}
	}
	
	@Test
	public void testStateMachine() throws FileNotFoundException, IOException, WrongFormatException {
		try(FileInputStream fis = new FileInputStream("docs/StateMachineExample.xlsx")) {
    		ExcelUtils excelUtils = new ExcelUtils();
    		List<List<List<String>>> data = excelUtils.readFromExcel(fis);
    		StateMachineDBHelper helper = new StateMachineDBHelper();
    		StateMachine m = helper.convert(data.get(0));
    		log.info(m.getRules().toString());
    	}
	}

}
