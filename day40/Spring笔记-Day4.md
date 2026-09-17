# Spring 笔记 · Day 4：`@SpringBootApplication` 自动配置 + 配置文件

> 日期：2026-09-17（W7 周四）｜主题：自动配置原理 + yml 外置配置
> 一句话：**main 里只有一行 `SpringApplication.run`，Tomcat、Jackson、切面代理全自动就位 —— 这些都是一个注解干的。**

---

## 一、场景：谁把 Tomcat 起来的？

我的 `SpringbootApplication` 里只有：
```java
public static void main(String[] args) {
    SpringApplication.run(SpringbootApplication.class, args);
}
```
一行配置都没写，结果：Tomcat 起来了、`UserController` 被扫到了、`LogAspect` 生效了、返回对象自动转 JSON。

**问：这些活儿是谁干的？** 答：`@SpringBootApplication`。

---

## 二、三合一拆解

| 拆出来的注解 | 干什么 | 项目里的证据 |
|---|---|---|
| `@SpringBootConfiguration` | 声明"这个类是配置类"（本质 = `@Configuration`） | `SpringbootApplication` 自身就是配置类 |
| `@ComponentScan` | 从**启动类所在包往下扫**，把 `@RestController` / `@Service` / `@Component` / `@Aspect` 注册成 Bean | `controller` / `service` / `aspect` 都在 `com.example.springboot` 子包下 |
| `@EnableAutoConfiguration` | 去 classpath 上所有 jar 里找"自动配置清单"，**按条件装配** | 加了 `spring-boot-starter-webmvc` → Tomcat 自动就位 |

**两条腿，缺一不可：**
- `@ComponentScan` 只扫**你自己写的代码**，扫不到 jar 里的东西
- jar 里的（Tomcat、Jackson、数据源）全靠 `@EnableAutoConfiguration`

> 可自验的推论：把 `UserController` 挪到 `com.example.other`（启动类包**外面**）→ 扫不到 → 启动报 `NoSuchBeanDefinitionException`。

---

## 三、自动配置加载链路（四步）

```
@SpringBootApplication
   └─ @EnableAutoConfiguration
        └─ @Import(AutoConfigurationImportSelector)          ← 清单搬运工
             └─ 读所有 jar 里的
                META-INF/spring/…AutoConfiguration.imports   ← 每个 starter 自带
                  └─ @ConditionalOnXxx 逐条筛选
                       ├─ 满足 → 类里的 @Bean 注册进容器
                       └─ 不满足 → 整块跳过（不报错、不浪费）
```

**关键点**：我的项目**永远不 import 任何框架实现**，只是"哪个 jar 带清单就认哪个"。

- 加 `spring-boot-starter-webmvc` → Tomcat / DispatcherServlet / Jackson 全自动配好
- 加 `spring-boot-starter-jdbc` → 数据源配置**凭空冒出来**
- 不加 → 那一整块自动配置类**根本不存在**

> **加分点**：Boot 2.7 之前清单文件叫 `META-INF/spring.factories`，2.7+ / 3.x / 4.x 改成了 `AutoConfiguration.imports`。面试问自动配置原理能说出这个变更很加分。

**口诀**：
> **一个注解三件事：声明配置类、扫包找 Bean、按条件自动装配。**
> **有就配、没有就跳；你没写我兜底、你写了我让路。**

---

## 四、条件注解的三类作用（附本项目 `--debug` 实测原文）

| 条件注解 | 决定什么 | 本项目实测证据 |
|---|---|---|
| `@ConditionalOnClass` | **有没有资格装配** | `GsonHttpMessageConvertersConfiguration: Did not match — @ConditionalOnClass did not find required class 'com.google.gson.Gson'` |
| `@ConditionalOnMissingBean` | **谁优先** | `WebMvcAutoConfiguration matched: … @ConditionalOnMissingBean (types: WebMvcConfigurationSupport) did not find any beans` → 我没自己配，所以默认的生效 |
| `@ConditionalOnProperty` | **开关** | `CglibAutoProxyConfiguration matched: @ConditionalOnBooleanProperty (spring.aop.proxy-target-class=true) matched` |

### 实测：条件评估报告（437 行）

**生效的（Positive matches）**
```
AopAutoConfiguration matched:
   - @ConditionalOnBooleanProperty (spring.aop.auto=true) matched

AopAutoConfiguration.AspectJAutoProxyingConfiguration matched:
   - @ConditionalOnClass found required class 'org.aspectj.weaver.Advice'
AopAutoConfiguration.AspectJAutoProxyingConfiguration.CglibAutoProxyConfiguration matched:
   - @ConditionalOnBooleanProperty (spring.aop.proxy-target-class=true) matched   ← 昨天的 Q2 由框架自己回答

WebMvcAutoConfiguration matched:
   - @ConditionalOnClass found required classes 'jakarta.servlet.Servlet',
     'org.springframework.web.servlet.DispatcherServlet', 'WebMvcConfigurer'
   - @ConditionalOnMissingBean (types: WebMvcConfigurationSupport) did not find any beans
```
→ 第一条说明：**Boot 默认 CGLIB 是框架报告里写着的**（对应 Day 3 那道代理题）。
→ 第二条说明：**"加了 webmvc 就有 Tomcat"的机制是 `@ConditionalOnClass` 查到了 DispatcherServlet 这些类。**

**没生效的（Negative matches）**
```
GsonHttpMessageConvertersConfiguration:
   Did not match:
      - @ConditionalOnClass did not find required class 'com.google.gson.Gson'
JdkDynamicAutoProxyConfiguration:
   Did not match:
      - @ConditionalOnBooleanProperty (spring.aop.proxy-target-class=false) did not find property
```
→ 没有 Gson 这个类就跳过；JDK 代理那套因为属性没设成 `false` 所以不生效 —— 和 CGLIB 那条正好是一对。

**自己怎么看**：`application.yml` 里写 `debug: true`（或启动加 `--debug`）→ Run 面板 `Ctrl+F` 搜 `Positive matches` / `Negative matches`。

> ⚠️ `debug: true` 只是学习用，**上线前要关掉**（会打几百行日志）。

---

## 五、配置文件：properties vs yml

同一个配置的两种写法，**功能完全等价**：

```properties
spring.application.name=springboot
server.port=8080
app.name=我的第一个SpringBoot应用
```
```yaml
spring:
  application:
    name: springboot
server:
  port: 8080
app:
  name: 我的第一个SpringBoot应用
```

**用 yml 的 3 个理由（都是给人看的）**：
1. **前缀不重复**：properties 要写 10 遍 `spring.`，yml 父级只写一次
2. **层级一眼可见**：谁是谁的子配置看缩进就知道（后面 MyBatis、Redis 的配置都是成组的）
3. **社区主流**：官方文档、开源项目、公司里基本都是 yml

**yml 的三个坑**：
| 坑 | 说明 |
|---|---|
| 冒号后必须有空格 | `port: 8080` ✅ ／ `port:8080` ❌（会被当成一个普通字符串） |
| 只能用空格缩进 | 敲 Tab 直接报错；同层必须对齐 |
| 值里有 `:` 或 `#` | 要用引号包起来 |

---

## 六、`@ConfigurationProperties` 实战（今天真踩到的坑）

```java
@Component
@ConfigurationProperties(prefix = "app")   // yml 里 app. 开头的配置绑到这个类
public class AppProperties {
    private String name;
    private String version;
    private String author;
    // getter / setter 必须写！见下面的坑
}
```

### ⚠️ 坑：**忘了写 setter = 静默绑不上（不报错，值是 null）**

今天第一次实测输出：
```
@ConfigurationProperties 拿到 = AppProperties{name='null', version='null', author='null'}
```
**没有任何报错**，启动完全正常 —— 因为 `app.name` 这类属性在 Bean 上找不到对应的 setter，Spring 就当成"不认识的属性"直接忽略了。

补上 getter/setter 后（IDEA：光标放类里 → `Alt + Insert` → Getter and Setter → 全选）：
```
@ConfigurationProperties 拿到 = AppProperties{name='我的第一个SpringBoot应用', version='1.0.0', author='姚晓飞'}
```

> 记住：**`@ConfigurationProperties` 靠 setter 注入。忘写 → 不报错但全是 null**，这是最难查的一类 bug。

### 三种取值方式对比

| 方式 | 写法 | 适用场景 |
|---|---|---|
| `@Value("${server.port}")` | 单个字段 | 取一两个零散的值 |
| `@ConfigurationProperties(prefix="app")` | 整个对象 | **成组的配置**（推荐，能类型校验、能嵌套） |
| `Environment` | `env.getProperty("app.name")` | 运行时动态取、不知道 key 的情况 |

---

## 七、配置优先级（实测证据）

**优先级（高 → 低）**：
```
命令行参数 > 环境变量 > jar 外部的 application.yml > jar 内部的 application.yml > 框架默认值
```

**实测 1：命令行压过配置文件**
yml 里写 `server.port: 8080`，启动加 `--server.port=8943` → 日志 `Tomcat started on port 8943`。✅

**实测 2：jar 旁边的配置文件覆盖 jar 内的（不重新打包！）**
jar 内 yml 是 `app.version: 1.0.0`，在 jar 同目录放一个只写了 `app.version: 2.0.0` 的 `application.yml`，**同一个 jar 再启动**：
```
@ConfigurationProperties 拿到 = AppProperties{name='我的第一个SpringBoot应用', version='2.0.0', author='姚晓飞'}
```
→ `version` 变成 2.0.0，`name` / `author` 仍来自 jar 内 —— **外部覆盖同名项，其余照旧**。

**为什么这样设计？** jar 交给运维后，人家不可能改你的源码、也不想重新打包，但可以：
- 改外面的配置文件
- 启动时加参数

**"外面的能压里面的"，就是为了不改代码、不改文件也能改行为。**

---

## 八、今天的产出与验收

| 项 | 结果 |
|---|---|
| `application.properties` → `application.yml`（含 `app.*` 自定义配置 + `debug: true`） | ✅ |
| `config/AppProperties.java`（`@ConfigurationProperties`） | ✅ **第一版漏了 setter → 已补** |
| `controller/ConfigController.java`（`@Value` / 对象绑定 / `Environment` 三种方式） | ✅ |
| `curl /config` 三行值 = yml 里写的 | ✅ 实测通过 |
| 外部配置覆盖验证 | ✅ `version` 1.0.0 → 2.0.0 不重新打包 |
| `/user`、`/user/2` 回归 | ✅ 200，接口未受影响 |

---

## 九、待办 / 下一步

- [ ] 自己改一次 `application.yml` 里 `app.version: 1.0.0` → `2.0.0`，重启看 `/config` 返回值跟着变（验收标准最后一条，亲手做一遍）
- [ ] 在 IDEA 里 `Ctrl+F` 搜 `Positive matches`，亲眼看一下自己项目的名字出现在条件报告里
- [ ] 学完把 `debug: true` 注释掉（上线不留）
- [ ] Day 5：Lombok `@Data` 压掉 getter/setter/toString（顺带解决"忘写 setter"这类坑）
- [ ] 9/20（周日）W7 复盘：Bean 生命周期 / 循环依赖三级缓存 / `@SpringBootApplication`；复测 `@Transactional` 失效 5 坑
