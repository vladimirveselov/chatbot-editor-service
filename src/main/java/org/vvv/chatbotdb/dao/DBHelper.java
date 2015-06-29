package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private Connection connection;

    private String connectionString = "jdbc:mysql://localhost:3306/chatbot?user=chatbot&password=sql2015&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";

    private String driverClass = "com.mysql.jdbc.Driver";

    private String user;

    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public void initConnection() throws SQLException, InstantiationException,
            IllegalAccessException, ClassNotFoundException {
        Class.forName(this.driverClass).newInstance();
        if (user == null) {
            this.connection = DriverManager
                    .getConnection(this.connectionString);
        } else {
            this.connection = DriverManager
                    .getConnection(this.connectionString, user, password);

        }
    }

    public void closeConnection() throws SQLException {
        this.connection.close();
    }

    public Connection getConnection() {
        return this.connection;
    }

}
