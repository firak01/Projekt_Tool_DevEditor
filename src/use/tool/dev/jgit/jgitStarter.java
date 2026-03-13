package use.tool.dev.jgit;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Set;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.lib.Config;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RemoteConfig;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;



public class jgitStarter {

	
	
	public boolean startit() throws IllegalStateException, GitAPIException, URISyntaxException {		                            
		//Zwei verschiedene lokale Repos, je nachdem welches Eclipse
		//A) auf TUBAF - HISinOne Eclipse:
		//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
		//B) auf TUBAF (Oxygen Version) für Z-Kernel Entwicklung
		//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\HIS_QISSERVER_FGL");
		
		//Auf Ermanarich, der HISinOne Tomcat
		File objFileDir = new File("C:\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
		
		
		//Trotz Einbinden von  in pom.xml Fehlermeldung;
		//ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console
		//Lösung dazu:
		//https://stackoverflow.com/questions/47881821/error-statuslogger-log4j2-could-not-find-a-logging-implementation
		//TODOGOON20260310;//jetzt wird eine logdatei all.log im Root des Projektordners angelegt. Das ist schlecht/unnoetig für GIT. Dort weg.
		System.setProperty("log4j.configurationFile","./use/tool/dev/jgit/log/log4j2.xml");
		
		//Logger log = LogManager.getLogger(this.getClass().getName());		
		Logger log = LogManager.getLogger();
		
		Git git = Git.init().setDirectory(objFileDir).call();
		System.out.println("Git-Repository init done.");
		
		System.out.println("STATUS BEFORE COMMIT");
		this.printStatus(git);
        //##################################################################
        
		//Fuege geänderte Dateien, die schon im Repository sind, hinzu.
		this.addFileTrackedChanged(git);
								
        //Mache einen commit               
        git.commit().setMessage("Commit by Java-Class a Projekt_Tool_DevEditor Solution").call();
        //System.out.println("Committed file " + myFile + " to repository at " + git.getRepository().getDirectory());
        
        //Mache den push
        //TODOGOON20230310;
        //... wie ??? 
        this.pushit(git);
        
        System.out.println("STATUS AFTER COMMIT");
        this.printStatus(git);
        //###############################################################
        
        //Fuege neue Dateien hinzu, die noch nicht im Repository sind.
       
        
		return true;
	}
	
	
	
	public void addFileTrackedChanged(Git git) throws NoWorkTreeException, GitAPIException {
		Status status = git.status().call();

		Set<String> uncommittedChanges = status.getUncommittedChanges();
		ArrayList<String> listasUncommitedChanges = new ArrayList<String>();
        for (String uncommitted : uncommittedChanges) {
           listasUncommitedChanges.add(uncommitted);
        }
        
        // run the add-call 
        for(String uncommited : listasUncommitedChanges) {
        	git.add().addFilepattern(uncommited).call();
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
	
	public void pushit(Git gitPrevious) throws URISyntaxException, InvalidRemoteException, TransportException, GitAPIException {
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
		
		//Zugang per ACCESS TOKEN ( PAT ) in github: Account, ganz unten im Navigator "Developer Settings"
		//String sPAT = "nicht hier, schau woanders nach";
		String sPAT = "";
		CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(sPAT, ""); //irgendwie empfohlen
		//CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider("firak01", sPAT); //so funktioniert es auch nicht
		
		/*Fehler:
		 Exception in thread "main" org.eclipse.jgit.errors.UnsupportedCredentialItem: ssh://git@github.com:22: org.eclipse.jgit.transport.CredentialItem$YesNoType:The authenticity of host 'github.com' can't be established.
RSA key fingerprint is.... .
Are you sure you want to continue connecting?
	at org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider.get(UsernamePasswordCredentialsProvider.java:119)
		 */

	  //Daher mal ein neues git Objekt erstellen   
      //Zwei verschiedene lokale Repos, je nachdem welches Eclipse
  		//A) auf TUBAF - HISinOne Eclipse:
  		//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
  		//B) auf TUBAF (Oxygen Version) für Z-Kernel Entwicklung
  		//File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\HIS_QISSERVER_FGL");
  		
  		//Auf Ermanarich, der HISinOne Tomcat
  		File objFileDir = new File("C:\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
  		
        
		
		Git git = Git.init().setDirectory(objFileDir).call();
		System.out.println("Git-Repository init done.");
		
		//aus: https://stackoverflow.com/questions/49292535/how-to-do-gitpush-using-jgit-api
		// add remote repo:
//		RemoteAddCommand remoteAddCommand = git.remoteAdd();
//	    remoteAddCommand.setName("origin");	
//		remoteAddCommand.setUri(new URIish("https://github.com/firak01/HIS_QISSERVER_FGL.git"));
		
		// aber mal explizit als pushCommand
		PushCommand pushCommand = git.push();
		//pushCommand.setRemote("https://github.com/firak01/HIS_QISSERVER_FGL.git");
		//aber Fehler.
		
		//An einigen Stellen wird die Syntax der URL mit Username:Token genannt.
		//git clone https://scuzzlebuzzle:<MYTOKEN>@github.com/scuzzlebuzzle/ol3-1.git --branch=gh-pages gh-pages
		pushCommand.setRemote("https://firak01:" + sPAT + "@github.com/firak01/HIS_QISSERVER_FGL.git");
		
		//wg Fehler: Caused by: javax.net.ssl.SSLException: Received fatal alert: protocol_version
		//GitHub verlangt TLS 1.2
		//System.setProperty("https.protocols", "TLSv1");
		System.setProperty("https.protocols", "TLSv1.2"); //aber Fehler: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
		//Merke: keytool ist wohl ein Program unter dem Java JDK
		//keytool -import -noprompt -trustcacerts -alias http://www.example.com -file "C:\Path\to\www.example.com.crt" -keystore cacerts
  
		// push to remote:
		pushCommand.setCredentialsProvider(credentialsProvider);
		pushCommand.call();
		
		
		//############### andere lösung aus stackoverview, aber wohl zu kompliziert
			//Config config = new Config();

			
						
			
			//Beispielcode:
			//config.setString("remote", "origin", "pushurl", "short:project.git");			
			//config.setString("url", "https://server/repos/", "name", "short:");
			
			//config.setString("remote", "origin", "pushurl", "short:project.git");
			//config.setString("url", "git@github.com:firak01/HIS_QISSERVER_FGL.git", "name", "short:");
			
			//RemoteConfig rc = new RemoteConfig(config, "origin");
			
			
			//mein Versuch
			//config.setString("remote", "origin", "pushurl", "short:project.git");
			
		  //assertFalse(rc.getPushURIs().isEmpty());
		  //assertEquals("short:project.git", rc.getPushURIs().get(0).toASCIIString());
			
			/*
			 String trackingBranch = "refs/remotes/" + remote + "/master";
			  RefUpdate trackingBranchRefUpdate = db.updateRef(trackingBranch);
			  trackingBranchRefUpdate.setNewObjectId(commit1.getId());
			  trackingBranchRefUpdate.update();

			  URIish uri = new URIish(db2.getDirectory().toURI().toURL());
			  remoteConfig.addURI(uri);
			  remoteConfig.addFetchRefSpec(new RefSpec("+refs/heads/*:refs/remotes/"
			      + remote + "/*"));
			  remoteConfig.update(config);
			  config.save();


			  RevCommit commit2 = git.commit().setMessage("Commit to push").call();

			  RefSpec spec = new RefSpec(branch + ":" + branch);
			  Iterable<PushResult> resultIterable = git.push().setRemote(remote)
			      .setRefSpecs(spec).call();
			
			*/
		
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
