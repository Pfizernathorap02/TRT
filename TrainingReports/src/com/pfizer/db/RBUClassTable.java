package com.pfizer.db; 

import java.util.List;

public class RBUClassTable 
{ 
    private String talbe_id;
    private int trainees_cnt;
    private int guests_cnt;
    
   public void setTalbe_id( String talbe_id ) {
        this.talbe_id = talbe_id;
    }
   public String getTalbe_id( ) {
        return this.talbe_id;
   }
   
   public void setTraineesCnt( int trainees_cnt ) {
        this.trainees_cnt = trainees_cnt;
    }
   public int getTraineesCnt( ) {
        return this.trainees_cnt;
   }
   
   public void setGuestCnt( int guests_cnt ) {
        this.guests_cnt = guests_cnt;
    }
   public int getGuestCnt( ) {
        return this.guests_cnt;
   }
} 
