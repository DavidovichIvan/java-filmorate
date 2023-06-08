/*

package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film getFilm(String id) {
        int requestId = VariablesValidation.checkRequestId(id);
        return filmStorage.getFilm(requestId);
    }

    public void putLike(String filmId, String userId) {
        int filmToPutLikeId = VariablesValidation.checkRequestId(filmId);
        int userWhoPutLikeId = VariablesValidation.checkRequestId(userId);

        checkIfFilmExists(filmToPutLikeId);
        userService.checkIfUserExists(userWhoPutLikeId);

        filmStorage.putLike(filmToPutLikeId, userWhoPutLikeId);
    }

    public void deleteLike(String filmId, String userId) {
        int filmToDeleteLikeId = VariablesValidation.checkRequestId(filmId);
        int userWhoDeleteLikeId = VariablesValidation.checkRequestId(userId);

        checkIfFilmExists(filmToDeleteLikeId);
        userService.checkIfUserExists(userWhoDeleteLikeId);

        filmStorage.deleteLike(filmToDeleteLikeId, userWhoDeleteLikeId);
    }

    public List<Film> getPopularFilms(String count) {
        int numberOfFilms = VariablesValidation.checkRequestId(count);
        return filmStorage.getPopularFilms(numberOfFilms);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    private void checkIfFilmExists(Integer filmId) {
        if (!filmStorage.getFilmsList().containsKey(filmId)) {
            throw new IdNotExistException();
        }
    }
}

 */