package com.pfizer.db; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RBUClassRoom 
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
    private RBUClassData rbuclass = null;
    private RBUClassData rbuclass2 = null;
    private List rbuclasses = new ArrayList();
   // private List tables = new ArrayList();
    //TO-DO should use tables.size - shannon
    private int assignedtalbes = 0;
    
    public void setRbuClasses (List r){
        this.rbuclasses = r;
    }
    
    public List getRbuClasses (){
        return rbuclasses;
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
    public void setRBUClass(RBUClassData rbuclass ) {
        this.rbuclass = rbuclass;
    }
    public RBUClassData getRBUClass( ) {
        return this.rbuclass;
    }
    
        public void setRBUClass2(RBUClassData rbuclass2 ) {
        this.rbuclass2 = rbuclass2;
    }
    public RBUClassData getRBUClass2( ) {
        return this.rbuclass2;
    }
   /* public void setTables( List tables ) {
        this.tables = tables;
    }
   public List getTables( ) {
        return this.tables;
   }
   */
   
   
    
    
} 
