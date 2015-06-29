package org.vvv.chatbotdb.dao;

public class DBObject {

    private Holder holder;
    
    private DBHelper dbHelper;

    public DBHelper getDbHelper() {
        return dbHelper;
    }

    public void setDbHelper(DBHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public Holder getHolder() {
        return holder;
    }

    public void setHolder(Holder holder) {
        this.holder = holder;
    }
    
}
