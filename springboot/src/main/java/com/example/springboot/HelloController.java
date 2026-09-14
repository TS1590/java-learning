package com.example.springboot;          // 必须和启动类同包或其子包

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController                            // = @Controller + @ResponseBody
public class HelloController {

    @GetMapping("/hello")                  // 映射 GET /hello
    public Map<String, String> hello() {
        return Map.of("msg", "Hello Spring Boot!");
    }
}
