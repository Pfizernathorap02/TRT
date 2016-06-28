package com.pfizer.utils;

import java.util.Comparator; 
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.*;
import com.pfizer.db.P2lActivityStatus;

public class StatusDateComparator implements Comparator
{ 
    public int compare(Object obj1, Object obj2) throws ClassCastException
    {
        int i=0;
        try
        {
            i = 0;            
            LatestActivityStatus activityStatus1 = (LatestActivityStatus) obj1;
            LatestActivityStatus activityStatus2 = (LatestActivityStatus) obj2;
            
            java.sql.Date statusDate1 = activityStatus1.getStatusDate();
            java.sql.Date statusDate2 =  activityStatus2.getStatusDate();                        
            if(statusDate1 !=null && statusDate2!=null){
                if(statusDate1.after(statusDate2)) i=-1;
                else if(statusDate1.before(statusDate2)) i=1;
                else if(statusDate1.equals(statusDate2)) i=0;
            }
  
        }
        // End: Modified for TRT 3.6 enhancement - F 3 -(sort actitvities based on completion status)        
        catch(ClassCastException e)
        {
            e.printStackTrace();
        }
         catch(Exception e)
        {
            e.printStackTrace();
        }
        return i;
    }
    

} 
