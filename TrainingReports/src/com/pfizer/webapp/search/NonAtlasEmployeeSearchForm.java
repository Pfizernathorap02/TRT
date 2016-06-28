package com.pfizer.webapp.search; 

import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.List;

public class NonAtlasEmployeeSearchForm 
{ 
    /*New java file added for PXED Search */
    public static String FIELD_FNAME = "fname";
	public static String FIELD_LNAME = "lname";
    public static String FIELD_EMAILID = "emailid";
    public static String FIELD_NTACCT = "ntacct";
    
    private String fname = new String();
	private String lname = new String();
    private String emailid=new String();
    private String ntacct=new String();
    
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
    
    public String getEmailid() {
		return this.emailid;
	}
	public void setEmailid(String emailid) {
		this.emailid = emailid;
	}
	public String getNtacct() {
		return this.ntacct;
	}
	public void setNtacct(String ntAcct) {
		this.ntacct = ntAcct;
	}
} 
