package com.pfizer.webapp.wc.components.report; 

import com.pfizer.db.ClassRosterBean;
import com.pfizer.db.EmpReport;
import com.pfizer.db.Product;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.AppQueryStrings;
import com.pfizer.webapp.report.ClassFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.tgix.wc.WebComponent;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.tgix.Utils.Util;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class GeneralSessionWc extends WebComponent
{ 
    private ClassFilterForm classFilterForm;
    private ClassRosterBean[] classData;         
    private HashMap classDataMap = new LinkedHashMap();
    private EmpReport[] empReport;
    private String event;
    
    public GeneralSessionWc(ClassFilterForm classFilterForm, ClassRosterBean[] classData, EmpReport[] empReport, String event) {
        this.classFilterForm = classFilterForm;
        this.classData = classData;
        this.setEvent(event);
        this.setEmpReport(empReport);        
        this.setClassDataMap(classData);
	}
    
    public ClassFilterForm getClassFilterForm() {
		return classFilterForm;
	}	
    
    public void setEmpReport(EmpReport[] empReport){
        this.empReport = empReport;
    }    
    
    public EmpReport[] getEmpReport(){
        return this.empReport;
    }
    
    public void setClassData(ClassRosterBean[] classData){
        this.classData = classData;
    }    
    
    public ClassRosterBean[] getClassData(){
        return this.classData;
    }
    
    public void setClassDataMap(ClassRosterBean[] classData) {
        Date startDate = null;
        Date endDate = null;
        Date trainingDate = null;
        List listDates = null;
        
        String classroom = null;
        
        List dateList = null;
        if (classData != null && classData.length > 0) {
            for (int i=0; i<classData.length; i++) {
                 startDate = classData[i].getStartDate();
                 endDate = classData[i].getEndDate();
                 listDates = Util.getDatesBetween(startDate,endDate);                 
                 classroom = classData[i].getClassroom();
                 if (classroom != null && listDates.size() > 0) {                     
                     if (!classDataMap.containsKey(classroom)) {
                        dateList = new ArrayList();                        
                        for (int j=0; j<listDates.size(); j++) {
                            trainingDate = (Date)listDates.get(j);                            
                            dateList.add(trainingDate);                                                        
                        }                        
                        classDataMap.put(classroom, dateList);                        
                     }
                     else {
                        dateList = (ArrayList)classDataMap.get(classroom);
                        for (int j=0; j<listDates.size(); j++) {
                            trainingDate = (Date)listDates.get(j);                                                        
                             if (!dateList.contains(trainingDate))
                                    dateList.add(trainingDate);                            
                        }                        
                     }
                 }
            }
        }
    }
    
    public HashMap getClassDataMap() {
        return this.classDataMap;
    }
    
    public String getJsp() { 
        if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            return AppConst.JSP_LOC + "/components/report/PDFHSGeneralSessionAttendance.jsp";		
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/SPFHSGeneralSessionAttendance.jsp";		
        }
        return AppConst.JSP_LOC + "/components/report/PDFHSGeneralSessionAttendance.jsp";		
	}
    
    public void setupChildren() {                
    }   
    
    public void setEvent( String event) {
		this.event = event;
	}
    
    public String getEvent() {
		return this.event;
	} 

} 
