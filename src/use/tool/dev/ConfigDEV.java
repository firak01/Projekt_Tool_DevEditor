package use.tool.dev;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.crypt.code.ICryptZZZ;
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
		String[] saArg = new String[3];
		saArg[0] = "-ssh";									
		saArg[1] = "-z";
		saArg[2] = this.getConfigFlagzJsonDefault();
		
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
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			sReturn = this.readConnectionTypeSSH();
			if(sReturn!=null) break main;
			
			sReturn = this.readConnectionTypeHTTPS();
			if(sReturn!=null) break main;
			
			sReturn = this.getConnectionTypeDefault();			
		}//end main:		
		return sReturn;
	}
	
	@Override
	public String readConnectionTypeSSH() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			sReturn = objOpt.readValue("ssh");			
		}//end main:		
		return sReturn;
	}
	
	@Override
	public String readConnectionTypeHTTPS() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			GetOptZZZ objOpt = this.getOptObject();
			if(objOpt==null) break main;
			if(objOpt.getFlag("isLoaded")==false) break main;
			
			sReturn = objOpt.readValue("https");			
		}//end main:		
		return sReturn;
	}
	
	
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
}
