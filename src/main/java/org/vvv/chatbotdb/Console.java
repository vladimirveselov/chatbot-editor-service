package org.vvv.chatbotdb;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vvv.chatbotdb.dao.Holder;
import org.vvv.chatbotdb.model.Query;
import org.vvv.chatbotdb.model.Rule;
import org.vvv.chatbotdb.model.Topic;

public class Console {

	private static Log log = LogFactory.getLog(Console.class);

	private String connectionString = "jdbc:mysql://localhost:3306/chatbot?user=chatbot&password=sql2015";

	private Holder holder;

	private Reader reader;

	private String properties = "db.properties";

	public Console() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(this.properties)) {
			props.load(fis);
			this.connectionString = props.getProperty("url", connectionString);
		} catch (FileNotFoundException e) {
			log.warn(this.properties + " file not found in "
					+ System.getProperty("user.dir") + " error "
					+ e.getMessage());
		} catch (IOException e) {
			log.warn(this.properties + " file cannot be opened "
					+ System.getProperty("user.dir") + " error "
					+ e.getMessage());
			e.printStackTrace();
		}

		this.holder = new Holder();
		this.holder.init();

		this.holder.getDbHelper().setConnectionString(connectionString);
		this.reader = new Reader();
		this.reader.setConsole(this);
	}

	public Topic createTopic(String topicName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();
		Topic topic = new Topic();
		topic.setTopicName(topicName);
		this.holder.getTopicDBHelper().save(topic);
		System.out.println("Topic " + topicName + " created");
		this.holder.getDbHelper().closeConnection();
		return topic;
	}

	public boolean topicExists(String topicName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();
		Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
		System.out.println("Topic " + topicName + " created");
		this.holder.getDbHelper().closeConnection();
		return topic != null;
	}

	public Topic getTopic(String topicName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();
		Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
		System.out.println("Topic " + topicName + " created");
		this.holder.getDbHelper().closeConnection();
		return topic;
	}

	public void deleteTopic(String topicName) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();
		Topic topic = new Topic();
		topic.setTopicName(topicName);
		this.holder.getTopicDBHelper().delete(topicName);
		System.out.println("Topic " + topicName + " deleted");
		this.holder.getDbHelper().closeConnection();
	}

	public void updateTopic(String topicName, Long rank)
			throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();

		Topic topic = this.holder.getTopicDBHelper().getByName(topicName);
		topic.setId(rank);
		this.holder.getTopicDBHelper().update(topic);

		System.out.println("Topic " + topicName + " updated");
		this.holder.getDbHelper().closeConnection();
	}

	public void listTopics() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();
		List<Topic> topics = this.holder.getTopicDBHelper().getAll();
		System.out.println("Topics:");
		for (Topic topic : topics) {
			System.out.println("\t" + topic.getTopicName() + " rank:"
					+ topic.getRank());
		}
		this.holder.getDbHelper().closeConnection();
	}

	public void createRule(Rule rule) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		this.holder.getDbHelper().initConnection();
		this.holder.getRuleDBHelper().save(rule);
		System.out.println("Rule created:" + rule.getName() + " id="
				+ rule.getId());
		this.holder.getDbHelper().closeConnection();
	}

	public void readFile(String file) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			FileNotFoundException {
		this.holder.getDbHelper().initConnection();
		this.reader.readFile(file);
		this.holder.getDbHelper().getConnection();
	}

	public Query startQuery(String text) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		Query query = new Query();
		query.setText(text);
		query.setSession_id("1");
		query.setStartDate(new Date());
		this.holder.getDbHelper().initConnection();
		this.holder.getQueryDBHelper().start(query);
		this.holder.getDbHelper().closeConnection();
		System.out.println("Query created:" + query.getId());
		return query;
	}

	public Query searchAnswer(Query query) throws SQLException,
			InstantiationException, IllegalAccessException,
			ClassNotFoundException {
		this.holder.getDbHelper().initConnection();
		query = this.holder.getSearchAnswer().search(query);
		this.holder.getDbHelper().closeConnection();
		return query;
	}

	public void ask(String text) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException {
		Query q = this.startQuery(text);
		this.searchAnswer(q);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, SQLException,
			FileNotFoundException {
		Console console = new Console();
		if (args.length < 1) {
			System.out.println("Wrong argument");
			return;
		}
		if (args[0].equals("list-topics")) {
			console.listTopics();
		}
		if (args[0].equals("add-topic")) {
			if (args.length < 2) {
				System.out.println("Wrong argument");
			}
			console.createTopic(args[1]);
		}
		if (args[0].equals("delete-topic")) {
			if (args.length < 2) {
				System.out.println("Wrong argument");
			}
			console.deleteTopic(args[1]);
		}

		if (args[0].equals("update-topic")) {
			if (args.length < 2) {
				System.out.println("Wrong argument");
			}
			console.deleteTopic(args[1]);
		}

		if (args[0].equals("load")) {
			if (args.length < 2) {
				System.out.println("Wrong argument");
			}
			for (int i=1; i<args.length; i++) {
				console.readFile(args[i]);
			}
		}

		if (args[0].equals("start-query")) {
			if (args.length < 2) {
				System.out.println("Wrong argument");
			}
			console.startQuery(args[1]);
		}

		if (args[0].equals("search")) {
			if (args.length < 2) {
				System.out.println("Wrong argument");
			}
			console.ask(args[1]);
		}
	}

}
