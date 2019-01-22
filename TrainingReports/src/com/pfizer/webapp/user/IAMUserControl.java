package com.pfizer.webapp.user; 

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.action.TrainingAction;
import com.pfizer.hander.AuditHandler;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.printing.LoggerHelper;

/**
 * @Name : Jeevitha Pothini
 * @Description : Modified for IAM Implementation
 * @Modified : removed all functions and modified checkAuth and LoginFull functions
 *             Also modified name of the class.
 * @Date : 22-Jun-09
 */

public class IAMUserControl {
	protected static final Log log = LogFactory.getLog( TrainingAction.class );
    // The functions had been removed and chekAuth and LoginFull functions had been modified as part of IAM Implementation by Jeevith.
	
    //public String UNAUTHORIZED_URL = "..\\Unauthorized.jsp";   
	public String UNAUTHORIZED_URL = "Unauthorized.jsp";
	/**
     * This function does the session handling.
     */	
	public void checkAuth(HttpServletRequest request,HttpServletResponse response, SuperWebPageComponents webpage) {
		try {
			
			if ( webpage == null) {
				return;
			}
			if ( !webpage.isLoginRequired() ) {
				return;
			}
            LoggerHelper.logSystemDebug("IAMUserControl--Step1--In checkAuth");
			
            UserSession uSession = (UserSession)request.getSession(true).getAttribute(UserSession.ATTRIBUTE);
            if(uSession!= null)
            {        
                LoggerHelper.logSystemDebug("The user login status "+uSession.isLoggedIn());
                LoggerHelper.logSystemDebug("The user session value "+uSession);
            }
            else
            {
                 LoggerHelper.logSystemDebug("The user session value "+uSession);
            }
            
            // when the session expires, it redirected to login full to create the User object
            // again using the credentials collected from the request headers set by IAM.
            
            if ( uSession == null || uSession.getUser() == null )
            {
                loginFull(request,response);
                uSession = (UserSession)request.getSession(true).getAttribute(UserSession.ATTRIBUTE); 
            }
            else if(!(uSession.getUser().getValidUser()))
            {
                LoggerHelper.logSystemDebug("Before going to unauthorised Page");
                response.sendRedirect(UNAUTHORIZED_URL);
            }
             
            
			if ( uSession == null || !uSession.isLoggedIn() ) {
            
				String url = request.getRequestURL().toString();
				if ( url != null) {
					url = url.toLowerCase();
				}
                LoggerHelper.logSystemDebug("The url in checkAuth()  is ##########################" + url);
                 				
                LoggerHelper.logSystemDebug("Sent to Session Expire from " + url);
                response.sendRedirect("..\\sessionExpired.jsp");
                
			}
		} catch (Exception e) {
            e.printStackTrace();
			log.error(e,e);
		}	
	} 

    /**
     * This  function creates the User object by taking the credentials from the request headers set by IAM
     * and checks if the user is authorized to access the Application and routes to unauthorized page if the
     * user is not authorised.And if the headers are not available it routes to session expired page.
     */
	public void loginFull(HttpServletRequest request,HttpServletResponse response) {
		
		 
         
         
		
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(request,qStrings);
			User user;
            
			UserSession uSession = (UserSession)request.getSession(true).getAttribute(UserSession.ATTRIBUTE);
            
            
		LoggerHelper.logSystemDebug("Step1--- In loginFull");		
		
        if (uSession == null || uSession.getUser() == null)  {
            try
            {
                LoggerHelper.logSystemDebug("Step2--- In loginFull");
                Enumeration e1 = request.getHeaderNames();	
                if(e1.hasMoreElements())
                {
                    
                    String emplid = request.getHeader("IAMPFIZERUSERWORKFORCEID");
                    String ntid = request.getHeader("IAMPFIZERUSERPFIZERNTLOGONNAME");
                    String domain= request.getHeader("IAMPFIZERUSERPFIZERNTDOMAINNAME");
                   
                    
                    
                    System.out.println("request.getRequestURL()-->"+request.getRequestURL());
                    if(request.getRequestURL().indexOf("localhost") >= 0)
                    { 
                    	//ASPXOVW4KGXK1S is local machin name
                        // hardcoding for testing purpose
                        //emplid = "1746404";
                        //ntid = "MUNDAS";
                        //RILEYL01//1061034
                        //ntid = "paramp01";
                        //emplid ="1787475";
                        //ntid ="PRASAN06";
                        //domain = "amer"; 
                        //emplid ="111111";
                        //ntid ="BAKHSR";
                       // domain = "amer"; 
                        
//                        emplid ="1795904";
//                        ntid ="BHUSAA";
//                        domain = "amer"; 
                        
                    	   emplid ="12345622";
                           ntid ="KUMAM102";
                           domain = "APAC"; 
                    	
                    }
                    LoggerHelper.logSystemDebug(emplid);
                    LoggerHelper.logSystemDebug(ntid);
                    LoggerHelper.logSystemDebug(domain);
                    LoggerHelper.logSystemDebug("----------------------------------");
                    
                   
                    if ( !Util.isEmpty(ntid) && !Util.isEmpty(domain) && !Util.isEmpty(emplid)
                        && null != ntid && null != domain && null != emplid ) 
                    {
                    	 
                        ntid = ntid.toUpperCase();
                        domain = domain.toUpperCase();
                    }
                    else
                    {
                        response.sendRedirect("..\\sessionExpired.jsp");
                    }
                    LoggerHelper.logSystemDebug("^^^Step2.1^^^");
                    	
                    
                    uSession = UserSession.getUserSession(request);
                    	
                    user = uSession.getUser( emplid,ntid,domain );
                    
                    LoggerHelper.logSystemDebug("^^^Step2.2^^^");
        
                    if (user.getValidUser())	
                    {
                    		
                        LoggerHelper.logSystemDebug("Before InsertAuditLogin");		
                        AuditHandler auditor = new AuditHandler();
                        auditor.insertAuditLogin( user );
                        LoggerHelper.logSystemDebug("After InsertAuditLogin");  
                    }
                    else
                    {
                        LoggerHelper.logSystemDebug("Before going to unauthorised Page");
                        response.sendRedirect(UNAUTHORIZED_URL);
                    } 
                }
                else
                {
                     response.sendRedirect("..\\sessionExpired.jsp");
                }
            } 
            catch (Exception e) 
            {
                    e.printStackTrace();
					log.error(e,e);
            }   
          	
		}
        
	}
	

	private String getAuthUserId(String domain, String ntid) 
    {
		
		String result = "";	
		try {			
			if (domain.length() > 0 && ntid.length() > 0) 
            {
                domain.toUpperCase();
                ntid.toUpperCase();
				result = domain + "\\" + ntid;
                
			} else 
            {
				result = "";			
			}
		} 
        catch(Exception ex) 
        {
            ex.printStackTrace();
			result = "";		
		}				
		LoggerHelper.logSystemDebug("The user ID is "+result);
		return result;
	}
} 
