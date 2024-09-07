USE soeldihub;

CREATE TABLE user(
    id int auto_increment PRIMARY KEY,
    username VARCHAR(60) UNIQUE,
    password VARCHAR(40)
);

INSERT INTO user (username, password) VALUES('soeldi', 'soeldispassword');
INSERT INTO user (username, password) VALUES('sophie', 'sophiespassword');