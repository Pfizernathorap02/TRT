package com.pfizer.db; 

import java.util.List;

public class RBURoomGridVO 
{  
    private String room_name;
    private String room_id;
    private String week_id;
    private List roomdata;
   
    public void setRoom_id( String room_id ) {
        this.room_id = room_id;
    }
    public String getRoom_id( ) {
        return this.room_id;
    } 
    public void setRoom_name(String room_name ) {
        this.room_name = room_name;
    }
    public String getRoom_name( ) {
        return this.room_name;
    } 
    public void setWeek_id( String week_id ) {
        this.week_id = week_id;
    }
    public String getWeek_id( ) {
        return this.week_id;
    }
     
    public void setRoomdata( List roomdata ) {
        this.roomdata = roomdata;
    }
    public List getRoomdata( ) {
        return this.roomdata;
    }   
} 
