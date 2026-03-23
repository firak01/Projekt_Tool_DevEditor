package use.tool.dev.jgit;

import org.eclipse.jgit.api.FetchCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.RefSpec;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.web.cgi.UrlLogicZZZ;

public class JgitUtil implements IConstantZZZ {
	
	//Z.B.: SSH VERSION:     git@github.com:firak01/Projekt_Kernel02_JAZDummy.git
	public static String computeRepositoryUrlPartFromUrlSSH(String sUrlSSH) throws ExceptionZZZ {
		String sReturn = null;
		main:{
			String sUrlPartDomainFromSshRepo = StringZZZ.right("@" + sUrlSSH, "@");
			sUrlPartDomainFromSshRepo = StringZZZ.left(sUrlPartDomainFromSshRepo + ":", ":");
			
			String sUrlPartRepoFromSshRepo = StringZZZ.right(":" + sUrlSSH, ":");
			
			sReturn = sUrlPartDomainFromSshRepo + "/" + sUrlPartRepoFromSshRepo;			
		}//end main:
		return sReturn;
	}
	
	//Z.B. HTTPS Version: 	https://github.com/firak01/Projekt_Kernel02_JAZDummy.git
	public static String computeRepositoryUrlPartFromUrlHTTPS(String sUrlHTTPS) throws ExceptionZZZ {
		String sReturn = null;
		main:{
			
			
			//String sUrlPartDomainFromHttpsRepo =StringZZZ.right("@" + sUrlHTTPS, "@");
			
			
			/////sUrlPartDomainFromHttpsRepo = StringZZZ.left(sUrlPartDomainFromHttpsRepo + ":", ":");
			
			////String sUrlPartRepoFromHttpsRepo = StringZZZ.right(":" + sUrlHTTPS, ":");
			
			////sReturn = sUrlPartDomainFromHttpsRepo + "/" + sUrlPartRepoFromHttpsRepo;
			
			sReturn = UrlLogicZZZ.getUrlWithoutParameter(sUrlHTTPS);
			
			String sUrlPartDomainFromHttpsRepo = UrlLogicZZZ.getHost(sUrlHTTPS); 
			String sUrlPartRepoFromHttpsRepo = UrlLogicZZZ.getPath(sReturn); 
			sReturn = sUrlPartDomainFromHttpsRepo + sUrlPartRepoFromHttpsRepo;
		}//end main:
		return sReturn;
	}
	
	public static String computeRepositoryUrlPartFromUrlRepo(String sUrlRepo) throws ExceptionZZZ {
		String sReturn = null;
		main:{
			if(StringZZZ.isEmpty(sUrlRepo)) {
				ExceptionZZZ ez = new ExceptionZZZ("Remote Repository URL", iERROR_PARAMETER_MISSING, JgitUtil.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;				
			}
			
			if(JgitUtil.isUrlHTTPS(sUrlRepo)) {
				sReturn = JgitUtil.computeRepositoryUrlPartFromUrlHTTPS(sUrlRepo);
			}else if(JgitUtil.isUrlSSH(sUrlRepo)) {
				sReturn = JgitUtil.computeRepositoryUrlPartFromUrlSSH(sUrlRepo);
			}else {
				ExceptionZZZ ez = new ExceptionZZZ("Remote Repository URL. Unbekannter Typ: '" + sUrlRepo + "'", iERROR_PARAMETER_VALUE, JgitUtil.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
		}//end main:
		return sReturn;
	}
	
	public static boolean isUrlSSH(String sUrlRepo) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(StringZZZ.isEmpty(sUrlRepo)) {
				ExceptionZZZ ez = new ExceptionZZZ("Remote Repository URL", iERROR_PARAMETER_MISSING, JgitUtil.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;				
			}
			
			String sProtocol = UrlLogicZZZ.getProtocol(sUrlRepo);
			if(sProtocol==null) break main;
			
			if(sProtocol.equals("git")) bReturn = true;
		}//end main:
		return bReturn;
	}
	
	public static boolean isUrlHTTPS(String sUrlRepo) throws ExceptionZZZ {
		boolean bReturn = false;
		main:{
			if(StringZZZ.isEmpty(sUrlRepo)) {
				ExceptionZZZ ez = new ExceptionZZZ("Remote Repository URL", iERROR_PARAMETER_MISSING, JgitUtil.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;				
			}
			
			String sProtocol = UrlLogicZZZ.getProtocol(sUrlRepo);
			if(sProtocol==null) break main;
			
			if(sProtocol.equals("https")) bReturn = true;
		}//end main:
		return bReturn;
	}
	
	
	//######################
	//Wenn nicht zu fetchen ist, wird eine Exception geworfen. Das ist unschoen.
	//von ChatGPT 20260320, aber für meine einfachen zwecke brauch ich kein FetchResult, also nur die ExceptionHandling uebernommen
	public static FetchResult fetchIgnoreNothingToFetch(
	        Git git,
	        String remote,
	        CredentialsProvider credentialsProvider
	) throws TransportException, GitAPIException {

	    try {
	    	
	    	
	    	
	        FetchCommand fetchCommand = git.fetch();

	        if (remote != null && remote.trim().length() > 0) {
	            fetchCommand.setRemote(remote); // kann Alias ODER URL sein
	        }

	        if (credentialsProvider != null) {
	            fetchCommand.setCredentialsProvider(credentialsProvider);
	        }
	        
	        //aus .git\config Datei:
	        //      fetch = +refs/heads/*:refs/remotes/origin/*
	        fetchCommand.setRefSpecs(new RefSpec("+refs/heads/*:refs/remotes/origin/*"));

	        
	        FetchResult result = fetchCommand.call();

	        // Optional: Logging / Prüfung
	        if (result.getTrackingRefUpdates().isEmpty()) {
	            System.out.println("Fetch erfolgreich, aber keine Änderungen vorhanden.");
	        } else {
	            System.out.println("Fetch erfolgreich, Änderungen empfangen.");
	        }

	        return result;

	    } catch (TransportException e) {

	        String msg = e.getMessage();

	        if (msg != null && msg.toLowerCase().contains("nothing to fetch")) {
	            System.out.println("Nothing to fetch - Repository ist aktuell.");
	            return null; // bewusst null zurückgeben als Signal
	        }

	        // alle anderen Fehler weiterwerfen!
	        throw e;
	    }
	}
}
