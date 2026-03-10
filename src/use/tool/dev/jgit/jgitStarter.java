package use.tool.dev.jgit;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;



public class jgitStarter {

	
	
	public boolean startit() throws IllegalStateException, GitAPIException {		                            
		//Zwei verschiedene lokale Repos, je nachdem welches Eclipse
		//A) HISinOne Eclipse: File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
		//B) Dieses Eclipse (Oxygen Version) für Z-Kernel Entwicklung
		File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\HIS_QISSERVER_FGL");
		
		
		//Trotz Einbinden von  in pom.xml Fehlermeldung;
		//ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console
		//Lösung dazu:
		//https://stackoverflow.com/questions/47881821/error-statuslogger-log4j2-could-not-find-a-logging-implementation
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
        TODOGOON20230310;
        //... wie ??? 
        
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
