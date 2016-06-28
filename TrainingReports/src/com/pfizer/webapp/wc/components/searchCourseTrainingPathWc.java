package com.pfizer.webapp.wc.components; 

import com.pfizer.db.P2lTrackPhase;
import com.pfizer.webapp.AppConst;
import com.tgix.wc.WebComponent;
import java.util.ArrayList;
import java.util.List;
import com.tgix.Utils.Util;

public class searchCourseTrainingPathWc extends WebComponent
{ 
    public static final String FIELD_ACTIVITY_NAME = "activityname";
    public static final String FIELD_CODE = "code";
    
    private String activityname = "";
    private String code=""; 
    private String type="";
    private P2lTrackPhase phase;
    private List currentNode = new ArrayList();
    private String trackId ;
    
    private List searchResults = new ArrayList();
    private int trackphaseId;
    
    public searchCourseTrainingPathWc() {
	}
    
    public void setType(String str) {
        this.type=str;
    }
    public String getType() {
        return this.type;
    }
    public void setTrackId(String id){
        this.trackId = id;
    }
    public String getTrackId(){
        return this.trackId;
    }
    public void setActivityname(String str) {
        this.activityname = str;
    }
    public String getActivityname() {
        return Util.toEmpty(this.activityname);
    }
    public void setCode(String str) {
        this.code = str;
    }
    public String getCode() {
        return Util.toEmpty(this.code);
    }
    
    public List getCurrent() {
        return currentNode;
    }
    public void setCurrent( List list ) {
        this.currentNode = list;
    }
    public List getSearchResults() {
        return searchResults;
    }
    public void setSearchResults( List list ) {
        this.searchResults = list;
    }
    public void setPhase( P2lTrackPhase phase ) {
        this.phase = phase;
    }		
    public P2lTrackPhase getPhase() {
        return this.phase;
    }
    public String getJsp(){
		return AppConst.JSP_LOC + "/components/searchTrainingPath.jsp";
	}  
	public void setupChildren() {}
} 
