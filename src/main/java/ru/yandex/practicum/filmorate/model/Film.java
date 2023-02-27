package ru.yandex.practicum.filmorate.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
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
    @ToString.Exclude
    transient Set<Long> usersLiked = new HashSet<>();
    String genre;
    String rating; // MPA rating

    public Film(Film film) {
        this(film.name,
                film.description,
                film.releaseDate,
                film.duration,
                new HashSet<>(film.usersLiked),
                film.genre,
                film.rating);
        this.id = film.id;
    }

    @ToString.Include(name = "Times liked")
    public int timesLiked() { return usersLiked.size(); }
}
