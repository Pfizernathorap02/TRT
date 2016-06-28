package com.pfizer.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.RBUSearchBean;
import com.pfizer.hander.AdminReportHandler;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.search.EmplSearchForm;
import com.pfizer.webapp.search.EmployeeSearchPDFHS;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.report.RBUResultsListWc;
import com.pfizer.webapp.wc.components.search.EmployeeSearchRBUWc;
import com.pfizer.webapp.wc.components.search.SearchFormRBUWc;
import com.pfizer.webapp.wc.global.HeaderRBUSearchWc;
import com.pfizer.webapp.wc.page.ListReportWpc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.wc.WebPageComponent;

public class RbuSearchControllerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{

	
	
	protected static final Log log = LogFactory.getLog(AdminAction.class );
	
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
	public HttpSession getSession() {
		return request.getSession();
	}
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		// TODO Auto-generated method stub
		this.request=request;
	}
	
	 private void callSecurePage() {
			SuperWebPageComponents tpage = new BlankTemplateWpc();
			tpage.setLoginRequired(true);
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(),getResponse(),tpage);
		}	
	    
	    
	   
	    public String begin()
	    {
	    	try{
			// do the same thing as searchRBU
	        return searchRBU();
	    	}
	    	catch (Exception e) {
	    	Global.getError(getRequest(),e);
	    	return new String("failure");
	    	}

	    }
	    public String searchRBU()
	    {
	    	try{
	        callSecurePage();
			if ( getResponse().isCommitted() ) {
				return null;
		}
	        
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(),form);
			
			UserSession uSession = UserSession.getUserSession(getRequest()); 
			User user = uSession.getUser();	
			boolean bolRefresh=false;        
	        
	        UserFilter filter = uSession.getUserFilter();  
			EmployeeSearchPDFHS es = new EmployeeSearchPDFHS();
	        List ret = new ArrayList();
			
	        if ( !Util.isEmpty( form.getLname() ) || !Util.isEmpty( form.getFname() ) || !Util.isEmpty(form.getTerrId()) || !Util.isEmpty(form.getEmplid())){
	            
	            
	            AdminReportHandler handler = new AdminReportHandler();          
	            RBUSearchBean[] rbuResultsBean = handler.getRBUSearchResults(form);
	            RBUResultsListWc varianceReport = new RBUResultsListWc(filter, rbuResultsBean, user, AppConst.EVENT_RBU);
	            
	            String sDownloadToExcel = getRequest().getParameter("downloadExcel");        
	            if (sDownloadToExcel != null && sDownloadToExcel.equalsIgnoreCase("true")) {            
	                WebPageComponent page;             
	                page = new BlankTemplateWpc(varianceReport);
	                getResponse().setContentType("application/vnd.ms-excel;charset=ISO-8859-2"); 
	                getRequest().setAttribute("exceldownload","true"); 
	                getResponse().setHeader ("Content-Disposition","attachment; filename=\"PDF - Variance Report.xls\"");             
	                getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
	                getRequest().setAttribute( ListReportWpc.ATTRIBUTE_NAME, varianceReport );
	                
	                return new String("successXls");
	            }
	            else {
	              //  MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "RBU","RBUSEARCH" );                
	              //  page.setMain(varianceReport);	            
	              //  getRequest().setAttribute( ListReportWpc.ATTRIBUTE_NAME, varianceReport );
	              //  getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		            
	              //  return new Forward("success");      
	                MainTemplateWpc page = new MainTemplateWpc( user, "RBU","RBUSEARCH");        
	                ((HeaderRBUSearchWc)page.getHeader()).setShowNav(false);
	                page.setMain( new EmployeeSearchRBUWc(form, ret ) );
	                getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
	                //page.setLoginRequired(true);     
	            } 
	            
	        } 
	        // this is to just display the form
	        MainTemplateWpc page = new MainTemplateWpc( user, "RBU","RBUSEARCH");        
	        ((HeaderRBUSearchWc)page.getHeader()).setShowNav(false);
			page.setMain( new EmployeeSearchRBUWc(form, ret ) );
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
			//page.setLoginRequired(true);
	        
	        
	        return new String("success");   
	    	}
	    	catch (Exception e) {
	    	Global.getError(getRequest(),e);
	    	return new String("failure");
	    }
	    }

	   
	    public String searchemployeerbu()
	    {
	    	
	        try{
	        callSecurePage();
			if ( getResponse().isCommitted() ) {
				return null;
			} 
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(),form);
			
			UserSession uSession = UserSession.getUserSession(getRequest()); 
			User user = uSession.getUser();	
	       
	        
	        UserFilter filter = uSession.getUserFilter();  
		
	  
			
	        if ( !Util.isEmpty( form.getLname() ) || !Util.isEmpty( form.getFname() ) || !Util.isEmpty(form.getTerrId()) || !Util.isEmpty(form.getEmplid())){
	            
	            
	            AdminReportHandler handler = new AdminReportHandler();          
	            RBUSearchBean[] rbuResultsBean = handler.getRBUSearchResults(form);
	            RBUResultsListWc searchwc = new RBUResultsListWc(filter, rbuResultsBean, user, AppConst.EVENT_RBU);
	            
	            String sDownloadToExcel = getRequest().getParameter("downloadExcel");        
	            if (sDownloadToExcel != null && sDownloadToExcel.equalsIgnoreCase("true")) {            
	                WebPageComponent page;             
	                page = new BlankTemplateWpc(searchwc);
	                getResponse().setContentType("application/vnd.ms-excel;charset=ISO-8859-2"); 
	                getRequest().setAttribute("exceldownload","true"); 
	                getResponse().setHeader ("Content-Disposition","attachment; filename=\"RBU - Search Report.xls\"");             
	                getRequest().setAttribute( BlankTemplateWpc.ATTRIBUTE_NAME, page );
	                getRequest().setAttribute( ListReportWpc.ATTRIBUTE_NAME, searchwc);
	                
	                return new String("successXls");
	            }
	            else {
	              //  MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), "RBU","RBUSEARCH" );                
	              //  page.setMain(varianceReport);	            
	              //  getRequest().setAttribute( ListReportWpc.ATTRIBUTE_NAME, varianceReport );
	              //  getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		            
	              //  return new Forward("success");      
	                MainTemplateWpc page = new MainTemplateWpc( user, "RBU","RBUSEARCH");        
	               // ((HeaderRBUSearchWc)page.getHeader()).setShowNav(false);
	               // page.setMain( new EmployeeSearchRBUWc(form, ret ) );
	                page.setMain(searchwc);
	                getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
	                //page.setLoginRequired(true);     
	            } 
	            
	        } 
		
			//page.setLoginRequired(true);
	        
	        
	        return new String("success");    
	        }
	    	catch (Exception e) {
	    	Global.getError(getRequest(),e);
	    	return new String("failure");
	    	}
	    }

	   
	    public String searchemployeerbuform()
	    { 
	        try{
	    	callSecurePage();
			if ( getResponse().isCommitted() ) {
				return null;
			} 
			EmplSearchForm form = new EmplSearchForm();
			FormUtil.loadObject(getRequest(),form);
			
			UserSession uSession = UserSession.getUserSession(getRequest()); 
			User user = uSession.getUser();	
			boolean bolRefresh=false;        
	        
	        UserFilter filter = uSession.getUserFilter();  


	        // this is to just display the form
	        MainTemplateWpc page = new MainTemplateWpc( user, "RBU","RBUSEARCH");        
	        ((HeaderRBUSearchWc)page.getHeader()).setShowNav(false);

			page.setMain( new SearchFormRBUWc(form) );
			getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
			//page.setLoginRequired(true);
	        
	        
	        return new String("success");  
	        }
	    	catch (Exception e) {
	    	Global.getError(getRequest(),e);
	    	return new String("failure");
	    	}
	    }

		@Override
		public void setServletResponse(HttpServletResponse response) {
			// TODO Auto-generated method stub
			this.response=response;
		}
	}



