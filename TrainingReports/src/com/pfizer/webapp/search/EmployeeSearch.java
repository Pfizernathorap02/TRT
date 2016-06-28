package com.pfizer.webapp.search; 

import com.pfizer.db.EmpSearch;
import com.pfizer.db.EmpSearchPOA;
import com.pfizer.db.Employee;
import com.pfizer.db.NAUserSearch;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.service.Service;
import com.pfizer.webapp.user.UserSession;
import java.util.ArrayList;
import java.util.List;

public class EmployeeSearch {
	
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
    
    
    
    	public List getPOAEmployeesByName( EmplSearchForm form, UserSession uSession ) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchPOA[] emps = eHandler.getPOAEmployeesByName( form, uSession );
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}    
    
    
    	public List getPOAEmployeesById( String emplid ) { 
		List retList = new ArrayList();
		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearchPOA[] emps = eHandler.getPOAEmployeesById(emplid);
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}        
    
      
    /* Add for RBU changes */
    public List getEmployeesForSimulationSearch( EmplSearchForm form, UserSession uSession ) { 
		List retList = new ArrayList();		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearch[] emps = eHandler.getEmployeesForSimulation( form, uSession );
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}  
     
    // Start: Start for TRT Phase 2
    public List getAllEmployees( EmplSearchForm form, UserSession uSession ) { 
		List retList = new ArrayList();		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		EmpSearch[] emps = eHandler.getAllEmployees( form, uSession );
		if ( emps != null ) {
			for ( int i=0; i < emps.length; i ++) {
				retList.add(emps[i]);
			}
		}
		
		return retList;
	}   
    // End for TRT  Phase 2
    /*Added for PXED Search*/
    public List getNAEmployeesSearch( NonAtlasEmployeeSearchForm form, UserSession uSession ) { 
        System.out.println("Inside getNAEmployeeSearch");
		List resList = new ArrayList();		
		EmployeeHandler eHandler = Service.getServiceFactory().getEmployeeHandler();
		NAUserSearch[] naus = eHandler.getNAEmployees( form, uSession );
		if ( naus != null ) {
			for ( int i=0; i < naus.length; i ++) {
				resList.add(naus[i]);
			}
		}
		
		return resList;
	}                                 
    
} 
