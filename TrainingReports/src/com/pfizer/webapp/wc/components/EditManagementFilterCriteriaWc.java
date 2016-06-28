package com.pfizer.webapp.wc.components; 

import com.pfizer.db.ManagementSummaryReport;
import com.pfizer.db.MenuList;
import com.pfizer.webapp.AppConst;
import com.tgix.html.HtmlBuilder;
import com.tgix.wc.WebComponent;
import java.util.HashMap;
import java.util.List;

public class EditManagementFilterCriteriaWc extends WebComponent
{ 
    private ManagementSummaryReport track;
    private MenuList menuItem;
    private String errorMsg="";
    private List allSalesOrg;
    private HashMap hMap =new HashMap();
// newly added
    private  boolean firstTime = true;
    
    
    
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
    public void setTrack(ManagementSummaryReport track ) {
        this.track = track;
    }
    public ManagementSummaryReport getTrack() {
        return this.track;
    }
   
   
    public void setCourseCodeMap(HashMap hMap){
        this.hMap = hMap;
    } 
    public HashMap getCourseCodeMap(){
        return this.hMap;
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
                        sb.append(" selected ");
                        sb.append(">").append(HtmlBuilder.encodeHtml(CourseNames.get(courseList.get(i)).toString())).append("</option>\n");
        }
     //    System.out.println("String Buffer value==="+sb);
       }
        return sb;
    }
    public String getJsp()
    {
       return AppConst.JSP_LOC + "/components/admin/EditManagementFilterCriteria.jsp";
    }
    public void setupChildren() {}
} 
