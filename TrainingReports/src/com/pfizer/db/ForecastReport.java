package com.pfizer.db; 

import com.tgix.Utils.Util;
import com.tgix.html.HtmlBuilder;
import java.util.List;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpSession;

public class ForecastReport 
{ 
    
    private List results;
    private ForecastReport track;
    public static List yesNoList;
    public static List yesNoAltList;
    private List roleCode;
    private List listOfEntry;
    
  
    private String role_cd;
    private String startDate;
    private String endDate;
    private String hireDate;
    private String promotionDate;
    private String duration;
    
    //////////////start: for optional fields///////////////////////////
    public static final String FIELD_GENDER = "gender";
    public static final String FIELD_TRACK_ID	= "trackId";
    public static final String FIELD_MANAGER_EMAIL="managerEmail";
    public static final String FIELD_SOURCE="source";
    public static final String FIELD_HIRE_DATE="hirDate";
    public static final String FIELD_PROM_DATE="promDate";
    public static final String FIELD_EMPLOYEE_ID="employeeId";
    public static final String FIELD_GUID="guid";
    public static final String FIELD_GEOGRAPHY_DESC="geographyDesc";
    public static final String FIELD_REGIONAL_OFFICE_STATE="regionalOfficeState";
    public static final String FIELD_PRODUCTS="products";


    private String trackId; 
    private String trackLabel;
    private String gender;
    private String managerEmail;
    private String source;
    private String hirDate;
    private String promDate;
    private String employeeId;
    private String guId;
    private String geographyDesc;
    private String regionalOfficeState;
    private String products;
    
    public void setManagerEmail( String managerEmail ) {
        this.managerEmail = managerEmail;
    }
    public boolean getManagerEmail() {
         return "Yes".equals(this.managerEmail);
    }
     public void setSource( String source ) {
        this.source = source;
    }
    public boolean getSource() {
         return "Yes".equals(this.source);
    }
     public void setHirDate( String hirDate ) {
        this.hirDate = hirDate;
    }
    public boolean getHirDate() {
         return "Yes".equals(this.hirDate);
    }
      public void setPromDate( String promDate ) {
        this.promDate = promDate;
    }
    public boolean getPromDate() {
         return "Yes".equals(this.promDate);
    }
     public void setEmployeeId( String employeeId ) {
        this.employeeId = employeeId;
    }
    public boolean getEmployeeId() {
         return "Yes".equals(this.employeeId);
    }
     public void setGuId( String guId ) {
        this.guId = guId;
    }
    public boolean getGuId() {
         return "Yes".equals(this.guId);
    }
     public void setGeographyDesc( String geographyDesc ) {
        this.geographyDesc = geographyDesc;
    }
    public boolean getGeographyDesc() {
         return "Yes".equals(this.geographyDesc);
    }
     public void setRegionalOfficeState( String regionalOfficeState ) {
        this.regionalOfficeState = regionalOfficeState;
    }
    public boolean getRegionalOfficeState() {
         return "Yes".equals(this.regionalOfficeState);
    }
     public void setProducts( String products ) {
        this.products = products;
    }
    public boolean getProducts() {
         return "Yes".equals(this.products);
    }
    
    
   //////////////end: for optional fields/////////////////////////// 
    
    
    
    public void setRoleCd (String role_cd){
        this.role_cd=role_cd;
    }
    
    public String getRoleCd (){
        
        return this.role_cd;
    }
     
    public void setStartDate (String startDate){
        this.startDate=startDate;
    }
    
    public String getStartDate (){
        
        return  this.startDate;
    }
    
    public void setEndDate (String endDate){
        this.endDate=endDate;
    }
    
    public String getEndDate (){
        
        return this.endDate ;
    }
    
    public void setHireDate (String hireDate){
        this.hireDate=hireDate;
    }
    
    public String getHireDate (){
        
        return this.hireDate ;
    }
    
    public void setPromotionDate (String promotionDate){
        this.promotionDate=promotionDate;
    }
    
    public String getPromotionDate(){
        
        return this.promotionDate ;
    }
    
    public void setDuration (String duration){
        this.duration=duration;
    }
    
    public String getDuration (){
        
        return this.duration ;
    }
    
    
    
    
    
    public void setListOfEntry(List listOfEntry){
        this.listOfEntry=listOfEntry;
    }
    
    public List getListOfEntry(){
        return this.listOfEntry;   
    }
    
    public void setRoleCode(List roleCode){
        this.roleCode=roleCode;   
    }
    public List getRoleCode(){
        return this.roleCode;   
    }
     
    public ForecastReport () {
    }
    static {
        yesNoList = new ArrayList();
        LabelValueBean bean = new LabelValueBean("Yes","Yes");
        yesNoList.add(bean);
        bean = new LabelValueBean("No","No");
        yesNoList.add(bean);
        yesNoAltList = new ArrayList();

        bean = new LabelValueBean("Yes","1");
        yesNoAltList.add(bean);
        bean = new LabelValueBean("No","0");
        yesNoAltList.add(bean);

	}
    public void setGender( String gender ) {
        this.gender = gender;
    }
    public boolean getGender() {
        System.out.println("gender"+gender);
         return "Yes".equals(this.gender);
    }
    
    
    
    
	public void setTrackId( String id ) {
        this.trackId = id;
    }
    public String getTrackId() {
        return this.trackId;
    }
    public void setTracklabel( String label ) {
        this.trackLabel = label;
    }
    public String getTrackLabel() {
        return this.trackLabel;
    }
    public void setFields( List results ) {
        this.results = results;
    }
    public List getFields() {
        return this.results;
    }
    public void setTrack( ForecastReport track ) {
        this.track = track;
    }
    public ForecastReport getTrack() {
        return this.track;
    }
    
    public  String getOptionalValFromMap(HttpSession session, String state){
        StringBuffer sb = new StringBuffer();
        //String idList[];
        HashMap desc = new HashMap();
        System.out.println("session.getAttribute(state)="+session.getAttribute(state));
        System.out.println("empty string comparison"+("".equals(session.getAttribute(state))));
        if (!("".equals(session.getAttribute(state)))){
            desc = (HashMap)session.getAttribute(state);
            String status = state+"_id";
            String id = (String)session.getAttribute(status);
            System.out.println("in else part"+id +"Status"+state);
            if (!(Util.toEmpty(id).equals(" ")) || (id!=null)){
                System.out.println("in else part"+id);
               //System.out.println("in else part"+desc.toString()); 
                String idList[]=(String[])id.split(",");
                //String valueList[]=(String[])session.getAttribute("compValueList");
            
                if(idList!=null && desc !=null){
                    for(int i=0;i<idList.length;i++){
                        System.out.println("in else part"+idList[i].toString());
                        
                        String andList[] = idList[i].split("AND");
                        String descDisplay= idList[i];
                        if(andList.length >0){
                            System.out.println("has AND");
                            for(int j=0;j<andList.length;j++){
                                System.out.println("desc.get(andList[j])==="+desc.get(andList[j].trim()));
                                System.out.println("andList=="+andList[j]);
                                //descDisplay = descDisplay + " AND " +desc.get(andList[j].trim());
                                descDisplay = descDisplay.replaceAll((String)andList[j].trim(),(String)desc.get(andList[j].trim()) );
                                System.out.println("descDisplay=="+descDisplay);
                            }
                        } else {
                            descDisplay = (String)desc.get(idList[i].trim());
                        }
                        
                         sb.append("<option value=\"").append(HtmlBuilder.encodeHtml(idList[i].trim())).append("\"");
                        sb.append(">").append(HtmlBuilder.encodeHtml(descDisplay)).append("</option>\n");
                        System.out.println("Options== "+sb.toString());
                    } 
                } 
            } 
        }
        
        return sb.toString();
   }
   
    public int getNumOptionalValFromMap(HttpSession session, String state){
        int count = 0;
        if (!("".equals(session.getAttribute(state)))){
            String status = state+"_id";
            String id = (String)session.getAttribute(status);
            if (!(Util.toEmpty(id).equals(" ")) || (id!=null)){
                String idList[]=(String[])id.split(",");
                if(idList!=null){
                    count=idList.length;
                } 
            } 
        }
        return count;
    }
   
} 
