package com.pfizer.db; 

import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class P2lEmployeeStatus {
    private Employee employee;
    private Date completeDate;
    private String status;
    private String statusToDisplay;
    private String phaseName;
    // Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid)
    private Date statusDate;
    // End : Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid)
    private List completeIds = new ArrayList();
    
    /* Added for CUE Training enhancement */
    private String score;
    
    public P2lEmployeeStatus( Employee employee, String status, String phaseName ){
        this.employee = employee;
        this.status = status;
        this.phaseName = phaseName;
    } 
    
    public Date getCompleteDate() {
        return completeDate;
    }
    public void setCompleteDate( Date date) {
        this.completeDate = date;
    }
    public String getPhaseName() {
        return phaseName;
    }
    public void setStatus( String status ) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
    
    public void setStatusToDisplay( String statusToDisplay ) {
        this.statusToDisplay = statusToDisplay;
    }
    public String getStatusToDisplay() {
        return statusToDisplay;
    }
    
    public List getCompleteId() {
        return completeIds;
    }
    private void setComplete( String id ) {
        completeIds.add(id);
    }
    public Employee getEmployee() {
        return employee;
    }
    
     /* Added for CUE Training enhancement */    
     public void setScore( String score ) {
        this.score = score;
    }
    public String getScore() {
        return score;
    }
  // Start: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid)
    public void setStatusDate(Date date)
    {
        this.statusDate = date;
    }
    public Date getStatusDate()
    {
        return this.statusDate;
    }
    // End: Modified for TRT 3.6 enhancement - F 4.5 -(user view of employee grid)
} 
