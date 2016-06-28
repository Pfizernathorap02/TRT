package com.pfizer.webapp.chart;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.servlet.ChartDeleter;
import org.jfree.chart.servlet.ServletUtilities;

 
 /**
59:  * Servlet used for streaming charts to the client browser from the temporary
60:  * directory.  You need to add this servlet and mapping to your deployment 
61:  * descriptor (web.xml) in order to get it to work.  The syntax is as follows:
62:  * <xmp>
63:  * <servlet>
64:  *    <servlet-name>DisplayChart</servlet-name>
65:  *    <servlet-class>org.jfree.chart.servlet.DisplayChart</servlet-class>
66:  * </servlet>
67:  * <servlet-mapping>
68:  *     <servlet-name>DisplayChart</servlet-name>
69:  *     <url-pattern>/servlet/DisplayChart</url-pattern>
70:  * </servlet-mapping>
71:  * </xmp>
72:  */
 public class DisplayChart implements ServletRequestAware, ServletResponseAware {
 
     /**
76:      * Default constructor.
77:      */
	 
	 
	 HttpServletRequest request;
	 HttpServletResponse response;
	 private JFreeChart chart;
	 private String filename;
	 
	 
	 
	
	 public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	

	public JFreeChart getChart() {
		return chart;
	}

	/*public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}*/
	
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;

 	}
	
	public HttpServletRequest getServletRequest() {
		return this.request;
	}
	
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	public HttpServletResponse getServletResponse() {
		return response;
	}
	
	public HttpSession getSession() {
		return request.getSession();
	}
	
	public DisplayChart()  {
         super();
         
     }
 
     /**
83:      * Init method.
84:      *
85:      * @throws ServletException never.
86:      */
     public void init() throws ServletException {
         return;
     }
 
     /**
92:      * Service method.
93:      *
94:      * @param request  the request.
95:      * @param response  the response.
96:      *
97:      * @throws ServletException ??.
98:      * @throws IOException ??.
99:      */
    
     
     
     
     
     public void service()
              {
    	 
         
     }
 
     
     public void execute() throws ServletException, IOException,Exception {        // chart creation logic... 
    	 
    	 
    	 HttpSession session = request.getSession();
         String fileName = request.getParameter("filename");
       
 
         if (StringUtils.isBlank(fileName)) {
             throw new ServletException("Parameter 'filename' must be supplied");
         }
 
         //  Replace ".." with ""
         //  This is to prevent access to the rest of the file system
         fileName = ServletUtilities.searchReplace(fileName, "..", "");
        
         
         //  Check the file exists
         File file = new File(System.getProperty("java.io.tmpdir"), fileName);
         if (!file.exists()) {
             throw new ServletException("File '" + file.getAbsolutePath() 
                     + "' does not exist");
         }
        
         
         //  Check that the graph being served was created by the current user
         //  or that it begins with "public"
        boolean isChartInUserList = false;
         ChartDeleter chartDeleter = (ChartDeleter) session.getAttribute(
                 "JFreeChart_Deleter");
         if (chartDeleter != null) {
             isChartInUserList = chartDeleter.isChartAvailable(fileName);
         }
 
         boolean isChartPublic = false;
         if (fileName.length() >= 6) {
             if (fileName.substring(0, 6).equals("public")) {
                 isChartPublic = true;
             }
         }
        
         boolean isOneTimeChart = false;
         if (fileName.startsWith(ServletUtilities.getTempOneTimeFilePrefix())) {
             isOneTimeChart = true;   
         }
         
         if (isChartInUserList || isChartPublic || isOneTimeChart) {
             //  Serve it up
        	 System.out.println("File : " + file + " & Response : " + response);
             ServletUtilities.sendTempFile(file, response);
             if (isOneTimeChart) {
                 file.delete();   
             }
         }
         else {
             throw new ServletException("Chart image not found");
         }
         return ;    
    	 }

     

		
 }

