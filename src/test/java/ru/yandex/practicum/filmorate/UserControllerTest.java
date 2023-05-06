package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.InvalidBirthdayException;
import ru.yandex.practicum.filmorate.exceptions.InvalidEmailException;
import ru.yandex.practicum.filmorate.exceptions.InvalidLoginException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTest {
    //@Autowired
    UserStorage uController = new InMemoryUserStorage();

    User testUser = new User("Dogman@ttt.tt",
            "BigDog",
            "Dogman",
            LocalDate.of(2020, 12, 12));

    int testId = testUser.getId();

    @Test
    public void allInputIsCorrectPositiveTest() {
        uController.getAllUsers().clear();
        uController.addUser(testUser);

        assertEquals(uController.getAllUsers().get(0), testUser);
    }

    @org.junit.Test(expected = InvalidEmailException.class)
    public void emailCouldNotBeEmptyTest() {
        testUser.setEmail("");
        uController.addUser(testUser);
    }

    @org.junit.Test(expected = InvalidEmailException.class)
    public void emailShouldContainAtTest() {
        testUser.setEmail("testmail.tt");
        uController.addUser(testUser);
    }

    @Test
    public void shouldUseLoginInsteadOfEmptyNameTest() {
        testUser.setName("");
        assertTrue(!testUser.getLogin().isEmpty());

        uController.addUser(testUser);
        assertEquals(uController.getAllUsers().get(0).getName(),
                uController.getAllUsers().get(0).getLogin());
    }

    @org.junit.Test(expected = InvalidBirthdayException.class)
    public void birthdayCouldNotBeInFutureTest() {
        testUser.setBirthday(LocalDate.now().plusDays(1));
        uController.addUser(testUser);
    }

    @org.junit.Test(expected = InvalidLoginException.class)
    public void loginShouldNotBeEmptyTest() {
        testUser.setLogin("");
        uController.addUser(testUser);
    }

    @org.junit.Test(expected = InvalidLoginException.class)
    public void loginShouldNotContainWhitespacesTest() {
        testUser.setLogin(" tes t");
        uController.addUser(testUser);
    }

    @AfterAll
    public static void cleanUp() {
        User.setUserIdCounter(1);
    }
}