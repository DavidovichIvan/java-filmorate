package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.DataBaseNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmServiceDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmControllerDB {

    private final FilmServiceDB filmService;

    private final String mostPopularFilmsNumber = "10";

    @Autowired
    public FilmControllerDB(FilmServiceDB filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Film> getFilm(@PathVariable("id") String id) {
        Optional<Film> film = filmService.getFilm(id);
        if (film.isEmpty()) {
            throw new DataBaseNotFoundException();
        }
        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film addFilm(@RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public boolean deleteFilm(@PathVariable("id") String id) {
        return filmService.deleteFilm(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String likeFilm(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        filmService.putLike(id, userId);
        return "Лайк поставлен";
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteLike(@PathVariable("id") String id, @PathVariable("userId") String userId) {
        filmService.deleteLike(id, userId);
        return "Лайк удален";
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = mostPopularFilmsNumber) String count) {
        return filmService.getPopularFilms(count);
    }
}