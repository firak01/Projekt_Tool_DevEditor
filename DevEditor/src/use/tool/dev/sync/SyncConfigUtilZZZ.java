package use.tool.dev.sync;

import java.util.Properties;

import basic.zBasic.ExceptionZZZ;
import basic.zBasic.IConstantZZZ;
import basic.zBasic.ReflectCodeZZZ;
import basic.zBasic.util.datatype.string.StringZZZ;

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
}
