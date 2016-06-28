package com.pfizer.webapp.wc.components;
import com.tgix.wc.WebComponent;
import com.pfizer.db.ForecastReport;
import com.pfizer.db.MenuList;
import com.pfizer.webapp.AppConst;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import com.tgix.Utils.Util;
import com.tgix.html.HtmlBuilder;
import java.util.ArrayList; 

public class EditReportForecastFilterWc extends WebComponent {
    
    
   
    private ForecastReport track;
   
    private MenuList menuItem;
    private String idList[];
    private String valueList[];
    private String errorMsg="";
    private boolean firstTime;
    private String idNotRegisteredList[];
    private String idCompletedList[];
    private String idNotCompletedList[];
    private String idRegisteredList[];
    private boolean save;
   
   public EditReportForecastFilterWc(){
        this.idList=idList;
   }
   public void setIdList(String[] idList){
        this.idList=idList;
        
   }
   public String[] getIdList(){
        return this.idList;
   }
   public void setValueList(String[] valueList){
        this.valueList=valueList;
   }
   
   public String[] getValueList(){
        return this.valueList;
   }
   
   public void setTrack(ForecastReport track){
        this.track=track;
   }
   public ForecastReport getTrack(){
        return this.track;
   }
   public void setMenu(MenuList menuItem){
        this.menuItem=menuItem;
   }
   public MenuList getMenu(){
        return this.menuItem;
   }
   public void setErrorMsg(String error){
        this.errorMsg=error;
   }
   public String getErrorMsg(){
        return this.errorMsg;
   }
   public void setFirstTime(boolean firstTime){
        this.firstTime=firstTime;
   }
   public boolean getFirstTime(){
        return this.firstTime;
   }
    public void setSave(boolean save){
        this.save=save;
   }
   public boolean getSave(){
        return this.save;
   }
   
   
    public String getJsp()
    {
        return AppConst.JSP_LOC + "/components/admin/editReportForecastFilter.jsp";
    }
    

    public void setupChildren()
    {
    }
    public void setCompletedIdList(String[] idCompletedList){
        this.idCompletedList=idCompletedList;
        
   }
   public String[] getCompletedIdList(){
        return this.idCompletedList;
   }
   public void setNotCompletedIdList(String[] idNotCompletedList){
        this.idNotCompletedList=idNotCompletedList;
        
   }
   public String[] getNotCompletedIdList(){
        return this.idNotCompletedList;
   }
   public void setRegisteredIdList(String[] idRegisteredList){
        this.idRegisteredList=idRegisteredList;
        
   }
   public String[] getRegisteredIdList(){
        return this.idRegisteredList;
   }
   public void setNotRegisteredIdList(String[] idNotRegisteredList){
        this.idNotRegisteredList=idNotRegisteredList;
        
   }
   public String[] getNotRegisteredIdList(){
        return this.idNotRegisteredList;
   }
   
}