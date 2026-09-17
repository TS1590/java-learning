# Spring笔记 · Day 3（W7 Day3 · 2026-09-16）

> 主题：AOP 入门 —— 代理模式 / JDK动态代理 vs CGLIB / @Aspect 切面实战

---

## 一、先搞清日志在哪看

**IDEA 底部 `Run` 面板**（启动后自动弹出 `Run: SpringbootApplication` 窗口）。
- 找不到：`Alt + 4`，或 `View → Tool Windows → Run`
- `log.info(...)`（切面日志）和 `System.out.println(...)`（打印类名）**都进这一个 Console**
- 启动成功的标志：`Started SpringbootApplication in x.xx seconds`

---

## 二、AOP 是什么（按"场景 → 口诀 → 定义"记）

### 1. 场景（为什么需要它）
`UserService` 有 10 个方法，每个方法都要：记日志 + 算耗时 + 查权限。
- 笨办法：10 个方法里各写一遍 → 100 行重复代码，改需求要改 10 处
- AOP 的办法：把这些"**跟业务无关、但每个方法都得做**"的活抽出来，交给"门卫"（代理）统一干

这类"每个方法都得做、但跟业务无关"的活，专业名词叫 **横切关注点**（日志、事务、权限、性能监控、限流）。

### 2. 口诀
> **业务归业务，杂活归切面；调用先进代理，代理干完杂活再喊真身。**
> `@Before` 进门前报备，`@AfterReturning` 出门后记账，`@Around` 是贴身保镖——前后都能插手。

### 3. 定义（背完口诀再看）
**AOP（面向切面编程）= 把横切关注点从业务代码里抽出来，通过代理在方法前后自动执行。**

### 4. 五个术语（对照自己的代码）
| 术语 | 白话 | 我的代码里对应 |
|---|---|---|
| 连接点 JoinPoint | 所有"能插一刀"的地方 | `UserService` 里每一个方法 |
| 切点 Pointcut | 我**挑中**的那几个 | `execution(* com.example.springboot.service..*.*(..))` |
| 通知 Advice | 插进去**干什么活** | `@Before` / `@AfterReturning` / `@Around` |
| 切面 Aspect | 切点 + 通知**打包** | `LogAspect` 类 |
| 代理 Proxy | 系统自动生成的"门卫" | `UserService$$SpringCGLIB$$0` |

---

## 三、代理机制（今天的核心）

### 1. 为什么要代理
`@Transactional`、`@Aspect` 这些注解**自己不会生效**，它们是靠**代理对象**在方法前后插入逻辑的。
调用方拿到的不是"真身"，而是"真身 + 门卫"合体的**代理**。

### 2. 两种代理方式对比（**实测数据，不是背书**）

实验方式：临时建 `PayService`（接口）+ `PayServiceImpl`（实现类），用 `CommandLineRunner` 打印注入对象的类名。

| 配置 | `UserService`（无接口） | `PayServiceImpl`（**有接口**） |
|---|---|---|
| Spring Boot 默认 | `UserService$$SpringCGLIB$$0` | `PayServiceImpl$$SpringCGLIB$$0` ← **CGLIB** |
| `--spring.aop.proxy-target-class=false` | `UserService$$SpringCGLIB$$0` | `jdk.proxy2.$Proxy63` ← **JDK 动态代理** |

### 3. 结论（面试直接背这段）
- **Spring Framework 默认**：有接口 → JDK 动态代理；无接口 → CGLIB
- **Spring Boot 默认**：`spring.aop.proxy-target-class=true` → **统一用 CGLIB**（连有接口的也用），可用配置切回
- **认身份证**：`jdk.proxy2.$ProxyN` = JDK 动态代理；`$$SpringCGLIB$$0` = CGLIB 代理
- JDK 动态代理要求"必须有接口"（它生成的是接口的实现类）；CGLIB 是"生成子类"，所以**无接口的类只能用 CGLIB**

---

## 四、通知的执行顺序 = 洋葱模型（实测踩到的）

我的实测打印顺序：
```
1  【@Before】进入方法：getAllUsers，参数：[]
2  （业务方法真正执行）
3  【@AfterReturning】getAllUsers 正常结束
4  【@Around】getAllUsers 执行完成，耗时 1 ms
```

**为什么 `@AfterReturning` 比 `@Around` 的"完成日志"先打？**
因为 **`@Around` 是最外层**，它 `proceed()` 的时候才轮到里面那层（`@Before` / `@AfterReturning`）执行；等它们跑完，控制权才回到 `@Around` 手里去算耗时。

> 口诀：**@Around 是洋葱最外面那层皮，其他通知都在它里面。内层先出，外层后出。**

---

## 五、@Transactional 失效的 5 个坑（高频面试题）

**"事务不回滚"是什么意思**：抛异常了，但前半截已经执行的 SQL **没被撤销**，真的写进库了。
例：转账 A 扣 100 → B 加 100 失败。回滚生效则 A 的钱退回来；事务失效则 A 的 100 凭空蒸发。

`@Transactional` = 给方法套的"自动撤销保险"：**方法抛异常 → 把这次方法里所有 SQL 撤销**。

| # | 坑 | 为什么失效 |
|---|---|---|
| 1 | **自己 `new` 出来的对象** | 拿到的是裸对象，**没走代理**，保险不在它身上 |
| 2 | 同类里 `this.xxx()` 自调用 | 也是绕过代理（专业说法：自调用失效） |
| 3 | 方法不是 `public` | 代理拦不到 private / protected 方法 |
| 4 | 自己 `try-catch` 把异常吞了 | 代理不知道出事了，以为一切正常 |
| 5 | 抛的是检查异常（编译期异常） | 默认只对 RuntimeException / Error 回滚 |

> **一句话根因**：`@Transactional` 靠**代理**实现，**只有从代理外面进来的调用才有保险**。

**怎么修第 1 条**：别自己 new，让 Spring 给你（构造器注入）；或注入 `ApplicationContext` 从容器里取 Bean。

---

## 六、今天的代码做了什么

`pom.xml` 加依赖（**Boot 4 改名了，注意**）：
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aspectj</artifactId>
</dependency>
```
> Boot 3.x 叫 `spring-boot-starter-aop`，**Boot 4.x 改成 `spring-boot-starter-aspectj`**（和 `web` → `webmvc` 一样的改名规律）。

新增 `aspect/LogAspect.java`：
- `@Aspect` + `@Component` → 声明"我是切面"，交给容器管
- `@Pointcut("execution(* com.example.springboot.service..*.*(..))")` → 挑对象：service 包及其子包下所有类的所有方法
- `@Before` → 进门报备（打印方法名 + 参数）
- `@Around` → 保镖：`joinPoint.proceed()` 放行真身，`return result` 把结果还回去（**漏了 return 接口会返回 null**）
- `@AfterReturning` → 出门记账

`UserController` 构造器加 1 行：
```java
System.out.println("【看代理】注入进来的 userService 实际类型 = " + userService.getClass().getName());
```

**实测结果（验收通过）**：
```
【看代理】...= com.example.springboot.service.UserService$$SpringCGLIB$$0   ← 拿到的是代理
【看真身】joinPoint.getTarget() = com.example.springboot.service.UserService  ← 切面能摸到真身
```
`curl localhost:8080/user` 返回 3 条 JSON，**UserController 业务代码一行未改**。

### 切点表达式的三个必踩坑
| 坑 | 后果 |
|---|---|
| 用 `spring-boot-starter-aop` | Boot 4 里找不到依赖，装不上 |
| 切点包名写错 / `..` 少一个点 | **一句日志都不打**，还以为切面没生效 |
| `@Around` 忘了 `return result` | 接口返回 null，浏览器一片空白 |

---

## 七、今日小测批改

- **Q1（事务 + new 为什么不回滚）**：不会 → 已讲，答案 = 5 坑第 1 条。**待复测**
- **Q2（有接口的类默认用哪种代理）**：答 B（JDK 动态代理）。**教材答案对**；但 **Spring Boot 默认 `proxy-target-class=true`，实际走 CGLIB**。面试要说完这句前提才算满分
- **Q3（10 个 Service 加耗时统计，手写 vs 切面）**：选 b **正确** ✅ 理由：① 横切关注点 ② 改 1 处 vs 改 10 处 ③ 业务代码不被污染、想关就关

---

## 八、补练：抽接口 + 补 toString（9/16 晚追加，commit `cf7c364`）

抽接口不是为了"写得好看"，而是为了**面向接口编程**：调用方只认接口签名，以后换实现（换成 MySQL 版 / 加缓存装饰器）Controller 一行都不用改。

| 文件 | 变化 |
|---|---|
| `service/UserService.java` | 变成**接口**：只有两个方法签名，**没有 `@Service`** |
| `service/UserServiceImpl.java` | **新建**：`@Service` + `implements UserService` + 两个 `@Override` |
| `entity/User.java` | 补上 `@Override public String toString()` |

> 口诀：**接口只写"能干什么"，实现类才写"怎么干"；`@Service` 永远标实现类。**
> （标在接口上 → 接口不能实例化 → 启动报 `NoSuchBeanDefinitionException`）

### 双轮实测：抽了接口，默认还是 CGLIB

| 启动方式 | 构造器打印的注入对象类型 |
|---|---|
| 默认 | `UserServiceImpl$$SpringCGLIB$$0` |
| 加 `--spring.aop.proxy-target-class=false` | `jdk.proxy2.$Proxy63` |

**这实锤了昨天的 Q2**：Spring Framework 的规则是"有接口走 JDK、无接口走 CGLIB"，但 **Spring Boot 默认 `proxy-target-class=true`，统一走 CGLIB**；关掉这个配置才切成 JDK 动态代理。而且 **JDK 代理下 AOP 照常生效**（`@Around` 日志照打）——因为代理身上挂着的是"切面通知 + 真身方法"，跟代理是怎么造出来的无关。

`toString()` 生效后，`@Around` 的日志从 `User@6986a0e7` 变成：
```
返回值：[User{id=1, name='张三', age=22}, User{id=2, name='李四', age=23}, User{id=3, name='王五', age=24}]
```

**埋一个坑（面试爱问）**：加了 `--spring.aop.proxy-target-class=false` 之后，如果 `UserController` 的构造器参数类型写成实现类 `UserServiceImpl` 而不是接口，启动会报 `BeanNotOfRequiredTypeException` —— 因为 JDK 代理不是 `UserServiceImpl` 的子类，塞不进去。**这就是"依赖接口而非实现"的实际理由。**

---

## 九、待办 / 下一步
- [x] 已做 ✅（commit `cf7c364`）：抽出 `UserService` 接口 + `UserServiceImpl` 实现类，双轮实测代理类名（默认 CGLIB / 关配置变 JDK 代理）
- [x] 已做 ✅：`entity/User` 补了 `toString()`，`@Around` 已能打印 `User{id=1, ...}` 而非 `User@6986a0e7`
- [ ] 用自己的话说清：`@Around` 里 `proceed()` 和 `return result` 分别干了什么
- [ ] Day 4：`@SpringBootApplication` 自动配置原理 + 配置文件（`application.properties`）
- [ ] 9/20（周日）：W7 复盘（Bean 生命周期 / 循环依赖三级缓存 / 自动配置）
