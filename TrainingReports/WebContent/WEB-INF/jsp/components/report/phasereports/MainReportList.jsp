<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.User"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>

<%
	MainReportListWc wc = (MainReportListWc)request.getAttribute(MainReportListWc.ATTRIBUTE_NAME);
     
    //Added for Major Enhancement 3.6 - F1
    if(session.getAttribute("parentActivityPk") != null){
            String fromRequest = (String)session.getAttribute("parentActivityPk");
            session.setAttribute("parentActivityPk",fromRequest);
    }
     //End:Added for Major Enhancement 3.6 - F1
     User user=wc.getUser();
%>
<LINK href="/TrainingReports/resources/css/p2lreporting.css" rel="stylesheet" type="text/css">

<script type="text/javascript" language="JavaScript">
addEvent(window, "load", sortables_init);


function sortables_init() {
    // Find all tables with class sortable and make them sortable
    if (!document.getElementsByTagName) return;
    tbls = document.getElementsByTagName("table");
    for (ti=0;ti<tbls.length;ti++) {
        thisTbl = tbls[ti];
        if (((' '+thisTbl.id+' ').indexOf("tsr_table") != -1) && (thisTbl.id)) {
            //initTable(thisTbl.id);
            ts_makeSortable(thisTbl);
        }
    }
}
// Added for TRT Phase 2 - HQ Users
function tabchange(tab){
var id = tab.id;
   if(id=='FF'){
        document.getElementById("FF").className="user_tabLinks_selected";
        document.getElementById("HQ").className="user_tabLinks";
        document.getElementById("hq_div").style.display='none';
        document.getElementById("ff_div").style.display='block';
   }
   else{
        document.getElementById("HQ").className="user_tabLinks_selected";
        document.getElementById("FF").className="user_tabLinks";
        document.getElementById("hq_div").style.display='block';
        document.getElementById("ff_div").style.display='none';
    }
    return true;
}
// Ends here
</script>

<table class="blue_table_without_border">
	
	<tr>
         <td colspan="3">
                <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif">
        </td>  
    </tr>   
    <tr>
        <td align ="left" colspan="3">
            <table>
                <tr>
                    <td  width="10px">&nbsp;</td>
                    <td colspan="2">	
                        <div class="breadcrumb">
                            <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> / 
                            <a href="begin?track=<%=wc.getTrack().getTrackId()%>"><%=wc.getTrack().getTrackLabel()%></a> / 
                            <a href="listReportAllStatus?activitypk=<%=wc.getActivityId()%>"><%=wc.getPageName()%></a>
                        </div>
                    </td>
                </tr>
          <%if (wc.getErrorMsg().length() > 0)  {%> 
                <tr>
                    <td>&nbsp;</td>
                    <td colspan="2">
                        <%=wc.getErrorMsg()%>
                    </td>
                </tr>
                <%} else {%>          
                <tr>
                    <td colspan="3">&nbsp;</td>
                </tr>
              </table>  
            </td>
    </tr>
	<tr>
        <td align="left" valign="top" colspan="3">
            <table class="blue_table_without_border">
                <tr>
                    <td width="10px">&nbsp;</td>
                    <td valign="top">
                        <inc:include-wc component="<%=wc.getArea1()%>"/>
                    </td>
                    <td align ="left" valign="top"  colspan="3"  width="600px">
                        <!--added for TRT major Enhancement 3.6-F1-->
                        <% if(wc.getDrillDownArea() != null) { %>
                            <inc:include-wc component="<%=wc.getDrillDownArea()%>"/>
                         <% } %>
                    </td>
                <td>&nbsp;</td>
                </tr>
                     <!-- Add here ------>
                <tr align="left">
            <!-- Start: Modified for TRT 3.6 enhancement - F 4.1 ( addition of search criteria) -->
                    <td>&nbsp;</td>
                    <td colspan="2" align="left">
                    <div class="chartscontrol_area" align="left">
                        <table class="blue_table_without_border">
                            <tr>
                                <td colspan="2" valign="top" >
                                <inc:include-wc component="<%=wc.getArea2()%>"/>
                                </td>
                                <td> 
                                <%if(!user.isHQUser()){%>
                                   <inc:include-wc component="<%=wc.getEsearch()%>"/>
                                <%}%>
                                </td>
                            </tr> 
                        </table>
                    </div>
                    </td>
                    <td colspan="3">&nbsp;</td>
                </tr>
            </table>
		</td>
    </tr>
    <tr>
        <%
        boolean admin=user.isAdmin();
        String source=user.getSalesPositionTypeCd();
        //boolean admin=false;
        //String source="HQM";
        System.out.println("isAdmin="+admin);
        System.out.println("source="+source);%>
        <td align ="left" colspan="3">
            <table class="blue_table">
                <tr>
                    <th align="left">Training Report Detail</th>
                </tr>
                <tr>
                    <td align="left" style="background-color:#E8EEF7;">
                        <table class="basic_table">
                            <tr>
                                <td>
                                    <div class="phasedtraining_wrapper" >
                                        <div class="phasedtraining_hdr_wrapper" >
                                            <div class="phasedtraining_tabLinks">
                                            <a id="FF" onclick="tabchange(this)" class="user_tabLinks_selected" >Field Force</a>
                                            <%if(admin||(!source.equals("PFE"))){%>
                                            <a id="HQ" onclick="tabchange(this)" class="user_tabLinks" >&nbsp;&nbsp;HQ&nbsp;&nbsp;</a>
                                            <%}%>
                                            </div>
                                        </div>
                                        <div id="ff_div" style="background:white;display:block">
                                            <inc:include-wc component="<%=wc.getArea3()%>"/>
                                        </div>
                                        <div id="hq_div" style="background:white;display:none">
                                            <inc:include-wc component="<%=wc.getArea4()%>"/>
                                        </div>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
</table>
<%}%>
