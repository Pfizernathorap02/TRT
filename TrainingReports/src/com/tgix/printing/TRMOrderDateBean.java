package com.tgix.printing; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TRMOrderDateBean  implements Serializable 
{ 
    private String OrderDate;
		
    public String getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(String orderDate) {
		this.OrderDate = orderDate;
	}    
    
} 
