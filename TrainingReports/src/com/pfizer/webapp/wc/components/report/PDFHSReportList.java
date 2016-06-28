package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;

public class PDFHSReportList extends WebComponent 
{ 
    
  private Employee[] employeeBean;
  private AppQueryStrings qStrings;
  private User user;
  public static final int LAYOUT_EXCEL	= 2;
  private int layout = 1;
  public String getJsp()  
    
    {
        	if ( layout == LAYOUT_EXCEL ) {
			return AppConst.JSP_LOC + "/components/report/POAReportListXls.jsp";
		}		
        return AppConst.JSP_LOC + "/components/report/PDFHSReportList.jsp";
    }

    public void setupChildren()
    {
    }
    
    PDFHSReportList(UserFilter filter, Employee[] emplBean, User user){
         System.out.println("EmplBean length here is "+emplBean.length);
         qStrings = filter.getQuseryStrings();
         this.user=user;
        setEmployeeBean(emplBean);
    }
    
    
     PDFHSReportList(UserFilter filter, Employee[] emplBean, User user, boolean excel){
         System.out.println("EmplBean length here is "+emplBean.length);
         qStrings = filter.getQuseryStrings();
         this.user=user;
        setEmployeeBean(emplBean);
        setLayout(2);
    }
    
    
    public void setEmployeeBean(Employee[] employeeBean){
        this.employeeBean=new Employee[employeeBean.length];
       
        for(int i=0;i<employeeBean.length;i++){
            this.employeeBean[i]=employeeBean[i];
        }
    }
    
    public Employee[] getEmployeeBean(){
        return this.employeeBean;
    }
    
    public AppQueryStrings getQueryStrings() {
		return qStrings;
	}
    
    public User getUser(){
        return this.user;
    }
	
    public void setLayout( int layout) {
		this.layout = layout;
	}
    
} 
