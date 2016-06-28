<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.GapAnalysisEntry"%>
<%@ page import="com.pfizer.db.GapAnalysisUIEntry"%>
<%@ page import="java.util.Map"%>;
<%@ page import="java.util.Iterator"%>;
<%@ page import="java.util.HashMap"%>;
<%@ page import="java.util.Date"%>;
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%@ page import="com.pfizer.webapp.wc.components.GapAnalysisMainWc"%>
<%@ page import="com.tgix.Utils.Util"%>

<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	GapAnalysisMainWc wc = (GapAnalysisMainWc)request.getAttribute(GapAnalysisMainWc.ATTRIBUTE_NAME);

%>

<html>
    <head>
        <title>
            Gap Analysis Report
        </title>
    </head>
    <body>
    
    
        <table cellspacing="0" id="tsr_table" width="100%" class="blue_table">
        <tr>
            <th nowrap>Employee ID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>GUID</th>
            <th nowrap>Email Address</th>
            <th nowrap>Role Code</th>
            <th nowrap>Sales Org</th>    
            <th nowrap>Sales Org Code</th>
            <th nowrap>Manager First Name</th>
            <th nowrap>Manager Last Name</th>
            <th nowrap>Manager Email ID</th>
            <% 
            //String[] productDesc=wc.getProductDesc();
             String[] productDesc=wc.getSelectedProdDesc();
             String tempStr="";
             for(int i=0;i<productDesc.length;i++)
             {
                tempStr=tempStr+productDesc[i];
             }
             System.out.println("Before replace :: "+tempStr);
             tempStr=tempStr.replaceAll("'","");
             System.out.println("after replace :: "+tempStr);
             
             productDesc=tempStr.split(",");
             
              for(int i=0;i<productDesc.length;i++)
             {
                System.out.println("productDesc :: "+productDesc[i]);
             }
             
             
            int[] productTotals = new int[productDesc.length]; 
            int grandTotal = 0;   
            for(int i=0;i<productDesc.length;i++){
            %>
            <th><%=productDesc[i]%></th>
            <% productTotals[i] = 0;}	   
            %> 
            <th>Gap Count</th>
          </tr>
          
          
             <%  boolean oddEvenFlag=false;
                GapAnalysisUIEntry uiEntry = null;
                GapAnalysisEntry empDet = null;
                Iterator it = wc.getResult().keySet().iterator();
                while(it.hasNext())  { 
                    oddEvenFlag = !oddEvenFlag; 
                    uiEntry = (GapAnalysisUIEntry)wc.getResult().get(it.next());
                    empDet = uiEntry.getEntry();
            %>
            <tr class="<%=oddEvenFlag?"even":"odd"%>">
                <td><%=Util.toEmpty(empDet.getEmpID())%></td>
                <td><%=Util.toEmpty(empDet.getFirstName())%> </td>
                <td><%=Util.toEmpty(empDet.getLastName())%></td>
                <td><%=Util.toEmpty(empDet.getGuID())%></td>
                <td><%=Util.toEmpty(empDet.getEmailAddr())%></td>
                <td><%=Util.toEmpty(empDet.getRolecd())%></td>
                <td><%=Util.toEmpty(empDet.getSalesOrg())%></td>
                <td><%=Util.toEmpty(empDet.getSalesOrgCd())%></td>
                 <td><%=Util.toEmpty(empDet.getMngrFirstName())%></td>
                  <td><%=Util.toEmpty(empDet.getMngrLastName())%></td>
                   <td><%=Util.toEmpty(empDet.getMngrEmail())%></td>
		   
		   <% 
                Map empProdMap = (HashMap)uiEntry.getEmplProd();
                Iterator itr = empProdMap.keySet().iterator();
                String productName=null;
                String status=null;
                
                int gapCount=0;
               for(int i=0;i<productDesc.length;i++){
                    if (null != empProdMap.get(productDesc[i]) && !(empProdMap.get(productDesc[i]).toString().equalsIgnoreCase("null")))
                    {
                       status=empProdMap.get(productDesc[i]).toString();
                        if (status.equalsIgnoreCase("G")) 
                        {
                            gapCount++;
                            grandTotal++;
                            productTotals[i]++;
                        }
                        %>
                             <%
                                if(status.equalsIgnoreCase("C")){
                                     gapCount++;
                                    grandTotal++;
                                    productTotals[i]++; 
                                %>
                                <td bgcolor="Green"><%=(status)%></td>
                                
                                <%}
                                else if(status.equalsIgnoreCase("R")){
                                     gapCount++;
                                    grandTotal++;
                                    productTotals[i]++;
                                %>
                                <td bgcolor="Yellow"><%=(status)%></td>
                                
                                <%}
                                else if(status.equalsIgnoreCase("G")){
                                %>
                                <td bgcolor="Red"><%=(status)%></td>
                                
                                <%
                                }
                                %>
                                
                        <%
                    }else  
                    %>
                        <td></td>
                    <%
                }
                

                     
                
             %>
                <td bgcolor="#9999FF"><%=gapCount%></td>
            </tr>
    
    <% } %>
      <tr> <td><B>Grand Total </B></td>
            <td colspan="10"></td>
            <% for(int i=0;i<productDesc.length;i++){
            %>
            <td><%=productTotals[i]%></td>
            <%}%> 
            <td><%=grandTotal%></td>
            </tr>
        </table>
       
    </body>
</html>