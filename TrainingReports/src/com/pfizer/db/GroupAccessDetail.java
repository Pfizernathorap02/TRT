package com.pfizer.db; 

public class GroupAccessDetail 
{ 
    private String isSave = "";
	private String isSubmit = "";
    
    public void setIsSave( String isSave ) {
		this.isSave = isSave;
	}
	public String getIsSave() {
		return this.isSave;
	}
	public GroupAccessDetail() {}
    
    public String getIsSubmit() {
		return this.isSubmit;
	}
	public void setIsSubmit( String isSubmit ) {
		this.isSubmit = isSubmit;
	}
} 
