# Day 38 · W7 Spring Boot Day 2：Bean 是谁造出来的 + 依赖怎么送进来

> 2026-09-15（二）· 面试高频考点：Bean 生命周期 / 依赖注入方式 / 构造器注入为什么最优

## 0. 先看场景（今天要解决的三个问题）

**场景 1**：昨天写的 `HelloController`，我一次都没 `new` 过它，那它是谁造出来的？为什么访问 `/hello` 就能进来？
**场景 2**：`UserController` 里要用 `UserService`，我写了个构造器就完事了，那个 `UserService` 对象是从哪冒出来的？
**场景 3**：同事把 `OrderService` 的 `@Service` 删了，项目直接起不来。一个注解，怎么能让整个项目启动失败？

三个问题指向同一件事：**Spring 容器（IOC 容器）在应用启动时就替我造了一堆对象，谁需要谁就去仓库里拿。**

> 口诀：**容器 = 仓库，Bean = 仓库里的货，`@Autowired` = 搬运工。**

---

## 1. 什么是 Bean，怎么"装"进仓库

**Bean = 交给 Spring 容器创建和管理的对象。**（我自己 `new` 的对象不叫 Bean）

怎么让它进仓库？**在类上标一个"组件注解"**，容器扫包时看到就把它造出来、放进仓库（默认单例，全局一个）：

| 注解 | 用在哪 | 记忆点 |
|---|---|---|
| `@Component` | 通用组件 | 万能款，其他三个本质都是它 |
| `@Controller` / `@RestController` | 控制器层 | 接 HTTP 请求 |
| `@Service` | 业务逻辑层 | 写业务判断，**事务一般加这层** |
| `@Repository` | 数据访问层 | 访问数据库（还能把数据库异常翻译成 Spring 异常） |

**还有第二种装货方式**：`@Configuration` + `@Bean`

```java
@Configuration
public class AppConfig {
    @Bean                    // 自己 new 出来，但交给仓库托管
    public UserService userService() {
        return new UserService();
    }
}
```
> 区别：`@Component` 是"**类自己报名**"（谁用谁标）；`@Bean` 是"**别人替他报名**"（第三方类改不了源码、或要复杂配置时用）。

**包扫描规则（昨天记过的，今天再验证一次）**：只扫**启动类所在包及其子包**。所以 `UserService` 放 `com.example.springboot.service` 里能被扫到；放到 `com.example.other` 里就会"启动失败，找不到这个 Bean"。

---

## 2. Bean 的一生（生命周期）

> 口诀：**造 → 填 → 用 → 销。**

| 阶段 | 容器干了什么 |
|---|---|
| ① **实例化**（造） | 调构造器 `new` 出对象（属性还是 null） |
| ② **属性填充**（填） | 把它依赖的 Bean 注入进去（构造器注入在第①步就完成了） |
| ③ **初始化**（装修） | 调 `@PostConstruct`、`InitializingBean`；**AOP 代理也在这一步生成** |
| ④ **使用**（用） | 放在容器里，谁要注入给谁，可反复复用（单例） |
| ⑤ **销毁**（销） | 容器关闭时调 `@PreDestroy`，释放资源 |

面试最常问的一句：**"AOP 代理是在初始化阶段生成的"** —— 记住这点，就懂了昨天那道题：**自己 `new` 出来的对象跳过了 ①~③ 整套流程，没有代理壳，所以 `@Transactional` 白写。**

---

## 3. 依赖怎么送进来：三种注入方式

```java
// ✅ 1. 构造器注入（官方推荐，今天用的就是这个）
private final UserService userService;

public UserController(UserService userService) {
    this.userService = userService;
}

// ⚠️ 2. setter 注入（对象建好后才能设值，适合"可选依赖"）
private UserService userService;

@Autowired
public void setUserService(UserService userService) {
    this.userService = userService;
}

// ❌ 3. 字段注入（老写法，Spring 官方已明确不推荐）
@Autowired
private UserService userService;
```

**为什么推荐构造器注入（面试答这 4 点就够）：**

1. **依赖不会为 null** —— 对象造出来那一刻依赖就已经就位（`new` 都 `new` 不出来，因为构造器必须传参）；
2. **能标 `final`** —— 依赖不可变，天然线程安全；
3. **依赖藏不住** —— 构造器参数一多就说明这个类职责太重，是代码坏味道的"报警器"（字段注入会把依赖藏起来）；
4. **最好写单测** —— 测试时直接 `new UserController(mockService)`，不需要启动 Spring 容器。

**字段注入的坑**：没法 `final`、脱离容器就 NPE、隐藏依赖、还容易掩盖循环依赖问题。

> 小知识：类里**只有一个构造器**时，`@Autowired` 可以省略（Spring 4.3+ 的约定），所以今天代码里我没写 `@Autowired`。

---

## 4. 今天的接口代码 + 实测记录

三个类（`entity.User` / `service.UserService` / `controller.UserController`），核心是这两句：

```java
@Service
public class UserService {            // ← 交给容器管理，仓库里才有这个货
    private final List<User> users = new ArrayList<>();   // 先用内存 List 假装是数据库
    ...
}

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {   // ← 容器把 UserService 塞进来
        this.userService = userService;
    }

    @GetMapping
    public List<User> list() { return userService.getAllUsers(); }   // GET /user

    @GetMapping("/{id}")
    public User detail(@PathVariable Long id) { return userService.getUserById(id); }  // GET /user/1
}
```

**实测（curl）：**

```
$ curl -i http://localhost:8080/user
HTTP/1.1 200
Content-Type: application/json

[{"id":1,"name":"张三","age":22},{"id":2,"name":"李四","age":23},{"id":3,"name":"王五","age":24}]

$ curl http://localhost:8080/user/2
{"id":2,"name":"李四","age":23}
```

`{id}` 叫**路径变量**，用 `@PathVariable` 取值：访问 `/user/2` → `id = 2`。
（对比昨天的 `@GetMapping("/hello")`：路径写死；今天用 `{}` 占位，一个接口就能处理所有 id。）

---

## 5. 关键实验：删掉 `@Service` 会怎样

把 `UserService` 上的 `@Service` 注释掉 → 重启 → **项目启动失败**：

```
NoSuchBeanDefinitionException: No qualifying bean of type
'com.example.springboot.service.UserService' available
```

**原因链条（一句话记住）：**

```
启动时容器扫包 → 没有 @Service，UserService 不会进仓库
        ↓
造 UserController 时发现：构造器要一个 UserService
        ↓
去仓库找 → 货架上空的 → 抛 NoSuchBeanDefinitionException → 启动崩
```

**为什么不是运行时才报错？** 因为 Spring 的哲学是**快速失败（Fail Fast）**：启动阶段就把所有 Bean 造好、依赖关系连好，有问题当场崩，而不是等用户访问接口时才 500。这也是为什么 Spring Boot 项目"启动成功"本身就是一次很全面的体检。

---

## 6. 今日小测复盘

**Q1（自己 new 的 Service，事务为什么不回滚？怎么改）→ 半对，已补正**

- 用户答："没有在代理上 new，加 `@Service`" —— **方向抓对了**（关键词：代理 / 加注解交给容器），但表述要修正为：
  - 不是"在代理上 new"，而是"**`new` 出来的是原始对象，根本没走代理**"：Spring 的事务是靠 **AOP 代理对象**实现的，只有从容器里拿到的那个**代理**，才会在方法前后开事务 / 提交 / 回滚；
  - 改法两步：① 类上加 `@Service` 交给容器管理；② 用到它的地方改成**注入**（构造器注入），**别再 `new`**。
- **加分点（自调用坑）**：就算加了 `@Service`，如果在**同一个类里**用 `this.method()` 调自己的 `@Transactional` 方法，走的还是原始对象、代理不生效 —— 这叫**自调用失效**，也是面试高频。解法：把自己注入自己 / 拆到另一个类 / 用 `AopContext.currentProxy()`。

**Q2（官方推荐哪种注入）→ C ✅**
构造器注入。理由就是上面 4 条：依赖不为 null、能 `final`、依赖关系显式、好写单测。

**Q3（删 `@Service` 会怎样）→ B ✅**
启动失败，报找不到 `UserService` 这个 Bean（`NoSuchBeanDefinitionException`）。已亲手做实验验证 ✅

---

## 7. 踩坑 / 细节记录

1. **`@RequestMapping("/user")` 放在类上 + 方法上 `@GetMapping`** → 最终路径 = `/user`；方法上写 `@GetMapping("/{id}")` → `/user/{id}`。
2. **`@PathVariable` 别忘**：路径里的 `{}` 变量要取出来用，必须声明参数并标 `@PathVariable`，否则参数为 null。
3. **找不到就返回 null**：现在 `/user/999` 返回的是空响应体（HTTP 200）。生产中要改成**统一返回体 + 全局异常处理**（后面 W8/W9 会补）。
4. **Bean 默认单例**：两个 Controller 注入同一个 `UserService`，拿到的是**同一个对象**（所以 Service 里别乱放会改变状态的可变字段）。

---

## 8. 今日成就

- ✅ 跑通"Controller → Service → 数据"三层结构，`GET /user` 和 `GET /user/1` 实测 200 + JSON
- ✅ 亲手做实验证明：删掉 `@Service` → 启动失败 `NoSuchBeanDefinitionException`（真懂了"Bean 是谁造的"）
- ✅ 掌握 Bean 生命周期 5 步，能答出"AOP 代理在初始化阶段生成"这个面试关键点
- ✅ 掌握三种注入方式，能说出构造器注入优于字段注入的 4 条理由
- ✅ 理解了 Spring 的**快速失败**设计：启动崩 = 好事，比运行时崩强
- ⏳ 下一步：W7 Day 3 —— **AOP 入门**（切面、通知、`@Transactional` 底层怎么织入）+ **`@SpringBootApplication` 自动配置是怎么生效的**（9/20 复盘要考）
