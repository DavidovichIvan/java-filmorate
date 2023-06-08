/*
package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.List;

@Slf4j
@Getter
@Setter
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUser(String id) {
        int requestId = VariablesValidation.checkRequestId(id);
        checkIfUserExists(requestId);

        return userStorage.getUser(requestId);
    }

    public void addFriend(String id, String friendId) {
        int userId = VariablesValidation.checkRequestId(id);
        int newFriendId = VariablesValidation.checkRequestId(friendId);

        checkIfUserExists(userId);
        checkIfUserExists(newFriendId);

        userStorage.addFriend(userId, newFriendId);
    }

    public void deleteFriend(String id, String friendId) {
        int userId = VariablesValidation.checkRequestId(id);
        int friendToDeleteId = VariablesValidation.checkRequestId(friendId);
        checkIfUserExists(userId);

        userStorage.deleteFriend(userId, friendToDeleteId);
    }

    public List<User> getFriends(String id) {
        int userId = VariablesValidation.checkRequestId(id);
        checkIfUserExists(userId);

        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(String id, String otherId) {
        int firstUserId = VariablesValidation.checkRequestId(id);
        int secondUserId = VariablesValidation.checkRequestId(otherId);
        checkIfUserExists(firstUserId);
        checkIfUserExists(secondUserId);

        return userStorage.getCommonFriends(firstUserId, secondUserId);
    }


    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public void checkIfUserExists(Integer requestId) {
        if (!userStorage.getUsersList().containsKey(requestId)) {
            throw new IdNotExistException();
        }
    }
}*/