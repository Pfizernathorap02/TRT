package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.PassFail;
import com.pfizer.db.Product;
import com.pfizer.processor.OverallProcessor;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReportListWc extends WebComponent { 
	public static final int LAYOUT_DEFAULT	= 1;
	public static final int LAYOUT_EXCEL	= 2;
	
	private List reportNameList = new ArrayList(); 
	private String reportTitle = new String();
	
	private OverallProcessor processor;
	private AppQueryStrings qStrings;
	private User user;
	private int layout = 1;
	
	public ReportListWc(UserFilter filter, OverallProcessor processor, User user ) {
		this.user = user;
		PassFailProcessor pfp = processor.getPassFailProcessor();
		qStrings = filter.getQuseryStrings();
		
		this.processor = processor;
		Map reports = pfp.getExams();
		Set keys = reports.keySet();
		for ( Iterator it = keys.iterator(); it.hasNext(); ) {
			reportNameList.add( it.next() );
		}
		
		if ( "sce".equals( filter.getQuseryStrings().getType() ) ) {
			reportTitle = "Sales Call Evaluation (SCE) : " + filter.getQuseryStrings().getSection();
		}				
		if ( "attend".equals( filter.getQuseryStrings().getType() ) ) {
			reportTitle = "Attendance : " + filter.getQuseryStrings().getSection();
		}				
		if ( "overall".equals( filter.getQuseryStrings().getType() ) ) {
			reportTitle = "Overall : " + filter.getQuseryStrings().getSection();
		}				
		if ( "test".equals( filter.getQuseryStrings().getType() ) ) {
			if (PassFail.CONST_TEST_FAIL.equals(filter.getQuseryStrings().getSection()) ) {
				reportTitle = filter.getQuseryStrings().getExam() + " : &lt; 80%";
			} else if (PassFail.CONST_TEST_PASS.equals(filter.getQuseryStrings().getSection()) ) {
				reportTitle = filter.getQuseryStrings().getExam() + " : &ge; 80%";
			} else {
				reportTitle = filter.getQuseryStrings().getExam() + " : " + filter.getQuseryStrings().getSection();
			}
		}				
	}
	
	public User getUser() {
		return this.user;
	}
	
	public OverallProcessor getProcessor() {
		return processor;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	
	public List getReportNames() {
		return reportNameList;
	}
	
	public AppQueryStrings getQueryStrings() {
		return qStrings;
	}
	
	public void setLayout( int layout) {
		this.layout = layout;
	}
    public String getJsp() {
		if ( layout == LAYOUT_EXCEL ) {
			return AppConst.JSP_LOC + "/components/report/ReportListXls.jsp";
		}		
		return AppConst.JSP_LOC + "/components/report/ReportList.jsp";
	}

    public void setupChildren() {
    }
} 
