package use.tool.dev;

import basic.zBasic.ExceptionZZZ;
import basic.zKernel.ConfigZZZ;
import junit.framework.TestCase;

public class ConfigDEVTest   extends TestCase{
	//Analog zu ConfigZZZ, aber ohne INI-Konfiguration
	private final String sApplicationKey = "DEV";
	private final String sSystemNumber = null;
	private final String sFile = null;
	private final String sDirectory = null;
	
	protected void setUp(){
//		try {			
//		
//			
//		} catch (ExceptionZZZ e) {
//			fail("Method throws an exception." + e.getMessageLast());
//		}		
	}
	
	/**F�r den Pattern String gilt: 1 Zeichen, ggf. gefolgt von einem Doppelpunkt
	 * Pr�fe auf: 
	 * - doppelte Zeichen (au�er dem Doppelpunkt)
	 * - pr�fe auf zwei hintereinander folgende Doppelpunkte
	 * 
	* lindhauer; 30.06.2007 08:21:33
	 */
	public void testInitialization(){
		
		try{
	
			//Ohne Argumente
			ConfigDEV objConfigInit = new ConfigDEV(null);
		    assertNotNull(objConfigInit);
		
		    //Mit leerem Argument
		    String[] saArg01 ={""};
		    ConfigDEV objConfigInit2 = new ConfigDEV(saArg01);
		    assertNotNull(objConfigInit2);
		    
		    //Mit gefuelltem Argument
			String[] saArg02 ={"-ssh"};
			ConfigDEV objConfigInit3 = new ConfigDEV(saArg02);
			assertNotNull(objConfigInit3);
		    
		} catch (ExceptionZZZ ez) {
			ez.printStackTrace();
			fail("Method throws an exception." + ez.getMessageLast());
		}	

	}
	
	public void testReadKey(){
		String sValue=null; String sValueExpected=null;
		try{
			//##########################
			//Objekt Ohne Argumente, also Defaultwert holen
			ConfigDEV objConfigInit1 = new ConfigDEV(null);
		    assertNotNull(objConfigInit1);
		    
		    sValue = objConfigInit1.readApplicationKey();
		    assertEquals("DEV",sValue);
		    
		    String stemp2proof = objConfigInit1.getApplicationKeyDefault();
		    assertTrue(stemp2proof.equals(sValue));
		    
		    sValue =objConfigInit1.readConnectionType();
		    sValueExpected = "ssh";
		    assertNotNull(sValue);
		    assertTrue(sValue.equals(sValueExpected));
		
		    //#########################
		    //Objekt Mit leerem Argument, also Defaultwert holen
		    String[] saArg ={""};
		    ConfigDEV objConfigInit2 = new ConfigDEV(saArg);
		    assertNotNull(objConfigInit2);
		    
		    sValue = objConfigInit2.readApplicationKey();
		    stemp2proof = objConfigInit2.getApplicationKeyDefault();
		    assertTrue(stemp2proof.equals(sValue));
		    
		    sValue =objConfigInit2.readConnectionType();
		    sValueExpected = "ssh";
		    assertNotNull(sValue);
		    assertTrue(sValue.equals(sValueExpected));
		    
		    //#########################
		    //Mit gefuelltem Argument, entspricht NICHT defaultwert
			String[] saArg02 ={"-https"};
			ConfigDEV objConfigInit3 = new ConfigDEV(saArg02);
			assertNotNull(objConfigInit3);
			
		    //Testfixture Object
		    sValue =objConfigInit3.readApplicationKey();
		    sValueExpected = sApplicationKey;
		    assertNotNull(sValue);
		    assertTrue(sValue.equals(sValueExpected));
		    
		    sValue =objConfigInit3.readConnectionType();
		    sValueExpected = "https";
		    assertNotNull(sValue);
		    assertTrue(sValue.equals(sValueExpected));		    		    		    		    

		} catch (ExceptionZZZ ez) {
			ez.printStackTrace();
			fail("Method throws an exception." + ez.getMessageLast());
		}	
	}
	
	
}
