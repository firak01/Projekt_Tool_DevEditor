package use.tool.dev;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.crypt.code.ICryptZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zKernel.AbstractKernelConfigZZZ;
import basic.zKernel.GetOptZZZ;
import basic.zKernel.IKernelConfigZZZ;
import basic.zKernel.file.ini.IKernelEncryptionIniSolverZZZ;


/**Klasse enth�lt die Werte, die im Kernel als default angesehen werden.
	 *- ApplicationKey: FGL
	 * - SystemNumber: 01
	 * - Verzeichnis: c:\\fglKernel\\KernelConfig
	 * - Datei:		ZKernelConfigKernel_default.ini
	
	Verwende eine eigene Klasse, die KernelConfigZZZ erweitert, um für eine Spezielles Projekt andere Werte zu verwenden.
 * @author lindhauer
 *
 */
public class ConfigDEV extends AbstractKernelConfigZZZ implements IConfigDEV{
	private static String sPROJECT_PATH = "Projekt_Tool_DevEditor";
	private static String sPROJECT_NAME = "Projekt_Tool_DevEditor"; //normalerweise kuerzer, z.B. sPROJECT_NAME = "JAZKernel";
	//private static String sDIRECTORY_CONFIG_DEFAULT = "c:\\fglKernel\\KernelConfig";//Wenn der String absolut angegeben ist, so muss er auch vorhanden sein.
	private static String sDIRECTORY_CONFIG_DEFAULT = "<z:Null/>";//Merke: Ein Leerstring ist der Root vom Classpath, z.B. in Eclipse der src-Ordner. Ein "." oder ein NULL-Wert ist der Projektordner in Eclipse
	private static String sFILE_CONFIG_DEFAULT = "";                //wird hier nicht benutzt... z.B.: "ZKernelConfigKernel_default.ini";
	private static String sKEY_APPLICATION_DEFAULT = "DEV";
	private static String sNUMBER_SYSTEM_DEFAULT= "";               //wird hier nicht benutzt    z.B.: "01";
	
	

	
	public ConfigDEV() throws ExceptionZZZ{
		super();
	}
	public ConfigDEV(String[] saArg) throws ExceptionZZZ {
		super(saArg); 
	} 
			
	@Override
	public String getPatternStringDefault() {
		return IConfigDEV.sPATTERN_DEFAULT;
	}
	
	@Override
	public String[] getArgumentArrayDefault() {
		String[] saArg = new String[7];
		saArg[0] = "-ssh";	//Merke: aus dem lokalen Repository, in der Datei .git\config kommt die remote URL 		 
		saArg[1] = "-ra";   //       dazu ist der Remote Alias wichtig, per Default ist das "origin", kann aber auch anders benannt werden.
		saArg[2] = "origin";
		saArg[3] = "-rl";
		saArg[4] = ConfigDEV.sPROJECT_PATH;
		saArg[5] = "-z";
		saArg[6] = this.getConfigFlagzJsonDefault();
		
		return saArg;
	}
	
	@Override
	public String getApplicationKeyDefault() {
		return ConfigDEV.sKEY_APPLICATION_DEFAULT;
	}
	@Override
	public String getConfigDirectoryNameDefault() {
		return ConfigDEV.sDIRECTORY_CONFIG_DEFAULT;
	}
	@Override
	public String getConfigFileNameDefault() {		
		return ConfigDEV.sFILE_CONFIG_DEFAULT;
	}	
	@Override
	public String getSystemNumberDefault() {
		return ConfigDEV.sNUMBER_SYSTEM_DEFAULT;
}
	@Override
	public String getProjectName() {
		return ConfigDEV.sPROJECT_NAME;
	}
	@Override
	public String getProjectDirectory() {
		return ConfigDEV.sPROJECT_PATH;
	}
	
	//######################################
	//### Spezielle Argumente, die nix mit dem Kernel zu tun haben
	@Override
	public String getConnectionTypeDefault() {
		return "ssh";
	}
	@Override
	public String readConnectionType() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			boolean bReturn = false;
			
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			bReturn = this.isConnectionTypeSSH();
			if(bReturn) {
				sReturn = "ssh";
				break main;
			}
			
			bReturn = this.isConnectionTypeHTTPS();
			if(bReturn) {
				sReturn = "https";
				break main;
			}
				
			sReturn = this.getConnectionTypeDefault();			
		}//end main:		
		return sReturn;
	}
	
	@Override
	public boolean isConnectionTypeSSH() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			String sReturn = objOpt.readValue("ssh");	
			if(!StringZZZ.isEmpty(sReturn)) bReturn = true;
			
		}//end main:		
		return bReturn;
	}
	
	@Override
	public boolean isConnectionTypeHTTPS() throws ExceptionZZZ{
		boolean bReturn = false;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			String sReturn = objOpt.readValue("https");
			if(!StringZZZ.isEmpty(sReturn)) bReturn = true;
			
		}//end main:		
		return bReturn;
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++	
	@Override
	public String readRepositoryRemoteAlias() throws ExceptionZZZ {
		String sReturn = null;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			sReturn = objOpt.readValue("ra");
			if(sReturn==null){
				sReturn = this.getRepositoryRemoteAliasDefault();
			}
		}//end main:		
		return sReturn;
	}
	@Override
	public String getRepositoryRemoteAliasDefault() throws ExceptionZZZ {
		return "orign";
	}
	
//	@Override
//	public String getRepositoryRemoteDefaultSSH() throws ExceptionZZZ {		
//		return null;
//	}
//	@Override
//	public String getRepositoryRemoteDefaultHTTPS() throws ExceptionZZZ {
//		return null;
//	}
	
//	@Override
//	public String readRepositoryRemote() throws ExceptionZZZ {
//		String sReturn = null;
//		main:{
//			GetOptZZZ objOpt = this.getOptObject();
//			if(objOpt==null) break main;
//			if(objOpt.getFlag("isLoaded")==false) break main;
//			
//			sReturn = objOpt.readValue("ssh");
//			if(!StringZZZ.isEmpty(sReturn)) break main;
//			
//			sReturn = objOpt.readValue("https");
//			if(!StringZZZ.isEmpty(sReturn)) break main;
//									
//		}//end main:		
//		return sReturn;
//	}
	
//	@Override
//	public String readRepositoryRemoteSSH() throws ExceptionZZZ {
//		String sReturn = null;
//		main:{
//			GetOptZZZ objOpt = this.getOptObject();
//			if(objOpt==null) break main;
//			if(objOpt.getFlag("isLoaded")==false) break main;
//			
//			sReturn = objOpt.readValue("ssh");
//			if(sReturn==null){
//				sReturn = this.getRepositoryRemoteDefaultSSH();
//			}
//		}//end main:		
//		return sReturn;
//	}
//	
//	@Override
//	public String readRepositoryRemoteHTTPS() throws ExceptionZZZ {
//		String sReturn = null;
//		main:{
//			GetOptZZZ objOpt = this.getOptObject();
//			if(objOpt==null) break main;
//			if(objOpt.getFlag("isLoaded")==false) break main;
//			
//			sReturn = objOpt.readValue("https");
//			if(sReturn==null){
//				sReturn = this.getRepositoryRemoteDefaultHTTPS();
//			}
//		}//end main:		
//		return sReturn;
//	}
	
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++
	@Override
	public String getPersonalAccessTokenDefault() {
		return ""; //Merke: GitHub verweigert das PUSHEN eines PATs durch sein Regelwerk!!!
	}
	@Override
	public String readPersonalAccessToken() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			sReturn = objOpt.readValue("pat");
			if(sReturn==null){
				sReturn = this.getPersonalAccessTokenDefault();
			}
		}//end main:		
		return sReturn;
	}
	
	//++++++++++++++++++++++++++++++++++++++++++
	@Override
	public String getRepositoryLocalDefault() throws ExceptionZZZ {
		return ConfigDEV.sPROJECT_PATH; //Also das eigene Verzeichnis als Default
	}
	@Override
	public String readRepositoryLocal() throws ExceptionZZZ {		
		String sReturn = null;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			sReturn = objOpt.readValue("rl");
			if(sReturn==null){
				sReturn = this.getRepositoryLocalDefault();
			}
		}//end main:		
		return sReturn;
	}
	
}
