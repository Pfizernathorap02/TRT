package com.pfizer.webapp.search; 

import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.List;

public class EmplSearchForm {
	public static String FIELD_FNAME = "SearchForm_fname";
	public static String FIELD_LNAME = "SearchForm_lname";
    public static String FIELD_EMPLID = "SearchForm_emplid";
    public static String FIELD_TERRITORYID = "SearchForm_terrId";
    
	
	private String fname = new String();
	private String lname = new String();
    private String emplid=new String();
    private String terrId=new String();
	 
    /* Addition of code for RBU changes */
    public static String FIELD_EMAIL = "SearchForm_email";
    public static String FIELD_BU = "SearchForm_bu";
    public static String FIELD_SALESORG = "SearchForm_salesorg";
    public static String FIELD_ROLE = "SearchForm_role";
    public static String FIELD_SALESPOSID= "SearchForm_salesposId";
    
    private String salesposId = new String();   
    private String email=new String();
    private String bu=new String();
    private String salesorg=new String();
    private String role=new String();
    
    private List buList = new ArrayList();
    private List salesOrgList = new ArrayList();
    private List roleList = new ArrayList();
    /* End of addition */
    
	public void setFname( String fname ) {
		this.fname = fname;
	} 
	public String getFname() {
		return fname;
	}
	
	public void setLname( String lname ) {
		this.lname = lname;
	} 
	public String getLname() {
		return lname;
	}
    
    public String getEmplid() {
		return this.emplid;
	}
	public void setEmplid(String emplid) {
		this.emplid = emplid;
	}
	public String getTerrId() {
		return this.terrId;
	}
	public void setTerrId(String terrId) {
		this.terrId = terrId;
	}
    
     /* Adding getter and setter methods for RBU changes */
    public String getEmail() {
		return this.email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
    public String getBu() {
		return this.bu;
	}
	public void setBu(String bu) {
		this.bu = bu;
	}
    public String getSalesorg() {
		return this.salesorg;
	}
	public void setSalesorg(String salesorg) {
		this.salesorg = salesorg;
	}
    public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
    public void setBuList(LabelValueBean labelValBean){
        this.buList.add(labelValBean);
    }
     public List getBuList(){
        return this.buList;
    }
    
    public void setSalOrgList(LabelValueBean labelValBean){
        this.salesOrgList.add(labelValBean);
    }
     public List getSalOrgList(){
        return this.salesOrgList;
    }
    
    public void setRoleList(LabelValueBean labelValBean){
        this.roleList.add(labelValBean);
    }
     public List getRoleList(){
        return this.roleList;
    }   
     public String getSalesposId() {
		return this.salesposId;
	}
	public void setSalesposId(String salesposId) {
		this.salesposId = salesposId;
	}       
    /* End of addition */	 
	
	 
	 
    
    
	
} 
