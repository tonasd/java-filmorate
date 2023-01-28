package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString
public class Film extends Item {
    final private String description;
    final private LocalDate releaseDate;
    final private Duration duration;
}
