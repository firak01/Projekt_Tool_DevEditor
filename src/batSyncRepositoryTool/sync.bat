@echo off
setlocal EnableDelayedExpansion

REM -------------------------------------------------
REM Usage:
REM   sync.bat toRepo|toProject SRC_BASE DST_BASE
REM
REM chatGPT 2026-01-29
REM -------------------------------------------------

if "%~3"=="" (
    echo Usage: sync.bat toRepo^|toProject SRC_BASE DST_BASE
    exit /b 1
)

set MODE=%~1
set SRC_BASE=%~2
set DST_BASE=%~3

REM --- Modus auswerten ---
if /i "%MODE%"=="toRepo" (
    set FROM=%SRC_BASE%
    set TO=%DST_BASE%
) else if /i "%MODE%"=="toProject" (
    set FROM=%DST_BASE%
    set TO=%SRC_BASE%
) else (
    echo Invalid mode: %MODE%
    exit /b 1
)

REM --- Existenz prüfen ---
if not exist "%FROM%" (
    echo Source base not found: %FROM%
    exit /b 1
)

if not exist "%TO%" (
    echo Target base not found: %TO%
    exit /b 1
)

REM --- Mappings laden ---
for /f "usebackq tokens=1,2 delims==" %%A in ("paths.cfg") do (
    if not "%%A"=="" (
        set %%A=%%B
    )
)

REM --- Synchronisation ---
for /f "tokens=1,2 delims==" %%A in ('set MAP_') do (
    set REL=%%B
    echo Copying !REL!
    xcopy "!FROM!\!REL!" "!TO!\!REL!" /Y /I /R >nul
)

echo Done.
endlocal
