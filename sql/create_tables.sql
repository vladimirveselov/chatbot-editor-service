-- USE chatbot;

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
DROP TABLE IF EXISTS sm_actions_rules;
DROP TABLE IF EXISTS sm_actions;
DROP TABLE IF EXISTS sm_conditions;
DROP TABLE IF EXISTS sm_rules;
DROP TABLE IF EXISTS sm_variables;
DROP TABLE IF EXISTS sm_state_machines;
DROP TABLE IF EXISTS sm_memory;

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

CREATE TABLE IF NOT EXISTS state_machines (
  id int(19) NOT NULL AUTO_INCREMENT,
  state_machine_name varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uc_StateMachineName (state_machine_name),
  KEY index_state_machine_name (state_machine_name) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  sm_actions  (
   id  int(19) NOT NULL AUTO_INCREMENT,
   state_machine_id  int(19) NOT NULL,
   sm_action_name  varchar(255) NOT NULL,
   action_text  text NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  uc_SMActionName  ( state_machine_id , sm_action_name ),
  CONSTRAINT  sm_actions_ibfk_1  FOREIGN KEY ( state_machine_id ) REFERENCES  state_machines  ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  sm_rules  (
   id  int(19) NOT NULL AUTO_INCREMENT,
   state_machine_id  int(19) NOT NULL,
   sm_rule_name  varchar(255) NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  uc_SMRuleName  ( state_machine_id , sm_rule_name ),
  CONSTRAINT  sm_rules_ibfk_1  FOREIGN KEY ( state_machine_id ) REFERENCES  state_machines  ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  sm_actions_rules  (
   id  int(19) NOT NULL AUTO_INCREMENT,
   sm_rule_id  int(19) NOT NULL,
   sm_action_id  int(19) NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  uc_SMActionRuleRecord  ( sm_rule_id , sm_action_id ),
  KEY  sm_action_id  ( sm_action_id ),
  CONSTRAINT  sm_actions_rules_ibfk_1  FOREIGN KEY ( sm_rule_id ) REFERENCES  sm_rules  ( id ),
  CONSTRAINT  sm_actions_rules_ibfk_2  FOREIGN KEY ( sm_action_id ) REFERENCES  sm_actions  ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE  sm_memory  (
   id  int(19) NOT NULL AUTO_INCREMENT,
   session_id  varchar(255) NOT NULL,
   state_machine_id  int(19) DEFAULT NULL,
   sm_variable_name  varchar(255) NOT NULL,
   sm_variable_value  tinyint(1) DEFAULT NULL,
   sm_last_modified  timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   short_string_value  varchar(255) DEFAULT NULL,
   long_string_value  text,
  PRIMARY KEY ( id ),
  UNIQUE KEY  idx_sm_memory_session_id_sm_variable_name  ( session_id , sm_variable_name ),
  KEY  index_sm_memory_matching  ( session_id , state_machine_id , sm_variable_name , sm_variable_value ) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE  sm_variables  (
   id  int(19) NOT NULL AUTO_INCREMENT,
   state_machine_id  int(19) NOT NULL,
   sm_variable_name  varchar(255) NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  uc_SMVariableName  ( state_machine_id , sm_variable_name ),
  CONSTRAINT  sm_variables_ibfk_1  FOREIGN KEY ( state_machine_id ) REFERENCES  state_machines  ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  sm_conditions  (
   id  int(19) NOT NULL AUTO_INCREMENT,
   sm_rule_id  int(19) NOT NULL,
   sm_variable_id  int(19) NOT NULL,
   sm_variable_value  tinyint(1) NOT NULL,
  PRIMARY KEY ( id ),
  UNIQUE KEY  uc_SMConditionName  ( sm_rule_id , sm_variable_id ),
  KEY  index_sm_conditions_matching  ( sm_variable_id , sm_variable_value ) USING BTREE,
  CONSTRAINT  sm_conditions_ibfk_1  FOREIGN KEY ( sm_rule_id ) REFERENCES  sm_rules  ( id ),
  CONSTRAINT  sm_conditions_ibfk_2  FOREIGN KEY ( sm_variable_id ) REFERENCES  sm_variables  ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;