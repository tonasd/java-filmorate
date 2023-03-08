package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
public class Film extends Item {
    @NotNull
    @NotEmpty(message = "Film's name cannot be empty")
    private
    String name;
    @NotNull @Size(max = 200)
    private
    String description;
    @NotNull
    private
    LocalDate releaseDate;
    @Positive
    private
    int duration;
    private List<Genre> genres;
    private Rating mpa; // MPA rating

    public Film() {
        this.genres = new ArrayList<>();
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
