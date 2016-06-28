package com.pfizer.webapp.wc.components.report.global; 

import com.pfizer.db.Employee;
import com.pfizer.db.PassFail;
import com.pfizer.db.Product;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserTerritory;
import com.pfizer.webapp.wc.components.ErrorWc;
import com.pfizer.webapp.wc.components.UnauthorizedWc;
import com.pfizer.webapp.wc.components.chart.ChartDetailWc;
import com.pfizer.webapp.wc.components.shared.AreaRegionDistJsWc;
import com.pfizer.webapp.wc.components.shared.TerritorySelectWc;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;

public class EmployeeInfoWc extends WebComponent { 
    private Employee employee;
    private String employeeImage;
    private Employee employeeManager;
    private String emailSubject = "Phased Training Follow-up";
    	
	public EmployeeInfoWc(Employee employee) {
        super();
        this.employee = employee;
    }
    
    public Employee getEmployee() {
        return employee;
    }
    
    public String getEmailSubject() {
        return emailSubject;
    }
    public void setEmailSubject( String sub ) {
        this.emailSubject = sub;
    }
    public void setManager(Employee emp) {
        this.employeeManager = emp;
    }
    public Employee getManager() {
        return employeeManager;
    }
    public String getImage() {
        return employeeImage;
    }
    public void setImage( String url ) {
        this.employeeImage = url;
    }
    public String getJsp() {
        return AppConst.JSP_LOC + "/components/report/global/EmployeeInfo.jsp";
    }

    public void setupChildren() {
        
    }
} 
