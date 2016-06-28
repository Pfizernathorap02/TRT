<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.io.InputStream"%>

<%!
//****** CHANGE THE FOLLOWING UP PROPERTIES ************//
public String UP_REDIRECT_URL = "http://up.pfizer.com/auth.cfm?Appid=2484"; //1304--for production 2467--for staging
public String UP_AUTH_URL     = "http://up.pfizer.com/authenticate_info.cfm"; 
public String UNAUTHORIZED_URL = "..\\Unauthorized.jsp";   
public String UP_AUTH_KEY1  = "authid"; 
//****** END ******************************//

public String getPageContent(URL aUrl) {
	String outMsg = "";
	try {
		if(aUrl != null) {
			StringBuffer result = new StringBuffer();
			InputStream in = aUrl.openStream();
			int c;
			while ((c = in.read()) != -1) result.append((char) c);
			outMsg = result.toString();		
		}			
	}
	catch(Exception ex) {
		outMsg = "exception:" + ex.toString();		
	}
	return outMsg;
}

public String parseAttribute(String aString, String aStartTag, String aEndTag) {
	String returnStr="";
	int i = aString.indexOf(aStartTag);
	i = i + aStartTag.length();
	int j = aString.indexOf(aEndTag);
	if(i>0 && j>0 && j>i) {
		returnStr = aString.substring(i,j);
	}
	return returnStr.trim();
}


private String getForwardURL(String reqURL){
    String thisURL="";
    if(reqURL.toLowerCase().indexOf("scestg.pfizer.com") > -1)  thisURL="http://up.pfizer.com/auth.cfm?Appid=2467";
    else
    if(reqURL.toLowerCase().indexOf("sce.pfizer.com") > -1)  thisURL="http://up.pfizer.com/auth.cfm?Appid=1304";
    return thisURL;
}


public String getAuthUserId(String aUpAuthUrl) {
	String domain = "";
	String ntid = "";
	String result = "";
	String upAuthUrl = aUpAuthUrl;
	try {
		URL objUrl = new URL(upAuthUrl);
		String pageContent = getPageContent(objUrl);
		
		domain = parseAttribute(pageContent,"<Domain>","</Domain>");		
		ntid = parseAttribute(pageContent,"<LoginID>","</LoginID>");
		
		if (domain.length() > 0 && ntid.length() > 0) {
			result = domain + "\\" + ntid;
		} else {
			result = "";			
		}
	}
	catch(Exception ex) {
		result = "";
		
	}				
	return result;
}

public String getUPTAGINFO(String aUpAuthUrl,String tag) {
    String result = "";
    String upAuthUrl = aUpAuthUrl;
    
    try  {
        URL objUrl = new URL(upAuthUrl);
        String pageContent = getPageContent(objUrl);
        result = parseAttribute(pageContent,"<"+tag+">","</"+tag+">");
        if (result.trim().length()==0) result="";
    } catch(Exception ex) {
        result = "";
    }                   
    return result;
}

%>

<%
	UserSession uSession = (UserSession)request.getAttribute(UserSession.ATTRIBUTE);
	System.out.println("\n\n\n\nHello\n\n\n\n");
	if ( uSession == null
			|| !uSession.isLoggedIn() ) {
		response.sendRedirect("http://www.yahoo.com");		
	System.out.println("\n\n\n\nSkipping Redirect\n\n\n\n");
	} else {
		response.sendRedirect("http://www.yahoo.com");		
	System.out.println("\n\n\n\nSkipping Redirect\n\n\n\n");
	}
	
response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
String newLocn = "/newpath/index.html";
response.setHeader("Location","http://www.yahoo.com");
	
%>