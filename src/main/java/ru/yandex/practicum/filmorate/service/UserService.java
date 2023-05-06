package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.IdNotExistException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.users.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

        return userStorage.getUsersList().get(requestId);
    }

    public void addFriend(String id, String friendId) {
        int userId = VariablesValidation.checkRequestId(id);
        int newFriendId = VariablesValidation.checkRequestId(friendId);

        checkIfUserExists(userId);
        checkIfUserExists(newFriendId);

        userStorage
                .getUsersList()
                .get(userId)
                .getFriends()
                .add(newFriendId);
        userStorage
                .getUsersList()
                .get(newFriendId)
                .getFriends()
                .add(userId);
    }

    public void deleteFriend(String id, String friendId) {
        int userId = VariablesValidation.checkRequestId(id);
        int friendToDeleteId = VariablesValidation.checkRequestId(friendId);
        checkIfUserExists(userId);

        if (userStorage.getUsersList().get(userId).getFriends().contains(friendId)) {
            userStorage
                    .getUsersList()
                    .get(userId)
                    .getFriends()
                    .remove(friendToDeleteId);
            userStorage
                    .getUsersList()
                    .get(friendToDeleteId)
                    .getFriends()
                    .remove(userId);
        }
    }

    public List<User> getFriends(String id) {
        int userId = VariablesValidation.checkRequestId(id);
        checkIfUserExists(userId);

        Set<Integer> friends = userStorage
                .getUsersList()
                .get(userId)
                .getFriends();

        List<User> friendsList = new ArrayList<>();

        for (Integer i : friends) {
            friendsList.add(userStorage.getUsersList().get(i));
        }
        return friendsList;
    }

    public List<User> getCommonFriends(String id, String otherId) {
        int firstUserId = VariablesValidation.checkRequestId(id);
        int secondUserId = VariablesValidation.checkRequestId(otherId);
        checkIfUserExists(firstUserId);
        checkIfUserExists(secondUserId);

        Set<Integer> firstUserFriends = userStorage
                .getUsersList()
                .get(firstUserId)
                .getFriends();

        Set<Integer> secondUserFriends = userStorage
                .getUsersList()
                .get(secondUserId)
                .getFriends();

        List<User> commonFriendsList = new ArrayList<>();

        for (Integer i : firstUserFriends) {
            if (secondUserFriends.contains(i)) {
                commonFriendsList
                        .add(userStorage.getUsersList().get(i));
            }
        }
        return commonFriendsList;
    }

    public void checkIfUserExists(Integer requestId) {
        if (!userStorage.getUsersList().containsKey(requestId)) {
            throw new IdNotExistException();
        }
    }
}