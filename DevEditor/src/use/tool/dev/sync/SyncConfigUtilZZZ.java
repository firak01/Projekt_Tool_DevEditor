package use.tool.dev.sync;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.abstractList.ArrayListUtilZZZ;
import basic.zBasic.util.datatype.dateTime.DateTimeZZZ;
import basic.zBasic.util.datatype.string.StringArrayZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;
import basic.zBasic.util.file.FileEasyZZZ;
import basic.zBasic.util.properties.PropertiesUtilZZZ;

public class SyncConfigUtilZZZ implements IConstantZZZ {
	public static int getKeyMax(Properties props) throws ExceptionZZZ{
		int iReturn = -1;				
		main:{
			if(props==null) {
				ExceptionZZZ ez = new ExceptionZZZ("Properties Object", iERROR_PARAMETER_MISSING, SyncConfigUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			for (String sKey : props.stringPropertyNames()) {
			    //String sValue = props.getProperty(sKey);
			    //System.out.println(sKey + " = " + sValue);
				int iKey = SyncConfigUtilZZZ.getKeyCounterFromKey(sKey);
				if(iKey>iReturn) {
					iReturn = iKey; 
				}
			}
			
			
		}//end main:
		return iReturn;
	}
	
	public static int getKeyCounterFromKey(String sKey) throws ExceptionZZZ {
		int iReturn = -1;
		main:{
			if(StringZZZ.isEmptyNull(sKey)) {
				ExceptionZZZ ez = new ExceptionZZZ("sKey", iERROR_PARAMETER_MISSING, SyncConfigUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
					
			String sReturn = getKeyCounterFromKeyAsString(sKey);
			iReturn = StringZZZ.toInteger(sReturn);
		}//end main:
		return iReturn;
	}
	
	public static String getKeyCounterFromKeyAsString(String sKey) throws ExceptionZZZ {
		String sReturn = null;
		main:{
			if(StringZZZ.isEmptyNull(sKey)) {
				ExceptionZZZ ez = new ExceptionZZZ("sKey", iERROR_PARAMETER_MISSING, SyncConfigUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
		//   Beispiel für eine Map - Zeile: MAP_09=WEB-INF\templates\dbinterface\hisinone\sospos-duplicate_unitPrePO4TUBAF.vm
		//   Also ist der key: MAP_09	
			sReturn = StringZZZ.right(sKey, "MAP_");
			
		}//end main:
		return sReturn;
	}
	
	public static String computeKey(int iKeyCounter) throws ExceptionZZZ {
		return "MAP_" + iKeyCounter;
	}
	
	public static String computeLineForKey(int iKeyCounter, String sLineValue) throws ExceptionZZZ {
		String sReturn = null;
		main:{
			if(iKeyCounter<=-1) {
				ExceptionZZZ ez = new ExceptionZZZ("iKeyCounter", iERROR_PARAMETER_VALUE, SyncConfigUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			if(StringZZZ.isEmptyNull(sLineValue)) {
				ExceptionZZZ ez = new ExceptionZZZ("sLineValue", iERROR_PARAMETER_MISSING, SyncConfigUtilZZZ.class, ReflectCodeZZZ.getPositionCurrent());
				throw ez;
			}
			
			String sLine = computeKey(iKeyCounter);
			sLine = sLine + "=" + sLineValue;
			
			sReturn = sLine;
		}//end main:
		return sReturn;
	}
	
	//###################################################
	
	 public static String createLineComment(String sCommentIn) throws ExceptionZZZ{
    	String sReturn = null;
    	main:{
    		String sComment="";
    		if(sCommentIn!=null) sComment = sCommentIn;
    		
    		sReturn = "# " + sComment;
    	}//end main:
    	return sReturn;    	
    }
    
    public static String createLineCommentDated(String sCommentIn) throws ExceptionZZZ{
    	String sReturn = null;
    	main:{
    		String sComment="";
    		if(sCommentIn!=null) sComment = sCommentIn;
    		
    		String sDate= DateTimeZZZ.computeTimestampStringFormatedDefault();
    		sReturn = sComment + " am ( " + sDate + " )";
    		sReturn = SyncConfigUtilZZZ.createLineComment(sReturn);
    	}//end main:
    	return sReturn;    	
    }
    
	    
	//#################################
	
	/** Z.B. Eingabezeile ist
     *       qisserver/WEB-INF/templates/oooreporting/tubaf/common/footerWithLabel.odt
     *       
     *       Notwendig ist, insgesamt. Ergo: Abgesehen vom Key muss WEB-INF vorne stehen.
     *       MAP_10=WEB-INF/templates/oooreporting/tubaf/stu/uitext/Exmatrikulation01.odt
     * @param listasLine
     * @return
     * @throws ExceptionZZZ
     */
    public static ArrayList<String> normalizeLines(List<String>listasLine, String sRootForFile) throws ExceptionZZZ{
    	ArrayList<String>listasReturn = null;
    	main:{
    		if(listasLine==null) break main;
    		listasReturn = new ArrayList<String>();
    		    		
    		for(String sLine : listasLine) {
    			if(!StringZZZ.isEmptyTrimmed(sLine)) {
	    			System.out.println(sLine);
	    			sLine = StringZZZ.stripFileSeparatorsLeft(sLine);
	    			sLine = StringZZZ.stripFileSeparatorsRight(sLine);
	    			
	    			sLine = FileEasyZZZ.normalizeFilePath(sLine, FileEasyZZZ.cDIRECTORY_SEPARATOR);
	    			
	    			String[]saFilePathParts = StringZZZ.explode(sLine, FileEasyZZZ.cDIRECTORY_SEPARATOR);
	    			if(StringArrayZZZ.contains(saFilePathParts, sRootForFile)) {
	    				//Alles vor dem Root abschneiden
	    				ArrayList<String>listasPathPart = new ArrayList<String>();
	    				
	    				int iIndexFirst = StringArrayZZZ.searchIndexFirst(saFilePathParts, sRootForFile);
	    				for(int iIndex = iIndexFirst; iIndex<=saFilePathParts.length-1;iIndex++) {
	    					listasPathPart.add(saFilePathParts[iIndex]);
	    				}
	    				
	    				saFilePathParts = ArrayListUtilZZZ.toArray(listasPathPart, String.class);//Merke: Cast funktioniert nicht (String[])
	    			}else {
	    				//Vorweg den Root ergänzen
	    				saFilePathParts = StringArrayZZZ.prepend(saFilePathParts, sRootForFile);   				
	    			}
	    			
	    			sLine = StringArrayZZZ.implode(saFilePathParts, FileEasyZZZ.cDIRECTORY_SEPARATOR);
	    			
	    			listasReturn.add(sLine); //Übernimm KEINE Leerzeilen
    			}//isEmpty sLine    			    			
    		}//end for
    		
    		
    		
    	}//end main:
    	return listasReturn;
    }
    
	/** Z.B. Zielzeile ist
     *      MAP_10=WEB-INF//templates//oooreporting//tubaf//stu//uitext//Exmatrikulation01.odt
     *       
     *       Eingabezeile ist eine Properties-Wert-Zeile, Kommentarzeile oder Leerzeile
     *       Nur die Properties-Wert Zeilen sollen bearbeiter werden.
     *     
     * @param listasLine
     * @return
     * @throws ExceptionZZZ
     */
    public static ArrayList<String> normalizePropertiesFileLines(List<String>listasLine, String sRootForFile) throws ExceptionZZZ{
    	ArrayList<String>listasReturn = null;
    	main:{
    		if(listasLine==null) break main;
    		listasReturn = new ArrayList<String>();
    		    		
    		for(String sLine : listasLine) {
    			if(PropertiesUtilZZZ.isValueLine(sLine)) { //(StringZZZ.isEmptyTrimmed(sLine)) {
	    			System.out.println(sLine);
	    			sLine = StringZZZ.stripFileSeparatorsLeft(sLine);
	    			sLine = StringZZZ.stripFileSeparatorsRight(sLine);
	    			
	    			sLine = FileEasyZZZ.normalizeFilePath(sLine, FileEasyZZZ.cDIRECTORY_SEPARATOR);
	    			
	    			String[]saFilePathParts = StringZZZ.explode(sLine, FileEasyZZZ.cDIRECTORY_SEPARATOR);
	    			if(StringArrayZZZ.contains(saFilePathParts, sRootForFile)) {
	    				//Alles vor dem Root abschneiden
	    				ArrayList<String>listasPathPart = new ArrayList<String>();
	    				
	    				int iIndexFirst = StringArrayZZZ.searchIndexFirst(saFilePathParts, sRootForFile);
	    				for(int iIndex = iIndexFirst; iIndex<=saFilePathParts.length-1;iIndex++) {
	    					listasPathPart.add(saFilePathParts[iIndex]);
	    				}
	    				
	    				saFilePathParts = ArrayListUtilZZZ.toArray(listasPathPart, String.class);//Merke: Cast funktioniert nicht (String[])
	    			}else {
	    				//Vorweg den Root ergänzen
	    				saFilePathParts = StringArrayZZZ.prepend(saFilePathParts, sRootForFile);   				
	    			}
	    			
	    			sLine = StringArrayZZZ.implode(saFilePathParts, FileEasyZZZ.cDIRECTORY_SEPARATOR);	    				    				      			
    			}//isEmpty sLine
    			
    			listasReturn.add(sLine); //Übernimm auch Leerzeilen und Kommentarzeile
    		}//end for
    		
    		
    		
    	}//end main:
    	return listasReturn;
    }
}
