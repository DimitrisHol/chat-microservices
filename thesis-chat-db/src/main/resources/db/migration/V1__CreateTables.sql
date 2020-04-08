CREATE TABLE users (
	user_id serial PRIMARY KEY,
	username VARCHAR (50) UNIQUE NOT NULL
);


CREATE TABLE messages(
    message_id serial primary key,
    author_id integer references users(user_id),
    content varchar(50) not null,
    submitted_at timestamp
);