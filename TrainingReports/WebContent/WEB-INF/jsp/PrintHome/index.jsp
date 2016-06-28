<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%--  <%@ page import="com.bea.wlw.netui.pageflow.util.PageflowTagUtils"%> --%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
<netui:html>  --%>
<html>
    <head>
        <title>
            Print Invitation Home
        </title>
        <LINK href="/EFTApplicationWeb/jsp/eft.css" type="text/css" rel="STYLESHEET">
        <LINK href="/EFTApplicationWeb/jsp/style.css" type="text/css" rel="STYLESHEET">
    </head>
        <script language="javascript">
        
    </script>
    <body>
    <jsp:include page="Header.jsp" >
    <jsp:param name="MenuType" value="Admin" />
    </jsp:include>
    
    <br>
       
       <table width="300" cellspacing="0" border="1" bordercolor="#9AB9D7">
        <tr>
            <td bgcolor="#003366" >
                <b><font color="#FFFFFF" size="2">
                TRM
                </font></b>
            </td>
            
        </tr>
        <tr>
            <td  colspan="2">
            <br>
                <%-- <p>&nbsp;&nbsp;&nbsp;<img border="0" src="/EFTApplicationWeb/jsp/images/arrow1.gif">&nbsp;&nbsp;<a href="<%=PageflowTagUtils.getRewrittenFormAction("printInvitation", pageContext)%>">Print Invitations</a></p> --%>                                                                        
                <br>
           </td>
       </tr>
       </table>
    
    <jsp:include page="/WEB-INF/jsp/Footer.jsp"/>
        
    </body>
<%-- </netui:html> --%>
</html>