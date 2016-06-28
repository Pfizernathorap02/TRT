package com.pfizer.db; 

import java.util.Date;
import java.util.HashMap;

public class RBUSearchBean { 
    //SELECT EMPLID, FIRST_NAME, LAST_NAME, Employee_Status, Active, Future_Role, Sales_Position,current_products,future_products, required_products 
	private String emplid = "";
	private String firstname = "";
	private String lastname = "";
	private String Employee_Status = "";
	private String Active = "";
	private String Future_Role = "";
	private String Sales_Position = "";
	private String current_products = "";
	private String postproduct = "";
    private String requiredproducts = "";

	            
        
    private HashMap productStatusMap=new HashMap();
	
	public RBUSearchBean() {}
        
    public String getEmplID(){
        return this.emplid;
    }

    public void setEmplID(String sEmplID){
        this.emplid = sEmplID;
    }

    public String getFirstName(){
        return this.firstname;
    }

    public void setFirstName(String sFirstName){
        this.firstname = sFirstName;
    }

    public String getLastName(){
        return this.lastname;
    }

    public void setLastName(String sLastName){
        this.lastname = sLastName;
    }





    public String getPostProduct(){
        return this.postproduct;
    }

    public void setPostProduct(String param){
        this.postproduct = param;
    }	

    public String getRequiredProducts(){
        return this.requiredproducts;
    }

    public void setRequiredProducts(String param){
        this.requiredproducts = param;
    }	
    
    public String getFuture_Role(){
        return this.Future_Role;
    }
    public void setFuture_Role(String param){
        this.Future_Role = param;
    }	
    
    public String getSales_Position(){
        return this.Sales_Position;
    }
    public void setSales_Position(String param){
        this.Sales_Position = param;
    }
    
    public String getCurrent_products(){
        return this.current_products;
    }
    public void setCurrent_products(String param){
        this.current_products = param;
    }
    
   public String getEmployee_Status(){
        return this.Employee_Status;
    }
    public void setEmployee_Status(String param){
        this.Employee_Status = param;
    }
    
    public String getActive(){
        return this.Active;
    }
    public void setActive(String param){
        this.Active = param;
    }

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		
		return sb.toString();
	}
} 
