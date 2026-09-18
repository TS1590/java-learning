package com.example.springboot.service;

import com.example.springboot.entity.User;
import com.example.springboot.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

// @Service 只能标在实现类上：接口不能实例化，标在接口上启动会报 NoSuchBeanDefinitionException
@Service
public class UserServiceImpl implements UserService {

    // 假装这是一张数据库表，先用内存 List 顶着（等 W8 接上 MySQL 再换掉）
    private final List<User> users = new ArrayList<>();

    // 自增 id 生成器：用 AtomicLong 而不是 `int id = 0; id++`，多线程下自增不会撞号
    private final AtomicLong idGen = new AtomicLong(3);

    public UserServiceImpl() {
        users.add(new User(1L, "张三", 22));
        users.add(new User(2L, "李四", 23));
        users.add(new User(3L, "王五", 24));
    }

    @Override
    public List<User> getAllUsers() {
        return users;
    }

    @Override
    public User getUserById(Long id) {
        for (User u : users) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        // 业务层只负责"说清出了什么事"，至于前端看到 404 还是 200，交给 GlobalExceptionHandler
        throw new UserNotFoundException(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(idGen.incrementAndGet());
        users.add(user);
        System.out.println("【Service】新增用户成功 = " + user);
        return user;
    }
}
