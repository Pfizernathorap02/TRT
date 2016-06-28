package com.pfizer.db; 

public class GAProdCourse 
{ 
    String productCode;
    String courseCode;
    String completion;
    String registration;
    
    public String getProductCode()
    {
        return this.productCode;
    }
    public void setProductCode(String productCode)
    {
        this.productCode = productCode;
    }
    public String getCourseCode()
    {
        return this.courseCode;
    }
    public void setCourseCode(String courseCode)
    {
        this.courseCode = courseCode;
    }
    
    
    public String getCompletion()
    {
        return this.completion;
    }
    public void setCompletion(String completion)
    {
        this.completion = completion;
    }
        
    public String getRegistration()
    {
        return this.registration;
    }
    public void setRegistration(String registration)
    {
        this.registration = registration;
    }


} 
