@echo off
REM ===============================================
REM Batch Datei zum Start von RegexReplaceTool
REM Nutzung:
REM   runReplace.bat <Pfad-zu-Datei-oder-Ordner>
REM Beispiel:
REM   runReplace.bat C:\meineProjekte\src
REM   runReplace.bat C:\meineProjekte\src\Beispiel.java
REM ===============================================

setlocal

REM Java-Klassenpfad setzen (aktuelles Verzeichnis)
set CP=../use/tool/dev/editor

REM Falls noch nicht kompiliert, kann man es hier einbauen:
REM javac RegexReplaceTool.java

if "%~1"=="" (
    echo Bitte einen Pfad angeben. Beispiel:
    echo runReplace.bat C:\meinProjekt\src
    goto :eof
)

java -cp %CP% RegexReplaceTool %1

endlocal
