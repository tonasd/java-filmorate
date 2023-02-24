# java-filmorate

![ER-diagramm](https://github.com/tonasd/java-filmorate/blob/DB-creation/Filmorate%20ER-diagram.jpg)

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
SELECT film_id,
       name,
       description,
       rlease_date,
       duration,
       ratings.rating_block
FROM films
LEFT JOIN likes ON films.film_id = likes.film_id
LEFT JOIN ratings ON films.rating_id = ratings.rating_id;
GROUP BY films.film_id
ORDER BY films.film_id DESC
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
                    
