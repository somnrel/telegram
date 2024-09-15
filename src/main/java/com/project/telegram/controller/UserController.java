package com.project.telegram.controller;

import org.springframework.web.bind.annotation.RestController;
import com.project.telegram.service.UserService;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


}
