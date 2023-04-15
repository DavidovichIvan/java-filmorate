package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@ToString
@EqualsAndHashCode
public class User {
    private static int UserIdCounter = 1;

    @Getter
    private final int id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    @Email
    @NotNull
    @NotBlank
    private String email;
    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;

        this.id = UserIdCounter;
        UserIdCounter++;
    }
}
