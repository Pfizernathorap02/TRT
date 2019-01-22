package com.pfizer.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.db.AccessRequest;
import com.pfizer.db.ReportTypeBean;
import com.pfizer.db.RoleBean;
import com.pfizer.db.SalesOrgBean;
import com.pfizer.db.UserAccess;
import com.pfizer.db.UserGroups;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.AccessApproverHandler;
import com.pfizer.hander.AccessRequestHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.HomePageConfigurationHandler;
import com.pfizer.hander.UserHandler;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.search.EmployeeSearch;
import com.pfizer.webapp.search.NonAtlasEmployeeSearchForm;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.EmployeeGridConfigAdminWc;
import com.pfizer.webapp.wc.components.HomePageConfigWC;
import com.pfizer.webapp.wc.components.search.NASearchFormWc;
import com.pfizer.webapp.wc.components.search.searchResultWc;
import com.pfizer.webapp.wc.components.user.EditGroupWc;
import com.pfizer.webapp.wc.components.user.EditUserWc;
import com.pfizer.webapp.wc.components.user.GroupListWc;
import com.pfizer.webapp.wc.components.user.PendingApprovalsListWc;
import com.pfizer.webapp.wc.components.user.PhaseEvaluationWc;
import com.pfizer.webapp.wc.components.user.SceFormAccessWc;
import com.pfizer.webapp.wc.components.user.UserListWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.Utils.LoggerHelper;
import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;
import com.tgix.html.FormUtil;
import com.tgix.html.LabelValueBean;
import com.tgix.printing.EmployeeGridOptFieldsBean;

@SuppressWarnings("serial")
public class AdminAction extends ActionSupport implements ServletRequestAware, ServletResponseAware{

	
	private TransactionDB trDb= new TransactionDB();
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
	
	public HttpServletRequest getServletRequest() {
		return this.request;
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	
	   /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward begin(){
     */
    public String begin(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		
    	try{
    	return user();
    	}
    	catch (Exception e) {
    		Global.getError(getRequest(),e);
    		return new String("failure");
    		}

    }
	
    /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward user(){
     */
    public String user(){
    	try{
		AppQueryStrings qString = new AppQueryStrings();
        qString.setMessage("");
		FormUtil.loadObject(getRequest(),qString); 
		String criteria = qString.getUserStatus();

		if (Util.isEmpty(criteria)) {
			criteria = UserAccess.STATUS_ACTIVE;
		} 
		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		List result = uh.getUsersByStatus(criteria);
		
		UserListWc main = new UserListWc(result,criteria);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
    
    
    //code added by manish to get pending approvers
    
    public String getPendingApprovalsList()
    {
    	try{
		AppQueryStrings qString = new AppQueryStrings();
        qString.setMessage("");
		FormUtil.loadObject(getRequest(),qString); 
		
		String criteria =  AccessRequest.SUBMITTED;
		
		System.out.println("**********************");
		
			System.out.println("criteria is:"+criteria);
		
		System.out.println("**********************");
		
		
			
		
		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		
		AccessRequestHandler requestHandler = new AccessRequestHandler();
		
		List result =requestHandler.getRequests(criteria);
		
		PendingApprovalsListWc main = new PendingApprovalsListWc(result,criteria);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "Pending Approvals");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
    
    
    //code end
    
    
		
    /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward edituser(){
     */
    public String edituser(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		try{
    	AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString);
		
		
		boolean isAccessRequest= (request.getParameter("isRequestAccess") != null && request.getParameter("isRequestAccess").toString().equalsIgnoreCase("true"));
		
		if(isAccessRequest)
		{
			IAMUserControl userControl = new  IAMUserControl();
			userControl.loginFull(request, response);
		}
		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		
		
		UserAccess ua = null;
		if (Util.isEmpty(qString.getUserId())) 
		{
			ua = new UserAccess();
			
			if(isAccessRequest)
			{
				ua.setEmail(request.getParameter("email"));
				ua.setFname(request.getParameter("firstName"));
				ua.setLname(request.getParameter("lastName"));
				ua.setNtId(request.getParameter("ntid"));
				ua.setEmplid(request.getParameter("emplId"));
				ua.setNtDomain(request.getParameter("ntdomain"));
				ua.setRequestedAccess(true);
				ua.setAccessRequestId(request.getParameter("userid"));
				
				AccessRequestHandler requestHandler = new AccessRequestHandler();
				
				AccessRequest accessRequest = requestHandler.getRequest(Integer.parseInt(ua.getAccessRequestId()));
				
				if(!accessRequest.getRequestStatus().equals(AccessRequest.SUBMITTED))
				{
					request.setAttribute("Result","accessRequestNotSubmitted");
					
					return new String("actionTaken");	
				}
				
			}	
		} 
		else 
		{
			ua = uh.getUserAccessById(qString.getUserId());
		}
		EditUserWc main = new EditUserWc(ua);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward saveuser(){
     */
    public String saveuser(){
    	System.out.println("**************Inside save user method**************");
    	
    	
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		try{
    	UserAccess ua = new UserAccess();		
		FormUtil.loadObject(getRequest(),ua);
		
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		
		if ( Util.isEmpty(ua.getUserId()) ) 
		{
			uh.insertUserAccess(ua);
		} 
		else 
		{
			uh.updateUserAccess(ua);
		}
		//log.info("UserAccess:" + )
		
		
		 final  String TRTTST = "trt-tst.pfizer.com/";
		 final  String TRTSTG = "trt-stg.pfizer.com/";
		final String TRTPROD = "trt.pfizer.com/";
		 final  String TRTLOCL = "localhost:8080/";
		 
		 String requestURL= getRequest().getRequestURL().toString();
		 
		 String mailURL= "";
		 
		 if(requestURL != null && requestURL.toString().contains(TRTPROD))
			 mailURL=TRTPROD;
		 else if(requestURL != null && requestURL.toString().contains(TRTSTG))
			 mailURL=TRTSTG;
		 else if(requestURL != null && requestURL.toString().contains(TRTTST))
			 mailURL=TRTTST;
		 else if(requestURL != null && requestURL.toString().contains(TRTLOCL))
			 mailURL=TRTLOCL;
		
		System.out.println("ua.getRequested status"+ua.getRequestedAccess());
		
		if(ua.getRequestedAccess() != null && ua.getRequestedAccess())
		{
			
			AccessRequestHandler accessReqHandle = new AccessRequestHandler();
			
			AccessApproverHandler approversHandler = new AccessApproverHandler();
			
			AccessRequest requestToUpdate = new AccessRequest();
			
			requestToUpdate.setId(Integer.parseInt(ua.getAccessRequestId()));
			
			requestToUpdate.setRequestStatus(AccessRequest.APPROVED);
			
			requestToUpdate.setApprovers_comments("Access approved.");
			
			accessReqHandle.updateAccessRequests(requestToUpdate);
			
			String emailCC[] = approversHandler.getAccessApproversEmails();
			
			
			String[] BCc = new String [] {"DL-SAMS-TRTSupport@pfizer.com"};
			
			
			
			String[] updatedEmailCC = (String[]) ArrayUtils.addAll(emailCC, BCc);
			   
		     String sSubject = "Access request in TRT system.";
		     
		     String emailBody =
		    		 "Dear User"
		     		 +"<br>"+"Your access request to TRT system has been completed per below details:-"
		    		
		     		 +"<br><br>"+"Name:"+ua.getFname()+" "+ua.getLname()+""
				     
				     +"<br>Email:"+" "+ua.getEmail()
			
				     +"<br>"+"NTID:"+" "+ua.getNtId()
			
				     +"<br>"+"NT Domain:"+" "+ua.getNtDomain()
				     
				    +"<br><br>"+ "<a href='http://"+mailURL+"' >"+"Click Here"+"</a>"+" "+ "to access" 
				     
				     +"<br><br><br><br>"+"Thanks and Regards,"
			
				     +"<br>"+"Training Reports Team.";

		  
		     try{
		     MailUtil.sendMessage("traininglogistics@pfizer.com",new String[]{ ua.getEmail()},updatedEmailCC,new String[0], sSubject, emailBody, "text/html", "java:jboss/trMailSession");
		     }
		     catch(Exception e)
		     {
		         System.out.println("Email sending error >>>>>>>>>>>>"+ e.getMessage());
		     }
		     
		     request.setAttribute("Result","accessRequestCompleted");		     
		     
		     return new String("accessRequest");	     
		}
		
		
		return user();
		
		}
		catch (Exception e) 
		{
			Global.getError(getRequest(),e);
			return new String("failure");
		}

	}
    
    /* Function added for Remove button enhancement 
     *  by Meenakshi
     *  27-Oct-2008
     */
     /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward deleteuser(){
     */
    public String deleteuser(){          
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
     try{
		AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString); 	
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
        
        String userId=null;
        userId = qString.getUserId();
        if (!Util.isEmpty(userId) )
        {  
            try
            {
                trDb.deleteUser(userId);
                qString.setMessage("User has been deleted successfully");   
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        /*Adding extra*/     
		String criteria = qString.getUserStatus();

		if (Util.isEmpty(criteria)) {
			criteria = UserAccess.STATUS_ACTIVE;
		} 	
		List result = uh.getUsersByStatus(criteria);
		
		UserListWc main = new UserListWc(result,criteria);
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
             
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
	 * This will make every action in this flow require login.
	 */
	public void beforeAction() {
        
		ServiceFactory factory = Service.getServiceFactory();
		SuperWebPageComponents tpage = new BlankTemplateWpc();
		tpage.setLoginRequired(true);
		IAMUserControl upControl = new IAMUserControl();
		upControl.checkAuth(getRequest(),getResponse(),tpage);
	}
    
     /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward group(){
     */
    public String group()
    {/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		try{
    	AppQueryStrings qString = new AppQueryStrings();
        qString.setMessage("");
        qString.setMessage2("");
		FormUtil.loadObject(getRequest(),qString); 
		String criteria = qString.getUserStatus();

		if (Util.isEmpty(criteria)) {
			criteria = UserAccess.STATUS_ACTIVE;
		} 
		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		List result = uh.getGroupsByStatus();
		
		GroupListWc main = new GroupListWc(result);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward savegroup(){
     */
    public String savegroup(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
        //added 19-Mar-09
      try{
    	UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
        AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString); 	
        //ended 19-Mar-09
        qString.setMessage("");
        qString.setMessage2("");
		UserGroups ug = new UserGroups();		
		FormUtil.loadObject(getRequest(),ug);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		//added 19-Mar-09
        String groupName=null;
        groupName = ug.getGroupName();
        if(Util.isEmpty(groupName) || groupName == " ")
        {
            qString.setMessage2("User Group cannot be blank. Please enter User Group"); 
                        
            HashMap buDataMap = null;
            buDataMap = new HashMap();
            buDataMap = uh.getUserGroupHashMap();
                
            ug.setSalesOrgsList((ArrayList)getRequest().getSession().getAttribute("SalesOrg List"));
            ug.setRolesList((ArrayList)getRequest().getSession().getAttribute("Roles List"));        
            EditGroupWc main = new EditGroupWc(ug, buDataMap);
                        
            MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
            page.setMain( main );
            getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
            /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	  
        }
        else
        {
            //ended 19-Mar-09
            if (ug.getGroupId() == 0) {
            //added 19-Mar-09
                try
                {
                    System.out.println("Check If User Group Already Present");
                    int checkCount = trDb.checkGroup(groupName);
                    if (checkCount > 0)
                    {
                        System.out.println("User Group already Present");
                        qString.setMessage2("User Group already Present. Please use another User Group");   
                        
                        HashMap buDataMap = null;
                        buDataMap = new HashMap();
                        buDataMap = uh.getUserGroupHashMap();
                          
                        ug.setSalesOrgsList((ArrayList)getRequest().getSession().getAttribute("SalesOrg List"));
                        ug.setRolesList((ArrayList)getRequest().getSession().getAttribute("Roles List"));                             
                        EditGroupWc main = new EditGroupWc(ug, buDataMap);
                        
                        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
                        page.setMain( main );
                        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
                        /**
						 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
						    	return new Forward("success");
						 */
						return new String("success");
						/**
						 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	
                    }
                    else
                    {
                        System.out.println("User Group not Present");
                        uh.insertUserGroups(ug);
                        qString.setMessage("User Group has been added successfully");   
                    }
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                //ended 19-Mar-09
                //uh.insertUserGroups(ug);
            } else {
                uh.updateUserGroups(ug);
            }
        }
		//log.info("UserAccess:" + )
		//return group();
        //added 19-Mar-09
        List result = uh.getGroupsByStatus();
		GroupListWc main = new GroupListWc(result);
		    
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
        /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success");
		 */
		return new String("success");
		//ended 19-Mar-09
      }
      catch (Exception e) {
    	  Global.getError(getRequest(),e);
    	  return new String("failure");
    	  }

		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

	}	
    
      /* Method added for RBU changes */
    /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward editgroup(){
     */
    public String editgroup(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
       try{
    	System.out.println("Inside Edit Group");
		AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString); 
		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		
		UserGroups ug = null;
         System.out.println("Inside Edit Group ID"+qString.getUserId());
		if (Util.isEmpty(qString.getUserId())) {
			ug = new UserGroups();
		} else {
             System.out.println("Inside else");
			ug = uh.getUserGroupsById(qString.getUserId());
		}
        HashMap buDataMap = null;
        buDataMap = new HashMap();
        
        ArrayList rolesList = null;
        rolesList = new ArrayList();
        ArrayList salesOrgsList = null;
        salesOrgsList = new ArrayList();
        //gets the Complete HashMap with BU, Sales Organization and User Roles
        buDataMap = uh.getUserGroupHashMap();
        //System.out.println("buDataMap"+buDataMap.size());
        //System.out.println("buDataMap"+buDataMap);
        
        //getting the complete Role List for Group Administration
        RoleBean[] roles= trDb.getAllRoleDesc();
        for(int i=0;i<roles.length;i++)
        {
            RoleBean role= roles[i];
            rolesList.add(role.getRoleDesc());
        }
        if(rolesList.size()>0)
        {
            ug.setRolesList(rolesList);
        }
        /* Setting the session */
        getRequest().getSession().setAttribute("Roles List",rolesList); 
        
        //getting the complete SalesOrg List for Group Administration
        SalesOrgBean[] salesOrgs= trDb.getAllSALESORG();
        for(int i=0;i<salesOrgs.length;i++)
        {
            SalesOrgBean salesOrg= salesOrgs[i];
            salesOrgsList.add(salesOrg.getSalesOrgDesc());
        }
        if(salesOrgsList.size()>0)
        {
            ug.setSalesOrgsList(salesOrgsList);
        }
        /* Setting the session */
        getRequest().getSession().setAttribute("SalesOrg List",salesOrgsList); 
        
		EditGroupWc main = new EditGroupWc(ug, buDataMap);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward deletegroup(){
     */
    public String deletegroup(){                    
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		try{
    	AppQueryStrings qString = new AppQueryStrings();
		FormUtil.loadObject(getRequest(),qString); 	
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
        
        String userId=null;
        userId = qString.getUserId();
        System.out.println("********** Printing User Id in delete group**********"+userId);
        if (!Util.isEmpty(userId) )
        {  
            try
            {
                trDb.deleteAccessToReports(userId);
                trDb.deleteGroup(userId);
                qString.setMessage("Group has been deleted successfully");   
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        /*Adding extra*/     
	/*	String criteria = qString.getUserStatus();

		if (Util.isEmpty(criteria)) {
			criteria = UserAccess.STATUS_ACTIVE;
		} 	
		List result = uh.getUsersByStatus(criteria);
	*/	
        List result = uh.getGroupsByStatus();
		GroupListWc main = new GroupListWc(result);
		System.out.println("Executing the extra");
    
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
             
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
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward phaseEvaluation(){
     */
    public String phaseEvaluation()
    {/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
       try{
    	AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(getRequest(),qString);	
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
        
        ReportTypeBean reportlist [] = trDb.getAllReportTypes();
        
        LabelValueBean labelValueBean ;
        List reportTypeList = new ArrayList();
        
        for(int i=0;i<reportlist.length;i++){
              labelValueBean=new LabelValueBean(reportlist[i].getReportType(),reportlist[i].getReportType());
              reportTypeList.add(labelValueBean);
        }
        
        String reportType=null;
        reportType = qString.getReportType();
        String queryCondition=null;
        
        System.out.println("Report Type in JPF phaseEvaluation():"+reportType);
        if (reportType == null)
        {
            queryCondition = " where report_type in ('Phase 4') ";
        }
        else
        {
            queryCondition = " where report_type in ( '"+reportType+ "') ";
        }
        
        List result = uh.getPhaseEvalStatusList(queryCondition);
        
		PhaseEvaluationWc main = new PhaseEvaluationWc(result, reportTypeList);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward getPhaseReport(){
     */
    public String getPhaseReport()
    {/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		try{
    	AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(getRequest(),qString);		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
    
        String saveAccessRoles=null;
        saveAccessRoles = qString.getSaveAccess();
        String submitAccessRoles=null;
        submitAccessRoles = qString.getSubmitAccess();
       
        System.out.println("SaveList from QueryString :"+saveAccessRoles);
        System.out.println("SubmitList from QueryString :"+submitAccessRoles);
       
		String reportType=null;
        reportType = qString.getReportType();
        String queryCondition=null;
        System.out.println("Report Type in JPF getPhaseReport():"+reportType);
        
        uh.updatePhaseEvaluationAccess(saveAccessRoles, submitAccessRoles, reportType);
        qString.setMessage("Changes updated successfully."); 
        
        phaseEvaluation();
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
    
    //Added on 29-06-2009
/*Added for SCE form evaluation*/
    /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward sceformaccess(){
     */
    public String sceformaccess()
    {/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
        try{
    	AppQueryStrings qString = new AppQueryStrings(); 
        //qString.setMessage("");
        qString.setMessage2("");
		FormUtil.loadObject(getRequest(),qString); 
		String criteria = qString.getUserStatus();
        UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
		List result = uh.getsceEvalStatusList();
        /*added from phase eval*/
        ReportTypeBean reportlist [] = trDb.getAllReportTypes();
        
        LabelValueBean labelValueBean ;
        List reportTypeList = new ArrayList();
        
        for(int i=0;i<reportlist.length;i++){
              labelValueBean=new LabelValueBean(reportlist[i].getReportType(),reportlist[i].getReportType());
              reportTypeList.add(labelValueBean);
        }
        System.out.println("Report Type Lis"+reportTypeList);

		if (Util.isEmpty(criteria)) {
			criteria = UserAccess.STATUS_ACTIVE;
		} 
        String reportType=null;
        reportType = qString.getReportType();
        String queryCondition=null;
        
        System.out.println("Report Type in JPF phaseEvaluation():"+reportType);
        if (reportType == null)
        {
            queryCondition = " where report_type in ('Phase 4') ";
        }
        else
        {
            queryCondition = " where report_type in ( '"+reportType+ "') ";
        }
        
        List results = uh.getPhaseEvalStatusList(queryCondition);
		/*Ends here*/
		
		SceFormAccessWc main = new SceFormAccessWc(result,reportTypeList,results);
		
		MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "User Admin");
		page.setMain( main );
		getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );
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
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward getSceEvalAccess(){
     */
    public String getSceEvalAccess()
    {/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
        try{
    	System.out.println("Inside sceeval !!!");
		AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(getRequest(),qString);		
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		ServiceFactory factory = Service.getServiceFactory();
		UserHandler uh = factory.getUserHandler();
    
        String sceSaveAccessRoles=null;
        sceSaveAccessRoles = qString.getSceSave();
        String sceSubmitAccessRoles=null;
        sceSubmitAccessRoles = qString.getSceSubmit();
       
        System.out.println("SaveList from QueryString :"+sceSaveAccessRoles);
        System.out.println("SubmitList from QueryString :"+sceSubmitAccessRoles);
       
		    
        uh.updateSceEvaluationAccess(sceSaveAccessRoles, sceSubmitAccessRoles);
        qString.setMessage("Changes updated successfully."); 
        System.out.println(qString.getMessage());
        sceformaccess();
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
    
   
    
    
    /*added for pxed search wishing me luck :)*/
    /**
     * @jpf:action
     * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp" 
     */
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward Usersearch(){
     */
    public String Usersearch(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
		if ( getResponse().isCommitted() ) {
			return null;
		}
        try{
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
		User user = null;
		if ( uSession != null) {
			user = uSession.getUser();
		}

		ServiceFactory factory = Service.getServiceFactory();
		EmployeeHandler eHandler = factory.getEmployeeHandler();
         
        NonAtlasEmployeeSearchForm eForm = new NonAtlasEmployeeSearchForm();      
     
        MainTemplateWpc page = new MainTemplateWpc( user, "simulateuser" );
        searchResultWc esearch = new searchResultWc(eForm,new ArrayList() );
        NASearchFormWc searchFormWc = new NASearchFormWc(eForm); 
        esearch.setSearchForm(searchFormWc);                 
		page.setMain(new NASearchFormWc(eForm));
        getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
		page.setLoginRequired(true);
      
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
     * @jpf:String name="index" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */	
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	protected Forward NAsearch(){
     */
    public String NAsearch(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
       
        try{
		if ( getResponse().isCommitted() ) {
			return null;
		}
        
        
        NonAtlasEmployeeSearchForm form = new NonAtlasEmployeeSearchForm();
        
        FormUtil.loadObject(getRequest(),form);		
		UserSession uSession = UserSession.getUserSession(getRequest()); 
		User user = uSession.getUser();
        
        EmployeeSearch es = new EmployeeSearch();
        
        List res = new ArrayList();
        
        if ( !Util.isEmpty( form.getLname() ) || !Util.isEmpty( form.getFname() ))
        {
			res = es.getNAEmployeesSearch( form, uSession );
        }
        
        
        MainTemplateWpc page = new MainTemplateWpc( user, "search" );
		
		page.setMain( new searchResultWc(form,res) );
		getRequest().setAttribute(MainTemplateWpc.ATTRIBUTE_NAME,page);		
		page.setLoginRequired(true);
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
 
        
        //return new String("success");getfeedBackUsers
    }
    
     // Start: Modified for TRT 3.6 enhancement - F 4.4 -(admin configuration of employee grid)         
    /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */	
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	private Forward employeeGridConfig(){
     */
    public String employeeGridConfig(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
       System.out.println("Inside emp grid");
       
        LoggerHelper.logSystemDebug("Employee grid Admin functionality..---\n");
        try{
        if ( getResponse().isCommitted() ) {
		return null;
        }
	
        AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(getRequest(),qString); 
		
        UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
        
      
        if("newInput".equals(qString.getType())) {

            String str = qString.getNewSet();
            
            try{
                trDb.firstInsert(str);
         //       System.out.println("Inside try");
                
            }
            catch(Exception e){
        //        System.out.println("in catch");
                trDb.setEmployeeGridSelecteOptFields(str);
            }
         } 

        String selectedFields = trDb.getEmployeeGridSelectedOptFields();
      //  System.out.println("selectedFields=="+selectedFields);
    
        EmployeeGridConfigAdminWc wc = new EmployeeGridConfigAdminWc();
        wc.setSelectedOptFields(selectedFields);
        System.out.println(wc.getSelectedOptFields());  // print selected optional fields
         
        ArrayList fieldslist = new ArrayList();
        EmployeeGridOptFieldsBean[] fields = trDb.getEmployeeGridAllOptFields();
        
        for(int i=0;i<fields.length;i++){
            fieldslist.add(fields[i]);
        }
        wc.setOptFields(fieldslist);
        
        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "Employee Grid Fields Config");
        page.setMain(wc);
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );

        
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
    
    
    
    
    
    
  // End: Modified for TRT 3.6 enhancement - F 4.4 -(admin configuration of employee grid)         
   // Start: Added for TRT 3.6 enhancement Phase 2 (Home Page configuration)  
     /**
     * @jpf:action
     * @jpf:String name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
     */	
    /**
     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
    	private Forward editHomePage(){
     */
    public String editHomePage(){
    	/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
        try{
    	HttpSession session = getSession();
        session.removeAttribute("sectionNamesList");
        session.removeAttribute("minimizeList");
        session.removeAttribute("trackIdList");
        session.removeAttribute("preview");
        AppQueryStrings qString = new AppQueryStrings();
        FormUtil.loadObject(getRequest(),qString); 
		UserSession uSession = (UserSession)getRequest().getSession(true).getAttribute(UserSession.ATTRIBUTE);
       
        int sort = trDb.getMaxSort()+1;
        int active=1;
      
        HomePageConfigurationHandler homeHandler = new HomePageConfigurationHandler();
        HomePageConfigWC wc = new HomePageConfigWC();

        boolean preview = false;
        // add a new section 
        if (getRequest().getParameter("addSection") != null) {
                    String[] sectionNames;
                    List sectionNamesList= new ArrayList();
                    sectionNames = getRequest().getParameterValues("sectionName");
                    for(int i=0;i<sectionNames.length;i++){
                         sectionNamesList.add(sectionNames[i].toUpperCase());
                    }
                    try{
                    String sectionName = getRequest().getParameter("newSection").trim();
                        if(!sectionNamesList.contains(sectionName.toUpperCase())){
                         //   System.out.println("inserting");
                            String delflag="N";
                            String minflag="N";
                            trDb.insertTrainingReports(sectionName,active,sort,delflag,minflag);  
                            wc.setErrorMsg("Section added successfully.");  
                       }
                        else{
                            wc.setErrorMsg("Section name must be unique.");
                        }
                     }
                    catch(Exception e){
                        System.out.println("Exception=="+e);
                        wc.setErrorMsg("Please enter a valid section name.");
                        
                    }
       } 
        
        
    //    HttpSession session = getSession();
    //    boolean preview = false;
    //    System.out.println("Preview=="+getRequest().getParameter("preview"));
        if(getRequest().getParameter("preview") != null){
            //    System.out.println("preview");
                String[] trackId;
                List trackIdList= new ArrayList();
                trackId = getRequest().getParameterValues("trackId"); 
                
                String[] sectionNames;
                List sectionNamesList= new ArrayList();
                sectionNames = getRequest().getParameterValues("sectionName");
                
                String[] minimize;
                List minimizeList = new ArrayList();
                minimize = getRequest().getParameterValues("min"); 
                
                for(int i=0;i<minimize.length;i++){
                    System.out.println("in preview=="+minimize[i]);
                    if(minimize[i]=="" || minimize[i].equals("")){
                         minimize[i]="N";
                    }
                   }
                for(int i=0;i<sectionNames.length;i++){
                 //   System.out.println(sectionNames[i]);
                    sectionNamesList.add(sectionNames[i]);
                }
                for(int j=0;j<trackId.length;j++){
                    trackIdList.add(trackId[j]);
                }
                for(int i=0;i<minimize.length;i++){
                    minimizeList.add(minimize[i]);
                }
                session.setAttribute("sectionNamesList",sectionNamesList);
                session.setAttribute("trackIdList",trackIdList);
                session.setAttribute("minimizeList",minimizeList);
            //    System.out.println("get Attribute=="+session.getAttribute("sectionNamesList"));
                preview = true;
                session.setAttribute("preview",preview+"");
             //   homeHandler.updateTrainingReportTable(sectionNames,trackId,minimize);
        } 
        
        // save a section
        if(getRequest().getParameter("save") != null){
                
                String[] trackId;
                trackId = getRequest().getParameterValues("trackId"); 
                
                String[] sectionNames;
                sectionNames = getRequest().getParameterValues("sectionName");
                
                String[] minimize;
                minimize = getRequest().getParameterValues("min"); 
                
                
                for(int i=0;i<sectionNames.length;i++){
                    System.out.println("sectionNames=="+sectionNames[i]);
                }
                
                  for(int i=0;i<minimize.length;i++){
                    if(minimize[i]=="" || minimize[i].equals("")){
                          minimize[i] = "N";
                          //minimize = trDb.getMinimize();
                         }
                   }
                boolean duplicate = false;   
                for(int i=0;i<sectionNames.length-1;i++){
                    for(int j=i+1;j<sectionNames.length;j++){
                        if(sectionNames[i].equals(sectionNames[j])){
                            duplicate = true;
                            wc.setErrorMsg("Section name must be unique.");
                        }
                    }
                }
               
             //   String 
             if(!duplicate){
                homeHandler.updateTrainingReportTable(sectionNames,trackId,minimize);
                wc.setErrorMsg("Section saved successfully.");
             }
             else{
                wc.setErrorMsg("Section name must be unique.");
             }
        }
        
        // delete a section
        if(getRequest().getParameter("del") != null){
                    String id = getRequest().getParameter("del"); 
                    // Update Training Report table
                    homeHandler.updateDeleteFlag(id);
                  
        }
        
        List resultList = homeHandler.getTrainingReportConfiguration();
        Iterator it = resultList.iterator();
       
        ArrayList sectionList = new ArrayList();
        ArrayList idList = new ArrayList();
        ArrayList checkedValuesList = new ArrayList();
        
         while(it.hasNext()){
            Map mFilter = (Map)it.next();
            String str = (String)mFilter.get("TRAINING_REPORT_LABEL");
            String id = mFilter.get("TRAINING_REPORT_ID").toString();
            String min = mFilter.get("MINIMIZE").toString();
            sectionList.add(str);
            idList.add(id);
            checkedValuesList.add(min);
        } 
        
    
      //  HomePageConfigWC wc = new HomePageConfigWC();
        wc.setSectionList(sectionList);
        wc.setIdList(idList);
        wc.setCheckedValuesList(checkedValuesList);
        
        MainTemplateWpc page = new MainTemplateWpc(uSession.getUser(), "Home Page Config");
        if(preview){
            page.setOnLoad("openPreview()");
        }
        page.setMain(wc);
        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page );

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

    }  //end of addition   
	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response=response;
	}
}
