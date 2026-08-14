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
public class SyncConfigMain_appendFile implements IConstantZZZ, ISyncConfigConstantsZZZ {
	
	
    private String sFile = null;
    private String sDirectory = null;
    
    String sRootFile=null; //Das Verzeichnis, mit dem die Dateipfad-Einträge anfangen sollen.
    
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
	    	   
	    	   	//1. Lies die Rohdaten ein
	        	SyncConfigConsoleUI sqlConsole = new SyncConfigConsoleUI();	        	
	        	List<String> listEintrag = sqlConsole.readLinesOrFileAsList(); //entweder Zeilen aus der Konsole oder aus einer Datei.
	        	System.out.println("Neue Dateipfadzeilen: Eingelesen.");
	        	
	        	//2. Überarbeitet die Rohdaten
	        	//Die neuen Dateipfade müssen mit WEB-INF anfangen.
	        	erzeuger = new SyncConfigMain_appendFile();
	        	erzeuger.setDirectory(sConfigFileDirectory);
	        	erzeuger.setFile(sConfigFileName);
	        	erzeuger.setRootForFiles("WEB-INF");
	        	listEintrag = erzeuger.normalizeLines(listEintrag);
	        	
	        	
	        	//3. Ermittle aus der aktuellen Konfiguration den höchsten Map Eintrag und dann den nächsten.
	        	//   Beispiel für eine Map - Zeile: MAP_09=WEB-INF\templates\dbinterface\hisinone\sospos-duplicate_unitPrePO4TUBAF.vm
	        	//   Also: Teile die Zeile an dem "=" auf. ...Java Properties machen das automatisch.
	        	String sConfigFile = FileEasyZZZ.joinFilePathName(sConfigFileDirectory, sConfigFileName);	        	
	        	File fileConfigFile = ResourceEasyZZZ.searchFile(sConfigFile);
	        	
	        	//KernelPropertyZZZ objProperty = KernelPropertyZZZ.getInstance(".\\JUnitTest.property");
	        	KernelPropertyZZZ objProperty = KernelPropertyZZZ.getInstance(fileConfigFile);
	        	HashMap<File,Properties> hm = objProperty.getFileLoadedAll();
	        	System.out.println("Vorhandene Konfigurationsdatei gelesen: '" + fileConfigFile.getAbsolutePath() + "'");
	        	
	        	//Properties prop = hm.get(new File(sConfigFile)); //Merke: Der Dateipfad ist ja inzwischen konkret. Die Property ist unter dem konkreten Pfad abgelegt.
	        	Properties prop = hm.get(fileConfigFile);
	        	//Beispielwerte:
	        	//Key=MAP_09
	        	//Value=WEB-INF\templates\dbinterface\hisinone\sospos-duplicate_unitPrePO4TUBAF.vm
	        	
	        	
	        	//Jetzt muss verhindert weden, dass Zeilen übernommen werden, die bereits schon in der Konfiguration vorhanden sind.
	        	List<String>listEintragNewly = new ArrayList<String>();
	        	for(String sListEintrag : listEintrag) {
	        		boolean bExistsYet = PropertiesUtilZZZ.hasValue(prop, sListEintrag);
	        		if(!bExistsYet) {
	        			listEintragNewly.add(sListEintrag);
	        		}
	       		}	
	        	
	        	int iKeyMax = SyncConfigUtilZZZ.getKeyMax(prop);
	        	
	        	String sKeyMax = SyncConfigUtilZZZ.computeKey(iKeyMax);
	        	System.out.println("Bisher hoechster Key Eintrag: " + sKeyMax);
	        	
	        	//4. Hänge eine neue, ausgerechnete Zeile an
	        	FileTextAppenderZZZ objFileAppender = new FileTextAppenderZZZ(fileConfigFile);
	        	
	        	int iCount = 0;
	        	for(String sLine : listEintragNewly) {	        		
	        		iCount++;
	        		
	        		//Vor der 1. Zeile einen Kommentar schreiben, "es folgen neue Zeilen, Datum".
	        		if(iCount==1) {
	        			String sLineComment = SyncConfigUtilZZZ.createLineCommentDated("Neu angehängte Zeile(n)");
	        			objFileAppender.append(sLineComment);
	        		}
	        		

		        	//String sKeyMaxNext = SyncConfigUtilZZZ.computeKey(iKeyMax+1);
		        	//System.out.println("Naechster Key Eintrag: " + sKeyMaxNext);
	        		String sLineEscaped = StringZZZ.escapeFileSeparators(sLine);
		        	String sLineNext = SyncConfigUtilZZZ.computeLineForKey(iKeyMax+iCount, sLineEscaped);
		        	System.out.println("Naechster Zeileneintrag: " + sLineNext);
		        	
		        	objFileAppender.append(sLineNext);
	        	}
	        	
	        	
	        	if(iCount >= 1) {
	        		boolean bSaved = objFileAppender.save();
	        		if(bSaved) {
	        			System.out.println("Datei mit neuen Zeileneintraegen gespeichert: " + objFileAppender.getFilePathSavedLast());
	        		}else {
	        			System.out.println("Datei nicht gespeichert: " + objFileAppender.getFilePath());
	        		}
	        	}
	        	
	        	
	        	
	        	
	        	
	        	
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
     *       qisserver/WEB-INF/templates/oooreporting/tubaf/common/footerWithLabel.odt
     *       
     *       Notwendig ist, insgesamt. Ergo: Abgesehen vom Key muss WEB-INF vorne stehen.
     *       MAP_10=WEB-INF\\templates\\oooreporting\\tubaf\\stu\\uitext\\Exmatrikulation01.odt
     *       
     *       Diese Methode soll das Format sicherstellen.
     *       
     * @param listasLine
     * @return
     * @throws ExceptionZZZ
     */
    public List<String> normalizeLines(List<String>listasLine) throws ExceptionZZZ{
    	return SyncConfigUtilZZZ.normalizeLines(listasLine, this.getRootForFiles());
    }
    
}