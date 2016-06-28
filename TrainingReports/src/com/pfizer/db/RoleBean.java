package com.pfizer.db; 

public class RoleBean 
{ 
    
    private String roleCd;
    private String roleDesc;
    
    public void setRoleCd(String roleCd){
        this.roleCd=roleCd;
    }
    
    public String getRoleCd(){
        return this.roleCd;
    }
    
    public void setRoleDesc(String roleDesc){
        this.roleDesc=roleDesc;
    }
    
    public String getRoleDesc(){
        return this.roleDesc;
    }
} 
