package com.pfizer.service;



import com.pfizer.hander.AttendanceHandler;

import com.pfizer.dao.TransactionDB;



import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.GNSMHandler;
import com.pfizer.hander.MSEPIHandler;
import com.pfizer.hander.PDFHSHandler;
import com.pfizer.hander.PLCHandler;
import com.pfizer.hander.POAHandler;

import com.pfizer.hander.PassFailHandler;
import com.pfizer.hander.RBUEmployeeHandler;
import com.pfizer.hander.RBUPieChartHandler;
import com.pfizer.hander.RBUSHandler;
import com.pfizer.hander.SPFHandler;

import com.pfizer.hander.SceHandler;

import com.pfizer.hander.TerritoryHandler;

import com.pfizer.hander.UserHandler;
import com.pfizer.hander.VRSHandler;

import com.pfizer.webapp.chart.PieChartBuilder;



/**

 * 

 */

public interface ServiceFactory {

	

	public UserHandler getUserHandler();

	public EmployeeHandler getEmployeeHandler();
    
	public TerritoryHandler getTerritoryHandler();

	public SceHandler getSceHandler();

	public PassFailHandler getPassFailHandler();

	public AttendanceHandler getAttendanceHandler();
    
    public POAHandler getPOAHandler();
    
    public PDFHSHandler getPDFHSHandler();
    
    public PLCHandler getPLCHandler();
    
    public SPFHandler getSPFHandler();    
    
    public GNSMHandler getGNSMHandler(); 
    
    public VRSHandler getVRSHandler();  
    
    public MSEPIHandler getMSEPIHandler();
    
    public RBUSHandler   getRBUSHander();
    
    public RBUPieChartHandler   getRBUPieChartHandler();
    
    public RBUEmployeeHandler   getRBUEmployeeHandler();
    
    
    
    
}

