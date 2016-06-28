package com.tgix.printing; 

public class TrainingWeeks 
{ 
    
    private String week_id;
    private String week_name;
    private String start_date;
    private String end_date;
    
    
   public void setWeek_id( String week_id ) {
        this.week_id = week_id;
    }
   public String getWeek_id( ) {
        return this.week_id;
   }
   
    public void setWeek_name( String week_name ) {
        this.week_name = week_name;
    }
   public String getWeek_name( ) {
        return this.week_name;
   }
   
   
   public void setStart_date( String start_date ) {
        this.start_date= start_date;
    }
   public String getStart_date( ) {
        return this.start_date;
   }
   
   public void setEnd_date( String end_date ) {
        this.end_date = end_date;
    }
   public String getEnd_date( ) {
        return this.end_date;
   }
} 
