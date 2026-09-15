# Day 37 · W7 Spring Boot Day 1：环境打通 + 第一个 REST 接口

> 2026-09-14（一）· W7 开跑 · 面试高频考点：IOC / DI / 自动配置 / REST 接口返回 JSON

## 1. IOC 与 DI 到底是什么

一句话：**控制反转（IOC）= 创建对象和装配依赖的主动权，从我自己的代码交给 Spring 容器。**

| | 传统写法 | Spring 写法 |
|---|---|---|
| 谁负责 new 对象 | 我自己在业务代码里 `new` | 容器启动时统一创建（Bean） |
| 依赖怎么来 | 自己 `new UserDao()`，写死在类里 | 容器"注入"给我（DI = 依赖注入） |
| 换实现 | 改业务代码 | 只改一处，业务代码不动 |
| 复用与增强 | 每次 new 都是新对象 | 默认单例，还能统一加 AOP（事务/日志） |

> **口诀：以前我 new，现在我要。**
> IOC 说的是"谁说了算"（控制权反转），DI 说的是"依赖怎么到我手上"（注入方式），IOC 是思想，DI 是实现手段。

## 2. 今天的项目是怎么来的（环境记录）

- 本机环境：IDEA **Ultimate 2026.1.1** + JDK 25（**没装独立 Maven**）
- 用 <https://start.spring.io> 生成 zip 下载 → 解压（zip 自带 `mvnw` 包装器，不需要另装 Maven）
- 参数选择：Maven 项目 / **Java 21** / **Spring Boot 4.1.1** / 依赖只勾 **Spring Web**
  - 注意：start.spring.io 现在**已经没有 3.5.x 了**，只剩 4.x 线，所以用 4.1.1
- 项目位置：`C:\Users\12412\IdeaProjects\学习\java\springboot`（**在 java-learning 仓库内**，每天能直接 commit）
- 本地仓库加速：新建了 `C:\Users\12412\.m2\settings.xml` 配阿里云镜像，否则从中央仓库下依赖很慢

**目录结构（要记住）**

```
springboot/
├── mvnw / mvnw.cmd              # Maven 包装器，不用装 Maven
├── pom.xml                      # 依赖清单（父工程 spring-boot-starter-parent）
└── src/
    ├── main/java/com/example/springboot/
    │   ├── SpringbootApplication.java   # 启动类（main 方法在这）
    │   └── HelloController.java         # 我写的第一个接口
    ├── main/resources/application.properties  # 配置文件
    └── test/java/...                    # 测试代码
```

## 3. 三个注解各干什么

| 注解 | 作用 | 记忆点 |
|---|---|---|
| `@SpringBootApplication` | 标在启动类上，= `@SpringBootConfiguration` + `@EnableAutoConfiguration` + `@ComponentScan` | "自动配置 + 扫包"的总开关 |
| `@RestController` | 标在类上，= `@Controller` + `@ResponseBody` | 返回值**直接写进响应体**，不去找页面 |
| `@GetMapping("/hello")` | 把 `GET /hello` 这个请求交给这个方法处理 | 换 `@PostMapping` 就只收 POST |

**包扫描规则（今天最重要的规则）：**
> Spring Boot 只扫描**启动类所在包及其子包**。启动类在 `com.example.springboot`，Controller 必须放这里或它的子包（如 `com.example.springboot.controller`）。
> 放到平级包（如 `com.example.other`）**不报错，但访问接口返回 404**。

## 4. 第一个接口 + 实测记录

```java
package com.example.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public Map<String, String> hello() {
        return Map.of("msg", "Hello Spring Boot!");
    }
}
```

启动 `SpringbootApplication`（内嵌 Tomcat，默认端口 8080），实测：

```
$ curl -i http://localhost:8080/hello
HTTP/1.1 200
Content-Type: application/json
Content-Length: 28

{"msg":"Hello Spring Boot!"}
```

**为什么返回 Map 就变成 JSON 了？** 因为有 `@ResponseBody`（`@RestController` 自带）+ Spring 内置的 **Jackson**：返回对象/Map 时自动序列化成 JSON 写进响应体。返回 `String` 则只是纯文本，不是 JSON。

## 5. 依赖注入：`new` 这行代码到底谁来做

不用 Spring：

```java
public class UserService {
    private UserDao dao = new UserDao();   // 自己 new，写死
}
```

用了 Spring（构造器注入，**推荐写法**）：

```java
@Service
public class UserService {
    private final UserDao dao;             // 只声明"我需要谁"

    public UserService(UserDao dao) {      // 容器把造好的 UserDao 塞进来
        this.dao = dao;
    }
}
```

**好处（面试答这两点）：**
1. **解耦、可替换** —— 业务代码不关心 `UserDao` 怎么造、是哪个实现，换实现不用改业务代码（面向接口编程）；
2. **统一管理 + 复用** —— 容器管 Bean 的创建/初始化/销毁，默认单例，避免到处 `new` 重复造对象，还能统一加 AOP 增强。

> **加分点（面试高频）**：`@Transactional` 只对**容器管理的 Bean** 生效。你自己 `new UserService()` 出来的对象，事务注解会失效——因为没经过 Spring 代理。这就是"我的事务为什么没生效"的标准答案。

## 6. 踩坑记录

1. **Controller 包放错 → 404**：不报错、启动成功，但接口访问不到。原因就是不在启动类的扫描范围内。
2. **Boot 4.x 把依赖改了名**：`spring-boot-starter-web` → **`spring-boot-starter-webmvc`**（看 3.x 视频时 pom 里写 `-web`，含义是一样的，不用怀疑）。
3. **中文目录 + `mvnw` 在命令行报 `ClassNotFoundException`**：这是环境/编码问题，不是代码问题；在 IDEA 里构建、运行完全正常。
4. **项目放仓库外 → git 看不见**：git 只管**仓库根目录（含 `.git` 的那个目录）以下**的东西。原来项目放在 `学习\springboot`（仓库外面），`git add` 根本看不到 → 已挪进 `学习\java\springboot`。
   - 同盘符下移动文件夹是**整体重命名**（只改一条"名字→编号"的登记，数据块不动），所以秒级完成、可逆、零风险；跨盘才会真的复制+删除。

## 7. 今日小测复盘

- **Q1 → B ✅**：IOC 的核心 = 对象的创建与依赖装配不再自己 `new`，交给容器统一管。（A 只是早期的一种配置方式，C 是编译，D 是连接池）
- **Q2 ✅**：谁来做 → **Spring 容器**创建 `UserDao` 并注入给 `UserService`；好处 → ①解耦易替换（面向接口）②统一管理生命周期、默认单例复用，还能加 AOP。加分点：`new` 出来的对象 `@Transactional` 失效。
- **Q3 ✅**：`@RestController` 类 + `@GetMapping("/hello")` 方法，返回 `Map<String,String>`，Jackson 自动转 JSON（实测 200 + `application/json`）。

## 8. 今日成就

- ✅ 打通 Spring Boot 环境（start.spring.io 生成 + mvnw 包装器，不装 Maven 也能跑）
- ✅ 跑通人生第一个 REST 接口：`GET /hello` 返回 JSON，实测 200
- ✅ 理解 IOC / DI：谁负责 new、依赖怎么来、两个好处、`@Transactional` 失效的坑
- ✅ 记住包扫描规则和 Boot 4.x 的 starter 改名
- ✅ 学会判断"我的 git 仓库在哪"（`git rev-parse --show-toplevel`；git 向上找最近的 `.git`）
- ⏳ 下一步：W7 Day 2 —— IOC 容器怎么装 Bean、Bean 生命周期、`@Autowired` 三种注入方式（为什么推荐构造器注入）

## 9. 补充：变式题三问（9/15 回炉讲解）

### Q1｜IOC 和 DI 是一回事吗？—— 不是

- **IOC 是"设计思想"**：它管的是**控制权归谁**——对象的创建与依赖装配，主动权从我手里交给容器。
- **DI 是"实现手段"**：它管的是**依赖怎么到我手上**——容器把我要的依赖注入进来（构造器 / Setter / 字段）。
- 类比：IOC 相当于"面向对象"这种思想，DI 相当于"用 class + 组合"这种做法。
- 还有另一种实现 IOC 的方式叫**依赖查找（DL）**，但工业界主流是 DI。

> 口诀：**IOC 管"谁创建"，DI 管"怎么送"。** 面试答"两个是一回事"会直接丢分。

### Q2｜同事自己 new 的对象，`@Transactional` 为什么不回滚？—— 因为事务长在代理上

```java
OrderService s = new OrderService();   // ❌ 原始对象，没有代理壳
s.createOrder();                       // @Transactional 没人读
```

- Spring 的事务是 **AOP 代理**实现的：只有从容器里拿到的**代理对象**，才会在方法前后开启 / 提交 / 回滚事务。
- `new` 出来的是**原始对象（裸对象）**，调用 `createOrder()` 就是一次普通方法调用，异常照抛，但**没人帮你回滚**。
- **改法**：① 给 `OrderService` 加 `@Service` 交给容器统一管理；② 用到它的地方改成**注入**（构造器注入），不要再 `new`。

> 口诀：**事务长在代理上，new 出来的是裸对象 —— 注解白写。**
> 这类事故线上很常见：老代码在工具类 / 静态方法 / 非 Spring 管理的类里 `new` 了 Service，排查一下午最后发现是这一行。

### Q3｜把 `@RestController` 改成 `@Controller`，访问 `/hello` 会看到什么？

- `@RestController` = `@Controller` + `@ResponseBody`：返回值**不当作页面名**，直接写进响应体 → Jackson 序列化成 JSON → 浏览器看到 `{"msg":"Hello Spring Boot!"}`。
- 只写 `@Controller`：Spring 把你的返回值当成**视图（页面）**去找模板 —— 项目里没有模板引擎、也没有叫这个名字的页面 → **报错页（Whitelabel Error Page）**，绝不是 JSON。
- 注意区分两种失败：**路径没匹配上才是 404**；这里是路径匹配成功、但回不出数据，属于"返回值处理方式错了"。
- 结论：**前后端分离项目一律用 `@RestController`。**

> 口诀：**`@RestController` 把返回值当数据，`@Controller` 把返回值当页面。**
