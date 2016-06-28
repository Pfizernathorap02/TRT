<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.tgix.printing.EmployeeListBean"%>
<%@ page import="com.tgix.Utils.PrintUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.StringTokenizer"%>
<%@ page import="java.util.Date" %>

<%-- <%-- Infosys code changes starts 
<%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%>  --%>
<!-- <netui:html> -->
<!-- Infosys code changes ends  -->

<html>
    <head>
        <title>
            Product Training (PSCPT) - Invitation Printing
        </title>
        <LINK href="/TrainingReports/resources/css/trainning.css" type="text/css" rel="STYLESHEET">
        <LINK href="/TrainingReports/resources/css/style.css" type="text/css" rel="STYLESHEET">
    <LINK href="/TrainingReports/resources/css/header.css"  type="text/css" rel="STYLESHEET">           
    </head>
    <script language="javascript">
    function openExcelPage(url){
    	
    window.open(url,'newExcel','height=0,width=0');
    return false;
    }
    
    function removeAttributes(){
     <%
   //  session.removeAttribute("excelEmployeeList");
     %>
    }
    
    </script>
    <body  onunload="removeAttributes();">
     <%
    response.setHeader("Cache-Control","no-store"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
    %>
        <jsp:include page="HeaderNoHomeLink.jsp" />
       <!-- Infosys code changes starts here 
        <netui-data:getData resultId="employeeResultMap" value="{pageFlow.employeeReportMap}" />
        <netui-data:getData resultId="employeeResultMapWithHSL" value="{pageFlow.employeeReportMapWithHSL}" />
        <netui-data:getData resultId="employeeResultMapSpecial" value="{pageFlow.employeeReportMapSpecial}" />

    
        <%
            String ALL_PROD=request.getAttribute("ALL_PROD")==null?"":request.getAttribute("ALL_PROD").toString();
            String Selected_Date=request.getAttribute("Selected_Date")==null?"":request.getAttribute("Selected_Date").toString();
            // Changes made for RBU
            String Selected_Box = request.getAttribute("Selected_Box")==null?"":request.getAttribute("Selected_Box").toString();
            List tempMaterialList;
            List tempProductList;            
        %>
        <table  width="800" bordercolor="#9AB9D7" border="0" cellspacing="0" style="margin: 25px 25px 25px 25px;">
        <tr>
        <td class="HeadingStyle" colspan="5"><b>Employee List&nbsp;&nbsp;</b></td>
        </tr>
        <tr>
        <td class="HeadingStyle" colspan="5">&nbsp;</td>
        </tr>
        <tr>
           <!-- <td>Cluster:&nbsp;<b>Powers&nbsp;&nbsp;</b></td>-->
            <td>TRM Order Date:&nbsp;<b><%=Selected_Date%>&nbsp;&nbsp;</b></td>
           <%--  Infosys code changes starts here
           <td  align="left" valign="middle"><a  href="#" onclick="openExcelPage('EmployeeListExcel.jsp?Selected_Date=<%=Selected_Date%>&Selected_Box=<%=Selected_Box%>');">Export To Excel&nbsp;</a>
             --%>
              <td  align="left" valign="middle"><a  href="#" onclick="openExcelPage('/TrainingReports/PrintHome/EmployeeListExcel.jsp?Selected_Date=<%=Selected_Date%>&Selected_Box=<%=Selected_Box%>');">Export To Excel&nbsp;</a>
            
            </td>
        </tr>
        </table>
        <br>
        <table width="100%" bordercolor="#9AB9D7" border="1" cellspacing="0" style="margin: 25px 25px 25px 25px;"> 
        <tr>
        <td class="ActionsTableHeader" nowrap>SNO.</td>
        <td class="ActionsTableHeader" nowrap>TRM ORDER No.</td>        
        <td class="ActionsTableHeader" nowrap>Products</td>        
        <td class="ActionsTableHeader" nowrap>EmplID</td>
        <td class="ActionsTableHeader" nowrap>First Name</td>
        <td class="ActionsTableHeader" nowrap>Last Name</td>        
        <td class="ActionsTableHeader" nowrap>Address1</td>
        <td class="ActionsTableHeader" nowrap>Address2</td>
        <td class="ActionsTableHeader" nowrap>City</td>
        <td class="ActionsTableHeader" nowrap>State</td>
        <td class="ActionsTableHeader" nowrap>Zip</td>
        <td class="ActionsTableHeader" nowrap>Training Material</td>
        <td class="ActionsTableHeader" nowrap>TRM Order Date</td>        
        </tr>
        <%
       /*  Infosys code changes starts
       TreeMap employeeReportMap=(TreeMap)pageContext.getAttribute("employeeResultMap");
        // Get the list for with HSL also
      
        TreeMap employeeReportMapWithHSL=(TreeMap)pageContext.getAttribute("employeeResultMapWithHSL");
        // Special handling
        TreeMap employeeReportMapSpecial=(TreeMap)pageContext.getAttribute("employeeResultMapSpecial"); 
        		 */
        		 
        		 TreeMap employeeReportMap=	(TreeMap)request.getAttribute("employeeReportMap");
        		 TreeMap employeeReportMapWithHSL=(TreeMap)request.getAttribute("employeeReportMapWithHSL");
        		 TreeMap employeeReportMapSpecial=(TreeMap)request.getAttribute("employeeReportMapSpecial");
        		 HttpSession sess = request.getSession(); 
        		 sess.setAttribute("employeeReportMap", employeeReportMap);
        		 sess.setAttribute("employeeReportMapWithHSL", employeeReportMapWithHSL);
        		 sess.setAttribute("employeeReportMapSpecial", employeeReportMapSpecial); 
        		 
        /* Infosys code changes  ends*/
        EmployeeListBean thisEmployeeListBean;
        boolean msgFlag=false;
        int i=0;
        String backGroundColor="";
        if(employeeReportMap != null){
        for(Iterator iter=employeeReportMap.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
        %>
          <tr bgcolor="<%=backGroundColor%>">
            <td nowrap ><%=++i%></td>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();
	    // Changes made by Jeevan for RBU	
            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterRBU = sOrderNo.indexOf("RBU");
             if(iAfterRBU >= 0)
               sOrderNo = sOrderNo.substring(iAfterRBU + 3);
            }
          
          %>
            
            
            <td nowrap><%=PrintUtils.ifNullNBSP(sOrderNo)%>&nbsp;</td>
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
            <td nowrap ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%>&nbsp;</td>            
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%>&nbsp;</td>
         <%
          tempMaterialList=new ArrayList();
          
          String sMaterials = thisEmployeeListBean.getMaterials();
          if(sMaterials!=null)          
          {
            StringTokenizer st = new StringTokenizer(sMaterials, ",");
            while(st.hasMoreTokens())
            {
              tempMaterialList.add((String)st.nextToken());            
            }
          }
                    
          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma="<br>";
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
        // Iterate over the with HSL list
        if(employeeReportMapWithHSL != null){
         for(Iterator iter=employeeReportMapWithHSL.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMapWithHSL.get(iter.next());
        %>
          <tr bgcolor="#ffcc66">
            <td nowrap ><%=++i%></td>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();
	    // Changes made by Jeevan for RBU	
            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterRBU = sOrderNo.indexOf("RBU");
             if(iAfterRBU >= 0)
               sOrderNo = sOrderNo.substring(iAfterRBU + 3);
            }
          
          %>
            <td nowrap><%=PrintUtils.ifNullNBSP(sOrderNo)%>&nbsp;</td>
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
            <td nowrap ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%>&nbsp;</td>            
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%>&nbsp;</td>
         <%
          tempMaterialList=new ArrayList();
          
          String sMaterials = thisEmployeeListBean.getMaterials();
          if(sMaterials!=null)          
          {
            StringTokenizer st = new StringTokenizer(sMaterials, ",");
            while(st.hasMoreTokens())
            {
              tempMaterialList.add((String)st.nextToken());            
            }
          }
                    
          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma="<br>";
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
        
        // Handling of special case
        
        if(employeeReportMapSpecial != null){
        for(Iterator iter=employeeReportMapSpecial.keySet().iterator();iter.hasNext();){
           // thisEmployeeListBean=(EmployeeListBean)employeeReportMap.get(iter.next());
            thisEmployeeListBean=(EmployeeListBean)employeeReportMapSpecial.get(iter.next());
        %>
          <tr bgcolor="#ffcc66">
            <td nowrap ><%=++i%></td>
          <%
            String sOrderNo = thisEmployeeListBean.getOrderNumber();
	    // Changes made by Jeevan for RBU	
            if(sOrderNo != null && sOrderNo.length() > 0)
            {                            
             sOrderNo = sOrderNo.trim();
             int iAfterRBU = sOrderNo.indexOf("RBU");
             if(iAfterRBU >= 0)
               sOrderNo = sOrderNo.substring(iAfterRBU + 3);
            }
          
          %>
            
            
            <td nowrap><%=PrintUtils.ifNullNBSP(sOrderNo)%>&nbsp;</td>
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
            <td nowrap ><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getEmplID())%>&nbsp;</td>            
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getFirstName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getLastName())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd1())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipAdd2())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipCity())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipState())%>&nbsp;</td>
            <td nowrap><%=PrintUtils.ifNullNBSP(thisEmployeeListBean.getShipZip())%>&nbsp;</td>
         <%
          tempMaterialList=new ArrayList();
          
          String sMaterials = thisEmployeeListBean.getMaterials();
          if(sMaterials!=null)          
          {
            StringTokenizer st = new StringTokenizer(sMaterials, ",");
            while(st.hasMoreTokens())
            {
              tempMaterialList.add((String)st.nextToken());            
            }
          }
                    
          String comma="";
          String tempMaterials="";
           for(int j=0;j<tempMaterialList.size();j++){
           //This will let us Print first three material code comma seprated and the 4th Material will be printed on the next line
           if(j>=1) comma=","; if(j>=3 && (j%3)==0) comma="<br>";
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
        
      //  session.setAttribute("excelEmployeeList",employeeReportMap); //Set the Attribute to be Used in EXCEL Report
       // employeeReportMap=null; //Lets make it eligible for Garbage Collection
        %>
        </table>
        <%
         if(msgFlag){
            %>
            <br>
            <b><i>NOTE : Row(s) in 'Yellow' represents 'RM' records.</i></b>
            <%
         }
        %>
        
        <jsp:include page="/WEB-INF/jsp/Footer.jsp" />
    </body>
<!-- </netui:html> -->

</html>
