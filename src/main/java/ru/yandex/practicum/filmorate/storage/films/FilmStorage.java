package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmStorage {

    Film addFilm(Film film);
    Film updateFilm(Film film);
    List<Film> getAllFilms();
    Map<Integer, Film> getFilmsList();

}
