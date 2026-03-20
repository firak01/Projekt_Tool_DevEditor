package use.tool.dev.jgit;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;

public abstract class AbstractJgitStarter implements IJgitStarter, IConstantZZZ{
	protected volatile String sRepositoryLocal=null;
	protected volatile String sRepositoryRemote=null;
	protected volatile String sRepositoryRemoteAlias=null;
	

	//aus IJgitStarter
	@Override
	public String getRepositoryLocal() throws ExceptionZZZ {
		return this.sRepositoryLocal;
	}
	
	@Override
	public void setRepositoryLocal(String sRepositoryLocal) throws ExceptionZZZ {
		this.sRepositoryLocal = sRepositoryLocal;
	}

	@Override
	public String getRepositoryRemote() throws ExceptionZZZ {
		return this.sRepositoryRemote;
	}

	@Override
	public void setRepositoryRemote(String sRepositoryRemote) throws ExceptionZZZ {
		this.sRepositoryRemote = sRepositoryRemote;
	}
	
	@Override
	public String getRepositoryRemoteAlias() throws ExceptionZZZ {
		return this.sRepositoryRemoteAlias;
	}

	@Override
	public void setRepositoryRemoteAlias(String sRepositoryRemoteAlias) throws ExceptionZZZ {
		this.sRepositoryRemoteAlias = sRepositoryRemoteAlias;
	}
	
	
	@Override
	public abstract boolean startit() throws ExceptionZZZ;

}
