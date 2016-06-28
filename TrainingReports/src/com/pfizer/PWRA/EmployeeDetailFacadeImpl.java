package com.pfizer.PWRA;
//import com.pfizer.PWRA.*;
//import com.bea.control.*;
import com.pfizer.db.Product;
import com.pfizer.db.RBUDBConfig;
import com.pfizer.dao.TransactionDB;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.pfizer.hander.EmployeeHandler;

import java.util.Date;
import java.util.List;

public class EmployeeDetailFacadeImpl implements EmployeeDetailFacade{

	
	

	
	/**
	 * @editor-info:code-gen control-interface="true"
	 */

	/* Methods added for Vists Rx Spiriva enhancement:getOverallVRSStatus,getVRSStatusInfo and updateVRSAttendance
	 * Author: Meenakshi
	 * Date:14-Sep-2008
	*/ 
	   
	
	    /**
	     * @common:control
	     */
		/*Infosys - Weblogic to Jboss migration change*/
	    private com.pfizer.dao.TransactionDB employeeDB = new TransactionDB();
	    /**
	     * @common:operation
	     */    
	    public EmployeeInfo getEmployeeInfo(String emplid){
	        EmployeeInfo employeeInfo = new EmployeeInfo();
	        try
	        {
	            employeeInfo = employeeDB.getEmployeeInfo(emplid);  
	            EmployeeInfo employeeInfoImg = employeeDB.getImageURL(emplid);
	            if(employeeInfoImg==null){
	                employeeInfoImg = new EmployeeInfo();
	                employeeInfoImg.setImageURL("");   
	            }
	            employeeInfo.setImageURL(employeeInfoImg.getImageURL());  
	            EmployeeInfo reportToEmployeeInfo = new EmployeeInfo();        
	            reportToEmployeeInfo = employeeDB.getReportToInfo(employeeInfo.getReportToEmplID());                    
	            reportToEmployeeInfo = (reportToEmployeeInfo==null)?new EmployeeInfo():reportToEmployeeInfo;
	            employeeInfo.setReportToEmail(reportToEmployeeInfo.getReportToEmail());
	            employeeInfo.setReportToLastName(reportToEmployeeInfo.getReportToLastName());
	            employeeInfo.setReportToPreferredName(reportToEmployeeInfo.getReportToPreferredName());
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }        
	        return employeeInfo;
	    }
	    

	    /**
	     * @common:operation
	     */    
	    public EmployeeInfo getEmployeeInfoGNSM(String emplid){
	        EmployeeInfo employeeInfo = new EmployeeInfo();
	        try
	        {
	            employeeInfo = employeeDB.getEmployeeInfoGSM(emplid);  
	            EmployeeInfo employeeInfoImg = employeeDB.getImageURL(emplid);      
	            if(employeeInfoImg==null){
	                employeeInfoImg = new EmployeeInfo();
	                employeeInfoImg.setImageURL("");   
	            }
	            employeeInfo.setImageURL(employeeInfoImg.getImageURL());  
	            EmployeeInfo reportToEmployeeInfo = new EmployeeInfo();        
	            reportToEmployeeInfo = employeeDB.getReportToInfo(employeeInfo.getReportToEmplID());                    
	            reportToEmployeeInfo = (reportToEmployeeInfo==null)?new EmployeeInfo():reportToEmployeeInfo;
	            employeeInfo.setReportToEmail(reportToEmployeeInfo.getReportToEmail());
	            employeeInfo.setReportToLastName(reportToEmployeeInfo.getReportToLastName());
	            employeeInfo.setReportToPreferredName(reportToEmployeeInfo.getReportToPreferredName());
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }  
	        return employeeInfo;
	    }
	    
	    /**
	     * @common:operation
	     */
	    public EmployeeInfo getEmployeeInfoRBU(String emplid)
	    {
	        //TODO -
	        
	        EmployeeInfo employeeInfo = new EmployeeInfo();
	        try
	        {
	            employeeInfo = employeeDB.getEmployeeInfoRBU(emplid);  
	            //System.out.println("future manager - " + employeeInfo.getFuture_manager());
	           // EmployeeInfo employeeInfoImg = employeeDB.getImageURL(emplid);
	            EmployeeInfo employeeInfoImg=new EmployeeInfo();
	            employeeInfoImg = employeeDB.getImageURL(emplid);
	            if(employeeInfoImg==null){
	                employeeInfoImg = new EmployeeInfo();
	                employeeInfoImg.setImageURL("");   
	            }
	            employeeInfo.setImageURL(employeeInfoImg.getImageURL());  
	            EmployeeInfo reportToEmployeeInfo = new EmployeeInfo();        
	            reportToEmployeeInfo = employeeDB.getReportToInfoRBU(employeeInfo.getReportToEmplID());                    
	            reportToEmployeeInfo = (reportToEmployeeInfo==null)?new EmployeeInfo():reportToEmployeeInfo;
	            employeeInfo.setReportToEmail(reportToEmployeeInfo.getReportToEmail());
	            employeeInfo.setReportToLastName(reportToEmployeeInfo.getReportToLastName());
	            employeeInfo.setReportToFirstName(reportToEmployeeInfo.getReportToFirstName());
	            // Get the Employee Fututre information for 4918
	            EmployeeInfo futureEmployeeInfo = new EmployeeInfo(); 
	            futureEmployeeInfo = employeeDB.getEmployeeFutureInfoRBU(emplid);
	            if(futureEmployeeInfo != null){
	                employeeInfo.setSalesPositionId(futureEmployeeInfo.getSalesPositionId());
	                employeeInfo.setSalesPositionDesc(futureEmployeeInfo.getSalesPositionDesc());
	                employeeInfo.setFutureRole(futureEmployeeInfo.getFutureRole());
	                employeeInfo.setFutureBU(futureEmployeeInfo.getFutureBU());
	                employeeInfo.setFutureRBU(futureEmployeeInfo.getFutureRBU());
	                employeeInfo.setFutureReportsToEmplID(futureEmployeeInfo.getFutureReportsToEmplID());
	                // Get he employee info for the reports to employee id
	                EmployeeInfo future = employeeDB.getReportToInfoRBU(futureEmployeeInfo.getFutureReportsToEmplID());
	                employeeInfo.setFutureReportToFirstName(future.getReportToFirstName());
	                employeeInfo.setFutureReportToLastName(future.getReportToLastName());
	                employeeInfo.setFutureReportToEmail(future.getReportToEmail());
	            }
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        } 
	        
	        //todo shannon do we still need following 
	        
	        ResultSet rs = null;
			PreparedStatement st = null;
			//Connection conn = null;
			Connection conn = JdbcConnectionUtil.getJdbcConnection();

	        
	        String sql = "SELECT product_cd FROM rbu_current_prod_terr_map cmap, " + RBUDBConfig.VIEW_CURRENT_SNAPSHOT + " mv " 
	                    + " WHERE mv.territory_id = cmap.territory_id " 
	                    + " AND mv.field_active = 'A' AND mv.EMPLID = '" +  emplid 
	                    + "' ORDER BY product_cd ASC";
	        
	        try {
				/*Context ctx = new InitialContext();
				DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

				conn = ds.getConnection();  */                   
	            st = conn.prepareCall(sql);			
	            rs = st.executeQuery();
	            List cprods = new ArrayList();
				while (rs.next()) {                
	                cprods.add(rs.getString("product_cd"));
				}
	            
	            sql = "SELECT fmap.product_cd product_cd  FROM " + RBUDBConfig.VIEW_FUTURE_ALIGNMENT +  " al, "+ RBUDBConfig.VIEW_FUTURE_PROD_TERR_MAP + " fmap" 
	                    + " WHERE al.territory_id =    fmap.territory_id " 
	                    + " AND al.emplid =  '" +  emplid + "'";

	            rs = st.executeQuery();
	            List fprods = new ArrayList();
				while (rs.next()) {                
	                fprods.add(rs.getString("product_cd"));
				}    
	            
	            
	            
	            sql = "SELECT product_cd FROM rbu_product_credits credit" 
	                    + " WHERE mv.emplid  =  " +  emplid;

	            rs = st.executeQuery();
	            List credits = new ArrayList();
				while (rs.next()) {                
	                credits.add(rs.getString("product_cd"));
				}   
	            System.out.println(fprods.size()+"     notepad    "+ employeeInfo);
	            employeeInfo.setFurrentProds(fprods);
	            employeeInfo.setCurrentProds(cprods);
	            employeeInfo.setCredits(credits);
	                    
	         
			} catch (Exception e) {
	            System.out.println(sql);
				e.printStackTrace();
			} finally {
				if ( rs != null) {
					try {
						rs.close();
					} catch ( Exception e2) {
						e2.printStackTrace();
					}
				}
				if ( st != null) {
					try {
						st.close();
					} catch ( Exception e2) {
						e2.printStackTrace();
					}
				}
				if ( conn != null) {
					try {
						conn.close();
					} catch ( Exception e2) {
						e2.printStackTrace();
					}
				}
			}
	               
	        return employeeInfo;
	    }
	    /**
	     * @common:operation 
	     */        
	    public ProductAssignmentInfo getProductAssignment(String emplid){
	        ProductAssignmentInfo productAssignmentInfo = new ProductAssignmentInfo();        
	        Iterator preIter = employeeDB.getPrePDFProduct(emplid);                                                      
	        productAssignmentInfo.setPrePDFProducts(new Vector());  
	        while(preIter.hasNext()){
	            PDFProduct pdfProduct = (PDFProduct)preIter.next();            
	            if(productAssignmentInfo.getPrePDFProductTeam()==null){
	                productAssignmentInfo.setPrePDFProductTeam(pdfProduct.getTeam());    
	            }            
	            productAssignmentInfo.getPrePDFProducts().addElement(pdfProduct.getProduct());            
	        }        
	        Iterator postIter = employeeDB.getPostPDFProduct(emplid);                                                      
	        productAssignmentInfo.setPostPDFProducts(new Vector());  
	        while(postIter.hasNext()){
	            PDFProduct pdfProduct = (PDFProduct)postIter.next();            
	            if(productAssignmentInfo.getPostPDFProductTeam()==null){
	                productAssignmentInfo.setPostPDFProductTeam(pdfProduct.getTeam());    
	            }            
	            productAssignmentInfo.getPostPDFProducts().addElement(pdfProduct.getProduct());            
	        }                
	        return productAssignmentInfo;
	    }
	    
	    /**
	     * @common:operation 
	     */        
	    public ProductAssignmentInfoRBU getProductAssignmentRBU(String emplid){
	        ProductAssignmentInfoRBU productAssignmentInfo = new ProductAssignmentInfoRBU();        

	        Iterator preIter = employeeDB.getPreRBUProduct(emplid);                                                      
	        productAssignmentInfo.setCurrentProducts(new Vector());  
	        while(preIter.hasNext()){
	            RBUProduct pdfProduct = (RBUProduct) preIter.next();            
	            if(productAssignmentInfo.getPreTeam()==null){
	                productAssignmentInfo.setPreTeams(pdfProduct.getPreTeam());    
	            }            
	            productAssignmentInfo.getCurrentProducts().addElement(pdfProduct.getProduct());            
	        }        
	        Iterator postIter = employeeDB.getPostRBUProduct(emplid);                                                      
	        productAssignmentInfo.setFutureProducts(new Vector());  
	 
	        while(postIter.hasNext()){
	            RBUPostProduct pdfProduct = (RBUPostProduct)postIter.next();    
	            System.out.println(" post RBUProduct " +pdfProduct.getBu() + pdfProduct.getProduct());        
	            if(productAssignmentInfo.getFutureBU()==null){
	                productAssignmentInfo.setFutureBU(pdfProduct.getBu());    
	            }            
	            productAssignmentInfo.getFutureProducts().addElement(pdfProduct.getProduct());            
	        }       
	        
	        
	        return productAssignmentInfo;
	    }
	    /**
	     * @common:operation 
	     */        
	    public ProductAssignmentInfo getProductAssignmentSPF(String emplid){
	        ProductAssignmentInfo productAssignmentInfo = new ProductAssignmentInfo();        
	        Iterator preIter = employeeDB.getPrePDFProduct(emplid);                                                      
	        productAssignmentInfo.setPrePDFProducts(new Vector());  
	        while(preIter.hasNext()){
	            PDFProduct pdfProduct = (PDFProduct)preIter.next();            
	            if(productAssignmentInfo.getPrePDFProductTeam()==null){
	                productAssignmentInfo.setPrePDFProductTeam(pdfProduct.getTeam());    
	            }            
	            productAssignmentInfo.getPrePDFProducts().addElement(pdfProduct.getProduct());            
	        }        
	        Iterator postIter = employeeDB.getPostSPFProduct(emplid);                                                      
	        productAssignmentInfo.setPostPDFProducts(new Vector());  
	        while(postIter.hasNext()){
	            PDFProduct pdfProduct = (PDFProduct)postIter.next();            
	            if(productAssignmentInfo.getPostPDFProductTeam()==null){
	                productAssignmentInfo.setPostPDFProductTeam(pdfProduct.getTeam());    
	            }            
	            productAssignmentInfo.getPostPDFProducts().addElement(pdfProduct.getProduct());                        
	        }                
	        return productAssignmentInfo;
	    }
	    /**
	     * @common:operation 
	     */        
	    public Vector getTrainingMaterialHistory(String emplid){
	        Vector output = new Vector();
	       Iterator iter = employeeDB.getTrainingMaterialHistory(emplid);
	       while(iter.hasNext()){
	            TrainingMaterialHistory data = (TrainingMaterialHistory)iter.next();
	            output.add(data);
	        }
	        return output;
	    }
	    /**
	     * @common:operation 
	     */            
	    public Vector getPdfHomeStudyStatusInfo(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getPDFHomeStudyStatus(emplid);
	        while(iter.hasNext()){
	            PDFHomeStudyStatus data = (PDFHomeStudyStatus)iter.next();
	            output.add(data);
	        }        
	        return output;            
	    }
	    
	    
	    /**
	     * @common:operation 
	     */            
	    public void reOrderTrainingMaterialHistory(Vector invIDs, String emplid){
	        Connection conn = null;
	        String seq = null;
	        try{
	            Context ctx = new InitialContext();
				DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
				conn =   ds.getConnection();  
	            // For testing get the next value from local datbase
	            // Change smade by Jeevan for RBU Shipment
	            PreparedStatement pstmt = conn.prepareStatement("SELECT ORDERID.NEXTVAL@trm.pfizer.com  FROM DUAL");
	            //PreparedStatement pstmt = conn.prepareStatement("SELECT TRM_ORDER_ID_TEST_SEQ.NEXTVAL  FROM DUAL");
	            ResultSet rs = pstmt.executeQuery();
	            if(rs.next()){
	                seq = rs.getString(1);    
	            }
	            pstmt.close();
	            rs.close();            
	            CallableStatement cstmt = conn.prepareCall("call RBU_TRM_SHIPMENT_manual_order (?,?,?,?)");
	            for(int i=0;i<invIDs.size();i++){
	                String id = (String)invIDs.elementAt(i);
	                cstmt.setString(1,emplid);
	                cstmt.setString(2,id);                
	                cstmt.setString(3,seq);                
	                cstmt.setString(4,i+1+"");                
	                cstmt.executeUpdate();                
	            }
	            cstmt.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }finally{
	            if(conn!=null){
	                try{
	                    conn.close();
	                }catch(Exception e){
	                    e.printStackTrace();    
	                }
	            }
	        }        
	    }
	    
	    /**
	     * @common:operation 
	     */ 
	    public String getOverallPLCStatus(String emplid){
	        return employeeDB.getOverallPLCStatus(emplid);
	    }
	    
	    /**
	     * @common:operation 
	     */ 
	    public String getOverallSPFStatus(String emplid){
	        return employeeDB.getOverallSPFStatus(emplid);
	    }
	    
	    /**
	     * @common:operation 
	     */     
	    public String getOverallHomeStudyStatus(String emplid){
	        return employeeDB.getOverallHomeStudyStatus(emplid);
	    }
	            
	    /**
	     * @common:operation 
	     */         
	    public Vector getPLCStatusInfo(String emplid){
	        Vector output = new Vector();
	        Vector copyPLCExam = new Vector();
	        Iterator iterPLC = employeeDB.getPLCStatus(emplid);
	        Iterator iterPLCExam = employeeDB.getPLCExamStatus(emplid);
	        while(iterPLCExam.hasNext()){
	            PLCExamStatus plcExamStatus = (PLCExamStatus)iterPLCExam.next();
	            copyPLCExam.addElement(plcExamStatus);
	        }        
	        while(iterPLC.hasNext()){
	            PLCStatus plcStatus = (PLCStatus)iterPLC.next();        
	            String productCode = plcStatus.getProductCode();
	            for(int i=0;i<copyPLCExam.size();i++){
	                PLCExamStatus plcExamStatus = (PLCExamStatus)copyPLCExam.elementAt(i);        
	                if(plcExamStatus.getProductCode().equalsIgnoreCase(productCode)){
	                    plcStatus.getPlcExamStatusList().addElement(plcExamStatus);    
	                }                
	            }            
	            output.addElement(plcStatus);
	        }        
	        return output;
	    }
	    
	        /**
	     * @common:operation 
	     */         
	    public Vector getPLCStatusInfoRBU(String emplid){
	        Vector output = new Vector();
	        Vector copyPLCExam = new Vector();
	        
	        //changes for RBU
	        Iterator iterPLC = employeeDB.getPLCStatusRBU(emplid);
	        Iterator iterPLCExam = employeeDB.getPLCExamStatusRBU(emplid);      
	        
	        while(iterPLCExam.hasNext()){
	            PLCExamStatus plcExamStatus = (PLCExamStatus)iterPLCExam.next();
	            copyPLCExam.addElement(plcExamStatus);
	        }        
	        while(iterPLC.hasNext()){
	            PLCStatus plcStatus = (PLCStatus)iterPLC.next();        
	            String productCode = plcStatus.getProductCode();
	            for(int i=0;i<copyPLCExam.size();i++){
	                PLCExamStatus plcExamStatus = (PLCExamStatus)copyPLCExam.elementAt(i);        
	                if(plcExamStatus.getProductCode().equalsIgnoreCase(productCode)){
	                    plcStatus.getPlcExamStatusList().addElement(plcExamStatus);    
	                }                
	            }            
	            output.addElement(plcStatus);
	        }        
	        return output;
	    }
	    /**
	     * @common:operation 
	     */         
	    public Vector getSPFStatusInfo(String emplid){
	        Vector output = new Vector();
	        Vector copyPLCExam = new Vector();
	        Iterator iterPLC = employeeDB.getSPFStatus(emplid);
	        Iterator iterPLCExam = employeeDB.getSPFExamStatus(emplid);
	        while(iterPLCExam.hasNext()){
	            PLCExamStatus plcExamStatus = (PLCExamStatus)iterPLCExam.next();
	            copyPLCExam.addElement(plcExamStatus);
	        }        
	        while(iterPLC.hasNext()){
	            PLCStatus plcStatus = (PLCStatus)iterPLC.next();        
	            String productCode = plcStatus.getProductCode();
	            for(int i=0;i<copyPLCExam.size();i++){
	                PLCExamStatus plcExamStatus = (PLCExamStatus)copyPLCExam.elementAt(i);        
	                if(plcExamStatus.getProductCode().equalsIgnoreCase(productCode)){
	                    plcStatus.getPlcExamStatusList().addElement(plcExamStatus);    
	                }                
	            }            
	            output.addElement(plcStatus);
	        }        
	        return output;
	    }
	    /**
	     * @common:operation 
	     */             
	    public Vector getCancelTraining(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getCancelTrainingSchedule(emplid);
	        while(iter.hasNext()){
	            TrainingSchedule t = (TrainingSchedule)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	        /**
	     * @common:operation 
	     */             
	    public Vector getRBUCancelTraining(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getRBUCancelTrainingSchedule(emplid);
	        while(iter.hasNext()){
	            TrainingSchedule t = (TrainingSchedule)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }

	    /**
	     * @common:operation 
	     */             
	    public Vector getTrainingScheduleInfo(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getTrainingSchedule(emplid);
	        while(iter.hasNext()){
	            TrainingSchedule t = (TrainingSchedule)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	        /**
	     * @common:operation 
	     */             
	    public Vector getRBUTrainingScheduleInfo(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getRBUTrainingSchedule(emplid);
	        while(iter.hasNext()){
	            TrainingSchedule t = (TrainingSchedule)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	        /**
	     * @common:operation 
	     */                 
	    public Vector getRBUTrainingScheduleList(String courseID){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getRBUTrainingScheduleList(courseID);
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	            /**
	     * @common:operation 
	     */                 
	    public Vector getRBUTrainingScheduleListByProduct(String productcd){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getRBUTrainingScheduleListByProduct(productcd);
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	    /**
	     * @common:operation 
	     */                 
	    public Vector getPDFTrainingScheduleListPHR(String courseID){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getPDFTrainingScheduleListPHR(courseID);
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	    /**
	     * @common:operation 
	     */                 
	    public Vector getSPFTrainingScheduleListPHR(String courseID){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getSPFTrainingScheduleListPHR(courseID);
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	    /**
	     * @common:operation 
	     */             
	    public Vector getPDFTrainingScheduleList(String courseID){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getPDFTrainingScheduleList(courseID);
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	    /**
	     * @common:operation 
	     */             
	    public Vector getSPFTrainingScheduleList(String courseID){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getSPFTrainingScheduleList(courseID);
	        
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        
	        return output;
	    }

	    /**
	     * @common:operation 
	     */             
	    public void updateCourseList(String userId,String emplid,String oldCourseID,String newCourseID){
	        //Update Value
	        employeeDB.updateCourseList(emplid,oldCourseID,newCourseID);
	        //Insert Audit Log
	        employeeDB.insertAuditLogTrainingScheduleChange(userId,emplid,oldCourseID,newCourseID);
	    } 
	    
	    /** added by shannon for RBU UPDATE
	     * @common:operation 
	     */             
	    public void updateRBUClass(String userId,String emplid,String oldCourseID,String newCourseID){
	        Connection conn = null;
	        String seq = null;
	        try{
	            Context ctx = new InitialContext();
				DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
				conn =   ds.getConnection();  
	            CallableStatement proc =
	            conn.prepareCall("{ call RBU_MANUAL_CLASS_UPDATE (?, ?, ?, ?) }");
	            proc.setString(1, emplid );
	            proc.setString(2, oldCourseID );
	            proc.setString(3, newCourseID );
	            proc.setString(4, userId );
	            ResultSet rs = proc.executeQuery();

	            proc.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }finally{
	            if(conn!=null){
	                try{
	                    conn.close();
	                }catch(Exception e){
	                    e.printStackTrace();    
	                }
	            }
	        }   
	    } 
	    
	    /**
	     * @common:operation 
	     */             
	    public void cancelRBUTraining(String userId, String emplid,String courseid,String reason){
	        Connection conn = null;
	        String seq = null;
	        try{
	            Context ctx = new InitialContext();
				DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
				conn =   ds.getConnection();  
	            CallableStatement proc =
	            conn.prepareCall("{ call RBU_MANUAL_CLASS_DELETE (?, ?, ?, ?) }");
	            proc.setString(1, emplid );
	            proc.setString(2, courseid );
	            proc.setString(3, reason );
	            proc.setString(4, userId );
	            ResultSet rs = proc.executeQuery();

	            proc.close();
	        }catch(Exception e){
	            e.printStackTrace();
	        }finally{
	            if(conn!=null){
	                try{
	                    conn.close();
	                }catch(Exception e){
	                    e.printStackTrace();    
	                }
	            }
	        }   
	    } 
	    /**
	     * @common:operation 
	     */             
	    public void cancelTraining(String emplid,String courseid, String sRole, String gender, String role, String status, String reason){
	        //Update Value
	        employeeDB.cancelTrainingSchedule(emplid,courseid);
	        //Insert Audit Log
	        employeeDB.updateDeletedCourseAssignment(emplid, courseid, gender, role, status , reason);  
	    } 
	        
	    /**
	     * @common:operation 
	     */             
	    public Vector getTrainingScheduleListLYRC(String courseID, String team){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getTrainingScheduleListLYRC(courseID, team);
	        while(iter.hasNext()){
	            TrainingScheduleList t = (TrainingScheduleList)iter.next();
	            output.addElement(t);
	        }
	        return output;
	    }
	    
	    /**
	     * @common:operation 
	     */             
	    public String getTeam(String emplId){
	        return employeeDB.getTeam(emplId);
	    }
	    
	    /**
	     * @common:operation 
	     */     
	    public String getOverallGNSMStatus(String emplid){
	        return employeeDB.getOverallGNSMStatus(emplid);
	    }

	    /**
	     * @common:operation 
	     */     
	    public String getOverallMSEPIStatus(String emplid){
	        return employeeDB.getOverallMSEPIStatus(emplid);
	    }
	    
	    /**
	     * @common:operation 
	     */         
	    public Vector getGNSMStatusInfo(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getGNSMStatus(emplid);
	        while(iter.hasNext()){
	            PDFHomeStudyStatus data = (PDFHomeStudyStatus)iter.next();
	            output.add(data);
	        }        
	        return output;            
	    }
	    
	   
	    /**
	     * @common:operation 
	     */     
	    public String getOverallVRSStatus(String emplid){
	        return employeeDB.getOverallVRSStatus(emplid);
	    }
	    
	    /**
	     * @common:operation 
	     */         
	    public Vector getVRSStatusInfo(String emplid){
	        Vector output = new Vector();
	        Iterator iter = employeeDB.getVRSStatus(emplid);
	        while(iter.hasNext()){
	            PDFHomeStudyStatus data = (PDFHomeStudyStatus)iter.next();
	            output.add(data);
	        }        
	        return output;            
	    }
	  
	        
	    /**
	     * @common:operation 
	     */             
	    public void updateGNSMAttendance(String emplid,String result, String mode, String userID){                
	        int output = employeeDB.updateGNSMAttendance(emplid,result,mode,userID);
	        if(output==0){
	            employeeDB.insertGNSMAttendance(emplid,result,mode,userID);    
	        }                
	    } 
	    
	     /**
	     * @common:operation 
	     */   
	    public void updateVRSAttendance(String emplid,String result, String mode, String userID){                
	        int output = employeeDB.updateVRSAttendance(emplid,result,mode,userID);
	        if(output==0){
	            employeeDB.insertVRSAttendance(emplid,result,mode,userID);    
	        }                
	    }     

	    /**
	     * @common:operation 
	     */             
	    public void updateMSEPIAttendance(String emplid,String result, String mode, String userID){                
	        int output = employeeDB.updateMSEPIAttendance(emplid,result,mode,userID);
	        if(output==0){
	            employeeDB.insertMSEPIAttendance(emplid,result,mode,userID);    
	        }                
	    }     
	    
	    /**
	     * @common:operation 
	     */             
	    public String getAttendance(String emplid,String mode){
	        return employeeDB.getAttendance(emplid,mode);        
	    }
	    
	    /**
	     * @common:operation 
	     */      
	    public String getVRSAttendance(String emplid,String mode){
	        return employeeDB.getVRSAttendance(emplid,mode);        
	    }
	          
	    
	    /**
	     * @common:operation 
	     */             
	    public String getMSEPIAttendance(String emplid,String mode){
	        return employeeDB.getMSEPIAttendance(emplid,mode);        
	    } 
	    
	    
	    
	    /**
	     * @common:operation 
	     */             
	    public String getPLAttendanceStatus(String emplid){
	        return employeeDB.getPLAttendanceStatus(emplid);        
	    }       


	    /**
	     * @common:operation 
	     */             
	    public String getMSEPIPLAttendanceStatus(String emplid){
	        return employeeDB.getMSEPIPLAttendanceStatus(emplid);        
	    }               
	    
	    /**
	     * @common:operation 
	     */        
	    public List getRBUTrainingStatus(String emplid, EmployeeInfo employeeinfo){
	        List Status = new ArrayList();
	       // RBUTrainingStatus rStatus = new RBUTrainingStatus();        
	        //ADDED  BY SHANNON - use direct jdbc instead of db control
	        for(Iterator i = employeeinfo.getFurrentProds().iterator(); i.hasNext();){
	            
	            String prod_cd = (String) i.next();
	            
	            RBUTrainingStatus rStatus = new RBUTrainingStatus(); 
	            rStatus.setProductDesc(prod_cd);
	            /*System.out.println("######################## " + prod_cd);
	            if(prod_cd.equalsIgnoreCase("Lyrica PC") || prod_cd.equalsIgnoreCase("Lyrica SM")){
	                prod_cd = "Lyrica";
	            }
	            if(prod_cd.equalsIgnoreCase("ARCPPC") || prod_cd.equalsIgnoreCase("ARCPSM")){
	                prod_cd = "ARCP";
	            }
	            if(prod_cd.equalsIgnoreCase("GEODPC") || prod_cd.equalsIgnoreCase("GEODSM")){
	                prod_cd = "GEOD";
	            }
	             System.out.println("######################## After" + prod_cd);*/
	            if(employeeinfo.getCurrentProds().contains(prod_cd) ||employeeinfo.getCredits().contains(prod_cd)){
	                rStatus.setStatus("Credit");                
	            }else{
	                rStatus.setStatus("Credit");
	            }
	            
	        }
	        ResultSet rs = null;
			PreparedStatement st = null;
			Connection conn = null;

	        
	        String sql = "SELECT CURRENT_PRODUCTS, CURRENT_TEAM, FUTURE_BU, FUTURE_PRODUCTS FROM V_RBU_VARIANCE_REPORT WHERE EMPLID = " + emplid;
	        
	        try {
				Context ctx = new InitialContext();
				DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);

				conn =   ds.getConnection();                     
	            st = conn.prepareCall(sql);			
	            rs = st.executeQuery();
	            
				while (rs.next()) {                

				}
	         
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if ( rs != null) {
					try {
						rs.close();
					} catch ( Exception e2) {
						e2.printStackTrace();
					}
				}
				if ( st != null) {
					try {
						st.close();
					} catch ( Exception e2) {
						e2.printStackTrace();
					}
				}
				if ( conn != null) {
					try {
						conn.close();
					} catch ( Exception e2) {
						e2.printStackTrace();
					}
				}
			}
	        

	        
	        
	        return Status;
	    }
	    
	
}

