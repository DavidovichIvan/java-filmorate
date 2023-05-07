package ru.yandex.practicum.filmorate.storage.users;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Getter
@Setter
@Component
public class InMemoryUserStorage implements UserStorage {

    @Getter
    Map<Integer, User> usersList = new HashMap<>();

    @Override
    public User addUser(User user) {

        if (usersList.containsKey(user.getId())) {
            log.info("Запрос на добавление; пользователь с id: {} уже существует", user.getId());
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new IdAlreadyExistsException();
        }

        userDataValidate(user);
        usersList.put(user.getId(), user);
        correctIdCounter(user);
        log.info("Запрос на добавление; сохранен пользователь: {} ", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!usersList.containsKey(user.getId())) {
            log.info("Запрос на обновление; не существует пользователя с id: {} ", user.getId());
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new IdNotExistException();
        }
        userDataValidate(user);
        usersList.put(user.getId(), user);
        User.setUserIdCounter(User.getUserIdCounter() - 1);
        log.info("Запрос на обновление; обновлен пользователь: {} ", user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}", usersList.size());
        List<User> users = new ArrayList<>(usersList.values());
        return users;
    }

    @Override
    public User getUser(Integer id) {
        return usersList.get(id);
    }

    @Override
    public void addFriend(Integer userId, Integer newFriendId) {
        usersList.get(userId)
                .getFriends()
                .add(newFriendId);
        usersList.get(newFriendId)
                .getFriends()
                .add(userId);
    }

    public void deleteFriend(Integer userId, Integer friendId) {
        if (usersList.get(userId).getFriends().contains(friendId)) {
            usersList.get(userId)
                    .getFriends()
                    .remove(friendId);

            usersList.get(friendId)
                    .getFriends()
                    .remove(userId);
        }
    }

    public List<User> getFriends(Integer userId) {

        Set<Integer> friends = usersList.get(userId)
                .getFriends();

        List<User> friendsList = new ArrayList<>();

        for (Integer i : friends) {
            friendsList.add(usersList.get(i));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(Integer firstUserId, Integer secondUserId) {
        Set<Integer> firstUserFriends = usersList.get(firstUserId)
                .getFriends();

        Set<Integer> secondUserFriends = usersList.get(secondUserId)
                .getFriends();

        List<User> commonFriendsList = new ArrayList<>();

        for (Integer i : firstUserFriends) {
            if (secondUserFriends.contains(i)) {
                commonFriendsList
                        .add(usersList.get(i));
            }
        }
        return commonFriendsList;
    }

    private void userDataValidate(User user) {
        if (!StringUtils.hasText(user.getEmail()) || !user.getEmail().contains("@")) {
            log.info("Адрес электронной почты не введен или введен в неверном формате {} ", user.getEmail());
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new InvalidEmailException(user.getEmail());
        }

        if (!StringUtils.hasText(user.getLogin()) || user.getLogin().contains(" ")) {
            log.info("Логин пустой или содержит пробелы");
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new InvalidLoginException();
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.info("Дата рождения установлена в будущем");
            User.setUserIdCounter(User.getUserIdCounter() - 1);
            throw new InvalidBirthdayException(user.getBirthday());
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void correctIdCounter(User user) {
        if (user.getId() != User.getUserIdCounter() - 1) {
            User.setUserIdCounter(User.getUserIdCounter() - 1);
        }
        while (usersList.containsKey(User.getUserIdCounter())) {
            User.setUserIdCounter(User.getUserIdCounter() + 1);
        }
    }
}