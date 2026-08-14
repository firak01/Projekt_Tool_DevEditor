package use.tool.dev.sync;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.file.csv.stream.FileCsvReaderZZZ;
import basic.zBasic.util.file.txt.stream.FileTextReaderZZZ;
import basic.zKernel.KernelPropertyZZZ;

public class SyncConfigConsoleUI {
	
	private BufferedReader objReaderConsole = null;
	private String sDirecory = null;
	private String sFilename = null;
	
	//### GETTER / SETTER
	public BufferedReader getReaderForConsole() throws ExceptionZZZ{
		if(this.objReaderConsole==null) {
			this.objReaderConsole = new BufferedReader(new InputStreamReader(System.in));
		}
		return this.objReaderConsole;
	}
	
	//+++++++++++++++++++++++++++
	public String getDirectory() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sDirecory)){
			this.sDirecory = this.readDirectory();
		}
		return this.sDirecory;
	}
	
	public void setDirectory(String sDirectory) throws ExceptionZZZ {
		this.sDirecory = sDirectory;
	}
	
	public String readDirectory() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			try {
			BufferedReader reader = this.getReaderForConsole();
		
			//Verzeichnisnamen eingeben
	    	System.out.print("Bitte geben Sie den Namen des Verzeichnisse ein (Leerstring verwendet default '" + SyncConfigMain_appendFile.sDIRECTORY_DEFAULT + "'):"
	    					 + "\n");
	        String directory = reader.readLine();
	        directory = directory.trim();
	        if (!StringZZZ.isEmpty(directory)) {
	        	sReturn = directory;	            	
	        }else {
	        	sReturn = SyncConfigMain_appendFile.sDIRECTORY_DEFAULT;
	        }	
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return sReturn;
	}
	
	//+++++++++++++++++++++++++++++++
	
	public String getFilename() throws ExceptionZZZ {
		if(StringZZZ.isEmptyNull(this.sFilename)){
			this.sFilename = this.readFilename();
		}
		return this.sFilename;
	}
	
	public void setFilename(String sFilename) throws ExceptionZZZ {
		this.sFilename = sFilename;
	}
	
	public String readFilename() throws ExceptionZZZ{
		String sReturn = null;
		main:{
			try {
				BufferedReader reader = this.getReaderForConsole();
			
				//Dateinamen eingeben
				System.out.print("Bitte geben Sie den Namen der Datei mit den neuen zu synchronisierenden Dateipfaden als String ein (Leerstring verwendet Default):"
								+ "\n");
		        String s = reader.readLine();
		        s = s.trim();
		        if (!StringZZZ.isEmpty(s)) {
		        	sReturn = s;	            	
		        }else {
		        	sReturn = SyncConfigMain_appendFile.sFILE_DEFAULT;
		        }	
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return sReturn;
	}
	
	
	/*
	 * @return
	 * @throws ExceptionZZZ
	 */
	public List<String> readLinesOrFileAsList()throws ExceptionZZZ{
		List<String> listasReturn = null;
		main:{
			try {
				BufferedReader reader = this.getReaderForConsole();		        
				String sDirectory = this.getDirectory();
		        
				
				listasReturn = new ArrayList<String>();
        
				String eintragOld="";
		        String eintrag="";
		        String sEintrag="";
		        File fileEintrag=null;
        
        
		        boolean bFile = false; boolean bFileChecked=false;
		        System.out.print("Bitte geben Sie einen einzulesenden Dateinamen mit Dateipfaden im Verzeichnis '" + sDirectory + "' an"
		        		       + "\noder die in der Konfiguration hinzuzufügenden neuen Dateipfade direkt ein (kommagetrennt, auch mehrer Zeilen auf einmal, ggfs. mehrfach ENTER druecken)(Leerstring zum Abbrechen):"
		        		       + "\n");
		        while (true) {                
		            eintrag = reader.readLine();
		            		       	    
		            //erst beim 2ten "ENTER" die Eingabe beenden
		            if (StringZZZ.isEmptyTrimmed(eintrag) && StringZZZ.isEmptyTrimmed(eintragOld)) {
		                break;
		            }else {	                	
		            	 if(!bFileChecked) {
		                	 //Ist der Eintrag ein Dateipfad?
		                	 bFile = FileEasyZZZ.exists(sDirectory, eintrag);
		                	 if(bFile) {
		                		 fileEintrag = new File(sDirectory, eintrag);
		                		 bFile = FileEasyZZZ.isFileExisting(fileEintrag);
		                		 if(bFile) {
		                			sEintrag = eintrag;		                			 		                					                			
		     	                    break;
		                		 }
		                	 }
		                	 bFileChecked=true;	 
		            	 }
		            	 
		            	 
		            	 sEintrag = sEintrag + "\n" + eintragOld;
		                 eintragOld = eintrag;
		            }                                       
		        }//end while(true)
		        System.out.println("Neue Dateipfadzeilen: Eingabe beendet.");
		        
		        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		        if(bFile) {		        	
			        //Hier als Alternative, das Einlesen der Eintragsliste per Datei
		        	FileTextReaderZZZ objReaderText = new FileTextReaderZZZ(fileEintrag);
		        	listasReturn = objReaderText.getLines(); //Das hat den Vorteil, das es nur Zeilen ohne Kommentar und keine Leerzeilen sind.
		        }else {		        	             
		            String[] saEintrag = sEintrag.split("\n");
		            for(String sEintragTemp : saEintrag) {
		            	listasReturn.add(sEintragTemp);
		            }		            
		        }        
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return listasReturn;		
	}
	
	public List<String> readFileAsList()throws ExceptionZZZ{
		List<String> listasReturn = null;
		main:{
			try {
				BufferedReader reader = this.getReaderForConsole();		        
				String sDirectory = this.getDirectory();
		        
				
				listasReturn = new ArrayList<String>();
        
		        String eintrag="";
		        File fileEintrag=null;
        
       
		        System.out.print("Bitte geben Sie einen Dateinamen mit der zu aktualisierenden Konfiguration im Verzeichnis '" + sDirectory + "' an. (Leerstring zum Abbrechen):"
		        		       + "\n");
		                       
		        boolean bFile = false; 
	            while(!bFile) {
		            eintrag = reader.readLine();
		            if (StringZZZ.isEmptyTrimmed(eintrag)) {
		                break;
		            }else {	       
	                	 //Ist der Eintrag ein Dateipfad?
	                	 bFile = FileEasyZZZ.exists(sDirectory, eintrag);
	                	 if(bFile) {
	                		 fileEintrag = new File(sDirectory, eintrag);
	                		 bFile = FileEasyZZZ.isFileExisting(fileEintrag);	                		
	                	 }
		            }
	            }//end while
		        System.out.println("Zu aktualisierende Konfiguration: Eingabe beendet.");
		        
		        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		        	        	
		        //Das Einlesen der Eintragsliste per Datei
	        	FileTextReaderZZZ objReaderText = new FileTextReaderZZZ(fileEintrag);
	        	listasReturn = objReaderText.getLines(); //Das hat den Vorteil, das es nur Zeilen ohne Kommentar und keine Leerzeilen sind.
		       
			}catch (IOException ioe){
				ExceptionZZZ ez = new ExceptionZZZ(ioe);
				throw ez;
			}
		}//end main:
		return listasReturn;		
	}
}
