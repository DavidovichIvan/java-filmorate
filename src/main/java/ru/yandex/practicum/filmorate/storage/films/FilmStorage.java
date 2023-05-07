package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Map<Integer, Film> getFilmsList();

    Film getFilm(Integer id);

    List<Film> getPopularFilms(Integer numberOfFilms);

    void putLike(Integer film, Integer user);

    void deleteLike(Integer film, Integer user);
}
