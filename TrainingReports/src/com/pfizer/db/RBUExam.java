package com.pfizer.db; 



import java.util.Date;



public class RBUExam {
	private String examName;
	private Date examDate;
	private String examScore;
	private String emplid;
    private String examStatus = "N/A";
    private String productcd;
    private String productdesc;
    private String examdisplayname;
	

	public RBUExam() {}	

	public String getEmplid() {
		return emplid;
	}

	public void setEmplid(String emplid) {
		this.emplid = emplid;
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
    
    	public String getExamDisplayName() {
		return examdisplayname;
	}
	public void setExamDisplayName(String examdisplayname) {
		this.examdisplayname = examdisplayname;
    }
	public String getExamScore() {
		return examScore;
	}

	public void setExamScore(String examScore) {
		this.examScore = examScore;
	}

	public String getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(String examStatus) {
		this.examStatus = examStatus;
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

} 

