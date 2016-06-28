<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserSession"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.TrainingPathDisplayWc"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%-- 
Infosys Code changes starts here

<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>



 <html>
    
    <%
    TrainingPathDisplayWc pathWc = (TrainingPathDisplayWc)request.getAttribute(TrainingPathDisplayWc.ATTRIBUTE_NAME);
    System.out.println("Message"+pathWc.getMessage());
    List pathList = pathWc.getTrainingPath();
    UserSession uSession = UserSession.getUserSession(request);	
	String user = pathWc.getUser();
	UserTerritory uTerritory = null;
	//if ( user != null) {
	//	uTerritory = user.getUserTerritory();
	//}
    System.out.println("Empl in jsp="+user);
    %>
    <body>
    
        <p>
           
           <table class="blue_table" width="100%" style="font-size:12" >
        <tr><th align="left" style="font-size:12" >Training Path</th></tr>
        <tr><td>
        <%if(pathWc.getMessage() != null){%>
            <%=pathWc.getMessage()%>
          <%}else {%>
          <TABLE class=blue_table_without_border>
  <TBODY>
<TR>
<TD width="8%" style="BORDER: 0px">
</TD>
<TD width="7%" style="BORDER: 0px">
<IMG src="<%=AppConst.APP_ROOT%>/resources/images/completelegend.gif">
</TD>
<TD width="18%" style="BORDER: 0px;font-size:12">Completed
</TD>
<TD width="7%" style="BORDER: 0px">
<IMG src="<%=AppConst.APP_ROOT%>/resources/images/registeredlegend.gif">
</TD>
<TD width="22%" style="BORDER: 0px;font-size:12">In Progress
</TD>
<TD width="7%" style="BORDER: 0px">
<IMG src="<%=AppConst.APP_ROOT%>/resources/images/pendinglegend.gif">
</TD>
<TD width="25%" style="BORDER: 0px;font-size:12">Not Started
</TD>
<TD width="6%" style="BORDER: 0px">
</TD>


</TR>
</TBODY>
</TABLE>
          
          
          
          
          
        <table class="blue_table_without_border">
        <tr style="border:1px">
           <% Iterator pathIterator = pathList.iterator(); 
           int count = 0;
           String previousStatus = "";
           String currentStatus = "";
           String newconfigID = "";
           String oldconfigID = "";
           while(pathIterator.hasNext()){
                Map pathDetails=(Map)pathIterator.next();
                String status = (String)pathDetails.get("STATUS");
                //status="Registered";
                String activityId = (String)pathDetails.get("CODE");
                String activityDesc = (String)pathDetails.get("DESCRIPTION");
                   // Code added by Neha
                newconfigID = pathDetails.get("CONFIG_ID").toString();
                if(oldconfigID.equals("") && !newconfigID.equals("") ){
                    oldconfigID=newconfigID;
                }
                if(!oldconfigID.equals(newconfigID) ){%>
                <TD style="BORDER: 0px;WIDTH: 21px; BORDER-BOTTOM: 0px" vAlign=top>
                <IMG style="POSITION: absolute" src="<%=AppConst.APP_ROOT%>/resources/images/<%=currentStatus%>start.gif">
                </TD>
                
                
                    </tr><tr>
                <%    count = 0;
                      oldconfigID= newconfigID; 
                }
                //-------
                System.out.println("Code="+(String)pathDetails.get("CODE"));
                System.out.println("Status="+(String)pathDetails.get("STATUS"));
                currentStatus = pathDetails.get("STATUS").toString().trim().toLowerCase();
                if (currentStatus.length() == 0) currentStatus = "pending";
                if (count == 0){
                %>
                <TD style="BORDER: 0px; WIDTH: 13px; BORDER-BOTTOM: 0px" vAlign=top>
                <IMG style="POSITION: absolute" src="<%=AppConst.APP_ROOT%>/resources/images/<%=currentStatus%>end.gif">
                </TD>
                <%
                }else{
                    %>
                <TD style="BORDER: 0px; WIDTH: 21px; BORDER-BOTTOM: 0px" vAlign=top>
                <IMG style="POSITION: absolute" src="<%=AppConst.APP_ROOT%>/resources/images/<%=previousStatus%><%=currentStatus%>.gif">
                </TD>                    
                <%
                    }
                if(("Complete").equals(status) || (("Exempt").equals(status)) || ("Waived").equalsIgnoreCase(status)){
                    // display image with dark blue.
                    %>
                    
                    <td style="width:45px;border:0px" valign="top">
                    <img src="<%=AppConst.APP_ROOT%>/resources/images/complete.gif" style="position:absolute;z-index:0;"/>
                    <div style="width:45;height:40;position:relative;top:1px;left:0px;overflow:hidden;font-size:12" 
                    title="<%=activityDesc%>">
                   <label><a href="/TrainingReports/p2l/employeeSearchDetailPage?activitypk=<%=activityId%>&emplid=<%=user%>"><font color="white"><%=activityDesc%></font></a></label>
                    </div> 
                    </td> 
                    <%
                } else if (("Registered").equals(status) || ("In Progress").equals(status)){
                    // display image with light blue.
                    %>
                    
                    <td style="width:45px;border:0px" valign="top">
                                            
                    <img src="<%=AppConst.APP_ROOT%>/resources/images/registered.gif" style="position:absolute;z-index:0;"/>
                    <div style="width:45;height:40;position:relative;top:1px;left:0px;overflow:hidden;font-size:12" 
                    title="<%=activityDesc%>">
                   <label><a href="/TrainingReports/p2l/employeeSearchDetailPage?activitypk=<%=activityId%>&emplid=<%=user%>"><font color="white"><%=activityDesc%></font></a></label>
                    </div>
                    </td>
                    
                    <%
                } else if ((("Cancelled").equals(status)) || (("Pending").equals(status)) || (("Assigned").equals(status)) || ("No Show").equals(status)  ||  (("").equals(status))){
                    // display image with no color.
                    %>
                    
                    <td style="width:45px;border:0px" valign="top">
                    
                    <img src="<%=AppConst.APP_ROOT%>/resources/images/pending.gif" style="position:absolute;z-index:0;"/>
                    <div style="width:45;height:40;position:relative;top:1px;left:0px;overflow:hidden;font-size:12" 
                    title="<%=activityDesc%>">
                   <label> <font color="black"><%=activityDesc%></font></label>
                    </div> 
                    </td>
                    <%
                }
                count++;
                
                if(count ==4){%>
                <TD style="BORDER: 0px;WIDTH: 21px; BORDER-BOTTOM: 0px" vAlign=top>
                <IMG style="POSITION: absolute" src="<%=AppConst.APP_ROOT%>/resources/images/<%=currentStatus%>start.gif">
                </TD>
                
                
                    </tr><tr>
                <%    count = 0; 
                }
                previousStatus = currentStatus;
           }
           if (count != 0){
            %>
            <TD style="BORDER: 0px;WIDTH: 21px; BORDER-BOTTOM: 0px" vAlign=top>
            <IMG style="POSITION: absolute" src="<%=AppConst.APP_ROOT%>/resources/images/<%=currentStatus%>start.gif">
            </TD>
            <%
           }
           
           %>
           </tr>
        </table>
        <%}%>
        </td></tr>
        
           </table>
        </p>
    </body>
  
</html>