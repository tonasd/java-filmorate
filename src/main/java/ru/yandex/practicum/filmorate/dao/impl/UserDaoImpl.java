package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.ItemNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User findUserById(long id) {
        final String sql = "SELECT * " +
                "FROM users " +
                "WHERE user_id = ? AND IS_DELETED IS false";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("User with id %d not found", id));
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        final String sql = "SELECT * " +
                "FROM users " +
                "WHERE NOT IS_DELETED";
        return jdbcTemplate.query(sql, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id")
                .usingColumns("email", "login", "name", "birthday");
        long id = simpleJdbcInsert.executeAndReturnKey(this.toMap(user)).longValue();
        return findUserById(id);
    }

    @Override
    public void updateUser(User user) {
        final String sql = "UPDATE users " +
                "SET email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(
                sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
    }

    @Override
    public void deleteUserById(long userId) {
        final String sql = "UPDATE users SET IS_DELETED = true " +
                "WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ItemNotFoundException(String.format("User with id %d not found", userId));
        }
    }

    private Map<String, Object> toMap(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        if (user.getId() != 0) {
            values.put("user_id", user.getId());
        }
        return values;
    }

    private User mapRowToUser(ResultSet rs, int rowNumber) throws SQLException {
        long id = rs.getLong("user_id");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();
        return new User(id, email, login, name, birthday);
    }
}