select * from messages

SELECT t1.* FROM messages t1
JOIN (
    SELECT incoming_id ,outgoing_id, MAX(message_id) as message_id
    FROM messages
    GROUP BY incoming_id ,outgoing_id
) t2 ON t1.message_id = t2.message_id where t2.incoming_id=1 or t2.outgoing_id=1

select incoming_id,outgoing_id,max(message_id) from messages group by incoming_id,outgoing_id

Create table messages( 
	message_id SERIAL PRIMARY KEY,   
  	incoming_id INT NOT NULL, 
	outgoing_id INT NOT NULL,
	messages TEXT NOT NULL,
	FOREIGN KEY(incoming_id) REFERENCES membership(member_id),
	FOREIGN KEY(outgoing_id) REFERENCES membership(member_id)
);

Create table membership( 
	member_id SERIAL PRIMARY KEY,   
  	member_uniqeid SERIAL NOT NULL, 
	member_first_name VARCHAR(20) NOT NULL,
	member_last_name VARCHAR(10) ,
	member_email VARCHAR(25) NOT NULL,
	member_password VARCHAR(20) NOT NULL,
	member_photo BYTEA,
	is_active Boolean Default false
); 



select * from membership

select * from messages

DROP TABLE membership

DROP TABLE messages

truncate table membership












truncate table messages