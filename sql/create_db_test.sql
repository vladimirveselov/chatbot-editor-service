DROP SCHEMA IF EXISTS `chatbot_test`;
CREATE SCHEMA `chatbot_test` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
DROP USER 'chatbot_test'@'localhost';
CREATE USER 'chatbot_test'@'localhost' IDENTIFIED BY 'sql';
GRANT ALL PRIVILEGES ON *.* TO 'chatbot_test'@'localhost';
FLUSH PRIVILEGES;

