@echo off
REM #############################################################
REM ### Beispiel einer Batch, in der Umgebungsvariablen fuer eine aufrufende Batch bereitgestellt werden.
REM ### Ist Vorlage für die Batch secret_starterZZZ.bat, die nicht in GitHub eingecheckt werden darf,
REM ### was durch .gitignore zu loesen ist.
REM #############################################################

REM #################################
REM ### Wird diese Batch aus einer anderen Batch (mit CALL, nicht mit START) gestartet, dann bleiben die Umgebungsvariablen erhalten. 
REM ### Die hier gesetzten Umgebungsvariablen muessen in der StaterBatch allerdings in neue Umgebungsvariable mit %sPATZZZ% uebernommen werden
REM ### REM Nutze beim Aufruf %~dp0 → das ist das Verzeichnis der aktuell laufenden Batchdatei.
REM ### Das geht nur mit einem Trick, der im Gunde dieser Aufruf ist cmd /c child.bat - darum kann das Verzeichnis nicht einfach '.\' sein:
REM ### for /f "delims=" %%i in ('"%~dp0child.bat') do set %%i

REM #################################
REM ### Alles, was child.bat ausgibt, wird interpretiert.
REM ### Darum muss der Inhalt der Variablen hier ausgegeben werden mit echo und nicht mit set gesetzt werden.
REM ### Darum duerfen in dieser Batch keine ECHO Ausgaben passieren
REM ### Endweder ECHO nach stderr - echo Debug >&2 - oder komplett ausstellen oben mit @ECHO off: 
@echo STARTE example_starterZZZ.bat >&2

REM ### Personal Access Token von GitHub, die GitHubRegeln verbieten selbst, das commiten des PAT Strings
REM ### Wenn man es doch versucht, hat man das Problem den abgelehnten Commit wieder ruckgaengig zu machen
@echo on 
@echo sPATZZZ=der_geheime_personal_access_code

@echo off
@echo BEENDE example_starterZZZ.bat >&2