package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.films.FilmStorageDB;
import ru.yandex.practicum.filmorate.storage.users.UserStorageDB;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//@Sql({"/dataForTests.sql"})
class FilmoRateApplicationTests {
    private final UserStorageDB userStorage;

    private final FilmStorageDB filmStorage;
    private List<User> testListUsers = new ArrayList<>();

    private List<Film> testListFilms = new ArrayList<>();
    private final int numberOfUsersInTestDB = 3;
    private final int numberOfFilmsInTestDB = 3;
    User testUser;

    Film testFilm;

    @Test
    public void testFindUserById() {
        Optional<User> userOptional = userStorage.getUser(1);
        testUser = userOptional.get();
        assertThat(testUser).isNotNull();
        assertThat(testUser.getName().equals("Tom")).isTrue();
    }

    @Test
    public void testGetAllUsers() {
        testListUsers = userStorage.getAllUsers();
        assertThat(testListUsers.size() == numberOfUsersInTestDB).isTrue();
        assertThat(testListUsers.get(0).getEmail().equals("Ben@mail.my")).isTrue();
    }

    @Test
    public void testDeleteUser() {
        testListUsers = userStorage.getAllUsers();
        assertThat(testListUsers.size() == numberOfUsersInTestDB).isTrue();
        userStorage.deleteUser(1);
        testListUsers = userStorage.getAllUsers();
        assertThat(testListUsers.size() == (numberOfUsersInTestDB - 1)).isTrue();
        assertThat(userStorage.deleteUser(2)).isTrue();
    }

    @Test
    public void testAddUser() {
        //  User addUser (User user);
        testListUsers = userStorage.getAllUsers();
        assertThat(testListUsers.size() == numberOfUsersInTestDB).isTrue();

        testUser = new User("Test@mail.my", "TTTT", "Timmy", LocalDate.of(1984, 12, 12));
        userStorage.addUser(testUser);
        testListUsers = userStorage.getAllUsers();
        assertThat(testListUsers.size() == numberOfUsersInTestDB + 1).isTrue();

        User userFromDB = userStorage.getUser(testUser.getId()).get();
        assertThat(userFromDB.getName().equals("Timmy")).isTrue();

        userStorage.deleteUser(testUser.getId());
    }

    @Test
    public void testUpdateUser() {
        testUser = userStorage.getUser(1).get();
        assertThat(testUser.getName().equals("Tom")).isTrue();

        User userForUpd = new User("Upd@mail.my", "Upd", "Updater", LocalDate.of(1984, 12, 12));
        userForUpd.setId(1);

        User oldUser = testUser;
        userStorage.updateUser(userForUpd);

        assertThat(userStorage.getUser(1).get().getName().equals("Tom")).isFalse();
        assertThat(userStorage.getUser(1).get().getEmail().equals("Upd@mail.my")
                && userStorage.getUser(1).get().getName().equals("Updater")).isTrue();

        userStorage.updateUser(oldUser);
    }

    @Test
    public void testGetFriends() {
        List<User> friends = userStorage.getFriends(1);
        int numberOfFriendsInTestDB = 2;
        assertThat(friends.size() == numberOfFriendsInTestDB).isTrue();
        assertThat(friends.get(0).getName().equals("Ben")
                && friends.get(1).getName().equals("Pam")).isTrue();
    }

    @Test
    public void testGetFilmById() {
        Optional<Film> filmOptional = filmStorage.getFilm(1);
        testFilm = filmOptional.get();
        assertThat(testFilm).isNotNull();
        assertThat(testFilm.getName().equals("Terminator")).isTrue();
    }

    @Test
    public void testGetAllFilms() {
        testListFilms = filmStorage.getAllFilms();
        assertThat(testListFilms.size() == numberOfFilmsInTestDB).isTrue();
        assertThat(testListFilms.get(2).getName().equals("Lady")
                && testListFilms.get(2).getDescription().equals("Love story")).isTrue();
    }

    @Test
    public void testDeleteFilm() {
        testListFilms = filmStorage.getAllFilms();
        assertThat(testListFilms.isEmpty()).isFalse();
        assertThat(testListFilms.size() == numberOfFilmsInTestDB).isTrue();

        filmStorage.deleteFilm(3);
        testListFilms = filmStorage.getAllFilms();
        assertThat(testListFilms.size() == (numberOfFilmsInTestDB - 1)).isTrue();
    }

    @Test
    public void testAddFilm() {

        testFilm = new Film("TestFilm", "TestFilm", LocalDate.of(1999, 12, 12), 120, new MPA(1, "Комедия"));
        filmStorage.addFilm(testFilm);
        testListFilms = filmStorage.getAllFilms();

        Film filmFromDB = filmStorage.getFilm(testFilm.getId()).get();
        assertThat(filmFromDB.getName().equals("TestFilm")).isTrue();

        filmStorage.deleteFilm(testFilm.getId());
    }

    @Test
    public void testGetPopularFilms() {
        List<Film> popular = filmStorage.getPopularFilms(2);
        assertThat(popular).isNotNull();
        assertThat(popular.get(0).getLikes().size() > 0).isTrue();
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = filmStorage.getAllGenres();
        int genresAmount = 6;
        assertThat(genres.size() == genresAmount).isTrue();
        assertThat(genres.get(0).getName()).isEqualTo("Комедия");
        assertThat(genres.get(1).getName()).isEqualTo("Драма");
        assertThat(genres.get(2).getName()).isEqualTo("Мультфильм");
        assertThat(genres.get(3).getName()).isEqualTo("Триллер");
        assertThat(genres.get(4).getName()).isEqualTo("Документальный");
        assertThat(genres.get(5).getName()).isEqualTo("Боевик");
    }

    @Test
    public void testGetAllMPA() {
        List<MPA> mpa = filmStorage.getAllMPA();
        int mpaAmount = 5;
        assertThat(mpa.size() == mpaAmount).isTrue();
        System.out.println(mpa.get(0).getName());
        assertThat(mpa.get(2).getName()).isEqualTo("PG-13");
    }

    @Test
    public void testUpdateFilm() {
        testFilm = filmStorage.getFilm(1).get();
        Film oldFilm = testFilm;
        assertThat(testFilm.getName()).isEqualTo("Terminator");

        Film filmForUpd = new Film("Rainbow", "UpdatedFilm", LocalDate.of(1999, 12, 12), 120, new MPA(1, "Комедия"));
        filmForUpd.setId(1);

        filmStorage.updateFilm(filmForUpd);
        testFilm = filmStorage.getFilm(1).get();
        assertThat(testFilm.getName()).isNotEqualTo("Terminator");
        assertThat(testFilm.getName()).isEqualTo("Rainbow");
        assertThat(testFilm.getDescription()).isEqualTo("UpdatedFilm");
        filmStorage.updateFilm(oldFilm);
    }
}