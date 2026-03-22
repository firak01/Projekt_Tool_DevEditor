package use.tool.dev.jgit;

import org.eclipse.jgit.api.Git;

import basic.zBasic.ExceptionZZZ;
import use.tool.dev.IConfigDEV;

public interface IJgitStarter {

	public String getRepositoryLocal() throws ExceptionZZZ;
	public void setRepositoryLocal(String sRepositoryLocal) throws ExceptionZZZ;
	
	public String getRepositoryRemote() throws ExceptionZZZ;
	public void setRepositoryRemote(String sRepositoryRemote) throws ExceptionZZZ;
	
	public String getRepositoryRemoteAlias() throws ExceptionZZZ;
	public void setRepositoryRemoteAlias(String sRepositoryRemote) throws ExceptionZZZ;
	
	public boolean pushit(IConfigDEV objConfig) throws ExceptionZZZ;
	public boolean pullit(IConfigDEV objConfig) throws ExceptionZZZ;
	
	//+++ Arbeit mit dem GitObject
	public Git getGitObject() throws ExceptionZZZ;
	public void setGitObject(Git git) throws ExceptionZZZ;
	
	public String searchRepositoryRemote() throws ExceptionZZZ;
	public String searchRepositoryRemote(String sRepositoryRemoteAlias) throws ExceptionZZZ;
}
