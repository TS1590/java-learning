# Day 31 · W6 Redis Day 2：String + List 专敲

> 2026-09-06（日）· 在 myredis 容器里实测，踩中 2 个经典暗坑，全记录

## 1. String（字符串）命令实测

```bash
docker exec -it myredis redis-cli
```

| 命令 | 实测输出 | 含义 |
|---|---|---|
| `SET name 张三` | OK | 写入键值对 |
| `GET name` | `"\xe5\xbc\xa0\xe4\xb8\x89"` | 读出来（中文被转义显示，见暗坑 1） |
| `STRLEN name` | 6 | 值的字节长度（张 3 字节 + 三 3 字节 = 6） |
| `SETNX lock 1` | (integer) 1 | **S**et if **N**ot e**X**ists：键不存在才写入（分布式锁雏形） |
| `INCR like:101` | (integer) 1 | +1（键不存在时自动从 0 起算） |
| `DECR like:101` | (integer) 0 | −1 |
| `EXPIRE code 300` | (integer) **0** | 设过期：**返回 0 = 失败！键不存在**（见暗坑 2） |
| `TTL code` | -2 | 剩余秒数：**-2 = 键不存在** / -1 = 存在但永不过期 / 正数 = 剩余秒 |
| `SETEX code 300 123456` | OK | 存值 + 设 300 秒过期，一步到位且原子 |
| `TTL code` | ~295 | 设置成功，秒数在倒计时 ✅ |

**String 经典场景**
- 手机验证码：`SETEX code 300 123456`（5 分钟有效，自动过期）
- 点赞/访问量计数：`INCR like:101`（原子 +1）
- 分布式锁雏形：`SETNX lock 1` 抢锁，抢到返回 1，抢不到返回 0

## 2. 暗坑 1：GET 中文显示 `\xe5\xbc\xa0\xe4\xb8\x89`

**数据没坏！** 这串字节就是"张三"的 UTF-8 编码（`\xe5\xbc\xa0`=张，`\xe4\xb8\x89`=三）。

原因：redis-cli 发现终端显示不了 UTF-8 中文（Windows cmd 默认 GBK 代码页），自动转义成字节防乱码。
解法：加 `--raw` 参数看原文：

```bash
docker exec -it myredis redis-cli --raw
```

## 3. 暗坑 2：EXPIRE 返回 0 = 静默失败

`EXPIRE` 返回 **1** = 设置成功，**0** = 键不存在，没东西可设过期（不报错，悄悄失败）。

口诀：**先有钥匙，才能上发条**（先 SET 出键，再 EXPIRE 设过期）。

这正是 **SETEX 存在的意义**：一条命令原子完成"存值 + 设过期"，不会中间出错导致验证码**永不过期**（真实线上 bug：缓存堆满永不过期的垃圾）。

**TTL 三值记忆**：`-1` = 键在但没设过期 / `-2` = 键不存在 / 正数 = 剩余秒数

## 4. INCR 为什么必须用（面试考点 ⭐）

**问题**：统计访问量，为什么用 `INCR page:home`，而不是"先 GET 拿到数，自己 +1 再 SET 回去"？

**答案**：因为"GET → 算 → SET"是**三步操作，不原子**。两个线程可能同时 GET 到 10，各自 +1 成 11 再写回 → 丢了 1 次计数（实际访问 2 次只记 1 次）。

而 `INCR` 是**一条命令**，Redis 是单线程执行命令的——命令在服务端**排队逐个执行，天然不被打断**，所以原子。10 次并发 INCR 一定等于 +10。

类比：INCR = 一个人守着计数器按一下（不会乱）；GET+SET = 两个人同时瞄一眼再各自去写，会互相覆盖。

> 口诀：**单条命令原子，读改写三步必炸。能用一条命令完成的计数，绝不拆三步。**

## 5. List（列表）专敲

```bash
LPUSH news:list 1 2 3    # 从左边进（头插）
RPUSH news:list 4 5      # 从右边进（尾插）
LRANGE news:list 0 -1    # 看全部（0 到 -1 = 头到尾）
LRANGE news:list 0 9     # 只看前 10 条
LPOP news:list           # 左边出（取走头部）
RPOP news:list           # 右边出
LLEN news:list           # 长度（几条）
```

**List 经典场景**
- **最新 10 条评论/新闻**：新数据用 `LPUSH` 头插 → 越新的越靠前 → `LRANGE key 0 9` 直接拿最新 10 条
- **消息队列**：生产者 `LPUSH` 进队，消费者 `RPOP` 出队（先入先出，排队打饭）
- 为什么用 List：**有序 + 可重复 + 头尾操作 O(1)**，天然就是"排队"的数据结构

> 口诀：**要"最新"就用 LPUSH 头插，LRANGE 0 9 取前 N。**

## 6. 今日小测复盘

- Q1：B ✅ `SETEX code 300 123456`（存 + 过期一步到位）
- Q2：INCR 原子性（见第 4 节，已讲透 + 巩固题）
- Q3：最新 10 条 = `LPUSH news:list 10 9 8 ... 1` → `LRANGE news:list 0 9`（头插让最新在最前，0 9 截前 10）

## 7. 今日成就

- ✅ String 7 条命令实测（SET/GET/SETNX/INCR/DECR/EXPIRE/SETEX/TTL/STRLEN）
- ✅ 踩坑并解决：GET 中文转义（--raw）、EXPIRE 0 静默失败（SETEX）
- ✅ INCR 原子性考点打通
- ✅ List 基础命令实测
- ⏳ 未完成：Hash/Set/ZSet（Day 3 起专敲）、Redis 笔记 push 后交 Day 2 测验巩固题
