<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.EmpReport"%>
<%@ page import="com.pfizer.db.Employee"%>
<%@ page import="com.pfizer.db.Product"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.AppQueryStrings"%>
<%@ page import="com.pfizer.webapp.report.ClassFilterForm"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ClassRosterWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.ReportListWc"%>
<%@ page import="com.pfizer.webapp.wc.page.ListReportWpc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.FormUtil"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Iterator"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%>
<%@ taglib uri="netui-tags-html.tld" prefix="netui"%>
<%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>

<%
    boolean downloadExcel = request.getParameter("downloadExcel") != null && request.getParameter("downloadExcel").equals("true");  
    ClassRosterWc wc = (ClassRosterWc)request.getAttribute(ListReportWpc.ATTRIBUTE_NAME);         
    SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
    ClassFilterForm form = wc.getClassFilterForm();
    HashMap productMap = wc.getProductMap();
    HashMap classDataMap = wc.getClassDataMap();        
%>

<%!

	public String getStringForClassRooms(HashMap classDataMap, String jsObjectName) {
		//Collections.sort(areas);		
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		StringBuffer sb = new StringBuffer();
		String classroomObjName;
		String dateObjName;
		String productObjName;		

		sb.append(addOptionString(jsObjectName,"All","All"));
		sb.append(".child = new tgixSelect('" + ClassFilterForm.FIELD_TRAININGDATE +"');\n") ;
		dateObjName = jsObjectName+".getOption('All').child"; 
		sb.append( addOptionString( dateObjName, "All", "All" ));
		sb.append(".child = new tgixSelect('" + ClassFilterForm.FIELD_PRODUCT +"');\n") ;
		productObjName = dateObjName + ".getOption('All').child";
		sb.append( addOptionString( productObjName, "All", "All" ) + ";\n");
		

		for ( Iterator itc = classDataMap.keySet().iterator(); itc.hasNext(); ) {
			Date dtTemp;
			String clsTemp = (String)itc.next();
			sb.append(addOptionString(jsObjectName,clsTemp,clsTemp));
			sb.append(".child = new tgixSelect('" + ClassFilterForm.FIELD_TRAININGDATE +"');\n") ;
			dateObjName = jsObjectName+".getOption('" + clsTemp + "').child"; 
			sb.append( addOptionString( dateObjName, "All", "All" ));	
			sb.append(".child = new tgixSelect('" + ClassFilterForm.FIELD_PRODUCT +"');\n") ;		
			productObjName = dateObjName + ".getOption('All').child";
			sb.append( addOptionString( productObjName, "All", "All" ) + ";\n");	
            HashMap dateHash = (HashMap)(classDataMap.get(clsTemp));            
			//Collections.sort(regions);		

			for ( Iterator it = dateHash.keySet().iterator(); it.hasNext(); ) {
				dtTemp = (Date)it.next();
                String dtTempStr = format.format(dtTemp);				
                sb.append(addOptionString(dateObjName,dtTempStr,dtTempStr));
				sb.append(".child = new tgixSelect('" + ClassFilterForm.FIELD_PRODUCT +"');\n") ;
				String prodObjTemp = dateObjName+".getOption('" + dtTempStr + "').child"; 
				sb.append( addOptionString( prodObjTemp, "All", "All" ) + ";\n");	
				List products = (List)dateHash.get(dtTemp);
				//Collections.sort(districts);	

				Product prodTemp;
				productObjName = dateObjName+".getOption('" + dtTempStr + "').child";				

				for ( Iterator itx = products.iterator(); itx.hasNext(); ) {
					prodTemp = (Product)itx.next();
					sb.append( addOptionString( productObjName, prodTemp.getProductDesc(), prodTemp.getProductCode() )  + ";\n");
				}
			}
		}
		return sb.toString();
	}

	public String addOptionString(String jsObjName,String name,String value) {
		StringBuffer sb = new StringBuffer();
		sb.append(jsObjName + ".addOption('" + name + "','" + value + "')");
		return sb.toString();
	}

%>
<script src="/TrainingReports/resources/js/tgixDynamicSelect.js"></script>
<script language="JavaScript1.2">


var dynamicSelect;
dynamicSelect = new tgixSelect('<%=ClassFilterForm.FIELD_CLASSROOM%>');
<%=getStringForClassRooms(classDataMap,"dynamicSelect")%>

function dropDownPopulate() {
	populateTgixSelect(dynamicSelect);
	<% if ( !Util.isEmpty( form.getClassRoom() ) ) { %>
		var tmpSel = document.getElementById( '<%=ClassFilterForm.FIELD_CLASSROOM%>' );
		if (tmpSel != null) {
			var opTemp;

			for( var x=0; x< tmpSel.options.length; x++ ) {
				if ( tmpSel.options[x].value == '<%=form.getClassRoom()%>' ) {
					tmpSel.options[x].selected = true;
					updateTgixSelect(tmpSel,dynamicSelect)
				} 
			}
		}
	<% } %>


	<% if ( !Util.isEmpty( form.getTrainingDate() ) ) { %>
		var tmpSel = document.getElementById( '<%=ClassFilterForm.FIELD_TRAININGDATE%>' );
		if (tmpSel != null) {
			var opTemp;
			for( var x=0; x< tmpSel.options.length; x++ ) {
				if ( tmpSel.options[x].value == '<%=form.getTrainingDate()%>' ) {
					tmpSel.options[x].selected = true;
					updateTgixSelect(tmpSel,dynamicSelect)
				} 
			}
		}
	<% } %>

	<% if ( !Util.isEmpty( form.getProduct() ) ) { %>
		var tmpSel = document.getElementById( '<%=ClassFilterForm.FIELD_PRODUCT%>' );
		if (tmpSel != null) {
			var opTemp;
			for( var x=0; x< tmpSel.options.length; x++ ) {
				if ( tmpSel.options[x].value == '<%=form.getProduct()%>' ) {
					tmpSel.options[x].selected = true;
					updateTgixSelect(tmpSel,dynamicSelect)
				} 
			}
		}
	<% } %>
}

</script>


<html>
    <head>
        <title>
            PDF - Class Roster
        </title>
    </head>
    
     

    <script src="/TrainingReports/resources/js/sorttable.js"></script>
    
    
    <script type="text/javascript" language="JavaScript">
        addEvent(window, "load", sortables_init);
        
        
        function sortables_init() {
            // Find all tables with class sortable and make them sortable
            if (!document.getElementsByTagName) return;
            tbls = document.getElementsByTagName("table");
            for (ti=0;ti<tbls.length;ti++) {
                thisTbl = tbls[ti];
                if (((' '+thisTbl.id+' ').indexOf("employee_table") != -1) && (thisTbl.id)) {
                    //initTable(thisTbl.id);
                    ts_makeSortable(thisTbl);
                }
            }
        }
    </script>
   
    
    <body>
        <div style="margin-left:10px;margin-right:10px">        
        
        
        <%if (!downloadExcel) {%>
        <h3 style="MARGIN-TOP:15PX">
            PDF - Class Roster Report
        </h3>
        <table class="basic_table" style="margin-left:10px;margin-right:10px">
        <form action="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportClassRoster" method="post" class="form_basic">
        

        <tr>
            <td><label>Class Room:</label></td>
        </tr>
		<tr>
            <td>
                <select id="<%=wc.getClassFilterForm().FIELD_CLASSROOM%>" name="<%=wc.getClassFilterForm().FIELD_CLASSROOM%>" onchange="updateTgixSelect(this,dynamicSelect)">                        
                </select>
            </td>
        </tr>


        <tr>
            <td><label>Training Date:</label></td>
        </tr>
		<tr>
            <td>
                <select id="<%=wc.getClassFilterForm().FIELD_TRAININGDATE%>" name="<%=wc.getClassFilterForm().FIELD_TRAININGDATE%>" onchange="updateTgixSelect(this,dynamicSelect)">                        
                </select>
            </td>
        </tr>
        
        
        <tr>
            <td><label>Product:</label></td>
        </tr>
		<tr>
            <td>
                <select id="<%=wc.getClassFilterForm().FIELD_PRODUCT%>" name="<%=wc.getClassFilterForm().FIELD_PRODUCT%>" onchange="updateTgixSelect(this,dynamicSelect)">                        
                </select>
            </td>
        </tr>
        
        <tr>
            <td>
                <input type="submit" value="Get Report">	
		    </td>
        </tr>
        <tr>
            <td height="20"></td>
        </tr>
        </form>
        </table>
        <div style="margin-left:10px;margin-right:10px">
            <div id="table_inst">
                <img src="<%=AppConst.APP_ROOT%>/resources/images/excel.gif" width="15px">&nbsp;<a href="<%=AppConst.APP_ROOT%>/PWRA/PDFHSReportClassRoster?downloadExcel=true&<%=wc.getClassFilterForm().FIELD_CLASSROOM%>=<%=form.getClassRoom()%>&<%=wc.getClassFilterForm().FIELD_TRAININGDATE%>=<%=form.getTrainingDate()%>&<%=wc.getClassFilterForm().FIELD_PRODUCT%>=<%=form.getProduct()%>">Download to Excel</a>
                &nbsp;&nbsp;|&nbsp;&nbsp;
                <a href="<%=AppConst.APP_ROOT%>/pdfhsreportselect">PDF Reports Home</a>
            </div>            
        </div>  
        <%}%>      
        

        
        <table cellspacing="0" id="employee_table" width="90%" class="employee_table" style="margin-left:10px;margin-right:10px">
        <%if (downloadExcel) {%>
        <tr>
            <td colspan="5"><b>PDF - Class Roster Report</b></td>
        </tr>
        <tr></tr>
        <tr>
            <td><label>Class Room:</label></td>
            <td><%=form.getClassRoom()%></td>
        </tr>
        <tr>
            <td><label>Training Date:</label></td>
            <td><%=form.getTrainingDate()%></td>
        </tr>
        <tr>
            <td><label>Product:</label></td>
            <td><%=Util.toEmptyNBSP("All".equalsIgnoreCase(form.getProduct()) ? "All" : (String)productMap.get(form.getProduct()))%></td>
        </tr>
        <tr></tr>
        <%}%>
        
        
        
        
        
        
        <tr>
            <th nowrap>SlNo.</th>
            <th nowrap>EMPLID</th>
            <th nowrap>First Name</th>
            <th nowrap>Last Name</th>
            <th nowrap>Preferred Name</th>
            <th nowrap>Post - PDF Cluster</th>
            <th nowrap>Post - PDF Team</th>
            <th nowrap>Post - PDF Role</th>
            <th nowrap>Address</th>
            <th nowrap>Area</th>
            <th nowrap>Region</th>
            <th nowrap>District</th>
            <th nowrap>Territory</th>
            <th nowrap>Reports To<br>(Empl Id)</th>
			<th nowrap>HR Status</th>		
			<th nowrap>Field Status</th>
        </tr>
        <%
            EmpReport[] arrEmpReport = wc.getEmpReport();
            EmpReport oEmpReport;
            if (arrEmpReport != null)
            for(int i=0; i<arrEmpReport.length; i++) {
                oEmpReport = arrEmpReport[i];
                
        %>
        <tr>
            <td><%=i+1%></td>
            <td><%=downloadExcel? Util.toEmptyNBSP(Util.formatStrExcel(oEmpReport.getEmplId())) : Util.toEmptyNBSP(oEmpReport.getEmplId())%></td>
            <%if (!downloadExcel) {%>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=PDF"><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></a></td>
            <td><a href="<%=AppConst.APP_ROOT%>/PWRA/employeeDetail/getEmployeeDetail?emplid=<%=oEmpReport.getEmplId()%>&m0=report&m1=PDF"><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></a></td>
            <%} else {%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFirstName())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getLastName())%></td>
            <%}%>
            <td><%=Util.toEmptyNBSP(oEmpReport.getPreferredName())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getClusterDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getTeamDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getRole())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFullAddress())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getAreaDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getRegionDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getDistrictDesc())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getTerritoryId())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getReportsToEmplid())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getEmplStatus())%></td>
            <td><%=Util.toEmptyNBSP(oEmpReport.getFieldActive())%></td>            
        <%
            }         
        %>
        </table>
        </div>
    </body>
</html>
<script type="text/javascript" language="JavaScript">

	// initializes the onload function

	document.onload = dropDownPopulate();

</script>