package use.tool.dev.jgit;

import java.io.File;
import java.net.URISyntaxException;

import javax.ws.rs.NotSupportedException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;

import basic.zBasic.ExceptionZZZ;
import use.tool.dev.ConfigDEV;
import use.tool.dev.jgit.https.JgitStarterHTTPS;
import use.tool.dev.jgit.ssh.JgitStarterSSH;

public class JgitStarterMain {

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
			TODOGOON20260316;//mal sehn, ob das klappt.... auch ohne Kernel-ini Datei...
			//-ct https oder ssh
			//-z  Flags, die aber noch nicht definiert sind.
			ConfigDEV objConfig = new ConfigDEV(args);
			String sConnectionType = objConfig.readConnectionType();
			String sPat = objConfig.readPersonalAccessToken();
			
			
			//Unterschiedliche Wege, TODOGOON20260316;//Mache ein Interface, das dann .startit() als Methode enthaelt
			//A) DEV04
			JgitStarterHTTPS objStarter = new JgitStarterHTTPS();
			
			//B) TUBAF
			//JgitStarterSSH objStarter = new JgitStarterSSH();
			objStarter.startit();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
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
