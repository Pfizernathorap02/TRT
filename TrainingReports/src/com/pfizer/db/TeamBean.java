package com.pfizer.db; 

public class TeamBean 
{ 
    
    private String teamCd;
    private String teamDesc;
    
    public void setTeamCd(String teamCd){
        this.teamCd=teamCd;
    }
    public String getTeamCd(){
        return this.teamCd;
    }
    
    public void setTeamDesc(String teamDesc){
        this.teamDesc=teamDesc;
    }
    
    public String getTeamDesc(){
        return this.teamDesc;
    }
    
} 
