package com.pfizer.db; 

public class Sce {
	public static final String STATUS_DC			= "Demonstrated Competence (DC)";
	public static final String STATUS_NI			= "Needs Improvement (NI)";
	public static final String STATUS_UN			= "Unacceptable (UN)";
	public static final String STATUS_NOT_COMPLETE	= "Not Applicable";
	
	private String rating;
	private String emplid;
	
	public Sce(){}
	
	public void setRating(String rating) {
		this.rating = rating;
	} 
	public String getRating() {
		return rating;
	}
	public String getEmplid() {
		return this.emplid;
	}
	public void setEmplid(String id) {
		this.emplid = id;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "rating: " + rating + "\n" );
		sb.append( "emplid: " + emplid + "\n" );
		return sb.toString();
	}
} 
