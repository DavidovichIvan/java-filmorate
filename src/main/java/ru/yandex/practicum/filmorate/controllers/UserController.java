/*
package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usersOld")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable("id") String id) {
        return userService.getUser(id);
    }

    @PutMapping("{id}/friendsOld/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public String addFriend(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        userService.addFriend(id, friendId);
        return "Друг добавлен";
    }

    @DeleteMapping("{id}/friendsOld/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteFriend(@PathVariable("id") String id, @PathVariable("friendId") String friendId) {
        userService.deleteFriend(id, friendId);
        return "Друг удален";
    }

    @GetMapping("{id}/friendsOld")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriends(@PathVariable("id") String id) {
        return userService.getFriends(id);
    }

    @GetMapping("{id}/friendsOld/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getCommonFriends(@PathVariable("id") String id, @PathVariable("otherId") String otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
*/