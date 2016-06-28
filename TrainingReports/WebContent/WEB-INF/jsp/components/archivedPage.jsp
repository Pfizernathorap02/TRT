<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.wc.components.archivedPageWc"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%--  Infosys migrated code changes starts 
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
<netui:html> --%>
<html>
<%--  Infosys migrated code changes ends  --%>
<%            
	archivedPageWc wc = (archivedPageWc)request.getAttribute(archivedPageWc.ATTRIBUTE_NAME);
    
%>
<head>
        
</head>
<body>
    <TABLE class="basic_table"> 
        <TR>
        <TD rowspan="2"><IMG src="/TrainingReports/resources/images/spacer.gif" width="15"></TD>
        <TD align="left">
            <div id=header_title style="color:#1f61a9;">Welcome to Training Reporting Tool</div>
        </TD>
        <td>
        <table align="right" class="blue_table_without_border" width="70%">
        <!-- Infosys code changes starts <tr><td><a href="/TrainingReports/reportselect.do">Training Reports</a></td></tr> -->
        <tr><td><a href="/TrainingReports/reportselect">Training Reports</a></td></tr>
       <!-- Infosys code changes ends %-->
        </table>
        </td>
        </TR>
        <TR>
        <TD>
        <h4>Please select Report Type:</h4>
        <table><tr><td>
        <table class="blue_table" align="left" width="350px">
        
        <tr>
        <%
          List labelList=wc.getLabelList();
          if(labelList!=null){
             Map map=new HashMap();
             map=(HashMap)labelList.get(0);
           
        %>
        
        <th align="left">
        <%=map.get("TRAINING_REPORT_LABEL")%> Archive
        </th>
        <th align="right">
         <%if(wc.getUser().isSuperAdmin()){%>
        <a href="/TrainingReports/adminHome/editArchive?id=<%=wc.getHeaderId()%>&name=<%=map.get("TRAINING_REPORT_LABEL")%>">
        <font style="color:white">[edit]</font></a> 
        <%}%>
        </th>
        <%}%>
        </tr>
        
        
        <tr><td colspan="2">
        <table border="0">
        <tr><td style="border:0">
        <%
            List labelUrlList=wc.getlabelUrlList();
            if(labelUrlList!=null){
              if(labelUrlList.size()!=0){  
            for(int i=0;i<labelUrlList.size();i++){
                Map map=new HashMap();
                map=(HashMap)labelUrlList.get(i);
                String url = "";
                if (map.get("TRAINING_REPORT_URL") != null)
                {
                url = map.get("TRAINING_REPORT_URL").toString();
                }
                String trackLabel = "";
                if (map.get("TRAINING_REPORT_LABEL") != null){
                    trackLabel = map.get("TRAINING_REPORT_LABEL").toString();
                }
                String trackId = "";
                if (map.get("TRACK_ID") != null){
                    trackId = map.get("TRACK_ID").toString();
                }
                String level = "";
                if (map.get("LEVEL") != null){
                    level = map.get("LEVEL").toString();
                }
                int groupLevel = (new Integer(level)).intValue();
                if (trackId.startsWith("GROUP")){
                    %> <A style="font-weight:bold"><%=trackLabel%></A><%}
                else{
            %>
            <div><a href="<%=url%>" <% if (groupLevel == 2){%> style="font-style:italic">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%}else{%>><%}%><%=trackLabel%></a></div>
                <%}
                }
        }else{%>
        No Archived Reports.
        <%}}%>
        
        </td>
        </tr>
        </TABLE>
        
        </td></tr>
        </table>
        
        </td></tr>
        </table>
        
        </TD>
        </TR>
        </TABLE>
</body>
<%--  
Infosys migrated code changes starts 
</netui:html> --%>
</html>
<%--  Infosys migrated code changes ends  --%>