<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.ForecastReport"%>
<%-- <%@ taglib uri="netui-tags-databinding.tld" prefix="netui-data"%> --%>
<%-- <%@ taglib uri="netui-tags-html.tld" prefix="netui"%> --%>
<%-- <%@ taglib uri="netui-tags-template.tld" prefix="netui-template"%> --%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.wc.components.EditMenuWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditForecastOptionalFieldsWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportLaunchMeetingWc"%>
<%@ page import="com.pfizer.webapp.wc.components.admin.EditReportWc"%>
<%@ page import="com.tgix.Utils.Util"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%
	EditForecastOptionalFieldsWc wc = (EditForecastOptionalFieldsWc)request.getAttribute(EditForecastOptionalFieldsWc.ATTRIBUTE_NAME);
    ForecastReport track = wc.getTrack();
    //List fields = track.getFields();
//    Map currMap = (Map)fields.iterator().next();
%>


<netui:html>
    <body>

            <table class="basic_table">
            <tr>
                <td rowspan="2">&nbsp;&nbsp;</td>
                <td>&nbsp;</td>
                <td rowspan="2">&nbsp;&nbsp;</td>
            </tr>
            <tr>
            <td>

            <table class="no_space_width"  height="0%">
                <tr>
                    <td>
                        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
                    </td>
                    <td colspan="=2">
                        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" height="25">
                    </td>
                </tr>
                <tr>
                    <td>
                        <img src="<%=AppConst.IMAGE_DIR%>/spacer.gif" width="5">
                    </td>
                    <td>
                        <div class="breadcrumb">
                            <a href="/TrainingReports/reportselect" style="margin-left:0px;">Home</a> /
                            <a href="/TrainingReports/adminHome/editMenu?name=Reports&id=<%=wc.getMenu().getId()%>"> <%=wc.getMenu().getLabel()%> </a> /
                            <%=track.getTrackLabel()%>
                        </div>
                    </td>
                </tr>
            </table>
            <form method="post" class="form_basic" action="/TrainingReports/adminHome/editForecastOptionalFields?trackID=<%=track.getTrackId()%>&trackName=<%=track.getTrackLabel()%>">
                <%

                    System.out.println("get class="+track.getGender());
                    //System.out.println("get class="+ fields.iterator().getClass());
                 %>
                 <table  width="80%" align="center">
                 <tr><td><Label class="basic_label">Report Name : </Label>
                <%=track.getTrackLabel()%></td></tr>
                </table>
                <br>
                <table class="blue_table" width="80%" align="center">
                <tr><th align="left" colspan="2">Forecast Optional Fields</th></tr>
                <tr><td>Gender</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_GENDER%>" id="<%=ForecastReport.FIELD_GENDER%>"  >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getGender()?"Yes":"No")%>
                    </select>
                </td></tr>


                <tr><td>Manager Email</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_MANAGER_EMAIL%>" id="<%=ForecastReport.FIELD_MANAGER_EMAIL%>"  >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getManagerEmail()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Source</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_SOURCE%>" id="<%=ForecastReport.FIELD_SOURCE%>"  >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getSource()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Hire Date</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_HIRE_DATE%>" id="<%=ForecastReport.FIELD_HIRE_DATE%>">
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getHirDate()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Promotion Date</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_PROM_DATE%>" id="<%=ForecastReport.FIELD_PROM_DATE%>"  >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getPromDate()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Employee Id</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_EMPLOYEE_ID%>" id="<%=ForecastReport.FIELD_EMPLOYEE_ID%>"  >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getEmployeeId()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Guid</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_GUID%>" id="<%=ForecastReport.FIELD_GUID%>" >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getGuId()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Geography Description</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_GEOGRAPHY_DESC%>" id="<%=ForecastReport.FIELD_GEOGRAPHY_DESC%>" >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getGeographyDesc()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Regional Office State</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_REGIONAL_OFFICE_STATE%>" id="<%=ForecastReport.FIELD_REGIONAL_OFFICE_STATE%>" >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getRegionalOfficeState()?"Yes":"No")%>
                    </select>
                </td></tr>

                <tr><td>Products</td>
                <td>
                    <select  style="width:50px;" name="<%=ForecastReport.FIELD_PRODUCTS%>" id="<%=ForecastReport.FIELD_PRODUCTS%>" >
                    <%=HtmlBuilder.getOptionsFromLabelValue(ForecastReport.yesNoList,track.getProducts()?"Yes":"No")%>
                    </select>
                </td></tr>
                </table>
                <table align="center" class="">
                <tr><td colspan="2" align="center" width="100%"><input type="submit" name="Save" value="Save" ></td>
                </tr>
                </table>
            </form>
  </td>
</tr>

</table>
<table class="basic_table">
<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=wc.getErrorMsg()%></td></tr>
</table>

    </body>
</netui:html>