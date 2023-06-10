package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Film {

    private int id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max = 200, message = "Максимальная длина описания 200 символов")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность не может быть отрицательной")
    private int duration;

    private final MPA mpa;

    private Set<Genre> genres;

    private Set<Integer> likes;

    int rate;

    public Film(String name, String description, LocalDate releaseDate, int durationInMinutes, MPA mpa) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = durationInMinutes;
        this.mpa = mpa;
        this.genres = new TreeSet<>(Comparator.comparingInt(Genre::getId));
        this.likes = new HashSet<>();
    }
}