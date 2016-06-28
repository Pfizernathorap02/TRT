package com.pfizer.action;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.PWRA.EmployeeDetailFacadeImpl;
import com.pfizer.actionForm.RBUGetEmployeeDetailForm;
import com.pfizer.actionForm.RBUGetEmployeeDetailFormToviazLaunch;
import com.pfizer.actionForm.RBUTrainingClassesForm;
import com.pfizer.actionForm.RBUTrainingScheduleListForm;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.RBU.RBUEmployeeDetailsWc;
import com.pfizer.webapp.wc.RBU.ToviazLaunchEmployeeDetailsExecWc;
import com.pfizer.webapp.wc.RBU.ToviazLaunchEmployeeDetailsWc;
import com.pfizer.webapp.wc.global.HeaderEmployeeDetailPageWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;



	
	public class RBUEmployeeDetailControllerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{
	    /**
	     * @common:control
	     */
	    private EmployeeDetailFacadeImpl employeeDetailFacade=new EmployeeDetailFacadeImpl();
	    private HttpServletRequest request;
		private HttpServletResponse response;
		private TransactionDB trDb= new TransactionDB();
		private RBUGetEmployeeDetailForm edform=new RBUGetEmployeeDetailForm();
		private RBUGetEmployeeDetailFormToviazLaunch edformtl=new RBUGetEmployeeDetailFormToviazLaunch();
		private RBUTrainingScheduleListForm tslForm=new RBUTrainingScheduleListForm();
		private RBUTrainingClassesForm tcForm=new RBUTrainingClassesForm();
		
		
		
		
		
		
		
		
		public RBUGetEmployeeDetailForm getEdform() {
			return edform;
		}

		public void setEdform(RBUGetEmployeeDetailForm edform) {
			this.edform = edform;
		}

		public RBUGetEmployeeDetailFormToviazLaunch getEdformtl() {
			return edformtl;
		}

		public void setEdformtl(RBUGetEmployeeDetailFormToviazLaunch edformtl) {
			this.edformtl = edformtl;
		}

		public RBUTrainingScheduleListForm getTslForm() {
			return tslForm;
		}

		public void setTslForm(RBUTrainingScheduleListForm tslForm) {
			this.tslForm = tslForm;
		}

		public RBUTrainingClassesForm getTcForm() {
			return tcForm;
		}

		public void setTcForm(RBUTrainingClassesForm tcForm) {
			this.tcForm = tcForm;
		}

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
		public void setServletResponse(HttpServletResponse response) {
			// TODO Auto-generated method stub
			this.response = response;
		}

	
		

		@Override
		public void setServletRequest(HttpServletRequest request) {
			this.request = request;

	 	}


	    // Uncomment this declaration to access Global.app.
	    // 
	    //     public global.Global globalApp;
	    // 

	    // For an example of page flow exception handling see the example "catch" and "exception-handler"
	    // annotations in {project}/WEB-INF/src/global/Global.app

	    /**
	     * This method represents the point of entry into the pageflow
	     * @jpf:action
	     * @jpf:forward name="success" path="getEmployeeDetails.do"
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward begin(){
	     */
	    
	    public String begin()
	    {
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	        System.out.println("i'm in deaitl controller begin ");
	        /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

	    }

	    /** 
	     * @jpf:action
	     * @jpf:forward name="success" path="cancelTraining.jsp"
	     */    
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward cancelTraining(){
	     */
	    
	    public String cancelTraining(){
	    	try {
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	RBUTrainingScheduleListForm form=getTslForm();
	        String emplid = getRequest().getParameter("emplid");
	        String courseID = getRequest().getParameter("oldclassid");
	        String product = getRequest().getParameter("product");   
	        System.out.println("oldclassid = " + getRequest().getParameter("oldclassid"));

	        form.setProductName(product);
	        form.setEmplid(emplid);                 
	        form.setOldCourseID(courseID);
	        
	        
	        
	      /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success",form);
		 */
		//return new String("success", form);
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
 
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	    }
	    

	    /**
	     * @jpf:action
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward recoverTraining(){
	     */
	    
	    public String recoverTraining()
	    {
	    	try{
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	        
	        /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("success");
			 */
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	    }

	    /** 
	     * @jpf:action
	     * @jpf:forward name="success" path="updateTrainingDate.jsp"
	     */    
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward updateTrainingDate(){
	     */
	    
	    public String updateTrainingDate(){
	    	try{
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	RBUTrainingScheduleListForm form=getTslForm();
	        String emplid = getRequest().getParameter("emplid");
	        String courseID = getRequest().getParameter("courseID");
	        String product = getRequest().getParameter("product");        
	     
	         
	        form.setProductName(product);
	        form.setEmplid(emplid);                 
	        form.setOldCourseID(courseID);    
	        form.setTrainingScheduleList(employeeDetailFacade.getRBUTrainingScheduleList(courseID));   
	        
	        
	      /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success",form);
		 */
		//return new String("success", form);
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */

	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	    }
	    
	        /** 
	     * @jpf:action
	     * @jpf:forward name="success" path="addTraining.jsp"
	     */    
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward addTraining(){
	     */
	    
	    public String addTraining(){
	    	try{
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	RBUTrainingScheduleListForm form=getTslForm();
	        form.setProductName(getRequest().getParameter("product"));
	        form.setProductCD(getRequest().getParameter("productcd"));
	        form.setEmplid(getRequest().getParameter("emplid"));                 
	        
	        form.setTrainingScheduleList(employeeDetailFacade.getRBUTrainingScheduleListByProduct(getRequest().getParameter("productcd")));   
	        
	      /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success",form);
		 */
		//return new String("success", form);
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	    }
	    


	    /**
	     * @jpf:action
	     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward getEmployeeDetails(){
	     */
	    
	    public String getEmployeeDetails()
	    {
	    	try{
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	        System.out.println("i'm in getEmployeeDetails");
	        RBUGetEmployeeDetailForm form =getEdform();
	        
	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        callSecurePage();
	        String emplid = getRequest().getParameter("emplid");
	        String commandChangeTime = getRequest().getParameter("commandchangetime");
	        UserSession uSession = UserSession.getUserSession(getRequest());
	        User user = uSession.getUser();
	        
	        RBUSHandler handler = new RBUSHandler();
	        
	        System.out.println("inside controller - created handler  "); 
	        //Change Schedule Time
	        if(commandChangeTime!=null&&commandChangeTime.equals("updateTraining")){            
	            String oldCourseID = getRequest().getParameter("ocourseid");
	            String newCourseID = getRequest().getParameter("courseID");        
	            System.out.println(" controller oldCourseID " + getRequest().getParameter("ocourseid"));
	            System.out.println(" controller newCourseID " + getRequest().getParameter("courseID"));   
	            //updateScheduleTime(emplid,oldCourseID,newCourseID);           
	            handler.updateTraining(emplid, user.getId(), oldCourseID, newCourseID, getRequest().getParameter("reason"));

	        }
	        if(commandChangeTime!=null&&commandChangeTime.equals("addTraining")){                 
	            handler.addTraining(emplid,user.getId(),getRequest().getParameter("courseID"), getRequest().getParameter("reason"));
	        }
	        
	        if(commandChangeTime!=null&&commandChangeTime.equals("cancelTraining")){
	     //       CourseHandler handler = new CourseHandler();
	            String sCourseID = getRequest().getParameter("oldclassid");            
	            String sReason = getRequest().getParameter("reason");
	            System.out.println("commandChangeTime oldclassid = " + getRequest().getParameter("oldclassid"));
	            handler.cancelTraining(emplid, user.getId(), sCourseID, sReason );            
	   //         handler.insertCancelInDelCourseAssign(emplid, sCourseID, sReason);
	     //       handler.delCancelFromCourseAssign(emplid, sCourseID);
	        }
	        if(commandChangeTime!=null&&commandChangeTime.equals("recoverTraining")){
	      //      CourseHandler handler = new CourseHandler();
	            String sCourseID = getRequest().getParameter("courseid");            
	            String sReason = getRequest().getParameter("reason");
	            
	      //      handler.insertRecoverInCourseAssign(emplid, sCourseID, sReason);
	      //      handler.delRecoverFromDeletedCourseAssign(emplid, sCourseID);            
	        }
	        
	        //Reorder Command
	        if(getRequest().getParameter("commandreorder")!=null){
	           Enumeration enumr = getRequest().getParameterNames();
	           Vector reorders = new Vector();                    
	           while(enumr.hasMoreElements()){
	                String name = (String)enumr.nextElement();            
	                if(name.startsWith("reorder")){                    
	                    reorders.addElement(getRequest().getParameter(name));
	                }
	            }
	            if(reorders.size()!=0){
	                reOrderTrainingMaterialHistory(reorders,emplid);
	            } 
	        }     
	                     

	       
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_PSCPTEDP,"PSCPTEDP" ); 
	        ((HeaderEmployeeDetailPageWc)page.getHeader()).setHeadString(handler.getHeaderString());
	        RBUEmployeeDetailsWc main = new RBUEmployeeDetailsWc();    
	        
	        form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoRBU(emplid));   
	        
	        form.setProductAssignmentInfo(employeeDetailFacade.getProductAssignmentRBU(emplid));  
	        
	        form.setRbuStatus(handler.getRBUAllStatus(emplid));  
	        
	        form.setRbuGuestTrainers(handler.getGuestTrainingList(emplid));
	        
	                         
	       // form.setRbuStatus(employeeDetailFacade.getRBUTrainingStatus(emplid, form.employeeInfo));
	        form.setTrainingMaterialHistoryInfo(employeeDetailFacade.getTrainingMaterialHistory(emplid));                        

	        form.setTrainingSchedule(employeeDetailFacade.getRBUTrainingScheduleInfo(emplid));
	        //form.setCancelTraining(employeeDetailFacade.getRBUCancelTraining(emplid));
	            
	        main.setFormBean(form);
	        page.setMain(main); 
	        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);          
	     
	        /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("success",form);
			 */
			//return new String("success", form);
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	    }
	    
	    /**
	     * @jpf:action
	     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward getEmployeeDetailsToviazLaunch(){
	     */
	    
	    public String getEmployeeDetailsToviazLaunch()
	    {
	    	try{
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	        
	        RBUGetEmployeeDetailFormToviazLaunch form=getEdformtl();
	        
	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        callSecurePage();
	        String emplid = getRequest().getParameter("emplid");
	        //  String commandChangeTime = getRequest().getParameter("commandchangetime");
	        UserSession uSession = UserSession.getUserSession(getRequest());
	        User user = uSession.getUser();
	        
	        RBUSHandler handler = new RBUSHandler();
	        
	        String type = "";
	        String section = "";
	        
	        if(getRequest().getParameter("type") != null){
	            type = getRequest().getParameter("type");
	        }
	        if(getRequest().getParameter("section") != null){
	            section = getRequest().getParameter("section");
	        }
	        System.out.println("inside controller - created handler  "); 
	        String actionBy = user.getEmplid(); 
	      if(getRequest().getParameter("commandattendance") != null){
	        String attendance = getRequest().getParameter("attendance");
	        System.out.println("############################# attendance " + attendance);
	        if(attendance.equals("Y")){
	            // Update completion code for attendance
	            handler.updateToviazLaunchAttComplete(emplid,actionBy);
	            
	        }
	        if(attendance.equals("N")){
	            // Register for Breeze compliance
	            handler.updateRegistrationForBreezeCompliance(emplid,actionBy);
	            getRequest().setAttribute("noClicked", "noClicked");
	        }
	      }
	      
	      if(getRequest().getParameter("commandmanagercertificaion") != null){
	        String certification = getRequest().getParameter("managerCertification");
	        System.out.println("#############################  certification " + certification);
	        if(certification.equals("Y")){
	            // Update completion code for Post LAunch conference
	            handler.updateToviazLaunchManagerCertification(emplid,actionBy);
	            
	        }
	      }
	      
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_PSCPTEDP,"TLEDP" ); 
	        
	        ToviazLaunchEmployeeDetailsWc main = new ToviazLaunchEmployeeDetailsWc();    
	        form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoRBU(emplid));   
	        // Get the status of employee        
	        List toviazLaunchStatus = handler.getEmployeeToviazLaunchStatus(emplid);
	        String IS_RIGISTERED_ATTENDANCE = "isRegisteredAttendance";
	        String IS_RIGISTERED_CONFERENCE = "isRegisteredLaunch";
	        String IS_ON_LEAVE = "onLeave";
	        String IS_COMPLETED_ATTENDANCE = "completedAttendance";
	        String IS_COMPLETED_CONFERENCE = "completedConference";
	        String IS_RIGISTERED_COMPLIANCE = "isRegisteredCompliance";
	        System.out.println("########### List here  " + toviazLaunchStatus);
	        if(toviazLaunchStatus.contains(IS_ON_LEAVE)){
	            System.out.println("############ 1");
	             form.setDisableAll("Y");
	       }
	       if(toviazLaunchStatus.contains(IS_COMPLETED_ATTENDANCE)){
	            // form.setRepAttendedToviazLaunch("Y");
	            // form.setDisableAll("Y");
	            System.out.println("############ 2");
	             form.setAttendance("Y");
	             form.setRegistered("");
	              form.setDisableAll("Y");
	       }
	       if(toviazLaunchStatus.contains(IS_RIGISTERED_ATTENDANCE)){
	             form.setRegistered("Y");
	             System.out.println("############ 3");
	           //  form.setAttendance("");
	       }
	       if(toviazLaunchStatus.contains(IS_COMPLETED_CONFERENCE)){
	            // form.setPostLaunchConference("Y");
	             form.setManagerCertification("Y");
	             System.out.println("############ 4");
	             form.setAttendance("N");
	            form.setDisableAll("Y");
	       }
	        
	       // Get the exam status for each of the applicble codes
	       List examStatus = new ArrayList();
	       if(getRequest().getParameter("commandmanagercertificaion") != null || getRequest().getParameter("commandattendance") != null){ 
	           if(toviazLaunchStatus.contains(IS_COMPLETED_ATTENDANCE)){
	                section = "Complete";
	           }
	       }
	       String status = "";
	       if(toviazLaunchStatus.contains(IS_COMPLETED_CONFERENCE)){
	        status = "conferenceCompleted";
	       }
	       examStatus = handler.getToviazExamsStatus(emplid, type, section, status);
	       // Get the status of complaince breeze if employee selected second option
	      String complianceStatus = handler.getComplianceStatus(emplid);
	      if(complianceStatus != null && complianceStatus.equalsIgnoreCase("complianceStatus")){
	            form.setComplianceStatus("Complete");
	            form.setAttendance("N");
	      }
	      else if(toviazLaunchStatus.contains(IS_RIGISTERED_COMPLIANCE)){
	             form.setComplianceStatus("Not Complete");
	             form.setAttendance("N");
	      }
	      else if(toviazLaunchStatus.contains(IS_COMPLETED_ATTENDANCE)){
	        form.setComplianceStatus("N/A");
	      }
	      else{
	        form.setComplianceStatus("");
	      }
	       form.setExamStatus(examStatus); 
	       // Get the overall status of training
	       String overallStatus = handler.getEmployeeTrainingStatus(emplid);
	         form.setOverallStatus(overallStatus) ;
	        main.setFormBean(form);
	        page.setMain(main); 
	        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);          
	                         

	        /**
			 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
			    	return new Forward("success",form);
			 */
			//return new String("success", form);
			return new String("success");
			/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	    }
	    
	    /**
	     * @jpf:action
	     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward getEmployeeDetailsToviazLaunchExec(){
	     */
	    
	    public String getEmployeeDetailsToviazLaunchExec()
	    {
	    	try{
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	RBUGetEmployeeDetailFormToviazLaunch form=getEdformtl();
	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        callSecurePage();
	        String emplid = getRequest().getParameter("emplid");
	        UserSession uSession = UserSession.getUserSession(getRequest());
	        User user = uSession.getUser();
	        
	        RBUSHandler handler = new RBUSHandler();
	        
	        String type = "";
	        String section = "";
	        
	        if(getRequest().getParameter("type") != null){
	            type = getRequest().getParameter("type");
	        }
	        if(getRequest().getParameter("section") != null){
	            section = getRequest().getParameter("section");
	        }
	        System.out.println("inside controller - created handler  ");
	        String actionBy = user.getEmplid(); 
	      if(getRequest().getParameter("commandattendance") != null){
	        String attendance = getRequest().getParameter("attendance");
	        System.out.println("############################# attendance " + attendance);
	        if(attendance.equals("Y")){
	            // Update completion code for attendance
	            handler.updateToviazLaunchAttComplete(emplid,actionBy);
	            
	        }
	        if(attendance.equals("N")){
	            // Register for Breeze compliance
	            handler.updateRegistrationForBreezeCompliance(emplid,actionBy);
	            getRequest().setAttribute("noClicked", "noClicked");
	        }
	      }
	      
	      if(getRequest().getParameter("commandmanagercertificaion") != null){
	        String certification = getRequest().getParameter("managerCertification");
	        System.out.println("#############################  certification " + certification);
	        if(certification.equals("Y")){
	            // Update completion code for Post LAunch conference
	            handler.updateToviazLaunchManagerCertification(emplid,actionBy);
	            
	        }
	      }
	      
	        MainTemplateWpc page = new MainTemplateWpc( uSession.getUser(), AppConst.EVENT_PSCPTEDP,"TLEDP" ); 
	        
	        ToviazLaunchEmployeeDetailsExecWc main = new ToviazLaunchEmployeeDetailsExecWc();    
	        form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoRBU(emplid));   
	        // Get the status of employee        
	        List toviazLaunchStatus = handler.getEmployeeToviazLaunchStatus(emplid);
	        String IS_RIGISTERED_ATTENDANCE = "isRegisteredAttendance";
	        String IS_RIGISTERED_CONFERENCE = "isRegisteredLaunch";
	        String IS_ON_LEAVE = "onLeave";
	        String IS_COMPLETED_ATTENDANCE = "completedAttendance";
	        String IS_COMPLETED_CONFERENCE = "completedConference";
	        String IS_RIGISTERED_COMPLIANCE = "isRegisteredCompliance";
	        System.out.println("########### List here  " + toviazLaunchStatus);
	        if(toviazLaunchStatus.contains(IS_ON_LEAVE)){
	             form.setDisableAll("Y");
	       }
	       if(toviazLaunchStatus.contains(IS_COMPLETED_ATTENDANCE)){
	             form.setAttendance("Y");
	             form.setRegistered("");
	              form.setDisableAll("Y");
	       }
	       if(toviazLaunchStatus.contains(IS_RIGISTERED_ATTENDANCE)){
	             form.setRegistered("Y");
	       }
	       if(toviazLaunchStatus.contains(IS_COMPLETED_CONFERENCE)){
	            // form.setPostLaunchConference("Y");
	             form.setManagerCertification("Y");
	             form.setAttendance("N");
	            form.setDisableAll("Y");
	       }
	        
	       // Get the exam status for each of the applicble codes
	       if(getRequest().getParameter("commandmanagercertificaion") != null || getRequest().getParameter("commandattendance") != null){ 
	           if(toviazLaunchStatus.contains(IS_COMPLETED_ATTENDANCE)){
	                section = "Complete";
	           }
	       }
	       String status = "";
	       if(toviazLaunchStatus.contains(IS_COMPLETED_CONFERENCE)){
	        status = "conferenceCompleted";
	       }
	       // Get the status of complaince breeze if employee selected second option
	      String complianceStatus = handler.getComplianceStatus(emplid);
	      if(complianceStatus != null && complianceStatus.equalsIgnoreCase("complianceStatus")){
	            form.setComplianceStatus("Complete");
	            form.setAttendance("N");
	      }
	      else if(toviazLaunchStatus.contains(IS_RIGISTERED_COMPLIANCE)){
	             form.setComplianceStatus("Not Complete");
	             form.setAttendance("N");
	      }
	      else if(toviazLaunchStatus.contains(IS_COMPLETED_ATTENDANCE)){
	        form.setComplianceStatus("N/A");
	      }
	      else{
	        form.setComplianceStatus("");
	      }
	       // Get the overall status of training
	       String overallStatus = handler.getEmployeeTrainingStatus(emplid);
	         form.setOverallStatus(overallStatus) ;
	        main.setFormBean(form);
	        page.setMain(main); 
	        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);          
	                         
	      /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success",form);
		 */
		//return new String("success", form);
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	   	    }
	    
	    
	    
	       private void updateAttendance(String emplid,String result,String mode,UserSession userSession,String module){
	        String userID = null;
	        if(userSession.isAdmin()){
	            userID = userSession.getOrignalUser().getId();
	        }else{
	            userID = userSession.getUser().getId();
	        }
	        if(module.equalsIgnoreCase("GNSM")){
	            employeeDetailFacade.updateGNSMAttendance(emplid,userID,result,mode);                        
	        }else if(module.equalsIgnoreCase("MSEPI")){
	            employeeDetailFacade.updateMSEPIAttendance(emplid,userID,result,mode);                    
	        }
	        else if (module.equalsIgnoreCase("VRS")){
	            employeeDetailFacade.updateVRSAttendance(emplid,userID,result,mode);
	        }        
	    }
	    
	    private void reOrderTrainingMaterialHistory(Vector invIDs, String emplid)
	    {                
	        employeeDetailFacade.reOrderTrainingMaterialHistory(invIDs,emplid);        
	    }

	    private void updateScheduleTime(String emplid,String oldCourseID,String newCourseID)
	    {                
	        employeeDetailFacade.updateCourseList(UserSession.getUserSession(getRequest()).getUser().getId(),emplid,oldCourseID,newCourseID);        
	    }         
	    
		private void callSecurePage() {
			SuperWebPageComponents tpage = new BlankTemplateWpc();
			tpage.setLoginRequired(true);
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(),getResponse(),tpage);
		}	    
	    
	    /**
	     * @jpf:action
	     * @jpf:forward name="success" path="addProductTraining.jsp"
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward addProductClass(){
	     */
	    
	    public String addProductClass()
	    {
	    	try{
	    		/**
	    	
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	RBUTrainingClassesForm form=getTcForm();
	        if ( getResponse().isCommitted() ) {
	            return null;
	        }
	        
	        callSecurePage();
	        String emplid = getRequest().getParameter("emplid");
	        String commandChangeTime = getRequest().getParameter("commandchangetime");
	        UserSession uSession = UserSession.getUserSession(getRequest());
	        User user = uSession.getUser();
	        
	        RBUSHandler handler = new RBUSHandler();

	        form.setClasses(handler.getRBUClassesbyEmployee(emplid));    
	        form.setFutureProds(handler.getFuturelassesbyEmployee(emplid));
	        
	      /**
		 * <!-- Infosys - Weblogic to Jboss migration changes start here -->
		    	return new Forward("success",form);
		 */
		//return new String("success", form);
		return new String("success");
		/**
		 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	} catch (Exception e) {
				Global.getError(getRequest(), e);
				return new String("failure");
			}
	   	    }

	    /**
	     * FormData get and set methods may be overwritten by the Form Bean editor.
	     */
	     
	     
	     
	    
	    
	}
