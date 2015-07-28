package org.vvv.chatbotdb.dao;

import java.sql.SQLException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.test.IntegrationTest;

@Category(IntegrationTest.class)
public class QueryDBHelperTest {
    
    private static Log log = LogFactory.getLog(QueryDBHelperTest.class);
    
    private Holder holder;
    @Before
    public void setUp() throws Exception {
        holder = new Holder();
        holder.init();
        holder.getDbHelper().initConnection();
        
    }

    @After
    public void tearDown() throws Exception {
        holder.getDbHelper().closeConnection();
    }

    @Test
    public void testStart() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Query query = new Query();
        query.setText("hi how are you?");
        query.setStartDate(new Date());
        query.setSession_id("1");
        
        holder.getQueryDBHelper().start(query);
        
        query.setResponse("hehehe");
        query.setRule_id(-1000L);
        
        holder.getQueryDBHelper().update(query);
    }
    
    @Test
    public void testStart2() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Query query = new Query();
        query.setText("что такое велосипед?");
        query.setStartDate(new Date());
        query.setSession_id("1");
        
        holder.getQueryDBHelper().start(query);
    }

}
