package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@ToString
@EqualsAndHashCode

public class User {
    @Getter
    @Setter
    private static int UserIdCounter = 1;

    @Getter
    private final int id;

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private LocalDate birthday;

    @Getter
    @Setter
    private Set<Integer> friends;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        this.friends = new HashSet<>();

        this.id = UserIdCounter;
        UserIdCounter++;
    }
}
