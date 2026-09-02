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
