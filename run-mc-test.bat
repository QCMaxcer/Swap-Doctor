@echo off
setlocal

cd /d "%~dp0"

rem Prefer the JDK used to verify Minecraft 1.21.11 + Fabric Loom.
if exist "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot\bin\java.exe" (
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot"
)

if not defined JAVA_HOME (
    where java >nul 2>nul
    if errorlevel 1 (
        echo [Swap Doctor] 未找到 Java。请安装 JDK 25，或手动设置 JAVA_HOME。
        pause
        exit /b 1
    )
)

echo [Swap Doctor] 启动 Minecraft 1.21.11 测试客户端...
echo [Swap Doctor] 关闭 Minecraft 后，此窗口会显示退出结果。
call "%~dp0gradlew.bat" runClient --no-daemon %*
set "EXIT_CODE=%ERRORLEVEL%"

if not "%EXIT_CODE%"=="0" (
    echo.
    echo [Swap Doctor] 测试客户端启动失败，退出码: %EXIT_CODE%
    pause
)

exit /b %EXIT_CODE%
