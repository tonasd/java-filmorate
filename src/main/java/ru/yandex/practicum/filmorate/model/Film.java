package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class Film extends Item {
    @NotNull
    @NotEmpty(message = "Film's name cannot be empty")
    String name;
    @NotNull @Size(max = 200)
    String description;
    @NotNull
    LocalDate releaseDate;
    @Positive
    int duration;
    Set<Genre> genres;
    Set<Director> directors;
    Rating mpa; // MPA rating

    public Film() {
        this.genres = new LinkedHashSet<>();
        this.directors = new HashSet<>();
    }


    public Film(@NotNull @NotEmpty(message = "Film's name cannot be empty") String name, @NotNull @Size(max = 200) String description, @NotNull LocalDate releaseDate, @Positive int duration, Rating mpa) {
        this();
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }
}