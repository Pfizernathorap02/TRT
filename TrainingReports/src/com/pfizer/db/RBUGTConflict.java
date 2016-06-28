package com.pfizer.db; 

import java.util.Date;

public class RBUGTConflict{
    String emplid;
    String firstname;
    String lastname;
    RBUClassData gclass;
    RBUClassData tclass;
    
    public String getEmplid(){
        return emplid;
    }
    public void setEmplid(String emplid){
        this.emplid = emplid;
    }
    
    public String getFirstname(){
        return firstname;
    }
    public void setFirstname(String firstname){
        this.firstname = firstname;
    }
    
    public String getLastname(){
        return lastname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }
    public RBUClassData getGclass(){
        return gclass;
    }
    public void setGclass(RBUClassData gclass){
        this.gclass = gclass;
    }
    
    public RBUClassData getTclass(){
        return tclass;
    }
    public void setTclass(RBUClassData tclass){
        this.tclass = tclass;
    }
} 
            