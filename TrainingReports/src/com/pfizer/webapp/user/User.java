package com.pfizer.webapp.user; 

import com.pfizer.db.Employee;
import com.pfizer.db.UserAccess;
import com.tgix.Utils.Util;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class User {
	public static final String USER_TYPE_SUPER_ADMIN = "SUPER ADMIN";
	public static final String USER_TYPE_ADMIN		 = "ADMIN";
	public static final String USER_TYPE_TSR		 = "TSR Admin";
     //Added for SCE Feedback form enhancement 
	public static final String USER_TYPE_FBU		 = "Feedback User";
    // Added for TRT Phase 2 enhancement - Requirement no. F3
    public static final String USER_TYPE_HQ		 = "HQ User";
    //public static final String USER_TYPE_FEEDBACK = "Feedback User"; 
    //End of addition
	
	private UserAccess userAccess;
	
	private boolean validUser = false;
	private UserTerritory ut = null;
	private Employee employee;
	private List products = new ArrayList();
	private boolean isSuperAdmin=false;
	private boolean isAdmin=false;
    
    /*Added for CSO requiremements */
    private String scoresVisible="";
    private String scoresFlag="N";
   
    //Adding new User Territory object 
    private UserTerritory uto=null;
    private Employee employeeold;
	
    private boolean isSpecialRole=false;
         
    //Added for SCE Feedback form enhancement 
    private boolean isFeedbackUser=false;
    private boolean isHQUser=false;
    private List groups=new ArrayList();    
    //End of addition 
    
	public User() {}
	
	public void setUserTerritory(UserTerritory ut) {
		this.ut = ut;
	}
	public boolean getValidUser() {
		return validUser;
	}
	public void setValidUser( boolean flag ) {
	 validUser = flag;
	}
    public String getGeoType() {
        return this.employee.getGeographyType();
    }
	public boolean isAdmin() {
		
        /* Adding feedback user to the condition for SCE Feedback enhancement */
		if ( userAccess != null) {
			if ( USER_TYPE_ADMIN.equals(userAccess.getUserType()) ||					
					USER_TYPE_SUPER_ADMIN.equals(userAccess.getUserType()) ||					
					USER_TYPE_FBU.equals(userAccess.getUserType())) {
				return true;
                //USER_TYPE_TSR.equals(userAccess.getUserType()) ||
			}
		}
		return false;
	}
	
	public boolean isTsrAdmin() {
		if ( userAccess != null) {
			/*if ( USER_TYPE_TSR.equals(userAccess.getUserType()) || 
					USER_TYPE_SUPER_ADMIN.equals(userAccess.getUserType()) ) {
				return true;
			}
            */
			if ( USER_TYPE_TSR.equals(userAccess.getUserType()) )
             {
				return true;
			}
            

		}
		return false;	
	}
	
	public boolean isSuperAdmin() {
		if ( userAccess != null) {
			if ( USER_TYPE_SUPER_ADMIN.equals(userAccess.getUserType()) ) {
				return true;
			}
		}
		return false;
	}
    // Start: Added for TRT Phase 2 Enhancement - HQ users Requirement no. F3
    public boolean isHQUser() {
		if ( userAccess != null) {
			if ( USER_TYPE_HQ.equals(userAccess.getUserType()) ) {
				return true;
			}
		}
		return false;
	}
    // Ends
	public boolean isExemptionRole() {
		
		if ( isAdmin() ) {
			return true;
		}
		if (employee == null) {
			return false;
		}
		if ("RM".equalsIgnoreCase(employee.getRole())
			|| "VP".equalsIgnoreCase(employee.getRole())
			|| "ARM".equalsIgnoreCase(employee.getRole())
			|| "SVP".equalsIgnoreCase(employee.getRole())
			|| "NSD".equalsIgnoreCase(employee.getRole())
			|| "DCO".equalsIgnoreCase(employee.getRole())
			|| "DAO".equalsIgnoreCase(employee.getRole())) {
			
			return true;
		}
		
		return false;
	}
	
	
	public String getId() 
	{
		if ( userAccess != null) 
		{
			return userAccess.getEmplid();
		}
		return employee.getEmplId();
	}
	
	public void setUserAcess( UserAccess ua ) {
		this.userAccess = ua;
	}
	
	public String getRole() {
		if ( userAccess != null) {
			return userAccess.getUserType();
		}
		return employee.getRole();
	}
	public String getName() {
		StringBuffer sb = new StringBuffer();
		if (!Util.isEmpty( employee.getPreferredName() ) ) {
			sb.append( employee.getPreferredName() );
		} else {
			sb.append( employee.getFirstName() );			
		}
		sb.append(" " + employee.getLastName());
		return  sb.toString();
	}
	
	public List getProducts() {
		return products;
	}
	public void setProducts( List prod ) {
		this.products = prod;
	}
	public UserTerritory getUserTerritory() {
		return ut;
	}

	
	//
	// Data delegated from employee object, employee not exposed to the outside.
	//
	
	/*public String getCluster() {
		return employee.getClusterCode();
	}*/
	public String getTeam() {
		return employee.getTeamCode();
	}
	public String getDisplayCluster() {
		return employee.getDisplayCluster();
	}
	
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
	
	public String getEmail() {
		return employee.getEmail();
	}
	public String getEmplid() {
		if (employee == null ) {
			return getId();
		}
		if (Util.isEmpty(employee.getEmplId())) {
			if ( userAccess != null && !Util.isEmpty(userAccess.getEmplid())) {
				return userAccess.getEmplid();
			}
			return getId();
		}
		return employee.getEmplId();
	}
    //neha begin
    public String getEmplIdForSpRole(){
       if (employee == null ) {
			return getId();
		}
        if(null!=employee.getEmplIdForSpRole()){
           return employee.getEmplIdForSpRole();
      }    
		else 
       return getId();
	}
    //neha end
	/*public String getAreaCd() {
		return employee.getAreaCd();	
	}
	public String getRegionCd() {
		return employee.getRegionCd();	
	} */
	public String getAreaCd() {
        if(employeeold !=null)
        {
            return employeeold.getAreaCd();
        }
        return "";
	}
	public String getRegionCd() {
        if(employeeold !=null)
        {
            return employeeold.getRegionCd();
        }
        return "";	
	}
    public String getCluster() {
        //System.out.println("Gettting cluster"+employeeold.getClusterCode());
		if(employeeold !=null)
        {
            return employeeold.getClusterCode();
        }
        return "";
        //return employee.getClusterCode();
	}    
	
    /* Added for RBU */
      public String getGeographyId()
    {
        System.out.print("GeographyId :"+employee.getGeographyId());
        return employee.getGeographyId();
    }
    
     public String getBusinessUnit() {
		return employee.getBusinessUnit();
	}
    
      public String getSalesOrganization() {
		return employee.getSalesOrgDesc();
	}
    
     public String getSalesPositionId() {
		return employee.getSalesPositionId();
	}
    
     public String getSalesPostionDesc() {
		return employee.getSalesPostionDesc();
	}
    
     public boolean isMultipleGeos()
    {
        if(employee.isMultipleGeo()==true)
        {
            return true;
        }
        return false;
    }
    
     public ArrayList getMultipleGeos() {
		return employee.getMultipleGeographyDesc();
	}
    
    public ArrayList getMultipleGeoIds() {
		return employee.getMultipleGeographyIds();
	}
    
    public String getGeographyDesc() {
		return employee.getGeographyDesc();
	}
    
    public HashMap getMultipleGeoMap()
    {
        HashMap geoMap=new HashMap();
        geoMap= employee.getMultipleGeoMap(employee.getMultipleGeographyIds(),employee.getMultipleGeographyDesc());        
       // System.out.println("Printing-------"+geoMap);
        return geoMap;
    }
    
    public ArrayList getMultipleGeoList()
    {
        return employee.getMultipleGeographyList();
    }
	
    public boolean isSpecialRole() {
		return isSpecialRole;
	}
	public void setIsSpecialRole( boolean flag ) {
	 isSpecialRole = flag;
	}
    
    public String getReportsToEmplid()
    {
        return employee.getReportsToEmplid();
    }
	
	//
	// End employee object delegation
	//
	
	/*Added for retaining Special events */
    public void setUserTerritoryOld(UserTerritory uto) {
		this.uto = uto;
	}
	
	public UserTerritory getUserTerritoryOld() {
		return uto;
	}
	
	public void setOldEmployee(Employee employeeold) {
		this.employeeold = employeeold;
	}
	
	public String getRoleDesc()
    {
        return employee.getRoleDesc();
    }
        
    public String getReportsToSalesPostion()
    {
        return employee.getReportsToSalesPostion();
    }
    
    public String getOldRole()
    {
		if ( userAccess != null) {
			return userAccess.getUserType();
		}
        if(employeeold!=null)
        {
            return employeeold.getRole();
        }
        return "";
	}
    
     // Added for SCE Feedback form enhancement 
    
        public boolean isFeedbackUser()
        {
            if(userAccess !=null)
            {
                if(USER_TYPE_FBU.equalsIgnoreCase(userAccess.getUserType()))
                {
                    return true;
                }            
            }
            return false;
        }   
        public List getGroups() 
        {
            return groups;
        }
        public void setGroups( List groups ) 
        {
            this.groups = groups;
        }
    
    // End of addition
    
    /* Added for CSO enhancement */
        public String getSalesPositionTypeCd()
        {
            return employee.getSalesPostionTypeCode();
        }
	/*End of addition */
    /* Log: Added by Meenakshi.M.B on 14-May-2010
    *  Added for CSO enhancement*/
    public String getScoresVisible(){
        return this.scoresVisible;
    }
    public void setScoresVisible(String scoresVisible) {
        this.scoresVisible = scoresVisible;
    }
     public void setScoresFlag(String scoresFlag){
        this.scoresFlag=scoresFlag;
    }    
    public String getScoresFlag(){
        return this.scoresFlag;
    }
    
    /* End of addition */
} 
