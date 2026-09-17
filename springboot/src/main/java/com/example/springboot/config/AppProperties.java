package com.example.springboot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")   // yml 里 app. 开头的配置，自动绑到这个类的字段
public class AppProperties {

    private String name;
    private String version;
    private String author;

    // @ConfigurationProperties 靠 setter 注入，没有 setter 就静默绑不上（不报错，值是 null）
    // IDEA 一键生成：光标放类里 → Alt + Insert → Getter and Setter → 全选

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "AppProperties{name='" + name + "', version='" + version + "', author='" + author + "'}";
    }
}
