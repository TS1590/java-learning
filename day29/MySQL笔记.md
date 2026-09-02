# MySQL 学习笔记 - Day 29（W5 Day 5 · 存储引擎 + 索引入门）

> 日期：2026-09-02
> 目标：① 说得出 InnoDB 为什么是默认引擎 ② 理解索引为什么让查询变快 ③ 会写索引三命令
> 进度：Day 5 ✅（含 EXPLAIN 实测验证）

## 一、存储引擎是什么

- 存储引擎 = MySQL 底层"怎么存数据、怎么读写"的具体实现，建表时可指定
- 查看本机支持的引擎：`SHOW ENGINES;`（Support 列 DEFAULT = 默认引擎）
- 8.0 默认引擎：InnoDB

## 二、InnoDB vs MyISAM（面试必背）

| 特性 | InnoDB（默认） | MyISAM |
|------|---------------|--------|
| 事务 | ✅ 支持 | ❌ 不支持 |
| 行级锁 | ✅ 只锁相关行 | ❌ 只能锁整张表 |
| 外键 | ✅ 支持 | ❌ 不支持 |
| 崩溃恢复 | ✅ redo log 可恢复 | ❌ 宕机易损坏表 |

**面试一句话**：
> "因为业务表要写数据，而写数据需要三样东西：事务保证一致性、行锁扛住并发、崩溃恢复防丢数据——这三样只有 InnoDB 全都有，MyISAM 一样都没有，所以 MySQL 5.5.5 起默认引擎就是 InnoDB。"

- 场景串记：下单 =「扣库存 + 生成订单」，要么都成功要么都失败（事务）；两个人同时抢最后 1 件库存，InnoDB 只锁那 1 行（行锁）→ 都指向 InnoDB
- 自查：`SHOW VARIABLES LIKE 'default_storage_engine';`

## 三、索引为什么让查询变快（核心原理）

### 本质：索引 = 排好序的"书的目录"（B+ 树）

- **没索引**：全表扫描，一行一行翻（n 次比较），像翻一本没有目录的书
- **有索引**：走 B+ 树目录，每次比较按大小砍掉一大半，4~5 层就定位到目标行（log n 次）
- 4000 行和 400 万行，树只差 1~2 层 → 数据量翻百倍，查询速度几乎不变
- 定位到后还要**回表**取整行数据（叶子存的是行的位置/主键）

### 我的实测（city 表，同一条 SQL 加索引前后）

```sql
USE world;
EXPLAIN SELECT * FROM city WHERE Population = 1000000;  -- 无索引：rows = 4046（全表扫）
CREATE INDEX idx_pop ON city(Population);
EXPLAIN SELECT * FROM city WHERE Population = 1000000;  -- 有索引：rows = 1！
SHOW INDEX FROM city;
DROP INDEX idx_pop ON city;                             -- 演示完删掉
```

- rows = MySQL 预估要扫描的行数，数字越小越快
- **4046 → 1** = 索引生效的铁证（type 从 ALL 全表扫描 → ref 走索引）

### ⚠️ 索引的代价（面试加分点）

1. 占磁盘空间
2. 每次增删改都要同步维护目录树 → **写变慢**
- 所以只给"经常 WHERE / ORDER BY / JOIN 的列"建，不是越多越好

### 真实工作场景

线上接口突然从 0.1 秒变 3 秒 → 查慢 SQL 日志 → EXPLAIN 发现 80 万行表全表扫描（type=ALL, rows=80万）→ 给 WHERE 列加索引 → 回到 0.01 秒。**排查慢 SQL → 加索引**是后端最日常的性能优化（Day 6 EXPLAIN 实战就是这个）。

## 四、索引三命令（建 → 看 → 删）

```sql
-- 1. 建索引
CREATE INDEX 索引名 ON 表名(列名);
CREATE INDEX idx_name ON student(name);

-- 2. 看索引
SHOW INDEX FROM student;
-- 重点看 3 列：Key_name(索引名) / Column_name(哪列) / Non_unique(0=唯一 1=可重复)

-- 3. 删索引
DROP INDEX 索引名 ON 表名;
DROP INDEX idx_name ON student;
```

口诀：**建 = CREATE INDEX 名 ON 表(列)，看 = SHOW INDEX FROM 表，删 = DROP INDEX 名 ON 表**——都以 INDEX 为锚点。

### 三种索引类型

| 类型 | 写法 | 特点 | 场景 |
|------|------|------|------|
| 主键索引 | 建表时 PRIMARY KEY | 唯一 + 非空，自带 | id |
| 唯一索引 | CREATE UNIQUE INDEX | 列值不能重复 | 手机号/邮箱注册 |
| 普通索引 | CREATE INDEX | 只提速，允许重复 | 经常 WHERE 的列 |

## 五、今日测验与答案

- Q1（引擎选型：订单表要事务+行锁）：**B. InnoDB**（MyISAM 无事务、锁整表）
- Q2（为什么索引让查询变快）：
  > "因为索引是排好序的目录结构，MySQL 按大小逐层缩小查找范围，把全表扫描的 n 次比较降成 log n 次，所以查询变快。"
- Q3（索引三命令）：见上第四节 ✅

## 六、口诀汇总

- InnoDB = 事务 + 行锁 + 外键 + 崩溃恢复，全都要所以是默认
- 索引 = 书的目录，逐层排除一大片，n 次比较变 log n 次
- EXPLAIN 的 rows = 预估要扫多少行，4046 → 1 就是索引生效的证据
- 建索引有代价：占空间 + 写变慢，只给常查的列建

## 七、下一步

- Day 6：EXPLAIN 执行计划实战（完整读懂 type/key/rows 各列）+ 索引深入

---

# MySQL 学习笔记 - Day 29（W5 Day 6 · EXPLAIN 执行计划实战）

> 日期：2026-09-02
> 目标：① 会读 EXPLAIN 每一列 ② 一眼看出 SQL 走没走索引 ③ 掌握 3 种索引失效写法
> 进度：Day 6 ✅

## 一、EXPLAIN 怎么用

```sql
EXPLAIN SELECT * FROM city WHERE Population = 1000000;      -- 不真查，输出执行计划
EXPLAIN SELECT * FROM city WHERE Population = 1000000\G      -- 竖排，一字段一行
```

**读报告三步法**：① 先看 type（怎么查的）→ ② 再看 key（用没用索引）→ ③ 最后看 rows + Extra（翻多少行、额外动作）

## 二、type 七级（面试必默写）

```
system > const > eq_ref > ref > range > index > ALL
（左快右慢）
```

| 等级 | 白话 | 出现场景 |
|------|------|---------|
| const | 主键/唯一键精确命中，只查 1 行 | WHERE id = 1 |
| eq_ref | 联表时主键一一对应 | JOIN 连接条件 |
| ref | 普通索引等值命中 | WHERE Population = 1000000 |
| range | 索引上范围查找 | BETWEEN / > / < |
| index | 把整棵索引树翻一遍 | 索引列只被 ORDER BY |
| ALL | 全表扫描，一行行翻 | 没索引 / 索引失效 |

**两个警报**：看到 `ALL`（最差）或 `index`（次差）= 该加索引了。

## 三、possible_keys vs key

- possible_keys = 候选名单；key = 实际用上的
- **key=NULL 且 type=ALL = 这条 SQL 没走索引**（没建 or 索引失效）

## 四、rows 的含义

- rows = MySQL 预估要扫描多少行，数字越小越快
- 我实测：建索引前 rows=4046（ALL）→ 建 idx_pop 后 rows=1（ref）
- 面试报真实数字：4046 次比较 → 1 次 = O(n) 变 O(log n) 的铁证

## 五、Extra 列

| Extra | 意思 | 评价 |
|-------|------|------|
| Using index | 覆盖索引，免回表 | 🟢 最好 |
| Using where | 索引定位后再过滤 | 🟡 正常 |
| Using filesort | 排序没走索引，额外排一次 | 🔴 大表要优化 |
| Using temporary | 用了临时表（GROUP BY 常见） | 🔴 留意 |

## 六、回表 vs 覆盖索引

- 普通索引叶子只存（索引列 + 主键 id）→ SELECT * 拿 id 回主键索引树再查一次 = **回表**（查 2 棵树）
- 查的列索引叶子全都有 = **覆盖索引**，Extra 显示 Using index（查 1 棵树）
- 面试一句话："覆盖索引 = 查询的列都包含在索引里，不用回表"
- 实战：高频查询 SELECT * 改成只查需要的列，建联合索引覆盖它

## 七、索引失效 3 大坑（面试连环问）

**坑 1：LIKE 前置通配符**
```sql
SELECT * FROM student WHERE name LIKE '%张%';   -- ❌ 不知道开头 → 索引失效
SELECT * FROM student WHERE name LIKE '张%';    -- ✅ 知道开头 → 走索引
-- 口诀：通配符在前 = 索引完蛋（目录按开头字母排的）
```

**坑 2：对索引列做函数/运算**
```sql
EXPLAIN SELECT * FROM city WHERE Population + 1 = 1000001;  -- ❌ type=ALL（实测验证！）
EXPLAIN SELECT * FROM city WHERE Population = 1000000;      -- ✅ type=ref（运算挪到另一边）
```

**坑 3：隐式类型转换**
```sql
SELECT * FROM user WHERE phone = 13800138000;    -- ❌ 数字 vs VARCHAR → 列被偷偷转换 = 函数
SELECT * FROM user WHERE phone = '13800138000';  -- ✅ 字符串，和列类型一致
```

> 口诀：**让索引列"裸奔"**——别套函数、别做运算、类型要和列一致。

## 八、我的 5 个 EXPLAIN 案例实测记录

```sql
USE world;
-- city 表现有索引：PRIMARY(id)、idx_pop(临时建)
EXPLAIN SELECT * FROM city WHERE ID = 1;                          -- type=const, key=PRIMARY, rows=1
EXPLAIN SELECT * FROM city WHERE Population = 1000000;            -- type=ref,   key=idx_pop, rows=1
EXPLAIN SELECT * FROM city WHERE Population BETWEEN 1000000 AND 2000000;  -- type=range, key=idx_pop
EXPLAIN SELECT * FROM city WHERE Name LIKE '%zhou%';              -- type=ALL（前置通配符失效）
EXPLAIN SELECT * FROM city ORDER BY Population;                   -- 可能 type=index / Extra=filesort
-- 挑战：索引失效 + 修复对比
EXPLAIN SELECT * FROM city WHERE Population + 1 = 1000001;        -- ❌ type=ALL（函数包列）
EXPLAIN SELECT * FROM city WHERE Population = 1000000;            -- ✅ type=ref（列裸奔）
```

## 九、慢 SQL 排查 SOP（后端真功夫）

```
收到"接口慢"告警 → 1. 捞慢查询日志 → 2. EXPLAIN：type=ALL, key=NULL, rows=80万
→ 3. 判断：没建索引 or 索引失效（LIKE/函数/类型） → 4. 加索引 or 改写 SQL
→ 5. 再 EXPLAIN：type 变 ref/range, rows 掉下来 → 收工
```

简历可写："参与慢 SQL 优化，用 EXPLAIN 定位全表扫描并加索引，查询耗时从 X 降到 Y"

## 十、今日测验与答案

- Q1（最危险 type）：**D. ALL**（全表扫描，A const 最快 / B range / C ref 都走索引）
- Q2（key=NULL + type=ALL 说明什么）：说明这条 SQL **没走任何索引在做全表扫描**；修复 = 给 WHERE 列建索引（或检查是否索引失效，先 EXPLAIN 验证再动手）
- Q3（`LIKE '%张%'`）：**不能用索引**——前置通配符不知道开头是什么，目录没法按区间查；修复 `LIKE '张%'`（真需要模糊搜中间 → 全文索引/ES 另说）

## 十一、口诀汇总

- type 从快到慢：const → ref → range → index → ALL；看到 ALL/index 就警惕
- 读 EXPLAIN 三步：type → key → rows/Extra
- key=NULL + ALL = 没走索引；rows 越小越快
- 索引失效三坑：LIKE 前置 % / 函数运算包列 / 隐式类型转换 → **让索引列裸奔**

## 下一步

- Day 7：MySQL 事务与隔离级别（ACID / 四种隔离级别 / 脏读·不可重复读·幻读）→ W5 收官
- W5 复盘：自测标准含 MVCC 的 ReadView、RR 如何解决幻读（Day 7 后安排）
