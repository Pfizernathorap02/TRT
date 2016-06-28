package com.tgix.Utils; 

import com.tgix.printing.LoggerHelper;
import com.tgix.printing.PrintingConstants;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class PrintUtils 
{ 
    
    public static String[] convertToStringArray(Object[] arguments)
    {
        List lst = Arrays.asList(arguments);        
        String[] argumentsStrArr  =null;
        argumentsStrArr = new String[lst.size()];
        argumentsStrArr = (String[])lst.toArray(argumentsStrArr);
        return argumentsStrArr;
    }

    public static Object[] convertToObjectArray(String arguments)
    {
        StringTokenizer st = new StringTokenizer(arguments,PrintingConstants.EMAIL_DELIMITER);
        Object[] objArr = null;
        if(st!=null)
        {
            objArr = new Object[st.countTokens()];
            int i=0;
            while(st.hasMoreElements())
            {
                objArr[i] = st.nextElement();
                i++;
            }
            if(objArr!=null && objArr.length > 0)
                LoggerHelper.logSystemDebug(" PrintUtils  convertToObjectArray "+Arrays.asList(objArr));
        }

        return objArr;
    }
    
    /**
    *
    * Preprocess a string literal for inclusion in a dynamic SQL/DQL query by replacing all
    * single quote characters (') with two adjacent single quote characters ('').
    */
     public static String makeSafeQLString ( String a_str )
   {
      return PrintUtils.replace(a_str, "'", "''");
   }
   
    public static final String replace(String srcStr, String oldString, String newString)
    {
        if (srcStr == null) {
            return null;
        }
        int i=0;
        if ( ( i=srcStr.indexOf( oldString, i ) ) >= 0 ) {
            char [] srcStr2 = srcStr.toCharArray();
            char [] newString2 = newString.toCharArray();
            int oLength = oldString.length();
            StringBuffer buf = new StringBuffer(srcStr2.length);
            buf.append(srcStr2, 0, i).append(newString2);
            i += oLength;
            int j = i;
            while( ( i=srcStr.indexOf( oldString, i ) ) > 0 ) {
            buf.append(srcStr2, j, i-j).append(newString2);
            i += oLength;
            j = i;
            }
            buf.append(srcStr2, j, srcStr2.length - j);
            return buf.toString().trim();
        }
    return srcStr.trim();
    }  
            
    public static String[] convertToStringArray(String arguments)
    {
        StringTokenizer st = new StringTokenizer(arguments,PrintingConstants.EMAIL_DELIMITER);
        String[] objArr = null;
        if(st!=null)
        {
            objArr = new String[st.countTokens()];
            int i=0;
            while(st.hasMoreElements())
            {
                objArr[i] = (String)st.nextElement();
                i++;
            }
            //if(objArr!=null && objArr.length > 0)
              //  LoggerHelper.logSystemDebug(" PrintUtils  convertToStringArray from String "+Arrays.asList(objArr));
        }

        return objArr;
    }
    
    
     public static String[] convertToStringArrayWithProdMap(String arguments,Map prodMap)
    {
        StringTokenizer st = new StringTokenizer(arguments,PrintingConstants.EMAIL_DELIMITER);
        String[] objArr = null;
        HashMap thisProdMap=(HashMap)prodMap;
        if(st!=null){
            objArr = new String[st.countTokens()];
            int i=0;
            String prodDesc;
            String prodCD="";
            while(st.hasMoreElements()){   
                prodCD=st.nextElement().toString();
                prodDesc=(String)thisProdMap.get(prodCD.trim().toUpperCase());
                objArr[i] = prodDesc;
                i++;
            }
        }
        return objArr;
    }
    
    
    
    
      public static String formatStringsForMailMessage(String[] idArray)
      {
        if ((idArray == null) || (idArray.length == 0))
        {
          return "";
        }
        
        StringBuffer buffer = new StringBuffer(32);
        buffer.append(idArray[0]);
        for (int i = 1; i < idArray.length; i++)
        {
          if (idArray[i] == null)
          {
            continue;
          }
          buffer.append(",").append(idArray[i]);
        }
        //LoggerHelper.logSystemDebug(" formatStringsForMailMessage "+buffer.toString() );
        return buffer.toString();
      }      
      
  public static String ifNull(Object o, String ifNullStr)
  {
    if (o != null)
    {
      return o.toString();
    }
    return ifNullStr;
  }
  
  public static String ifNull(Object o)
  {
    return ifNull(o, "");
  }      
  
  
   public static String ifNullNBSP(Object o)
  {
   return ifNull(o, "&nbsp;");
  }      
  
  public static boolean isFieldNotNullAndComplete(String field)
  {
    return ((field!=null && !field.trim().equals("")) ? true : false);
  }
  
 
  
   public static String formatStringsForInClause(String[] idArray)
     {
       if ((idArray == null) || (idArray.length == 0))
       {
         return "";
       }
          StringBuffer buffer = new StringBuffer("'");
       buffer.append(idArray[0]);
       for (int i = 1; i < idArray.length; i++)
       {
         if (idArray[i] == null)
         {
           continue;
         }
         buffer.append("','").append(idArray[i].trim());
       }
       buffer.append("'");
         return buffer.toString();
     }         
     
  public static DateFormat getNormalDateFormat()
  {
    return new SimpleDateFormat(PrintingConstants.DEFAULT_DATE_FORMAT);
  }     
  
   public static String formatDateForDisplay(Date date)
  {
    String formattedDateStr = "";
        try
        {
            if(date!=null)
            formattedDateStr = new SimpleDateFormat(PrintingConstants.DEFAULT_DATE_FORMAT).format(date);
        }catch(Exception e) {LoggerHelper.logSystemError("Exception in formatting Date for display ",e);}
    return formattedDateStr;
  }     
  
   public static String formatDateForDisplayNBSP(Date date){
    
    
    String formattedDateStr = "&nbsp;";
        try
        {
            if(date!=null)
            formattedDateStr = new SimpleDateFormat(PrintingConstants.DEFAULT_DATE_FORMAT).format(date);
        }catch(Exception e) {LoggerHelper.logSystemError("Exception in formatting Date for display ",e);}
    return formattedDateStr;
   }
  
  
  public static DateFormat getDateFormat()
  {
    return new SimpleDateFormat(PrintingConstants.EXTENDED_DATE_FORMAT);
  }
  
            
  
    
  
  public static String truncate(String a_str, int noOfChars)
   {
    if(a_str!=null && a_str.length() < 30)
        return a_str;
    if(a_str == null) return null;
    
        String truncatedStr = null;
        try
        {
            truncatedStr = a_str.substring(0,noOfChars);
        }catch(Exception e) {LoggerHelper.logSystemError("Exceptio in truncating string "+a_str,e);} 
    
    return truncatedStr;
    }
    
    
    public static Date getDate(String effDateStr)        
    {
        Date effDate = null;
        try
        {  
            if(effDateStr!=null)
            effDate = getNormalDateFormat().parse(effDateStr);
            
        }catch(Exception e) {LoggerHelper.logSystemError("EligibilityUtils Date Format exception "+effDateStr,e);}        
        return effDate;
    }
    
    public  static String getCurrentDate(){
        Calendar cal = new GregorianCalendar();
        Date newDate;
        // Get the components of the date
        int year = cal.get(Calendar.YEAR);             // 2002
        int month = cal.get(Calendar.MONTH);           // 0=Jan, 1=Feb, ...
        int day = cal.get(Calendar.DAY_OF_MONTH);      // 1...
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=Sunday, 2=Monday, ...
       // start date on given date. public Date(int year,int month,int date)
        newDate=new Date(year-1900,month,day);
        return new SimpleDateFormat(PrintingConstants.INVITATION_DATE_FORMAT).format(newDate);
    }
    
    public static Date addDays(Date dateOriginal, int iDaysToAdd)
    {
      int MILLIS_IN_DAY = 1000 * 60 * 60 * 24 * iDaysToAdd;        
      
      if(dateOriginal == null)
        return null;
              
      long lnewMilliSeconds = dateOriginal.getTime() + MILLIS_IN_DAY;
      Date newDate = new Date(lnewMilliSeconds);
      return newDate;        
    }
    
    
    
    public static String getPedCloseDateFormatted(Date date){
        /**
         * This Method Return The Date Formated for Use In Invitation Letter
         */
        return new SimpleDateFormat(PrintingConstants.INVITATION_DATE_FORMAT).format(date);
    }
    
    
    public static String getCurrentDateinDefaultFormat(){
        
           Calendar cal = new GregorianCalendar();
        Date newDate;
        // Get the components of the date
        int year = cal.get(Calendar.YEAR);             // 2002
        int month = cal.get(Calendar.MONTH);           // 0=Jan, 1=Feb, ...
        int day = cal.get(Calendar.DAY_OF_MONTH);      // 1...
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1=Sunday, 2=Monday, ...
       // start date on given date. public Date(int year,int month,int date)
        newDate=new Date(year-1900,month,day);
        return new SimpleDateFormat(PrintingConstants.DEFAULT_DATE_FORMAT).format(newDate);
    }
    
   public  static List convertMapValuesToList(Map map) {
   List list = new ArrayList();
   for(Iterator tempIter=map.keySet().iterator();tempIter.hasNext();)
   list.add(map.get(tempIter.next()));
   return list;
   }
   
   
   
   
   
    
  
} 
