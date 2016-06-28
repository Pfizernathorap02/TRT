package com.pfizer.db; 

public class SalesPosition {
    
    private String reportToSalesPosId = null;
	private String childSalesPosId = null;
	private String childSalesPosDesc = null;
	private String salesLevel = null;
	
	public String getReportToSalesPosId() {
		return reportToSalesPosId;
	}
	public void setReportToSalesPosId(String reportToSalesPosId) {
		this.reportToSalesPosId = reportToSalesPosId;
	}
	
	public String getChildSalesPosId() {
		return childSalesPosId;
	}
	public void setChildSalesPosId(String childSalesPosId) {
		this.childSalesPosId = childSalesPosId;
	}
	
	public String getChildSalesPosDesc() {
		return childSalesPosDesc;
	}
	public void setChildSalesPosDesc(String childSalesPosDesc) {
		this.childSalesPosDesc = childSalesPosDesc;
	}
		
	public String getSalesLevel() {
		return salesLevel;
	}
	public void setSalesLevel(String salesLevel) {
		this.salesLevel = salesLevel;
	}
}    
	