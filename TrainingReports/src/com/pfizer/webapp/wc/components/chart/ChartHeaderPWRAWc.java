package com.pfizer.webapp.wc.components.chart; 



import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.UserFilter;

import com.tgix.wc.WebComponent;



public final class ChartHeaderPWRAWc extends WebComponent { 

	private final String district;

	private final String region;

	private final String area;

	private final int numTrainees;

	private String productCd;
    
    private String teamCd;
    
    

	

	public ChartHeaderPWRAWc(String area, String region, String district, int numTrainess, String productCd) {

		this.district = district;

		this.area = area;

		this.region = region;

		this.numTrainees = numTrainess;

		this.productCd = productCd;

	}
    
    public ChartHeaderPWRAWc(String area, String region, String district, int numTrainess, String productCd, String teamCd) {

		this.district = district;

		this.area = area;

		this.region = region;

		this.numTrainees = numTrainess;

		this.productCd = productCd;
        
        this.teamCd=teamCd;

	}

    

    public String getJsp() {

		return AppConst.JSP_LOC + "/components/chart/chartHeaderPWRA.jsp";

	}

	public String getProductCode() {

		return this.productCd;

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

	public int getNumTrainees() {

		return numTrainees;

	}
    
    
    public String getTeamCd(){
        return this.teamCd;
    }

	public void setupChildren() {}	



} 

