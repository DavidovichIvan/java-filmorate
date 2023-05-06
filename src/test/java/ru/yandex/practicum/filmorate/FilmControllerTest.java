package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDurationException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.films.InMemoryFilmStorage;

import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTest {

    InMemoryFilmStorage fController = new InMemoryFilmStorage();

    Film testFilm = new Film("Terminator",
            "A human-like cyborg came from the future to nowadays to kill Sarah Connor",
            LocalDate.of(2020, 12, 12),
            120);
    int testId = testFilm.getId();

    @Test
    public void allInputIsCorrectPositiveTest() {
        assertTrue(fController
                .getAllFilms()
                .isEmpty());

        fController.addFilm(testFilm);
        assertEquals(fController.getAllFilms().get(0),
                testFilm);
    }

    @org.junit.Test(expected = InvalidFilmNameException.class)
    public void filmNameCouldNotBeEmptyTest() {
        testFilm.setName("");
        fController.addFilm(testFilm);
    }

    @org.junit.Test(expected = InvalidDescriptionException.class)
    public void descriptionShouldNotExceedMaxLength() {
        int maxLengthTest = fController.getDescriptionMaxLength();
        byte[] array = new byte[maxLengthTest + 1];
        String veryLongDescription = new String(array, Charset.forName("UTF-8"));
        testFilm.setDescription(veryLongDescription);
        fController.addFilm(testFilm);
    }

    @org.junit.Test(expected = InvalidFilmReleaseDateException.class)
    public void releaseDateIsNotEarlierThanTest() {

        LocalDate theMostEarlyDate = fController.getEarliestReleaseDate();
        LocalDate testDate = theMostEarlyDate.minusDays(1);
        testFilm.setReleaseDate(testDate);
        fController.addFilm(testFilm);
    }

    @Test
    public void properReleaseDateTest() {

        LocalDate theMostEarlyDate = fController.getEarliestReleaseDate();
        LocalDate testDate = theMostEarlyDate;
        testFilm.setReleaseDate(testDate);

        fController.addFilm(testFilm);
        assertEquals(testFilm, fController.getAllFilms().get(0));
    }

    @org.junit.Test(expected = InvalidFilmDurationException.class)
    public void inappropriateDurationFormatTest2() {
        //String negativeDuration = "-1";
        int negativeDuration = -1;
        testFilm.setDuration(negativeDuration);

        fController.addFilm(testFilm);
    }

}