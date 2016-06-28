package com.pfizer.action;
/*import com.bea.wlw.netui.pageflow.Forward;
import com.bea.wlw.netui.pageflow.PageFlowController;*/

import com.pfizer.db.SCEList;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.P2lHandler;
import com.pfizer.utils.DBUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.EditMenuWc;
import com.pfizer.webapp.wc.components.ListSCEWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.UpdateSCEWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.html.FormUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
/**
 * @jpf:controller
 * @jpf:view-properties view-properties::
 * <!-- This data is auto-generated. Hand-editing this section is not recommended. -->
 * <view-properties>
 * <pageflow-object id="pageflow:/adminSCE/AdminSCEController.jpf"/>
 * <pageflow-object id="action:begin.do">
 *   <property value="80" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:updateSCE.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:listSCE.do">
 *   <property value="60" name="x"/>
 *   <property value="40" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="action:unauthorized.do">
 *   <property value="120" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="page:/WEB-INF/jsp/templates/mainTemplate.jsp">
 *   <property value="240" name="x"/>
 *   <property value="100" name="y"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:begin.do@">
 *   <property value="116,160,160,204" name="elbowsX"/>
 *   <property value="92,92,92,92" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_1" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:updateSCE.do@">
 *   <property value="96,150,150,204" name="elbowsX"/>
 *   <property value="32,32,81,81" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:listSCE.do@">
 *   <property value="96,150,150,204" name="elbowsX"/>
 *   <property value="32,32,81,81" name="elbowsY"/>
 *   <property value="East_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="forward:path#index#/WEB-INF/jsp/templates/mainTemplate.jsp#@action:unauthorized.do@">
 *   <property value="120,83,83,204" name="elbowsX"/>
 *   <property value="56,56,81,81" name="elbowsY"/>
 *   <property value="North_1" name="fromPort"/>
 *   <property value="West_0" name="toPort"/>
 *   <property value="index" name="label"/>
 * </pageflow-object>
 * <pageflow-object id="control:db.TrDB#trDb">
 *   <property value="26" name="x"/>
 *   <property value="34" name="y"/>
 * </pageflow-object>
 * </view-properties>
 * ::
 */
public class AdminSCEControllerAction extends ActionSupport implements ServletRequestAware,  ServletResponseAware{

	/*Infosys - Weblogic to Jboss Migrations changes start here: Added the below code to define variables used*/
	/**
     * @common:control
     */
    /*private db.TrDB trDb;*/
	private static final long serialVersionUID = 1L;
	TransactionDB trDb= new TransactionDB();
	Map masterMap = new HashMap();
	 
	protected static final Log log = LogFactory.getLog( P2lControllerAction.class );

	
	private static final String TSR_ADMIN  = "TSR Admin";   
	
	private HttpServletRequest request;
	private HttpServletResponse response;	
	
	private HttpSession session;
	
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
	
	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}
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
     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	 */
	/*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward begin(){
    public String begin()
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
	{
        return listSCE();
    }

    /**
     * @jpf:action
     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */	
	/*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward updateSCE(){
    public String updateSCE(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    	if ( getResponse().isCommitted() ) {
			return null;            
		}
    	
		callSecurePage();
        UserSession uSession = buildUserSession();                   		
		User user = uSession.getUser();
            		
        if(!user.isSuperAdmin()){
            return unauthorized();
        } 
        String exCourseCode = getRequest().getParameter("exCourseCode");
        String exEventID = getRequest().getParameter("exEventID");
        
        String courseCode = getRequest().getParameter("courseCode");
        String eventID = getRequest().getParameter("eventID");
        
        String command = getRequest().getParameter("command");  
        if(command!=null&&command.equalsIgnoreCase("Update")){
            trDb.deleteSCECourse(exCourseCode);
            trDb.insertSCECourse(courseCode,eventID);
        }              
        UpdateSCEWc wc = new UpdateSCEWc(user);        
		MainTemplateWpc page = new MainTemplateWpc( user, "reportselect" );		                        
        if(courseCode!=null){
            wc.setCourseCode(courseCode);    
        }else if (exCourseCode!=null){
            wc.setCourseCode(exCourseCode);    
        }
        if(eventID!=null){
            wc.setEventID(eventID);
        }else if(exEventID!=null){
            wc.setEventID(exEventID);    
        }        
		page.setMain(wc);	         
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		        
		page.setLoginRequired(true);                
		/*Infosys - Weblogic to Jboss Migrations changes start here*/
        //return new Forward("index");
        return ("index");
        /*Infosys - Weblogic to Jboss Migrations changes end here*/
    }     
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */	
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward listSCE(){
    public String listSCE(){
    	/*Infosys - Weblogic to Jboss Migrations changes end here*/
        if ( getResponse().isCommitted() ) {
			return null;            
		}   
		
		callSecurePage();
        UserSession uSession = buildUserSession();                   		
		User user = uSession.getUser();
            		
        if(!user.isSuperAdmin()){
            return unauthorized();
        } 
        
        if((getRequest().getParameter("command") != null)&&("deleteSCE".equals(getRequest().getParameter("command")))) {
            deleteSCE(getRequest().getParameter("delID"));
        }
        if((getRequest().getParameter("addCommand")!= null)&&("Add SCE".equalsIgnoreCase(getRequest().getParameter("addCommand")))) {                
            insertSCE(getRequest().getParameter("courseCode"),getRequest().getParameter("eventID"));
        }                        
                
        ListSCEWc wc = new ListSCEWc(user);        
        //Render        
		MainTemplateWpc page = new MainTemplateWpc( user, "reportselect" );		        
        renderListSCE(wc);        
		page.setMain(wc);	         
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		        
		page.setLoginRequired(true);                
		/*Infosys - Weblogic to Jboss Migrations changes start here*/
        //return new Forward("index");
        return ("index");
        /*Infosys - Weblogic to Jboss Migrations changes end here*/

    }
    private void deleteSCE(String idSCE){
        int i = trDb.deleteSCECourse(idSCE);        
    }
    private void insertSCE(String courseCode,String eventID){
        int i = trDb.insertSCECourse(courseCode,eventID);
    }
    private void renderListSCE(ListSCEWc wc){        
        Vector list = new Vector();
        SCEList[] sceList = trDb.getListSCE();
        for(int i=0;i<sceList.length;i++){
            list.addElement(sceList[i]);                    
        }                                                                        
        wc.setSCEList(list);                
    }

        
	private void callSecurePage() {
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(),getResponse(),tpage);
	}    
    private UserSession buildUserSession() {
		UserSession uSession;
		uSession = UserSession.getUserSession(getRequest());
        TerritoryFilterForm filterForm = uSession.getUserFilterForm();
        
        
		
		// process query stings
		AppQueryStrings qStrings = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qStrings);
		FormUtil.loadObject(getRequest(),qStrings.getSortBy());
		
        // This will give you the full query string if needed
		qStrings.setFullQueryString( getRequest().getQueryString() );
		
        // Setup user filter obejct
		UserFilter uFilter = uSession.getUserFilter();
		uFilter.setEmployeeId(qStrings.getEmplid());	
		uFilter.setAdmin( uSession.getUser().isAdmin() );	
		uFilter.setClusterCode(uSession.getUser().getCluster());	
		uFilter.setFilterForm(filterForm);
		uFilter.setQueryStrings(qStrings);
        
        
        return uSession;
    }    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    //protected Forward unauthorized(){
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    public String unauthorized(){
    	if ( getResponse().isCommitted() ) {
			return null;
		}
		
		MainTemplateWpc page = new MainTemplateWpc( null,"unauthorized" );
		page.setMain( new UnauthorizedWc());	
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
		/*Infosys - Weblogic to Jboss Migrations changes start here*/
        //return new Forward("index");
        return ("index");
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
