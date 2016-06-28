package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Attendance;
import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.PedagogueExam;
import com.pfizer.db.Product;
import com.pfizer.db.SceFull;
import com.pfizer.db.TrainingOrder;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.processor.OverallResult;
import com.pfizer.processor.PassFailProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.Utils.Util;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainDetailReportWc extends WebComponent { 

	private PedagogueExam[] pExams;
	private Attendance[] courseAttendance;
	private SceFull[] sceFull;
	private Map examModules;
	private User user;
	private UserFilter userFilter;
	private Employee employee;
	private OverallProcessor processor;
	private Employee reportsToEmployee;
	private String employeeImage = "";
    private TrainingOrder trainingOrder;
	
	public MainDetailReportWc(User user, UserFilter filter, Employee employee, OverallProcessor processor) {
		this.user = user;
		this.userFilter = filter;
		this.employee = employee;
		this.processor = processor;
	}
	
	public UserFilter getUserFilter() {
		return userFilter;
	}
	
	public User getUser() {
		return user;
	}
	public OverallResult getOverall() {
		OverallResult ret = null;
		Map tmp = processor.getAllEmployeeMap();
		ret = (OverallResult)tmp.get( employee.getEmplId() );
		return ret;
	}
	public Employee getEmployee() {
		return employee;
	}
	
	public OverallProcessor getOverallProcessor() {
		return processor;
	}
	
	public void setEmployeeImage( String url ) {
		this.employeeImage = url;
	}
	
	public String getEmployeeImage() {
		return this.employeeImage;
	}
		
	public void setExamModules( Map mods ) {
		this.examModules = mods;
	}
	public Map getExamModules() {
		return this.examModules;
	}
	public void setPedagogueExam( PedagogueExam[] exams ) {
		this.pExams = exams;
	}
	public void setCourseAttendance( Attendance[] attendance ) {
		this.courseAttendance = attendance;
	}
	public Attendance[] getCourseAttendance() {
		return this.courseAttendance;
	}
	public Employee getReportsTo() {
		return this.reportsToEmployee;				
	}
	public void setReportsTo( Employee emp ) {
		this.reportsToEmployee = emp;
	}
	
	public SceFull[] getFullSce() {
		return sceFull;
	}
	public void setSecFull( SceFull[] sce ) {
		this.sceFull = sce;
	}
    
    public void setTrainingOrder(TrainingOrder trainingOrder){
        this.trainingOrder=trainingOrder;
    }
    public TrainingOrder getTrainingOrder(){
        return this.trainingOrder;
    }
	
	public List getExams() {
		List retList = new ArrayList();
		PassFailProcessor pfp = processor.getPassFailProcessor();
		 for ( int i = 0; i < pExams.length; i ++ ) {
			if ( !Util.isEmpty( pExams[i].getExamScore() ) ) {
				if ( Util.parseLong(pExams[i].getExamScore()) >= 80) {
					pExams[i].setCoaching( "Not Required" );
				} else {
					pExams[i].setCoaching( "Recommended" );
				}
				retList.add( pExams[i] );
			} else {
				if (!"Not taken".equals(pExams[0].getExamName())) {
	                pExams[i].setCoaching("Test Required");
		            retList.add( pExams[i] );
				}
			}
		 }
		 
		 // add exam names not taken
         
		 if (pExams.length ==1 && "Not taken".equals(pExams[0].getExamName())) {
			 Map exams = pfp.getExams();
			 Set keys = exams.keySet();
			 for ( Iterator it = keys.iterator(); it.hasNext(); ) {
				String name = (String)it.next();
				
				if ( pfp.checkStatusByEmployeeId( name, PassFail.CONST_TEST_NOT_TAKEN, employee.getEmplId() ) ) {
					PedagogueExam texam = new PedagogueExam();
					texam.setExamName( name );
					texam.setCoaching( "Test Required" );
					//texam.setExamIssueDate()
					retList.add( texam );
				}
			 }
		}

		return retList;
	}
		
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/MainDetailReport.jsp";
	}

    public void setupChildren() {
    }
} 
