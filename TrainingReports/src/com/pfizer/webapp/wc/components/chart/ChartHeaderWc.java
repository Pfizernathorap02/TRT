package com.pfizer.webapp.wc.components.chart; 



import com.pfizer.db.P2lTrack;
import com.pfizer.webapp.AppConst;

import com.pfizer.webapp.chart.PieChart;

import com.pfizer.webapp.user.TerritoryFilterForm;

import com.pfizer.webapp.user.User;

import com.pfizer.webapp.user.UserFilter;

import com.tgix.wc.WebComponent;



public final class ChartHeaderWc extends WebComponent { 

	private  String district;
	private String region;
	private  String area;
	private final int numTrainees;

	private String productCd="";
    
    private String teamCd="";
    private P2lTrack track;
    
    private final String businessUnit;    
    private String geographyDesc;    
    private final String geographyType;    
    private String salesOrgDesc="";
    private final String salesPositionDesc;
   
    // default value for the jsp page.
    private String jsp = AppConst.JSP_LOC + "/components/chart/chartHeader.jsp";
    
     public ChartHeaderWc(String businessUnit, String salesPositionDesc, String geographyType, int numTrainess, String productCd ) {
		this.businessUnit = businessUnit;
		this.salesPositionDesc = salesPositionDesc;        
        this.geographyType = geographyType;
		this.numTrainees = numTrainess;
		this.productCd = productCd;
    }
	
	public ChartHeaderWc(String businessUnit, String salesPositionDesc, String geographyType, int numTrainess ) {
		this.businessUnit = businessUnit;
		this.salesPositionDesc = salesPositionDesc;       
        this.geographyType = geographyType;
		this.numTrainees = numTrainess;

	}
    
     public ChartHeaderWc(String businessUnit, String salesPositionDesc, String geographyType, int numTrainess, String productCd, String salesOrgDesc ) {
		this.businessUnit = businessUnit;
		this.salesPositionDesc = salesPositionDesc;        
        this.geographyType = geographyType;
		this.numTrainees = numTrainess;
		this.productCd = productCd;        
        this.salesOrgDesc = salesOrgDesc;
	}
    

	/*public ChartHeaderWc(String area, String region, String district, int numTrainess, String productCd ) {

		this.district = district;

		this.area = area;

		this.region = region;

		this.numTrainees = numTrainess;

		this.productCd = productCd;

	}
	public ChartHeaderWc(String area, String region, String district,int numTrainess ) {

		this.district = district;

		this.area = area;

		this.region = region;

		this.numTrainees = numTrainess;

	}*/
    
     public ChartHeaderWc(String area, String region, String district, int numTrainess, String productCd,String teamCd, String temp1, String temp2, String temp3) {

		this.district = district;

		this.area = area;

		this.region = region;

		this.numTrainees = numTrainess;

		this.productCd = productCd;
        
        this.teamCd=teamCd;
        
        this.businessUnit = temp1;
		this.salesPositionDesc = temp2;       
        this.geographyType = temp3;

	}
    

    public String getJsp() {

		return jsp;

	}

    public void setTrack( P2lTrack track ) {
        this.track = track;
    }
    public P2lTrack getTrack() {
        return track;
    }
    public void setJsp( String jsp ) {
        this.jsp = jsp;
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

     public String getSalesOrgDesc(){
        return this.salesOrgDesc;
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
	public void setupChildren() {}	

} 

