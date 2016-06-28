package com.pfizer.db; 

import java.util.Date;
import java.util.List;

public class P4TrainingWeek 
{ 
    private String week_id;
    private String wave_id;
    private String week_name;
    private Date start_date;
    private Date end_date;
    
    
   public void setWeek_id( String week_id ) {
        this.week_id = week_id;
    }
   public String getWeek_id( ) {
        return this.week_id;
   }
   public void setWave_id( String wave_id ) {
        this.wave_id = wave_id;
    }
   public String getWave_id() {
        return this.wave_id;
   }
   
    public void setWeek_name( String week_name ) {
        this.week_name = week_name;
    }
   public String getWeek_name( ) {
        return this.week_name;
   }
   
   
   public void setStart_date( Date start_date ) {
        this.start_date= start_date;
    }
   public Date getStart_date( ) {
        return this.start_date;
   }
   
   public void setEnd_date( Date end_date ) {
        this.end_date = end_date;
    }
   public Date getEnd_date( ) {
        return this.end_date;
   }
} 
