package ru.yandex.practicum.filmorate.model;


import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    long eventId;
    long timestamp;
    EventType eventType;
    EventOperation operation;
    long userId;
    long entityId;
}