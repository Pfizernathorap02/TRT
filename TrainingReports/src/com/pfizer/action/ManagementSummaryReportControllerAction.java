package com.pfizer.action;

/*import com.bea.wlw.netui.pageflow.Forward;
import com.bea.wlw.netui.pageflow.PageFlowController;*/
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.dao.TransactionDB;
import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.db.createManagementSql;
import com.pfizer.hander.ManagementFilterHandler;
import com.pfizer.utils.Global;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.components.ManagementSummaryReportWc;
import com.pfizer.webapp.wc.global.EmptyPageWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;


/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/ManagementSummaryReport/ManagementSummaryReportController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:getCourses.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *   <property value="120" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/blankTemplate.jsp">
 *   <property value="140" name="x"/>
 *   <property value="120" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:begin.do@">
 *   <property value="44,1,1,109" name="elbowsX"/>
 *   <property value="92,92,56,56" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#successXls#/WEB-INF/jsp/templates/blankTemplate.jsp#@action:begin.do@">
 *   <property value="44,20,20,129" name="elbowsX"/>
 *   <property value="81,81,76,76" name="elbowsY"/>
 *   <property value="West_0" name="fromPort"/>
 *   <property value="North_0" name="toPort"/>
 *   <property value="successXls" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#success#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:getCourses.do@">
 *   <property value="24,24,120,120" name="elbowsX"/>
 *   <property value="32,4,4,56" name="elbowsY"/>
 *   <property value="West_1" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="success" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#successXls#/WEB-INF/jsp/templates/blankTemplate.jsp#@action:getCourses.do@">
 *   <property value="24,24,82,140" name="elbowsX"/>
 *   <property value="43,76,76,76" name="elbowsY"/>
 *   <property value="West_2" name="fromPort"/>
 *   <property value="North_1" name="toPort"/>
 *   <property value="successXls" name="label"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
 /*Infosys - Weblogic to Jboss Migrations changes start here: Struts supporting changes*/
public class ManagementSummaryReportControllerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	private static final long serialVersionUID = 1L;
	TransactionDB trDb= new TransactionDB();
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
		
	}
	
	//	private db.TrDB trDb;
	/*Infosys - Weblogic to Jboss Migrations changes end here*/
    // Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app
	/**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
     */
	/*Infosys - Weblogic to Jboss Migrations changes start here*/
	//protected Forward begin()
	public String begin()
	/*Infosys - Weblogic to Jboss Migrations changes end here*/
    {
        return getCourses();
    }
	
	
	/**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/templates/blankTemplate.jsp"
     */
	 /*Infosys - Weblogic to Jboss Migrations changes start here*/
	//protected Forward getCourses()
	public String getCourses()
	/*Infosys - Weblogic to Jboss Migrations changes end here*/
	{

		EmptyPageWc main = new EmptyPageWc();
        main.setRandomString("edit was clicked");
        System.out.println("edit was clicked");
     //   UserSession uSession = buildUserSession();
        try {
			UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
			//UserSession uSession=UserSession.getUserSession(getRequest());
    //   System.out.println(uSession);
			boolean admin=uSession.getUser().isAdmin();
			String emplID = uSession.getUser().getEmplid();
			ManagementFilterHandler mHandler = new ManagementFilterHandler();
			ManagementSummaryReport track = new ManagementSummaryReport();
      
			String trackId = getRequest().getParameter("track");
			System.out.println("trackId=="+trackId);
			track=mHandler.getTrack(trackId);
     
			List listOfEntry = mHandler.getSelectedManagementFilter(track.getTrackId());
			Iterator it = listOfEntry.iterator();
			
			List groupBy = new ArrayList();
			if(listOfEntry.size() > 0){
			    while(it.hasNext())
			    {
			        Map mFilter = (Map)it.next();
			      
			        track.setHireStartDate((String)mFilter.get("HIRE_START_DATE"));
			        track.setHireEndDate((String)mFilter.get("HIRE_END_DATE"));
			        track.setTrainingCompletionStartDate((String)mFilter.get("TRAINING_COMPLETION_START_DATE"));
			        track.setTrainingCompletionEnddate((String)mFilter.get("TRAINING_COMPLETION_END_DATE"));
			        track.setTrainingRegistrationStartDate((String)mFilter.get("TRAINING_REG_START_DATE"));
			        track.setTrainingRegistrationEndDate((String)mFilter.get("TRAINING_REG_END_DATE"));
			        track.setSalesOrg((String)mFilter.get("SALES_ORG"));
			        track.setBusinessUnit((String)mFilter.get("BUSINESS_UNIT"));
			        track.setRoleCode((String)mFilter.get("ROLE_CODE"));
			        track.setCourseCode((String)mFilter.get("COURSE_CODE"));
			        System.out.println((String)mFilter.get("SALES_ORG"));
			        System.out.println(((String)mFilter.get("TRAINING_REG_END_DATE")+"reg end date"));
			       
			        track.setGender((String)mFilter.get("GENDER"));
			        if(mFilter.get("FIRST_GROUP_BY")==null || mFilter.get("FIRST_GROUP_BY").equals("null"))
			            groupBy.add("");
			        else    
			            groupBy.add((String)mFilter.get("FIRST_GROUP_BY"));
			        
			        if(mFilter.get("SECOND_GROUP_BY")==null || mFilter.get("SECOND_GROUP_BY").equals("null"))
			            groupBy.add("");
			        else
			            groupBy.add((String)mFilter.get("SECOND_GROUP_BY"));
			            
			        if(mFilter.get("THIRD_GROUP_BY")==null || mFilter.get("THIRD_GROUP_BY").equals("null"))     
			            groupBy.add("");
			        else
			            groupBy.add((String)mFilter.get("THIRD_GROUP_BY"));
			        
			        if(mFilter.get("FOURTH_GROUP_BY")==null || mFilter.get("FOURTH_GROUP_BY").equals("null"))         
			            groupBy.add("");
			        else
			            groupBy.add((String)mFilter.get("FOURTH_GROUP_BY"));
			        
			        if(mFilter.get("FIFTH_GROUP_BY")==null || mFilter.get("FIFTH_GROUP_BY").equals("null"))         
			            groupBy.add("");
			        else
			        groupBy.add((String)mFilter.get("FIFTH_GROUP_BY"));
			        
			        if(mFilter.get("SIXTH_GROUP_BY")==null || mFilter.get("SIXTH_GROUP_BY").equals("null"))         
			            groupBy.add("");
			        else
			            groupBy.add((String)mFilter.get("SIXTH_GROUP_BY"));
			        
			        track.setGroupByList(groupBy);
			        
			    }
			}
			
			List filterType = mHandler.getFilterType(track.getTrackId());
			
			List activityIdList = mHandler.getActivityIdList(track.getTrackId());
			track.setActivityIdList(activityIdList);
			
			List courseCodeList = mHandler.getCourseCodeList(track.getTrackId());
			track.setcourseCodeList(courseCodeList);
			
    //   String courseCodeDesc=track.getcourseCodeList().toString();
			List reportList = new ArrayList();
    // Calling createMgtSql
    if(groupBy != null && groupBy.size() > 0){
			createManagementSql MgtSql = new createManagementSql(track,admin,emplID);
			String sql = MgtSql.createMgtSql();
     // Execute query. reportList contains resultset of the executed query 
			 reportList = mHandler.getQueryResult(sql);
    }   
      //  courseDescription contains list of all course description
			List courseDescription = new ArrayList();
			courseDescription = track.getcourseCodeList();
			
			ManagementSummaryReportWc wc = new ManagementSummaryReportWc();
			wc.setReportList(reportList);
			if ( "true".equals( getRequest().getParameter("downloadExcel") ) ) {

			    wc.setLayout(ManagementSummaryReportWc.LAYOUT_XLS);
			    BlankTemplateWpc page = new BlankTemplateWpc(wc);
			    getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
			    getResponse().addHeader("content-disposition","attachment;filename=ManagementSummaryReport.xls");
			    getResponse().setContentType("application/vnd.ms-excel");
			    getResponse().setHeader("Cache-Control","max-age=0"); 
			    getResponse().setHeader("Pragma","public");

			    /*Infosys - Weblogic to Jboss Migrations changes start here*/
			    //return new Forward("successXls");
			    return ("successXls");
			    /*Infosys - Weblogic to Jboss Migrations changes end here*/
			}
			MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "reportselect" );
      
    
			wc.setMenu(uSession.getMenuList());
			wc.setTrack(track);
			page.setMain(wc);
      
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
			/*Infosys - Weblogic to Jboss Migrations changes start here*/
			    //return new Forward("success");
			    return ("success");
		} catch (Exception e) {
			Global.getError(getRequest(),e);
			return new String("failure");
		}
	        /*Infosys - Weblogic to Jboss Migrations changes end here*/
	}
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
		
	}
	

	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
}
