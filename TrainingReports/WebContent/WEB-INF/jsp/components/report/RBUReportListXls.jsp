
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
<%@ page import="java.util.*"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>


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
	        ProductDataBean thisProductDataBean[] = null;
            if(session.getAttribute("product") != null){
                thisProductDataBean = (ProductDataBean[])session.getAttribute("product");
              //  System.out.println("###############" + thisProductDataBean.length);
            }
            AppQueryStrings qString = new AppQueryStrings();
	        FormUtil.loadObject(request,qString);
	     // String type=qString.getType();
        
	      List examList = new ArrayList();
	      if(!xlsType.equalsIgnoreCase("Overall")){
	            if(session.getAttribute("examList") != null){
	                examList = (List)session.getAttribute("examList");
	                session.removeAttribute("examList");
	            }
      }
        
           %>

<table cellspacing="0" id="employee_table" width="100%">
  <tr>
	
			
		
        <th nowrap>Future BU</th>
        <th nowrap>Future RBU</th>
			<th nowrap>Last Name</th>
			<th nowrap>First Name</th>
			<th nowrap>Emplid</th>
       <%if("Overall".equalsIgnoreCase(xlsType)){
                        if(thisProductDataBean != null){
                        for(int i=0;i<thisProductDataBean.length;i++){
                %>
                    <th nowrap><b><%=thisProductDataBean[i].getProductDesc()%></b></th>            
            <%
                }
                }
            }
           if(!"Overall".equalsIgnoreCase(xlsType)){
                Iterator iter = examList.iterator();
                while(iter.hasNext()){
            %>
            
            <th nowrap><b><%=(String)iter.next()%></b></th>
			<%
                }
             %>
             <th>OverAll</th>
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
                
                 <%if("Overall".equalsIgnoreCase(xlsType)){
                    empHashMap=emp.getProductStatusMap();
                    if(thisProductDataBean != null){
                        for(int k=0;k<thisProductDataBean.length;k++){
                    %>
                    <td ><%=Util.toEmptyNA((String)empHashMap.get(thisProductDataBean[k].getProductCd()))%></td>
                <%}
                    }
                    }
                else{
                    List examSocres = emp.getAvailableExams();
                    empHashMap=emp.getProductStatusMap();
                    if(examList != null)
                    {
                         Iterator iter = examList.iterator();
                        while(iter.hasNext()){
                            String exam = (String)iter.next();
                           // System.out.println("Exam >>>>>>>>>> " + exam);
                            Iterator examIter = examSocres.iterator();
                            boolean found = false;                            
                             boolean recordPresent = false;                              
                            while(examIter.hasNext()){
                                HashMap map = (HashMap)examIter.next();
                                 if(map.size() > 0){
                                    recordPresent = true;
                                }
                                if(map.containsKey(exam)){
                                    found = true;
                                String score = Util.toEmptyBlank((String)map.get(exam));
                                String font ="black";
                                if(score != null && !exam.equals("SCE") && !score.equals("")){
                                    if(Integer.parseInt(score) < 80){
                                        font = "red";
                                    }
                                    else{
                                        font = "black";
                                    }
                                }
                          if(section != null && section.equals("On Leave")){        
                    %>
                     <td align="center"><font color="<%=font%>"><%=Util.toEmptyL((String)map.get(exam))%></font></td>
                    <%
                       }
                       else{
                        
                     %>
                     <td align="center"><font color="<%=font%>"><%=Util.toEmptyNC((String)map.get(exam))%></font></td>
                     
                     <%
                       }       }
                            }
                            
                            
                       if(!found && !exam.equals("SCE")){
                         if(section != null && section.equals("On Leave")){         
                     %>           
                        <td align="center">L</td>
                       <% 
                           }       
                          else{
                        %>    
                         <td align="center">NC</td>
                          
                        <%
                          }   
                               
                            }    
                           if(!found && exam.equals("SCE")){
                           if(recordPresent){              
                         %>           
                            <td align="center">N/A</td>
                           <%  
                               }       
                              else{
                                 if(section.equals("On Leave")){
                            %>    
                              <td align="center">L</td>
                              
                            <%
                                 }
                                 else{
                                    
                            %>
                                <td align="center">NC</td>
                                
                            <%        
                                 }
                              }
                        }  
                               
                        }
                }
                %>
                
                 <td><%=section%><%=bb%></td>
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