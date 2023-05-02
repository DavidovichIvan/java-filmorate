/*
ТЗ-10
 */

package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    @Getter
    private final LocalDate earliestReleaseDate = LocalDate.of(1895, 12, 28);

    @Getter
    private final int descriptionMaxLength = 200;
    @Getter
    private Map<Integer, Film> filmsList = new HashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Film addFilm(@RequestBody Film film) {
        if (filmsList.containsKey(film.getId())) {
            log.info("Запрос на добавление; фильм с id: {} уже существует", film.getId());
            throw new IdAlreadyExistsException();
        }
        filmDataValidate(film);
        filmsList.put(film.getId(), film);
        correctIdFilmCounter();
        log.info("Запрос на добавление; сохранен фильм: {} ", film);
        return film;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@RequestBody Film film) {
        if (!filmsList.containsKey(film.getId())) {
            log.info("Запрос на обновление; не существует фильма с id: {} ", film.getId());
            throw new IdNotExistException();
        }
        filmDataValidate(film);
        filmsList.put(film.getId(), film);
        correctIdFilmCounter();
        log.info("Запрос на обновление; обновлен фильм: {} ", film);
        return film;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        log.info("Запрос на все фильмы, всего фильмов в картотеке: {}", filmsList.size());
        List<Film> films = new ArrayList<>(filmsList.values());
        return films;
    }

    private void filmDataValidate(Film film) {
        int filmDuration;

        if (!StringUtils.hasText(film.getName())) {
            log.info("Не введено название фильма {} ", film.getName());
            throw new InvalidFilmNameException();
        }

        if (StringUtils.hasText(film.getDescription())
                && film.getDescription().length() > descriptionMaxLength) {
            log.info("Описание фильма превышает предельную длину");
            throw new InvalidDescriptionException(descriptionMaxLength);
        }

        try {
            filmDuration = film.getDuration();
        } catch (NumberFormatException nfe) {
            throw new InvalidFilmDurationException();
        }

        if (filmDuration <= 0) {
            log.info("Некорректно введена продолжительность фильма в минутах {} ", film.getDuration());
            throw new InvalidFilmDurationException();
        }

        if (film.getReleaseDate().isBefore(earliestReleaseDate)
                || film.getReleaseDate().isAfter(LocalDate.now())) {
            log.info(String.valueOf(earliestReleaseDate));
            log.info("Некорректно введена дата релиза фильма {} ", film.getReleaseDate());
            throw new InvalidFilmReleaseDateException();
        }
    }
    private void correctIdFilmCounter() {  //если вдруг добавили фильм id которого передали в запросе,
        // тогда не исключено, что счетчик id стал (или остался) с id который уже существует и записан в мапу;
        // но он (счетчик) должен быть все время на единицу больше (не должен быть равен значениям, которые в мапе уже есть)
        // иначе при попытке создания нового объекта будет неустраняемый баг (будет говорить что такой объект уже существует)
        //чтобы это учесть мы каждый раз после добавления или обновления фильма проверяем счетчик
        // и если он равен какомуто существующему объекту, то увеличиваем его.
        //В этом методе я не стал писать как в таком же для UserController первую часть с понижением счетчика
        // в тех случаях когда новый объект не удалось создать, так как это нужно было чтобы автотест пройти
            while (filmsList.containsKey(Film.getFilmIdCounter())) {
            Film.setFilmIdCounter(Film.getFilmIdCounter() + 1);
        }
    }
}
//не забыть влить ветку в мейн