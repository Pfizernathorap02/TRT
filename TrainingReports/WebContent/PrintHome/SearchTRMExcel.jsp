<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.tgix.printing.EmployeeListBean"%>
<%@ page import="com.tgix.Utils.PrintUtils"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.StringTokenizer"%>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>
 <netui-data:getData resultId="trmGroupMap" value="{pageFlow.searchTRMEmployeeMap}" />
 <netui-data:getData resultId="trmGroupMapWithHSL" value="{pageFlow.searchTRMEmployeeMapWithHSL}" /> --%>
<html>
    <head>
        <title>
            Product Training (PSCPT) - Search Excel Result
        </title>
    </head>
    <body>
       <%
        String mimeType = "application/vnd.ms-excel";
        response.setContentType(mimeType);
        String fileName=session.getAttribute("fileNameExcel")==null?"EmployeeList":session.getAttribute("fileNameExcel").toString();
        fileName=fileName.replace('/',' ');
        fileName=removeSpaces(fileName);
        String msgtoDisplay=session.getAttribute("printMsgToDisp")==null?"EmployeeList":session.getAttribute("printMsgToDisp").toString();
        msgtoDisplay="Result "+msgtoDisplay;
        
       response.setHeader ("Content-Disposition","attachment; filename=\""+fileName.trim()+".xls"+"\""); 
       %>
       <table width="100%">
       
    <tr>
       <td colspan="4">
       <%=msgtoDisplay%>
        </td>
    </tr>
    </table>
       <table border="1" cellpadding="2" cellspacing="0" bordercolor="#9AB9D7" width="100%">
        <tr>
        <td bgcolor="#EBEBE1" valign="top" nowrap>SNO.</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>EmplID</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>First Name</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Last Name</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>TRM ORDER No.</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Address1</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Address2</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>City</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>State</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Zip</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Training Material</td>
        </tr>
        <%
        /* 
        Infosys code changes starts here
        TreeMap employeeReportMap=(TreeMap)pageContext.getAttribute("trmGroupMap");
        TreeMap employeeReportMapWithHSL=(TreeMap)pageContext.getAttribute("trmGroupMapWithHSL"); */
        
        HttpSession sess = request.getSession(false); //use false to use the existing session
        TreeMap employeeReportMap=(TreeMap)sess.getAttribute("employeeReportMap");
        TreeMap employeeReportMapWithHSL=(TreeMap)sess.getAttribute("employeeResultMapWithHSL");
       
        /* Infosys code changes ends here */
        EmployeeListBean thisEmployeeListBean;
        List tempMaterialList;
        int i=0;
        String blankSpace="";
        if(employeeReportMap != null){
        for(Iterator iter=employeeReportMap.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());        
        %>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("PDF");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
                  
          <tr>
            <td  ><%=++i%></td>
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(sOrderNo)%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%></td>
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%></td>
        <%
          tempMaterialList=new ArrayList();



          String sMaterials = thisEmployeeListBean.getMaterials();
          StringTokenizer st = new StringTokenizer(sMaterials, ",");
          while(st.hasMoreTokens())
          {
            tempMaterialList.add((String)st.nextToken());            
          }


          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma=",";
           tempMaterials=tempMaterials+comma+tempMaterialList.get(j);
           }
           if(tempMaterials!=null && !tempMaterials.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempMaterials)%></td>
            <%}%>
         </tr>
        <%}
        }
        // For HSL
         if(employeeReportMapWithHSL != null){
        for(Iterator iter=employeeReportMapWithHSL.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMapWithHSL.get(iter.next());        
        %>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("PDF");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
                  
          <tr>
            <td  ><%=++i%></td>
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(sOrderNo)%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%></td>
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%></td>
        <%
          tempMaterialList=new ArrayList();



          String sMaterials = thisEmployeeListBean.getMaterials();
          StringTokenizer st = new StringTokenizer(sMaterials, ",");
          while(st.hasMoreTokens())
          {
            tempMaterialList.add((String)st.nextToken());            
          }


          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma=",";
           tempMaterials=tempMaterials+comma+tempMaterialList.get(j);
           }
           if(tempMaterials!=null && !tempMaterials.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempMaterials)%></td>
            <%}%>
         </tr>
        <%}
        }
        %>
        </table>
    </body>
</html>
<%!

public String removeSpaces(String s) {
  StringTokenizer st = new StringTokenizer(s," ",false);
  String t="";
  while (st.hasMoreElements()) t += st.nextElement();
  return t;
  }



%>