package com.pfizer.db; 



import java.util.Date;



public class RBUPedagogueExam extends RBUExam{


    private int pedIndex;
	

	public RBUPedagogueExam() {}



	public int getPedIndex() {
		return pedIndex;
	}

	public void setPedIndex(int pedIndex) {
		this.pedIndex = pedIndex;
	}	
    
    public boolean equals(Object anObject){
         if (this == anObject) {       
            return true;   
        }   
        
        if (anObject instanceof RBUPedagogueExam) 
        {       
            RBUPedagogueExam e = (RBUPedagogueExam)anObject;   
            if(this.getExamName()!= null){
                return this.getExamName().equals(e.getExamName());              
            }else{
                return false;
            }
        }   
        
        return false;
        
    }

} 

