package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@ToString
@EqualsAndHashCode
public class Film {
    @Getter
    @Setter
    private static int filmIdCounter = 1;
    @Getter
    private final int id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private LocalDate releaseDate;
    @Getter
    @Setter
    private int duration;

    @Getter
    @Setter
    private Set<Integer> likes;

    public Film(String name, String description, LocalDate releaseDate, int durationInMinutes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = durationInMinutes;
        this.likes = new HashSet<>();

        this.id = filmIdCounter;
        filmIdCounter++;
    }

}
