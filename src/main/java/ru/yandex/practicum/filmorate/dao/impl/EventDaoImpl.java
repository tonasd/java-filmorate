package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.EventDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.EventOperation;
import ru.yandex.practicum.filmorate.model.EventType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EventDaoImpl implements EventDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getUserEvents(long id) {
        return jdbcTemplate.query("SELECT EVENT_ID, TIME_FEED, " +
                        "EVENT_TYPE, OPERATION, USER_ID, ENTITY_ID " +
                        "FROM FEED " +
                        "WHERE USER_ID = ? ",
                this::mapRowToEvent, id);
    }

    @Override
    public void addEvent(Event event) {
        String sql = "INSERT INTO FEED (TIME_FEED,EVENT_TYPE,OPERATION,USER_ID,ENTITY_ID) " +
                "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, Timestamp.from(Instant.now()), event.getEventType().toString(),
                event.getOperation().toString(), event.getUserId(), event.getEntityId());
    }

    private Event mapRowToEvent(ResultSet rs, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(rs.getLong("EVENT_ID"))
                .timestamp(rs.getTimestamp("TIME_FEED").toInstant().toEpochMilli())
                .eventType(EventType.valueOf(rs.getString("EVENT_TYPE")))
                .operation(EventOperation.valueOf(rs.getString("OPERATION")))
                .userId(rs.getLong("USER_ID"))
                .entityId(rs.getLong("ENTITY_ID"))
                .build();
    }
}