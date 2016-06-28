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

public class ClassRosterWc extends WebComponent
{ 
    private ClassFilterForm classFilterForm;
    private ClassRosterBean[] classData; 
    private HashMap productMap = new HashMap();    
    private HashMap classDataMap = new LinkedHashMap();
    private EmpReport[] empReport;
    private String event;
    
    public ClassRosterWc(ClassFilterForm classFilterForm, ClassRosterBean[] classData, EmpReport[] empReport, String event) {
        this.classFilterForm = classFilterForm;
        this.classData = classData;
        this.setEvent(event);
        this.setEmpReport(empReport);
        this.setProductMap(classData);
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
    
    public void setProductMap(ClassRosterBean[] classData) {
        if (classData != null && classData.length > 0) {
            for (int i=0; i<classData.length; i++) {
                if (!productMap.containsKey(classData[i].getProductCode())) {
                    productMap.put(classData[i].getProductCode(),classData[i].getProductDesc());
                }
            }
        }                
    }
    
    public void setClassDataMap(ClassRosterBean[] classData) {
        Date startDate = null;
        Date endDate = null;
        Date trainingDate = null;
        List listDates = null;
        
        String productCode = null;
        String productDesc = null;
        Product product = null;
        String classroom = null;
        
        HashMap dateMap = null;
        List productList = null;
        if (classData != null && classData.length > 0) {
            for (int i=0; i<classData.length; i++) {
                 startDate = classData[i].getStartDate();
                 endDate = classData[i].getEndDate();
                 listDates = Util.getDatesBetween(startDate,endDate);
                 productCode = classData[i].getProductCode();                 
                 productDesc = classData[i].getProductDesc();
                 product = new Product();
                 product.setProductCode(productCode);
                 product.setProductDesc(productDesc);
                 
                 classroom = classData[i].getClassroom();
                 if (classroom != null && listDates.size() > 0) {
                     if (!classDataMap.containsKey(classroom)) {
                        dateMap = new LinkedHashMap();                        
                        for (int j=0; j<listDates.size(); j++) {
                            trainingDate = (Date)listDates.get(j);                            
                            productList = new ArrayList();
                            //productList.add(productCode);
                            productList.add(product);
                            dateMap.put(trainingDate, productList);
                        }
                        classDataMap.put(classroom, dateMap);                        
                     }
                     else {
                        dateMap = (HashMap)classDataMap.get(classroom);
                        for (int j=0; j<listDates.size(); j++) {
                            trainingDate = (Date)listDates.get(j);                            
                            if (!dateMap.containsKey(trainingDate)) {
                                productList = new ArrayList();
                                //productList.add(productCode);
                                productList.add(product);
                                dateMap.put(trainingDate, productList);                     
                            }
                            else {
                                productList = (List)dateMap.get(trainingDate);
                                /*if (!productList.contains(productCode))
                                    productList.add(productCode);*/
                                if (!productList.contains(product))
                                    productList.add(product);
                            }
                        }                        
                     }
                 }
                 
            }
        }     
    }
    
    public HashMap getClassDataMap() {
        return this.classDataMap;
    }
    
    public HashMap getProductMap() {
        return this.productMap;
    }
    
    public String getJsp() { 
        if (AppConst.EVENT_PDF.equalsIgnoreCase(event)) {
            return AppConst.JSP_LOC + "/components/report/PDFHSClassRosterReport.jsp";		
        }
        else if (AppConst.EVENT_SPF.equalsIgnoreCase(event)) {            
            return AppConst.JSP_LOC + "/components/report/SPFHSClassRosterReport.jsp";		
        }
        return AppConst.JSP_LOC + "/components/report/PDFHSClassRosterReport.jsp";				
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
