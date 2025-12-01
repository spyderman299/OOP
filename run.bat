@echo off
REM Script to run Student Management System
REM This script uses JAVA_HOME if available, otherwise uses default JDK path

setlocal

set "JAVA_EXE="

if defined JAVA_HOME (
    set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_EXE=C:\Program Files\Java\jdk-21\bin\java.exe"
)

echo ========================================
echo Starting Student Management System...
echo ========================================
echo Using Java: %JAVA_EXE%
echo.

REM Check if out directory exists
if not exist out (
    echo ERROR: Compiled classes not found in 'out' directory!
    echo Please run compile.bat first to compile the application.
    echo.
    pause
    exit /b 1
)

REM Run the application
"%JAVA_EXE%" -cp "lib\*;out" Main

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo Application exited with error code: %ERRORLEVEL%
    echo.
)

pause

