@echo off
rem ============================================================
rem  Start: run the built app with local JDK 17
rem  Set DEEPSEEK_API_KEY first (see env.example)
rem ============================================================
setlocal
cd /d "%~dp0"
set "JAVA_HOME=D:\jdk-17.0.20.1+1"
set "JAR=target\spring-ai-deepseek-demo-1.0.0.jar"

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] JDK 17 not found: %JAVA_HOME%
    exit /b 1
)
if not exist "%JAR%" (
    echo [ERROR] %JAR% not found. Run build.bat first.
    exit /b 1
)

rem Load DEEPSEEK_API_KEY from .env if present
if exist ".env" (
    for /f "usebackq tokens=1,* delims==" %%a in (".env") do set "%%a=%%b"
)
if "%DEEPSEEK_API_KEY%"=="" (
    echo [WARN] DEEPSEEK_API_KEY is empty. Model calls will return 401.
    echo        See env.example, or run: set DEEPSEEK_API_KEY=sk-xxxxxx
    echo        (you can create a .env in the project root)
    echo.
)

echo Using JDK: %JAVA_HOME%
"%JAVA_HOME%\bin\java" -jar "%JAR%"
endlocal
