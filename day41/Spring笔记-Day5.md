# Spring 笔记 - Day 5（W7 第 5 天 · 2026-09-18）

> 主题：REST 分层 + 参数校验 `@Valid` + 全局异常处理 `@RestControllerAdvice`
> 今日口诀：**校验注解只贴标签，@Valid 才是开工令** / **告诉前端你错哪了，别扔个 500**

---

## 一、今天的真实场景（为什么要学这个）

Day 2 的 `POST` 接口长这样：前端传什么就存什么。

后果：前端（或者一个恶意脚本）发一句

```json
{ "name": "", "age": 200 }
```

数据库里就真的多了一个「没名字、200 岁」的用户。项目上线第一天，运营就会拿着这份数据来找你。

**所以后端必须有第二道防线**：前端校验是「体验」，后端校验是「纪律」——前端能绕过，后端绕不过。

---

## 二、口诀 → 再看定义

| 口诀 | 含义 |
|---|---|
| **校验注解只贴标签，@Valid 才是开工令** | `@NotBlank` / `@Min` 只是写在字段上的标签，没有任何执行能力。真正触发校验的是**方法参数上的 `@Valid`** |
| **告诉前端你错哪了，别扔个 500** | 校验失败、业务失败都要变成**看得懂的 HTTP 状态码 + 消息**，而不是 500 白页或一长串堆栈 |
| **业务层只描述"出了什么事"，谁来翻译成 HTTP 码是别人的事** | Service 抛 `UserNotFoundException`，至于前端看到 404 还是 200，由 `GlobalExceptionHandler` 决定 |

---

## 三、三个新角色

### 1. DTO：专门用来"收参数"的类

```java
public class UserCreateRequest {
    @NotBlank(message = "名字不能为空")
    private String name;

    @NotNull(message = "年龄不能为空")
    @Min(value = 0, message = "年龄不能小于 0")
    @Max(value = 150, message = "年龄不能大于 150")
    private Integer age;
    // getter/setter 必须写（Day 4 的教训：不写就静默绑不上）
}
```

**为什么不用 `entity/User` 直接接参数？** 这就是「分层」考点：

| | 职责 | 举例 |
|---|---|---|
| `entity/User` | 数据库里长什么样 | 有 `id`（数据库自增主键） |
| `dto/UserCreateRequest` | 接口该收什么参数 | **没有 `id`**（id 由后端生成，不能让前端传） |

两个东西一开始长得像，早晚会分叉（比如注册接口要 `password`、查询接口不能返回 `password`）。硬用 entity 接参数，改一处崩一片。

### 2. `@Valid`：贴在方法参数上的"开工令"

```java
@PostMapping
public User create(@Valid @RequestBody UserCreateRequest req) { ... }
```

执行顺序（很重要）：

```
请求 JSON → @RequestBody 反序列化成 DTO → @Valid 校验 → 通过才进方法体
                                        ↘ 不通过：抛 MethodArgumentNotValidException（方法体一行都没执行）
```

### 3. `@RestControllerAdvice`：全局异常"兜底网"

```java
@RestControllerAdvice                      // = @ControllerAdvice + @ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValid(...) { ... }   // → 400
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(...) { ... } // → 404
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleOther(...) { ... }    // → 500 兜底
}
```

它和 Day 3 学的 **AOP 是同一思想**：`@ControllerAdvice` 本质就是一个横切所有 Controller 的切面——
把你原本要在每个方法里写的 `try-catch`，**集中到一个类里写一次**。

---

## 四、真机实测记录（2 个版本对比，代码见 springboot 项目）

### A 版：有 `@Valid`（当前代码）

| 请求 | 结果 |
|---|---|
| `POST /user {"name":"ZhaoLiu","age":28}` | `200` → `{"id":4,"name":"ZhaoLiu","age":28}` |
| `POST /user {"name":"","age":200}` | **`400`** → `{"code":400,"msg":"参数校验不通过","errors":{"age":"年龄不能大于 150","name":"名字不能为空"}}` |
| `POST /user {"name":"SunQi"}`（age 缺失） | **`400`** → `{"code":400,...,"errors":{"age":"年龄不能为空"}}` |
| `GET /user/99`（查不到） | **`404`** → `{"code":404,"msg":"用户不存在：id=99"}` |
| `GET /user` | `200` → 4 条，只有合法的那条被加进去了 |

### B 版：把 `@Valid` 删掉，其他代码一个字不改

| 请求 | 结果 |
|---|---|
| `POST /user {"name":"","age":200}` | **`200`** → `{"id":4,"name":"","age":200}` |
| `POST /user {"name":"","age":-5}` | **`200`** → `{"id":5,"name":"","age":-5}` |
| `GET /user` | `[...,{"id":4,"name":"","age":200},{"id":5,"name":"","age":-5}]` → **脏数据真的进库了** |

> **结论**：不写 `@Valid`，DTO 上的注解**完全不执行**，且**不会有任何报错**——这才是最危险的地方：代码看起来"很规范"，实际形同虚设。
> 面试里被问「`@Valid` 是干什么的」，能把上面这条对比讲出来，比背定义有效十倍。

---

## 五、分层后的职责边界（面试常问）

| 层 | 只负责 | 不负责 |
|---|---|---|
| Controller | 收参数、`@Valid` 校验、转成实体、调 Service、返回 JSON | 不写业务判断（`if (user == null)`）、不写 SQL |
| Service | 业务规则（查不到就抛 `UserNotFoundException`） | 不关心 HTTP 状态码 |
| GlobalExceptionHandler | 把异常翻译成 HTTP 状态码 + 结构化消息 | 不写业务 |

**一句话**：Controller 像前台（收货、验货），Service 像后厨（干活），全局异常处理像客服（出事统一对外解释）。

---

## 六、今日三题批改

### Q1（换场景）DTO 写了 `@NotBlank`，空 name 却照样进 Service，最可能漏了什么？

**答**：方法参数上漏了 `@Valid`（开工令没发）。
`@NotBlank` 只是字段上的标签，需要有「人来执行」——`@Valid` 就是通知 Spring 去执行的那些注解。对照实验 B 版已实测：删 `@Valid` 后 `{"name":"","age":200}` 返回 200 且脏数据入库。

（同类漏写还有：DTO 忘了 getter/setter → Day 4 实测过，静默绑不上、值为 null、不报错。）

### Q2（简答）`@RestControllerAdvice` 为什么不用每个方法写 try-catch？和 AOP 什么关系？

**答**：`@RestControllerAdvice` = `@ControllerAdvice` + `@ResponseBody`，本质是**面向所有 Controller 的一个切面**。
Spring 在 `DispatcherServlet` 调 Controller 时，如果方法抛出异常，会先去找**匹配类型的 `@ExceptionHandler`**：
- 找得到 → 用这个方法生成响应（我们写的 400 / 404 / 500）
- 找不到 → 才交给默认的 `/error` 处理（就是那个 500 白页）

所以它和 Day 3 的 `@Aspect` 是同一套思想（横切 + 集中处理），只是入口不同：AOP 是**自己定义切点**，`ControllerAdvice` 是**Spring 已经帮你定好切点（所有 Controller）**。

### Q3（换场景）查不到用户，Controller 里 `if` 判断 vs Service 抛异常 + 全局处理？

**答**：**选方案 B**（项目已按 B 实现）。

理由三条：
1. **分层**：`null` 判断是业务语义（"这个人不存在"），该由 Service 说；Controller 里写 `if` 是把业务规则漏到表现层，下次换个接口调同一个 Service 还得再写一遍 `if`。
2. **复用**：任何地方（下单、加好友、发消息）调到「查这个用户」，都会自动拿到 404 语义，不用重复判断。
3. **可测**：Service 单测只需要断言「抛了 `UserNotFoundException`」，不需要造一个假的 HTTP 上下文。

同时注意：异常别乱抛——**能用异常表达"业务失败"，但不要用异常表达"正常分支"**（比如"用户已存在"要返回给前端做提示，那属于正常业务分支，建议返回业务码而不是抛 500 语义的异常）。

---

## 七、今日新踩的坑 / 注意点

1. **缺失字段 vs 空值**：`@NotNull` 管的是「没传 / 传了 null」，`@NotBlank` 管的是「null / 空串 / 全是空格」。字段缺失时 `required = false` 才不会报「缺少请求体」，校验注解才轮得上。
2. **`@Valid` 只在「开了校验的方法参数」上生效**：写在自己的私有方法参数上没用。
3. **异常路径下 AOP 的表现（现场日志）**：`GET /user/99` 时日志是
   `【@Before】进入方法：getUserById` → `【@Around】getUserById 抛异常：用户不存在：id=99`，
   **没有 `【@AfterReturning】` 那行** —— `@AfterReturning` 只在「正常返回」时才执行，抛异常走的是 `@AfterThrowing`。这条正好把 Day 3 的洋葱模型补圆了。
4. **`AtomicLong` 生成 id**：内存版"假装数据库"用 `AtomicLong.incrementAndGet()`，比 `int id++; id` 在多线程下安全（W3 学的原子性，这里用上了）。

---

## 八、明天（Day 6）预告

`Bean 生命周期` + `Spring 怎么解决循环依赖（三级缓存）` —— 这是 W7 周日复盘自测的必考点，也是面试高频。

---

## 附：本次提交

- 代码 commit：`Day41`（`dto/UserCreateRequest.java`、`exception/GlobalExceptionHandler.java`、`exception/UserNotFoundException.java`、`service/UserService(+Impl)`、`controller/UserController`、`pom.xml` 加 `spring-boot-starter-validation`）
- 笔记 commit：`Day41`（本文件）
- 备注：Boot 4.1.1 里 `spring-boot-starter-validation` **没有改名**（跟 3.x 视频一致，与本项目已踩过的 `web→webmvc`、`aop→aspectj` 不同）
