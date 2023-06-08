package ru.yandex.practicum.filmorate.storage.films;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Optional;

public interface FilmStorageDB {
    Optional<Film> getFilm(Integer id);

    List<Genre> getAllGenres();

   Optional<Genre> getGenre(Integer id);

   List<MPA> getAllMPA();
   Optional<MPA> getMPA(Integer id);

    List<Film> getAllFilms();

    Film addFilm(Film film);

    boolean deleteFilm(Integer filmId);

    Film updateFilm(Film film);

    void putLike(Integer film, Integer user);

    void deleteLike(Integer film, Integer user);
    List<Film> getPopularFilms(Integer numberOfFilms);
}
