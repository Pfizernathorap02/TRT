



package com.pfizer.webapp.wc.components; 



import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.db.MenuList;
import com.pfizer.db.TrainingPathConfigBean;
import com.pfizer.hander.TrainingPathHandler;
import com.pfizer.webapp.AppConst;
import com.tgix.html.HtmlBuilder;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.List;

public class TrainingPathAdminWc extends WebComponent
{ 
    private TrainingPathConfigBean track;
    private MenuList menuItem;
    private String errorMsg="";
    private List allSalesOrg;
    private HashMap hMap =new HashMap();
    private HashMap hAliasMap =new HashMap();
    private List salesList;
    private List buList;
    private List roleList;
    private List courseList;
    private List reportList;
     private List courseAliasList;
    private List courseAliasCodeList;
      //  private int configId;
    private List configIdList;
    private TrainingPathConfigBean tPathConfig;
    private int trackId;
    private List TrainingPathConfigurationList;
    
// newly added
  /*  private List salesDescList;
    private List buDescList;
    private List roleDescList;
    private List courseDescList; */
    private  boolean firstTime = true;
    
    public void setTrainingPathConfigurationList(List TrainingPathConfigurations){
        this.TrainingPathConfigurationList = TrainingPathConfigurations;
    }
    public List getTrainingPathConfigurationList(){
        return this.TrainingPathConfigurationList;
    }
    
   public void setTrainingPath(TrainingPathConfigBean tPathConfig){
        this.tPathConfig=tPathConfig;
   }
   public TrainingPathConfigBean getTrainingPath(){
        return this.tPathConfig;
   }
     public void setReportList(List reportList){
        this.reportList = reportList;
    }
    public List getReportList(){
        return this.reportList;
    }
     public void setFirstTime(boolean firstTime){
        this.firstTime=firstTime;
    }
    public boolean getFirstTime(){
        return this.firstTime;
    }
    public void setLabelBean(List allSalesOrg){
        this.allSalesOrg=allSalesOrg;
    }
    public List getLabelBean(){
        return allSalesOrg;
    }
    
    public void setMenu( MenuList menuItem ) {
        this.menuItem = menuItem;
    }
    public MenuList getMenu() {
        return this.menuItem;
    }

    public void setErrorMsg( String msg ) {
        this.errorMsg = msg;
    }
    public String getErrorMsg() {
        return this.errorMsg;
    }
    public void setTrack(TrainingPathConfigBean track ) {
        this.track = track;
    }
    public TrainingPathConfigBean getTrack() {
        return this.track;
    }
   
   
    public void setCourseCodeMap(HashMap hMap){
        this.hMap = hMap;
    } 
    public HashMap getCourseCodeMap(){
        return this.hMap;
    }
    
    public HashMap getCourseAliasMap(){
        return this.hAliasMap;
    }
    
    public void setCourseAliasMap(HashMap hMap){
        this.hAliasMap = hMap;
    }
    
    public StringBuffer getOptionalFieldsFromMap(List courseList,HashMap CourseNames)
    {
       StringBuffer sb = new StringBuffer();
     //  System.out.println("Size=="+CourseNames.size());
       if(CourseNames.size()>0){
        for(int i=0;i<courseList.size();i++){
       //     System.out.println("in web component==="+courseList.get(i));
       //     System.out.println("in web component==="+CourseNames.get(courseList.get(i)));
            
            sb.append("<option value=\"").append(HtmlBuilder.encodeHtml(courseList.get(i).toString())).append("\"");
                        sb.append(" ");
                        sb.append(">").append(HtmlBuilder.encodeHtml(CourseNames.get(courseList.get(i)).toString())).append("</option>\n");
        }
     //    System.out.println("String Buffer value==="+sb);
       }
        return sb;
    }
    
    public void setSalesList(List salesList){
        this.salesList = salesList;
    } 
    public List getSalesList(){
        return this.salesList;
    }
    
    public void setBuList(List buList){
        this.buList = buList;
    } 
    public List getBuList(){
        return this.buList;
    }
    
    public void setRoleList(List roleList){
        this.roleList = roleList;
    } 
    public List getRoleList(){
        return this.roleList;
    }
    
    public void setCourseList(List courseList){
        this.courseList = courseList;
    } 
    public List getCourseList(){
        return this.courseList;
    }
  
     public void setCourseAliasList(List courseAliasList){
        this.courseAliasList = courseAliasList;
    } 
    public List getCourseAliasList(){
        return this.courseAliasList;
    }
    
    public void setCourseAliasCodeList(List courseAliasCodeList){
        this.courseAliasCodeList = courseAliasCodeList;
    } 
    public List getCourseAliasCodeList(){
        return this.courseAliasCodeList;
    }
    
    
  
  /*  public void setSalesDescList(List salesDesc){
        this.salesDescList = salesDesc;
    }
    public List getSalesDescList(){
     return this.salesDescList;
    }
    
    public void setBuDescList(List buDesc){
        this.buDescList=buDesc;
    }
    public List getBuDescList(){
        return this.buDescList;
    }
    
    public void setRoleDescList(List roleDescList){
        this.roleDescList=roleDescList;
    }
    public List getRoleDescList(){
        return this.roleDescList;
    }
    
    public void setCourseDescList(List courseDescList){
        this.courseDescList=courseDescList;
    }
    public List getCourseDescList(){
        return this.courseDescList;
    }
    
    public void setConfigId(int configId){
        this.configId = configId;
    }
    public int getConfigId(){
        return this.configId;
    }
    */
    public void setTrackId(int trackId){
        this.trackId = trackId;
    }
    public int getTrackId(){
        return this.trackId;
    }
    public void setConfigIdList(List configIdList){
        this.configIdList=configIdList;
    }
    public List getConfigIdList(){
        return this.configIdList;
    }
    public String getJsp()
    {
       return AppConst.JSP_LOC + "/components/TrainingPathConfig.jsp";
    }
    public void setupChildren() {}
} 

