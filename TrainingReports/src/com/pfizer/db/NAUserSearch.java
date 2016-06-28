package com.pfizer.db; 

public class NAUserSearch 
{ 
    private String firstName = "";
	private String lastName = "";
	private String ntacct = "";
	private String emailid = "";
    
    
    
    public void setFirstName( String first ) {
		this.firstName = first;
	}
	public String getFirstName() {
		return this.firstName;
	}
	public NAUserSearch() {}
    
    public String getLastName() {
		return this.lastName;
	}
	public void setLastName( String lastName ) {
		this.lastName = lastName;
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
    
    public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( firstName);
		sb.append( "," + lastName+ ";" );
		sb.append( ntacct  );
        return sb.toString();
	}
} 
