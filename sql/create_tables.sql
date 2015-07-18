USE chatbot;

DROP TABLE IF EXISTS split_queries;
DROP TABLE IF EXISTS queries;
DROP TABLE IF EXISTS words;
DROP TABLE IF EXISTS split_inputs;
DROP TABLE IF EXISTS inputs;
DROP TABLE IF EXISTS actions;
DROP TABLE IF EXISTS outputs;
DROP TABLE IF EXISTS rules;
DROP TABLE IF EXISTS topics_chatbots;
DROP TABLE IF EXISTS topics;
DROP TABLE IF EXISTS chatbots;


CREATE TABLE IF NOT EXISTS topics (
  id INT(19) NOT NULL AUTO_INCREMENT,
  topic_name VARCHAR(255) NOT NULL,
  rank INT(11),
  PRIMARY KEY (id),
  CONSTRAINT uc_TopicName UNIQUE (topic_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX index_topic_name ON topics(topic_name) USING BTREE;

CREATE TABLE IF NOT EXISTS rules (
  id INT(19) NOT NULL AUTO_INCREMENT,
  topic_id INT(19),
  rule_name VARCHAR(255) NOT NULL,
  rank INT(11),
  response VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (topic_id)
    REFERENCES topics(id),
  CONSTRAINT uc_RuleName UNIQUE (topic_id, rule_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS inputs (
  id INT(19) NOT NULL AUTO_INCREMENT,
  rule_id INT(19),
  input_text TEXT NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (rule_id)
    REFERENCES rules(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS outputs (
  id INT(19) NOT NULL AUTO_INCREMENT,
  rule_id INT(19),
  output_text TEXT NOT NULL,
  request VARCHAR(255),
  PRIMARY KEY (id),
  FOREIGN KEY (rule_id)
    REFERENCES rules(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS actions (
  id INT(19) NOT NULL AUTO_INCREMENT,
  rule_id INT(19),
  action_body TEXT NOT NULL,
  priority INT(19),
  PRIMARY KEY (id),
  FOREIGN KEY (rule_id)
    REFERENCES rules(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS split_inputs (
  id INT(19) NOT NULL AUTO_INCREMENT,
  input_id INT(19) NOT NULL,
  prev_word varchar(255),
  word varchar(255) NOT NULL,
  next_word varchar(255),
  PRIMARY KEY (id),
  FOREIGN KEY (input_id)
    REFERENCES inputs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX index_InputID ON split_inputs(input_id) USING BTREE;
CREATE INDEX index_word ON split_inputs(word) USING BTREE;
CREATE INDEX index_word_triplet ON split_inputs(prev_word, word, next_word) USING BTREE;

CREATE TABLE IF NOT EXISTS queries (
  id INT(19) NOT NULL AUTO_INCREMENT,
  query_text TEXT NOT NULL,
  session_id varchar(255),
  chatbot_name varchar(255),
  event_date timestamp,
  response TEXT,
  actions TEXT,
  request VARCHAR(255),
  rule_id INT(19),
  PRIMARY KEY (id)  
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE INDEX index_session_chatbot ON queries(session_id, chatbot_name) USING BTREE;

CREATE TABLE IF NOT EXISTS split_queries (
  id INT(19) NOT NULL AUTO_INCREMENT,
  query_id INT(19),
  prev_word varchar(255),
  word varchar(255),
  next_word varchar(255),
  PRIMARY KEY (id),
  FOREIGN KEY (query_id)
    REFERENCES queries(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS words (
  id INT(19) NOT NULL AUTO_INCREMENT,
  syn_to_id INT(19),
  word varchar(255),
  PRIMARY KEY (id),
  FOREIGN KEY (syn_to_id)
    REFERENCES words(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS chatbots (
  id INT(19) NOT NULL AUTO_INCREMENT,
  chatbot_name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uc_ChatbotName UNIQUE (chatbot_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS topics_chatbots (
  id INT(19) NOT NULL AUTO_INCREMENT,
  topic_id INT(19) NOT NULL,
  chatbot_id INT(19) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (topic_id)
    REFERENCES topics(id),
  FOREIGN KEY (chatbot_id)
    REFERENCES chatbots(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;