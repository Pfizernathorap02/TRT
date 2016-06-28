package com.pfizer.webapp.wc.page; 
import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PedagogueExam;
import com.pfizer.db.SceFull;
import com.pfizer.db.TrainingOrder;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.wc.components.report.MainReportListWc;
 
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.report.MainDetailReportWc;
import com.pfizer.webapp.wc.components.report.MainReportListWc;
import com.pfizer.webapp.wc.templates.MainTemplateWpc;
import com.tgix.wc.WebPageComponent;
import java.util.Map;

public class DetailReportWpc extends MainTemplateWpc { 
	
	public DetailReportWpc(User user, UserFilter filter, Employee employee,OverallProcessor processor ) {
		super( user, "detailreport" );
		
		main =  new MainDetailReportWc( user, filter, employee, processor );
	}
	
	public void setPedagogueExam( PedagogueExam[] exams ) {
		((MainDetailReportWc)main).setPedagogueExam( exams );
	}
	public void setCourseAttendance( Attendance[] course ) {
		((MainDetailReportWc)main).setCourseAttendance( course );
	}
	public void setSceFull( SceFull[] sces ) {
		((MainDetailReportWc)main).setSecFull( sces );
	}
	public void setReportsTo( Employee emp ) {
		((MainDetailReportWc)main).setReportsTo( emp );
	}
	public void setExamModules( Map modules ) {
		((MainDetailReportWc)main).setExamModules( modules );
	}
	public void setEmployeePhoto( String url ) {
		((MainDetailReportWc)main).setEmployeeImage( url );
	}
    
    public void setTrainingOrder(TrainingOrder thisTrainingOrder){
        ((MainDetailReportWc)main).setTrainingOrder( thisTrainingOrder);
    }
    
} 
