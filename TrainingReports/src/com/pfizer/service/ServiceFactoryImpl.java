package com.pfizer.service;

import com.pfizer.db.PassFail;
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

public class ServiceFactoryImpl implements ServiceFactory {
	
	public ServiceFactoryImpl( ) {
	}
	
	public UserHandler getUserHandler() {
		return new UserHandler(  );
	}
	public PassFailHandler getPassFailHandler() {
		return new PassFailHandler( );
	}
	public AttendanceHandler getAttendanceHandler() {
		return new AttendanceHandler();
	}
	public EmployeeHandler getEmployeeHandler() {
		return new EmployeeHandler();
	}

	public TerritoryHandler getTerritoryHandler() {
		return new TerritoryHandler( );
	}
	
	public SceHandler getSceHandler() {
		return new SceHandler();
	}

    public POAHandler getPOAHandler()
    {
        return new POAHandler();
    }

    public PDFHSHandler getPDFHSHandler()
    {
        return new PDFHSHandler();
    }
    
    public PLCHandler getPLCHandler()
    {        
        return new PLCHandler();            
    }   

    public SPFHandler getSPFHandler()
    {
        return new SPFHandler();            
    }
    public GNSMHandler getGNSMHandler()
    {
        return new GNSMHandler();    
    }
    /* Added for Vista Rx Spiriva enhancement
     * Author: Meenakshi
     * Date: 14-Sep-2008
    */
    public VRSHandler getVRSHandler()
    {
        return new VRSHandler();    
    }
    /* End of addition */
    
    public MSEPIHandler getMSEPIHandler()
    {
        return new MSEPIHandler();    
    }
    
        

    public RBUSHandler getRBUSHander()
    {
        return new RBUSHandler(); 
    }
    
     public RBUPieChartHandler getRBUPieChartHandler()
    {
        return new RBUPieChartHandler(); 
    }
    
     public RBUEmployeeHandler getRBUEmployeeHandler()
    {
        return new RBUEmployeeHandler(); 
    }
}
