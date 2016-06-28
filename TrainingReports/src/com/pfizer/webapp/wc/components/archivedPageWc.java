package com.pfizer.webapp.wc.components; 

import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.user.User;
import com.tgix.wc.WebComponent;
import java.util.List;

public class archivedPageWc extends WebComponent{ 
    private User user;
    private String headerId;
    private List labelList;
    private List labelUrlList;
    private int count;
    
    public void setLabelList(List labelList){
        this.labelList=labelList;
    }
    public List getLabelList(){
        return this.labelList;
    }
    public void setlabelUrlList(List labelUrlList){
        this.labelUrlList=labelUrlList;
    }
    public List getlabelUrlList(){
        return this.labelUrlList;
    }
    
    
    public void setHeaderId(String headerId){
        this.headerId=headerId;
    }
    public String getHeaderId(){
      return this.headerId;   
    }
    
    public archivedPageWc(User user){
        this.user=user;
    }
    public User getUser() {
		return user;
	}
    
   
    
    public String getJsp()
    {
        return AppConst.JSP_LOC + "/components/archivedPage.jsp";
    }
    

    public void setupChildren()
    {
    }
} 
