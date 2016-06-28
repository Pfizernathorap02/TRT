package com.tgix.printing; 

import java.util.ArrayList;
import java.util.List;

public class TRMGroupBean 
{ 
    private String groupID;
	private String clusterCD;
	private String products;
    private String dateOrdered;
    private List dateOrderedList=new ArrayList();
	
    public String getClusterCD() {
		return clusterCD;
	}
	public void setClusterCD(String clusterCD) {
		this.clusterCD = clusterCD;
	}
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
    
    public void setDateOrdered(String dateOrdered){
        this.dateOrdered=dateOrdered;
        
    }
    
    public String getDateOrdered(){
        return this.dateOrdered;
    }
    
    public void addDateOrdered(String dateOrdered){
        this.dateOrderedList.add(dateOrdered);
    }
    
    public List getAllOrderedDate(){
        return this.dateOrderedList;
    }
    
    
} 
