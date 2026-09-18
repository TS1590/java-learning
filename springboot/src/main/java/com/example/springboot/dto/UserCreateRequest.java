package com.example.springboot.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO = Data Transfer Object：专门用来"接前端传进来的参数"
// 为什么不直接用 entity/User？因为 User 是数据库长什么样，DTO 是接口该收什么参数，两者职责不同
public class UserCreateRequest {

    @NotBlank(message = "名字不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于 0")
    @Max(value = 150, message = "年龄不能大于 150")
    private Integer age;

    // getter/setter 一定要写（Day4 的教训：不写就静默绑不上）
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
