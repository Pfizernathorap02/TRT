package com.pfizer.webapp.wc.components;

import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;

public class AddSelectedStatusCourseWc extends WebComponent{
    private String selectIdList[];
    private String selectValueList[];
    private String completedIdList[];
    private String notCompletedIdList[];
    private String registeredIdList[];
    private String notRegisteredIdList[];
    
    
    public AddSelectedStatusCourseWc()
    {}
    
    public void setSelectIdList(String[] selectIdList){
        this.selectIdList=selectIdList;
    }
    public String[] getSelectIdList(){
        return this.selectIdList;
    }
     public void setSelectValueList(String[] selectValueList){
        this.selectValueList=selectValueList;
    }
    public String[] getSelectValueList(){
        return this.selectValueList;
    }
    
    public String getJsp()
    {
        return AppConst.JSP_LOC + "/components/addSelectedStatusCourse.jsp";
    }
    

    public void setupChildren()
    {
    }
    
     public void setCompletedIdList(String[] completedIdList){
        this.completedIdList=completedIdList;
    }
    public String[] getCompletedIdList(){
        return this.completedIdList;
    }
     public void setNotCompletedIdList(String[] notCompletedIdList){
        this.notCompletedIdList=notCompletedIdList;
    }
    public String[] getNotCompletedIdList(){
        return this.notCompletedIdList;
    }
    public void setRegisteredIdList(String[] registeredIdList){
        this.registeredIdList=registeredIdList;
    }
    public String[] getRegisteredIdList(){
        return this.registeredIdList;
    }
     public void setNotRegisteredIdList(String[] notRegisteredIdList){
        this.notRegisteredIdList=notRegisteredIdList;
    }
    public String[] getNotRegisteredIdList(){
        return this.notRegisteredIdList;
    }
} 