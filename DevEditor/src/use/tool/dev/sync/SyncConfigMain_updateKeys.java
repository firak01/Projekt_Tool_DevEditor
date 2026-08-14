package use.tool.dev.sync;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.util.abstractList.ArrayListUniqueZZZ;
import basic.zBasic.util.abstractList.ArrayListUtilZZZ;
import basic.zBasic.util.datatype.dateTime.DateTimeZZZ;
import basic.zBasic.util.datatype.string.StringArrayZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.file.ResourceEasyZZZ;
import basic.zBasic.util.file.txt.stream.FileTextAppenderZZZ;
import basic.zBasic.util.file.txt.stream.FileTextSaverZZZ;
import basic.zBasic.util.file.txt.stream.FileTextWriterZZZ;
import basic.zBasic.util.properties.PropertiesUtilZZZ;
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
public class SyncConfigMain_updateKeys implements IConstantZZZ, ISyncConfigConstantsZZZ{

    private String sFile = null;
    private String sDirectory = null;
    
    String sRootFile=null; //Das Verzeichnis, mit dem die Dateipfad-Einträge anfangen sollen.
    
    ArrayListUniqueZZZ<String>listasFile=null;
    ArrayListUniqueZZZ<String>listasAppend=null;
    
    public SyncConfigMain_updateKeys() {
    }
    
    // Einstiegspunkt des Programms
    public static void main(String[] args) {
    	
    	main:{	    		        	        	   
	    	SyncConfigMain_updateKeys updater = null;
	
		       String sConfigFileName;
		       String sConfigFileDirectory;
		       
		       
		       try {	
		    	   if(args!=null && args.length>=1) {
		    		   sConfigFileName = args[1];
		        	}else {
		        		sConfigFileName = SyncConfigMain_updateKeys.sCONFIGURATION_FILENAME_DEFAULT; //hard coded zum Entwickeln
		        	}
		    	   
		    	   if(args!=null && args.length>=2) {
		    		   sConfigFileDirectory = args[2];
		        	}else {
		        		sConfigFileDirectory = SyncConfigMain_updateKeys.sCONFIGURATION_DIRECTORY_DEFAULT; //hard coded zum Entwickeln
		        	}
		    	   
		    	   	//1. Lies die Rohdaten ein
		        	SyncConfigConsoleUI sqlConsole = new SyncConfigConsoleUI();	        	
		        	List<String> listEintrag = sqlConsole.readFileAsList(); //entweder Zeilen aus der Konsole oder aus einer Datei.
		        	System.out.println("Zeilen der zu aktualisierenden Konfigurationsdatei gelesen.");
		        	
		        	//2. Überarbeitet die Rohdaten
		        	//Die neuen Dateipfade müssen mit WEB-INF anfangen, die Schluessel mit MAP_ ... werden neu numeriert.
		        	updater = new SyncConfigMain_updateKeys();
		        	updater.setDirectory(sConfigFileDirectory);
		        	updater.setFile(sConfigFileName);
		        	updater.setRootForFiles("WEB-INF");
		        	
		        	int iKeyCounter= 0; String sKey = null;
		        	ArrayList<String>listEintragUpdated = new ArrayList<String>();
		        	for(String sLine : listEintrag) {
		        		boolean bSkipLine = PropertiesUtilZZZ.isCommentLine(sLine) || StringZZZ.isEmptyNull(sLine);
		        		if(bSkipLine) {
		        			//nimm am Schluss der Schleife nur den Kommentarzeilen-Wert auf.
		        		}else {		        			
		        			boolean bKeyMissingLine = PropertiesUtilZZZ.isValueLineWithoutKey(sLine);
		        			if(bKeyMissingLine) {
		        				//Einfach einen neuen Key davorsetzen
		        				iKeyCounter++;
		        				sKey = SyncConfigUtilZZZ.computeKey(iKeyCounter);
		        				sLine = sKey + sLine;
		        			}else {		        				
		        				boolean bValueLine = PropertiesUtilZZZ.isValueLine(sLine);
		        				if(bValueLine) {
		        					//den bisherigen Key austauschen
		        					iKeyCounter++;
		        					sKey = SyncConfigUtilZZZ.computeKey(iKeyCounter);
		        					sLine = StringZZZ.rightKeep(sLine, "=");
		        					sLine = sKey + sLine;
		        				}else {
		        					//diese Zeile ist eine Kommentarzeile
		        					sLine = PropertiesUtilZZZ.createCommentLine(sLine);
		        				}		        			
		        			}//end if bKeyMissingLine
		        			
		        		}//end if bSkipLine	
		        		
		        		listEintragUpdated.add(sLine);
		        	}//end for
		        	
		        	
		        	List<String>listaEintragNormalized= updater.normalizeLines(listEintragUpdated);
		        	
		        	
		        	//3. Ermittle die aktuelle Konfiguration, um sie zu ersetzten.		        
		        	String sConfigFile = FileEasyZZZ.joinFilePathName(sConfigFileDirectory, sConfigFileName);	        	
		        	File fileConfigFile = ResourceEasyZZZ.searchFile(sConfigFile);
		        			        			        	
		        	//4. Übernimm die geänderten Zeilen in diese Konfigurationsdatei
		        	FileTextSaverZZZ objFileSaver = new FileTextSaverZZZ(fileConfigFile);
		        	objFileSaver.setLines(listaEintragNormalized);		        
	        		boolean bSaved = objFileSaver.save();
	        		if(bSaved) {
	        			System.out.println("Datei mit aktualisierten Zeilenentraegen gespeichert: " + objFileSaver.getFilePathSavedLast());
	        		}else {
	        			System.out.println("Datei nicht gespeichert: " + objFileSaver.getFilePath());
	        		}
		        	
//	       } catch (IOException e) {
//	           System.out.println("Fehler beim Einlesen: " + e.getMessage());
	       } catch (ExceptionZZZ ez){
	    	   System.out.println("Fehler: " + ez.getMessageLast());
	       }
    	}//end main:
    	System.out.println("Verarbeitung beendet.");
       	return;     
    }
    
    
    //### GETTER / SETTER
    public SyncConfigMain_updateKeys(String sDirectory, String sFile) {
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
    
    public String getRootForFiles() {
        return this.sRootFile;
    }

    public void setRootForFiles(String sRootFile) {
        this.sRootFile = sRootFile;
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
    
     
    /** Z.B. Eingabezeile ist
     *       eine Properties Zeile mit einem Dateipfad als String
     *       
     *       Notwendig ist, insgesamt. Ergo: Abgesehen vom Key muss WEB-INF vorne stehen.
     *       MAP_10=WEB-INF\\templates\\oooreporting\\tubaf\\stu\\uitext\\Exmatrikulation01.odt
     *       
     *       Diese Methode soll das Format sicherstellen.
     *       
     *       
     * @param listasLine
     * @return
     * @throws ExceptionZZZ
     */
    public ArrayList<String> normalizeLines(List<String>listasLine) throws ExceptionZZZ{
    	return SyncConfigUtilZZZ.normalizePropertiesFileLines(listasLine, this.getRootForFiles());
    }
    
}