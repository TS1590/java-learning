package com.example.springboot.controller;

import com.example.springboot.dto.UserCreateRequest;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
        // 实验：看容器注入进来的到底是真身还是代理
        System.out.println("【看代理】注入进来的 userService 实际类型 = " + userService.getClass().getName());
    }

    // GET http://localhost:8080/user
    @GetMapping
    public List<User> list() {
        return userService.getAllUsers();
    }

    // GET http://localhost:8080/user/1  （查不到 → 404 + 提示，不再是 null）
    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // POST http://localhost:8080/user
    // @Valid = 开工令：没有它，DTO 上的 @NotBlank / @Min / @Max 全是摆设（注解只负责贴标签）
    // @RequestBody = 把请求体里的 JSON 反序列化成 UserCreateRequest 对象
    @PostMapping
    public User create(@Valid @RequestBody UserCreateRequest req) {
        // Controller 只干三件事：收参数 → 转成实体 → 交给 Service。业务规则不写在这里
        User user = new User(null, req.getName(), req.getAge());
        return userService.addUser(user);
    }
}
