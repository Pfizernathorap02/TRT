package com.pfizer.webapp.wc.components.report;

import com.pfizer.db.Employee;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.rbu.ProductDataBean;
import com.tgix.wc.WebComponent;

public class LaunchMeetingReportList extends WebComponent
{

  private Employee[] employeeBean;
  private AppQueryStrings qStrings;
  private User user;
  private ProductDataBean[] product;
  public static final int LAYOUT_EXCEL	= 2;
  private int layout = 1;
  private String trackId = "";
  public String getJsp()

    {
        	if ( layout == LAYOUT_EXCEL ) {
			return AppConst.JSP_LOC + "/components/report/LaunchMeetingReportListXls.jsp";
		}
        return AppConst.JSP_LOC + "/components/report/LaunchMeetingReportList.jsp";
    }

    public void setupChildren()
    {
    }

    LaunchMeetingReportList(UserFilter filter, Employee[] emplBean, User user, String trackId){
//         System.out.println("EmplBean length here is "+emplBean.length);
         qStrings = filter.getQuseryStrings();
         this.user=user;
         this.trackId = trackId;
        setEmployeeBean(emplBean);
    }
    LaunchMeetingReportList(UserFilter filter, Employee[] emplBean, User user, ProductDataBean[] product){
//         System.out.println("EmplBean length here is "+emplBean.length);
         qStrings = filter.getQuseryStrings();
         this.user=user;
         this.product = product;
         System.out.println("Length of product in RBUReportList >>>>>>> " + product.length);
        setEmployeeBean(emplBean);
    }


     LaunchMeetingReportList(UserFilter filter, Employee[] emplBean, User user, boolean excel){
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
    
    public String getTrackId(){
        return this.trackId;
    }

    public ProductDataBean[] getProductDataBean(){
        return this.product;
    }

    public void setLayout( int layout) {
		this.layout = layout;
	}

}
