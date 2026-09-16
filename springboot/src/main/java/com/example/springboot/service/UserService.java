package com.example.springboot.service;

import com.example.springboot.entity.User;

import java.util.List;

// 接口：只声明"能干什么"，不写"怎么干"
// 从 Java 8 起接口也能有 default / static 方法，但这里先只写方法签名
public interface UserService {

    List<User> getAllUsers();

    User getUserById(Long id);
}
