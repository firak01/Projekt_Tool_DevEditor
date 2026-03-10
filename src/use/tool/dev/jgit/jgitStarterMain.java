package use.tool.dev.jgit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class jgitStarterMain {

	public static void main(String[] args) {
		//siehe: https://www.baeldung.com/jgit
		try {
			jgitStarter objStarter = new jgitStarter();
			objStarter.startit();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
