package com.pfizer.db; 

import java.util.Date;

public class RBUGuestTrainersClassData 
{ 
    private String productDesc;
    private Date startDate;
    private Date endDate;
    
    public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
    }
    
    public Date getStartDate(){
        return startDate;
    }
    
    public void setStartDate(Date startDate){
        this.startDate = startDate;
    } 
    
     public Date getEndDate(){
        return endDate;
    }
    
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    } 
} 
