package use.tool.dev.jgit;

import use.tool.jgit.JgitStarterMain;

public class JgitStarterMainDEV {
	public static void main(String[] args) {
		
		//Umgebungsvariablen an die Methode des konkreten Projekts durchreichen
		//Sie sind pro Maschine/Eclipse Instanz ggfs. unterschiedlich
		//Nicht vergessen: Diese Umgebungsvariablen werden NUR beim Eclipsestart(!) im entsprechenden Starter gesetzt.
		System.out.println("Vorhandene Umgebungsvariablen, seit Eclipsestart:");
		System.out.println(System.getenv("MY_TRUSTSTORE"));
		System.out.println(System.getenv("sPATZZZ"));
		System.out.println(System.getenv("sRLZZZ"));
		System.out.println(System.getenv("sRRHZZZ"));		
		System.out.println(System.getenv("sRRACZZZ"));
		JgitStarterMain.main(args);
	}
}
