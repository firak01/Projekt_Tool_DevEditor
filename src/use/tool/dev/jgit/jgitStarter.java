package use.tool.dev.jgit;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

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
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.datatype.dateTime.DateTimeZZZ;



public class jgitStarter {
	//Zugang per ACCESS TOKEN ( PAT ) in github: Account, ganz unten im Navigator "Developer Settings"
	//String sPAT = "nicht hier, schau woanders nach";
	public final String sPAT = "";
	
	
	public boolean startit() throws IllegalStateException, GitAPIException, URISyntaxException, ExceptionZZZ {	

		//Zwei verschiedene lokale Repos, je nachdem welches Eclipse
		//A) auf TUBAF - HISinOne Eclipse:
		//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
		//B) auf TUBAF (Oxygen Version) für Z-Kernel Entwicklung
		//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\HIS_QISSERVER_FGL");
		
		//Auf Ermanarich, der HISinOne Tomcat
		//File objFileDir = new File("C:\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
		
		//Zur Entwicklung (auf DEV04), ein Dummy Verzeichnis
		//File objFileDir = new File("C:\\1fgl\\repo\\EclipseOxygen_V01\\Projekt_Kernel02_JAZDummy"); 
		
		//Zur Entwicklung (auf ERMANARICH), ein Dummy Verzeichnis
		File objFileDir = new File("C:\\1fgl\\repo\\EclipseOxygen\\Projekt_Kernel02_JAZDummy");

  		
		
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
		System.out.println("Git-Repository init done.");
						
		CredentialsProvider credentialsProvider = this.createCredentialsProviderByToken(git);
		System.out.println("Git Credentials Provider created done.");
		
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
		String sRepoRemote = "https://github.com/firak01/HIS_QISSERVER_FGL.git"; //Noch ungenutzt, muss aufgeteilt werden und dann der PAT Token eingebaut werden.
        this.pushit(git, credentialsProvider, sRepoRemote);
       
        System.out.println("STATUS AFTER PUSH");
        this.printStatus(git);
       
        
        //s. ChatGPT vom 20260313
        //Problem: Eclipse "registriert/bemerkt" den Push nicht (also Pfeil nach oben mit 1 dahinter wird angezeigt).
        //Damit in Eclipse auch der Push "registriert/bemerkt wird" muss noch ein Fetch gemacht werden.
        //Der letzte fetch() sorgt dafür, dass lokale Remote-Tracking-Branches synchron bleiben, 
        //was besonders hilfreich ist, wenn gleichzeitig ein Tool wie Eclipse auf das gleiche Repository schaut.
        
        
        //aber manchmal ist nichts zu fetchen, darum Fehler abfangen     
		try {
			Git git4Fetch = Git.open(objFileDir); 
			System.out.println("Git-Repository 4 Fetch repository opened.");
			
	    	FetchCommand gitCommandFetch = git4Fetch.fetch();
	    	gitCommandFetch.setRemote("origin"); //Laut chat gpt nicht die URL, da die Remote Daten schon im .git/config stehen
	    	gitCommandFetch.call();
	    	System.out.println(("FETCH DONE"));
	    }catch(TransportException tex) {
	    	System.out.println(tex.getMessage());
	    } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //###############################################################
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
	
	public CredentialsProvider createCredentialsProviderByToken(Git git) {
		//aus Eclipse-Push Konfiguration:
				//entspricht dem Github - Projekt - SSH
				//git@github.com:firak01/HIS_QISSERVER_FGL.git
				
				//aus Github - Projekt - HTTPS
				//https://github.com/firak01/HIS_QISSERVER_FGL.git
				
				//##################
				//Authentifizierung mit https
				/*https://medium.com/autotrader-engineering/working-with-git-in-java-part-1-a-jgit-tutorial-bc03b404a517
				Authenticating with a remote
				Most remote repos will require authentication (at least for the push command). In this tutorial, we’ll be working with remote repositories hosted on GitHub, which has two common authentication methods:
		    	Using a personal access token (PAT) for authentication over HTTPS
		    	Using SSH keys for authentication over SSH
				To keep things simple in this tutorial, we’ll only be covering HTTPS authentication; SSH is more complex and will be covered in part 2 of this two-part blog post.

				So in the following examples, we’ll be using a personal access token (PAT) for authentication via HTTPS. For more information on creating a PAT token, see the GitHub docs.
				Providing Credentials for Authentication

				The JGit command objects for operations such as git push, git pull, and git clone all share a setCredentialsProvider method that allows us to provide credentials to authenticate with the remote repository.

				The setCredentialsProvider method takes a CredentialsProvider instance as its parameter. This interface has many implementations, the one we need to use for a PAT token is the UsernamePasswordCredentialsProvider (more commonly used for basic authentication).
				Constructing a CredentialsProvider for a PAT token

				The UsernamePasswordCredentialsProvider 's constructor requires a username and password. When using a PAT token, we pass the token as the username and an empty string as the password:
				 */
				
				
				CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(sPAT, ""); //irgendwie empfohlen
				//CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("firak01", sPAT); //so funktioniert es auch nicht
				
				/*Fehler:
				 Exception in thread "main" org.eclipse.jgit.errors.UnsupportedCredentialItem: ssh://git@github.com:22: org.eclipse.jgit.transport.CredentialItem$YesNoType:The authenticity of host 'github.com' can't be established.
		RSA key fingerprint is.... .
		Are you sure you want to continue connecting?
			at org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider.get(UsernamePasswordCredentialsProvider.java:119)
				 */

			 
		return credentialsProvider;
	}
	
	public void pushit(Git git, CredentialsProvider credentialsProvider, String sRepoRemote) throws URISyntaxException, InvalidRemoteException, TransportException, GitAPIException {
				
		// aber mal explizit als pushCommand
		PushCommand pushCommand = git.push();
		//pushCommand.setRemote("https://github.com/firak01/HIS_QISSERVER_FGL.git");
		//aber Fehler.
				
		//An einigen Stellen wird die Syntax der URL mit Username:Token genannt.
		//git clone https://scuzzlebuzzle:<MYTOKEN>@github.com/scuzzlebuzzle/ol3-1.git --branch=gh-pages gh-pages
		//pushCommand.setRemote("https://firak01:" + sPAT + "@github.com/firak01/HIS_QISSERVER_FGL.git");
		
		//Zur Entwicklung, ein Dummy Projekt
		//https://github.com/firak01/Projekt_Kernel02_JAZDummy.git
		
		pushCommand.setRemote("https://firak01:" + sPAT + "@github.com/firak01/Projekt_Kernel02_JAZDummy.git");
		
		//wg Fehler: Caused by: javax.net.ssl.SSLException: Received fatal alert: protocol_version
		//GitHub verlangt TLS 1.2
		//System.setProperty("https.protocols", "TLSv1");
		System.setProperty("https.protocols", "TLSv1.2"); //aber Fehler: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
		//Merke: keytool ist wohl ein Program unter dem Java JDK
		//keytool -import -noprompt -trustcacerts -alias http://www.example.com -file "C:\Path\to\www.example.com.crt" -keystore cacerts
  
		// push to remote:
		pushCommand.setCredentialsProvider(credentialsProvider);
		pushCommand.call();
		
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
