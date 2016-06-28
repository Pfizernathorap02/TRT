package com.pfizer.processor; 

public class ORSortBy {
	public static final String FIELD_SECTION			= "sb_field";	
	public static final String FIELD_TYPE				= "sb_direction";
	
	private String field;
	private String direction;

	public ORSortBy () {}

	public void setField( String field ) {
		this.field = field;	
	} 
	public String getField() {
		return field;
	}
	public void setDirection( String dir ) {
		this.direction = dir;
	}
	public String getDirection() {
		return this.direction;
	}
} 
