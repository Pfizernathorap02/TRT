package com.pfizer.webapp.wc.RBU; 

import com.pfizer.webapp.wc.POA.POAChartBean;

public class RBUChartBean extends POAChartBean 
{ 
    private String status;
    private int total;
    
    
    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return this.status;
    }
    
    public void setTotal(int total){
        this.total=total;
    }
    public int getTotal(){
        return this.total;
    }
    
} 
