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
//
//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id) {
//        return userService.getUserById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
//        return userService.getUserById(id)
//                .map(existingUser -> {
//                    user.setId(id);
//                    return ResponseEntity.ok(userService.saveUser(user));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
    @DeleteMapping()
    public ResponseEntity<Void> deleteAllUser() {
        var allUsers = userService.getAllUsers();

        for (var user: allUsers ) {
            userService.deleteUser(user.getId());
        }
        return null;
    }
}
