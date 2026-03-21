package use.tool.dev.jgit.ssh;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.transport.SshSessionFactory;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.dateTime.DateTimeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import use.tool.dev.jgit.AbstractJgitStarter;
import use.tool.dev.jgit.JgitStarterMain;
import use.tool.dev.jgit.https.JgitStarterHTTPS;



public class JgitStarterSSH extends AbstractJgitStarter implements IJgitStarterSSH{
	
	@Override
	public boolean startit() throws ExceptionZZZ {	
		try {
			
			//Konfiguriere JGit für SSH
			//+++ Zugriff sicherstellen
			JGitSshConfigZZZ.configure();
			System.out.println("Verwendete SSH Session Factory: " + SshSessionFactory.getInstance().getClass());
				
			//Zwei verschiedene lokale Repos, je nachdem welches Eclipse
			//A) auf TUBAF - HISinOne Eclipse:
			//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
			//B) auf TUBAF (Oxygen Version) für Z-Kernel Entwicklung
			//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\HIS_QISSERVER_FGL");
			String sDirectoryRepositoryLocal = this.getRepositoryLocal();
			if(StringZZZ.isEmpty(sDirectoryRepositoryLocal)) {
				ExceptionZZZ ez = new ExceptionZZZ("Lokales Repository Verzeichnis, Angabe fehlt: '" + sDirectoryRepositoryLocal + "'", iERROR_PARAMETER_MISSING, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			File objFileDir = new File(sDirectoryRepositoryLocal);
			if(!objFileDir.exists()) {
				ExceptionZZZ ez = new ExceptionZZZ("Lokales Repository Verzeichnis existiert nicht: '" + sDirectoryRepositoryLocal + "'", iERROR_PARAMETER_VALUE, this, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;				
			}
			//Auf Ermanarich, der HISinOne Tomcat
			//File objFileDir = new File("C:\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
			
			//Zur Entwicklung (auf DEV04), ein Dummy Verzeichnis
			//File objFileDir = new File("C:\\1fgl\\repo\\EclipseOxygen_V01\\Projekt_Kernel02_JAZDummy"); 
			
			//Zur Entwicklung (auf ERMANARICH), ein Dummy Verzeichnis
			//File objFileDir = new File("C:\\1fgl\\repo\\EclipseOxygen\\Projekt_Kernel02_JAZDummy");
	
			
			//Trotz Einbinden von  in pom.xml Fehlermeldung;
			//ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console
			//Lösung dazu:
			//https://stackoverflow.com/questions/47881821/error-statuslogger-log4j2-could-not-find-a-logging-implementation
			//TODOGOON20260310;//jetzt wird eine logdatei all.log im Root des Projektordners angelegt. Das ist schlecht/unnoetig für GIT. Dort weg.
			System.setProperty("log4j.configurationFile","./use/tool/dev/jgit/log/log4j2.xml");
			
			//Logger log = LogManager.getLogger(this.getClass().getName());		
			Logger log = LogManager.getLogger();
			
			InitCommand gitCommandInit = Git.init();
			gitCommandInit.setDirectory(objFileDir);
			
			Git git = gitCommandInit.call(); //Merke: damit das funktioniert muss der Pfad zu git.exe in der PATH Umgebungsvariablen sein. Z.B. c:\Progamme\Git\bin
			this.setGitObject(git);
			System.out.println("Git-Repository init done: " + objFileDir.getAbsolutePath());
			
			//+++ Hole die URL vom Remote Repository
			//TODOGOON20260320;//Plausibilitaet: Prüfe, ob https oder ssh in der .git\config Datei steht
			String sRepositoryRemote = this.getRepositoryRemote();
			if(StringZZZ.isEmpty(sRepositoryRemote)) {
				String sRepositoryRemoteAlias = this.getRepositoryRemoteAlias();
				if(StringZZZ.isEmpty(sRepositoryRemoteAlias)){
					ExceptionZZZ ez = new ExceptionZZZ("Alias vom Remote Repository", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
			}
			if(StringZZZ.isEmpty(sRepositoryRemote)) {
				ExceptionZZZ ez = new ExceptionZZZ("Weder Url direkt angegeben noch per Alias '" + sRepositoryRemoteAlias + "' ermittelbar.", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			this.setRepositoryRemote(sRepositoryRemote);
			
			//+++ SSL Zugriff sicherstellen
			//Merke: Das Vorhandensein der notwendigen Dateien in .ssh wird vorausgesetzt
			//
			//+++++++++++++++++++++++++++++++++		
			
			System.out.println("STATUS BEFORE COMMIT");		
			this.printStatus(git);
	        //##################################################################
	        
			//Fuege geänderte Dateien, die schon im Repository sind, hinzu.
			this.addFileTrackedChanged(git);
									
	        //Mache einen commit (mit aktuellem Datum/Uhrzeit)
			long lTimestamp = DateTimeZZZ.computeTimestamp();
			SimpleDateFormat dateFormater = new SimpleDateFormat("dd-MM-yyyy_H:m");		
			String sDateFormated = dateFormater.format(lTimestamp);
	
			
			CommitCommand gitCommandCommit = git.commit();
			gitCommandCommit.setMessage(sDateFormated + " - Commit by Java-Class from a module of Projekt_Tool_DevEditor");
			gitCommandCommit.call();
	        //System.out.println("Committed file " + myFile + " to repository at " + git.getRepository().getDirectory());
	        
	        System.out.println("STATUS AFTER COMMIT");
	        this.printStatus(git);
			
	        //Fuege neue Dateien hinzu, die noch nicht im Repository sind.
	        //TODOGOON20260313
	        
	        //Mache den push	
	        this.pushit(git);
	       
	        System.out.println("STATUS AFTER PUSH");
	        this.printStatus(git);
	       
	        
	        //s. ChatGPT vom 20260313
	        //Problem: Eclipse "registriert/bemerkt" den Push nicht (also Pfeil nach oben mit 1 dahinter wird angezeigt).
	        //Damit in Eclipse auch der Push "registriert/bemerkt wird" muss noch ein Fetch gemacht werden.
	        //Der letzte fetch() sorgt dafür, dass lokale Remote-Tracking-Branches synchron bleiben, 
	        //was besonders hilfreich ist, wenn gleichzeitig ein Tool wie Eclipse auf das gleiche Repository schaut.
	        
	        
	        //aber manchmal ist nichts zu fetchen, darum Fehler abfangen     
//			Git git4Fetch = Git.open(objFileDir); 
//			System.out.println("Git-Repository 4 Fetch repository opened.");
//				
//	    	FetchCommand gitCommandFetch = git4Fetch.fetch();
//	    	gitCommandFetch.setRemote(sRepositoryRemoteAlias); //Laut chat gpt nicht die URL, da die Remote Daten schon im .git/config stehen
//	    	gitCommandFetch.call();
	        
	        JgitStarterHTTPS.fetchIgnoreNothingToFetch(objFileDir, sRepositoryRemote);
		    System.out.println(("FETCH DONE"));
		  		    	
        //###############################################################	  
		}catch(TransportException tex) {
			ExceptionZZZ ez = new ExceptionZZZ(tex);
			throw ez;	
		}catch(IllegalStateException ie) {
			ExceptionZZZ ez = new ExceptionZZZ(ie);
			throw ez;
		}catch(GitAPIException gae) {
			ExceptionZZZ ez = new ExceptionZZZ(gae);
			throw ez;
		}
		return true;
	}
	
	
	
	public void addFileTrackedChanged(Git git) throws NoWorkTreeException, GitAPIException {
		
		StatusCommand gitCommandStatus = git.status();
		Status status = gitCommandStatus.call();

		Set<String> uncommittedChanges = status.getUncommittedChanges();
		Set<String> untracked          = status.getUntracked();
		ArrayList<String> listasUncommitedChanges = new ArrayList<String>();
		
		AddCommand gitCommandAdd = git.add();		
        for (String uncommitted : uncommittedChanges) {
        	if(!untracked.contains(uncommitted)) {
        		listasUncommitedChanges.add(uncommitted);
        	}
        }
        
        // run the add-call 
        for(String uncommitted : listasUncommitedChanges) {
        	System.out.println("uncommitted to add: '" + uncommitted + "'");
        	try {
        		gitCommandAdd.addFilepattern(uncommitted);
        		gitCommandAdd.call();
        	}catch(java.lang.IllegalStateException isex) {
        		System.out.println(isex.getMessage());
        	}
        }
       
	}
	
	public void addFileUntracked(Git git) throws NoWorkTreeException, GitAPIException {
		Status status = git.status().call();

		Set<String> setUntracked = status.getUntracked();
		ArrayList<String> listasUntracked = new ArrayList<String>();
        for (String  sUntracked : setUntracked ) {
        	listasUntracked.add(sUntracked);
        }
        
        for(String sUntracked : listasUntracked) {
        	git.add().addFilepattern(sUntracked).call();
        }
	
	}
	
	
	
	public void pushit(Git git) throws ExceptionZZZ {
			
		try {
		//wg. Authentifizierung: Ausgabe der verwendeten SessionFactory - Klasse... ist das auch meine?
		System.out.println(SshSessionFactory.getInstance().getClass());
		
		// aber mal explizit als pushCommand
		PushCommand pushCommand = git.push();
		
		String sRemoteRepositoryAlias = this.getRepositoryRemoteAlias();
		pushCommand.setRemote(sRemoteRepositoryAlias);

		System.setProperty("https.protocols", "TLSv1.2"); 
		
		// push to remote:	
		pushCommand.call();
		
		//###############################################################
		}catch(InvalidRemoteException ire) {
			ExceptionZZZ ez = new ExceptionZZZ(ire);
			throw ez;
		}catch(TransportException te) {
			ExceptionZZZ ez = new ExceptionZZZ(te);
			throw ez;
		}catch(GitAPIException gae) {
			ExceptionZZZ ez = new ExceptionZZZ(gae);
			throw ez;
		}
	}
	
	//##################################################
	public void printStatus(Git git) throws NoWorkTreeException, GitAPIException {
		
		Status status = git.status().call();

        Set<String> added = status.getAdded();
        for (String add : added) {
            System.out.println("Added: " + add);
        }
        Set<String> uncommittedChanges = status.getUncommittedChanges();
        for (String uncommitted : uncommittedChanges) {
            System.out.println("Uncommitted: " + uncommitted);
        }

        Set<String> untracked = status.getUntracked();
        for (String untrack : untracked) {
            System.out.println("Untracked: " + untrack);
        }
	}
}
