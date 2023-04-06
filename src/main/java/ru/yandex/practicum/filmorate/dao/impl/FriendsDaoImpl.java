package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFriend(long userIdRequestFrom, long friendIdRequestTo) {
        userExists(userIdRequestFrom);
        userExists(friendIdRequestTo);
        // check if it is a new friendship or approval
        final String sqlCheckApproved = "SELECT approved " +
                "FROM friends " +
                "WHERE user_id_request_from = ? AND user_id_request_to = ?";
        Boolean approved;
        try {
            approved = jdbcTemplate.queryForObject(sqlCheckApproved, Boolean.class, friendIdRequestTo, userIdRequestFrom);
        } catch (EmptyResultDataAccessException e) {
            // it means that it is not approval. Needed to be created new friendship
            approved = null;
        }
        String sql;
        if (approved == null) {
            sql = "INSERT INTO friends (user_id_request_from,user_id_request_to) " +
                    "VALUES (?,?)";
        } else {
            sql = "UPDATE friends " +
                    "SET APPROVED=true " +
                    "WHERE user_id_request_to = ? AND user_id_request_from = ?";
        }
        jdbcTemplate.update(sql, userIdRequestFrom, friendIdRequestTo);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        userExists(userId);
        userExists(friendId);
        final String sqlCheckApproved = "SELECT approved " +
                "FROM friends " +
                "WHERE user_id_request_from = ? AND user_id_request_to = ?";
        Boolean approved;
        try {
            approved = jdbcTemplate.queryForObject(sqlCheckApproved, Boolean.class, userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            // it means that it is not such record
            approved = null;
        }
        String sql;
        if (approved == null) { // here we need to set approved to false
            sql = "UPDATE friends " +
                    "SET APPROVED = false " +
                    "WHERE user_id_request_to = ? AND user_id_request_from = ?";
        } else if (!approved) { // here we need to delete line
            sql = "DELETE FROM friends " +
                    "WHERE user_id_request_from = ? AND user_id_request_to = ?";
        } else { // here we need to create new line with contra versa to and from with approved = false (default in DB)
            jdbcTemplate.update("INSERT INTO friends (user_id_request_to, user_id_request_from) VALUES (?,?)",
                    friendId, userId);
            // and then delete original line
            sql = "DELETE FROM friends " +
                    "WHERE user_id_request_from = ? AND user_id_request_to = ?";
        }
        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<Long> getFriendsIds(long userId) {
        userExists(userId);
        final String sql = "SELECT user_id_request_to FROM FRIENDS f " +
                "JOIN USERS u ON f.USER_ID_REQUEST_TO = u.USER_ID " +
                "WHERE f.user_id_request_from = 1 AND NOT u.IS_DELETED " +
                "UNION " +
                "SELECT user_id_request_from FROM FRIENDS f " +
                "JOIN USERS u ON f.USER_ID_REQUEST_FROM = u.USER_ID " +
                "WHERE f.user_id_request_to = ? AND NOT u.is_deleted AND approved IS TRUE";
        return jdbcTemplate.queryForList(sql, Long.class, userId, userId);
    }

    private void userExists(long id) {
        final String sql = "SELECT user_id " +
                "FROM users " +
                "WHERE user_id = ? AND NOT IS_DELETED";
        try {
            jdbcTemplate.queryForObject(sql, Long.class, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("User with id %d not found", id));
        }
    }
}