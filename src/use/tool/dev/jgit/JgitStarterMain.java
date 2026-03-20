package use.tool.dev.jgit;

import java.io.File;
import java.net.URISyntaxException;

import javax.ws.rs.NotSupportedException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import use.tool.dev.ConfigDEV;
import use.tool.dev.jgit.https.JgitStarterHTTPS;
import use.tool.dev.jgit.ssh.JgitStarterSSH;

public class JgitStarterMain implements IConstantZZZ{

	public static void main(String[] args) {
		//siehe: https://www.baeldung.com/jgit
		//siehe: https://www.vogella.com/tutorials/JGit/article.html
		//siehe: https://medium.com/autotrader-engineering/working-with-git-in-java-part-1-a-jgit-tutorial-bc03b404a517
		
		//#######################################
		//### Pfad zum certifier - store, je nach Arbeitsumgebung ist das ein anderer.
		//### Diesen in der Startkonfiguration setzen
		//### von DEV04
		//### -Djavax.net.ssl.trustStore=C:\java\jdk1.8.0\jre\lib\security\cacerts  -Djavax.net.ssl.trustStorePassword=changeit
		//###
		//### von ERMANARICH / TUBAF
		//### -Djavax.net.ssl.trustStore=C:\java\jdk1.8.0_202\jre\lib\security\cacerts  -Djavax.net.ssl.trustStorePassword=changeit

		
		try {												
			/* Problem: Je nach Rechner / Eclipse
			 *          gibt es unterschiedlichen Repository Konfigurationen, die dann für die Authentifizierung wichti sind.
			 * 
			 * s. ChatGPT vom 2026-03-16
 6. Debug-Test
Zum Prüfen:
System.out.println(
    git.getRepository().getConfig()
       .getString("remote","origin","url")
);

Wenn dort SSH steht, wird dein Token ignoriert.

✅ Meine Vermutung (sehr wahrscheinlich):

Deine .git/config enthält noch
git@github.com:firak01/...
→ JGit versucht SSH, obwohl du HTTPS + Token im Code übergeben willst.
			 */
			
			//### Versuch Argumente entgegenzunehmen
									
			//-z  Flags, die aber noch nicht definiert sind.
			ConfigDEV objConfig = new ConfigDEV(args);
			String sConnectionType = objConfig.readConnectionType();
			if(StringZZZ.isEmpty(sConnectionType)){
				ExceptionZZZ ez = new ExceptionZZZ("Verbindungstyp", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;				
			}
			
			
			//Repository Kombinationenfuer TESTS								
			//Merke; Der RemoteAlias wird auch lokal definiert in .git\config
			
			//A) auf TUBAF - HISinOne Eclipse:
			//RepositoryLocal	C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL
			//RepositoryRemote	SSH
			//RepositoryRemote	HTTPS			
			//RemoteAlias     	"origin";					
			
			//B) auf TUBAF (Oxygen Version) für Z-Kernel Entwicklung
			//RepositoryLocal 	C:\\HIS-Workspace\\1fgl\\repo\\EclipseOxygen\\HIS_QISSERVER_FGL	
			//RepositoryRemote	SSH
			//RepositoryRemote	HTTPS			
			//RemoteAlias     	"origin";
			
			//C) auf Ermanarich, der HISinOne Tomcat
			//RepositoryLocal	C:\\repo\\Eclipse202312\\HIS_QISSERVER_FGL
			//RepositoryRemote	SSH
			//RepositoryRemote	HTTPS			
			//RemoteAlias		"origin";
			
			//D) Zur Entwicklung (auf DEV04), ein Dummy Verzeichnis
			//RepositoryLocal				C:\\1fgl\\repo\\EclipseOxygen_V01\\Projekt_Kernel02_JAZDummy 
			//RepositoryRemote	SSH			git@github.com:firak01/Projekt_Kernel02_JAZDummy.git
			//RepositoryRemote	HTTPS		https://github.com/firak01/Projekt_Kernel02_JAZDummy.git	
			//RemoteAlias					JAZDummy
			//man braucht die remote repository angabe nicht..... liegt in .git/config Datei, unter dem Alias .......                also Konsolenstring:			-ssh git@github.com:firak01/Projekt_Kernel02_JAZDummy.git -rl C:\1fgl\repo\EclipseOxygen_V01\Projekt_Kernel02_JAZDummy
			//also Konsolenstring:			-ssh -rl C:\1fgl\repo\EclipseOxygen_V01\Projekt_Kernel02_JAZDummy -ra JAZDummy
			
			//E) Zur Entwicklung (auf ERMANARICH), ein Dummy Verzeichnis
			//RepositoryLocal	C:\\1fgl\\repo\\EclipseOxygen\\Projekt_Kernel02_JAZDummy");
			//RepositoryRemote	SSH
			//RepositoryRemote	HTTPS			
			//RemoteAlias		"origin";
	  		
			String sRepositoryLocal = objConfig.readRepositoryLocal();
			if(StringZZZ.isEmpty(sRepositoryLocal)){
				ExceptionZZZ ez = new ExceptionZZZ("Pfad zum lokalen Repository", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			String sRepositoryRemoteAlias = objConfig.readRepositoryRemoteAlias();
			if(StringZZZ.isEmpty(sRepositoryRemoteAlias)){
				ExceptionZZZ ez = new ExceptionZZZ("Alias vom Remote Repository", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			
			
			//Unterschiedliche Wege, TODOGOON20260316;//Mache ein Interface, das dann .startit() als Methode enthaelt
			//-https oder -ssh
			//mit jeweils unterschiedlichem Remote Repository
			//PAT nur bei HTTPS notwendig
//			String sRepositoryRemote = null;			
			switch(sConnectionType) {
			case"ssh":
				//B) TUBAF
//				sRepositoryRemote = objConfig.readRepositoryRemoteSSH();
//				if(StringZZZ.isEmpty(sRepositoryRemote)){
//					ExceptionZZZ ez = new ExceptionZZZ("URL zum entfernten/remote SSH Repository", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
//					throw ez;
//				}
											
				JgitStarterSSH objStarterSSH = new JgitStarterSSH();
				objStarterSSH.setRepositoryLocal(sRepositoryLocal);
				objStarterSSH.setRepositoryRemoteAlias(sRepositoryRemoteAlias);					
				objStarterSSH.startit();
				break;
			case "https":
				//A) DEV04				
//				sRepositoryRemote = objConfig.readRepositoryRemoteHTTPS();
//				if(StringZZZ.isEmpty(sRepositoryRemote)){
//					ExceptionZZZ ez = new ExceptionZZZ("URL zum entfernten/remote SSH Repository", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
//					throw ez;
//				}
				
		
				String sPat = objConfig.readPersonalAccessToken();
				if(StringZZZ.isEmpty(sPat)){
					ExceptionZZZ ez = new ExceptionZZZ("Remote Repository, Personal Access Token (PAT)", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
					throw ez;
				}
				
				JgitStarterHTTPS objStarterHTTPS = new JgitStarterHTTPS();
				objStarterHTTPS.setRepositoryLocal(sRepositoryLocal);				
				objStarterHTTPS.setRepositoryRemoteAlias(sRepositoryRemoteAlias);
				objStarterHTTPS.setPersonalAccessToken(sPat);
				objStarterHTTPS.startit();
				break;							
			default:
				ExceptionZZZ ez = new ExceptionZZZ("Nicht behandelter Verbindungstyp '" + sConnectionType + "'", iERROR_PARAMETER_VALUE, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		} catch (ExceptionZZZ e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JGitInternalException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
