# java-filmorate

![ER-diagramm](https://github.com/tonasd/java-filmorate/blob/3a7ca63d8c2e59214afd3e855f1e427e7c56bfd0/Filmorate%20ER-diagram.jpg)

Requets exmples for DB

1. Get all users
```
SELECT user_id,
       email,
       login,
       name,
       birthday
FROM users;
```

2. Get all films
```
SELECT film_id,
       name,
       description,
       rlease_date,
       duration,
       ratings.rating_block
FROM films
LEFT JOIN ratings ON films.rating_id = ratings.rating_id;
```

3. Top n films
```
SELECT *
FROM films AS f
LEFT JOIN age_restriction_ratings AS r ON f.rating_id = r.rating_id
LEFT JOIN (SELECT film_id,
              COUNT(user_id) AS likes
           FROM favorite_films
           GROUP BY film_id) AS l ON f.film_id = l.film_id
ORDER BY likes DESC
LIMIT n;
```

4. Get common friends id and other_id
```
SELECT user_id,
       email,
       login,
       name,
       birthday
FROM users
WHERE user_id IN ((SELECT user_id_request_from AS user_id
            FROM friends
            WHERE user_id_request_to = id
            UNION
            SELECT user_id_request_to AS user_id
            FROM friends
            WHERE user_id_request_from = id)
            INTERSECT
            (SELECT user_id_request_from AS user_id
            FROM friends
            WHERE user_id_request_to = other_id
            UNION
            SELECT user_id_request_to AS user_id
            FROM friends
            WHERE user_id_request_from = other_id));
```
5. Add user
```
INSERT INTO users(email, login, name, birthday)
VALUES (*/in the same order/*);
```
