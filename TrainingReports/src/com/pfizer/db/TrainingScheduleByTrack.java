package com.pfizer.db; 

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrainingScheduleByTrack 
{ 
    private int track_id;
    private String productCode;
    private String productDesc;
    private String class_id;
    
    private String track_desc;
    private Date start_date;
    private Date end_date;
    private Map rolegroupcount;
    private int traineecount;    
    
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
    
    public String getClass_id() {
        return class_id;
    }
    
    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }
    public int getTrack_id(){
        return track_id;
    }    
    
    public void setTrack_id(int track_id){
        this.track_id = track_id;
    }
    
    public String getTrack_desc(){
        return track_desc;
    }    
    
    public void setTrack_desc(String track_desc){
        this.track_desc = track_desc;
    }
    
    public int getTraineecount(){
        return traineecount;
    }    
    
    public void setTraineecount(int traineecount){
        this.traineecount = traineecount;
    }
    
    public Date getStart_date(){
        return start_date;
    }    
    
    public void setStart_date(Date start_date){
        this.start_date = start_date;
    }
    
    public Date getEnd_date(){
        return end_date;
    }    
    
    public void setEnd_date(Date end_date){
        this.end_date = end_date;
    }
    
    public Map getRolegroupcount(){
        return new HashMap(rolegroupcount);
    }    
    
    public void setRolegroupcount(Map rolegroupcount){
        this.rolegroupcount = new HashMap(rolegroupcount);
    }
    
} 
