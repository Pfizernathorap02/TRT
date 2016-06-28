package com.pfizer.utils; 

import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.http.HttpServletRequest;

/*
 *Infosys - Weblogic to to Jboss Migration changes
 *New class added as prt of this change 
 */
public class Global
{ 
	/*
	 *This class is used to set errors into the stackTrace. 
	 */
	public static void getError(HttpServletRequest request,Exception ex){
		System.out.println("in Global");
		String errorSessionKey = request.getParameter("session_key");
		request.getSession().setAttribute(errorSessionKey + ".error.message", ex.getMessage());
		StringWriter stackTrace = new StringWriter();
		PrintWriter stackTracePrinter = new PrintWriter(stackTrace);
		ex.printStackTrace(stackTracePrinter);
		ex.printStackTrace();
		stackTracePrinter.close();
		request.getSession().setAttribute(errorSessionKey + ".error.stacktrace", stackTrace.toString());
	}
}
    

    
