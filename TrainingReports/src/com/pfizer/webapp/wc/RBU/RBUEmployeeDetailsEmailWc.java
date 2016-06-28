package com.pfizer.webapp.wc.RBU;




import com.pfizer.actionForm.RBUGetEmployeeDetailForm;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class RBUEmployeeDetailsEmailWc extends WebComponent
{
    RBUGetEmployeeDetailForm getEmployeeDetailForm;
    private String sSource = "";

    public RBUEmployeeDetailsEmailWc(){
        super();
    }

    public RBUEmployeeDetailsEmailWc(String sSource){
        super();
        this.sSource = sSource;
    }

    public String getSource(){
        return sSource;
    }
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/RBUEmployeeDetailEmailReminder.jsp";
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
