# Day 30 · W6 Redis 开篇：环境搭建 + 第一课

> 2026-09-05（六）深夜打通 · 为打通 Docker 环境前后奋战 ~3 小时，复盘全记录

## 1. 本机环境最终形态（牢记！）

| 组件 | 版本 / 状态 | 说明 |
|---|---|---|
| Docker Desktop | v29.7.2 | 装在 `C:\Users\12412\AppData\Local\Programs\DockerDesktop`（用户目录，非 Program Files） |
| WSL2 | 2.7.13.0（内核 6.18.33.2-2） | 微软官方 MSI 手动安装版，比 Windows 自带的老版 WSL 强 |
| WSL 发行版 | docker-desktop（VERSION 2） | Docker 自动创建，我们不需要自己装 Ubuntu |
| Redis | redis:latest 容器 `myredis` | 端口 6379 映射到宿主机 |
| 火绒安全 | HipsDaemon 运行中 | 曾拦截"不受信任"的损坏安装包（教训见下） |

## 2. Docker 国内镜像加速（关键配置！已写好）

**问题**：Docker Hub 官方源（registry-1.docker.io）在中国直连超时
**解法**：给 `C:\Users\12412\.docker\daemon.json` 配置 registry-mirrors：

```json
{
  "experimental": false,
  "registry-mirrors": [
    "https://docker.m.daocloud.io",
    "https://docker.1ms.run",
    "https://hub.rat.dev",
    "https://docker.xuanyuan.me"
  ]
}
```

改完要重启 Docker Desktop 才生效。

## 3. Redis 每日必敲命令（Day 1 通关 ✅）

```bash
# 容器日常操作
docker ps                       # 看 myredis 在不在运行
docker start myredis            # 启动容器（重启电脑后容器是停的！要先 start）
docker exec -it myredis redis-cli   # 进入 redis 命令行（交互式）

# 核心命令（今天已全部敲通）
ping                → PONG        # 连通性测试
set name 张三        → OK          # 写一个键值对（key-value）
get name            → 张三         # 读出来（中文完美支持）
keys *              → name         # 列出所有 key（生产禁用！只用来学习）
exists name         → 1            # 判断 key 是否存在，1=在 0=不在
del name            → 1            # 删除 key，返回删除个数
```

## 4. Redis 5 种数据结构（先混脸熟，后面每天专攻一个）

| 结构 | 英文 | 底层长相 | 一句话记忆 | 例子 |
|---|---|---|---|---|
| 字符串 | String | `key → value` | 最简单，一个键一个值 | 验证码、计数器 |
| 列表 | List | `key → [a,b,c]` 有序可重复 | 排队 | 消息队列、最新列表 |
| 哈希 | Hash | `key → {字段:值}` | 像 Java 对象 | 用户信息、购物车 |
| 集合 | Set | `key → {a,b,c}` 无序不重复 | 去重神器 | 共同好友、签到 |
| 有序集合 | ZSet | `key → {成员:分数}` 按分数排序 | 排行榜专用 | 热度榜、积分榜 |

口诀：**S-L-H-S-Z → "顺序排"**（String 基础 / List 排队 / Hash 对象 / Set 去重 / ZSet 排序）

## 5. 今晚环境攻坚复盘（教训全是干货）

1. **沙箱无法弹 UAC 到用户桌面** → 提权操作只能引导用户在自己桌面双击/管理员 PowerShell
2. **下载大文件**：用系统 curl.exe 在后台跑，不要用 PowerShell Invoke-WebRequest（2 分钟被杀）
3. **不要用 `-C -` 断点续传**：断点偏移错位会污染文件（比官方多 16KB），签名全毁
4. **校验下载完整性**：不能只看文件大小 → 必须 `Get-AuthenticodeSignature` 看 Status=Valid；Event 1008"对象不受信任"= 文件损坏签名失效
5. **MSI 装不上先查事件日志**：1008=信任问题（文件坏）/ 11925=权限问题（要管理员）
6. **"Another installation is in progress"** = 残留 msiexec 互斥锁 → 等它自己结束或重启电脑
7. **REGDB_E_CLASSNOTREG**（装完新版 WSL 后 wsl 命令报错）= COM 组件未注册 → **重启电脑**即好

## 6. 今日成就

- ✅ WSL2 从零启用（dism 开功能 → 官方 MSI 装 2.7.13 → 重启激活）
- ✅ Docker Desktop 引擎跑通（WSL2 后端）
- ✅ 国内镜像加速器配置完成
- ✅ redis 容器运行 + 6 条核心命令全通
- ⏳ 未完成：5 种数据结构详敲（Day 2 起每天 30min 专攻一种）
