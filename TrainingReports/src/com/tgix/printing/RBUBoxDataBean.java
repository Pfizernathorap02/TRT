package com.tgix.printing; 

import java.io.Serializable;

public class RBUBoxDataBean implements Serializable 
{
    private String BoxName;
    private String BoxId;
    private String ProductName;
    private String BoxCombo;
    private String[] Dates;
    
     public String getBoxName() {
		return BoxName;
	}
	public void setBoxName(String boxName) {
		this.BoxName = boxName;
	}  
    
    public String getBoxId() {
		return BoxId;
	}
	public void setBoxId(String boxId) {
		this.BoxId = boxId;
	} 
    
     public String getProductName() {
		return ProductName;
	}
	public void setProductName(String productName) {
		this.ProductName = productName;
	} 
    
    public String getBoxCombo() {
		return BoxCombo;
	}
	public void setBoxCombo(String boxCombo) {
		this.BoxCombo = boxCombo;
	} 
    
    public String[] getDates(){
     return Dates;   
    }
    
    public void setDates(String[] dates){
        this.Dates = dates;
        
    }
     
} 
