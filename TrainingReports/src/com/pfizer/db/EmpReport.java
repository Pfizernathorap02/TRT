package com.pfizer.db; 

import java.util.Date;

public class EmpReport extends Employee
{ 
    private String productCode;
    private String productDesc;
    private Integer courseId;
    private String courseDesc;
    private String clusterDesc;
    private Date startDate;
    private Date endDate;
    private Date trainingDate;
    private String teamDesc = "";
    private String emplStatus="";
    private String  fieldActive="";
    private String shipAdd1;
    private String shipAdd2;
    private String shipCity;
    private String shipState;
    private String shipZip;
    private String attendanceStatus;
            
    /* This is the Count By Product, Start Date, End Date, Team */
    private Integer count;
    private Integer manager_count;
    
    private Integer totalCount;
    private Integer TRMShipmentCount;
    private Integer EmailInvitationCount;
    private Integer P2LregistrationCount;
    private Date TRMShipmentDate;
    private Date EmailInvitationDate;
    private Date P2LregistrationDate;
    
    //added by shannon for RBU
    private int guestCount;
    private String futureBU;
     private String futureRBU;
    
    
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public String getProductDesc() {
        return productDesc;
    }
    
    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }
    
    public Integer getCourseId() {
        return courseId;
    }
    
    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseDesc() {
        return courseDesc;
    }
    
    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }
    
    public String getClusterDesc() {
        return clusterDesc;
    }
    
    public void setClusterDesc(String clusterDesc) {
        this.clusterDesc = clusterDesc;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public Date getTrainingDate() {
        return trainingDate;
    }
    
    public void setTrainingDate(Date trainingDate) {
        this.trainingDate = trainingDate;
    }
    
    public String getTeamDesc() {
        return teamDesc;
    }
    
    public void setTeamDesc(String teamDesc) {
        this.teamDesc = teamDesc;
    }
    
    public String getEmplStatus() {
			return this.emplStatus;
	}
	
    public void setEmplStatus(String emplStatus) {
			this.emplStatus = emplStatus;
	}
    
    public String getFieldActive() {
			return this.fieldActive;
	}
    
	public void setFieldActive(String fieldActive) {
			this.fieldActive = fieldActive;
	}
    
    public String getShipAdd1() {
		return shipAdd1;
	}
    
	public void setShipAdd1(String shipAdd1) {
		this.shipAdd1 = shipAdd1;
	}

    public String getShipAdd2() {
		return shipAdd2;
	}
    
	public void setShipAdd2(String shipAdd2) {
		this.shipAdd2 = shipAdd2;
    }
    
    public String getShipCity() {
		return shipCity;
	}
    
	public void setShipCity(String shipCity) {
		this.shipCity = shipCity;
	}
    
    public String getShipState() {
		return shipState;
	}
    
	public void setShipState(String shipState) {
		this.shipState = shipState;
	}
    
	public String getShipZip() {
		return shipZip;
	}
    
	public void setShipZip(String shipZip) {
		this.shipZip = shipZip;
	}
    
    public String getAttendanceStatus() {
        return attendanceStatus;
    }
    
    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
    }
    
    public Integer getManager_count() {
        return manager_count;
    }
    
    public void setManager_count(Integer manager_count) {
        this.manager_count = manager_count;
    }

    public Integer getTotalCount() {
        return totalCount;
    }
    
    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    	public Integer getTRMShipmentCount() {
		return TRMShipmentCount;
	}
	public void setTRMShipmentCount(Integer shipmentCount) {
		TRMShipmentCount = shipmentCount;
	}
	public Integer getEmailInvitationCount() {
		return EmailInvitationCount;
	}
	public void setEmailInvitationCount(Integer emailInvitationCount) {
		EmailInvitationCount = emailInvitationCount;
	}
	public Integer getP2LregistrationCount() {
		return P2LregistrationCount;
	}
	public void setP2LregistrationCount(Integer lregistrationCount) {
		P2LregistrationCount = lregistrationCount;
	}
    	public Date getTRMShipmentDate() {
		return TRMShipmentDate;
	}
	public void setTRMShipmentDate(Date shipmentDate) {
		TRMShipmentDate = shipmentDate;
	}
	public Date getEmailInvitationDate() {
		return EmailInvitationDate;
	}
	public void setEmailInvitationDate(Date emailInvitationDate) {
		EmailInvitationDate = emailInvitationDate;
	}
	public Date getP2LregistrationDate() {
		return P2LregistrationDate;
	}
	public void setP2LregistrationDate(Date lregistrationDate) {
		P2LregistrationDate = lregistrationDate;
	}
    
    public int getGuestCount() {
		return guestCount;
	}
	public void setGuestCount(int guestCount) {
		this.guestCount = guestCount;
	}
    
        
    public String getFutureBU() {
		return futureBU;
	}
	public void setFutureBU(String futureBU) {
		this.futureBU = futureBU;
	}
    
    public String getFutureRBU(){
        return this.futureRBU;
    }

    public void setFutureRBU (String param){
        this.futureRBU = param;
    }
    
    public String getFullAddress() {
        StringBuffer sbr=new StringBuffer();
        sbr.append(this.shipAdd1);
        if (this.shipAdd2 != null && !this.shipAdd2.trim().equals(""))
            sbr.append(", " + this.shipAdd2);
        sbr.append(", " + this.shipCity);
        sbr.append(", " + this.shipState);
        sbr.append(" " + this.shipZip);
        return sbr.toString();
    }
} 
