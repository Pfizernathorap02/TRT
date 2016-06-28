package com.pfizer.webapp.user; 

import com.pfizer.action.TrainingAction;
import com.pfizer.hander.AuditHandler;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;

import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class UPUserControl {
	protected static final Log log = LogFactory.getLog( TrainingAction.class );
	//public String UP_REDIRECT_URL = "http://up.pfizer.com/auth.cfm?Appid=1387"; // prod- 1387 stage-2484
    public String UP_REDIRECT_URL = "http://up.pfizer.com/auth.cfm?Appid=1500";
	public String UP_REDIRECT_URL_STAGE = "http://upstg.pfizer.com/auth.cfm?Appid=2484"; // prod- 1387 stage-2484 
    public String UP_REDIRECT_URL_INT = "http://upstg.pfizer.com/auth.cfm?Appid=2515";
    /* Modified by Meenakshi on 30-Jul-2008*/ 
   // public String UP_REDIRECT_URL_INT = "http://upstg.pfizer.com/auth.cfm?Appid=3001"; 
    /* End of modifcation*/
    
	//"http://localhost:7001/TrainingReports/home.jpf?emplid=admin";//"http://up.pfizer.com/auth.cfm?Appid=2484"; //1304--for production 2467--for staging
	//public String UP_AUTH_URL     = "http://up.pfizer.com/authenticate_info.cfm";
    public String UP_AUTH_URL     = "http://up.pfizer.com/authenticate_info.cfm";  
    public String UP_AUTH_URL_STG     = "http://upstg.pfizer.com/authenticate_info.cfm";  
    /* Modified by Meenakshi on 18-Sep-2008*/
   // public String UP_AUTH_URL_INT     = "http://upint.pfizer.com/authenticate_info.cfm";  
	public String UP_AUTH_URL_INT     = "http://upstg.pfizer.com/authenticate_info.cfm";    
    /* End of modifcation*/
    public String UNAUTHORIZED_URL = "Unauthorized.jsp";   
	public String UP_AUTH_KEY1  = "authid"; 
	
	public void checkAuth(HttpServletRequest request,HttpServletResponse response, SuperWebPageComponents webpage) {
		try {
			UserSession uSession = (UserSession)request.getSession(true).getAttribute(UserSession.ATTRIBUTE);
			if ( webpage == null) {
				return;
			}
			if ( !webpage.isLoginRequired() ) {
				return;
			}
            System.out.print("UPUserControl--Step1--In checkAuth");
			// check to see if already logged in, else go to UP
			if ( uSession == null || !uSession.isLoggedIn() ) {
				String url = request.getRequestURL().toString();
				if ( url != null) {
					url = url.toLowerCase();
				}
				request.getSession(true).setAttribute("bounceBack",request.getRequestURL().toString() + "?" + request.getQueryString());
				if ( url != null && (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
					response.sendRedirect(UP_REDIRECT_URL_STAGE);
				} else if (url != null && url.indexOf("trt-tst.pfizer.com") > 0) {
					response.sendRedirect(UP_REDIRECT_URL_INT);
                } else {
                    System.out.print("UPUserControl--Step2--Redirecting to UP");
					response.sendRedirect(UP_REDIRECT_URL);
				}
				return;
			}
		} catch (Exception e) {
            e.printStackTrace();
			log.error(e,e);
		}	
	} 


	public void login(HttpServletRequest request) {
		
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(request,qStrings);
		User user;
		UserSession uSession;
		
		if (request.getParameter(UP_AUTH_KEY1) != null)  {
            String url = request.getRequestURL().toString();
            String upStr = "";
            if ( url != null && (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
                upStr = UP_AUTH_URL_STG;
            } else if (url != null && url.indexOf("trt-tst.pfizer.com") > 0) {
                upStr = UP_AUTH_URL_INT;
            } else {
                System.out.print("UPUserControl--Step3--Redirecting to UP again");
                upStr = UP_AUTH_URL;
            }
			String authUrl = upStr + "?" + UP_AUTH_KEY1 + "=" + request.getParameter(UP_AUTH_KEY1).toString();
			System.out.print("UPUserControl--Step4--Auth URL"+authUrl);
            String emplidUP = getUPTAGINFO(authUrl,"EmployeeID");
			String ntid = getUPTAGINFO(authUrl,"LoginID");
			String domain = getUPTAGINFO(authUrl,"Domain");
			qStrings.setEmplid( emplidUP );
			System.out.print("UPUserControl--Step5--EmplID"+emplidUP);
            System.out.print("UPUserControl--Step6--ntid"+ntid);
			//String temp1 = "THANGAVELS";
			//String temp2 = "mendem14".toUpperCase();
			if ( !Util.isEmpty(ntid) ) {
				ntid = ntid.toUpperCase();
			} 

			if ( !Util.isEmpty(ntid) && !Util.isEmpty(domain) ) {
				
			}
			//if (temp1.equals(ntid) || temp2.equals(ntid)) {
			//	qStrings.setEmplid( "admin" );
			//}
				
		}		
		
		if ( !Util.isEmpty(qStrings.getEmplid() ) )  {			
			request.getSession().removeAttribute( UserSession.ATTRIBUTE);			
			uSession = UserSession.getUserSession(request);
			user = uSession.getUser( qStrings.getEmplid() );
		}		
	}
	
	public void loginFull(HttpServletRequest request,HttpServletResponse response) {
			AppQueryStrings qStrings = new AppQueryStrings();
			FormUtil.loadObject(request,qStrings);
			User user;
			UserSession uSession;
		System.out.print("Step1--- In loginFull");		
		if (request.getParameter(UP_AUTH_KEY1) != null)  {
        System.out.print("Step2--- In loginFull");
            String url = request.getRequestURL().toString();
            String upStr = "";
            if ( url != null && (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
                upStr = UP_AUTH_URL_STG;
            } else if (url != null && url.indexOf("trt-tst.pfizer.com") > 0) {
                upStr = UP_AUTH_URL_INT;
            } else {
                System.out.print(upStr);
                upStr = UP_AUTH_URL;
            }
            
			String authUrl = upStr + "?" + UP_AUTH_KEY1 + "=" + request.getParameter(UP_AUTH_KEY1).toString();
			String pageContent = getPageContent(authUrl);
			
			// check if authid expired if so redirect back to UP
			String errorCode = getUPTAGINFOByPage(pageContent,"ErrorCode");
			if ("1000".equals(errorCode)) {
				try {				
					//String url = request.getRequestURL().toString();
					if ( url != null) {
						url = url.toLowerCase();
					}
					if ( url != null 
						&& (url.indexOf("localhost") > 0 || url.indexOf("trt-stg.pfizer.com") > 0 )) {
						response.sendRedirect(UP_REDIRECT_URL_STAGE);
					} else if (url != null && url.indexOf("trt-tst.pfizer.com") > 0) {
						response.sendRedirect(UP_REDIRECT_URL_INT);
					} else {
						response.sendRedirect(UP_REDIRECT_URL);
					}
				} catch (Exception e) {
                    e.printStackTrace();
					log.error(e,e);
				}
			}
			String emplidUP = getUPTAGINFOByPage(pageContent,"EmployeeID");
			String ntid = getUPTAGINFOByPage(pageContent,"LoginID");
			String domain = getUPTAGINFOByPage(pageContent,"Domain");
			System.out.print(emplidUP);
            System.out.print(ntid);
            System.out.print(domain);
            System.out.print("----------------------------------");
			if ( !Util.isEmpty(ntid) ) {
				ntid = ntid.toUpperCase();
			} 
			if ( !Util.isEmpty(domain) ) {
				domain = domain.toUpperCase();
			}
            System.out.print("^^^Step2.1^^^");
			request.getSession().removeAttribute(UserSession.ATTRIBUTE);			
			uSession = UserSession.getUserSession(request);
			user = uSession.getUser( emplidUP,ntid,domain );
            System.out.print("^^^Step2.2^^^");

			if (user.getValidUser())	{		
				AuditHandler auditor = new AuditHandler();
				auditor.insertAuditLogin( user );
                System.out.print("After InsertAuditLogin");  
			}	
		} else {
            System.out.print("Indside else");            
			String emplidUP = qStrings.getEmployeeID();
			String ntid = qStrings.getLoginID();
			String domain = qStrings.getDomain();

			if ( !Util.isEmpty(ntid) ) {
				ntid = ntid.toUpperCase();
			} 
			if ( !Util.isEmpty(domain) ) {
				domain = domain.toUpperCase();
			}
			
			request.getSession().removeAttribute( UserSession.ATTRIBUTE);			
			uSession = UserSession.getUserSession(request);
			System.out.print("Step4--- in LoginFull"+uSession);     
            //Selvam This original Code
            user = uSession.getUser( emplidUP,ntid,domain );			
            
            //Modified for Development
            //user = uSession.getUser("admin","balasm01","AMER");			            
            System.out.print("After getting the user");
		}		 
		
	}
		
	private String getPageContent(String url) {
		String outMsg = "";
		try {
			URL aUrl = new URL(url);

			if(aUrl != null) {
				StringBuffer result = new StringBuffer();
				InputStream in = aUrl.openStream();
				int c;
				while ((c = in.read()) != -1) result.append((char) c);
				outMsg = result.toString();		
			}			
		}
		catch(Exception ex) {
            ex.printStackTrace();
            outMsg = "exception:" + ex.toString();		
		}
		return outMsg;
	}

	private String parseAttribute(String aString, String aStartTag, String aEndTag) {
		String returnStr="";
		int i = aString.indexOf(aStartTag);
		i = i + aStartTag.length();
		int j = aString.indexOf(aEndTag);
		if(i>0 && j>0 && j>i) {
			returnStr = aString.substring(i,j);
		}
        System.out.print("Step5--- in parseAttribute"+returnStr);   
		return returnStr.trim();
	}


//	private String getForwardURL(String reqURL){
//		String thisURL="";
//		if(reqURL.toLowerCase().indexOf("scestg.pfizer.com") > -1)  thisURL="http://up.pfizer.com/auth.cfm?Appid=2467";
//		else
//		if(reqURL.toLowerCase().indexOf("sce.pfizer.com") > -1)  thisURL="http://up.pfizer.com/auth.cfm?Appid=1304";
//		return thisURL;
//	}


	private String getAuthUserId(String aUpAuthUrl) {
		String domain = "";
		String ntid = "";
		String result = "";
		String upAuthUrl = aUpAuthUrl;
	
		try {
			String pageContent = getPageContent(upAuthUrl);
			
			domain = parseAttribute(pageContent,"<Domain>","</Domain>");		
			ntid = parseAttribute(pageContent,"<LoginID>","</LoginID>");
			
			if (domain.length() > 0 && ntid.length() > 0) {
				result = domain + "\\" + ntid;
			} else {
				result = "";			
			}
		} catch(Exception ex) {
            ex.printStackTrace();
			result = "";		
		}				
		
		return result;
	}

	public String getUPTAGINFO(String aUpAuthUrl,String tag) {
		String result = "";
		String upAuthUrl = aUpAuthUrl;
		
		try {
			String pageContent = getPageContent(aUpAuthUrl);
			result = parseAttribute(pageContent,"<"+tag+">","</"+tag+">");
			if (result.trim().length()==0) result="";
		}
		catch(Exception ex)     {
            ex.printStackTrace();
			result = "";
		}                   
		return result;
	}

	public String getUPTAGINFOByPage(String pageContent,String tag) {
		String result = "";
		
		try {
			result = parseAttribute(pageContent,"<"+tag+">","</"+tag+">");
			if (result.trim().length()==0) result="";
		}
		catch(Exception ex)     {
            ex.printStackTrace();
			result = "";
		}                   
		return result;
	}

} 
