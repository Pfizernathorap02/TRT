package com.pfizer.db; 



import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;



public class RBUAllStatus {
	private String emplid;
    private int ped_count;
    private List peds = new ArrayList();
    private RBUSCEExam sce;
    private String productcd;
    private String productdesc;
    private String classid;
    private Date classchedule;
    private String notes;
    private String mostRecentUpdateFlag;
    private String overallstatus;
    private boolean hasCredit;    
    private String lso;
        
        


	public RBUAllStatus() {}

	public String getEmplid() {
		return emplid;
	}

	public void setEmplid(String emplid) {
		this.emplid = emplid;
    }
    public int getPed_count() {
		return ped_count;
	}

	public void setPed_count(int ped_count) {
		this.ped_count = ped_count;
    }
    public List getPeds() {
		return peds;
	}

	public void setPeds(List peds) {
		this.peds = peds;
    }
    
    public RBUSCEExam getSCE() {
		return sce;
	}

	public void setSCE(RBUSCEExam sce) {
		this.sce = sce;
    }
    	public String getProductcd() {
		return productcd;
	}

	public void setProductcd(String productcd) {
		this.productcd = productcd;
	}	
    
    public String getProductdesc() {
		return productdesc;
	}

	public void setProductdesc(String productdesc) {
		this.productdesc = productdesc;
	}	
    
    public Date getClasschedule() {
		return classchedule;
	}

	public void setClasschedule(Date classchedule) {
		this.classchedule = classchedule;
	}	
        
    public String getClassid() {
		return classid;
	}

	public void setClassid(String classid) {
		this.classid = classid;
	}
    	
    public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}	
        	
    public String getMostRecentUpdateFlag() {
		return mostRecentUpdateFlag;
	}
	public void setMostRecentUpdateFlag(String mostRecentUpdateFlag) {
		this.mostRecentUpdateFlag = mostRecentUpdateFlag;
	}	
    public boolean getHasCredit() {
		return hasCredit;
	}
	public void setHasCredit(boolean hasCredit) {
		this.hasCredit = hasCredit;
	}	
    
    public String getLso() {
        return lso;
    }
    
    public void setLso(String lso) {
        this.lso = lso;
    }
    
    public String getOverallStatus(){
        String status = "NC";
        
        if("D".equalsIgnoreCase(mostRecentUpdateFlag)){
            return "Cancelled";
        }
        
        //sce == null && ped = null  -> NC
        //sce == null && ped=C  -> C
        //sce == null && ped=NC  -> NC
        
        
        //ped == null && sce == C -> C
        //ped == null && sce == NC -> NC
        
        if(sce == null){
            //check peds
            if(peds.size()==0){
                //peds is n/a
                status = "N/A";//TO - DO REVIEW 
            }else if(peds.size()<ped_count){
                status = "NC"; 
            }else  if(isPedCompleted()){
                status = "C";
           }
            
        }else{
            if("C".equals(sce.getExamStatus())){
               //check peds
                if(peds.size()==0){
                    //peds is n/a
                    status = "N/A";//TO - DO REVIEW 
                }else if(peds.size()<ped_count){
                    status = "NC"; 
                }else  if(isPedCompleted()){
                    status = "C";
               }
            }else{
                status = "NC";               
            }
        }

        /*
       if(sce == null && peds.size() == 0){
            status = "NC";
       }else if(sce == null){
           if(peds.size()<ped_count){
                status = "NC";  
           }else if(isPedCompleted()){
                status = "C";
           }
       }else if(peds.size() == 0){
            if("C".equals(sce.getExamStatus())){
                status = "C";
            }else{
                status = "NC";               
            }
       }
       if(isPedCompleted()){
          if(sce == null) {
            status = "C";
          }
          else {
             if("C".equals(sce.getExamStatus())){
                status = "C";
            }else{
                status = "NC";               
            }
          }
          
       }
        */
      //  if(status.equals("NC")){
      //  if (getClasschedule() != null) { status = "NC"; System.out.println("all status " + getClasschedule().toLocaleString());}
        
        if(status.equals("N/A")){
            if(hasCredit) status = "Credit";            
        }
        
        return status;
        
    }
    
    public boolean isPedCompleted(){   
        for(Iterator i = peds.iterator(); i.hasNext();){
                RBUPedagogueExam ped = (RBUPedagogueExam)i.next();
                if(null == ped.getExamStatus()||ped.getExamStatus().equals("NC")){
                     return false;
                }
         }
         
         return true;       
    }
    
    public boolean isManagerRequired(){
        boolean tmp = false;
        
         for(Iterator i = peds.iterator(); i.hasNext();){
                RBUPedagogueExam ped = (RBUPedagogueExam)i.next();
                if(null ==ped.getExamScore()){
                    tmp = false;
                }else if(Integer.parseInt(ped.getExamScore())<80){
                    tmp = true;
                    break;
                }
        }  
        
        return tmp;
    
    }

} 

