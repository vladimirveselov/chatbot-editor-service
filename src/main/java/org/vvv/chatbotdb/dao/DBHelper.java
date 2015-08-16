package org.vvv.chatbotdb.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DBHelper {
    
    private static Log log = LogFactory.getLog(DBHelper.class);

	private Connection connection;

	private String connectionString = "jdbc:mysql://localhost:3306/chatbot?user=chatbot&password=sql2015&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";

	private String driverClass = "com.mysql.jdbc.Driver";

	private String user;

	private String password;

	private String dbname = "chatbot";

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
	    log.info("driver:" + this.driverClass);
	    log.info("connection string:" + this.connectionString.substring(0, 20));
		Class.forName(this.driverClass).newInstance();
		if (user == null) {
			this.connection = DriverManager
					.getConnection(this.connectionString);
		} else {
			this.connection = DriverManager.getConnection(
					this.connectionString, user, password);

		}
	}

	public void initRDSConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		this.dbname = System.getProperty("RDS_DB_NAME");
		this.user = System.getProperty("RDS_USERNAME");
		this.password = System.getProperty("RDS_PASSWORD");
		String hostname = System.getProperty("RDS_HOSTNAME");
		String port = System.getProperty("RDS_PORT");
		this.connectionString = "jdbc:mysql://"
				+ hostname
				+ ":"
				+ port
				+ "/"
				+ dbname
				+ "?user="
				+ user
				+ "&password="
				+ password
				+ "&useUnicode=true&characterEncoding=utf8&characterSetResults=utf8";
		this.initConnection();
	}

	public void closeConnection() throws SQLException {
		this.connection.close();
	}

	public Connection getConnection() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			try {
				this.initConnection();
				log.info("open local connection");
			} catch (Exception e) {
				this.initRDSConnection();
			}
		}
		return this.connection;
	}

}
