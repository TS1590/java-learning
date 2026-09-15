package com.example.springboot.controller;

import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 构造器注入：容器看到这个构造器，自己把 UserService 塞进来（不用写 @Autowired）
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET http://localhost:8080/user
    @GetMapping
    public List<User> list() {
        return userService.getAllUsers();
    }

    // GET http://localhost:8080/user/1
    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.getUserById(id);
    }
}
