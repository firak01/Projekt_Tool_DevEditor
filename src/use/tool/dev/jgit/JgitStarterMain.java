package use.tool.dev.jgit;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class JgitStarterMain {

	public static void main(String[] args) {
		//siehe: https://www.baeldung.com/jgit
		try {
			JgitStarterMain objMain = new JgitStarterMain();
			objMain.startit();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public boolean startit() throws IllegalStateException, GitAPIException {
		File objFileDir = new File("C:\\HIS-Workspace\\1fgl\\repo\\Eclipse202312\\HIS_QISSERVER_FGL");
		Git git = Git.init().setDirectory(objFileDir).call();
		System.out.println("Git-Repository found.");
		return true;
	}

}
