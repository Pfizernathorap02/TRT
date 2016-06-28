package com.pfizer.db; 



public class Territory { 

	public Territory() {}
	
    /*added for RBU*/
    private String level1;
    private String level2;
    private String level3;
    private String level4;
    private String level5;
    private String level6;
    private String level7;
    private String level8;
    private String level9;
    private String level10;
    /* End of addition */	

	private String areaCd;
	private String areaDesc;
	private String regionCd;
	private String regionDesc;
	private String districtId;
	private String districtDesc;
	private String territoryId;

    /*Added for RBU */
    public String getLevel1() {
		return this.level1;
	}
	public void setLevel1( String level1 ) {
		this.level1 = level1;
	}
    
    public String getLevel2() {
		return this.level2;
	}
	public void setLevel2( String level2 ) {
		this.level2 = level2;
	}
    
    public String getLevel3() {
		return this.level3;
	}
	public void setLevel3( String level3 ) {
		this.level3 = level3;
	}
    
    public String getLevel4() {
		return this.level4;
	}
	public void setLevel4( String level4 ) {
		this.level4 = level4;
	}
    
    public String getLevel5() {
		return this.level5;
	}
	public void setLevel5( String level5 ) {
		this.level5 = level5;
	}
    
    public String getLevel6() {
		return this.level6;
	}
	public void setLevel6( String level6 ) {
		this.level6 = level6;
	}
    
    public String getLevel7() {
		return this.level7;
	}
	public void setLevel7( String level7 ) {
		this.level7 = level7;
	}
    
    public String getLevel8() {
		return this.level8;
	}
	public void setLevel8( String level8 ) {
		this.level8 = level8;
	}
    
    public String getLevel9() {
		return this.level9;
	}
	public void setLevel9( String level9 ) {
		this.level9 = level9;
	}
    
    public String getLevel10() {
		return this.level10;
	}
	public void setLevel10( String level10 ) {
		this.level10 = level10;
	}
    //ended for RBU
	public String getAreaCd() {
		return this.areaCd;
	}
	public void setAreaCd( String areaCd ) {
		this.areaCd = areaCd;
	}
	public String getAreaDesc() {
		return this.areaDesc;
	}
	public void setAreaDesc( String desc ) {
		this.areaDesc = desc;
	}
	public String getRegionCd() {
		return this.regionCd;
	}	
	public void setRegionCd( String cd ) {
		this.regionCd = cd;
	}

	public String getRegionDesc() {
		return this.regionDesc;
	}
	public void setRegionDesc( String desc ) {
		this.regionDesc = desc;
	}
	public String getDistrictId() {
		return this.districtId;
	}
	public void setDistrictId( String id ) {
		this.districtId = id;
	}
	public String getDistrictDesc() {
		return this.districtDesc;
	}
	public void setDistrictDesc( String desc ) {
		this.districtDesc = desc;
	}
	public String getTerritoryId() {
		return this.territoryId;
	}
	public void setTerritoryId(String id) {
		this.territoryId = id;
	}

	public String toString() {

		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "      areaCd: " + areaCd + "\n" );
		sb.append( "    areaDesc: " + areaDesc + "\n" );
		sb.append( "    regionCd: " + regionCd + "\n" );
		sb.append( "  regionDesc: " + regionDesc + "\n" );
		sb.append( "  districtId: " + districtId + "\n" );
		sb.append( "districtDesc: " + districtDesc + "\n" );
		sb.append( " territoryId: " + territoryId + "\n" );
		return sb.toString();
	}

} 

