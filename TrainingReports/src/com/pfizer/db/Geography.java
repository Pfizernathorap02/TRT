package com.pfizer.db; 

public class Geography {
	
	private String parentGeographyId = null;
	private String childGeographyId = null;
	private String childGeographyDesc = null;
	private String childGeographyType = null;
	private String geoLevel = null;
	
	public String getParentGeographyId() {
		return parentGeographyId;
	}
	public void setParentGeographyId(String parentGeographyId) {
		this.parentGeographyId = parentGeographyId;
	}
	
	public String getChildGeographyId() {
		return childGeographyId;
	}
	public void setChildGeographyId(String childGeographyId) {
		this.childGeographyId = childGeographyId;
	}
	
	public String getChildGeographyDesc() {
		return childGeographyDesc;
	}
	public void setChildGeographyDesc(String childGeographyDesc) {
		this.childGeographyDesc = childGeographyDesc;
	}
	
	public String getChildGeographyType() {
		return childGeographyType;
	}
	public void setChildGeographyType(String childGeographyType) {
		this.childGeographyType = childGeographyType;
	}
	
	public String getGeoLevel() {
		return geoLevel;
	}
	public void setGeoLevel(String geoLevel) {
		this.geoLevel = geoLevel;
	}
}    
	