# MySQL 学习笔记 - Day 28-31（W5 Day 1-3 · MySQL 周开跑）

> 日期：2026-08-30 ~ 2026-08-31
> 目标：装好 MySQL + 建库建表 + CRUD 增删改查完整闭环
> 进度：Day 1（环境）✅ Day 2（建库建表）✅ Day 3（CRUD）✅

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

## 七、Day 2：建库建表 + 常用数据类型

### 建库建表示例（school 库 student 表）

```sql
-- 建库：utf8mb4 支持中文和 emoji（乱码 90% 是字符集问题）
CREATE DATABASE IF NOT EXISTS school DEFAULT CHARACTER SET utf8mb4;
USE school;

-- 建表
CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT COMMENT '主键，自增',
    name VARCHAR(20) NOT NULL COMMENT '姓名',
    age INT COMMENT '年龄',
    score DECIMAL(5,2) COMMENT '成绩，保留2位小数',
    birthday DATE COMMENT '出生日期',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 插入
INSERT INTO student (name, age, score, birthday) VALUES ('张三', 20, 88.5, '2006-03-15');
```

### 常用数据类型（后端必背）

| 类型 | 用途 | 例子 |
|------|------|------|
| INT | 整数 | 年龄、数量、库存 |
| VARCHAR(n) | 变长字符串（n=最长字符数） | 姓名、标题 |
| DECIMAL(m,n) | 精确小数（共 m 位，n 位小数） | **金额必须用它！** |
| DATE | 日期 | 生日 2006-03-15 |
| DATETIME | 日期+时间 | 创建时间 |

### 关键概念

- **PRIMARY KEY 主键**：每行的唯一身份证，增删改查靠它定位
- **AUTO_INCREMENT 自增**：主键自动编号（1,2,3...），不用自己填，不冲突
- **NOT NULL**：该列不能为空
- ⚠️ **金额一律 DECIMAL 不用 DOUBLE**：浮点有精度误差（0.1+0.2=0.30000000000000004），DOUBLE 存钱=算错账
- 后端建表标配 = id 主键 + created_at 创建时间

### UPDATE 更新数据

```sql
ALTER TABLE student ADD email VARCHAR(50);          -- 加一列
UPDATE student SET email = 'zhangsan@qq.com' WHERE id = 1;  -- 改数据
-- ⚠️ 不带 WHERE = 全表都改成这个值 = 生产事故！
```

### 踩坑记录：SQL 1064 语法错误

- 症状：`Error 1064 ... near 'INSERT INTO ... VALUES ('李四'...'`
- 原因：从微信/文档复制 SQL 时，**中文字符串的引号变成了中文全角引号**（' 变 '），MySQL 只认英文引号
- 排查：DBeaver 逐条执行（Ctrl+Enter 只执行选中语句）+ 看字符串引号颜色 + 手敲一遍
- 教训：复制 SQL 后检查引号，或直接手敲

## 八、Day 3：CRUD 增删改查完整闭环

### C 增（INSERT）

```sql
-- 一次插多行
INSERT INTO goods (name, price, stock) VALUES
('iPhone 17', 6999.00, 100),
('机械键盘', 399.00, 50);
```

### R 查（SELECT 五种姿势）

```sql
SELECT * FROM goods;                              -- 全查
SELECT name, price FROM goods WHERE price > 500;   -- 条件查
SELECT * FROM goods ORDER BY price DESC;          -- 降序 DESC / 升序 ASC
SELECT * FROM goods LIMIT 2;                      -- 前 2 条
SELECT * FROM goods WHERE name LIKE '%键盘%';      -- 模糊查询
```

### U 改（UPDATE）

```sql
UPDATE goods SET stock = stock - 1 WHERE id = 1;  -- 卖出一件，库存减 1
UPDATE goods SET price = 349.00 WHERE name = '机械键盘';
```

### D 删（DELETE）

```sql
DELETE FROM goods WHERE id = 3;
-- ⚠️ 不带 WHERE 删全表！生产事故
```

### 分页查询（面试必考）

```sql
-- LIMIT 跳过条数, 取几条
SELECT * FROM goods ORDER BY id LIMIT 0,2;   -- 第 1 页（第 1-2 条）
SELECT * FROM goods ORDER BY id LIMIT 2,2;   -- 第 2 页（第 3-4 条）
-- 公式：第 n 页 = LIMIT (n-1)*每页条数, 每页条数
```

### 口诀

- CRUD = 增删改查，后端一切接口的本质
- **改删必带 WHERE**，不然全表遭殃
- 金额 DECIMAL，浮点会翻车

## 九、下一步

- Day 4：WHERE 进阶（AND/OR/IN/BETWEEN）+ 聚合函数（COUNT/SUM/AVG/MAX/MIN）+ GROUP BY
- 每天 30min：造表、写 SQL、看 Explain
