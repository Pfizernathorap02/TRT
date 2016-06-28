package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.ClassRosterBean;
import com.pfizer.db.EmpReport;
import com.pfizer.db.Product;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.tgix.Utils.Util;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class P4SceReportWc extends WebComponent
{ 
    private List allDatesList;
    private List allClassnames;
    private List dateClassMap;
    private boolean isExcelDownload = false;
    private List reportList = new ArrayList();
    private String selectedDate = "all";
    private String selectedExam = "all";
    
    public P4SceReportWc() {
	}
    
    public String getSelectedExam() {
        return selectedExam;
    }
    public void setExcelDownload(boolean flag) {
        this.isExcelDownload = flag;
    }
    public boolean  getExcelDownload() {
        return this.isExcelDownload;
    }
    public void setSelectedExam(String exam) {
        this.selectedExam = exam;
    }
    public String getSelectedDate() {
        return selectedDate;
    }
    public void setSelectedDate(String date) {
        this.selectedDate = date;
    }
    public List getReportList() {
        return reportList;
    }
    public void setReportList(List list) {
        this.reportList = list;
    }
    public List getAllDates() {
        return allDatesList;
    }
    public void setAllDates(List list) {
        this.allDatesList = list;
    }
    public List getAllClassnames() {
        return this.allClassnames;
    }
    public void setAllClassnames(List list) {
        this.allClassnames = list;
    }
    public List getAllDateClassMap() {
        return this.dateClassMap;
    }
    public void setAllDateClassMap(List list) {
        this.dateClassMap = list;
    }
    
    public String getJsp() { 
         return AppConst.JSP_LOC + "/components/report/P4SceReport.jsp";
	}
    
    
    
    
    
    public void setupChildren() {                
    }
    

} 
