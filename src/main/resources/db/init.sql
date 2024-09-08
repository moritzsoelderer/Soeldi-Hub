USE soeldihub;

CREATE TABLE user(
    id int auto_increment PRIMARY KEY,
    username VARCHAR(60) UNIQUE,
    password VARCHAR(40)
);

INSERT INTO user (username, password) VALUES('soeldi', 'soeldispassword');
INSERT INTO user (username, password) VALUES('sophie', 'sophiespassword');

CREATE TABLE flow(
    id int auto_increment PRIMARY KEY,
    title VARCHAR(40),
    uploaded_at timestamp default CURRENT_TIMESTAMP,
    uploaded_by int,
    source VARCHAR(120),
    FOREIGN KEY (uploaded_by) REFERENCES user(id)
);

INSERT INTO flow (title, uploaded_by, source) VALUES('test flow', 1, 'Snapchat-65967388.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow2', 2, 'Snapchat-159574429.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow3', 2, 'Snapchat-260561450.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow4', 1, 'Snapchat-1597124343.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow5', 2, 'Snapchat-65967388.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow6', 2, 'Snapchat-159574429.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow7', 1, 'Snapchat-1597124343.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow8', 2, 'Snapchat-65967388.mp4');
INSERT INTO flow (title, uploaded_by, source) VALUES('test flow9', 2, 'Snapchat-260561450.mp4');