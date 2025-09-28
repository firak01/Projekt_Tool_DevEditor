@echo off
REM ===============================================
REM Batch Datei zum Start von RegexReplaceTool
REM Nutzung:
REM   starter_RegexReplaceTool.bat <Pfad-zu-Datei-oder-Ordner>
REM Beispiel:
REM   starter_RegexReplaceTool.bat C:\meineProjekte\src
REM   starter_RegexReplaceTool.bat C:\meineProjekte\src\Beispiel.java
REM ===============================================

setlocal

REM Basisverzeichnis für compilierten Code
REM ..\ = gehe von bin\bat eine Ebene höher nach bin\
set CP=%~dp0..\
echo Classpath=%CP%

REM Klassenname inkl. Package-Pfad
REM Beispiel: folder1.folder1_1.folder1_1_1.RegexReplaceTool
REM (angepasst an deine tatsächliche Package-Deklaration!)
set MAINCLASS=use/tool/dev/editor/RegexReplaceTool

if "%~1"=="" (
    echo Bitte einen Pfad angeben. Beispiel:
    echo starter_RegexReplaceTool.bat C:\meinProjekt\src
    goto :eof
)

java -cp %CP% %MAINCLASS% %1

endlocal
