package com.example.game.controller;

import com.example.game.model.User;
import com.example.game.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public User createUser() {
        return userService.createUser();
    }

    @PostMapping("/updateLevel/{userId}")
    public User updateLevel(@PathVariable Long userId) {
        return userService.updateLevel(userId);
    }
}