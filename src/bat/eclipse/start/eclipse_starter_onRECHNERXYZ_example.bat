@echo on
@echo Einige Tests vorab
@echo 1) Version der Shell: %COMSPEC%
@echo 2) aktuelles Verzeichnis: %~dp0
for /f %%i in ('"echo test_auf_zerlegung_OK"') do @echo FOR /F funktioniert? %%i
pause



@echo off
REM ####################################################################
REM ### Denkbar sind verschiedenen Startmechanismen
REM ####################################################################
REM A) Verwende eine Zeile im Windows Link 
REM - angepasst an die tatsächliche Position des Repositories -
REM - analog zum Starter fuer das VMD Projekt: C:\1fgl\repository\Projekt_VMD\bat\4_starterFGL_copyLocal2Repository_CLIENT_ON_TOTILA.bat
REM - fuer diese Batch-Datei:
REM C:\Workspace\EclipseOxygenWorkspace\Projekt_Tool_DevEditor\src\bat\eclipse\start\eclipse_starter_onTUBAF.bat

REM B) START PER KOMMANDOZEILE
REM - Nachteil: Dann müssen alle Umgebungsvariablen (auch die geheimen) darein.
REM C:\Windows\System32\cmd.exe /c "set sPATZZZ=abcxblabla && "C:\java\eclipse-jee-oxygen-3a-win32-x86_64\eclipse\eclipse.exe" -data C:\Workspace\EclipseOxygenWorkspace -vmargs -Xms2048m -Xmx2048m -Dfile.encoding=UTF-8  -vm"

REM C) START PER BATCH
REM - Übergib die geheimen Daten per extra Batch, die nicht ins GIT-Repository committet werden darf
REM - Hierfür auch extra ein Mechanismus entwickelt auf die Umgebungsvariablen in der Launch-Konfiguration zuzugreifen
REM   und über die Kernel ConfigZZZ-Klassen im Java zu nutzen
REM - Beispiel für eine .launch Konfiguration, die dann die Umgebungsvariablen nutzen kann
REM   -push -https -pat "$.{sPATZZZ#PAT per Umgebungsvariable fehlt}"
REM   -rr "$.{sRRZZZ#RepositoryRemote per Umgebungsvariable fehlt}"
REM   -rl "$.{sRLZZZ#RepositoryLocal per Umgebungsvariable fehlt}"
REM
REM   notwendige Flags würden dann so (noch direkt) gesetzt, z.B. für einen pull..
REM   -z {"IGNORE_CHECKOUT_CONFLICTS":true}
REM
REM ##################################################################
REM ### Umgebungsvariabeln sind nur für diese Eclipse-Session gültig
REM ##################################################################

REM Pfad zu Eclipse, den zu verwendenden Workspace und die Java VM 
set MY_ECLIPSE=C:\java\eclipse-jee-oxygen-3a-win32-x86_64\eclipse\eclipse.exe
set MY_DATA=C:\Workspace\EclipseOxygenWorkspace
set MY_JAVA=c:\java\jre8\bin\javaw.exe

REM Verwende den TRUSTSORE in Eclipse in der jeweils benoetigten .launch Datei, oder Eclipse selbst:   -Djavax.net.ssl.trustStore=${env_var:MY_TRUSTSTORE}
set MY_TRUSTSTORE=C:\java\jdk1.8.0_202\jre\lib\security\cacerts

REM TODOGOON: Noch ncht genutzters DEFAULT-Projektalias... vielleicht für die Ermittlung der Java-Development-Tools 
set MY_PROJECTALIAS=DEV

REM Pfad zum lokalen Repository
set sRLZZZ=C:\HIS-Workspace\1fgl\repo\EclipseOxygen\Projekt_Kernel02_JAZDummy

REM Pfad zum remote Repository
set sRRZZZ=https://github.com/firak01/Projekt_Kernel02_JAZDummy.git

REM Zusaetzliche Umgebungsvariablen setzen. Dies passiert in der Batch, die nie ins gitHub Repository eingecheckt werden darf.
REM Prinzipiell denkbar sind folgende Ansätze
REM 1. Verwende CALL statt START, damit der gleiche context verwendet wird und die Umgebungsvariablen von hier dort auch vorhande sind.
REM CALL ".\secret_starterZZZ.bat"
REM 
REM 2. Gewählte Lösung:
REM Wenn die Parent-Batch eine Child-Batch aufruft und
REM in der Parent-Batch die Umgebungsvariablen der Child-Batch entgegengenommen werden sollen, muss man so vorgehen:
REM Mehr ist in der entsprechenden Child-Batch dokumentiert
REM Nutze %~dp0 → das ist das Verzeichnis der aktuell laufenden Batchdatei.
for /f "delims=" %%i in ('"%~dp0secret_starterZZZ.bat"') do set %%i

@echo off
@echo Variable: %sPATZZZ%
REM PAUSE

REM Eclipse starten
@echo off
REM Zeile aus dem Windows Link: C:\java\eclipse-jee-oxygen-3a-win32-x86_64\eclipse\eclipse.exe -refresh -clean -data C:\Workspace\EclipseOxygenWorkspace -vm c:\java\jre8\bin\javaw.exe -clean  -Xms2048m -Xmx2048m -vmargs -Dfile.encoding=UTF-8 -Duser.name="Fritz Lindhauer"

start "" "%MY_ECLIPSE%" -refresh -clean  ^
-data "%MY_DATA%"  ^
-vm "%MY_JAVA%" -Xms2048m -Xmx2048m  ^
-vmargs -Dfile.encoding=UTF-8 -Djavax.net.ssl.trustStore="%MY_TRUSTSTORE%" ^
-Duser.name="Fritz Lindhauer"

@echo off
REM das konsolenfenster schliessen. Was wichtig ist, falls ein aufrufendes Programm auf das Ende des Batch-Tasks wartet.
REM PAUSE
BREAK
exit