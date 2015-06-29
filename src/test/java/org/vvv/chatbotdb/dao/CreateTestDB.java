package org.vvv.chatbotdb.dao;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

public class CreateTestDB {
    
    public void initDB() {
        SQLExec sqlExec=new SQLExec();
        sqlExec.setUserid("chatbot_test");
        sqlExec.setPassword("sql");
        sqlExec.setUrl("jdbc:mysql://localhost:3306/chatbot_test");
        sqlExec.setDriver("com.mysql.jdbc.Driver");
        sqlExec.setProject(new Project());
        sqlExec.setSrc(new File("sql/create_tables.sql"));
        sqlExec.execute();
    }
}
