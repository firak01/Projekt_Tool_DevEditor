@echo off
setlocal EnableDelayedExpansion

REM chatGPT Ergebnis 2026-01-29

REM --- Konfig laden ---
for /f "usebackq tokens=1,2 delims==" %%A in ("HIS_QISSERVER_FGL_paths.cfg") do (
    set %%A=%%B
)

REM --- Richtung wählen ---
if "%1"=="" (
    echo Usage: sync.bat toRepo ^| toProject
    exit /b 1
)

if /i "%1"=="toRepo" (
    set FROM=%SRC_BASE%
    set TO=%DST_BASE%
) else if /i "%1"=="toProject" (
    set FROM=%DST_BASE%
    set TO=%SRC_BASE%
) else (
    echo Unknown mode %1
    exit /b 1
)

REM --- Dateien synchronisieren ---
for /f "tokens=1,2 delims==" %%A in ('set MAP_') do (
    set REL=%%B
    echo Copying !REL!
    xcopy "!FROM!\!REL!" "!TO!\!REL!" /Y /I /R >nul
)

echo Done.
