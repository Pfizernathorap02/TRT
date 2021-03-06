<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
	"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="Content-Language" content="en-us" />
        <title>Training Reports</title>
        <meta name="ROBOTS" content="ALL" />
        <meta http-equiv="imagetoolbar" content="no" />
        <meta name="MSSmartTagsPreventParsing" content="true" />
        <meta name="Keywords" content="_KEYWORDS_" />
        <meta name="Description" content="_DESCRIPTION_" />
        <link href="<%=request.getContextPath()%>/resources/css/header.css" rel="stylesheet" type="text/css" media="all" />
        <link href="<%=request.getContextPath()%>/resources/css/trainning.css" rel="stylesheet" type="text/css" media="all" />
        <!--[if IE 6]>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/evaluation/resources/_css/ie-6.0.css" />
        <![endif]-->       
    </head>
    <body id="p-contact">
        <div id="wrap">
    
            <div id="top_header">
                <div id=header_logo></div>
                <div id=header_title>Training Reports</div>
                <!-- end #top_header -->
            </div>
            
            <table  class="no_space_table"   background="/TrainingReports/resources/images/h3_background.gif" style="background-repeat: repeat-y;" bgcolor="#eff7fc">
            <TR>
            <TD width="100%" valign="middle">
            <h3>Session Expired</h3>
            </TD>            
            </TR>
            </TABLE>
    
            
            
            <%
            String reqURL = request.getRequestURL().toString().toLowerCase();
            
            String thisURL="";
            //if((reqURL.toLowerCase().indexOf("sceint.pfizer.com")) > -1) thisURL="http://upint.pfizer.com/auth.cfm?Appid=2516";
            if((reqURL.indexOf("trt-tst.pfizer.com")) > -1) 
                thisURL="http://trt-tst.pfizer.com";
            //else if((reqURL.indexOf("wls") > -1))  
                //thisURL="http://upint.pfizer.com/auth.cfm?Appid=3001";
            else if(reqURL.indexOf("trt-stg.pfizer.com") > -1)  
                thisURL="http://trt-stg.pfizer.com";
            else if(reqURL.indexOf("trt-dev.pfizer.com") > -1)  
                thisURL="http://trt-dev.pfizer.com";
            else if(reqURL.indexOf("trt.pfizer.com") > -1)
                thisURL="http://trt.pfizer.com";
   
            %>
            <div id="main_content">
                <h4>Your session got expired, please click here <A HREF="<%=thisURL%>">Login</A> to login again.</h4>
                <div class="clear"></div>	
            </div> <!-- end #content -->
    
        </div><!-- end #wrap -->
    
    </body>

</html>