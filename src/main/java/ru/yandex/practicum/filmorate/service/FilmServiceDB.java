package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Setter
@Service
public class FilmServiceDB {
    private final FilmStorage filmStorage;
    private final UserServiceDB userService;

    @Autowired
    public FilmServiceDB(FilmStorage filmStorage, UserServiceDB userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Optional<Film> getFilm(int requestId) {
        return filmStorage.getFilm(requestId);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Optional<Genre> getGenre(String id) {
        int genreId = Integer.parseInt(id);
        return filmStorage.getGenre(genreId);
    }

    public List<MPA> getAllMPA() {
        return filmStorage.getAllMPA();
    }

    public Optional<MPA> getMPA(int mpaId) {
        return filmStorage.getMPA(mpaId);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public boolean deleteFilm(int filmId) {
        return filmStorage.deleteFilm(filmId);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void putLike(int filmId, int userId) {
        filmStorage.putLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }
}