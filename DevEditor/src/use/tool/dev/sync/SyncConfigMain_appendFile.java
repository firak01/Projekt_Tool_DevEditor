package use.tool.dev.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractList.ArrayListUniqueZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.file.ResourceEasyZZZ;
import basic.zBasic.util.system.Syso;
import basic.zKernel.KernelPropertyZZZ;


/**Ziel ist es die Konfiguration für die Batch - Dateien zu bearbeiten.
 * Datei: bat.sync.RepositoryTool/<Name des Repositories>_paths.cfg
 * 
 * a) Neue Einträge - sprich Dateien - hinzufügen.
 * 
 * 
 * @author fl86kyvo
 *
 */
public class SyncConfigMain_appendFile implements IConstantZZZ {
	
	public final static String sDIRECTORY_DEFAULT = "c:\\temp";
	public final static String sFILE_DEFAULT = "newFileList.txt";
	
	public final static String sCONFIGURATION_DIRECTORY_DEFAULT="Projekt_Tool_DevEditor\\DevEditor\\src\\bat\\syncRepositoryTool";
	public final static String sCONFIGURATION_FILENAME_DEFAULT="HIS_QISSERVER_FGL_paths.cfg";
	
    private String sFile = null;
    private String sDirectory = null;
    
    ArrayListUniqueZZZ<String>listasFile=null;
    ArrayListUniqueZZZ<String>listasAppend=null;
    
    public SyncConfigMain_appendFile() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
    	
    	main:{	    		        	        	   
	        SyncConfigMain_appendFile erzeuger = null;
	
	       String sConfigFileName;
	       String sConfigFileDirectory;
	       try {	
	    	   if(args!=null && args.length>=1) {
	    		   sConfigFileName = args[1];
	        	}else {
	        		sConfigFileName = SyncConfigMain_appendFile.sCONFIGURATION_FILENAME_DEFAULT; //hard coded zum Entwickeln
	        	}
	    	   
	    	   if(args!=null && args.length>=2) {
	    		   sConfigFileDirectory = args[2];
	        	}else {
	        		sConfigFileDirectory = SyncConfigMain_appendFile.sCONFIGURATION_DIRECTORY_DEFAULT; //hard coded zum Entwickeln
	        	}
	    	   
	    	   
	        	SyncConfigConsoleUI sqlConsole = new SyncConfigConsoleUI();	        	
	        	List<String> listEintrag = sqlConsole.readFilesAsList(); //entweder Zeilen aus der Konsole oder aus einer Datei.
	        		        	
	        	//1. Ermittle aus der aktuellen Konfiguration den höchsen Map Eintrag und dann den nächsten.
	        	//   Beispiel für eine Map - Zeile: MAP_09=WEB-INF\templates\dbinterface\hisinone\sospos-duplicate_unitPrePO4TUBAF.vm
	        	//   Also: Teile die Zeile an dem "=" auf. ...Java Properties machen das automatisch.
	        	String sConfigFile = FileEasyZZZ.joinFilePathName(sConfigFileDirectory, sConfigFileName);	        	
	        	File fileConfigFile = ResourceEasyZZZ.searchFile(sConfigFile);
	        	
	        	//KernelPropertyZZZ objProperty = KernelPropertyZZZ.getInstance(".\\JUnitTest.property");
	        	KernelPropertyZZZ objProperty = KernelPropertyZZZ.getInstance(fileConfigFile);
	        	HashMap<File,Properties> hm = objProperty.getFileLoadedAll();
	        	System.out.println("Vorhandene Konfigurationsdatei gelesen.");
	        	
	        	//Properties prop = hm.get(new File(sConfigFile)); //Merke: Der Dateipfad ist ja inzwischen konkret. Die Property ist unter dem konkreten Pfad abgelegt.
	        	Properties prop = hm.get(fileConfigFile);
	        	//Beispielwerte:
	        	//Key=MAP_09
	        	//Value=WEB-INF\templates\dbinterface\hisinone\sospos-duplicate_unitPrePO4TUBAF.vm
	        	
	        	
	        	int iKeyMax = SyncConfigUtilZZZ.getKeyMax(prop);
	        	
	        	String sKeyMax = SyncConfigUtilZZZ.computeKey(iKeyMax);
	        	System.out.println("Hoechster Key Eintrag: " + sKeyMax);
	        	
	        	String sKeyMaxNext = SyncConfigUtilZZZ.computeKey(iKeyMax+1);
	        	System.out.println("Naechster Key Eintrag: " + sKeyMaxNext);
	        	
	        	
	        	
	        	
	        	
	        	
	        	
	       } catch (IOException e) {
	           System.out.println("Fehler beim Einlesen: " + e.getMessage());
	       } catch (ExceptionZZZ ez){
	    	   System.out.println("Fehler: " + ez.getMessageLast());
	       }
    	}//end main:
    	System.out.println("Verarbeitung beendet.");
       	return;     
    }
    
    
    //### GETTER / SETTER
    public SyncConfigMain_appendFile(String sDirectory, String sFile) {
        this.sFile = sFile;
        this.sDirectory = sDirectory;
    }

    public String getFile() {
        return this.sFile;
    }

    public void setFile(String sFile) {
        this.sFile = sFile;
    }
    
    public String getDirectory() {
        return this.sDirectory;
    }

    public void setDirectory(String sDirectory) {
        this.sDirectory = sDirectory;
    }
    
    public ArrayList<String> getListFile(){
    	if(this.listasFile==null) {
    		this.listasFile = new ArrayListUniqueZZZ<String>();
    	}
    	return this.listasFile;
    }
    
    public void setListFile(List<String> listasFile) {
    	this.listasFile = (ArrayListUniqueZZZ<String>) listasFile;
    }
    
    public ArrayList<String> getListAppend(){
    	if(this.listasAppend==null) {
    		this.listasAppend = new ArrayListUniqueZZZ<String>();
    	}
    	return this.listasAppend;
    }
    
    public void setListAppend(List<String> listasAppend) {
    	this.listasAppend = (ArrayListUniqueZZZ<String>) listasAppend;
    }
    
    
    //######################################
    
    
    //### Hilfsfunktionen / Komfortfunktionen
    public void addAppend(String sAppend) {
    	this.getListAppend().add(sAppend);
    }
    
    public void addFile(String sFile) {
    	this.getListFile().add(sFile);
    }
    
}