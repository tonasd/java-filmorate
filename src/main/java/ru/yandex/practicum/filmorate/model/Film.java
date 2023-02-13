package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Film extends Item {
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
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
