package ru.yandex.practicum.filmorate.controllers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmServiceDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresControllerDB {

    @NonNull
    private final FilmServiceDB filmService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getAllGenres() {
        return filmService.getAllGenres();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Genre> getGenre(@PathVariable("id") String id) {
        return filmService.getGenre(id);
    }

}
