DROP USER 'chatbot_test'@'localhost';
CREATE USER 'chatbot_test'@'localhost' IDENTIFIED BY 'sql';
GRANT ALL PRIVILEGES ON *.* TO 'chatbot_test'@'localhost';
FLUSH PRIVILEGES;

