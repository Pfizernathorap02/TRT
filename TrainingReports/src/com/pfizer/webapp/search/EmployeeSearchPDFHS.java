package com.pfizer.webapp.search; 

import com.pfizer.db.EmpSearch;
import com.pfizer.db.EmpSearchGNSM;
import com.pfizer.db.EmpSearchTSHT;
import com.pfizer.db.EmpSearchPDFHS;
import com.pfizer.db.Employee;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.service.Service;
import com.pfizer.webapp.user.UserSession;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSearchPDFHS {
	
	public List getEmployeesByName( EmplSearchForm form, UserSession uSession ) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearch[] emps = eHandler.getEmployeesByName( form, uSession );
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}
    
    
    
    public List getPDFHSEmployeesByName( EmplSearchForm form, UserSession uSession, String event ) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchPDFHS[] emps = eHandler.getPDFHSEmployeesByName( form, uSession, event );
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}    
    
    
    	public List getPDFHSEmployeesById( String emplid ) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchPDFHS[] emps = eHandler.getPDFHSEmployeesById(emplid);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}        


    public List getGNSMEmployees( EmplSearchForm form, UserSession uSession) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchGNSM[] emps = eHandler.getGNSMEmployees( form, uSession);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}
    
    /* Added method for Vista Rx Spiriva enhancement
     * Author: Meenakshi
     * Date:14-Sep-2008
    */    
     public List getVRSEmployees( EmplSearchForm form, UserSession uSession) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchGNSM[] emps = eHandler.getVRSEmployees( form, uSession);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}
    
    /* End of addition */
    

    public List getTSHTEmployees( EmplSearchTSHTForm form, UserSession uSession) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		Employee[] emps = eHandler.getTSHTEmployees( form, uSession);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}    

    public List getTSHTEmployeeDetail( String emplId, UserSession uSession) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		Employee[] emps = eHandler.getTSHTEmployeeDetail( emplId, uSession);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}    



    public List getMSEPIEmployees( EmplSearchForm form, UserSession uSession) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchGNSM[] emps = eHandler.getMSEPIEmployees( form, uSession);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}    

    
} 
