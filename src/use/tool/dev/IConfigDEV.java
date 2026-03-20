package use.tool.dev;

import basic.zBasic.ExceptionZZZ;

public interface IConfigDEV {

	//#####################################################################
	//####### Konfiguration der Argumgentuebergabe von aussen an das Program (s. GetOptZZZ).
	//Merke1: Ein Doppelpunkt bedeutet "es folgt ein Wert". 
	//        Moeglich ist auch ein Pipe "|" nachfolgend. D.h. es gibt dazu keinen Wert.
	//        Entsprechend wird ein Wert ohne "|" gesehen.
	//Merke2: Es ist auch moeglich Argumente mit mehr als 2 Zeichen zu definieren.
	final static String sPATTERN_DEFAULT="ssh|https|pat:ra:rl:z:"; //ConnectionType: HTTPS oder SSH
													  //gefolgt jeweils von einer URL
													  //pat = Personal Access Token fuer HTTPS
													  //rl  = Repository local
	                                              //z = Flags, die dann JSON aehnlich uebergeben werden
	final static String sFLAGZ_DEFAULT="{}";      //leerer JSON aehnlicher String für zu setztende Flags, z.B. gefuellt {"DEBUGUI_PANELLABEL_ON":true}
	
	
	public String getConnectionTypeDefault() throws ExceptionZZZ;
	public String readConnectionType() throws ExceptionZZZ;	
	public boolean isConnectionTypeSSH() throws ExceptionZZZ;
	public boolean isConnectionTypeHTTPS() throws ExceptionZZZ;
	
	public String readPersonalAccessToken() throws ExceptionZZZ;
	public String getPersonalAccessTokenDefault() throws ExceptionZZZ;
	
	public String readRepositoryLocal() throws ExceptionZZZ;
	public String getRepositoryLocalDefault() throws ExceptionZZZ;
	
	//public String readRepositoryRemote() throws ExceptionZZZ;
	//schon mal gar nicht     public String getRepositoryRemoteDefaultSSH() throws ExceptionZZZ;
	//public String readRepositoryRemoteSSH() throws ExceptionZZZ;
	//schon mal gar nicht public String getRepositoryRemoteDefaultHTTPS() throws ExceptionZZZ;
	//public String readRepositoryRemoteHTTPS() throws ExceptionZZZ;
	
	public String readRepositoryRemoteAlias() throws ExceptionZZZ;
	public String getRepositoryRemoteAliasDefault() throws ExceptionZZZ;
}
