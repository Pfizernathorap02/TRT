package com.tgix.printing; 

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class EmployeeBean 
{ 
    private String lastName;
    private String firstName;
    private String prefName;
    private String templateFileName;
    private String templateBody;
    
    public void setLastName(String lastName){
        this.lastName=lastName;
    }
    public String getLastName(){
        return this.lastName;
    }
        
    public void setFirstName(String firstName){
        this.firstName=firstName;
    }
    
    public String getFirstName(){
        return this.firstName;
    }
    
    public void setPrefName(String prefName){
        this.prefName=prefName;
    }
    
    public String getPrefName(){
        return this.prefName;
    }
    
    public void setTemplateFileName(String templateFileName){
        this.templateFileName=templateFileName;
    }
    
    public String getTemplateFileName(){
        return this.templateFileName;
    }
    
    
    public void prepareTemplateBody(){
       InputStream is = null;
       String templateBody="";
       try{
          is = getClass().getResourceAsStream(PrintingConstants.Printing_Template_Directory+"/"+PrintingConstants.templateFileName+".html");
          }catch(Exception e){
          LoggerHelper.logSystemError("Class:EmployeeBean Method:prepareTemplateBody--Couldn't locate the Email Template in prepareTemplateBody ",e);
          }
          BufferedReader   bf = new BufferedReader(new InputStreamReader(is));
          StringBuffer sbr =new StringBuffer();
          StringWriter mailMessage = new StringWriter();
          
          String strSM="";
          try
          {
            while((strSM = bf.readLine())!=null){
              sbr.append(strSM+" ");
           }
          bf.close();  
          }catch(Exception e) {LoggerHelper.logSystemError("Class:EmployeeBean Method:prepareTemplateBody--Error Reading Email temaplte Body ",e);}
          
          this.templateBody=sbr.toString();
    }

    public String getTemplateBody(){
        return this.templateBody;
    }
        
} 
