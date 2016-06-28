package com.tgix.printing; 


//Added file for TRT 3.6 Enhancement F4.4 - admin configuration of employee grid. 
public class EmployeeGridOptFieldsBean 
{ 
    
    private String dBColumnName;
    private String fieldName;
    
    public void setDBColumnName (String name){
        this.dBColumnName =name;
    }
    
    public String getDBColumnName (){
        return this.dBColumnName;
    }
    public void setFieldName(String name)
    {
        this.fieldName=name;
    }
    public String getFieldName(){
        return this.fieldName;
    }
    //getter and setter for fieldname

    public boolean inList(String str) {
        //split str with delimiter as “,”
       String[] arr= str.split(",");
       for(int i=0; i< arr.length; i++) {
          if(dBColumnName.equals(arr[i]))
			return true;
		}
	 //end for
	return false;
    }
    
}




