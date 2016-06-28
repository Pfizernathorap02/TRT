package com.pfizer.webapp.wc.components.chart; 



import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.UserFilter;

import com.tgix.wc.WebComponent;



public final class GenericChartHeaderWc extends WebComponent { 

    private WebComponent rightWc;
    private WebComponent leftWc;
	private String district;

	private String region;

	private String area;
    private String team;

    private final String businessUnit;    
    private  String geographyDesc;    
    private final String geographyType;    
    private String salesOrgDesc="";
    private final String salesPositionDesc;
    
    // default value for the jsp page.
    private String jsp = AppConst.JSP_LOC + "/components/chart/genericChartHeader.jsp";
    

	
/*
	public GenericChartHeaderWc(String area, String region, String district, String team) {

		this.district = district;

		this.area = area;

		this.region = region;

		this.team = team;
	}
*/
	   public GenericChartHeaderWc(String businessUnit, String salesPositionDesc, String geographyType, String salesOrgDesc) {

		this.businessUnit = businessUnit;

		this.salesPositionDesc = salesPositionDesc;

		this.geographyType = geographyType;

		this.salesOrgDesc = salesOrgDesc;

	}    
    public String getJsp() {

		return jsp;

	}

    public void setJsp( String jsp ) {
        this.jsp = jsp;
    }
    
    

	public WebComponent getLeftWc() {
        return leftWc;
    }
    public WebComponent getRightWc() {
        return rightWc;
    }
    public void setLeftWc( WebComponent left ) {
        this.leftWc = left;
    }
    public void setRightWc( WebComponent right ) {
        this.rightWc = right;
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
    public String getTeam() {
        return team;
    }

      public String getBusinessUnit() {

		return businessUnit;

	}

	public String getSalesPositionDesc() {

		return salesPositionDesc;

	}

	public String getGeographyType() {

		return geographyType;

	}
    public String getSalesOrgDesc() {
        return salesOrgDesc;
    }

	public void setupChildren() {}	



} 

