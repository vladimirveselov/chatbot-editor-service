DROP SCHEMA IF EXISTS `chatbot`;
CREATE SCHEMA `chatbot` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;
DROP USER 'chatbot'@'localhost';
CREATE USER 'chatbot'@'localhost' IDENTIFIED BY 'sql2015';
GRANT ALL PRIVILEGES ON *.* TO 'chatbot'@'localhost';
FLUSH PRIVILEGES;

