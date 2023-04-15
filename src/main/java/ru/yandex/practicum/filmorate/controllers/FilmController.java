package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
    LocalDate FIRST_FILM_RELEASE_DATE = LocalDate.of(1985, 12, 28);

    @Getter
    private final int FILM_DESCRIPTION_MAX_LENGTH = 200;
    @Getter
    private Map<Integer, Film> filmsList = new HashMap<>(); //id = key

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (filmsList.containsKey(film.getId())) {
            log.info("Запрос на добавление; фильм с id: {} уже существует", film.getId());
            throw new IdAlreadyExistsException(film);
        }
        filmDataValidate(film);

        filmsList.put(film.getId(), film);
        log.info("Запрос на добавление; сохранен фильм: {} ", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (!filmsList.containsKey(film.getId())) {
            log.info("Запрос на обновление; не существует фильма с id: {} ", film.getId());
            throw new IdNotExistException(film);
        }
        filmDataValidate(film);

        filmsList.put(film.getId(), film);
        log.info("Запрос на обновление; обновлен фильм: {} ", film);
        return film;
    }

    @GetMapping
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
        if (film.getDescription().length() > FILM_DESCRIPTION_MAX_LENGTH) {
            String correctedDescription = film.getDescription().substring(0, FILM_DESCRIPTION_MAX_LENGTH);
            log.info("Описание фильма сокращено до {} символов", FILM_DESCRIPTION_MAX_LENGTH);
            film.setDescription(correctedDescription);
        }

        try {
            filmDuration = Integer.parseInt(film.getDuration());
        } catch (NumberFormatException nfe) {
            throw new InvalidFilmDurationException();
        }

        if (filmDuration <= 0) {
            log.info("Некорректно введена продолжительность фильма в минутах {} ", film.getDuration());
            throw new InvalidFilmDurationException();
        }

        if (film.getReleaseDate().isBefore(FIRST_FILM_RELEASE_DATE)
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.info(String.valueOf(FIRST_FILM_RELEASE_DATE));
            log.info("Некорректно введена дата релиза фильма {} ", film.getReleaseDate());
            throw new InvalidFilmReleaseDateException();
        }

    }
}
