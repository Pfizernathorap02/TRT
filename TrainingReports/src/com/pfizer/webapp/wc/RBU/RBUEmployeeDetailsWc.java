package com.pfizer.webapp.wc.RBU; 





import com.pfizer.actionForm.RBUGetEmployeeDetailForm;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class RBUEmployeeDetailsWc extends WebComponent
{ 
	RBUGetEmployeeDetailForm getEmployeeDetailForm;
    private String sSource = "";

    public RBUEmployeeDetailsWc(){
        super();    
    }

    public RBUEmployeeDetailsWc(String sSource){
        super();    
        this.sSource = sSource;
    }
    
    public String getSource(){
        return sSource;
    }
    //TODO - find out if this jsp is reusable - Shannon
    public String getJsp() { 
		return AppConst.JSP_LOC + "/components/report/RBUEmployeeDetail.jsp";
	}
    
    public void setupChildren() {                
    }

    public void setFormBean(RBUGetEmployeeDetailForm getEmployeeDetailForm){
        this.getEmployeeDetailForm = getEmployeeDetailForm;
    }    
    public RBUGetEmployeeDetailForm getFormBean(){
        return this.getEmployeeDetailForm;
    }
} 
