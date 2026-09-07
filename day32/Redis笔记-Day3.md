# Day 32 · W6 Redis Day 3：Hash + Set 专敲

> 2026-09-07（一）· 在 myredis 容器实测，含 WRONGTYPE 类型冲突坑 + 4 大场景对号入座

## 1. Hash（哈希/散列）—— 存"对象"，按字段读写

```bash
HSET user:1 name 张三 age 21 city 温州   # 存对象（多个字段一次写）
HGET user:1 name      # → 张三（取单个字段）
HGETALL user:1        # → name 张三 age 21 city 温州（取全部）
HINCRBY user:1 age 1  # → 22（给 age 字段 +1）
HDEL user:1 city      # → 1（删字段）
HEXISTS user:1 name   # → 1（字段在不在）
```

**Hash 经典场景：缓存用户对象/商品对象**

vs String+JSON 的关键差异 = **字段级读写**：

| 场景 | String+JSON | Hash |
|---|---|---|
| 只改 name 一个字段 | 取出整个 JSON → 反序列化 → 改 → 再序列化 → 覆盖写回（重） | `HSET user:1 name 新名`（轻） |
| 只读 age | 整个 JSON 取回再解析 | `HGET user:1 age` |
| 对象里有计数器（积分/等级） | 同上，笨重 | `HINCRBY user:1 score 10` |

> 真实工作：用户信息、商品详情这类"**一个对象 + 常改单字段**"用 Hash；低频整体读写才用 String+JSON。

## 2. Set（集合）—— 去重名单

```bash
SADD like:101 user1 user2 user3   # 加人（重复加自动忽略）
SMEMBERS like:101                 # → 看全部成员（顺序不保证！）
SCARD like:101                    # → 3（人数）
SISMEMBER like:101 user2          # → 1 = 在集合里（点过赞）/ 0 = 不在
SREM like:101 user2               # → 1（移除 = 取消点赞）
SPOP like:101                     # → 随机弹出一个（抽奖，弹出即删）
```

**为什么 Set 天然适合"点赞去重"**：Set 内部只收不重复的成员，**重复 SADD 同一个人会被忽略**，所以无论点几次赞，名单里只有一条记录，`SISMEMBER` 一查便知点没点过——不用先查重再插入。

**Set 经典场景**
- **点赞/收藏名单**：`SADD post:888 user1` + `SISMEMBER post:888 user1` 判断红心亮没亮
- **抽奖**：`SPOP` 随机弹一人，弹完即删不会重复中奖
- **共同好友/共同关注**：集合运算（见下）

## 3. 集合运算：SINTER / SUNION / SDIFF（面试加分点 ⭐）

```bash
SADD user:1 user2 user3 user4
SADD user:2 user1 user3 user5

SINTER user:1 user:2    # → user3（交集：共同好友）
SUNION user:1 user:2    # → user1..user5 去重合并（并集）
SDIFF user:1 user:2     # → user4（差集：只有我有，你没有）
```

> 口诀：**SINTER 交（都要有）/ SUNION 并（全都要）/ SDIFF 差（只有我有）。**
>
> 真实场景：共同好友推荐、兴趣标签匹配（物以类聚）、"可能认识的人"= SDIFF 我好友 − 你好友。

## 4. 暗坑：WRONGTYPE（类型冲突）⚠️

**现象**：`SADD like:101 user1 user2 user3` 报错 `WRONGTYPE Operation against a key holding the wrong kind of value`

**原因**：`like:101` 在 Day 2 已被 `INCR/DECR` 用成了 **String**（值 0）。Redis 里 **一个 key 只能存一种类型**，不能先当计数器用、又当名单用。

**解法**：
```bash
TYPE like:101     # 先看它现在是什么类型 → string
DEL like:101      # 删掉重建（或换个新键名）
SADD like:101 user1 user2 user3   # 重来，成功
```

**规范做法：同业务"计数"和"名单"必须分键**
- 点赞数（数字）：`like:cnt:101` → INCR
- 点赞人（名单）：`like:users:101` → SADD/SISMEMBER

> 生产上键名冲突是真实事故源，养成"业务:类型:id"命名习惯（如 `user:1`、`post:888`、`like:users:101`）。

## 5. 底层编码：小结构省内存，大了升级（面试一句话）

| 类型 | 小（省内存） | 大（查得快） | 升级触发 |
|---|---|---|---|
| Hash | **ziplist**（紧凑数组） | **hashtable**（散列表） | 字段多/值大 |
| Set | **intset**（整数数组） | **hashtable** | 非整数 / 成员多 |

一句话：**元素少时用紧凑连续结构省内存（ziplist/intset），变大了自动升级散列表保性能。**
（Redis 7+ 内部小结构改叫 listpack，面试答 ziplist 是经典叫法同样给分；日常业务里绝大多数 key 都是小结构，所以 Redis 默认很省内存。）

## 6. 今日小测复盘

- Q1：**B ✅** Hash（对象 + 常改单字段 → 字段级读写）
- Q2：✅ Set 内部只收不重复成员，重复 SADD 被忽略 → 天然去重，SISMEMBER 一查便知
- Q3：✅ `SINTER user:1 user:2` 共同好友

## 7. 今日成就

- ✅ Hash 6 命令实测（HSET/HGET/HGETALL/HINCRBY/HDEL/HEXISTS）
- ✅ Set 8 命令实测（SADD/SMEMBERS/SCARD/SISMEMBER/SREM/SPOP + SINTER/SUNION/SDIFF）
- ✅ 踩坑解决：WRONGTYPE 类型冲突 → 分键规范（like:cnt vs like:users）
- ✅ 4 大场景对号入座：对象缓存(Hash) / 点赞去重(Set) / 共同好友(SINTER) / 抽奖(SPOP)
- ✅ 底层编码一句话打通（ziplist/intset → hashtable）
- ⏳ 下一步：Day 4 ZSet（排行榜，跳表考点）
