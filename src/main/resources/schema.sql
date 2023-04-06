CREATE TABLE IF NOT EXISTS PUBLIC.AGE_RESTRICTION_RATINGS (
	RATING_ID INTEGER NOT NULL AUTO_INCREMENT,
	RATING_BLOCK CHARACTER VARYING(32) NOT NULL,
	RATING_DESCRIPTION CHARACTER VARYING(255),
	CONSTRAINT AGE_RESTRICTION_RATINGS_PK PRIMARY KEY (RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILMS (
	FILM_ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(255) NOT NULL,
	DESCRIPTION CHARACTER VARYING(200) NOT NULL,
	RELEASE_DATE DATE NOT NULL,
	DURATION INTEGER NOT NULL, -- keep film duration in minutes
	RATING_ID INTEGER,
	IS_DELETED BOOLEAN DEFAULT false,
	CONSTRAINT FILMS_PK PRIMARY KEY (FILM_ID),
	CONSTRAINT FILMS_FK FOREIGN KEY (RATING_ID) REFERENCES PUBLIC.AGE_RESTRICTION_RATINGS(RATING_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.GENRES (
	GENRE_ID INTEGER NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(32) NOT NULL,
	CONSTRAINT GENRES_PK PRIMARY KEY (GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_GENRE (
	FILM_ID BIGINT NOT NULL,
	GENRE_ID INTEGER NOT NULL,
	CONSTRAINT FILM_GENRE_PK PRIMARY KEY (FILM_ID,GENRE_ID),
	CONSTRAINT FILM_GENRE_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID),
	CONSTRAINT FILM_GENRE_FK_1 FOREIGN KEY (GENRE_ID) REFERENCES PUBLIC.GENRES(GENRE_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
	USER_ID BIGINT NOT NULL AUTO_INCREMENT,
	EMAIL CHARACTER VARYING(256) NOT NULL,
	LOGIN CHARACTER VARYING(255) NOT NULL,
	NAME CHARACTER VARYING(255) NOT NULL,
	BIRTHDAY DATE NOT NULL,
	IS_DELETED BOOLEAN DEFAULT (false),
	CONSTRAINT USERS_PK PRIMARY KEY (USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FRIENDS (
	USER_ID_REQUEST_FROM BIGINT NOT NULL,
	USER_ID_REQUEST_TO BIGINT NOT NULL,
	APPROVED BOOLEAN DEFAULT false NOT NULL,
	CONSTRAINT FRIENDS_PK PRIMARY KEY (USER_ID_REQUEST_FROM,USER_ID_REQUEST_TO),
	CONSTRAINT FRIENDS_FK FOREIGN KEY (USER_ID_REQUEST_FROM) REFERENCES PUBLIC.USERS(USER_ID),
	CONSTRAINT FRIENDS_FK_1 FOREIGN KEY (USER_ID_REQUEST_TO) REFERENCES PUBLIC.USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FAVORITE_FILMS (
	USER_ID BIGINT NOT NULL,
	FILM_ID BIGINT NOT NULL,
	CONSTRAINT FAVORITE_FILMS_PK PRIMARY KEY (USER_ID,FILM_ID),
	CONSTRAINT FAVORITE_FILMS_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID),
	CONSTRAINT FAVORITE_FILMS_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.DIRECTORS (
	DIRECTOR_ID BIGINT NOT NULL AUTO_INCREMENT,
	NAME CHARACTER VARYING(255) NOT NULL,
	IS_DELETED BOOLEAN DEFAULT false,
	CONSTRAINT DIRECTORS_PK PRIMARY KEY (DIRECTOR_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.FILM_DIRECTOR (
	FILM_ID BIGINT NOT NULL,
	DIRECTOR_ID BIGINT NOT NULL,
	CONSTRAINT FILM_DIRECTOR_PK PRIMARY KEY (DIRECTOR_ID, FILM_ID),
	CONSTRAINT FILM_DIRECTOR_FK FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID),
	CONSTRAINT FILM_DIRECTOR_FK_1 FOREIGN KEY (DIRECTOR_ID) REFERENCES PUBLIC.DIRECTORS(DIRECTOR_ID)
);

CREATE TABLE IF NOT EXISTS PUBLIC.REVIEWS
(
    REVIEW_ID BIGINT NOT NULL AUTO_INCREMENT,
    USEFUL INT,
    IS_POSITIVE BOOLEAN,
    CONTENT VARCHAR(2000),
    USER_ID BIGINT,
    FILM_ID BIGINT,
    IS_DELETED BOOLEAN DEFAULT false,
    CONSTRAINT REVIEWS_PK PRIMARY KEY (REVIEW_ID),
    CONSTRAINT REVIEWS_FK FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID),
    CONSTRAINT REVIEWS_FK_1 FOREIGN KEY (FILM_ID) REFERENCES PUBLIC.FILMS(FILM_ID)
    );

CREATE TABLE IF NOT EXISTS PUBLIC.REVIEWS_USERS
(
    REVIEW_ID BIGINT,
    USER_ID BIGINT,
    IS_USEFUL BOOLEAN,
    CONSTRAINT REVIEWS_USERS_PK PRIMARY KEY (REVIEW_ID),
    CONSTRAINT REVIEWS_USERS_FK FOREIGN KEY (REVIEW_ID) REFERENCES PUBLIC.REVIEWS(REVIEW_ID),
    CONSTRAINT REVIEWS_USERS_FK_1 FOREIGN KEY (USER_ID) REFERENCES PUBLIC.USERS(USER_ID)
);


CREATE TABLE IF NOT EXISTS PUBLIC.FEED (
	EVENT_ID      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	TIME_FEED     TIMESTAMP,
	EVENT_TYPE 	  VARCHAR(16) NOT NULL,
	OPERATION     VARCHAR(16) NOT NULL,
	USER_ID       BIGINT REFERENCES PUBLIC.USERS (USER_ID),
	ENTITY_ID     BIGINT NOT NULL
);