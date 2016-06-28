package com.pfizer.webapp; 

import com.pfizer.processor.ORSortBy;
import com.tgix.Utils.Util;

public class AppQueryStrings {
	// Pie chart slice click
	public static final String FIELD_SECTION			= "section";
	
	// type of pie that was clicked
	public static final String FIELD_TYPE				= "type";
	
	// if type was test, which exam was clicked
	public static final String FIELD_EXAM				= "exam";
	
	public static final String FIELD_SUBMIT             = "submit";
	public static final String FIELD_EMPLOYEE			= "emplid";
	public static final String FIELD_USER_ID			= "userId";
	
	public static final String FIELD_UPEMP				= "employeeID";
	public static final String FIELD_NTID				= "loginID";
	public static final String FIELD_NTDOMAIN			= "domain";
	
	public static final String FIELD_EXCEL_DOWNLOAD		= "downloadExcel";
	
	public static final String FIELD_FROM_SEARCH		= "search";
	public static final String FIELD_FROM_EMAIL			= "email";
	
	public static final String FIELD_SITE_REPORT		= "report";

	public static final String FIELD_HAS_LOGGED_ON		= "haslogged";	

	public static final String FIELD_USER_ACCESS_STATUS	= "userStatus";	

	public static final String FIELD_ACTIVITY_PK        = "activitypk";
	public static final String FIELD_COMPLETE_ACTIVITY        = "cactivitid";
	public static final String FIELD_SPECIAL_ACTIVITY        = "sactivitid";
	public static final String FIELD_TRACK              = "track";
	public static final String FIELD_MODE              = "mode";
	public static final String FIELD_ID              = "id";
	
	// Product logo clicks
	public static final String FIELD_PRODUCT_CODE	= "productCode";
	private String productCode;

      /* Added for RBU */
    public static final String FIELD_SALES = "sales";
    public static final String FIELD_SALESLEVEL = "saleslevel";
    public static final String FIELD_MULTIPLE = "multiple";
    public static final String FIELD_SALESVALUE="salesvalue";
    public static final String FIELD_SALESORG="salesorg";

    public static final String FIELD_SAVEACCESS="saveAccess";
    public static final String FIELD_SUBMITACCESS="submitAccess";
    public static final String FIELD_REPORTTYPE="reportType";
    
    /*Added for SCE Evaluation form */
    public static final String FIELD_SCESAVE = "sceSave";
    public static final String FIELD_SCESUBMIT = "sceSubmit";
    /*Added for PXED Search*/
    public static final String FIELD_FNAME = "fName";
    public static final String FIELD_LNAME = "lName";
    
    public static final String FIELD_SELECTVALUE = "selectedvalue";
    
    /*Added for SCE Evaluation form*/
    public static final String FIELD_EMPLID = "emplId";
    public static final String FIELD_ACTIVITYID = "activityId";
    public static final String FIELD_EVALID = "evalId";
    public static final String FIELD_EVALNM="evalNm";
    public static final String FIELD_EMPNM="empNm";
    public static final String FIELD_FLAG="flag";
    public static final String FIELD_SUPERADMIN="superAdmin";
    public static final String FIELD_TRACKID= "trackId";

    //added for TRT major enhancement 3.6- F1
     public static final String FIELD_NEWGROUP= "inputADDGroup";
    public static final String FIELD_PARENTACTIVTYPK= "parentActivityPK";
   // Start: Modified for TRT 3.6 enhancement - F 2 -(management summary report) 
    public static final String FIELD_NEWSET="newSet";
    public static final String FIELD_NEWINPUT="newInput";
    public static final String FIELD_SORG="sOrg";
    public static final String FIELD_BU="bu";
    public static final String FIELD_ROLES="roles";
    public static final String FIELD_COURSES="courses";
    public static final String FIELD_GENDER="gender";
   // End: Modified for TRT 3.6 enhancement - F 2 -(management summary report) 
	
	public AppQueryStrings(){}
	
	private String id;
    private String parentActivityPk;
	public void setId( String id ) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
    // Start: Modified for TRT 3.6 enhancement - F 2 -(management summary report)
    private String newSet;
    public void setNewSet(String newset){
        this.newSet=newset;
    }
    public String getNewSet(){
        return this.newSet;
    }
    
    private String newInput;
    public void setNewInput(String newInput)
    {
        this.newInput=newInput;
    }
    public String getNewInput(){
        return this.newInput;
    }
    
    private String sOrg;
    public void setSorg(String sOrg){
        this.sOrg=sOrg;
    }
    public String getSorg(){
       return this.sOrg;
    }
    
    private String bu;
    public void setBu(String bu){
        this.bu=bu;
    }
    public String getBu(){
       return this.bu;
    }   
    
    private String roles;
    public void setRoles(String roles){
        this.roles=roles;
    }
    public String getRoles(){
       return this.roles;
    }   
    
    private String courses;
    public void setCourses(String courses){
        this.courses=courses;
    }
    public String getCourses(){
       return this.courses;
    }   
    
    private String gender;
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return this.gender;
    }
    
    // End: Modified for TRT 3.6 enhancement - F 2 -(management summary report)
    
    
    
    public void setParentActivityPK( String parentActivityPk ) {
		this.parentActivityPk = parentActivityPk;
	}
	public String getParentActivityPK() {
		return this.parentActivityPk;
	}

	private String submit;
	public void setSubmit( String id ) {
		this.submit = id;
	}
	public String getSubmit() {
		return this.submit;
	}


	private String employeeID;
	public void setEmployeeID( String id ) {
		this.employeeID = id;
	}
	public String getEmployeeID() {
		return this.employeeID;
	}
	
	private String activitypk;
    public void setActivitypk( String str ) {
        this.activitypk = str;
    }
    public String getActivitypk() {
        return activitypk;
    }
	private String cactivitid;
    public void setCactivitid( String str ) {
        this.cactivitid = str;
    }
    public int getCactivityid() {
        if ( Util.isEmpty(cactivitid) ) {
            return 0;
        }
        return Integer.parseInt(cactivitid);
    }

	private String sactivitid;
    public void setSactivitid( String str ) {
        this.sactivitid = str;
    }
    public int getSactivityid() {
        if ( Util.isEmpty(sactivitid) ) {
            return 0;
        }
        return Integer.parseInt(sactivitid);
    }


	private String loginID;
	public void setLoginID( String id ) {
		this.loginID = id;
	}
	public String getLoginID() {
		return this.loginID;
	}
    
    private String track = "";
	public void setTrack( String str ) {
        this.track = str;
    }
    public String getTrack() {
        return this.track;
    }
    
    private String mode;
    public String getMode() {
        return mode;
    }
    public void setMode( String mode ) {
        this.mode = mode;
    }
	private String domain;
	public void setDomain( String id ) {
		this.domain = id;
	}
	public String getDomain() {
		return this.domain;
	}
	
	private String section;
	public void setSection(String section) {
		this.section = section;
	}
	public String getSection() {
		return this.section;
	}
	
	private String type;	
	public void setType( String type) {
		this.type = type;
	}
	public String getType() {
		return this.type;
	}
	
	private String exam;	
	public void setExam( String exam ) {
		this.exam = exam;
	}
	public String getExam() {
		return this.exam;
	}
	
	public void setProductCode(String code) {
		this.productCode = code;
	}
	public String getProductCode() {
		return this.productCode;
	}
	
	private String emplid;
	public void setEmplid( String id ) {
		this.emplid = id;
	}
 
	public String getEmplid() {
		return emplid;
	}
	
	private String email;	
	public void setEmail( String flag ) {
		this.email = flag;
	}
	public String getFromEmail() {
		return this.email;
	}
	
	private String downloadExcel;
	public String getDownloadExcel() {
		return downloadExcel;
	}
	public void setDownloadExcel( String flag ) {
		this.downloadExcel = flag;
	}
	

	private String fullQueryString;
	private ORSortBy sortBy = new ORSortBy();
	public void setFullQueryString( String str ) {
		this.fullQueryString = str;
	}
	public String getFullQueryString() {
		return this.fullQueryString;
	}
	
	public String getQueryStringsNoSort() {
		if ( !Util.isEmpty( fullQueryString ) &&  fullQueryString.indexOf("sb_field") != -1 ) {
			return fullQueryString.substring(0,fullQueryString.indexOf("sb_field")-1);
		}
		
		return fullQueryString;
	}

	
	public ORSortBy getSortBy() {
		return this.sortBy;
	}
	
	private String search;
	public String getFromSearch() {
		return search;
	}
	public void setSearch( String str ) {
		this.search = str;
	}
	
	private String report;	
	public void setReport( String report ) {
		this.report = report;
	}
	public String getReport() {
		return this.report;
	}
	
	private String haslogged;
	public void setHaslogged( String str ) {
		haslogged = str;
	}
	public String getHaslogged() {
		return haslogged;
	}
	
	private String userStatus;
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus( String userStatus ) {
		this.userStatus = userStatus;
	}
	
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId( String userId ) {
		this.userId = userId;
	}
    
    //Added for Remove button
    public static String message;
    
    public String getMessage() {
		return message;
	}
	public void setMessage( String message ) {
		this.message = message;
	}
    
    //to check for duplicate User Group
    public static String message2;
    
    public String getMessage2() {
		return message2;
	}
	public void setMessage2( String message2 ) {
		this.message2 = message2;
    }
          
    /* Added for RBU */
    private String sales;
	public String getSales() {
		return sales;
	}
	public void setSales( String sales ) {
		this.sales = sales;
	}
     private String saleslevel;
	public String getSaleslevel() {
		return saleslevel;
	}
	public void setSaleslevel( String saleslevel ) {
		this.saleslevel = saleslevel;
	}
    private String multiple;
	public String getMultiple() {
		return multiple;
	}
	public void setMultiple( String multiple ) {
		this.multiple = multiple;
	}
    
    private String salesvalue;
	public String getSalesvalue() {
		return salesvalue;
	}
	public void setSalesvalue( String salesvalue ) {
		this.salesvalue = salesvalue;
	}
    
    
    
    private String salesorg;
	public String getSalesorg() {
		return salesorg;
	}
	public void setSalesorg( String salesorg ) {
		this.salesorg = salesorg;
	}
    /* End of addition */
    
    /*RBU Phase 2 Added*/
    private String saveAccess;
	public String getSaveAccess() {
		return saveAccess;
	}
	public void setSaveAccess( String saveAccess ) {
		this.saveAccess = saveAccess;
	}
    
    private String submitAccess;
	public String getSubmitAccess() {
		return submitAccess;
	}
	public void setSubmitAccess( String submitAccess ) {
		this.submitAccess = submitAccess;
	}
     //added for TRT major enhancement 3.6- F1
    private String inputADDGroup;
	public String getInputADDGroup() {
		return this.inputADDGroup;
	}
	public void setInputADDGroup( String grp ) {
		this.inputADDGroup = grp;
	}
    
   
    //ends here
    
    private String reportType;
	public String getReportType() {
		return reportType;
	}
	public void setReportType( String reportType ) {
		this.reportType = reportType;
	}
    /*RBU Phase 2 End of Addition*/
    /*Added for SCE form evaluation */
     private String sceSave;
	public String getSceSave() {
		return sceSave;
	}
	public void setSceSave( String sceSave ) {
		this.sceSave = sceSave;
	}
    
    private String sceSubmit;
	public String getSceSubmit() {
		return sceSubmit;
	}
	public void setSceSubmit( String sceSubmit ) {
		this.sceSubmit = sceSubmit;
	}
    /*End Addition for SCE form evaluation */
    /*Added for PXED search function*/
    private String fName;
	public String getFName() {
		return fName;
	}
	public void setFName( String fName ) {
		this.fName = fName;
	}
    
    private String lName;
	public String getLName() {
		return lName;
	}
	public void setLName( String lName ) {
		this.lName = lName;
	}
    /*END of addition for PXED search*/
    
    private String selectedValue;
	public String getSelectedValue() {
		return selectedValue;
	}
	public void setSelectedValue( String selectedValue ) {
		this.selectedValue = selectedValue;
	}
    /*Added SCE evaluation form*/
    private String empId;
	public String getEmpId() {
		return empId;
	}
	public void setEmpId( String empId ) {
		this.empId = empId;
	}
    
    private String activityId;
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId( String activityId ) {
		this.activityId = activityId;
	}
    
    private String evalId;
	public String getEvalId() {
		return evalId;
	}
	public void setEvalId( String evalId ) {
		this.evalId = evalId;
	}
    
    private String evalNm;
	public String getEvalNm() {
		return evalNm;
	}
	public void setEvalNm( String evalNm ) {
		this.evalNm = evalNm;
	}
    
    private String empNm;
	public String getEmpNm() {
		return empNm;
	}
	public void setEmpNm( String empNm ) {
		this.empNm = empNm;
	}
    
    private String flag;
	public String getFlag() {
		return flag;
	}
	public void setFlag( String flag ) {
		this.flag = flag;
	}
    
     private String superAdmin;
	public String getSuperAdmin() {
		return superAdmin;
	}
	public void setSuperAdmin( String superAdmin ) {
		this.superAdmin = superAdmin;
	}
    
     private String trackId;
	public String getTrackId() {
		return trackId;
	}
	public void setTrackId( String trackId ) {
		this.trackId = trackId;
	}
    /*Addition ended*/
} 

