package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@ToString
@EqualsAndHashCode
public class Film {
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
    private String duration;

    public Film(String name, String description, LocalDate releaseDate, String durationInMinutes) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = durationInMinutes;

        this.id = filmIdCounter;
        filmIdCounter++;
    }
}
