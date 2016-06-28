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

public class MainExemptReportWc extends WebComponent { 

	private Employee employee;
    private String employeeImg;
	private Employee reportsToEmployee;
    private String productCode;
	private User user;
    private String exemptionReason;
	
	public MainExemptReportWc(Employee employee, String productCode, User user) {
		this.employee = employee;
		this.productCode = productCode;
		this.user = user;
	}
	
	public Employee getEmployee() {
		return employee;
	}
	public User getUser() {
		return user;
	}
    public String getProductCode() {
		return productCode;
	}
    public void setEmployeeImg(String employeeImg){
        this.employeeImg=employeeImg;
    }
    
    public String getEmployeeImg(){
        return this.employeeImg;
    }
    
	public Employee getReportsTo() {
		return this.reportsToEmployee;				
	}
	public void setReportsTo( Employee emp ) {
		this.reportsToEmployee = emp;
	}
		
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/MainExemptReport.jsp";
	}

    public void setupChildren() {
    }
    
    public void setExemptionReason(String exemptionReason){
        this.exemptionReason=exemptionReason;
    }
    
    public String getExemptionReason(){
        return this.exemptionReason;
    }

} 
