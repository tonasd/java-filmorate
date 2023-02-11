package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@ToString(callSuper = true)
public class Film extends Item {
    @NonNull
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @ToString.Exclude
    private Set<Long> usersLiked;

    public Film(@NonNull String name, String description, LocalDate releaseDate, int duration) {
        this(name, description, releaseDate, duration, new HashSet<>());
    }
    public Film(Film film) {
        this(film.name, film.description, film.releaseDate, film.duration, film.usersLiked);
        this.id = film.id;
    }

    @ToString.Include
    private String usersLiked() {
        return String.format("Was liked %d times", usersLiked.size());
    }

    @ToString.Include(name = "Times liked")
    public int timesLiked() { return usersLiked.size(); }
}
