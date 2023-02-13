package ru.yandex.practicum.filmorate.model;

import lombok.*;

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
public class Film extends Item {
    @NotNull
    @NotEmpty(message = "Film's name cannot be empty")
    private String name;
    @NotNull @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private int duration;
    @ToString.Exclude
    private transient Set<Long> usersLiked = new HashSet<>();

    public Film(@NonNull String name, String description, LocalDate releaseDate, int duration) {
        this(name, description, releaseDate, duration, new HashSet<>());
    }
    public Film(Film film) {
        this(film.name, film.description, film.releaseDate, film.duration);
        this.usersLiked = new HashSet<>(film.usersLiked);
        this.id = film.id;
    }

    @ToString.Include(name = "Times liked")
    public int timesLiked() { return usersLiked.size(); }
}
