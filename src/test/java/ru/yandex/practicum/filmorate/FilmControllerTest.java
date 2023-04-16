package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exceptions.InvalidDescriptionException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmDurationException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmNameException;
import ru.yandex.practicum.filmorate.exceptions.InvalidFilmReleaseDateException;
import ru.yandex.practicum.filmorate.model.Film;

import java.nio.charset.Charset;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class FilmControllerTest {

    FilmController fController = new FilmController();
    Film testFilm = new Film("Terminator",
            "A human-like cyborg came from the future to nowadays to kill Sarah Connor",
            LocalDate.of(2020, 12, 12),
            120);
    int testId = testFilm.getId();

    @Test
    public void allInputIsCorrectPositiveTest() {
        assertTrue(fController
                .getFilmsList()
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

   /* @Test
    public void descriptionShouldNotExceedMaxLength() {
        int maxLengthTest = fController.getDescriptionMaxLength();
        //left boundary
        byte[] array = new byte[maxLengthTest - 1];
        String veryLongDescription = new String(array, Charset.forName("UTF-8"));
        assertTrue(veryLongDescription.length() == maxLengthTest - 1);

        testFilm.setDescription(veryLongDescription);
        fController.addFilm(testFilm);

        assertEquals(fController.getFilmsList()
                        .get(testId)
                        .getDescription().length(),
                maxLengthTest - 1);

        //right boundary
        fController.getFilmsList().clear();
        array = new byte[maxLengthTest];
        veryLongDescription = new String(array, Charset.forName("UTF-8"));
        assertTrue(veryLongDescription.length() == maxLengthTest);

        testFilm.setDescription(veryLongDescription);
        fController.addFilm(testFilm);

        assertEquals(fController.getFilmsList()
                        .get(testId)
                        .getDescription().length(),
                maxLengthTest);

        //far beyond boundary
        fController.getFilmsList().clear();
        array = new byte[maxLengthTest * 2];
        veryLongDescription = new String(array, Charset.forName("UTF-8"));
        assertTrue(veryLongDescription.length() == maxLengthTest * 2);

        testFilm.setDescription(veryLongDescription);
        fController.addFilm(testFilm);

        assertEquals(fController.getFilmsList()
                        .get(testId)
                        .getDescription().length(),
                maxLengthTest);
    } */

    @org.junit.Test(expected = InvalidFilmReleaseDateException.class)   //left boundary test
    public void releaseDateIsNotEarlierThanTest() {

        LocalDate theMostEarlyDate = fController.getEarliestReleaseDate();

        //left boundary test
        LocalDate testDate = theMostEarlyDate.minusDays(1);
        testFilm.setReleaseDate(testDate);
        fController.addFilm(testFilm);
    }
//test plan

    @Test    //right boundary test
    public void properReleaseDateTest() {

        LocalDate theMostEarlyDate = fController.getEarliestReleaseDate();

        //left boundary test
        LocalDate testDate = theMostEarlyDate;
        testFilm.setReleaseDate(testDate);

        fController.addFilm(testFilm);
        assertEquals(testFilm, fController.getAllFilms().get(0));

    }

    /* @org.junit.Test(expected = InvalidFilmDurationException.class)
    public void inappropriateDurationFormatTest() {
        testFilm.setDuration("test");
        fController.addFilm(testFilm);
    } */

    @org.junit.Test(expected = InvalidFilmDurationException.class)
    public void inappropriateDurationFormatTest2() {
        //String negativeDuration = "-1";
        int negativeDuration = -1;
        testFilm.setDuration(negativeDuration);

        fController.addFilm(testFilm);
    }

}