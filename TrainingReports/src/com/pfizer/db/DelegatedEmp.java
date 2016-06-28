package com.pfizer.db; 

import java.util.Date;

public class DelegatedEmp 
{ 
    private String delfrId;
    private String deltoId;
    private String delfr="";
    private String delto="";
    
    
    public String getdelfrId() {
        return delfrId;
    }
    
    public void setdelfrId(String delfrId) {
        this.delfrId = delfrId;
    }
    
     public String getdeltoId() {
        return deltoId;
    }
    
    public void setdeltoId(String deltoId) {
        this.deltoId = deltoId;
    }
    
    public String getdelfr() {
        return delfr;
    }
    
    public void setdelfr(String delfr) {
        this.delfr = this.delfr + " " + delfr;
    }
    
    public String getdelto() {
        return delto;
    }
    
    public void setdelto(String delto) {
        this.delto = this.delto + " " +delto;
    }
    public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "       delfr: " + delfr + "\n" );
		sb.append( "    delto: " + delto + "\n" );
		return sb.toString();
	}
    
    
} 
