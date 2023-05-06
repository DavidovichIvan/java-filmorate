package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.films.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

        if (!filmStorage.getFilmsList().containsKey(requestId)) {
            throw new IdNotExistException();
        } else return filmStorage.getFilmsList().get(requestId);
    }

    public void putLike(String filmId, String userId) {
        int filmToPutLikeId = VariablesValidation.checkRequestId(filmId);
        int userWhoPutLikeId = VariablesValidation.checkRequestId(userId);

        checkIfFilmExists(filmToPutLikeId);
        userService.checkIfUserExists(userWhoPutLikeId);

        filmStorage.
                getFilmsList().
                get(filmToPutLikeId).
                getLikes().
                add(userWhoPutLikeId);
    }

    public void deleteLike(String filmId, String userId) {
        int filmToDeleteLikeId = VariablesValidation.checkRequestId(filmId);
        int userWhoDeleteLikeId = VariablesValidation.checkRequestId(userId);

        checkIfFilmExists(filmToDeleteLikeId);
        userService.checkIfUserExists(userWhoDeleteLikeId);

        filmStorage.
                getFilmsList().
                get(filmToDeleteLikeId).
                getLikes().
                remove(userWhoDeleteLikeId);
    }

    public List<Film> getPopularFilms(String count) {
        int numberOfFilms = VariablesValidation.checkRequestId(count);

        List<Film> popularFilms = new ArrayList<>(filmStorage.getAllFilms());
        popularFilms.sort(Comparator.comparingInt(a -> a.getLikes().size()));
        Collections.reverse(popularFilms);

        while (popularFilms.size() > numberOfFilms) {
            popularFilms.remove(popularFilms.size() - 1);
        }
        return popularFilms;
    }

    private void checkIfFilmExists(Integer filmId) {
        if (!filmStorage.getFilmsList().containsKey(filmId)) {
            throw new IdNotExistException();
        }
    }
}