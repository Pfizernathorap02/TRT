package com.pfizer.db; 
import java.util.Date;

public class RBUEnrollChangeReport 
{ 
    private String emplid;
    private String first_name;
    private String last_name;
    private String email_address;
    private String product_cd;
    private String operation;
    private String reason;
    private Date operation_date;
    private String changed_by;

    
    public String getEmplId() {
        return emplid;
    }
    
    public void setEmplId(String param) {
        this.emplid = param;
    }
    
    public String getFirstName() {
        return first_name;
    }
    
    public void setFirstName(String param) {
        this.first_name = param;
    }
    
    public String getLastName() {
        return last_name;
    }
    
    public void setLastName(String param) {
        this.last_name = param;
    }
    
    public String getEmailAddress() {
        return email_address;
    }
    
    public void setEmailAddress(String param) {
        this.email_address = param;
    }
    
    public String getProdctCd() {
        return product_cd;
    }
    
    public void setProductCd(String param) {
        this.product_cd = param;
    }
    
    public String getOperation() {
        return operation;
    }
    
    public void setOperation(String param) {
        this.operation = param;
    }
    
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String param) {
        this.reason = param;
    }
    
     public String getChanged_by() {
        return changed_by;
    }
    
    public void setChanged_by(String param) {
        this.changed_by = param;
    }

	public Date getOperationDate() {
		return operation_date;
	}
        
	public void setOperationDate(Date param) {
		operation_date = param;
	}
} 
