/*
Спасибо за ревью.
 */

package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Getter
    LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    @Getter
    private final int descriptionMaxLength = 200;
    @Getter
    private Map<Integer, Film> filmsList = new HashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film addFilm(@RequestBody Film film) {
        if (filmsList.containsKey(film.getId())) {
            log.info("Запрос на добавление; фильм с id: {} уже существует", film.getId());
            throw new IdAlreadyExistsException();
        }
        filmDataValidate(film);

        filmsList.put(film.getId(), film);
        log.info("Запрос на добавление; сохранен фильм: {} ", film);
        return film;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film film) {
        if (!filmsList.containsKey(film.getId())) {
            log.info("Запрос на обновление; не существует фильма с id: {} ", film.getId());
            throw new IdNotExistException();
        }
        filmDataValidate(film);

        filmsList.put(film.getId(), film);
        log.info("Запрос на обновление; обновлен фильм: {} ", film);
        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        log.info("Запрос на все фильмы, всего фильмов в картотеке: {}", filmsList.size());
        List<Film> films = new ArrayList<>(filmsList.values());
        return films;
    }

    private void filmDataValidate(Film film) {
        int filmDuration;

        if (film.getName() == null || film.getName().isBlank()) {
            log.info("Не введено название фильма {} ", film.getName());
            throw new InvalidFilmNameException();
        }

        if (film.getDescription().length() > descriptionMaxLength) {
            log.info("Описание фильма превышает предельную длину");
            throw new InvalidDescriptionException(descriptionMaxLength);
        }

        //  if (film.getDescription().length() > descriptionMaxLength) {
        //      String correctedDescription = film.getDescription().substring(0, descriptionMaxLength);
        //        log.info("Описание фильма сокращено до {} символов", descriptionMaxLength);
        //       film.setDescription(correctedDescription);
        //    }

        try {
            //filmDuration = Integer.parseInt(film.getDuration());
            filmDuration = film.getDuration();
        } catch (NumberFormatException nfe) {
            throw new InvalidFilmDurationException();
        }

        if (filmDuration <= 0) {
            log.info("Некорректно введена продолжительность фильма в минутах {} ", film.getDuration());
            throw new InvalidFilmDurationException();
        }

        if (film.getReleaseDate().isBefore(earliestReleaseDate)
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.info(String.valueOf(earliestReleaseDate));
            log.info("Некорректно введена дата релиза фильма {} ", film.getReleaseDate());
            throw new InvalidFilmReleaseDateException();
        }

    }
}
