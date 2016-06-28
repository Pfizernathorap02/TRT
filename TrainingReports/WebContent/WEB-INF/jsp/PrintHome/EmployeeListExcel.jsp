<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.tgix.printing.EmployeeListBean"%>
<%@ page import="com.tgix.printing.LoggerHelper"%>
<%@ page import="com.tgix.Utils.PrintUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.StringTokenizer"%>
<%@ page import="java.util.Date" %>

<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>

<html>
<!-- <netui-data:getData resultId="employeeResultMap" value="{pageFlow.employeeReportMap}" />
<netui-data:getData resultId="employeeResultMapWithHSL" value="{pageFlow.employeeReportMapWithHSL}" />
<netui-data:getData resultId="employeeResultMapSpecial" value="{pageFlow.employeeReportMapSpecial}" />
   -->
   
                    

   <body>
    <%
    String mimeType = "application/vnd.ms-excel";
	response.setContentType(mimeType);
	
     String Selected_Date=request.getParameter("Selected_Date")==null?"":request.getParameter("Selected_Date").toString();
   
    String dateForFileName=removeSpaces(Selected_Date.trim().replace('/',' '));
    String fileName="TRM Order List -Date-"+dateForFileName.trim()+".xls";
    response.setHeader ("Content-Disposition","attachment; filename=\""+fileName+"\""); 
    %>
    <table width="100%">       
    <tr>
        <td>TRM Order Date:&nbsp;<b><%=Selected_Date%>&nbsp;&nbsp;</b></td>
    </tr>
    </table>
    <table border="1" cellpadding="2" cellspacing="0" bordercolor="#9AB9D7" width="100%">
        <tr>
        <td bgcolor="#EBEBE1" valign="top" nowrap>SNO.</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>TRM ORDER No.</td>        
        <td bgcolor="#EBEBE1" valign="top" nowrap>Products</td>        
        <td bgcolor="#EBEBE1" valign="top" nowrap>EmplID</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>First Name</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Last Name</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Address1</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Address2</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>City</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>State</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Zip</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>Training Material</td>
        <td bgcolor="#EBEBE1" valign="top" nowrap>TRM Order Date</td>        
        </tr>
        <%
        /*  Infosys code changes starts here
        TreeMap employeeReportMap=(TreeMap)pageContext.getAttribute("employeeResultMap");
        TreeMap employeeReportMapWithHSL=(TreeMap)pageContext.getAttribute("employeeResultMapWithHSL");
        TreeMap employeeReportMapSpecial=(TreeMap)pageContext.getAttribute("employeeResultMapSpecial");  */
        // new code added 
        HttpSession sess = request.getSession(false); //use false to use the existing session
        TreeMap employeeReportMap=	(TreeMap)sess.getAttribute("employeeReportMap"); 
		TreeMap employeeReportMapWithHSL=(TreeMap)sess.getAttribute("employeeReportMapWithHSL");
		TreeMap employeeReportMapSpecial=(TreeMap)sess.getAttribute("employeeReportMapSpecial");
	//	sess.invalidate(); // Kill the session after export excel
        /*Infosys code changes ends  here*/
        EmployeeListBean thisEmployeeListBean;
        List tempMaterialList;
        List tempProductList;     
        int i=0;
        String blankSpace="";
        if(employeeReportMap != null){
        for(Iterator iter=employeeReportMap.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
        
        %>
          <tr>
            <td  ><%=++i%></td>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("RBU");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
          <td ><%=PrintUtils.ifNullNBSP(sOrderNo)%></td>
        <%
          tempProductList=new ArrayList();
          tempProductList=thisEmployeeListBean.getAllProducts();
          String commaProduct="";
          String tempProducts="";
          String tempUniqueProducts="";
           for(int j=0;j<tempProductList.size();j++){
            String tempCurrentProduct = "" + tempProductList.get(j);
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) commaProduct=","; if(j>=3 && (j%3)==0) commaProduct="<br>";
            // This eliminate the Duplicate Product code being displayed on the screen
            if (tempProducts.indexOf(tempCurrentProduct) < 0){
                tempProducts=tempProducts+commaProduct+tempProductList.get(j);
            }
           }
           if(tempProducts!=null && !tempProducts.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempProducts)%></td>
            <%}%>                     
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%></td>
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
            <%
             Date orderDate = thisEmployeeListBean.getOrderDate();             
             SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
             //Date dateFormattedDate = com.tgix.Utils.Util.parseStandardUSDate(orderDate.getMonth() + "/" + orderDate.getDate() + "/" + orderDate .getYear());
             String sFormattedDate = format.format(orderDate);//orderDate..getMonth() + "/" + orderDate.getDate() + "/" + orderDate .getYear();
            %>
            
            <td nowrap><%=PrintUtils.ifNullNBSP(sFormattedDate)%>&nbsp;</td>             
         </tr>
        <%}
        }
        // For HSL
        if(employeeReportMapWithHSL != null){
        for(Iterator iter=employeeReportMapWithHSL.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMapWithHSL.get(iter.next());
        
        %>
          <tr>
            <td  ><%=++i%></td>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("RBU");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
          <td ><%=PrintUtils.ifNullNBSP(sOrderNo)%></td>
        <%
          tempProductList=new ArrayList();
          tempProductList=thisEmployeeListBean.getAllProducts();
          String commaProduct="";
          String tempProducts="";
          String tempUniqueProducts="";
           for(int j=0;j<tempProductList.size();j++){
            String tempCurrentProduct = "" + tempProductList.get(j);
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) commaProduct=","; if(j>=3 && (j%3)==0) commaProduct="<br>";
            // This eliminate the Duplicate Product code being displayed on the screen
            if (tempProducts.indexOf(tempCurrentProduct) < 0){
                tempProducts=tempProducts+commaProduct+tempProductList.get(j);
            }
           }
           if(tempProducts!=null && !tempProducts.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempProducts)%></td>
            <%}%>                     
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%></td>
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
            <%
             Date orderDate = thisEmployeeListBean.getOrderDate();             
             SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
             //Date dateFormattedDate = com.tgix.Utils.Util.parseStandardUSDate(orderDate.getMonth() + "/" + orderDate.getDate() + "/" + orderDate .getYear());
             String sFormattedDate = format.format(orderDate);//orderDate..getMonth() + "/" + orderDate.getDate() + "/" + orderDate .getYear();
            %>
            
            <td nowrap><%=PrintUtils.ifNullNBSP(sFormattedDate)%>&nbsp;</td>             
         </tr>
        <%}
        }
        // For special
         if(employeeReportMapSpecial != null){
        for(Iterator iter=employeeReportMapSpecial.keySet().iterator();iter.hasNext();){
            thisEmployeeListBean=(EmployeeListBean)employeeReportMapSpecial.get(iter.next());
        
        %>
          <tr>
            <td  ><%=++i%></td>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();

            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterPDF = sOrderNo.indexOf("RBU");
             if(iAfterPDF >= 0)
               sOrderNo = sOrderNo.substring(iAfterPDF + 3);
            }
          
          %>
          <td ><%=PrintUtils.ifNullNBSP(sOrderNo)%></td>
        <%
          tempProductList=new ArrayList();
          tempProductList=thisEmployeeListBean.getAllProducts();
          String commaProduct="";
          String tempProducts="";
          String tempUniqueProducts="";
           for(int j=0;j<tempProductList.size();j++){
            String tempCurrentProduct = "" + tempProductList.get(j);
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) commaProduct=","; if(j>=3 && (j%3)==0) commaProduct="<br>";
            // This eliminate the Duplicate Product code being displayed on the screen
            if (tempProducts.indexOf(tempCurrentProduct) < 0){
                tempProducts=tempProducts+commaProduct+tempProductList.get(j);
            }
           }
           if(tempProducts!=null && !tempProducts.equalsIgnoreCase("")){
           %>
           <td nowrap><%=PrintUtils.ifNullNBSP(tempProducts)%></td>
            <%}%>                     
            <td >&nbsp;<%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%></td>
            <td ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%></td>
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
            <%
             Date orderDate = thisEmployeeListBean.getOrderDate();             
             SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");             
             //Date dateFormattedDate = com.tgix.Utils.Util.parseStandardUSDate(orderDate.getMonth() + "/" + orderDate.getDate() + "/" + orderDate .getYear());
             String sFormattedDate = format.format(orderDate);//orderDate..getMonth() + "/" + orderDate.getDate() + "/" + orderDate .getYear();
            %>
            
            <td nowrap><%=PrintUtils.ifNullNBSP(sFormattedDate)%>&nbsp;</td>             
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
