DROP USER 'chatbot'@'localhost';
CREATE USER 'chatbot'@'localhost' IDENTIFIED BY 'sql2015';
GRANT ALL PRIVILEGES ON *.* TO 'chatbot'@'localhost';
FLUSH PRIVILEGES;

