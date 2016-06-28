package com.pfizer.db; 



import java.util.Date;



public class PedagogueExam {

	private String examName;

	private Date examDate;

	private String examScore;

	private String setId;

	private String emplid;

	private String coaching;
    
    private String examIssueDate="";

	

	public  PedagogueExam() {}

	

	public String getEmplid() {

		return emplid;

	}



	public void setEmplid(String emplid) {

		this.emplid = emplid;

	}
    
    
    public void setExamIssueDate(String examIssueDate){
        this.examIssueDate=examIssueDate;
    }
    public String getExamIssueDate(){
        return this.examIssueDate;
    }



	public Date getExamDate() {

		return examDate;

	}



	public void setExamDate(Date examDate) {

		this.examDate = examDate;

	}



	public String getExamName() {

		return examName;

	}



	public void setExamName(String examName) {

		this.examName = examName;

	}



	public String getExamScore() {

		return examScore;

	}



	public void setExamScore(String examScore) {

		this.examScore = examScore;

	}



	public void setCoaching( String coaching) {

		this.coaching = coaching;

	}

	public String getCoaching() {

		return this.coaching;

	}

	

	public String getSetId() {

		return setId;

	}



	public void setSetId(String setId) {

		this.setId = setId;

	}



	

	

} 

