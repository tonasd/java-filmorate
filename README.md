# java-filmorate

![ER-diagramm](https://github.com/tonasd/java-filmorate/blob/DB-creation/Filmorate%20ER-diagram.jpg)

Requets exmples for DB

1. Get all users
```
SELECT *
FROM user;
```

2. Get all films
```
SELECT *
FROM film;
```

3. Top n films
```
SELECT *
FROM film
WHERE id IN (SELECT film_id
            FROM likes
            GROUP BY film_id
            ORDER BY COUNT(user_id) DESC
            LIMIT n);
```

4. Get common friends id_1 and id_2
```
WHITH friends_user_id_1 AS (SELECT user_id_request_from AS id
            FROM friends
            WHERE user_id_request_to = id_1
            UNION
            SELECT user_id_request_to AS id
            FROM friends
            WHERE user_id_request_from = id_1),
       friends_user_id_2 AS (SELECT user_id_request_from AS id
            FROM friends
            WHERE user_id_request_to = id_2
            UNION
            SELECT user_id_request_to AS id
            FROM friends
            WHERE user_id_request_from = id_2) 
                        

SELECT *
FROM user
WHERE id IN (SELECT * 
             FROM friends_user_id_1
             INNER JOIN frinds_user_id_2 ON friends_user_id_1.id = friends_user_id_2.id);
```
                    


