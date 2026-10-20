@echo off
rem ============================================================
rem  Build: use local JDK 17 + Maven 3.9
rem  Output: target\spring-ai-deepseek-demo-1.0.0.jar
rem ============================================================
setlocal
cd /d "%~dp0"
set "JAVA_HOME=D:\jdk-17.0.20.1+1"
set "M3=%USERPROFILE%\maven\apache-maven-3.9.11\bin\mvn"

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] JDK 17 not found: %JAVA_HOME%
    exit /b 1
)
if not exist "%M3%.cmd" (
    echo [ERROR] Maven 3.9 not found: %M3%
    exit /b 1
)

echo Using JDK: %JAVA_HOME%
"%JAVA_HOME%\bin\java" -version 2>&1
echo.

"%M3%" -B clean package
set "RC=%ERRORLEVEL%"
if "%RC%"=="0" (
    echo.
    echo BUILD OK. Run start.bat to launch.
) else (
    echo.
    echo BUILD FAILED, code %RC%
)
endlocal
exit /b %RC%
