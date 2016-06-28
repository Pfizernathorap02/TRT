package com.pfizer.webapp.report; 

//import com.bea.b2b.server.Service;
import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.Sce;
import com.pfizer.hander.AttendanceHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.PassFailHandler;
import com.pfizer.hander.SceHandler;
import com.pfizer.processor.AttendanceProcessor;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.processor.PassFailProcessor;
import com.pfizer.processor.SceProcessor;
import com.pfizer.service.ServiceFactory;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.tgix.Utils.Timer;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReportBuilder { 	
	protected static final Log log = LogFactory.getLog( ReportBuilder.class );
	
	private  ServiceFactory service = null;
	
	
	public ReportBuilder(ServiceFactory service) {
		this.service = service;
	}
	
	public OverallProcessor getOverallProcessor( UserFilter uFilter ) {
		Timer timer = new Timer();
		
		OverallProcessor processor = new OverallProcessor( uFilter );
		
		EmployeeHandler handler = service.getEmployeeHandler();
		
		Employee[] employees = handler.getEmployees( uFilter );
		log.info( "OverallProcessor:getEmployees:" + timer.getFromLast() );
		processor.setEmployees(employees); 
		
		Map allEmp = processor.getAllEmployeeMap();
		
		processor.setPassFailProcessor( getPassFailProcessor( uFilter, allEmp ) );
		log.info( "OverallProcessor:getPassFailProcessor:" + timer.getFromLast() );

		SceProcessor scep = null;
		if ( !"ZYRT".equals( uFilter.getProduct() ) 
			 && !"RVTO".equals( uFilter.getProduct() ) ) {
			scep = getSceProcessor( uFilter, allEmp );
			processor.setSceProcessor( scep );
			log.info( "OverallProcessor:getSceProcessor:" + timer.getFromLast() );
		}

		// There are cases where certain products do not have SCE or Attendance
		if ( !"ZYRT".equals( uFilter.getProduct() ) ) {
			processor.setAttendanceProcessor( getAttendanceProcessor( uFilter, allEmp, scep ) );
			log.info( "OverallProcessor:getAttendanceProcessor:" + timer.getFromLast() );
		}

		processor.validate();
		return processor;
	}
	
	public OverallProcessor getOverallProcessorByProduct( UserSession uSession, String productCd ) {
		UserFilter uFilter;
		uFilter = new UserFilter();
		uFilter.setProdcut( productCd );
		if ( !uSession.getUser().isAdmin() ) {
			uFilter.setClusterCode( uSession.getUser().getCluster() );
		} else {
			uFilter.setAdmin(true);
		}
		uFilter.setFilterForm( uSession.getNewTerritoryFilterForm() );
		return getOverallProcessor(uFilter);
	}
	
	private PassFailProcessor getPassFailProcessor( UserFilter uFilter, Map allemp ) {
		PassFailHandler handler = service.getPassFailHandler();
		
		PassFail[] passFail = handler.getPassFail( uFilter );					 
		PassFailProcessor processor = new PassFailProcessor( passFail, allemp );
		
		return processor;
	}

	private AttendanceProcessor getAttendanceProcessor( UserFilter uFilter, Map allemp, SceProcessor scep ) {
		AttendanceHandler handler = service.getAttendanceHandler();
		
		Attendance[] attendance = handler.getAttendance( uFilter );					 
		AttendanceProcessor processor = new AttendanceProcessor( attendance, allemp, scep );
		
		return processor;
	}

	private SceProcessor getSceProcessor( UserFilter uFilter, Map allemp ) {
		SceHandler handler = service.getSceHandler();
		
		Sce[] sce = handler.getSalesCallEvaluation( uFilter );					 
		SceProcessor processor = new SceProcessor( sce, allemp );
		
		return processor;
	}

} 
