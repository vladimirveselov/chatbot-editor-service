
select r.id, r.rule_name, b.id, b.input_text,sum(d.weight)*r.rank*t.rank total, c.query_id from 
	split_inputs a, 
    inputs b, 
    split_queries c,
    (select word, (1.0/count(id)) weight from split_inputs group by word) d,
    rules r,
    topics t
where 
((
	b.id = a.input_id and
    a.word = d.word and
    a.word = c.word 
) OR (
	b.id = a.input_id and
    a.word = d.word and
    a.prev_word = c.prev_word and
    a.word = c.word and
    a.next_word = c.next_word 
) OR (
	b.id = a.input_id and
    a.word = d.word and
    a.word = c.word and
    a.next_word = c.next_word 
) OR (
	b.id = a.input_id and
    a.word = d.word and
    a.prev_word = c.prev_word and
    a.word = c.word
)) AND
b.rule_id = r.id AND
r.topic_id = r.topic_id AND
c.query_id = 8

group by b.id
order by total desc
limit 10
;
