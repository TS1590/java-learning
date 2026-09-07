# Day 33 · W6 Redis Day 4：ZSet 专敲（有序集合 · 排行榜神器）

> 2026-09-07（一）· 在 myredis 容器实测 ZADD/ZREVRANGE/ZINCRBY 全套 + 跳表考点入门

## 1. ZSet 是什么：Set + 分数排序

ZSet = **去重 + 每个成员带一个分数（score），Redis 按分数排好序**。

和 Set 的区别（一句话）：**Set 只管"有没有"，ZSet 还管"排第几"。**

| | Set | ZSet |
|---|---|---|
| 去重 | ✅ | ✅ |
| 存什么 | 成员 | 成员 + 分数 |
| 顺序 | 无序（SMEMBERS 顺序不保证） | **按分数排好序** |
| 典型场景 | 点赞名单、共同好友 | 排行榜、热搜榜、限时抢购倒计时 |

## 2. 命令实测

```bash
docker exec -it myredis redis-cli --raw

ZADD rank:game 1000 user1          # 加人 + 给分（分数在前，成员在后）
ZADD rank:game 800 user2 1200 user3 # 一次加多个
ZSCORE rank:game user1             # → 1000（查某人的分数）
ZCARD rank:game                    # → 3（总人数）
ZRANGE rank:game 0 -1 WITHSCORES   # 从低到高列出（升序）
ZREVRANGE rank:game 0 2 WITHSCORES # 从高到低取前 3（降序 = 排行榜！）
ZRANK rank:game user2              # → 0（升序排名，0 开始）
ZREVRANK rank:game user2           # → 2（降序排名：user2 800 分排第 3）
ZINCRBY rank:game 50 user1         # → 1050（分数 +50）
ZREM rank:game user2               # → 1（移除成员）
```

## 3. 排行榜取前三（核心场景）

```bash
ZREVRANGE rank:game 0 2 WITHSCORES
# → 分数最高的前 3 名 + 分数
```

> 记忆点：**REV = reverse 反转 = 降序**。升序用 ZRANGE，降序排行榜用 ZREVRANGE。
> 游戏排行榜、热搜榜、销量榜、直播间礼物榜……全是这一个套路。

## 4. ZINCRBY：加分 / 建档一条命令搞定 ⭐

```bash
ZINCRBY rank:game 200 user5   # user5 不存在 → 自动创建，从 0 起 +200
```

**暗藏考点**：ZINCRBY 对**不存在的键或成员会自动创建**（从 0 起加），原子完成"有则加分、无则建档"——不用先 ZADD 判断再加。

> 延续 INCR 家族的铁律：**单条命令原子**，加分场景别拆成"GET 分数 → 自己 + 200 → 再写回"。

## 5. 跳表 skip list（面试考点入门 ⭐）

**问题**：ZSet 要"按分数排序"，为什么用跳表？

**答案**：普通链表查第 N 个要一个个走 O(n)。跳表 = 给链表**多加几层"索引"**，高层一次跨好几个节点，查询/插入都是 **O(log n)**。

拿空间换时间：多存几层指针，换来"跳着走"的查找速度。

> 一句话面试版：**ZSet 底层 = 哈希表（存成员→分数映射）+ 跳表（按分数排序），查询插入 O(log n)。** 跳表细节进阶期再展开，今天先记住这个名字和"跳着走"的直觉。

## 6. 今日小测复盘

- Q1：**B ✅** `ZREVRANGE rank 0 9`（REV=降序=排行榜）
- Q2：⚠️ 首次答"跳表 O(log n)"= 答非所问（那是实现层）→ 巩固重答 ✅ "**Set 只管有没有，ZSet 还管排第几**"（功能层区别，面试答题层次要分清：先功能后实现）
- Q3：**✅** `ZINCRBY rank:game 200 user5`（不存在自动创建，从 0 起加）

## 7. 今日成就

- ✅ ZSet 9 命令实测（ZADD/ZSCORE/ZCARD/ZRANGE/ZREVRANGE/ZRANK/ZREVRANK/ZINCRBY/ZREM）
- ✅ 排行榜三连场景打通（ZREVRANGE 0 9 WITHSCORES）
- ✅ ZINCRBY"无则建档"考点
- ✅ Set vs ZSet 功能层区别一句话（有没有 vs 排第几）
- ✅ 跳表入门（多层索引跳着走，O(log n)，拿空间换时间）
- ⏳ 下一步：Day 5 缓存三兄弟（穿透/击穿/雪崩，面试必问）
