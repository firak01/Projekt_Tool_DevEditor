package use.tool.dev.jgit;

import basic.zBasic.ExceptionZZZ;

public interface IJgitStarter {

	public String getRepositoryLocal() throws ExceptionZZZ;
	public void setRepositoryLocal(String sRepositoryLocal) throws ExceptionZZZ;
	
	public String getRepositoryRemote() throws ExceptionZZZ;
	public void setRepositoryRemote(String sRepositoryRemote) throws ExceptionZZZ;
	
	public String getRepositoryRemoteAlias() throws ExceptionZZZ;
	public void setRepositoryRemoteAlias(String sRepositoryRemote) throws ExceptionZZZ;
	
	public boolean startit() throws ExceptionZZZ;
}
