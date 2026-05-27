@echo off
:: Pfad zur jgit jar datei
REM set JGIT_JAR=C:\Pfad\zu\ihrer\org.eclipse.jgit.pgm-7.0.0.202409031743-r.jar

REM "C:\java\eclipse-jee-oxygen-3a-win32-x86_64\eclipse\plugins\org.eclipse.jgit.archive_4.9.2.201712150930-r.jar"
REM Aber Fehlermeldung "es fehlt wohl manifest" set JGIT_JAR=C:\java\eclipse-jee-oxygen-3a-win32-x86_64\eclipse\plugins\org.eclipse.jgit.archive_4.9.2.201712150930-r.jar

REM also als Maven-Projekt, jar heruntergeladen
REM Gleicher Fehler set JGIT_JAR=C:\Users\fl86kyvo\.m2\repository\org\eclipse\jgit\org.eclipse.jgit\4.6.0.201612231935-r\org.eclipse.jgit-4.6.0.201612231935-r.jar

REM Also eigene .jar bauen und damit jgit.jar fernsteuern


:: JGit mit Java starten und alle Argumente (%*) übergeben
java -jar "%JGIT_JAR%" %*