@echo on
@echo Einige Tests vorab
@echo 1) Version der Schell: %COMSPEC%
@echo 2) aktuelles Verzeichnis: %~dp0
for /f %%i in ('"echo test_auf_zerlegung_OK"') do @echo FOR /F funktioniert: %%i
pause



@echo off
REM Verwende dazu diese Zeile im Windows Link 
REM - angepasst an die tatsächliche Position des Repositories -
REM - analog zum Starter fuer das VMD Projekt: C:\1fgl\repository\Projekt_VMD\bat\4_starterFGL_copyLocal2Repository_CLIENT_ON_TOTILA.bat
REM - fuer diese Batch-Datei:
REM C:\1fgl\repo\EclipseOxygen_V02\Projekt_Tool_DevEditor\src\bat\eclipse\start\eclipse_starter_onDEV04.bat

REM Nur für diese Eclipse-Session gültig
REM Verwende den TRUSTSORE in Eclipse in der jeweils benoetigten .launch Datei, oder Eclipse selbst:   -Djavax.net.ssl.trustStore=${env_var:MY_TRUSTSTORE}
set MY_TRUSTSTORE=C:\java\jdk1.8.0\jre\lib\security\cacerts
set MY_PROJECTALIAS=DEV

REM Pfad zum lokalen Repository
set sRLZZZ=C:\1fgl\repo\EclipseOxygen_V02\Projekt_Kernel02_JAZDummy

REM Pfad zum remote Repository
set sRRZZZ=https://github.com/firak01/Projekt_Kernel02_JAZDummy.git


REM Zusaetzliche Umgebungsvariablen setzen. Dies passiert in der Batch, die nie ins gitHub Repository eingecheckt werden darf.
REM Verwende CALL statt START, damit der gleiche context verwendet wird und die Umgebungsvariablen von hier dort auch vorhande sind.
REM CALL ".\secret_starterZZZ.bat"
REM 
REM Wenn - wie hier gewünscht - in der Parent-Batch Umgebungsvariablen der Child-Batch entgegengenommen werden sollen, muss man so operieren.
REM Mehr ist in der entsprechenden Child-Batch dokumentiert
REM Nutze %~dp0 → das ist das Verzeichnis der aktuell laufenden Batchdatei.
for /f "delims=" %%i in ('"%~dp0secret_starterZZZ.bat"') do set %%i

@echo off
@echo Variable: %sPATZZZ%
REM PAUSE

REM Eclipse starten
@echo off
REM Zeile aus dem Windows Link: C:\java\eclipse-jee-oxygen\eclipse.exe -refresh -clean -data c:\1fgl\Workspace\EclipseOxygen_V02 -vm c:\java\jre8\bin\javaw.exe -clean  -Xms2048m -Xmx2048m -vmargs -Dfile.encoding=UTF-8 -Duser.name="Fritz Lindhauer"
start "" "C:\java\eclipse-jee-oxygen\eclipse.exe" -refresh -clean  ^
-data "c:\1fgl\Workspace\EclipseOxygen_V02"  ^
-vm "c:\java\jre8\bin\javaw.exe" -Xms2048m -Xmx2048m  ^
-vmargs -Dfile.encoding=UTF-8 -Djavax.net.ssl.trustStore="%MY_TRUSTSTORE%" ^
-Duser.name="Fritz Lindhauer"

@echo off
REM das konsolenfenster schliessen. Was wichtig ist, falls ein aufrufendes Programm auf das ende des Batch-Tasks wartet.
REM PAUSE
BREAK
exit