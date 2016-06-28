package com.pfizer.webapp.wc.components.report.phasereports; 

import com.pfizer.db.P2lTrack;
import com.tgix.html.LabelValueBean;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CourseSearchForm { 
    public static String FIELD_TRAINING = "SearchForm_training";
    public static String FIELD_PHASE = "SearchForm_phase";
    public static String FIELD_ACTIVITY = "SearchForm_activity";
    public static String FIELD_SUMBITFLAG = "SearchForm_submitflag";
    
    private String courseid;
    private String status;
    private String training;
    private String phase;
    private String type;
    private String activity;
    private String submitflag;
    
    public static List statusList;
    private List trainingList;
    
    static {
        statusList = new ArrayList();
        LabelValueBean bean = new LabelValueBean("Complete","Complete");
        statusList.add(bean);
        bean = new LabelValueBean("Assigned","Assigned"); 
        statusList.add(bean);       
	}
    public CourseSearchForm(){}

    public void setSubmitflag( String str ) {
        this.submitflag = str;
    }
    public boolean isSubmit() {
        return "true".equals(submitflag);
    }
    public String getActivity() {
        return activity;
    }
    public void setActivity( String str ) {
        this.activity = str;
    }
	public String getCourseid() {
		return courseid;
	}

    private List tracks = new ArrayList();
    public void setTrackPhase(List tracks) {
        trainingList = new ArrayList();
        this.tracks = tracks;
        for (Iterator it = tracks.iterator(); it.hasNext();) {
            P2lTrack track = (P2lTrack)it.next();
            LabelValueBean bean = new LabelValueBean(track.getTrackLabel(),track.getTrackId());
            trainingList.add(bean);
        }
    }
    public List getTracks() {
        return tracks;
    }
    public List getTrainingList() {
        return trainingList;
    }
	public void setCourseid(String courseId) {
		this.courseid = courseId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTraining() {
		return training;
	}

	public void setTraining(String training) {
		this.training = training;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
    
    
} 
