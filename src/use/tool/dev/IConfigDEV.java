package use.tool.dev;

import basic.zBasic.ExceptionZZZ;

public interface IConfigDEV {

	//#####################################################################
	//####### Konfiguration der Argumgentuebergabe von aussen an das Program (s. GetOptZZZ).
	//Merke1: Ein Doppelpunkt bedeutet "es folgt ein Wert". 
	//        Moeglich ist auch ein Pipe "|" nachfolgend. D.h. es gibt dazu keinen Wert.
	//        Entsprechend wird ein Wert ohne "|" gesehen.
	//Merke2: Es ist auch moeglich Argumente mit mehr als 2 Zeichen zu definieren.
	final static String sPATTERN_DEFAULT="ssh|https|pat:z:"; //ConnectionType: HTTPS oder SSH
													  //pat = Personal Access Token fuer HTTPS
	                                              //z = Flags, die dann JSON aehnlich uebergeben werden
	final static String sFLAGZ_DEFAULT="{}";      //leerer JSON aehnlicher String für zu setztende Flags, z.B. gefuellt {"DEBUGUI_PANELLABEL_ON":true}
	
	
	public String getConnectionTypeDefault() throws ExceptionZZZ;
	public String readConnectionType() throws ExceptionZZZ;
	public String readConnectionTypeSSH() throws ExceptionZZZ;
	public String readConnectionTypeHTTPS() throws ExceptionZZZ;
	
	public String getPersonalAccessTokenDefault() throws ExceptionZZZ;
	public String readPersonalAccessToken() throws ExceptionZZZ;
	
}
