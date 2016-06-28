package com.pfizer.utils;

import java.util.Comparator; 
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import com.pfizer.db.P2lActivityStatus;

public class DateComparator implements Comparator
{ 
    public int compare(Object obj1, Object obj2) throws ClassCastException
    {
        Date endDate1=null;
        Date endDate2=null;
        
        
        int i=0;
        try
        {
            
            DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
            
             i = 0;
            
            String eDate1 = ((P2lActivityStatus)obj1).getEndDate();
            String eDate2 = ((P2lActivityStatus)obj2).getEndDate();
            
            // Start: Modified for TRT 3.6 enhancement - F 3 -(sort actitvities based on completion status) 
            if(eDate1 !=null &&  eDate2 !=null &&
                 (eDate1.trim().length() != 0 && eDate2.trim().length() !=0)
            )
            {
                
                       
                endDate1= df.parse(eDate1);
                endDate2= df.parse(eDate2);
                
                System.out.println("obj1"+((P2lActivityStatus)obj1).getActivityName()); 
                System.out.println("End Date1"+endDate1); 
                System.out.println("obj2"+((P2lActivityStatus)obj2).getActivityName()); 
                System.out.println("End Date2"+endDate2);  
                
                
            if(endDate1.after(endDate2))
            {
                i = 1;
            }
            else if (endDate1.before(endDate2))
            {
                i = -1;
            }
            }
            else {
                if((eDate1 == null || eDate1.trim().length() == 0) && 
                    eDate2 != null && eDate2.trim().length() != 0)
                {
                    i = -1;
                }
                else if ((eDate1 !=null || eDate1.trim().length() != 0))
                {
                    i = 1;
                }   
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
