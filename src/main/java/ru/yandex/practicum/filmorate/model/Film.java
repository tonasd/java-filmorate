package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Film {
    final private int id;
    final private String name;
    final private String description;
    final private LocalDate releaseDate;
    final private Duration duration;
}
