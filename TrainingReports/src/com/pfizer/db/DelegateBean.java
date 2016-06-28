package com.pfizer.db; 

import java.util.Date;

public class DelegateBean 
{ 
    private String fName;
    private String lName;
    private String empId;
    
    public DelegateBean() {
        fName = "";
        lName = "";
        empId = "";
    }    
    
    public String getfName() {
        return fName;
    }
    
    public void setfName(String fName) {
        this.fName =fName;
    }
    
    public String getlname() {
        return lName;
    }
    
    public void setlName(String lName) {
        this.lName = lName;
    }
    
    public String getempId() {
        return empId;
    }
    
    public void setempId(String empId) {
        this.empId = empId;
    }
    
    
} 

