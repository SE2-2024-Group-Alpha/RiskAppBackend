package se.alpha.riskappbackend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.alpha.riskappbackend.entity.User;
import se.alpha.riskappbackend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteAllUser() {
        var allUsers = userService.getAllUsers();

        for (var user: allUsers ) {
            userService.deleteUser(user.getId());
        }
        return null;
    }
}
