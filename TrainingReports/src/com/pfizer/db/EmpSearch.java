package com.pfizer.db; 

import java.util.Date;

public class EmpSearch { 
	private String emplId = "";
	private String lastName = "";
	private String middleName = "";
	private String firstName = "";
	private String preferredName = "";
	private String productCd = "";
    private String roleCd="";
    private String emplStatus="";
    private String exam_issued_on="";
    private String territoryId="";	
	private String  fieldActive="";
    private String  materialOrderDate="";
    private String  trainingNeed="";
	
    /* Adding code for RBU changes */
    private String salesPosId="";
    private String email="";
    private String busUnit="";
    private String salesOrg="";
    
	
    public String getMaterialOrderDate() {
	    return this.materialOrderDate;
	}
	public void setMaterialOrderDate(String materialOrderDate) {
			this.materialOrderDate = materialOrderDate;
	}
    
    public String getFieldActive() {
			return this.fieldActive;
	}
	public void setFieldActive(String fieldActive) {
			this.fieldActive = fieldActive;
	}
	public EmpSearch() {}
	
	public String getEmplId() {
		return this.emplId;
	}	
	public void setEmplId( String id ) {
		this.emplId = id;
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
	
	public void setProductCd( String prod ) {
		this.productCd = prod;
	}
	public String getProductCd () {
		return this.productCd;
	}
		
	public String getEmplStatus() {
			return this.emplStatus;
	}
	
    public void setEmplStatus(String emplStatus) {
			this.emplStatus = emplStatus;
	}
	
    public String getExam_issued_on() {
			return this.exam_issued_on;
	}
	
    public void setExam_issued_on(String exam_issued_on) {
			this.exam_issued_on = exam_issued_on;
	}
	
    public String getRoleCd() {
			return this.roleCd;
	}
	
    public void setRoleCd(String roleCd) {
			this.roleCd = roleCd;
	}
	
    public String getTerritoryId() {
			return this.territoryId;
	}
	
    public void setTerritoryId(String territoryId) {
			this.territoryId = territoryId;
	}
    
    public void setTrainingNeed	(String trainingNeed){
        this.trainingNeed=trainingNeed;
    }
    
    public String getTrainingNeed(){
        return this.trainingNeed;
    }
    
      /*Adding for RBU changes */
      public void setSalesPosId(String salesPosId){
        this.salesPosId=salesPosId;
    }
    
    public String getSalesPosId(){
        return this.salesPosId;
    }
      public void setEmail(String email){
        this.email=email;
    }
    
    public String getEmail(){
        return this.email;
    }
      public void setBusUnit(String busUnit){
        this.busUnit=busUnit;
    }
    
    public String getBusUnit(){
        return this.busUnit;
    }
      public void setSalesOrg(String salesOrg){
        this.salesOrg=salesOrg;
    }
    
    public String getSalesOrg(){
        return this.salesOrg;
    }
	 
    
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append( "\n" + this.getClass().getName() + "\n" );
		sb.append( "       emplId: " + emplId + "\n" );
		sb.append( "    firstName: " + firstName + "\n" );
		sb.append( "     lastName: " + lastName + "\n" );
		sb.append( "preferredName: " + preferredName + "\n" );
		sb.append( "   middleName: " + middleName + "\n" );
		
		return sb.toString();
	}
    
    
} 
