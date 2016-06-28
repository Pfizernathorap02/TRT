package com.pfizer.webapp.wc.global; 

public class ChartBean 
{ 
    private String courseStatus;
    private int total;
    
    
    public void setCourseStatus(String courseStatus){
        this.courseStatus=courseStatus;
    }
    public String getCourseStatus(){
        return this.courseStatus;
    }
    
    public void setTotal(int total){
        this.total=total;
    }
    public int getTotal(){
        return this.total;
    }
    
        
    
} 
