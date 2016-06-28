package com.pfizer.db; 


public class RBUSCEExam extends RBUExam{
    private String examScore = "";
	public RBUSCEExam() {}
    
    
    public void setExamScore(String examScore) {
		this.examScore = examScore;
	}
    public String getExamScore() {
        if(examScore!= null && examScore.equalsIgnoreCase("UN")){
            return "UN score to be revied";
        }
		return examScore;
	}
    
    
} 

