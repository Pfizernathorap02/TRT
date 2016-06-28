package com.pfizer.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.pfizer.db.EmpSearchTSHT;
import com.pfizer.utils.Global;
import com.pfizer.webapp.search.EmplSearchTSHTForm;
import com.pfizer.webapp.search.EmployeeSearchPDFHS;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.search.EmployeeSearchTSHTDetailWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchTSHTWc;
import com.pfizer.webapp.wc.global.HeaderTSHTSEARCHWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.wc.WebPageComponent;

public class TSHTReportsControllerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{

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
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request = request;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response=response;
	}
	// Uncomment this declaration to access Global.app.
    // 
    //     protected global.Global globalApp;
    // 

    // For an example of page flow exception handling see the example "catch" and "exception-handler"
    // annotations in {project}/WEB-INF/src/global/Global.app

    /**
     * This method represents the point of entry into the pageflow
     * @jpf:action
     * @jpf:forward name="success" path="index.jsp"
     */
	  /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward begin(){
     */
       public String begin(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		
    	try{
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    	}

		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
    }
    
    
    /**
     * @jpf:action
     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */	
    
    	  /**
         * <!-- Infosys - Weblogic to Jboss migration changes start here -->
        	protected Forward searchTSHT(){
         */
         public String searchTSHT(){
        	 
        	 try{
        	/**
    		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		callSecurePage();
		if ( getResponse().isCommitted() ) {
			return null;
		} 
		EmplSearchTSHTForm form = new EmplSearchTSHTForm();
		FormUtil.loadObject(getRequest(),form);
		
		UserSession uSession = UserSession.getUserSession(getRequest()); 
		User user = uSession.getUser();	
		boolean bolRefresh=false;        
        
        //Test
		EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();
        List ret = new ArrayList();
		if ( !Util.isEmpty( form.getLname() ) || !Util.isEmpty( form.getFname() ) || !Util.isEmpty(form.getCourseName()) || !Util.isEmpty(form.getEmplid())){
            //ret = es.getGNSMEmployees( form, uSession);
            ret = es.getTSHTEmployees( form, uSession);                                                            
            
            
        }   
        // If the search fetches only one employee, directly show detail screen
        if (ret.size() == 1) {
            List ret1 = new ArrayList();  
            String emplid = "";
            for(Iterator iter=ret.iterator();iter.hasNext();){        
                EmpSearchTSHT emp = (EmpSearchTSHT)iter.next();  
                emplid= emp.getEmplId();
            }
            ret1 = es.getTSHTEmployeeDetail( emplid, uSession);                                                            
            
            MainTemplateWpc page = new MainTemplateWpc( user, "detailreport","TSHTSEARCHDETAIL");        
            ((HeaderTSHTSEARCHWc)page.getHeader()).setShowNav(false);
            page.setMain( new EmployeeSearchTSHTDetailWc(form, ret1 ) );
            getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
            page.setLoginRequired(true);
        // If the search fetches more then one employee, show the list and let the user to select and view 
        }else{
            MainTemplateWpc page = new MainTemplateWpc( user, "detailreport","TSHTSEARCH");        
            ((HeaderTSHTSEARCHWc)page.getHeader()).setShowNav(false);
            page.setMain( new EmployeeSearchTSHTWc(form, ret ) );
            getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
            page.setLoginRequired(true);
        }
        
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("index");
		 */
		return new String("index");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
    }


/**
     * @jpf:action
     * @jpf:forward name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     * @jpf:forward name="successXls" path="/WEB-INF/jsp/templates/blankTemplate.jsp" 
     */	
         /**
          * <!-- Infosys - Weblogic to Jboss migration changes start here -->
          protected Forward showTSHTDetail(){
          */
        public String showTSHTDetail(){
    	try{
    		/**
    		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		callSecurePage();
		if ( getResponse().isCommitted() ) {
			return null;
		} 
        String emplid = getRequest().getParameter("emplid");
		EmplSearchTSHTForm form = new EmplSearchTSHTForm();
		FormUtil.loadObject(getRequest(),form);
		
		UserSession uSession = UserSession.getUserSession(getRequest()); 
		User user = uSession.getUser();	
		boolean bolRefresh=false;        
        
		EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();
        
        List ret1 = new ArrayList();    
        ret1 = es.getTSHTEmployeeDetail( emplid, uSession);      
        
       String sDownloadToExcel = getRequest().getParameter("downloadExcel");        
        
        if (sDownloadToExcel != null && sDownloadToExcel.equalsIgnoreCase("true")) {            
            WebPageComponent page;             
            page = new BlankTemplateWpc(new EmployeeSearchTSHTDetailWc(form, ret1 ));
            getResponse().setContentType("application/vnd.ms-excel;charset=ISO-8859-2"); 
            getRequest().setAttribute("exceldownload","true"); 
            getResponse().setHeader ("Content-Disposition","attachment; filename=\"Historical Training Server Transcripts Report.xls\"");             
            getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
            /**
    		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    		     return new Forward("successXls");
    		 */
            return new String("successXls");
            /**
    		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
        } else{                                                             
            MainTemplateWpc page = new MainTemplateWpc( user, "detailreport","TSHTSEARCHDETAIL");        
            ((HeaderTSHTSEARCHWc)page.getHeader()).setShowNav(false);
            page.setMain( new EmployeeSearchTSHTDetailWc(form, ret1 ) );
            getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
            page.setLoginRequired(true);        
        }
        
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("index");
		 */
		return new String("index");
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
    }

	private void callSecurePage() {
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(),getResponse(),tpage);
	}	
}

