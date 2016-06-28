package com.pfizer.webapp.chart; 



public class ChartData {

	private String section;

	private int count;

	

	public ChartData( String section, int count) {

		this.section = section;

		this.count = count;

	} 

	

	public String getSection() {

		return this.section;

	}

	

	public int getCount() {

		return this.count;

	}

} 

