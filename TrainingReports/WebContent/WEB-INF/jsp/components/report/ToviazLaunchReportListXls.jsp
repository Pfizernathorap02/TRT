
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.POAReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.RBUReportList"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="com.tgix.rbu.ProductDataBean"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>


<html>
           <%
           response.setContentType("application/vnd.ms-excel"); 
          // pageContext.setAttribute("exceldownload","YES"); 
           response.setHeader ("Content-Disposition","attachment; filename=\"CompletionSummary.xls\""); 
           response.setHeader("Cache-Control","max-age=0");
           response.setHeader("Pragma","public");
           String xlsType=(String)session.getAttribute("xlsType");
          // System.out.println("########### " + xlsType);
           String section=(String)session.getAttribute("section");  
            AppQueryStrings qString = new AppQueryStrings();
	        FormUtil.loadObject(request,qString);
             List exams = new ArrayList();
            if(session.getAttribute("exams") != null){
            exams = (List)session.getAttribute("exams");
        }
	     // String type=qString.getType();
        
           %>

<table cellspacing="0" id="employee_table" width="100%">
  <tr>
	
			
		
        <th nowrap>Future BU</th>
        <th nowrap>Future RBU</th>
			<th nowrap>Last Name</th>
			<th nowrap>First Name</th>
			<th nowrap>Emplid</th>
       <%
           if(!"Attendance".equalsIgnoreCase(xlsType)){
               Iterator iter = exams.iterator();
                 while(iter.hasNext()){
                        String activityName   = (String)iter.next();
            %>
            
            <th nowrap><b><%=activityName%></b></th>
			<%
                }
             %>
           <%     
           }
            %>
        </tr>
        

 
        <%
         Employee[] employeeBean=(Employee[])session.getAttribute("xlsBean");
          HashMap empHashMap=new HashMap();
         Employee emp;
         for(int i=0;i<employeeBean.length;i++){
         emp=employeeBean[i];
          String trClass = "";
           String b="<b>";
         String bb="</b>";
         boolean doFlag = false;  // flag to determine of a row should be shown
          if (!doFlag) { 	
			
				if ( "VP".equals( emp.getRole() ) ) {
					trClass = "class='active_row avp_row'";
				}
				if ( "RM".equals( emp.getRole() ) || "ARM".equals( emp.getRole() ) ) {
					trClass = "class='active_row rm_row'";
				}
				if ( "DM".equals( emp.getRole() ) ) {
					trClass = "class='active_row dm_row'";
				}
         }
         
         %>
         <tr  >
				<td><%=(emp.getFutureBU()==null)?"":emp.getFutureBU()%></td>
                <td><%=(emp.getFutureRBU()==null)?"":emp.getFutureRBU()%></td>
                <td><%=emp.getLastName()%></td>
				<td><%=emp.getFirstName()%></td>
				<td>&nbsp;<%=emp.getEmplId()%></td>
                <%--
                 <%
               if(!"Attendance".equalsIgnoreCase(xlsType)){
                    List examScores = emp.getAvailableExams();
                    empHashMap=emp.getProductStatusMap();
                    if(exams != null)
                    {
                           Iterator iter = exams.iterator();
                            while(iter.hasNext()){
                            String exam   = (String)iter.next();
                            Iterator examIter = examScores.iterator();
                            boolean found = false;                            
                            while(examIter.hasNext()){
                                HashMap map = (HashMap)examIter.next();
                                if(map.containsKey(exam.trim())){
                                    found = true;
                                String score = Util.toEmptyBlank((String)map.get(exam));
                                String font ="black";
                                if(score != null && !exam.equals("Attendance") && !score.equals("")){
                                    if(Integer.parseInt(score) < 80){
                                        font = "red";
                                    }
                                    else{
                                        font = "black";
                                    }
                                }
                    %>
                     <td align="center"><font color="<%=font%>"><%=Util.toEmptyNotComplete((String)map.get(exam))%></font></td>
                    <%
                                }
                            }
                            if(!found){
                     %>           
                        <td align="center">N/A</td>
                       <%         
                            }    
                        }
                }
                %>
                
                 
               <%  
                }
                %>
               --%> 
                <%
                    if(!"Attendance".equalsIgnoreCase(xlsType)){
                    String ped1 = emp.getPed1();
                    String font1 ="black";
                    if(ped1 != null && !ped1.equals("Not Complete") && !ped1.equals("On Leave")){
                       if(Integer.parseInt(ped1) < 80){
                           font1 = "red";
                        }
                        else{
                            font1 = "black";
                        }
                        
                    }
                    
                    String ped2 = emp.getPed1();
                    String font2 ="black";
                    if(ped2 != null && !ped2.equals("Not Complete") && !ped2.equals("On Leave")){
                       if(Integer.parseInt(ped2) < 80){
                           font2 = "red";
                        }
                        else{
                            font2 = "black";
                        }
                        
                    } 
                     
                %>
                <%
                   if("Overall".equalsIgnoreCase(xlsType) || "Ped1".equalsIgnoreCase(xlsType)){
                %>
                <td bgcolor="#FFF39C"><font color="<%=font1%>"><%=b%><%=emp.getPed1()%><%=bb%></font></td>
                <%
                   }
                   if("Overall".equalsIgnoreCase(xlsType) || "Ped2".equalsIgnoreCase(xlsType)){
                %>
                <td bgcolor="#FFF39C"><font color="<%=font2%>"><%=b%><%=emp.getPed2()%><%=bb%></font></td>
                <%
                   }
                   }
                   if("Overall".equalsIgnoreCase(xlsType)){
                %>
                    <td bgcolor="#FFF39C"><%=b%><%=emp.getAttendance()%><%=bb%></td> 
                <%
                   }
                %>
                
                </tr>
                <%
         }
                %>
        
</table>
    </body>
</html>