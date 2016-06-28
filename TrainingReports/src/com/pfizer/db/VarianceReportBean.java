package com.pfizer.db; 

import java.util.Date;
import java.util.HashMap;

public class VarianceReportBean { 
	private String emplid = "";
	private String firstname = "";
	private String lastname = "";
	private String precluster = "";
	private String preteam = "";
	private String prerole = "";
	private String preproduct = "";
	private String postcluster = "";
	private String postteam = "";
	private String postrole = "";
	private String postproduct = "";
    private String requiredproducts = "";
    //added by Shannon for RBU
    private String credits = "";
    private String futurebu = "";
	private String futurerbu = "";          
        
    private HashMap productStatusMap=new HashMap();
	
	public VarianceReportBean() {}
        
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

    public String getPreCluster(){
        return this.precluster;
    }

    public void setPreCluster(String param){
        this.precluster = param;
    }

    public String getPreTeam(){
        return this.preteam;
    }

    public void setPreTeam(String param){
        this.preteam = param;
    }

    public String getPreRole(){
        return this.prerole;
    }

    public void setPreRole(String param){
        this.prerole = param;
    }

    public String getPreProduct(){
        return this.preproduct;
    }

    public void setPreProduct(String param){
        this.preproduct = param;
    }
    
    public String getPostCluster(){
        return this.postcluster;
    }

    public void setPostCluster(String param){
        this.postcluster = param;
    }

    public String getPostTeam(){
        return this.postteam;
    }

    public void setPostTeam(String param){
        this.postteam = param;
    }

    public String getPostRole(){
        return this.postrole;
    }

    public void setPostRole(String param){
        this.postrole = param;
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
    
    public String getCredits(){
        return this.credits;
    }

    public void setCredits(String param){
        this.credits = param;
    }	
    
    public String getFutureBU(){
        return this.futurebu;
    }

    public void setFutureBU (String param){
        this.futurebu = param;
    }	
    
    public String getFutureRBU(){
        return this.futurerbu;
    }

    public void setFutureRBU (String param){
        this.futurerbu = param;
    }
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		
		return sb.toString();
	}
} 
