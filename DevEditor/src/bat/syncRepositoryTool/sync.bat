@echo off
setlocal EnableDelayedExpansion

REM -------------------------------------------------
REM Usage:
REM   sync.bat toRepo|toProject SRC_BASE DST_BASE
REM
REM chatGPT 20260129, 
REM         20260813 update mit "entferne doppelte Backslashe"
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
for /f "usebackq tokens=1,2 delims==" %%A in ("HIS_QISSERVER_FGL_paths.cfg") do (
    if not "%%A"=="" (
        set %%A=%%B
    )
)

REM --- Synchronisation ---
REM --- XCOPY bricht ab wenn die Datei noch nicht vorhanden ist, etc. . Darum /C hinzufügen... copy errors, continue
REM for /f "tokens=1,2 delims==" %%A in ('set MAP_') do (
REM    set REL=%%B
REM    echo Copying !REL!
REM    xcopy "!FROM!\!REL!" "!TO!\!REL!" /Y /I /R /C>nul
REM )

REM --- 20260708: Neuer robusterer Code
for /f "tokens=1,2 delims==" %%A in ('set MAP_') do (
    set REL=%%B

    REM --- Backslashes aus java.util.Properties normalisieren: \\ -> \
    set "REL=!REL:\\=\!"

    set SRC_FILE=!FROM!\!REL!
    set DST_FILE=!TO!\!REL!

    if exist "!SRC_FILE!" (
        echo [COPY ] !REL!

        for %%D in ("!DST_FILE!") do (
            if not exist "%%~dpD" mkdir "%%~dpD"
        )

        copy /Y "!SRC_FILE!" "!DST_FILE!" >nul
    ) else (
        echo [MISS ] !REL!
    )
)

echo Done.
endlocal
