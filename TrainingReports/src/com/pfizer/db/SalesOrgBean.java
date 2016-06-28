package com.pfizer.db; 

public class SalesOrgBean 
{ 
    
    private String salesOrgCd;
    private String salesOrgDesc;
    
    public void setSalesOrgCd(String salesOrgCd){
        this.salesOrgCd=salesOrgCd;
    }
    
    public String getSalesOrgCd(){
        return this.salesOrgCd;
    }
    
    public void setSalesOrgDesc(String salesOrgDesc){
        this.salesOrgDesc=salesOrgDesc;
    }
    
    public String getSalesOrgDesc(){
        return this.salesOrgDesc;
    }
    
} 
