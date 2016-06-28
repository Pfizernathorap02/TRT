package com.pfizer.db; 

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class StatusSubSetBean {

private boolean statusSubsetSelected;
private String chkStatus;
private String statusFromDate;
private String statusToDate;


  
public void setStatusSubsetSelected(boolean statusSubsetSelected){
       this.statusSubsetSelected = statusSubsetSelected;
    }
public boolean isStatusSubsetSelected()
{
    return statusSubsetSelected;
    
}
    
public void setChkStatus(String chkStatus ) {
        this.chkStatus = chkStatus;
    }
    
public String getChkStatus() {
        return chkStatus;
    }
    
public void setStatusFromDate(String statusFromDate ) {
        this.statusFromDate = statusFromDate;
    }
    
public String getStatusFromDate() {
        return statusFromDate;
    }
    
public void setStatusToDate(String statusToDate ) {
        this.statusToDate = statusToDate;
    }
    
public String getStatusToDate() {
        return statusToDate;
    }
}

