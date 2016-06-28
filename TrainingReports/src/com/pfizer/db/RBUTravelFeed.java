package com.pfizer.db; 



import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class RBUTravelFeed {
	private String emplid = "-1";
    private String firstName;
    private String lastName;
    private String sex;
    private String email;
    private String future_role;
    private String master_flag;
    private String trainer_flag;
    private String manager_flag;
    private String tracks = "";
    private String gttracks ="";
//CURRENT_ROLE,CURRENT_SUPERVISOR,CURRENT_SUPERVISOR_EMAIL,CURRENT_AREA,CURRENT_REGION,CURRENT_CLUSTER,CURRENT_TEAM
    private String role;
	private String areaDesc;
	private String regionDesc;    
	private String teamCode;
	private String clusterCode;
    
    private String managername = "";
    private String supervisorEmail;
    private String operation = "";
    private String areaCd = "";
    private String clusterDesc = "";
    private String toviazIdentifier = "";
    private String state = "";
    private String mappedRole;
    private String managerSalesPositionId = "";
    private String managerLastName = "";
    private String managerFirstName = "";
    private String managerEmailAddress = "";
    
	public RBUTravelFeed() {}


    public String getMappedRole() {
		return mappedRole;
	}

	public void setMappedRole(String mappedRole) {
		this.mappedRole = mappedRole;
    }
    
     public String getManagerLastName() {
		return managerLastName;
	}

	public void setManagerLastName(String managerLastName) {
		this.managerLastName = managerLastName;
    }
    
    
    public String getManagerFirstName() {
		return managerFirstName;
	}

	public void setManagerFirstName(String managerFirstName) {
		this.managerFirstName = managerFirstName;
    }
    
    public String getManagerEmailAddress() {
		return managerEmailAddress;
	}

	public void setManagerEmailAddress(String managerEmailAddress) {
		this.managerEmailAddress = managerEmailAddress;
    }
    
    public String getManagerSalesPositionId() {
		return managerSalesPositionId;
	}

	public void setManagerSalesPositionId(String managerSalesPositionId) {
		this.managerSalesPositionId = managerSalesPositionId;
    }
    
    
    public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
    }
    
    public String getToviazIdentifier() {
		return toviazIdentifier;
	}

	public void setToviazIdentifier(String toviazIdentifier) {
		this.toviazIdentifier = toviazIdentifier;
    }
    
    public String getClusterDesc() {
		return clusterDesc;
	}

	public void setClusterDesc(String clusterDesc) {
		this.clusterDesc = clusterDesc;
    }
    public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
    }
    
    public String getAreaCd() {
		return areaCd;
	}

	public void setAreaCd(String areaCd) {
		this.areaCd = areaCd;
    }

	public String getEmplid() {
		return emplid;
	}

	public void setEmplid(String emplid) {
		this.emplid = emplid;
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
    public void setSex( String sex ) {
		this.sex = sex;
	}
	public String getSex() {
		return this.sex;
	}
    public void setEmail( String email ) {
		this.email = email;
	}
	public String getEmail() {
		return this.email;
	}
    public void setFutre_role( String future_role ) {
		this.future_role = future_role;
	}
	public String getFutre_role() {
		return this.future_role;
	}
    
    public void setMaster_flag( String master_flag ) {
		this.master_flag = master_flag;
	}
	public String getMaster_flag() {
		return this.master_flag;
	}
    
    public void setTrainer_flag( String trainer_flag ) {
		this.trainer_flag = trainer_flag;
	}
	public String getTrainer_flag() {
		return this.trainer_flag;
	}
    
    public void setManager_flag( String manager_flag ) {
		this.manager_flag = manager_flag;
	}
	public String getManager_flag() {
		return this.manager_flag;
	}
        
    public void setTracks( String tracks ) {
		this.tracks = tracks;
	}
	public String getTracks() {
		return this.tracks;
	}
    
    public void setGttracks( String gttracks ) {
		this.gttracks = gttracks;
	}
	public String getGttracks() {
		return this.gttracks;
	}
    
    public String getRole() {
		return this.role;
	}
	public void setRole(String role) {
		this.role = role;
	}
    
    public String getRegionDesc() {
		return this.regionDesc;
	}
	public void setRegionDesc( String desc ) {
		this.regionDesc = desc;
	}
    
    public String getClusterCode() {
		return clusterCode;
	}
	
	public void setClusterCode(String code) {
		this.clusterCode = code;
	}
    
    public String getTeamCode() {
		return teamCode;
	}
	public void setTeamCode(String code) {
		this.teamCode = code;
	}
    
    public String getAreaDesc() {
		return this.areaDesc;
	}
	public void setAreaDesc( String desc ) {
		this.areaDesc = desc;
	}
     public void setManagername( String name ) {
        this.managername = name;
    }
    public String getManagername( ) {
        return this.managername;
    }

    public void setSupervisorEmail( String supervisorEmail ) {
        this.supervisorEmail = supervisorEmail;
    }
    public String getSupervisorEmail( ) {
        return this.supervisorEmail;
    }
    public boolean equals(Object obj){
        if (obj == null) return false;
        if( !(obj instanceof RBUTravelFeed)){
            return false;
        }else{
            RBUTravelFeed data = (RBUTravelFeed) obj;
            return data.getEmplid().equals(this.emplid);
        }
        
    }
    
    public int hashCode() 
    {
      return Integer.parseInt(emplid);
    }
} 

