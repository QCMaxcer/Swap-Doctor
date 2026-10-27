# Swap Doctor

Swap Doctor 是一个面向 Minecraft 1.21.11 的 **客户端专用 Fabric 练习与诊断 Mod**，用于记录物品切换与攻击/使用输入之间的本地时间关系。

> **重要：** Swap Doctor 只报告客户端本地时序。一次“通过”表示本地观察到切换和鼠标输入处于同一客户端 tick，且间隔不超过配置窗口；它不确认伤害、附魔、生物属性、图腾消耗、数据包顺序或任何服务器端效果。

## 功能

- 记录快捷栏切换和主/副手交换（`F`）事件。
- 记录左键攻击与右键使用输入，支持切换先发生或输入先发生。
- 使用最近邻、一对一匹配，显示有符号的毫秒间隔。
- 提供数据 HUD 与无背景刻度 HUD，可分别开关、设置显示模式和淡出时长。
- 支持拖动布局、刻度宽度、长度/厚度倍率、正方向和小数毫秒显示。
- 离开游戏、失去焦点、死亡、断开连接或切换世界时清理待匹配事件和显示结果。
- 全部用户界面提供简体中文和英文翻译。

## 安装

1. 安装 Minecraft **1.21.11**、Fabric Loader **0.20.1 或更高版本**和对应的 Fabric API。
2. 安装运行时依赖 [YetAnotherConfigLib（YACL）](https://modrinth.com/mod/yacl)，版本至少为 `3.8.2+1.21.11-fabric`。
3. 将 Release 中的 `swap-doctor-1.0.0.jar` 放入实例的 `mods` 文件夹。
4. Mod Menu 是可选依赖；未安装 Mod Menu 时仍可使用默认 **K** 键打开设置。

本 Mod 的 `environment` 是 `client`，不需要也不提供服务端安装包。

## 游戏内使用

- 按 **K** 打开设置界面。
- 默认最大时间窗口为 **50 ms**，可设置为 **1–500 ms**。
- 数据 HUD 显示有符号的动作/切换间隔；刻度 HUD 将间隔映射到 `[-阈值,+阈值]`。
- 可分别设置两个 HUD 的持续显示/即时显示模式和淡出时长（**1–3000 ms**）。
- 可设置刻度宽度（**30–300**）、长度倍率与厚度倍率（**0.1x–1.0x**）。
- 可打开 HUD 布局编辑器拖动两个 HUD，位置会以归一化坐标保存。
- 默认关闭小数毫秒；打开后显示三位小数。

完整的人工验收矩阵见 [`docs/manual-smoke-test.md`](docs/manual-smoke-test.md)。

## 开发与构建

要求：Java 25 JDK 运行当前 Fabric Loom 构建链；项目代码仍使用 `--release 21` 编译，因此生成的 Mod 运行目标为 Java 21。

```bash
./gradlew clean check
./gradlew build
```

Windows PowerShell 或命令提示符可使用 `gradlew.bat`。开发运行客户端：

```bash
./gradlew runClient
```

构建产物位于 `build/libs/`。本项目使用官方 Mojang mappings、Fabric Loom 和 JUnit 5。

## 版本与发布

- 当前首个公开版本：`v1.0.0`
- 版本记录：[`CHANGELOG.md`](CHANGELOG.md)
- 贡献指南：[`CONTRIBUTING.md`](CONTRIBUTING.md)
- 安全问题：[`SECURITY.md`](SECURITY.md)

每个 Release 都会提供运行 JAR、可选的 sources JAR 以及 SHA-256 校验值。发布构建使用 GitHub Actions 自动执行 `./gradlew clean check build`。

## 许可证

本项目使用 [MIT License](LICENSE)。
