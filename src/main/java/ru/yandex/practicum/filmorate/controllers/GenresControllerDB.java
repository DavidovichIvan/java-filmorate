package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmServiceDB;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/genres")
public class GenresControllerDB {

    private final FilmServiceDB filmService;

    @Autowired
    public GenresControllerDB(FilmServiceDB filmService) {
        this.filmService = filmService;
    }

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
