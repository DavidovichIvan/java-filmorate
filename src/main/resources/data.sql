--INSERT INTO "User" (name, email, login, birthday) VALUES ('TestUser', 'TT@mail.my', 'TTTT', '1998-12-12');
--БД заполнена, можно раскоментить если потребуется перезаполнить пустую БД; только в этом случае чтобы автозаполение прошло нужно индексы таблиц обнулить = проще всего в бобре удалить просто все таблицы; тогда тут при запуске они заново создадутся из схемы а тут из даты заполнятся
--ALTER TABLE USERS_DB alter column user_id set increment by 1; --эта команда изменяет сам шаг инкремента но не значение


--некоторые данные тесты не добавляют но используют

--раскоментить при отправке на ПР, все таблицы базы удалить и второй блок закоментить

INSERT INTO Rating (rating_name) VALUES ('G');
INSERT INTO Rating (rating_name) VALUES ('PG');
INSERT INTO Rating (rating_name) VALUES ('PG-13');
INSERT INTO Rating (rating_name) VALUES ('R');
INSERT INTO Rating (rating_name) VALUES ('NC-17');

INSERT INTO Genre (genre_id, genre_name) VALUES (1,'Комедия');
INSERT INTO Genre (genre_id, genre_name) VALUES (2,'Драма');
INSERT INTO Genre (genre_id, genre_name) VALUES (3,'Мультфильм');
INSERT INTO Genre (genre_id, genre_name) VALUES (4,'Триллер');
INSERT INTO Genre (genre_id, genre_name) VALUES (5,'Документальный');
INSERT INTO Genre (genre_id, genre_name) VALUES (6, 'Боевик');


/*
INSERT INTO Users_DB (name, email, login, birthday) VALUES ('Tom', 'Thomas@mail.my', 'Tommy','1978-10-10');
INSERT INTO Users_DB (name, email, login, birthday) VALUES ('Ben', 'Ben@mail.my', 'Bennn', '1998-12-12');
INSERT INTO Users_DB (name, email, login, birthday) VALUES ('Pam', 'Pp@mail.my', 'Pammacota', '1977-06-18');

INSERT INTO Friends (user_id, friend_id) VALUES (1, 2);
INSERT INTO Friends (user_id, friend_id) VALUES (1, 3);
INSERT INTO Friends (user_id, friend_id) VALUES (2, 3);

INSERT INTO Rating (rating_name) VALUES ('G');
INSERT INTO Rating (rating_name) VALUES ('PG');
INSERT INTO Rating (rating_name) VALUES ('PG-13');
INSERT INTO Rating (rating_name) VALUES ('R');
INSERT INTO Rating (rating_name) VALUES ('NC-17');

INSERT INTO Genre (genre_id, genre_name) VALUES (1,'Комедия');
INSERT INTO Genre (genre_id, genre_name) VALUES (2,'Драма');
INSERT INTO Genre (genre_id, genre_name) VALUES (3,'Мультфильм');
INSERT INTO Genre (genre_id, genre_name) VALUES (4,'Триллер');
INSERT INTO Genre (genre_id, genre_name) VALUES (5,'Документальный');
INSERT INTO Genre (genre_id, genre_name) VALUES (6, 'Боевик');

INSERT INTO Films (title, description, release_date, duration, rating_id) VALUES ('Terminator','Robot came from future','1984-12-12',90,1);
INSERT INTO Films (title, description, release_date, duration, rating_id) VALUES ('Race','Whzzzzzz','1990-11-29',86,2);
INSERT INTO Films (title, description, release_date, duration, rating_id) VALUES ('Lady','Love story','1991-05-14',122,3);


INSERT INTO Likes(film_id, user_id) VALUES(1,1);
INSERT INTO Likes(film_id, user_id) VALUES(2,1);
INSERT INTO Likes(film_id, user_id) VALUES(2,2);

INSERT INTO Film_genre (film_id, genre_id) VALUES(1,1);
INSERT INTO Film_genre (film_id, genre_id) VALUES(1,2);
INSERT INTO Film_genre (film_id, genre_id) VALUES(2,2);
INSERT INTO Film_genre (film_id, genre_id) VALUES(2,3);
*/
