<%@page import="javax.swing.ImageIcon"%>
<%@page import="java.awt.Image"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.URI"%>
<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.global.EmployeeInfoWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
	EmployeeInfoWc wc = (EmployeeInfoWc)request.getAttribute(EmployeeInfoWc.ATTRIBUTE_NAME);
%>


<!-- Commented for TRT Phase 2
<div class="stnd_hdr" style="margin-top:20px;margin-bottom:10px;font-size:14px">	
    <%//=wc.getEmployee().getPreferredName()%> <%=wc.getEmployee().getLastName()%> 
</div>-->
<table class="blue_table" >
    <tr>
        <th colspan="3" align="left">Employee Information</th>
    </tr>
    <tr class="even">
        <td style="font-weight: bold;">Employee Status</td>
        <td width="150px">
        <%
            String emplStatus=wc.getEmployee().getEmployeeStatus();
            if( emplStatus.equalsIgnoreCase( "On-Leave" ) || emplStatus.equalsIgnoreCase( "Terminated" ) ){ %>
                <font color="red"><b><%=emplStatus%></b></font>
        <%	} else { %>
                <%=emplStatus%>
        <%	} %>
         </td>
      <% if ( !Util.isEmpty( wc.getImage() ) ) {%>
            <td rowspan="13" valign="top"><img src="<%=wc.getImage()%>" /></td>
        <% } else { %>
            <td rowspan="13" >No Photo</td>
        <% } %>    
       
        	
    <tr class="odd">
        <td style="font-weight: bold;">Employee ID</td>
        <td ><%=wc.getEmployee().getEmplId()%></td>
        
    </tr>
        
    <tr class="even">
        <td style="font-weight: bold;">Full Name</td>
        <td><%=wc.getEmployee().getLastName()%>, <%=wc.getEmployee().getPreferredName()%></td>
    </tr>	
    <tr class="odd">
        <td  style="font-weight: bold;">Gender</td>
        <td><%=Util.toEmpty( wc.getEmployee().getGender() )%></td>
    </tr>	
    <tr class="even">
        <td  style="font-weight: bold;">Hire Date</td>
        <td><%=Util.formatDateShort( wc.getEmployee().getHireDate() ) %></td>
    </tr>	
<!--     <tr class="odd"> -->
<!--         <td style="font-weight: bold;">Promotion Date</td> -->
<%--         <td><%=Util.formatDateShort( wc.getEmployee().getPromoDate() ) %></td> --%>
<!--     </tr>	 -->
    <tr class="odd">
        <td style="font-weight: bold;">E-Mail</td>
        
        <td>
        <a href="mailto:<%=Util.toEmpty(wc.getEmployee().getEmail() )%>?subject=<%=wc.getEmailSubject()%>">	<%=Util.toEmpty(wc.getEmployee().getEmail() )%> </a>
        </td>
    </tr>	
    <tr class="even">
        <td style="font-weight: bold;">Business Unit</td>
        <td><%=Util.toEmpty( wc.getEmployee().getBusinessUnit() )%></td>
    </tr>	
    <tr class="odd">
        <td style="font-weight: bold;">Sales Organization</td>
        <td><%=Util.toEmpty(wc.getEmployee().getSalesOrgDesc() )%></td>
    </tr>	
    <tr class="even">
        <td style="font-weight: bold;">Role</td>
        <td><%=Util.toEmpty(wc.getEmployee().getRole() )%></td>
    </tr>	
  <%--  <!-- <tr class="even">
        <td style="font-weight: bold;">Geography Description</td>
        <td><%=Util.toEmpty(wc.getEmployee().getGeographyDesc() )%> </td>
    </tr>	
    <tr class="odd">
        <td style="font-weight: bold;">Geography Type</td>
        <td><%=Util.toEmpty(wc.getEmployee().getGeographyType() )%> </td>
    </tr>	--> --%>
    <tr class="odd">
        <td style="font-weight: bold;">Sales Position Id</td>
        <td><%=Util.toEmpty(wc.getEmployee().getSalesPositionId() )%> </td>
    </tr>	
    <tr class="even">
        <td style="font-weight: bold;">Sales Position Desc</td>
        <td><%=Util.toEmpty(wc.getEmployee().getSalesPostionDesc() )%> </td>
    </tr>
    <tr class="odd">
        <td style="font-weight: bold;">Reports to</td>
        <td>
        <% if ( wc.getManager() != null ) { %>
        <a href="mailto:<%=wc.getManager().getEmail()%>?subject=<%=wc.getEmailSubject()%>">	<%=wc.getManager().getLastName()%>, <%=wc.getManager().getPreferredName()%> </a>
        <% }else{ %> Vacant
        <%
        	} 
        %>
        </td>
    </tr>	
</table>
