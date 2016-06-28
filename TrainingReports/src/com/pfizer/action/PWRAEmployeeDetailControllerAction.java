package com.pfizer.action;

import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.pfizer.PWRA.EmployeeDetailFacadeImpl;
import com.pfizer.actionForm.PWRAGetEmployeeDetailForm;
import com.pfizer.actionForm.PWRATrainingScheduleListForm;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.CourseHandler;
import com.pfizer.utils.Global;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.IAMUserControl;
import com.pfizer.webapp.user.UserSession;
import com.pfizer.webapp.wc.SuperWebPageComponents;
import com.pfizer.webapp.wc.components.report.PLCEmployeeDetailWc;
import com.pfizer.webapp.wc.templates.BlankTemplateWpc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;

public class PWRAEmployeeDetailControllerAction extends ActionSupport implements ServletRequestAware,ServletResponseAware{ 
	    
	    private EmployeeDetailFacadeImpl employeeDetailFacade = new EmployeeDetailFacadeImpl();
	    private HttpServletRequest request;
		private HttpServletResponse response;
		private TransactionDB trDb= new TransactionDB();
		private PWRAGetEmployeeDetailForm employeeDetailForm=new PWRAGetEmployeeDetailForm();
		private PWRATrainingScheduleListForm trainingScheduleListForm = new PWRATrainingScheduleListForm();
		
		

		
		public PWRAGetEmployeeDetailForm getEmployeeDetailForm() {
			return employeeDetailForm;
		}

		public void setEmployeeDetailForm(PWRAGetEmployeeDetailForm employeeDetailForm) {
			this.employeeDetailForm = employeeDetailForm;
		}

		public PWRATrainingScheduleListForm getTrainingScheduleListForm() {
			return trainingScheduleListForm;
		}

		public void setTrainingScheduleListForm(
				PWRATrainingScheduleListForm trainingScheduleListForm) {
			this.trainingScheduleListForm = trainingScheduleListForm;
		}

		public EmployeeDetailFacadeImpl getEmployeeDetailFacade() {
			return employeeDetailFacade;
		}

		public void setEmployeeDetailFacade(EmployeeDetailFacadeImpl employeeDetailFacade) {
			this.employeeDetailFacade = employeeDetailFacade;
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
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward begin(){
	     */
	    
	    public String begin()
	    {/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	        return new String("success");
	    }

	    /**
	     * @jpf:action
	     * @jpf:forward name="success" path="/WEB-INF/jsp/templates/mainTemplate.jsp"
	     */
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward getEmployeeDetail(){
	     */
	    
	    public String getEmployeeDetail()
	    {/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	try{
	        callSecurePage();
	        PWRAGetEmployeeDetailForm form=getEmployeeDetailForm();
	        String emplid = getRequest().getParameter("emplid");
	        String commandChangeTime = getRequest().getParameter("commandchangetime");
	        UserSession uSession = UserSession.getUserSession(getRequest());
	        //Change Schedule Time
	        if(commandChangeTime!=null&&commandChangeTime.equals("t")){            
	            String oldCourseID = getRequest().getParameter("ocourseID");
	            String newCourseID = getRequest().getParameter("courseID");           
	            updateScheduleTime(emplid,oldCourseID,newCourseID);
	           
	        }
	        if(commandChangeTime!=null&&commandChangeTime.equals("cancelTraining")){
	            CourseHandler handler = new CourseHandler();
	            String sCourseID = getRequest().getParameter("courseid");            
	            String sReason = getRequest().getParameter("reason");
	            handler.insertCancelInDelCourseAssign(emplid, sCourseID, sReason);
	            handler.delCancelFromCourseAssign(emplid, sCourseID);
	        }
	        if(commandChangeTime!=null&&commandChangeTime.equals("recoverTraining")){
	            CourseHandler handler = new CourseHandler();
	            String sCourseID = getRequest().getParameter("courseid");            
	            String sReason = getRequest().getParameter("reason");
	            handler.insertRecoverInCourseAssign(emplid, sCourseID, sReason);
	            handler.delRecoverFromDeletedCourseAssign(emplid, sCourseID);            
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
	        //command attendance
	        if(getRequest().getParameter("commandattendance")!=null){
	            String attendance = getRequest().getParameter("attendance");
	            updateAttendance(emplid,attendance,"ATT",uSession,"GNSM");           
	        }
	        /* Added for Vista Rx Spiriva enhancement
	        */
	         if(getRequest().getParameter("vrsattendance")!=null){
	            String attendance = getRequest().getParameter("attendance");
	            updateAttendance(emplid,attendance,"ATT",uSession,"VRS");           
	        }
	        /*End of addition */
	               
	        //commandmanager certificaion
	        if(getRequest().getParameter("commandmanagercertificaion")!=null){
	            String managerCertification = getRequest().getParameter("managerCertification");
	            updateAttendance(emplid,managerCertification,"MC",uSession,"GNSM");           
	        }                         

	        //command attendance MSEPI
	        if(getRequest().getParameter("commandattendanceMSEPI")!=null){
	            String attendance = getRequest().getParameter("attendance");
	            updateAttendance(emplid,attendance,"ATT",uSession,"MSEPI");           
	        }        
	        //commandmanager certificaion MSEPI
	        if(getRequest().getParameter("commandmanagercertificaionMSEPI")!=null){
	            String managerCertification = getRequest().getParameter("managerCertification");
	            updateAttendance(emplid,managerCertification,"MC",uSession,"MSEPI");           
	        }                         

	        
	        MainTemplateWpc page = null; 
	        String m0 = getRequest().getParameter("m0")==null?"":getRequest().getParameter("m0");
	        String m1 = getRequest().getParameter("m1")==null?"":getRequest().getParameter("m1");
	        String m2 = getRequest().getParameter("m2")==null?"":getRequest().getParameter("m2");
	        
	        if(m0.equalsIgnoreCase("search")&&m1.equalsIgnoreCase("GNSM")){ 
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","GNSMSEARCH");             
	        }else if(m0.equalsIgnoreCase("search")&&m1.equalsIgnoreCase("MSEPI")){ 
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","MSEPISEARCH");
	        }
	        /* Added for Vista Rx Spiriva enhancement
	        */
	        else if(m0.equalsIgnoreCase("search")&&m1.equalsIgnoreCase("VRS")){ 
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","VRSSEARCH");
	        }
	        /* End of addition */  
	        else if(m0.equalsIgnoreCase("report")) {
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","ADMIN");
	        }else if(m0.equalsIgnoreCase("search")){
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","SEARCH");       
	        }else if(m1.equalsIgnoreCase("GNSM")) {
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","GNSM");                         
	        }
	        /* Added for Vista Rx Spiriva enhancement */
	        else if(m1.equalsIgnoreCase("VRS")) {
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","VRS");                         
	        }     
	        /* End of addition */
	        else if(m1.equalsIgnoreCase("MSEPI")) {
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","MSEPI");                                     
	        }else if(m1.equalsIgnoreCase("PDF")&&m2.equalsIgnoreCase("PLC")){
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","PLC");    
	        }else if(m1.equalsIgnoreCase("PDF")&&m2.equalsIgnoreCase("HS")){
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","PDFHS");    
	        }else if(m1.equalsIgnoreCase("SPF")&&m2.equalsIgnoreCase("PLC")){
	            page = new MainTemplateWpc(uSession.getUser(),"detailreport","SPFPLC");                
	        }
	        
	        PLCEmployeeDetailWc main = new PLCEmployeeDetailWc();        

	        if(m1.equalsIgnoreCase("GNSM")){                        
	            form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoGNSM(emplid));
	            form.setOverallStatus(employeeDetailFacade.getOverallGNSMStatus(emplid));
	            form.setPlAttendacne(employeeDetailFacade.getPLAttendanceStatus(emplid)); 
	            form.setGnsmStatusInfo(employeeDetailFacade.getGNSMStatusInfo(emplid));                                      
	            form.setAttendacne(employeeDetailFacade.getAttendance(emplid,"ATT"));
	            form.setMcAttendacne(employeeDetailFacade.getAttendance(emplid,"MC"));
	            main.setFormBean(form);
	            page.setMain( main);  
	            getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);                        
	          //return new String("success", form);
		        return new String("success");
		   	        }
	        if(m1.equalsIgnoreCase("MSEPI")){                        
	            form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoGNSM(emplid));
	            form.setOverallStatus(employeeDetailFacade.getOverallMSEPIStatus(emplid));
	            //form.setPlAttendacne(employeeDetailFacade.getMSEPIPLAttendanceStatus(emplid)); 
	            form.setAttendacne(employeeDetailFacade.getMSEPIAttendance(emplid,"ATT"));
	            form.setMcAttendacne(employeeDetailFacade.getMSEPIAttendance(emplid,"MC"));
	            main.setFormBean(form);
	            page.setMain( main);  
	            getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);                        
	          //return new String("success", form);
		        return new String("success");
		   	        }
	        
	        /* Added the following if condition for Vista Rx Spiriva enhancement
	         * Author: Meenakshi
	         * Date:14-Sep-2008 
	        */
	         if(m1.equalsIgnoreCase("VRS")){
	            System.out.println("INSIDE EMPLOYEE DETAIL CONTROLLER--VRS LOOP");                               
	            form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfoGNSM(emplid));
	            form.setOverallStatus(employeeDetailFacade.getOverallVRSStatus(emplid));
	           // form.setPlAttendacne(employeeDetailFacade.getPLAttendanceStatus(emplid)); 
	            form.setVrsStatusInfo(employeeDetailFacade.getVRSStatusInfo(emplid));                                      
	         //   form.setAttendacne(employeeDetailFacade.getAttendance(emplid,"ATT"));
	            form.setAttendacne(employeeDetailFacade.getVRSAttendance(emplid,"ATT"));
	         //   form.setMcAttendacne(employeeDetailFacade.getAttendance(emplid,"MC"));
	            System.out.println("After setting values in the form"); 
	            main.setFormBean(form);
	            page.setMain( main);  
	            getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);                        
	          //return new String("success", form);
		        return new String("success");
		   	        }
	        /* End of addition */
	        
	        form.setEmployeeInfo(employeeDetailFacade.getEmployeeInfo(emplid));    
	      //  form.setTrainingMaterialHistoryInfo(employeeDetailFacade.getTrainingMaterialHistory(emplid));                        
	        /* These are event specific */ 
	        // Home Study is only for PDF
	        if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	            form.setProductAssignmentInfo(employeeDetailFacade.getProductAssignment(emplid));
	            form.setPdfHomeStudyStatus(employeeDetailFacade.getPdfHomeStudyStatusInfo(emplid));
	            form.setOverallHomeStudyStatus(employeeDetailFacade.getOverallHomeStudyStatus(emplid));
	            form.setOverallPLCStatus(employeeDetailFacade.getOverallPLCStatus(emplid));
	            form.setPlcStatusInfo(employeeDetailFacade.getPLCStatusInfo(emplid));
	        }
	        else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {  
	            form.setProductAssignmentInfo(employeeDetailFacade.getProductAssignmentSPF(emplid));                   
	            form.setOverallPLCStatus(employeeDetailFacade.getOverallSPFStatus(emplid));
	            form.setPlcStatusInfo(employeeDetailFacade.getSPFStatusInfo(emplid));
	        }
	        /* End These are event specific */
	        form.setTrainingSchedule(employeeDetailFacade.getTrainingScheduleInfo(emplid));
	        form.setCancelTraining(employeeDetailFacade.getCancelTraining(emplid));
	            
	        main.setFormBean(form);
	        page.setMain( main); 
	        getRequest().setAttribute( MainTemplateWpc.ATTRIBUTE_NAME, page);                        
	      //return new String("success", form);
	        return new String("success");
	    	}
	    	 catch (Exception e) {
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

	    /** 
	     * @jpf:action
	     * @jpf:forward name="success" path="updateTrainingDate.jsp"
	     */    
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    private Forward updateTrainingDate(TrainingScheduleListForm form){
	     */
	    
	    public String updateTrainingDate(){
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	PWRATrainingScheduleListForm form=getTrainingScheduleListForm();
	        String emplid = getRequest().getParameter("emplid");
	        String courseID = getRequest().getParameter("courseID");
	        String product = getRequest().getParameter("product");        
	        String role = getRequest().getParameter("role");        
	        String m1 = getRequest().getParameter("m1")==null?"":getRequest().getParameter("m1");       
	        form.setProductName(product);
	        form.setEmplid(emplid);                 
	        form.setOldCourseID(courseID);
	        if(role.equalsIgnoreCase("PHR")){
	            if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleListPHR(courseID));            
	            } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleListPHR(courseID));            
	            }
	        }else{
	            // For Lyrica            
	            /*if (courseID.equals("303") || courseID.equals("304") || courseID.equals("305") || courseID.equals("317")) {
	                String team = "LYRICA - " + Util.ifNull(employeeDetailFacade.getTeam(emplid),"").toUpperCase();                
	                form.setTrainingScheduleList(employeeDetailFacade.getTrainingScheduleListLYRC(courseID, team));            
	            }
	            else {
	                if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                    form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleList(courseID));            
	                } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                    form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleList(courseID));            
	                }
	            }*/
	            if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleList(courseID));            
	            } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleList(courseID));            
	            }
	        }        
	        
	        
	      //return new String("success", form);
	        return new String("success");
	   	    }
	    
	    /** 
	     * @jpf:action
	     * @jpf:forward name="success" path="cancelTraining.jsp"
	     */    
	    /**
	     * <!-- Infosys - Weblogic to Jboss migration changes start here -->
	    Added By 
	    protected Forward recoverTraining(){
	     */
	    
	    public String recoverTraining(){
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	PWRATrainingScheduleListForm form=getTrainingScheduleListForm();
	        String emplid = getRequest().getParameter("emplid");
	        String courseID = getRequest().getParameter("courseID");
	        String product = getRequest().getParameter("product");        
	        String role = getRequest().getParameter("role");        
	        String m1 = getRequest().getParameter("m1")==null?"":getRequest().getParameter("m1");
	        form.setProductName(product);
	        form.setEmplid(emplid);                 
	        form.setOldCourseID(courseID);
	        if(role.equalsIgnoreCase("PHR")){
	            if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleListPHR(courseID));            
	            } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleListPHR(courseID));            
	            }
	        }else{
	            // For Lyrica            
	            /*if (courseID.equals("303") || courseID.equals("304") || courseID.equals("305") || courseID.equals("317")) {
	                String team = "LYRICA - " + Util.ifNull(employeeDetailFacade.getTeam(emplid),"").toUpperCase();                
	                form.setTrainingScheduleList(employeeDetailFacade.getTrainingScheduleListLYRC(courseID, team));            
	            }
	            else {
	                if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                    form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleList(courseID));            
	                } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                    form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleList(courseID));            
	                }
	            }*/
	            if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleList(courseID));            
	            } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleList(courseID));            
	            }
	        }        
	        
	        
	      //return new String("success", form);
	        return new String("success");
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
	    	/**
			 * <!-- Infosys - Weblogic to Jboss migration changes ends here --> */
	    	PWRATrainingScheduleListForm form=getTrainingScheduleListForm();
	        String emplid = getRequest().getParameter("emplid");
	        String courseID = getRequest().getParameter("courseID");
	        String product = getRequest().getParameter("product");        
	        String role = getRequest().getParameter("role");        
	        String m1 = getRequest().getParameter("m1")==null?"":getRequest().getParameter("m1");
	        form.setProductName(product);
	        form.setEmplid(emplid);                 
	        form.setOldCourseID(courseID);
	        if(role.equalsIgnoreCase("PHR")){
	            if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleListPHR(courseID));            
	            } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleListPHR(courseID));            
	            }
	        }else{
	            // For Lyrica            
	            /*if (courseID.equals("303") || courseID.equals("304") || courseID.equals("305") || courseID.equals("317")) {
	                String team = "LYRICA - " + Util.ifNull(employeeDetailFacade.getTeam(emplid),"").toUpperCase();                
	                form.setTrainingScheduleList(employeeDetailFacade.getTrainingScheduleListLYRC(courseID, team));            
	            }
	            else {
	                if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                    form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleList(courseID));            
	                } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                    form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleList(courseID));            
	                }
	            }*/            
	            if (AppConst.EVENT_PDF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getPDFTrainingScheduleList(courseID));            
	            } else if (AppConst.EVENT_SPF.equalsIgnoreCase(m1)) {
	                form.setTrainingScheduleList(employeeDetailFacade.getSPFTrainingScheduleList(courseID));            
	            }
	        }        
	        
	        
	      //return new String("success", form);
	        return new String("success");
	   	    }
	    
	    
	        
	    
		private void callSecurePage() {
			SuperWebPageComponents tpage = new BlankTemplateWpc();
			tpage.setLoginRequired(true);
			IAMUserControl upControl = new IAMUserControl();
			upControl.checkAuth(getRequest(),getResponse(),tpage);
		}	    
	    
}
