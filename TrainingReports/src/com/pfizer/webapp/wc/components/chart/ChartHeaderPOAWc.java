package com.pfizer.webapp.wc.components.chart; 



import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.UserFilter;

import com.tgix.wc.WebComponent;



public final class ChartHeaderPOAWc extends WebComponent { 

	private final String district;

	private final String region;

	private final String area;
    
    private final String team;

	
    

	

	public ChartHeaderPOAWc(String team,String area, String region, String district) {

		this.district = district;

		this.area = area;

		this.region = region;
        
        this.team=team;

	
	}

    public String getJsp() {

		return AppConst.JSP_LOC + "/components/chart/chartHeaderPOA.jsp";

	}

	
	

	public String getDistrict() {

		return district;

	}

	public String getRegion() {

		return region;

	}

	public String getArea() {

		return area;

	}

	

	public void setupChildren() {}	



} 

