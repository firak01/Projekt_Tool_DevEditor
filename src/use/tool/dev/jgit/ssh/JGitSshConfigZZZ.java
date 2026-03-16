package use.tool.dev.jgit.ssh;

import java.io.File;
import java.io.FilenameFilter;

import org.eclipse.jgit.transport.SshSessionFactory;
import org.eclipse.jgit.transport.JschConfigSessionFactory;
import org.eclipse.jgit.transport.OpenSshConfig;
import org.eclipse.jgit.util.FS;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**Erstellt von ChatGPT 2026-03-16
 * @author fl86kyvo
 *
 */
public class JGitSshConfigZZZ {

    public static void configure() {

    	JschConfigSessionFactoryZZZ myJschConfigSessionFactory = new JschConfigSessionFactoryZZZ();    	
        SshSessionFactory.setInstance(myJschConfigSessionFactory) ;
    }
}