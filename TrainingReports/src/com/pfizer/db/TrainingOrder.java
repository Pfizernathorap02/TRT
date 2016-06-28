package com.pfizer.db; 

public class TrainingOrder 
{ 
    private String trmId="";
    private String orderDate="";
    
    public void setTrmId(String trmId){
        this.trmId=trmId;
    }
    public String getTrmId(){
        return this.trmId;
    }
    
    public void setOrderDate(String orderDate){
        this.orderDate=orderDate;
    }
    public String getOrderDate(){
        return this.orderDate;
    }        
        
} 
