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

public class CueSceReportWc extends WebComponent
{ 
    
    private boolean isExcelDownload = false;
    private List reportList = new ArrayList();
    private String activityPk=null;
    private int layout = 0;
    private String section="";
    private String reportLabel="";
        
    public static final int LAYOUT_XLS = 1;
    
    public CueSceReportWc() {
	}
    
   
    public void setExcelDownload(boolean flag) {
        this.isExcelDownload = flag;
    }
    public boolean  getExcelDownload() {
        return this.isExcelDownload;
    }
    
    public List getReportList() {
        return reportList;
    }
    public void setReportList(ArrayList list) {
        this.reportList = list;
    }
    
     public String getActivityPk() {
        return activityPk;
    }
    
    public void setActivityPk(String activityPk) {
        this.activityPk = activityPk;
    }
    
     public void setLayout(int layout) {
        this.layout=layout;
    }
    
    public String getJsp() { 
      
        if ( layout == LAYOUT_XLS ) {
            return AppConst.JSP_LOC + "/components/report/CueSceReportXls.jsp";
        }
       
         return AppConst.JSP_LOC + "/components/report/CueSceReport.jsp";
	}
    
    public void setupChildren() {                
    }
    
    public String getSection() {
        return section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    
     public String getReportLabel() {
        return reportLabel;
    }
    
    public void setReportLabel(String reportLabel) {
        this.reportLabel = reportLabel;
    }
    
} 
