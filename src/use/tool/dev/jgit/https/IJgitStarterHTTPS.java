package use.tool.dev.jgit.https;

import basic.zBasic.ExceptionZZZ;
import use.tool.dev.jgit.IJgitStarter;

public interface IJgitStarterHTTPS extends IJgitStarter{
	public void setPersonalAccessToken(String sPat) throws ExceptionZZZ;
	public String getPersonalAccessToken() throws ExceptionZZZ;
}
