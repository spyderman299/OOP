@echo off
REM Script to compile Student Management System
REM This script uses JAVA_HOME if available, otherwise uses default JDK path

setlocal enabledelayedexpansion

set JAVAC_EXE=

if defined JAVA_HOME (
    set "JAVAC_EXE=%JAVA_HOME%\bin\javac.exe"
) else (
    set "JAVAC_EXE=C:\Program Files\Java\jdk-21\bin\javac.exe"
)

echo ========================================
echo Compiling Student Management System...
echo ========================================
echo Using Java Compiler: %JAVAC_EXE%
echo.

REM Create output directory
if not exist out mkdir out

REM Compile all Java files using PowerShell
echo Finding and compiling Java files...
powershell -NoProfile -Command "$files = Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { $_.FullName }; if ($files.Count -gt 0) { & '%JAVAC_EXE%' -cp 'lib\*;src' -d out $files } else { Write-Host 'No Java files found!' }"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo Compilation successful!
    echo ========================================
    echo Run the application using: run.bat
    echo.
) else (
    echo.
    echo ========================================
    echo Compilation failed!
    echo ========================================
    echo.
)

pause
