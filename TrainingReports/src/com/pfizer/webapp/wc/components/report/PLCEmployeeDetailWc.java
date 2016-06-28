package com.pfizer.webapp.wc.components.report; 




import com.pfizer.actionForm.PWRAGetEmployeeDetailForm;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class PLCEmployeeDetailWc extends WebComponent{ 
	PWRAGetEmployeeDetailForm getEmployeeDetailForm;
    private String sSource = "";

    public PLCEmployeeDetailWc(){
        super();    
    }

    public PLCEmployeeDetailWc(String sSource){
        super();    
        this.sSource = sSource;
    }
    
    public String getSource(){
        return sSource;
    }
    
    public String getJsp() { 
		return AppConst.JSP_LOC + "/components/report/PLCEmployeeDetail.jsp";
	}
    
    public void setupChildren() {                
    }

    public void setFormBean(PWRAGetEmployeeDetailForm getEmployeeDetailForm){
        this.getEmployeeDetailForm = getEmployeeDetailForm;
    }    
    public PWRAGetEmployeeDetailForm getFormBean(){
        return this.getEmployeeDetailForm;
    }
} 
