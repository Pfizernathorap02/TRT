<%@ page language="java" contentType="text/html;charset=UTF-8"%>
<%@ page import="com.pfizer.db.P2lTrack"%>
<%@ page import="com.pfizer.db.P2lTrackPhase"%>
<%@ page import="com.pfizer.webapp.AppConst"%>
<%@ page import="com.pfizer.webapp.user.UserTerritory"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseCompletionWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseSearchForm"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.CourseSearchFormWc"%>
<%@ page import="com.pfizer.webapp.wc.components.report.phasereports.MainReportListWc"%>
<%@ page import="com.tgix.html.HtmlBuilder"%>
<%@ page import="com.tgix.html.LabelValueBean"%>
<%@ page import="java.awt.GraphicsEnvironment"%>
<%@ taglib uri="/WEB-INF/dynamic-include.tld" prefix="inc" %>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Hashtable"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.util.Vector"%>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>


<%
	CourseSearchFormWc wc = (CourseSearchFormWc)request.getAttribute(CourseSearchFormWc.ATTRIBUTE_NAME);
%>
<SCRIPT LANGUAGE="JavaScript" SRC="/TrainingReports/resources/js/DynamicOptionList.js"></SCRIPT>

<SCRIPT LANGUAGE="JavaScript">
var listB = new DynamicOptionList("<%=CourseSearchForm.FIELD_PHASE%>","<%=CourseSearchForm.FIELD_TRAINING%>");
var listC = new DynamicOptionList("<%=CourseSearchForm.FIELD_ACTIVITY%>","<%=CourseSearchForm.FIELD_TRAINING%>","<%=CourseSearchForm.FIELD_PHASE%>");
<%
    List tracks = wc.getForm().getTracks();
    
    for (Iterator it = tracks.iterator(); it.hasNext(); ) {
        P2lTrack track = (P2lTrack)it.next();
        List phases = track.getPhases();
%>

<%      Iterator itb = phases.iterator(); 

        P2lTrackPhase firstphase = null;
        if (itb.hasNext())
            firstphase = (P2lTrackPhase)itb.next();
%>

        listB.addOptions("<%=track.getTrackId()%>"
        ,"<%=firstphase.getPhaseNumber()%>","<%=firstphase.getRootActivityId()%>"
        <%  
        while (itb.hasNext() ){
            P2lTrackPhase phase = (P2lTrackPhase)itb.next();
%>
            ,"<%=phase.getPhaseNumber()%>","<%=phase.getRootActivityId()%>"
<%            
        }
%>
            );
        listB.setDefaultOption("<%=firstphase.getTrackId()%>","<%=firstphase.getRootActivityId()%>"); 
        
        
        <%  itb = phases.iterator();
            while (itb.hasNext() ){
                P2lTrackPhase phase = (P2lTrackPhase)itb.next();
        %>
        listC.addOptions("<%=phase.getTrackId()%>|<%=phase.getRootActivityId()%>"
            <%
                List acts = phase.getActivities();
                Iterator itc = acts.iterator();
                Map item = (Map)itc.next();
                Map itemFirst = item;
             %>
        ,"<%=itemFirst.get("activityname".toUpperCase())%>","<%=itemFirst.get("activityname".toUpperCase())%>"
             
             <%   
                while (itc.hasNext()) {
                    item = (Map)itc.next();
            %>
            ,"<%=item.get("activityname".toUpperCase())%>","<%=item.get("activityname".toUpperCase())%>"
            <%  } %>
            );
            listC.setDefaultOption("<%=phase.getTrackId()%>|<%=phase.getRootActivityId()%>","<%=itemFirst.get("activityname".toUpperCase())%>");
        <%  }%>
           
<%        
    }
%>




//listC.setDefaultOption("PHR|California","Los Angeles");


function initm() {
	var theform = document.searchForm;
    
	listB.init(theform);
	listC.init(theform);
    var tmpSel = document.getElementById( '<%=CourseSearchForm.FIELD_TRAINING%>' );
    if (tmpSel != null) {
        for( var x=0; x< tmpSel.options.length; x++ ) {
            if ( tmpSel.options[x].value == '<%=wc.getForm().getTraining()%>' ) {
                tmpSel.options[x].selected = true;
                listB.populate();
                listC.populate();
            }
        }
    }
    tmpSel = document.getElementById( '<%=CourseSearchForm.FIELD_PHASE%>' );
    if (tmpSel != null) {
        for( var x=0; x< tmpSel.options.length; x++ ) {
            if ( tmpSel.options[x].value == '<%=wc.getForm().getPhase()%>' ) {
                tmpSel.options[x].selected = true;
                listC.populate();
            }
        }
    }
    tmpSel = document.getElementById( '<%=CourseSearchForm.FIELD_ACTIVITY%>' );
    if (tmpSel != null) {
        for( var x=0; x< tmpSel.options.length; x++ ) {
            if ( tmpSel.options[x].value == '<%=wc.getForm().getActivity()%>' ) {
                tmpSel.options[x].selected = true;
            }
        }
    }
}
window.onload=initm; 
</script>





	<!-- START Search Target Course -->


	<div class="stnd_hdr" style="margin-top:20px;margin-bottom:10px;">
		Search Target Course:
	</div>

	<div class="searchbox" style="width:850px;">
		<form name="searchForm" action="" method="post">
            <input type="hidden" name="<%=CourseSearchForm.FIELD_SUMBITFLAG%>" value="true">
			<div class="searchsection">
				<div class="searchbox_Hdr">Select Training:</div>
                <select name="<%=CourseSearchForm.FIELD_TRAINING%>" id="<%=CourseSearchForm.FIELD_TRAINING%>" onChange="listB.populate();listC.populate();" >
                    <%=HtmlBuilder.getOptionsFromLabelValue(wc.getForm().getTrainingList(),wc.getForm().getTraining())%>
                </select>
			</div>
			<div class="searchsection">
				<div class="searchbox_Hdr">Select Phase:</div>
				<select name="<%=CourseSearchForm.FIELD_PHASE%>" id="" onChange="listC.populate();">
                    <SCRIPT LANGUAGE="JavaScript">listB.printOptions()</SCRIPT>
				</select>
			</div>
			<div class="searchsection">
				<div class="searchbox_Hdr">Select Activity:</div>
				<select name="<%=CourseSearchForm.FIELD_ACTIVITY%>" >
                    <SCRIPT LANGUAGE="JavaScript">listC.printOptions()</SCRIPT>
				</select>
			</div>
            
			<div class="searchsection2" style="width:175px;">
				<input name="" type="image" src="/TrainingReports/resources/images/btn_search.gif" style="border-style:none;margin-top:15px;">
			</div>

		</form>
	</div>
	<div class="clear" ><img src="/TrainingReports/resources/images/spacer.gif"></div>


