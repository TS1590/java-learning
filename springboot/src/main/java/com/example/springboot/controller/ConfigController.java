package com.example.springboot.controller;

import com.example.springboot.config.AppProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Value("${server.port}")                 // 方式1：单个值
    private String port;

    private final AppProperties appProperties;   // 方式2：整个对象绑定
    private final Environment environment;       // 方式3：什么都能拿的"配置大全"

    public ConfigController(AppProperties appProperties, Environment environment) {
        this.appProperties = appProperties;
        this.environment = environment;
    }

    @GetMapping
    public String config() {
        return "@Value 拿到端口 = " + port
                + "\n@ConfigurationProperties 拿到 = " + appProperties
                + "\nEnvironment 拿到 app.name = " + environment.getProperty("app.name");
    }
}
