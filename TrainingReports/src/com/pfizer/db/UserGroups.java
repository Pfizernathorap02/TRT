package com.pfizer.db; 

import java.util.ArrayList;

public class UserGroups {
	public static final String FIELD_GROUPNAME	= "groupName";
	public static final String FIELD_BUSUNIT	= "busUnit";
	public static final String FIELD_SALESORG	= "salesOrg";
	public static final String FIELD_ROLE		= "role";
    public static final String FIELD_ID         = "groupId";
    public static final String FIELD_NAU         = "NAU";	
    public static final String FIELD_BUSUNIT_SELECTED = "selectedBU";
    public static final String FIELD_SALESORG_SELECTED = "selectedSalesorg";
    public static final String FIELD_ROLE_SELECTED = "selectedRole";
    public static final String FIELD_FBU_SELECTED = "selectedFBU";
    public static final String FIELD_HQU_SELECTED = "selectedHQU";
    
	private int groupId;
	private String groupName;
	private String busUnit;
	private String salesOrg;
	private String role;
    private String selectedBU;
    private String selectedSalesorg;
    private String selectedRole;
    private ArrayList rolesList = new ArrayList();
    private ArrayList salesOrgsList = new ArrayList();
    /*Added for SCE search screen*/
    private String selectedFBU;
    private String selectedHQU;
    
	
	public UserGroups() {}
    
     public ArrayList getRolesList() {
		return rolesList;
	}

	public void setRolesList(ArrayList rolesList) {
		this.rolesList = rolesList;
	}
    
    public ArrayList getSalesOrgsList() {
		return salesOrgsList;
	}

	public void setSalesOrgsList(ArrayList salesOrgsList) {
		this.salesOrgsList = salesOrgsList;
	}
    
    public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getBusUnit() {
		return busUnit;
	}

	public void setBusUnit(String busUnit) {
		this.busUnit = busUnit;
	}

	public String getSalesOrg() {
		return salesOrg;
	}

	public void setSalesOrg(String salesOrg) {
		this.salesOrg = salesOrg;
	}
    
    public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
    
    public String getSelectedBU() {
		return selectedBU;
	}

	public void setSelectedBU(String selectedBU) {
		this.selectedBU = selectedBU;
	}
    
     public String getSelectedSalesorg() {
		return selectedSalesorg;
	}

	public void setSelectedSalesorg(String selectedSalesorg) {
		this.selectedSalesorg = selectedSalesorg;
	}
    
     public String getSelectedRole() {
		return selectedRole;
	}

	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}
    
    /*Added for SCE screen*/
    public String getSelectedFBU() {
		return selectedFBU;
	}

	public void setSelectedFBU(String selectedFBU) {
		this.selectedFBU = selectedFBU;
	}
	/*End of code addition*/
        public String getSelectedHQU() {
		return selectedHQU;
	}

	public void setSelectedHQU(String selectedHQU) {
		this.selectedHQU = selectedHQU;
        System.out.println("selectedHQU in Bean is"+selectedHQU);
	}
	
} 
