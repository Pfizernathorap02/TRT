package com.pfizer.webapp.wc.RBU;





import com.pfizer.actionForm.RBUGetEmployeeDetailFormToviazLaunch;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class ToviazLaunchEmployeeDetailsWc extends WebComponent
{
	RBUGetEmployeeDetailFormToviazLaunch getEmployeeDetailForm;
    private String sSource = "";

    public ToviazLaunchEmployeeDetailsWc(){
        super();
    }

    public ToviazLaunchEmployeeDetailsWc(String sSource){
        super();
        this.sSource = sSource;
    }

    public String getSource(){
        return sSource;
    }
    //TODO - find out if this jsp is reusable - Shannon
    public String getJsp() {
		return AppConst.JSP_LOC + "/components/report/ToviazLaunchEmployeeDetail.jsp";
	}

    public void setupChildren() {
    }

    public void setFormBean(RBUGetEmployeeDetailFormToviazLaunch getEmployeeDetailForm){
        this.getEmployeeDetailForm = getEmployeeDetailForm;
    }
    public RBUGetEmployeeDetailFormToviazLaunch getFormBean(){
        return this.getEmployeeDetailForm;
    }
}
