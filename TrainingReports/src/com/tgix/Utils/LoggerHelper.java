package com.tgix.Utils; 



//import weblogic.logging.NonCatalogLogger;
import org.jboss.logging.Logger;


public class LoggerHelper {        

	private static transient Logger pspLogger = Logger.getLogger(LoggerHelper.class);         

	

	public static void logSystemError(String msg) {

		pspLogger.error(msg);

	}

	

	public static void logSystemError(String msg,Throwable t) {

		pspLogger.error(msg,t);

	}          

	

	public static void logSystemDebug(String msg) {

		pspLogger.debug(msg);

	}

	public static void logSystemDebug(String msg,Throwable t) {

		pspLogger.debug(msg,t);

	}

	

	public static void logSystemWarning(String msg) {

		pspLogger.warn(msg);

	}

	

	public static void logSystemWarning(String msg,Throwable t)	{

		pspLogger.warn(msg,t);

	}        

}//end of the Class

