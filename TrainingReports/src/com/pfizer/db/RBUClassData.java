package com.pfizer.db; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RBUClassData{
        String courseID;
        Date startDate;
        Date endDate;
        String courseName;
        String productcd;
        String productdesc;
        private List tables = new ArrayList();
        
        
            public void setTables( List tables ) {
        this.tables = tables;
    }
   public List getTables( ) {
        return this.tables;
   }
        public String getCourseID() {
            return courseID;
        }
        public void setCourseID(String courseID) {
            this.courseID = courseID;
        }
        public Date getStartDate() {
            return startDate;
        }
        public void setStartDate(Date startDate) {
            this.startDate = startDate;
        }  
        public Date getEndDate() {
            return endDate;
        }
        public void setEndDate(Date endDate) {
            this.endDate = endDate;
        }          
         public String getCourseName() {
            return courseName;
        }
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }
        
        public String getProductcd() {
            return productcd;
        }
        public void setProductcd(String productcd) {
            this.productcd = productcd;
        }
         public String getProductdesc() {
            return productdesc;
        }
        public void setProductdesc(String productdesc) {
            this.productdesc = productdesc;
        }
        
    } 
            