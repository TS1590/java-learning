# MySQL 学习笔记 - Day 28（W5 Day 1 · MySQL 周开跑）

> 日期：2026-08-30
> 目标：装好 MySQL + 示例库跑起来，掌握基础连接与查询

## 一、环境信息

- MySQL 版本：**8.0.46**（MySQL Community Server，Win64）
- 服务名：`MySQL80`（运行中）
- 安装路径：`C:\Program Files\MySQL\MySQL Server 8.0\bin\`
- 该 bin 目录已加入用户 PATH → **新开终端**后可直接敲 `mysql`
- 登录账号：root / 123456
- 官方示例库：**world**（已导入）

## 二、登录方式

```bash
# 方式一：交互式（推荐，安全）
mysql -u root -p
# 然后输入密码 123456

# 方式二：密码直接写在命令里（仅学习用，生产环境严禁！）
mysql -u root -p123456
```

## 三、常用命令速查

```sql
SHOW DATABASES;                 -- 列出所有数据库
USE world;                      -- 切换到 world 库（必须先选库才能操作库里的表）
SHOW TABLES;                    -- 查看当前库的所有表
DESC city;                      -- 查看表结构（字段名/类型/是否主键）
SELECT * FROM city LIMIT 5;     -- 查 city 表前 5 行
exit;                           -- 退出
```

## 四、world 示例库信息

- 共 3 张表：`city`（城市）、`country`（国家）、`countrylanguage`（国家语言）
- `city` 表结构（5 列）：
  - ID（主键）
  - Name（城市名）
  - CountryCode（国家代码，外键关联 country）
  - District（省/州）
  - Population（人口数）
- 数据量：city 表 4079 行

## 五、今日测验与答案

- Q1（关系型数据库）：**B. MySQL**（Redis=键值库、Elasticsearch=搜索引擎、MongoDB=文档库）
- Q2（USE / SHOW TABLES 顺序）：先 `USE world` 切换库，再 `SHOW TABLES` 才能看到该库的表（"先选库再看表"）
- Q3（手写查询）：`SELECT Name, Population FROM city WHERE Population > 1000000;`
  - SELECT 选列 → FROM 定表 → WHERE 过滤行

## 六、Navicat 连接参数

- 连接名：随便填（如 mymysql）
- 主机：localhost
- 端口：3306
- 用户名：root
- 密码：123456

## 七、下一步

- Day 2：建库建表（CREATE DATABASE / CREATE TABLE / 数据类型）
- 每天 30min：造表、写 SQL、看 Explain
