SELECT
	A.rule_name,
    A.session_id
    FROM
(SELECT 
	count(m.id) cond_count,
    r.sm_rule_name rule_name,
    r.id rule_id,
    m.session_id session_id, 
    m.state_machine_id state_machine_id
    FROM 
    sm_memory m,
	sm_rules r,
    sm_conditions c,
    sm_variables v    
    WHERE
    m.state_machine_id = r.state_machine_id AND
    c.sm_rule_id = r.id AND
    v.id  = c.sm_variable_id AND
    v.sm_variable_name = m.sm_variable_name AND
    c.sm_variable_value = m.sm_variable_value
	GROUP BY c.sm_rule_id, m.session_id, m.state_machine_id
) A,
(SELECT 
count(c.id) var_count, 
c.sm_rule_id rule_id, 
r.sm_rule_name rule_name, 
r.state_machine_id state_machine_id
FROM sm_conditions c, sm_rules r
WHERE
c.sm_rule_id = r.id
GROUP BY c.sm_rule_id, r.state_machine_id) B
WHERE 
A.cond_count = B.var_count AND
A.rule_id = B.rule_id AND
A.state_machine_id = B.state_machine_id
;