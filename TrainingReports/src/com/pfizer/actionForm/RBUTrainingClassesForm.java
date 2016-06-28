package com.pfizer.actionForm;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class RBUTrainingClassesForm extends ActionSupport{
    String emplid;        
    List classes;
    List futureProds;

    public String getEmplid() {
        return emplid;
    }
    public void setEmplid(String emplid) {
        this.emplid = emplid;
    }   

    public List getClasses() {
        return classes;
    }
    public void setClasses(List classes) {
        this.classes = classes;
    }   
    
    public List getFutureProds() {
        return futureProds;
    }
    public void setFutureProds(List futureProds) {
        this.futureProds = futureProds;
    }  

}
