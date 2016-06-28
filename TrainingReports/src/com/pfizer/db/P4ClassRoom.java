package com.pfizer.db; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class P4ClassRoom 
{ 
    public final static String MONDAY = "Monday";
    public final static String TUESDAY = "Tuesday";
    public final static String WENSEDAY = "Wenseday";
    public final static String THURSDAY = "Thursday";
    public final static String FRIDAY = "Friday";

    private String room_id;
    private String room_name;
    private String weekday;
    private Date day;
    private P4ClassData p4class = null;
    private P4ClassData p4class2 = null;
    private List p4classes = new ArrayList();
   // private List tables = new ArrayList();
    //TO-DO should use tables.size - shannon
    private int assignedtalbes = 0;
    
    public void setP4Classes (List r){
        this.p4classes = r;
    }
    
    public List getP4Classes (){
        return p4classes;
    }
    
    
    public void setRoom_id( String room_id ) {
        this.room_id = room_id;
    }
    public String getRoom_id( ) {
        return this.room_id;
    }
    
    public void setAssignedtalbes( int assignedtalbes ) {
        this.assignedtalbes = assignedtalbes;
    }
    public int getAssignedtalbes( ) {
        return this.assignedtalbes;
    }
    
    public void setRoom_name( String room_name ) {
        this.room_name = room_name;
    }
    
    public String getRoom_name( ) {
        return this.room_name;
    }
    
    public void setWeekday(String weekday ) {
        this.weekday = weekday;
    }
    public String getWeekday( ) {
        return this.weekday;
    }
    public void setDay(Date day ) {
        this.day = day;
    }
    public Date getDay( ) {
        return this.day;
    }
    public void setP4Class(P4ClassData rbuclass ) {
        this.p4class = rbuclass;
    }
    public P4ClassData getP4Class( ) {
        return this.p4class;
    }
    
        public void setP4Class2(P4ClassData rbuclass2 ) {
        this.p4class2 = rbuclass2;
    }
    public P4ClassData getP4Class2( ) {
        return this.p4class2;
    }
   /* public void setTables( List tables ) {
        this.tables = tables;
    }
   public List getTables( ) {
        return this.tables;
   }
   */
   
   
    
    
} 
