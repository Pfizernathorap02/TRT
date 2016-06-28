package com.pfizer.db; 

import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagementSummaryReport 
{ 
    private String trackId;
    private String trackLabel;
    private List allRoleCodes;
    private List allSalesOrg;
    private List allCourseCodes;
    private List allBusinessUnit;
    private String gender;
    private List filterDesc;
/*    private Date hireStartDate;
    private Date hireEndDate;
    private Date trainingCompletionStartDate;
    private Date trainingCompletionEndDate;
    private Date trainingRegistrationStartDate;
    private Date trainingRegistrationEndDate; */

    private String hireStartDate;
    private String hireEndDate;
    private String trainingCompletionStartDate;
    private String trainingCompletionEndDate;
    private String trainingRegistrationStartDate;
    private String trainingRegistrationEndDate;

    private List mFilter;
    private String filterType;
    private List activityIdList;
    private List courseCodeList;
    
    private HashMap count;
    
    private String roleCode;
    private String salesOrg;
    private String courseCode;
    private String businessUnit;
    private List count1;
    private List groupByList;
// Added for group by clause
    private String groupby1;
    private String groupby2;
    private String groupby3;
    private String groupby4;
    private String groupby5;
    private String groupby6;
// End
    private List salesOrgList = new ArrayList();
    private List businessUnitList = new ArrayList();
    private List roleList = new ArrayList();
    private List courseList = new ArrayList();
    
    private String selSalesOrgList;
    
    
    public static List genderList = new ArrayList();
     public static List groupByLabelValueList = new ArrayList();

    static {
       // genderList = new ArrayList();
        LabelValueBean bean = new LabelValueBean("Female","F");
        genderList.add(bean);
        bean = new LabelValueBean("Male","M");
        genderList.add(bean);
        setAllGender(genderList);
        
        //LabelValueBean group = new LabelValueBean("Sales Organization","sales_group");
      /*  LabelValueBean group = new LabelValueBean("None","");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Sales Organization","sales_group");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Business Unit","bu");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Role Code","role_cd");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Gender","sex");
        groupByLabelValueList.add(group);
     //   group = new LabelValueBean("Course Code","activityfk");
        group = new LabelValueBean("Status","status");
        groupByLabelValueList.add(group);
        setGroupByLabelValueList(groupByLabelValueList); */
      
      LabelValueBean group = new LabelValueBean("None","");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Business Unit","bu");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Gender","sex");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Role Code","role_cd");
        groupByLabelValueList.add(group);
         group = new LabelValueBean("Sales Organization","sales_group");
        groupByLabelValueList.add(group);
     //   group = new LabelValueBean("Course Code","activityfk");
        group = new LabelValueBean("Source","source");
        groupByLabelValueList.add(group);
        group = new LabelValueBean("Status","status");
        groupByLabelValueList.add(group);
        setGroupByLabelValueList(groupByLabelValueList);
        
	}
    

     public static void setAllGender(List genderList)
    {
        genderList=genderList;
    }
    public List getAllGender(){
        return this.genderList;
    }
    
    public void setGroupBy1(String groupby1)
    {
        this.groupby1=groupby1;
    }
    public String getGroupBy1(){
        return this.groupby1;
    }
    
     public void setGroupBy2(String groupby2)
    {
        this.groupby2=groupby2;
    }
    public String getGroupBy2(){
        return this.groupby2;
    }
    
     public void setGroupBy3(String groupby3)
    {
        this.groupby3=groupby3;
    }
    public String getGroupBy3(){
        return this.groupby3;
    }
    
    public void setGroupBy4(String groupby4)
    {
        this.groupby4=groupby4;
    }
    public String getGroupBy4(){
        return this.groupby4;
    }
    
    public void setGroupBy5(String groupby5)
    {
        this.groupby5=groupby5;
    }
    public String getGroupBy5(){
        return this.groupby5;
    }
    
    public void setGroupBy6(String groupby6)
    {
        this.groupby6=groupby6;
    }
    public String getGroupBy6(){
        return this.groupby6;
    }
    public void setRoleCode(String roleCode)
    {
        this.roleCode=roleCode;
    }
    public String getRoleCode(){
        return this.roleCode;
    }
    
    public void setSalesOrg(String salesOrg)
    {
        this.salesOrg=salesOrg;
    }
    public String getSalesOrg(){
        return this.salesOrg;
    }
    
     public void setCourseCode(String courseCode)
    {
        this.courseCode=courseCode;
    }
    public String getCourseCode(){
        return this.courseCode;
    }
    
     public void setBusinessUnit(String businessUnit)
    {
        this.businessUnit=businessUnit;
    }
    public String getBusinessUnit(){
        return this.businessUnit;
    }
    
    public void setTrackId(String trackId){
        this.trackId=trackId;
    }
    public String getTrackId()
    {
        return this.trackId;
    }
   
    public void setTrackLabel(String trackLabel){
        this.trackLabel=trackLabel;
    }
    public String getTrackLabel()
    {
        return this.trackLabel;
    }
    
    public void setAllRoleCodes(List roleCodes){
        this.allRoleCodes=roleCodes;
    }
    public List getAllRoleCodes()
    {
        return this.allRoleCodes;
    }
    
    public void setAllCourseCodes(List courseCodes){
        this.allCourseCodes=courseCodes;
    }
    public List getAllCourseCodes()
    {
        return this.allCourseCodes;
    }
    
    public void setAllSalesOrg(List salesOrg){
        this.allSalesOrg=salesOrg;
    }
    public List getAllSalesOrg()
    {
        return this.allSalesOrg;
    }
    
    public void setAllBusinessUnit(List businessUnit){
        this.allBusinessUnit=businessUnit;
    }
    public List getAllBusinessUnit()
    {
        return this.allBusinessUnit;
    }
    
    public void setGender(String gender){
        this.gender=gender;
    }
    public String getGender()
    {
        return this.gender;
    }
    
/*    public void setHireStartDate(Date hireStartDate)
    {
        this.hireStartDate = hireStartDate;
    }
    public Date getHireStartDate()
    {
        return this.hireStartDate;
    }
   
   
    public void setHireEndDate(Date hireEndDate)
    {
       this.hireEndDate=hireEndDate;
    }
    public Date getHireEndDate()
    {
        return this.hireEndDate = hireEndDate;
    }
    
     public void setTrainingCompletionStartDate(Date trainingCompletionStartDate)
    {
       this.trainingCompletionStartDate = trainingCompletionStartDate;
    }
    public Date getTrainingCompletionStartdate()
    {
        return this.trainingCompletionStartDate = trainingCompletionStartDate;
    }
    
     public void setTrainingCompletionEnddate(Date trainingCompletionEndDate)
    {
       this.trainingCompletionEndDate = trainingCompletionEndDate;
    }
    public Date getTrainingCompletionEndDate()
    {
        return this.trainingCompletionEndDate = trainingCompletionEndDate;
    }
    
    
    public void setTrainingRegistrationStartDate(Date trainingRegistrationStartDate)
    {
       this.trainingRegistrationStartDate = trainingRegistrationStartDate;
    }
    public Date getTrainingRegistrationStartDate()
    {
        return this.trainingRegistrationStartDate = trainingRegistrationStartDate;
    }
    
     public void setTrainingRegistrationEndDate(Date trainingRegistrationEndDate)
    {
       this.trainingRegistrationEndDate = trainingRegistrationEndDate;
    }
    public Date getTrainingRegistrationEndDate()
    {
        return this.trainingRegistrationEndDate = trainingRegistrationEndDate;
    }
    
*/        
    public void setHireStartDate(String hireStartDate)
    {
        this.hireStartDate = hireStartDate;
    }
    public String getHireStartDate()
    {
        return this.hireStartDate;
    }
   
   
    public void setHireEndDate(String hireEndDate)
    {
       this.hireEndDate=hireEndDate;
    }
    public String getHireEndDate()
    {
        return this.hireEndDate = hireEndDate;
    }
    
     public void setTrainingCompletionStartDate(String trainingCompletionStartDate)
    {
       this.trainingCompletionStartDate = trainingCompletionStartDate;
    }
    public String getTrainingCompletionStartdate()
    {
        return this.trainingCompletionStartDate = trainingCompletionStartDate;
    }
    
     public void setTrainingCompletionEnddate(String trainingCompletionEndDate)
    {
       this.trainingCompletionEndDate = trainingCompletionEndDate;
    }
    public String getTrainingCompletionEndDate()
    {
        return this.trainingCompletionEndDate = trainingCompletionEndDate;
    }
    
    
    public void setTrainingRegistrationStartDate(String trainingRegistrationStartDate)
    {
       this.trainingRegistrationStartDate = trainingRegistrationStartDate;
    }
    public String getTrainingRegistrationStartDate()
    {
        return this.trainingRegistrationStartDate = trainingRegistrationStartDate;
    }
    
     public void setTrainingRegistrationEndDate(String trainingRegistrationEndDate)
    {
       this.trainingRegistrationEndDate = trainingRegistrationEndDate;
    }
    public String getTrainingRegistrationEndDate()
    {
        return this.trainingRegistrationEndDate = trainingRegistrationEndDate;
    }
    
        
    public void setManagementFilter(List mFilter){
		this.mFilter = mFilter;
	}
    public List getManagementFilter (){
		return this.mFilter;
	}
    
    // Stsrt: for TRT enhancement  
    
    
    public void setAllSalesOrgList(LabelValueBean labelValBean){
        this.salesOrgList.add(labelValBean);
    }
    public List getAllSalesOrgList()
    {
        return this.salesOrgList;
    }
    
    
   
    public void setAllBusinessUnitList(LabelValueBean labelValBean){
        this.businessUnitList.add(labelValBean);
    }
    public List getAllBusinessUnitList()
    {
        return this.businessUnitList;
    }
    
   
   
    public void setAllRoleList(LabelValueBean labelValBean){
        this.roleList.add(labelValBean);
    }
    public List getAllRoleList()
    {
        return this.roleList;
    }
    
  
    public void setAllCourseList(LabelValueBean labelValBean){
        this.courseList.add(labelValBean);
    }
    public List getAllCourseList()
    {
        return this.courseList;
    }
    
   /* public void setSelSalesOrgList(String selSalesOrgList){
        this.selSalesOrgList = selSalesOrgList;
    }
    public String getSelSalesOrgList()
    {
        return this.selSalesOrgList;
    } */
    //End
    
    public void setGroupByList(List groupList)
    {
        this.groupByList = groupList;
    }
    public List getGroupByList(){
        return this.groupByList;
    }
    
    
    public void setFilterType(String ftype){
        this.filterType = ftype;
    }
    public String filterType()
    {
        return this.filterType;
    }
   
    public void setCount(HashMap count){
        this.count=count;
    }
    public HashMap getCount(){
     return this.count;   
    }
    public void setCount1(List count1){
        this.count1=count1;
    }
    public List getCount1(){
     return this.count1;   
    }
    
    public void setActivityIdList(List activityList){
        this.activityIdList = activityList;
    }
    public List getActivityIdList(){
        return this.activityIdList;
    }
    
    public void setcourseCodeList(List courseCodeList){
        this.courseCodeList = courseCodeList;
    }
    public List getcourseCodeList(){
        return this.courseCodeList;
    }
    
  
     public static void setGroupByLabelValueList(List groupByLabelValueList)
    {
        groupByLabelValueList=groupByLabelValueList;
    }
    public List getGroupByLabelValueList(){
        return this.groupByLabelValueList;
    }
    public void setFilterDesc(List filterDesc){
        this.filterDesc=filterDesc;
    }
    public List getFilterDesc(){
        return this.filterDesc;
    }
} 
