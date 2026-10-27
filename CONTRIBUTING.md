# 贡献指南

感谢参与 Swap Doctor。请先阅读 README，了解本项目的 client-only 和 timing-only 边界。

## 开发环境

- Java 25 JDK（构建链要求）；源码目标版本为 Java 21
- Minecraft 1.21.11
- Fabric Loader 0.20.1+
- Fabric API 与 YACL 依赖由 Gradle 管理

## 提交前检查

```bash
./gradlew clean check
./gradlew build
```

涉及 HUD 或游戏内行为时，请同步更新 `docs/manual-smoke-test.md`，并说明尚未执行的真实游戏验证。

## 提交约定

- 提交信息使用简短、可读的 Conventional Commit 风格，例如 `feat: 增加 HUD 布局编辑器`、`fix: 修正跨 tick 匹配边界`。
- 每个 Pull Request 尽量只处理一个主题。
- 不要提交 `.gradle/`、`build/`、`run/`、`logs/`、IDE 配置或个人凭据。
- 不要把服务器端效果验证写成客户端 timing 结果。

## Pull Request

请填写 PR 模板，说明变更范围、测试命令、配置兼容性和手动验收证据。涉及用户可见文本时，同时更新中文和英文资源。
