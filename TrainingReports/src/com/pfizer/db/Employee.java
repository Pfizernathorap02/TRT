package com.pfizer.db; 

import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Employee { 
	private String emplId = "";
	private String geoType = "";
	private String guId = "";
	private String role = "";
	private String teamCode = "";
	private String clusterCode = "";
	private String lastName = "";
	private String middleName = "";
	private String firstName = "";
	private String preferredName = "";
	
	private String areaCd = "";
	private String areaDesc = "";
	private String regionCd = "";
	private String regionDesc = "";
	private String districtId = "";
	private String districtDesc = "";
	
	private String reportsToEmplid = "";
	private String gender = "";
	private String email = "";
	private Date hireDate = null;
	private Date promoDate = null;
	private String employeeStatus="";
    
    private String managerFname = "";
    private String managerLname = "";
    
    private String exemptionReason="";
		
	private String territoryId;
    
    private String courseStatus="";
    
    private String futureBU;
    private String futureRBU;
    private String ped1;
    private String ped2;
    private String ped3;
    private String ped4;
    private String sce;
    private String overAll;
    private HashMap productStatusMap=new HashMap();
	private List availableExams;
    private String productDesc = "";
    private String productCode = "";
    private String attendance = "";
    private String reportsToEmail = "";
	/*Adding variables for RBU changes */
    private String geographyId;  
    private String geographyDesc;
    private String geographyType;
    private String salesPositionId="";  
    private String salesOrgDesc="";
    private String parentGeographyId;
    private String businessUnit="";
    private String salesPostionDesc="";
    private String reportsToSalesPosition="";
    
    // Start: Modified for TRT 3.6 enhancement - F 4.4 -(user view of employee grid)     
    
    private String NtId;
    private String State;
    private String managerEmail;
    private String source;
    private String sourceOrg;
    
    // End: Modified for TRT 3.6 enhancement - F 4.4 -(user view of employee grid)  
    private ArrayList mutipleGeos=new ArrayList();
    private ArrayList multipleGeoIds=new ArrayList();
    private boolean isMultipleGeo=false;
    private ArrayList multipleGeoList=new ArrayList();
    private String emplIdForSpRole = "";
    private String salesPositionIdForSpRole = "";
    
    private String roleDesc;
    /*End of addition*/
    
    /* Added for CSO enhancement*/
    
    private String salesPositionTypeCode="";
     /*End of addition*/
    // Starte: Added for TRT phase 2
    private String groupCD = "";
    private String hqManager = "";
    
	public Employee() {}
    
   public List getAvailableExams(){
    return availableExams;
   }
   
   public void setAvailableExams(List availableExams){
        this.availableExams  = availableExams;
   }
   
    public void setManagerFname( String name ) {
        this.managerFname = name;
    }
    public String getManagerFname( ) {
        return this.managerFname;
    }
    
     public void setReportsToEmail( String reportsToEmail ) {
        this.reportsToEmail = reportsToEmail;
    }
    public String getReportsToEmail( ) {
        return this.reportsToEmail;
    }
    
    
     public void setAttendance( String attendance ) {
        this.attendance = attendance;
    }
    public String getAttendance( ) {
        return this.attendance;
    }
    
     public void setProductDesc( String productDesc ) {
        this.productDesc = productDesc;
    }
    public String getProductDesc( ) {
        return this.productDesc;
    }
    
     public void setProductCode( String productCodec ) {
        this.productCode = productCode;
    }
    public String getProductCode( ) {
        return this.productCode;
    }
    
    public void setManagerLname( String name ) {
        this.managerLname = name;
    }
    public String getManagerLname( ) {
        return this.managerLname;
    }

    public void addToproductStatusMap(String product_cd,String status){
        productStatusMap.put(product_cd,status);
    }
    
    public HashMap getProductStatusMap(){
        return this.productStatusMap;
    }
    
	public void setEmployeeStatus(String employeeStatus ){
        this.employeeStatus=employeeStatus;
    }
    public String getEmployeeStatus(){
        return this.employeeStatus;
    }
    
    public String getGeoType() {
        return this.geoType;
    }
    public void setGeoType( String str ) {
        this.geoType = str;
    }
    public String getGuid() {
        return this.guId;
    }
    
    public void setGuid( String guid ) {
        this.guId = guid;
    }
    
	public String getEmplId() {
		return this.emplId;
	}	
	public void setEmplId( String id ) {
		this.emplId = id;
	}
    //added by neha for MO user---begin
    public String getEmplIdForSpRole(){
		return this.emplIdForSpRole;
	}	
	public void setEmplIdForSpRole( String id ) {
		this.emplIdForSpRole = id;
	}
    
    public String getSalesPositionIdForSpRole(){
		return this.salesPositionIdForSpRole;
	}	
	public void setSalesPositionIdForSpRole( String id ) {
		this.salesPositionIdForSpRole = id;
	}
	//added by neha for MO user --end
	public void setReportsToEmplid( String id ) {
		this.reportsToEmplid = id;
	}
	public String getReportsToEmplid() {
		return this.reportsToEmplid;
	}
	
	public void setGender( String gender ) {
		this.gender = gender;
	}
	public String getGender() {
		return this.gender;
	}
	
	public void setEmail( String email ) {
		this.email = email;
	}
	public String getEmail() {
		return this.email;
	}
	
	public void setHireDate( Date date ) {
		this.hireDate = date;
	}
	public Date getHireDate() {
		return this.hireDate;
	}
	public void setPromoDate( Date date ) {
		this.promoDate = date;
	}
	public Date getPromoDate() {
		return this.promoDate;
	}
	
	
	public void setFirstName( String first ) {
		this.firstName = first;
	}
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setLastName( String lastName ) {
		this.lastName = lastName;
	}
	public String getLastName() {
		return this.lastName;
	}
	
	public void setPreferredName( String name ) {
		this.preferredName = name;
	}
	public String getPreferredName() {
		return this.preferredName;
	}
	
	public void setMiddleName( String middle ) {
		this.middleName = middle;
	}
	public String getMiddleName () {
		return this.middleName;
	}
	
	
	public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String code) {
		this.teamCode = code;
	}
	
	public String getClusterCode() {
		return clusterCode;
	}
	
	public String getDisplayCluster() {
	/*	if ("Specialty Marke".equals(clusterCode)) {
			return "Specialty Market";
		}
		return clusterCode;
	*/
	return salesOrgDesc;
	}
	
	public void setClusterCode(String code) {
		this.clusterCode = code;
	}

	
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
	
	public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
    public void setExemptionReason(String exemptionReason){
        this.exemptionReason=exemptionReason;
    }
    
    public String getExemptionReason(){
        return this.exemptionReason;
    }
    
       
    public void setCourseStatus(String courseStatus){
        this.courseStatus=courseStatus;
    }
    
    public String getCourseStatus(){
        return this.courseStatus;
    }
    
    public String getFutureBU(){
        return futureBU;
    }
    public void setFutureBU(String futureBU){
        this.futureBU = futureBU;
    }
     public String getFutureRBU(){
        return futureRBU;
    }
    public void setFutureRBU(String futureRBU){
        this.futureRBU = futureRBU;
    }
    
    public String getPed1(){
        return ped1;
    }
    
    public void setPed1(String ped1){
        this.ped1 = ped1;
    }
    
     public String getPed2(){
        return ped2;
    }
    
    public void setPed2(String ped2){
        this.ped2 = ped2;
    }
     public String getPed3(){
        return ped3;
    }
    
    public void setPed3(String ped3){
        this.ped3 = ped3;
    }
     public String getPed4(){
        return ped4;
    }
    
    public void setPed4(String ped4){
        this.ped4 = ped4;
    }
    
    public String getSce(){
        return sce;
    }
    public void setSce(String sce){
        this.sce = sce;
    }
    
     public String getOverAll(){
        return overAll;
    }
    public void setOverAll(String overAll){
        this.overAll = overAll;
    }
    
    /* Added for RBU */
    public String getGeographyId() {
		return this.geographyId;
	}
	public void setGeographyId( String geoid) {
      if(geoid!=null)
      {  
        if(geoid.indexOf(";")>0)
        {
            String multipleGeoIds=geoid;
            String arr[]=null;
            arr=geoid.split(";");
            geoid=arr[0];
            setMultipleGeographyIds(multipleGeoIds);
            setMultipleGeo(true);
        }
      }
		this.geographyId = geoid;
	}
    
     public String getGeographyType() {
		return this.geographyType;
	}
	public void setGeographyType( String geotype) {
		this.geographyType =geotype;
	}
    
     public String getGeographyDesc() {
		return this.geographyDesc;
	}
	public void setGeographyDesc( String geodesc) {
      if(geodesc !=null)
      {  
        if(geodesc.indexOf(";")>0)
        {
            String multipleGeos=geodesc;           
            String arr[]=null;          
            arr=geodesc.split(";");
            geodesc=arr[0];
         //   System.out.println("First Array element"+geodesc);
            setMultipleGeographyDesc(multipleGeos);
            setMultipleGeo(true);
        }
      }
		this.geographyDesc = geodesc;
	}
    
    public void setMultipleGeographyDesc( String geodesc) {    
       if(geodesc !=null)
        {
            if(geodesc.indexOf(";")>0)
            {
                String arr[]=null;
                arr=geodesc.split(";");
                for(int i=0;i<arr.length;i++)
                {
                   // if(!mutipleGeos.contains(arr[i].toString()))
                        mutipleGeos.add(arr[i].toString());
                }
            }
        }        
		//this.mutipleGeos = mutipleGeos;
        ArrayList geoIds= new ArrayList();
        geoIds=this.multipleGeoIds;
        for(int i=0;i<geoIds.size();i++)
        {
            LabelValueBean labelValueBean;
            //tempLabelValueBean = (LabelValueBean)geoIds.get(i);     
            labelValueBean=new LabelValueBean((String)mutipleGeos.get(i),(String)geoIds.get(i));
            multipleGeoList.add(labelValueBean);            
        }              
	}
    
     public ArrayList getMultipleGeographyDesc() {
       return this.mutipleGeos;
	}
    
     public ArrayList getMultipleGeographyList() {
       return this.multipleGeoList;
	}
    
     public void setMultipleGeographyIds( String geoid) {
       if(geoid!=null)
       { 
        if(geoid.indexOf(";")>0)
        {
            String arr[]=null;
            arr=geoid.split(";");
            for(int i=0;i<arr.length;i++)
            {
                System.out.println("Inital Setting of Multi Geo DD");
                if(!multipleGeoIds.contains(arr[i].toString()))
                    multipleGeoIds.add(arr[i].toString());
            }
        }
       }
		this.multipleGeoIds = multipleGeoIds;
	}
    
      public ArrayList getMultipleGeographyIds() {
       return this.multipleGeoIds;
	}
    
    public boolean isMultipleGeo() {
		return isMultipleGeo;
	}
	public void setMultipleGeo(boolean flag) {
		this.isMultipleGeo = flag;
	}
    
     public HashMap getMultipleGeoMap( ArrayList geoIds, ArrayList geoDesc) {
        HashMap multipleGeoMap=new HashMap();  
        if(geoDesc!=null && geoIds!=null)
        {
           int arrSize=geoDesc.size();
           System.out.println("Sizeeeeee"+arrSize);
           if(geoDesc.size()== geoIds.size())
           {
               for(int j=0;j<arrSize;j++)
               {                    
                    System.out.println("Putting"+j);
                    multipleGeoMap.put((String)geoDesc.get(j),(String)geoIds.get(j));                
               }
           }
        }
		return multipleGeoMap;
	}
    
	public String getSalesOrgDesc() {
		return this.salesOrgDesc;
	}
	public void setSalesOrgDesc( String desc ) {
		this.salesOrgDesc = desc;
	}
	
	public String getSalesPositionId() {
		return this.salesPositionId;
	}
	public void setSalesPositionId(String id) {
		this.salesPositionId = id;
	}
    
    public String getParentGeographyId() {
		return this.parentGeographyId;
	}
	public void setParentGeographyId(String pid) {
		this.parentGeographyId = pid;
	}
    
      public String getBusinessUnit() {
		return this.businessUnit;
	}
	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
    }
    
    public String getRoleDesc() {
		return roleDesc;
	}
	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}
    
    public String getSalesPostionDesc() {
		return this.salesPostionDesc;
	}
	public void setSalesPostionDesc(String salesPostionDesc) {
		this.salesPostionDesc = salesPostionDesc;
	}
    
    public String getReportsToSalesPostion() {
		return this.reportsToSalesPosition;
	}
	public void setReportsToSalesPostion(String reportsToSalesPosition) {
		this.reportsToSalesPosition = reportsToSalesPosition;
	}
    // Start: Modified for TRT 3.6 enhancement - F 4.4 -(user view of employee grid)     
    
    public void setNtId(String NtId){
        this.NtId=NtId;
        }
    public String getNtId()
    {
        return this.NtId;
    }
    
    public void setState(String State){
        this.State=State;
    }
    public String getState(){
        return this.State;
    }
    
    public void setManagerEmail(String email)
    {
        this.managerEmail = email;
    }
    public String getManagerEmail()
    {
        return this.managerEmail;
    }
    public void setSource(String source)
    {
        this.source=source;
    }
    public String getSource()
    {
        return this.source;
    }
    
    public void setSourceOrg(String sourceOrg)
    {
        this.sourceOrg=sourceOrg;
    }
    public String getSourceOrg()
    {
        return this.sourceOrg;
    }
    
    // End: Modified for TRT 3.6 enhancement - F 4.4 -(user view of employee grid)
    
    /* Log: Added by Meenakshi.M.B on 14-May-2010
    *  Added for CSO enhancement*/
    
    public String getSalesPostionTypeCode(){
        return this.salesPositionTypeCode;
    }
    public void setSalesPositionTypeCode(String salesPositionTypeCode){
        this.salesPositionTypeCode=salesPositionTypeCode;
    }
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "       emplId: " + emplId + "\n" );
		sb.append( "    firstName: " + firstName + "\n" );
		sb.append( "     lastName: " + lastName + "\n" );
		sb.append( "preferredName: " + preferredName + "\n" );
		sb.append( "   middleName: " + middleName + "\n" );
		sb.append( "       areaCd: " + areaCd + "\n" );
		sb.append( "     areaDesc: " + areaDesc + "\n" );
		sb.append( "     regionCd: " + regionCd + "\n" );
		sb.append( "   regionDesc: " + regionDesc + "\n" );
		sb.append( "   districtId: " + districtId + "\n" );
		sb.append( " districtDesc: " + districtDesc + "\n" );
		sb.append( "  territoryId: " + territoryId + "\n" );
		sb.append( "         role: " + role + "\n" );
		sb.append( "     teamCode: " + teamCode + "\n" );
		sb.append( "  clusterCode: " + clusterCode + "\n" );
		
		return sb.toString();
	}
    // Start: Added for TRT Phase 2
    public void setGroupCD(String groupCD)
    {
        this.groupCD=groupCD;
    }
    public String getGroupCD()
    {
        return this.groupCD;
    }
    public void setHQManager(String hqManager)
    {
        this.hqManager=hqManager;
    }
    public String getHQManager()
    {
        System.out.println("HQMANAGER  "+hqManager);
        return this.hqManager;
    }
    //Added by Swati
    private String homeState="";  
    
    public String getHomeState(){
        return this.homeState;
    }
    public void setHomeState(String state){
        this.homeState = state;
    }
} 
