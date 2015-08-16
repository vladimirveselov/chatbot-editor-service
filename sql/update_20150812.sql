ALTER TABLE sm_memory ADD COLUMN short_string_value VARCHAR(255);
ALTER TABLE sm_memory ADD COLUMN long_string_value TEXT;
ALTER TABLE sm_memory MODIFY sm_variable_value TINYINT(1) NULL;