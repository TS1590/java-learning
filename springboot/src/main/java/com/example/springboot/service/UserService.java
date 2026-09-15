package com.example.springboot.service;

import com.example.springboot.entity.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    // 假装这是一张数据库表，先用内存 List 顶着（等 W8 接上 MySQL 再换掉）
    private final List<User> users = new ArrayList<>();

    public UserService() {
        users.add(new User(1L, "张三", 22));
        users.add(new User(2L, "李四", 23));
        users.add(new User(3L, "王五", 24));
    }

    public List<User> getAllUsers() {
        return users;
    }

    public User getUserById(Long id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
    }
}
