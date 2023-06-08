/*
package ru.yandex.practicum.filmorate.storage.films;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    @Getter
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    @Getter
    private final int descriptionMaxLength = 200;

    @Getter
    @Setter
    private Map<Integer, Film> filmsList = new HashMap<>();

    @Override
    public Film addFilm(Film film) {
        if (filmsList.containsKey(film.getId())) {
            log.info("Запрос на добавление; фильм с id: {} уже существует", film.getId());
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new IdAlreadyExistsException();
        }
        filmDataValidate(film);
        filmsList.put(film.getId(), film);
        correctIdFilmCounter(film);
        log.info("Запрос на добавление; сохранен фильм: {} ", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!filmsList.containsKey(film.getId())) {
            log.info("Запрос на обновление; не существует фильма с id: {} ", film.getId());
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new IdNotExistException();
        }
        filmDataValidate(film);
        filmsList.put(film.getId(), film);
        correctIdFilmCounter(film);
        log.info("Запрос на обновление; обновлен фильм: {} ", film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Запрос на все фильмы, всего фильмов в картотеке: {}", filmsList.size());
        List<Film> films = new ArrayList<>(filmsList.values());
        return films;
    }

    private void filmDataValidate(Film film) {
        int filmDuration;

        if (!StringUtils.hasText(film.getName())) {
            log.info("Не введено название фильма {} ", film.getName());
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new InvalidFilmNameException();
        }

        if (StringUtils.hasText(film.getDescription())
                && film.getDescription().length() > descriptionMaxLength) {
            log.info("Описание фильма превышает предельную длину");
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new InvalidDescriptionException(descriptionMaxLength);
        }

        try {
            filmDuration = film.getDuration();
        } catch (NumberFormatException nfe) {
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new InvalidFilmDurationException();
        }

        if (filmDuration <= 0) {
            log.info("Некорректно введена продолжительность фильма в минутах {} ", film.getDuration());
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new InvalidFilmDurationException();
        }

        if (film.getReleaseDate().isBefore(earliestReleaseDate)
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.info(String.valueOf(earliestReleaseDate));
            log.info("Некорректно введена дата релиза фильма {} ", film.getReleaseDate());
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
            throw new InvalidFilmReleaseDateException();
        }
    }

    @Override
    public Film getFilm(Integer id) {
        if (!filmsList.containsKey(id)) {
            throw new IdNotExistException();
        }
        return getFilmsList().get(id);
    }

    @Override
    public List<Film> getPopularFilms(Integer numberOfFilms) {
        List<Film> popularFilms = new ArrayList<>(getAllFilms());
        popularFilms.sort(Comparator.comparingInt(a -> a.getLikes().size()));
        Collections.reverse(popularFilms);

        while (popularFilms.size() > numberOfFilms) {
            popularFilms.remove(popularFilms.size() - 1);
        }
        return popularFilms;
    }

    @Override
    public void putLike(Integer film, Integer user) {
        filmsList.get(film)
                .getLikes()
                .add(user);
    }

    public void deleteLike(Integer film, Integer user) {
        filmsList.get(film)
                .getLikes()
                .remove(user);
    }

    private void correctIdFilmCounter(Film film) {
        if (film.getId() != Film.getFilmIdCounter() - 1) {
            Film.setFilmIdCounter(Film.getFilmIdCounter() - 1);
        }

        while (filmsList.containsKey(Film.getFilmIdCounter())) {
            Film.setFilmIdCounter(Film.getFilmIdCounter() + 1);
        }
    }
}

 */