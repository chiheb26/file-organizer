@echo off
SETLOCAL ENABLEDELAYEDEXPANSION

REM === Configuration ===
SET JAR=target\file-organizer-1.0-SNAPSHOT.jar
SET MAIN=com.fileorganizer.Main
SET LOG_USED=false

REM === Check for folder path ===
IF "%~1"=="" (
    echo Usage: run-organizer.bat "C:\folder\to\organize" [--dry-run] [--log] [--delete-empty]
    exit /b
)

REM === Check if --log is present
FOR %%A IN (%*) DO (
    IF "%%A"=="--log" SET LOG_USED=true
)

echo [INFO] Building project...
call mvn clean package >nul
IF %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Build failed.
    exit /b
)

echo [INFO] Running File Organizer...
call java -cp %JAR% %MAIN% %*

IF "!LOG_USED!"=="true" (
    echo [INFO] Opening organizer.log...
    start notepad organizer.log
)

ENDLOCAL
