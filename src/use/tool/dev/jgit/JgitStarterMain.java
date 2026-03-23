package use.tool.dev.jgit;

import java.util.HashMap;

import javax.ws.rs.NotSupportedException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.errors.JGitInternalException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractList.ArrayListZZZ;
import basic.zBasic.util.abstractList.HashMapUtilZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.flag.json.FlagContainerZZZ;
import use.tool.dev.ConfigDEV;
import use.tool.dev.jgit.https.JgitStarterHTTPS;
import use.tool.dev.jgit.ssh.JgitStarterSSH;

public class JgitStarterMain implements IConstantZZZ{

	/**
	//#######################################
	//### Pfad zum certifier - store, je nach Arbeitsumgebung ist das ein anderer.
	//### Diesen in der Startkonfiguration der JVM setzen
	//### von DEV04
	//### -Djavax.net.ssl.trustStore=C:\java\jdk1.8.0\jre\lib\security\cacerts  -Djavax.net.ssl.trustStorePassword=changeit
	//###
	//### von ERMANARICH / TUBAF
	//### -Djavax.net.ssl.trustStore=C:\java\jdk1.8.0_202\jre\lib\security\cacerts  -Djavax.net.ssl.trustStorePassword=changeit
    //###
	//#########################################
	
	//#########################################
	//### Syntax der Batch-Zeile, für den Aufruf
	//###  -Djavax.net.ssl.trustStore=C:\java\jdk1.8.0\jre\lib\security\cacerts ^
	//###   -Djavax.net.ssl.trustStorePassword=changeit ^
	//###   -cp JgitStarter.jar JgitStarterMain %1
	//###
	//##########################################
	
	//#######################################################
	//### Es gibt:
	//### a) Verschiedene lokale Repos, je nachdem welches Eclipse/welche Entwicklungsumgebung
	//### b) Der RemoteAlias kann ggfs. anders definiert worden sein. Normalfall scheint "origin" zu sein.
	//### Der zu verwendenden Name steht in der Datei .git\config
	//###	 z.B.:
	//###	[remote "JAZDummy"]
	//###	url = git@github.com:firak01/Projekt_Kernel02_JAZDummy.git
	//###	fetch = +refs/heads/*:refs/remotes/JAZDummy/*
	//###
	//######################################################
	
	//#######################################################
	//Repository Kombinationen fuer TESTS								
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
	//man braucht die remote repository angabe nicht..... liegt in .git/config Datei, unter dem Alias ....... 
	//aber dort ist vielleicht ein anderes Protokoll definiert
	//also Konsolenstring:			-https https://github.com/firak01/Projekt_Kernel02_JAZDummy.git -rl C:\1fgl\repo\EclipseOxygen_V01\Projekt_Kernel02_JAZDummy
	//also Konsolenstring:			-ssh -ra JAZDummy -rl C:\1fgl\repo\EclipseOxygen_V01\Projekt_Kernel02_JAZDummy
	
	//E) Zur Entwicklung (auf ERMANARICH), ein Dummy Verzeichnis
	//RepositoryLocal	C:\\1fgl\\repo\\EclipseOxygen\\Projekt_Kernel02_JAZDummy");
	//RepositoryRemote	SSH
	//RepositoryRemote	HTTPS			
	//RemoteAlias		"origin";
	  		
	 * @param args
	 * @author Fritz Lindhauer, 22.03.2026, 07:01:47
	 */
	public static void main(String[] args) {
		//siehe: https://www.baeldung.com/jgit
		//siehe: https://www.vogella.com/tutorials/JGit/article.html
		//siehe: https://medium.com/autotrader-engineering/working-with-git-in-java-part-1-a-jgit-tutorial-bc03b404a517
		
		try {												
			String sAction=null;
			ArrayListZZZ<String>listasAction = new ArrayListZZZ<String>();
						
			//Trotz Einbinden von  in pom.xml Fehlermeldung;
			//ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console
			//Lösung dazu:
			//https://stackoverflow.com/questions/47881821/error-statuslogger-log4j2-could-not-find-a-logging-implementation
			//TODOGOON20260310;//jetzt wird eine logdatei all.log im Root des Projektordners angelegt. Das ist schlecht/unnoetig für GIT. Dort weg.
			System.setProperty("log4j.configurationFile","./use/tool/dev/jgit/log/log4j2.xml");
			
			//Logger log = LogManager.getLogger(this.getClass().getName());		
			Logger log = LogManager.getLogger();
			
			//wg Fehler: Caused by: javax.net.ssl.SSLException: Received fatal alert: protocol_version
			//Github benoetigt TLS Version 1.2 mindestens (kann sogar von WinXP bereitgestellt werden).
			//System.setProperty("https.protocols", "TLSv1");		
			System.setProperty("https.protocols", "TLSv1.2"); 
						
			//### Argumente entgegenzunehmen
			ConfigDEV objConfig = new ConfigDEV(args);
			
			//+++++++++++++++++++++++++++++++++
			//actions
			sAction = objConfig.readActionPull();
			if(!StringZZZ.isEmpty(sAction))listasAction.add(sAction);
			
			sAction = objConfig.readActionPush();
			if(!StringZZZ.isEmpty(sAction))listasAction.add(sAction);
			
			if(listasAction.isEmpty()) {
				ExceptionZZZ ez = new ExceptionZZZ("Action", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;
			}
			
			//++++++++++++++++++++++++++++++++
			//-z  Flags, die aber noch nicht definiert sind.
			TODOGOON20260323;//Wie nun das JSOn-Objekt in der Kommandozeile definieren
			//für IJgitStarterHTTPS.FLAGZ.IGNORE_CHECKOUT_CONFLICTS
			//sArg = "{\"FlagZZZ\":{\"hmFlag\":{\"XYZ\":true,\"abc\":true}}}";
			FlagContainerZZZ objFlagContainer = null;
			String sFlagZJson = objConfig.readFlagzJson();			
			if(!StringZZZ.isEmpty(sFlagZJson)) {
				Gson gson = new Gson();			
				objFlagContainer = gson.fromJson(sFlagZJson, FlagContainerZZZ.class);
				//Merke: Erst bei einem Objekt, das FlagZ behandelt, kann der Inhalt des FlagContainers verwendet werden.
			}
			
			
			
			
			
			
		
			//++++++++++++++++++++++++++++++++
			//Steuerung der Verbindung
			String sConnectionType = objConfig.readConnectionType();
			if(StringZZZ.isEmpty(sConnectionType)){
				ExceptionZZZ ez = new ExceptionZZZ("Verbindungstyp", iERROR_PARAMETER_MISSING, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
				throw ez;				
			}
			
			//Unterschiedliche Wege: 
			//-https oder -ssh
			//mit jeweils unterschiedlichem Remote Repository
			//PAT nur bei HTTPS notwendig
			
			boolean bReturn = false;				
			switch(sConnectionType) {
			case"ssh":								
										
				//##############################################################
				//Starte die passende Klasse mit der passenden Methode
				JgitStarterSSH objStarterSSH = new JgitStarterSSH();
				
				//Ggfs. uebergebene Flags setzen
				if(objFlagContainer!=null) {
					HashMap<String,Boolean> hmFlag = objFlagContainer.getHmFlag();
					for(int i=0; i< hmFlag.size(); i++) {
						String sFlagName = (String) HashMapUtilZZZ.getKeyByIndex(hmFlag, i);
						Boolean boolFlagValue = hmFlag.get(sFlagName);
						boolean bFlagValue = boolFlagValue.booleanValue();
						objStarterSSH.setFlag(sFlagName, bFlagValue);
					}
				}
				
				
				for(String sActionTemp : listasAction) {				
					switch(sActionTemp) {
					case "pull":
						bReturn = objStarterSSH.pullit(objConfig);
						break;
					case "push":
						bReturn = objStarterSSH.pushit(objConfig);						
						break;
					default:
						ExceptionZZZ ez = new ExceptionZZZ("Action not available", iERROR_PARAMETER_VALUE, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					
					if(!bReturn) {
						ExceptionZZZ ez = new ExceptionZZZ("Action '" + sActionTemp + "' was not successful.", iERROR_RUNTIME, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;						
					}
				}
				break;
				
			case "https":
				
				//##############################################################
				//Starte die passende Klasse mit der passenden Methode
				JgitStarterHTTPS objStarterHTTPS = new JgitStarterHTTPS();
	
				//Ggfs. uebergebene Flags setzen
				if(objFlagContainer!=null) {
					HashMap<String,Boolean> hmFlag = objFlagContainer.getHmFlag();
					for(int i=0; i< hmFlag.size(); i++) {
						String sFlagName = (String) HashMapUtilZZZ.getKeyByIndex(hmFlag, i);
						Boolean boolFlagValue = hmFlag.get(sFlagName);
						boolean bFlagValue = boolFlagValue.booleanValue();
						objStarterHTTPS.setFlag(sFlagName, bFlagValue);
					}
				}
				
				for(String sActionTemp : listasAction) {				
					switch(sActionTemp) {
					case "pull":
						bReturn = objStarterHTTPS.pullit(objConfig);
						break;
					case "push":
						bReturn = objStarterHTTPS.pushit(objConfig);						
						break;
					default:
						ExceptionZZZ ez = new ExceptionZZZ("Action not available", iERROR_PARAMETER_VALUE, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;
					}
					
					if(!bReturn) {
						ExceptionZZZ ez = new ExceptionZZZ("Action '" + sActionTemp + "' was not successful.", iERROR_RUNTIME, JgitStarterMain.class, ReflectCodeZZZ.getMethodCurrentName());
						throw ez;						
					}
				}
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
