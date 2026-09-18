package com.example.springboot.exception;

// 自定义异常：业务层说"找不到这个人"，至于前端看到 404 还是别的，不归它管
// 继承 RuntimeException（非受检），这样 Service 方法签名上不用写 throws
public class UserNotFoundException extends RuntimeException {

    private final Long id;

    public UserNotFoundException(Long id) {
        super("用户不存在：id=" + id);
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
