USE soeldihub;

CREATE TABLE user(
    id int auto_increment PRIMARY KEY,
    username VARCHAR(60) UNIQUE,
    password VARCHAR(40)
);

INSERT INTO user (username, password) VALUES('soeldi', 'soeldispassword');
INSERT INTO user (username, password) VALUES('sophie', 'sophiespassword');
INSERT INTO user (username, password) VALUES('marie', 'mariespassword');

CREATE TABLE flow(
    id int auto_increment PRIMARY KEY,
    title VARCHAR(40),
    uploaded_at timestamp default CURRENT_TIMESTAMP,
    uploaded_by int,
    caption VARCHAR(150),
    source VARCHAR(120),
    FOREIGN KEY (uploaded_by) REFERENCES user(id)
);

INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow', 1, 'test caption', 'Snapchat-65967388.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow2', 2, 'test caption', 'Snapchat-159574429.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow3', 2, 'test caption', 'Snapchat-260561450.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow4', 1, 'test caption', 'Snapchat-1597124343.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow5', 2, 'test caption', 'Snapchat-65967388.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow6', 2, 'test caption', 'Snapchat-159574429.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow7', 1, 'test caption', 'Snapchat-1597124343.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow8', 2, 'test caption', 'Snapchat-65967388.mp4');
INSERT INTO flow (title, uploaded_by, caption, source) VALUES('test flow9', 2, 'test caption', 'Snapchat-260561450.mp4');

CREATE TABLE user_likes_flow(
    id int auto_increment PRIMARY KEY,
    user_id int,
    flow_id int,
    created_at timestamp default CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (flow_id) REFERENCES flow(id),
    CONSTRAINT UNIQUE index(user_id, flow_id)
);

INSERT INTO user_likes_flow (user_id, flow_id) VALUES(2, 1);
INSERT INTO user_likes_flow (user_id, flow_id) VALUES(3, 1);
INSERT INTO user_likes_flow (user_id, flow_id) VALUES(1, 2);

CREATE TABLE comment(
    id int auto_increment PRIMARY KEY,
    user_id int,
    flow_id int,
    created_at timestamp default CURRENT_TIMESTAMP,
    text VARCHAR(320),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (flow_id) REFERENCES flow(id)
);

INSERT INTO comment (user_id, flow_id, text)  VALUES (1, 1, 'Hi here test comment');
INSERT INTO comment (user_id, flow_id, text)  VALUES (2, 1, 'Hi here test comment 2');
INSERT INTO comment (user_id, flow_id, text)  VALUES (3, 1, 'Hi here I am');
INSERT INTO comment (user_id, flow_id, text)  VALUES (2, 1, 'thats crazy');
INSERT INTO comment (user_id, flow_id, text)  VALUES (3, 1, 'Look at me!');
INSERT INTO comment (user_id, flow_id, text)  VALUES (1, 1, 'Hi here I am 2');
INSERT INTO comment (user_id, flow_id, text)  VALUES (2, 1, 'thats crazy 2');
INSERT INTO comment (user_id, flow_id, text)  VALUES (2, 1, 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit am');
