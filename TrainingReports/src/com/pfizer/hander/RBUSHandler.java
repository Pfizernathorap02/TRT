package com.pfizer.hander; 

//import PWRA.employeeDetail.EmployeeDetailFacade.EmployeeInfo;
import au.com.bytecode.opencsv.CSVReader;

import com.pfizer.db.RBUAllStatus;
import com.pfizer.db.RBUClassData;
import com.pfizer.db.RBUClassRoom;
import com.pfizer.db.RBUClassTable;
import com.pfizer.db.RBUGTConflict;
import com.pfizer.db.RBUGuestClassData;
import com.pfizer.db.RBUGuestTrainersClassData;
import com.pfizer.db.RBUPedagogueExam;
import com.pfizer.db.RBUProductDocsLinks;
import com.pfizer.db.RBURoomGridVO;
import com.pfizer.db.RBUSCEExam;
import com.pfizer.db.RBUTrainee;
import com.pfizer.db.RBUTrainingWeek;
import com.pfizer.db.RBUUnassignedEmployees;
import com.pfizer.db.ToviazLaunchExamStatus;
import com.pfizer.db.UserAccess;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.wc.RBU.RBUChartBean;
import com.tgix.Utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class RBUSHandler 
{ 
    	protected static final Log log = LogFactory.getLog( RBUSHandler.class );
        
        public RBUSHandler(){
        }
        
        
        
        //View Changed from V_POWERS_POA_DATA to V_PWRA_HS_DATA
        public RBUChartBean[] getFilteredRBUSChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            String sqlQuery1="SELECT 'Complete' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_PWRA_HS_DATA"+
                             " WHERE  STATUS = 'P' "+
                             "        AND PRODUCT_CD = '"+productCd+"' "  ;
            String sqlQuery2="SELECT 'InComplete' COURSESTATUS,"+ 
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_PWRA_HS_DATA"+
                             " WHERE  STATUS = 'I' "+
                             "        AND PRODUCT_CD = '"+productCd+"' "  ;
            String sqlQuery3="SELECT 'OnLeave' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_PWRA_HS_DATA"+
                             " WHERE  STATUS = 'L' "+
                             "        AND PRODUCT_CD = '"+productCd+"' " ;
            //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(areaCd)){
             condQuery=condQuery+ "AND AREA_CD='"+areaCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(teamCd) ||  "".equalsIgnoreCase(teamCd.trim())){
             condQuery=condQuery+ "AND TEAM_CD='"+teamCd+"' ";
            }
            
            if(!"ALL".equalsIgnoreCase(regionCd)){
             condQuery=condQuery+ "AND REGION_CD='"+regionCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(districtId)){
             condQuery=condQuery+ "AND DISTRICT_ID='"+districtId+"' ";
            }
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for PDFHS Handler with filter:"+finalQuery.toString());
            return executeStatement(finalQuery);
        }
        
        
        
         public RBUChartBean[] getFilteredRBUSOverallChart(String productCd,String areaCd,String regionCd,String districtId){
            String sqlQuery1="SELECT 'Complete' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_PWRA_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS = 'P' ";
                             
            String sqlQuery2="SELECT 'InComplete' COURSESTATUS,"+ 
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_PWRA_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS = 'I' ";
                             
            String sqlQuery3="SELECT 'OnLeave' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_PWRA_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS = 'L' ";
                             
            //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(areaCd)){
             condQuery=condQuery+ "AND AREA_CD='"+areaCd+"' ";
            }
            
            if(!"ALL".equalsIgnoreCase(regionCd)){
             condQuery=condQuery+ "AND REGION_CD='"+regionCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(districtId)){
             condQuery=condQuery+ "AND DISTRICT_ID='"+districtId+"' ";
            }
            sqlQuery1=sqlQuery1+condQuery;
            sqlQuery2=sqlQuery2+condQuery;
            sqlQuery3=sqlQuery3+condQuery;
            String finalQuery=sqlQuery1+ " UNION " + sqlQuery2 + " UNION "  + sqlQuery3;
            log.debug("Query for PDFHS Handler with filter:"+finalQuery.toString());
            return executeStatement(finalQuery);
        }
        
       
        
       public int getPLCOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId) {
        String sqlQuery1="SELECT COUNT(DISTINCT EMPLID) COUNT"+
                             " FROM   V_PWRA_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS IN ('L',"+
                             "                           'I',"+
                             "                           'P')" ;

        
        
        //ConditionQuery
        String condQuery="";
            if(!"ALL".equalsIgnoreCase(areaCd)){
             condQuery=condQuery+ "AND AREA_CD='"+areaCd+"' ";
            }

            if(!"ALL".equalsIgnoreCase(regionCd)){
             condQuery=condQuery+ "AND REGION_CD='"+regionCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(districtId)){
             condQuery=condQuery+ "AND DISTRICT_ID='"+districtId+"' ";
            }
            
            sqlQuery1=sqlQuery1+condQuery;
            return getCount(sqlQuery1);
         
        }
        
        private int getCount(String query){
         int total=0;
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
        	 
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/
        	st = conn.prepareCall(query);			
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                total=rs.getInt("COUNT");
        
          }
          
            st.close();
            conn.close();
        }catch(Exception e){
                log.error(e,e);
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
         
		}
           return total;   
           }
         
    
     private RBUChartBean[] executeStatement(String query){
         
         RBUChartBean[] rbuChartBean=null;
         RBUChartBean thisPOAChartBean;
         List tempList = new ArrayList();	
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareCall(query);			
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                thisPOAChartBean=new RBUChartBean();
                thisPOAChartBean.setCourseStatus(rs.getString("COURSESTATUS"));
                thisPOAChartBean.setTotal(rs.getInt("TOTAL"));
                tempList.add(thisPOAChartBean);                        
            }
            rbuChartBean=new RBUChartBean[tempList.size()];
            for(int i=0;i<tempList.size();i++){
                rbuChartBean[i]=(RBUChartBean)tempList.get(i);
            }
            
            
            st.close();
            conn.close();
        }catch(Exception e){
                log.error(e,e);
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
        return rbuChartBean;
        }
        

    public List getRBUAllStatus(String emplid)
    {
        //System.out.println("i'm in rbushandler for getRBUAllStatus ");
        List rbustatuses = new ArrayList();
        String sql = "select EMPLID, PRODUCT_CD, PRODUCT_DESC, LSO_URL FROM V_RBU_FUTURE_PRODUCTS p where p.emplid= " + emplid + " ORDER BY PRODUCT_DESC";

        ResultSet rs = null;
        PreparedStatement st = null;
        ResultSet rs1 = null;
        PreparedStatement st1 = null;
        Connection conn = null;      
        String query = "";
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareCall(sql);			
            rs = st.executeQuery();
            

            
            
            while (rs.next()) {
                ///System.out.println("i'm in rbushandler for getRBUAllStatus - finding status ");
                RBUAllStatus status = new RBUAllStatus();
                RBUSCEExam sce = new RBUSCEExam();
                List peds = new ArrayList();
                
                
                status.setEmplid(rs.getString("EMPLID"));
                status.setProductcd(rs.getString("PRODUCT_CD"));
                status.setProductdesc(rs.getString("PRODUCT_DESC"));
                status.setLso(rs.getString("LSO_URL"));
                
                //get peds data
                            
                query = "SELECT EXAM_NAME, TEST_SCORE, STATUS, PED_ORDER, EXAM_TAKEN_DATE, PED_COUNT, EXAM_DISPLAY_NAME DISPLAY FROM V_RBU_PED_SCE_DATA p "
                        + " where p.EXAM_TYPE = 'PED' AND p.product_cd = '" + status.getProductcd() + "' AND p.emplid ='" + emplid
                        + "' ORDER BY p.PED_ORDER asc";
                st1 = conn.prepareCall(query);			
                rs1 = st1.executeQuery();
                
                while (rs1.next()){
                    RBUPedagogueExam ped = new RBUPedagogueExam();
                    
                    ped.setEmplid(status.getEmplid());
                    ped.setExamName(rs1.getString("EXAM_NAME"));
                    ped.setExamScore(rs1.getString("TEST_SCORE"));
                    ped.setExamStatus(rs1.getString("STATUS"));
                    ped.setPedIndex(rs1.getInt("PED_ORDER"));
                    ped.setExamDate(rs1.getDate("EXAM_TAKEN_DATE"));
                    ped.setExamDisplayName(rs1.getString("DISPLAY"));
                    peds.add(ped);
                    status.setPed_count(Integer.parseInt(rs1.getString("PED_COUNT")));
                }
                status.setPeds(peds);
                
                query = "SELECT * FROM V_RBU_PED_SCE_DATA p "
                        + " where p.EXAM_TYPE = 'SCE' AND p.product_cd = '" + status.getProductcd() + "' AND p.emplid ='" + emplid + "'";
                st1 = conn.prepareCall(query);			
                rs1 = st1.executeQuery();
                
                if (rs1.next()){
                    sce.setEmplid(status.getEmplid());
                    sce.setExamName(rs1.getString("EXAM_NAME"));
                    sce.setExamScore(rs1.getString("TEST_SCORE"));
                    sce.setExamStatus(rs1.getString("STATUS"));
                    sce.setExamDate(rs1.getDate("EXAM_TAKEN_DATE"));     
                    status.setSCE(sce);
                } 
                
                
                
                query = "SELECT C.CLASS_ID,C.START_DATE COURSESCHEDULE"
                        + " FROM RBU_CLASS C,RBU_CLASS_ASSIGNMENT CA where C.CLASS_ID = CA.CLASS_ID AND ca.emplid =" + emplid
                        + " AND c.PRODUCT_CD = '" + status.getProductcd()+"'" ;
                
                st1 = conn.prepareCall(query);			
                rs1 = st1.executeQuery();
                
                if (rs1.next()){
                    status.setClassid(rs1.getString("CLASS_ID"));    
                     
                    status.setClasschedule(rs1.getDate("COURSESCHEDULE"));    
                }
                
                
                query = "SELECT update_by, REPLACE(REPLACE(update_reason,chr(10),''),chr(13),'') as update_reason, update_flag"
                        + " FROM RBU_CLASS C,RBU_MANUAL_CLASS_ASSIGNMENT CA where C.CLASS_ID = CA.CLASS_ID AND CA.emplid =" + emplid
                        + " AND C.PRODUCT_CD = '" + status.getProductcd() 
                        + "' order by update_date desc ";
                        
                st1 = conn.prepareCall(query);			
                rs1 = st1.executeQuery();
                           
                if (rs1.next()){
                    status.setMostRecentUpdateFlag(rs1.getString("update_flag"));
                    status.setNotes(rs1.getString("update_reason"));    
                }             
                // Changes made by jeevan/Arpit to show the correct value of Credits in EDP 02/13/2009
                String prod_cd = status.getProductcd();
                if(prod_cd.equalsIgnoreCase("LYRCPC") || prod_cd.equalsIgnoreCase("LYRCSM")){
                    prod_cd = "LYRC";
                }
                if(prod_cd.equalsIgnoreCase("ARCPPC") || prod_cd.equalsIgnoreCase("ARCPSM")){
                    prod_cd = "ARCP";
                }
                if(prod_cd.equalsIgnoreCase("GEODPC") || prod_cd.equalsIgnoreCase("GEODSM")){
                    prod_cd = "GEOD";
                }
               /* query = "select product_cd from V_RBU_CURRENT_PRODUCTS "
                        + " where product_cd = '" + status.getProductcd() + "' and emplid = '" + emplid + "' "
                        + " union "
                        + " select product_cd from rbu_product_credits where product_cd= '" + status.getProductcd()+ "' and emplid = '" + emplid + "' ";*/
               
               query = "select product_cd from V_RBU_CURRENT_PRODUCTS "
                        + " where product_cd = '" + prod_cd + "' and emplid = '" + emplid + "' "
                        + " union "
                        + " select product_cd from rbu_product_credits where product_cd= '" + prod_cd+ "' and emplid = '" + emplid + "' ";
               
                st1 = conn.prepareCall(query);			
                rs1 = st1.executeQuery();
                
                if (rs1.next()){
                    status.setHasCredit(true);
                }else{
                    status.setHasCredit(false);
                }
                
                if(status.isManagerRequired()){
                       query = "select LSO_URL from RBU_PRODUCTS p where p.product_cd = '" + status.getProductcd() + "'";
                        st1 = conn.prepareCall(query);			
                        rs1 = st1.executeQuery();
                        
                        if (rs1.next()){
                            status.setLso(rs1.getString("LSO_URL"));
                        }
                }
                
                rbustatuses.add(status);
            }
            
            
            st.close();
            st1.close();
            conn.close();
        }catch(Exception e){
                log.error(e,e);
                //System.out.println(query);
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
            if ( rs1 != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st1 != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
        return rbustatuses;
    }
    
    public void cancelTraining(String emplid, String adminid, String old, String note){
         CallableStatement proc = null;
		 Connection conn = null;      
          //(EMPL_ID IN VARCHAR, oldclassid in varchar, note in varchar, adminid in varchar)   
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            proc = conn.prepareCall("{call RBU_MANUAL_CLASS_DELETE(?, ?, ?, ?)}");
            proc.setString(1, emplid);
            proc.setString(2, old);
            proc.setString(3, note);
            proc.setString(4, adminid);
            //System.out.println(" oldclassid - " + old + " note - " + note );  
            proc.execute();
            
            
            proc.close();
            conn.close();
        }catch(SQLException e){
                log.error(e,e);
                e.printStackTrace();
        }/*Infosys - Weblogic to Jboss migration changes start here*/
         /*catch(NamingException e){
                log.error(e,e);
                e.printStackTrace();      
        }*/
         /*Infosys - Weblogic to Jboss migration changes end here*/ 
         finally {

			if ( proc != null) {
				try {
					proc.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
    }
    
    public void updateTraining(String emplid, String adminid, String oldclassid, String newclassid, String reason){
         CallableStatement proc = null;
		 Connection conn = null;      
          // (EMPL_ID IN VARCHAR, oldclassid in varchar, newclassid in varchar, adminid in varchar, reason in varchar)
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            //System.out.println(" reason2 - " + reason);
            proc = conn.prepareCall("{call RBU_MANUAL_CLASS_UPDATE(?, ?, ?, ?,?)}");
            proc.setString(1, emplid);
            proc.setString(2, oldclassid);
            proc.setString(3, newclassid);
            proc.setString(4, adminid);
            proc.setString(5, reason);
            //System.out.println(" handler oldclassid - " + oldclassid + " newclassid - " + newclassid + " reason3 - " + reason);  
            
            proc.execute();
            
            
            proc.close();
            conn.close();
        }catch(SQLException e){
                log.error(e,e);
                e.printStackTrace();
        }
        /*Infosys - Weblogic to Jboss migration changes start here*/ 
        /* catch(NamingException e){
                log.error(e,e);
                e.printStackTrace();
        }*/
        /*Infosys - Weblogic to Jboss migration changes end here*/ 
        finally {

			if ( proc != null) {
				try {
					proc.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
    }
     public void addTraining(String emplid, String adminid, String newclassid, String reason){
         CallableStatement proc = null;
		 Connection conn = null;      
          // (EMPL_ID IN VARCHAR, newclassid in varchar, adminid in varchar)IS
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/
            proc = conn.prepareCall("{call RBU_MANUAL_CLASS_ENROLLMENT(?, ?, ?, ?)}");
            proc.setString(1, emplid);
            proc.setString(2, newclassid);
            proc.setString(3, adminid);
            proc.setString(4, reason);

            
            proc.execute();
            
            
            proc.close();
            conn.close();
        }catch(SQLException e){
                log.error(e,e);
                e.printStackTrace();
        }/*Infosys - Weblogic to Jboss migration changes start here*/ 
         /*catch(NamingException e){
                log.error(e,e);
                e.printStackTrace();
        }*/
         /*Infosys - Weblogic to Jboss migration changes end here*/
         finally {

			if ( proc != null) {
				try {
					proc.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
    }
    
            
     public List getRBUClassesbyEmployee(String emplid){
         
         List classes = new ArrayList();
  
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareCall("select emplid, c.product_cd, v.product_desc, c.class_id, start_date, class_name from "
                                + "V_RBU_FUTURE_PRODUCTS v, RBU_CLASS c "
                                + " where c.product_cd = v.product_cd and emplid = "+ emplid
                                + " order by c.product_cd, start_date" );			
			
            rs = st.executeQuery();
            while (rs.next()) {
              RBUClassData data = new RBUClassData();
              data.setCourseID(rs.getString("class_id"));
              data.setCourseName(rs.getString("class_name"));
              data.setProductcd(rs.getString("product_cd"));
              data.setProductdesc(rs.getString("product_desc"));
              data.setStartDate(rs.getDate("start_date"));              
              classes.add(data);                     
            }
            
            st.close();
            conn.close();

        }catch(Exception e){
                log.error(e,e);
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
        return classes;
        }
        
      public List getFuturelassesbyEmployee(String emplid){
         
        List fprods = new ArrayList();
  
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareStatement("SELECT * FROM V_RBU_FUTURE_PRODUCTS v where " 
                    + " emplid =  " +  emplid + " order by product_desc ");
            rs = st.executeQuery();
            
			while (rs.next()) {                
                fprods.add(rs.getString("product_desc"));
			}    
            

            st.close();
            conn.close();
        }catch(Exception e){
                log.error(e,e);
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
        return fprods;
        }
        
        
        public List getGTbyClassId(String classid){
           List gts = new ArrayList();
  
           ResultSet rs = null;
           PreparedStatement st = null;
		   Connection conn = null;      
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareStatement("select * from rbu_guest_trainers where class_id = "  +  classid + " order by first_name ");
            rs = st.executeQuery();
            
			while (rs.next()) {    
                RBUGuestClassData data = new RBUGuestClassData();            
                data.setClassid(classid);
                data.setEmail(rs.getString("email_address"));
                data.setEmplid(rs.getString("emplid"));
                data.setFirstname(rs.getString("first_name"));
                data.setLastname(rs.getString("last_name"));
                data.setNt_domain(rs.getString("NT_DOMAIN"));
                data.setNt_id(rs.getString("NT_ID"));
                data.setEnrolledby(rs.getString("Enrolled_by"));
                gts.add(data);
			}    
            
            st.close();
            conn.close();

        }catch(Exception e){
                log.error(e,e);
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
        return gts;
        }
        
       public void addGuest(RBUGuestClassData guest){
           List gts = new ArrayList();
  
           ResultSet rs = null;
           PreparedStatement st = null;
		   Connection conn = null;   
           guest = getQsafeGuestData(guest);
           
           String sql = "insert into rbu_guest_trainers(CLASS_ID, EMPLID, ENROLLMENT_DATE, ENROLLED_BY, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, NT_DOMAIN, NT_ID) "
                                        + " VALUES(" + guest.getClassid() + ", '" + guest.getEmplid() + "', SYSDATE, " + guest.getEnrolledby() 
                                        + ", '" + guest.getFirstname() + "', '" + guest.getLastname() + "', '" + guest.getEmail() 
                                        +  "', '" + guest.getNt_domain() + "', '" + guest.getNt_id()  + "') ";   
         
           if(guest.getEmplid() == null || guest.getEmplid().length()<1){
            sql = "insert into rbu_guest_trainers(CLASS_ID, EMPLID, ENROLLMENT_DATE, ENROLLED_BY, FIRST_NAME, LAST_NAME, EMAIL_ADDRESS, NT_DOMAIN, NT_ID) "
                                        + " VALUES(" + guest.getClassid() + ", NULL, SYSDATE, " + guest.getEnrolledby() 
                                        + ", '" + guest.getFirstname() + "', '" + guest.getLastname() + "', '" + guest.getEmail() 
                                        +  "', '" + guest.getNt_domain() + "', '" + guest.getNt_id()  + "') ";     
         
           }
           
           
           
              
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            st.close();
            conn.close();
      
        }catch(Exception e){
                log.error(e,e);
                //System.out.println(sql);
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   

        }
        
    public void updateGuest(RBUGuestClassData guest){
  
           ResultSet rs = null;
           PreparedStatement st = null;
		   Connection conn = null;      
           String emplid = guest.getEmplid();
           //System.out.println("updateGuest " + emplid);
           String sql;
            
           guest = getQsafeGuestData(guest);
           
           if(emplid == null || emplid.length()<1){
                sql = "update rbu_guest_trainers set class_id = "+ guest.getClassid() 
                                                + ", ENROLLMENT_DATE = SYSDATE "
                                                + ", ENROLLED_BY = " + guest.getEnrolledby()
                                                + ", FIRST_NAME = '" + guest.getFirstname()
                                                + "', LAST_NAME = '" + guest.getLastname()  
                                                + "', EMAIL_ADDRESS = '" + guest.getEmail()    
                                                + "', NT_DOMAIN = '" + guest.getNt_domain() 
                                                + "', NT_ID = '" + guest.getNt_id()                                         
                                                + "' WHERE EMAIL_ADDRESS = '" + guest.getEmail() + "'";
            }else{
                                   
                    sql = "update rbu_guest_trainers set class_id = "+ guest.getClassid() 
                                                + ", ENROLLMENT_DATE = SYSDATE "
                                                + ", ENROLLED_BY = " + guest.getEnrolledby()
                                                + ", FIRST_NAME = '" + guest.getFirstname()
                                                + "', LAST_NAME = '" + guest.getLastname()
                                                + "', EMAIL_ADDRESS = '" + guest.getEmail()
                                                + "', NT_DOMAIN = '" + guest.getNt_domain() 
                                                + "', NT_ID = '" + guest.getNt_id()  
                                                + "' WHERE EMPLID = " + emplid;
            }                                     

            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareStatement(sql);
            st.executeQuery();
      
            st.close();
            conn.close();
        }catch(Exception e){
                log.error(e,e);
                //System.out.println(sql);
                e.printStackTrace();
                
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   

        }
        
        private RBUGuestClassData getQsafeGuestData(RBUGuestClassData guest){
            RBUGuestClassData safeone = new RBUGuestClassData();
            safeone.setClassid(guest.getClassid());
            safeone.setEmail(guest.getEmail().replaceAll("'", "''"));
            safeone.setFirstname(guest.getFirstname().replaceAll("'", "''"));
            safeone.setLastname(guest.getLastname().replaceAll("'", "''"));
            safeone.setNt_domain(guest.getNt_domain().replaceAll("'", "''"));
            safeone.setNt_id(guest.getNt_id());
            safeone.setEnrolledby(guest.getEnrolledby());
            safeone.setEmplid(guest.getEmplid());
            return safeone;
            
        }
            
        
        public void deleteGuests(String [] guests, String classid){
 
           ResultSet rs = null;
           PreparedStatement st = null;
		   Connection conn = null;      
           String sql = "";
           
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            if(guests != null && guests.length >0){                
                for(int i = 0 ;i <guests.length; i ++){                
                    sql= "delete from  rbu_guest_trainers  WHERE (EMPLID = '" + guests[i].replaceAll("'", "''") 
                            + "' or email_address = '"+ guests[i].replaceAll("'", "''") 
                            + "') and class_id = " + classid;
                    
                    st = conn.prepareStatement(sql);
                    st.executeQuery();
                }
            }

      
            st.close();
            conn.close();
        }catch(Exception e){
                log.error(e,e);
                //System.out.println(sql);
                e.printStackTrace();
                
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   

        }
        
        public String processGuestFile(String classid, String enrolledby,  FileItem fi){
            String returncode = "The Guest Trainers list has been uploaded successfully.";
            
            if(fi == null){
                returncode = "Upload failed.";
            }
            
            try{
                //File fNew= new File(fi.getName());

               // CSVReader reader = new CSVReader(new FileReader(fNew));
                 CSVReader reader = new CSVReader(new InputStreamReader(fi.getInputStream()));
                String [] nextLine;
                nextLine = reader.readNext();
                //process header
                if(nextLine != null){
                   if(nextLine.length <6){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                    }
                   if(!nextLine[0].equalsIgnoreCase("FirstName")){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                   }
                   if(!nextLine[1].equalsIgnoreCase("LastName")){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                   }
                   if(!nextLine[2].equalsIgnoreCase("EMPLID")){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                   }
                   if(!nextLine[3].equalsIgnoreCase("Email")){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                   }
                   if(!nextLine[4].equalsIgnoreCase("NT_DOMAIN")){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                   }
                   if(!nextLine[5].equalsIgnoreCase("NT_ID")){
                        returncode = "Format of the uploaded file is incorrect.";
                        return returncode;
                   }
                }
                while ((nextLine = reader.readNext()) != null) {
                    RBUGuestClassData data = new RBUGuestClassData();
                    // nextLine[] is an array of values from the line
                    
                    if(nextLine.length == 6){ 
                        data.setClassid(classid);                    
                        data.setFirstname(nextLine[0]);
                        data.setLastname(nextLine[1]);
                        data.setEmplid(nextLine[2]);                        
                        data.setEmail(nextLine[3]);
                        data.setNt_domain(nextLine[4]);
                        data.setNt_id(nextLine[5]);
                        data.setEnrolledby(enrolledby);
                        
                        addGuest(data);
                    }

                }
               
            }         
            catch(FileNotFoundException e){
                e.printStackTrace();
            }catch(IOException e){
                e.printStackTrace();
            }
            return returncode;
            
        }
        
        
    /**
     *  
     */        
    public List getGuestTrainingList(String emplid){
        // Added to get the list of Guest training claases for this employee
        List guestTraining = new ArrayList();
        ResultSet rs = null;
		PreparedStatement st = null;
		Connection conn = null;

        
        String sql = " select p.PRODUCT_DESC, c.START_DATE, c.END_DATE  from RBU_GUEST_TRAINERS g, RBU_CLASS c, RBU_PRODUCTS p " +
                     " where g.CLASS_ID = c.CLASS_ID and p.PRODUCT_CD = c.PRODUCT_CD and" + 
                    "  g.EMPLID= " + emplid;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.prepareStatement(sql);		
            rs = st.executeQuery();
            
			while (rs.next()) {
                RBUGuestTrainersClassData guestData = new  RBUGuestTrainersClassData();                   
                if(rs.getString("PRODUCT_DESC") != null){
                    guestData.setProductDesc(rs.getString("PRODUCT_DESC"));
                }
                if(rs.getDate("START_DATE") != null){
                    guestData.setStartDate(rs.getDate("START_DATE"));
                }
                 if(rs.getDate("END_DATE") != null){
                    guestData.setEndDate(rs.getDate("END_DATE"));
                }
                guestTraining.add(guestData);
                
			}
            
            st.close();
            conn.close();
         
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
        return guestTraining;
    }
    
    
    public List getAllGTConflicts(){
               // Added to get the list of Guest training claases for this employee
        List conflicts = new ArrayList();
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;

        
        String sql = " select * from V_RBU_GUEST_CONFLICTS";
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();		
            rs = st.executeQuery(sql);
            
			while (rs.next()) {
                RBUClassData gclass = new RBUClassData();
                RBUClassData tclass = new RBUClassData();
                gclass.setCourseID(rs.getString("G_CLASS_ID"));
                gclass.setStartDate(rs.getDate("G_START_DATE"));
                gclass.setEndDate(rs.getDate("G_END_DATE"));
                gclass.setProductdesc(rs.getString("G_PRODUCT_DESC"));
                
                tclass.setCourseID(rs.getString("T_CLASS_ID"));
                tclass.setStartDate(rs.getDate("T_START_DATE"));
                tclass.setEndDate(rs.getDate("T_END_DATE"));
                tclass.setProductdesc(rs.getString("T_PRODUCT_DESC"));
                
                if(ifConflict(gclass, tclass)){
                    RBUGTConflict data = new RBUGTConflict();
                    data.setEmplid(rs.getString("emplid"));
                    data.setFirstname(rs.getString("firstname"));
                    data.setLastname(rs.getString("lastname"));
                    data.setGclass(gclass);
                    data.setTclass(tclass);
                    conflicts.add(data);
                }              

			}
            st.close();
            conn.close();
         
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
        return conflicts;
    }
    
    private boolean ifConflict(RBUClassData gclass, RBUClassData tclass){
       // boolean ifconflict = false;
        if(gclass.getStartDate() == null || tclass.getStartDate() == null ){
            return false;
        }
        long r1start = gclass.getStartDate().getTime();
        long r2start = tclass.getStartDate().getTime();
        long r1end = gclass.getEndDate().getTime();       
        long r2end = tclass.getEndDate().getTime();
        
        
        return (r1start == r2start) || (r1start > r2start ? r1start <= r2end : r2start <= r1end);
        
       // return ifconflict;
    }
    public List getClassWeeks(){
        List weeks = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select week_id, week_name, start_date, end_date from rbu_training_weeks where week_id in (select distinct week_id from v_rbu_class_roster_report) order by week_id");
            
            while(rs.next()){
                RBUTrainingWeek week = new RBUTrainingWeek();
                week.setWeek_id(rs.getString("week_id"));
                week.setWeek_name(rs.getString("week_name"));
                week.setStart_date(rs.getTimestamp("start_date"));
                week.setEnd_date(rs.getTimestamp("end_date"));
                
                weeks.add(week);
            }            
            
            
            st.close();
            conn.close();
        	
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
        return weeks;
        
    }
    public RBUTrainingWeek getCurrentWeek(){
        RBUTrainingWeek week = null;
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select * from rbu_training_weeks where start_date < sysdate and (end_date +2)> sysdate");
            
            while(rs.next()){
                week = new RBUTrainingWeek();
                week.setWeek_id(rs.getString("week_id"));
                week.setWeek_name(rs.getString("week_name"));
                week.setStart_date(rs.getTimestamp("start_date"));
                week.setEnd_date(rs.getTimestamp("end_date"));

            }            
            
            
            st.close();
            conn.close();
        	
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
        return week;
        
    }
    
    public List getTables(String class_id){
        List tables = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct t.table_id , (SELECT COUNT (emplid) FROM V_RBU_CLASS_ROSTER_REPORT  WHERE table_id = t.table_id and class_id = t.class_id AND IS_TRAINER = 'N') tnt, "
                            + "(SELECT COUNT (emplid) FROM V_RBU_CLASS_ROSTER_REPORT  WHERE table_id = t.table_id  and class_id = t.class_id AND IS_TRAINER = 'Y') gnt from rbu_class_classroom_table_map t where t.class_id = " + class_id + " order by t.table_id ");
            
            while(rs.next()){
                
                RBUClassTable table = new RBUClassTable();
                table.setTalbe_id(rs.getString("table_id"));
                table.setTraineesCnt(rs.getInt("tnt"));
                table.setGuestCnt(rs.getInt("gnt"));
                
                tables.add(table);
            }            
            
            st.close();
            conn.close();
            
        	
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
        return tables;
        
    }
    
    public List getRooms(String class_id){
        List rooms = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select distinct m.room_id, room_name,(select count(table_id) from rbu_class_classroom_table_map where class_id =" + class_id+ " and room_id = m.room_id) tablecnt "
                        +"from rbu_class_classroom_map m, rbu_classroom r "
                    + " where r.ROOM_ID = m.ROOM_ID and class_id = " + class_id  + " group by m.room_id, room_name order by m.room_id");
            while(rs.next()){
                RBUClassRoom room = new RBUClassRoom();
                room.setRoom_id(rs.getString("room_id"));
                room.setRoom_name(rs.getString("room_name"));
                room.setAssignedtalbes(rs.getInt("tablecnt"));
                rooms.add(room);               
                
            }            
            
            st.close();
            conn.close();
            
        	
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
        return rooms;
        
    }
    public List getEmpListByTalbe(String class_id, String table_id){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select EMPLID, FIRST_NAME, LAST_NAME, FUTURE_ROLE from V_RBU_CLASS_ROSTER_REPORT where table_id =" 
                            + table_id  + " and class_id = " + class_id + "  and IS_TRAINER = 'N' ");
            
            while(rs.next()){
                RBUTrainee t = new RBUTrainee();
                t.setEmplId(rs.getString("emplid"));
                t.setFirstName(rs.getString("first_name"));
                t.setLastName(rs.getString("LAST_NAME"));
                t.setRole(rs.getString("FUTURE_ROLE"));
                ts.add(t);
            }            
            st.close();
            conn.close();
        	
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
        return ts;
        
    }
    
    public List getUnassignedEmployees(String week_id){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
        ResultSet rs1 = null;
		Statement st1 = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select EMPLID, FIRST_NAME, LAST_NAME, FUTURE_ROLE, CLASS_ID, PRODUCT_DESC, IS_TRAINER from V_RBU_UNASSIGNED_REPORT where WEEK_ID =" 
                            + week_id  + " ");
                            
             st1 = conn.createStatement();	
            
            rs1 = st1.executeQuery("select distinct CLASS_ID  from V_RBU_UNASSIGNED_REPORT where WEEK_ID =" 
                            + week_id  + " ");
             HashMap result = new LinkedHashMap();
             // Get the tables for each distinct class_id returned by the view
             while(rs1.next()){
                String classId = rs1.getString("CLASS_ID");
                List classTables = getTables(classId);
                result.put(classId, classTables);
             }              
            
            while(rs.next()){
                RBUUnassignedEmployees t = new RBUUnassignedEmployees();
                t.setEmplId(rs.getString("EMPLID"));
                t.setFirstName(rs.getString("FIRST_NAME"));
                t.setLastName(rs.getString("LAST_NAME"));
                t.setFutureRole(rs.getString("FUTURE_ROLE"));
                t.setProductDesc(rs.getString("PRODUCT_DESC"));
                t.setIsTrainer(rs.getString("IS_TRAINER"));
                // Get the list of tables available for this classId
                List tablesForClasses = new ArrayList();
                String classId = rs.getString("CLASS_ID");
                if(result.containsKey(classId)){
                    tablesForClasses  = (List)result.get(classId);
                }
                t.setClassId(classId);
               // tablesForClasses = getTables(classId);
                t.setTablesForClassId(tablesForClasses);
                ts.add(t);
            }            
            st.close();
            conn.close();
        	
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
            if ( rs1 != null) {
				try {
					rs1.close();
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
            if ( st1 != null) {
				try {
					st1.close();
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
        return ts;
        
    }
    
     public void updateTable(Map gMap, Map tMap, String class_id, String assigned_by, String room_id, String table){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            String sql = "";
            Iterator keyValuePairs1 = tMap.entrySet().iterator();
            for (int i = 0; i < tMap.size(); i++)
            {
                 Map.Entry entry = (Map.Entry) keyValuePairs1.next();
                 String tid = (String)entry.getKey();
                 String table_id =(String) entry.getValue();
                 sql = "UPDATE RBU_CLASS_TRAINEE_TABLE_MAP set TABLE_ID = " + table_id +
                        ", ASSIGNED_BY ='" + assigned_by +"' WHERE CLASS_ID = "+ class_id + " AND EMPLID = '" + tid + "'" ;
                 //System.out.println(sql);
                 st.executeUpdate(sql);
            }
            
            
            Iterator keyValuePairs2 = gMap.entrySet().iterator();
            for (int i = 0; i < gMap.size(); i++)
            {
                 Map.Entry entry = (Map.Entry) keyValuePairs2.next();
                 String gid = (String)entry.getKey();
                 String table_id =(String) entry.getValue();
                 sql = "UPDATE RBU_CLASS_GT_TABLE_MAP set TABLE_ID = " + table_id +
                        ", ASSIGNED_BY ='" + assigned_by +"' WHERE CLASS_ID = "+ class_id + " AND EMPLID = '" + gid + "'" ;
                 //System.out.println(sql);
                 st.executeUpdate(sql);
            }
            if(table != null && room_id != null && class_id != null){
                sql = "UPDATE rbu_class_classroom_table_map  set ROOM_ID = "+ room_id + " WHERE TABLE_ID = " 
                       +  table + "AND CLASS_ID =  " + class_id;
                st.executeUpdate(sql);
            }
        	st.close();
            conn.close();
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
        
        
    }
    
     public void assignEmployeesToTables(List employeesToUpdate, List guestTrainersToUpdate, String assigned_by){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            String sql = "";
            
            Iterator iter = employeesToUpdate.iterator();
            while(iter.hasNext())
            {
                 RBUUnassignedEmployees employees = (RBUUnassignedEmployees)iter.next();
                 String emplid = employees.getEmplId();
                 String tableId = employees.getTableId();
                 String classId = employees.getClassId();
                 sql = "INSERT INTO RBU_CLASS_TRAINEE_TABLE_MAP ( CLASS_ID, EMPLID, TABLE_ID, ASSIGNED_BY,ASSIGNED_DATE )" +
                       " values ('" + classId  + "','" + emplid + "', '" + tableId + "','" + assigned_by + "', sysdate) ";
                 //System.out.println("############## Assign employees " + sql);
                 st.executeUpdate(sql);
            }
            Iterator iter2 = guestTrainersToUpdate.iterator();
            while(iter2.hasNext())
            {
                 RBUUnassignedEmployees employees = (RBUUnassignedEmployees)iter2.next();
                 String emplid = employees.getEmplId();
                 String tableId = employees.getTableId();
                 String classId = employees.getClassId();
                 sql = "INSERT INTO RBU_CLASS_GT_TABLE_MAP ( CLASS_ID, EMPLID, TABLE_ID, ASSIGNED_BY,ASSIGNED_DATE )" +
                       " values ('" + classId  + "','" + emplid + "', '" + tableId + "','" + assigned_by + "', sysdate) ";
                 //System.out.println("############## Assign guest trainers " + sql);
                 st.executeUpdate(sql);
            }
            
        	st.close();
            conn.close();
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
        
        
    }
    
    public List getGuestListByTable(String class_id, String table_id){
        List ts = new ArrayList();
        
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            
            rs = st.executeQuery("select EMPLID, FIRST_NAME, LAST_NAME, FUTURE_ROLE from V_RBU_CLASS_ROSTER_REPORT where table_id =" 
                            + table_id  + " and class_id = " + class_id + " and IS_TRAINER = 'Y'");
            
            while(rs.next()){
                RBUTrainee t = new RBUTrainee();
                t.setEmplId(rs.getString("EMPLID"));
                t.setFirstName(rs.getString("first_name"));
                t.setLastName(rs.getString("LAST_NAME"));
                t.setRole(rs.getString("FUTURE_ROLE"));
                ts.add(t);
            }            
            st.close();
            conn.close();
        	
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
        return ts;
        
    }
    
     public List getClassRooms(String week_id){
               // Added to get the list of Guest training claases for this employee
        List roomvos = new ArrayList(); // a list of roomdata of RBURoomGridVO
        
        SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a"); 
        
        SimpleDateFormat dateformat1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"); 
        
        

        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;

        ResultSet rs1 = null;
		Statement st1 = null;
        
        String sql = "select distinct room_id, room_name, week.start_date from v_rbu_class_table v, rbu_training_weeks week  "
                     + " where v.start_date >= week.start_date and v.end_date <= week.end_date and week_id =  " + week_id
                     + " order by room_id ";

        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();		
            st1 = conn.createStatement();	
            rs = st.executeQuery(sql);


            //convert relational rows to object hierarchy       
            //Step1. find all rooms for this week 
			while (rs.next()) {
                RBURoomGridVO gvo = new RBURoomGridVO();
                gvo.setRoom_id(rs.getString("room_id"));
                gvo.setRoom_name(rs.getString("room_name"));
                gvo.setWeek_id(week_id);       
                
                Calendar calendar = GregorianCalendar.getInstance();
                calendar.setFirstDayOfWeek(GregorianCalendar.MONDAY);
                calendar.setTime(rs.getTimestamp("start_date")); 
                
                
                Calendar calendar2 = GregorianCalendar.getInstance();
                calendar2.setFirstDayOfWeek(GregorianCalendar.MONDAY);
                calendar2.setTime(calendar.getTime()); 
                calendar2.roll(Calendar.HOUR_OF_DAY, 12);
        
                Date [] weekdays = new Date [5];
                Date [] weekdays2 = new Date [5];
                
       
                for(int i = 0; i < weekdays.length; i ++){
                    weekdays [i] = calendar.getTime();
                    weekdays2 [i] = calendar2.getTime();
                    ////System.out.println("date1 " + dateformat1.format(calendar.getTime()));
                    calendar.roll(Calendar.DAY_OF_MONTH, 1);
                    calendar2.roll(Calendar.DAY_OF_MONTH, 1);
                }
                
                
                List roomdatas = new ArrayList();

                //Step2. get data for each room
                        //for each weekday, find classroom data 
                        //for each classroom data, find out table info
                for(int i = 0; i < weekdays.length; i ++){
                    sql = " select distinct class_id class_id, product_cd, product_desc, table_id, t_cnt, g_cnt,v.start_date  from v_rbu_class_table v "
                         + " where TO_DATE('" + dateformat.format(weekdays2[i]) +  "', 'mm/dd/yyyy hh:mi:ss am') >= v.start_date " 
                         + " AND TO_DATE('" + dateformat.format(weekdays[i]) +  "', 'mm/dd/yyyy hh:mi:ss am') <= v.end_date "                        
                         + " and room_id = '" + gvo.getRoom_id() + "' "
                         + " order by v.start_date, product_desc, table_id";
                    //System.out.println(sql);
                    rs1 = st1.executeQuery(sql);  
                    
                    RBUClassRoom roomdata = new RBUClassRoom();
                    List rbuclasses = new ArrayList();
                    RBUClassData rbuclass = new RBUClassData();
                    List tables = new ArrayList();   
                    RBUClassTable table = new RBUClassTable();            
                    //RBUClassData rbuclass2 = null;
                    //List tables2 = new ArrayList();
                    
                    if(rs1.next()){
                         roomdata.setRoom_id(gvo.getRoom_id());
                         roomdata.setRoom_name(gvo.getRoom_name());
                         roomdata.setWeekday(getWeekDay(i));  
                         roomdata.setDay(weekdays[i]);
                         
                        // RBUClassData rbuclass = new RBUClassData();
                         rbuclass.setCourseID(rs1.getString("class_id"));
                         rbuclass.setProductcd(rs1.getString("product_cd"));
                         rbuclass.setProductdesc(rs1.getString("product_desc"));                                            
                         
                                                 
                         
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("t_cnt"));
                         table.setGuestCnt(rs1.getInt("g_cnt"));   
                         tables.add(table);
                    }

                    while(rs1.next()){
                        if(rs1.getString("class_id").equals(rbuclass.getCourseID())){
                         table = new RBUClassTable();
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("t_cnt"));
                         table.setGuestCnt(rs1.getInt("g_cnt"));   
                         tables.add(table);    
                        }else{
                          //when new class found - add current class to list, reset it for new class
                          rbuclass.setTables(tables);
                          rbuclasses.add(rbuclass);
                          
                          rbuclass = new RBUClassData();
                          tables = new ArrayList();
                                                
                          rbuclass.setCourseID(rs1.getString("class_id"));
                          rbuclass.setProductcd(rs1.getString("product_cd"));
                          rbuclass.setProductdesc(rs1.getString("product_desc"));                                            
                        //System.out.println(rs1.getString("product_desc"));
                          table = new RBUClassTable();
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("t_cnt"));
                         table.setGuestCnt(rs1.getInt("g_cnt"));   
                         tables.add(table);
                        //System.out.println(table2.getTalbe_id());    
                         //   break;
                          
                        }
                    }    
                      
                   rbuclass.setTables(tables);
                   rbuclasses.add(rbuclass);
                   roomdata.setRbuClasses(rbuclasses);
                   
                   
    //                roomdata.setRBUClass(rbuclass);                        


                    /* 
                     while(rs1.next()){
                         RBUClassTable table = new RBUClassTable();
                         table.setTalbe_id(rs1.getString("table_id"));
                         table.setTraineesCnt(rs1.getInt("t_cnt"));
                         table.setGuestCnt(rs1.getInt("g_cnt"));   
                         tables2.add(table);    
                    }  
                    
                    if(rbuclass2 !=null){
                        rbuclass2.setTables(tables2);
                        roomdata.setRBUClass2(rbuclass2);
                         System.out.println(tables2.size());   
                    }
                    */
                    
                    
                   // roomdata.setTables(tables);
                    roomdatas.add(roomdata);
                }
                
                gvo.setRoomdata(roomdatas);                     
                roomvos.add(gvo);
			}

            st.close();
            st1.close();
         
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
            if ( rs1 != null) {
				try {
					rs1.close();
				} catch ( Exception e2) {
					e2.printStackTrace();
				}
			}
			if ( st1 != null) {
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
        return roomvos;
    }
    
   public void deleteGTByTable(String class_id, String table_id, String gid){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "delete from RBU_CLASS_GT_TABLE_MAP where class_id = " + class_id + " and EMPLID = '" + gid + "'";
            //System.out.println(sql);
            st.execute(sql);
           
            st.close();
            conn.close();
        	
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
        
    }
    
    private String getWeekDay(int i){
        switch (i){
           case 0: return RBUClassRoom.MONDAY;
           case 1: return RBUClassRoom.TUESDAY;
           case 2: return RBUClassRoom.WENSEDAY;
           case 3: return RBUClassRoom.THURSDAY;
           case 4: return RBUClassRoom.FRIDAY;
           default: return RBUClassRoom.MONDAY;
        }
    }
    
    public List getEmployeeToviazLaunchStatus(String emplId){
        List result = new ArrayList();
        String IS_RIGISTERED_ATTENDANCE = "isRegisteredAttendance";
        String IS_RIGISTERED_CONFERENCE = "isRegisteredLaunch";
        String IS_ON_LEAVE = "onLeave";
        String IS_COMPLETED_ATTENDANCE = "completedAttendance";
        String IS_COMPLETED_CONFERENCE = "completedConference";
        String IS_RIGISTERED_COMPLIANCE = "isRegisteredCompliance";
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "SELECT a.STATUS, b.ACTIVITY_TYPE, b.ACTIVITY_NAME from TOVIAZ_LAUNCH_ATTENDANCE a, TOVIAZ_LAUNCH_P2L_CODES b"+
                        " where a.TOVIAZ_LAUNCH_CODE = b.P2L_COURSE_CODE and EMPLID = '" + emplId + "'";
            //System.out.println("Query #################"+  sql);
            rs =  st.executeQuery(sql);
            boolean hasRecords = false;
            while(rs.next()){
                hasRecords = true;
                int status = rs.getInt("STATUS");
                String activityType = rs.getString("ACTIVITY_TYPE");
                String activityName = rs.getString("ACTIVITY_NAME");
               /* if(status == 0 && activityType != null && activityType.equals("ATT")){
                    result.add(IS_RIGISTERED_ATTENDANCE);
                }*/
                if(status == 4 && activityType != null && activityType.equals("ATT")){
                    result.add(IS_COMPLETED_ATTENDANCE);
                }
                if(status == 4 && activityType == null && activityName.equals("Toviaz Manager Confirmation Post Launch Meeting Training")){
                    result.add(IS_COMPLETED_CONFERENCE);
                }
                if(status == 0 && activityType == null && activityName.equals("Toviaz National Launch Meeting Compliance Breeze")){
                    result.add(IS_RIGISTERED_COMPLIANCE);
                }
            }
            // Get the employee status
            String status =  getEmployeeStatus(emplId);
            if(!hasRecords && status.equals("On-Leave")){
                // No records so this employee is on leave
                result.add(IS_ON_LEAVE);
            }
            else{
                if(result.size() == 0){ 
                    result.add(IS_RIGISTERED_ATTENDANCE);
                }
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
        
        
        return result;
    }
    
    
    public void updateToviazLaunchAttComplete(String emplid, String actionBy){
        
         ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        // Get the P2L code for attendance 
        String p2lCourseCode = getP2LCourseCode("Toviaz National Launch Meeting Attendance");
        //System.out.println("Course code for attendance is  " + p2lCourseCode);
        String completionDate = getTodaysDate();
        String guid = getEmplNumber(emplid);
        //System.out.println("#############Course code for attendance is  " + p2lCourseCode + "Completion Date " + completionDate + "GUID ########## " + guid) ;
        // Insert into Toviaz attendance table
         try {
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();
            	
            String sql = "INSERT INTO TOVIAZ_LAUNCH_ATTENDANCE( EMPLID, TOVIAZ_LAUNCH_CODE, REGISTRATION_DATE, COMPLETION_DATE, SCORE, PASSED," +
                         " STATUS, CREATED_DATE, EMP_NUMBER, CANCELLATION_DATE, NOTES, ACTION_BY )" + 
                         " VALUES( "+
                         "'" + emplid + "', '" + p2lCourseCode + "', null,'" + completionDate + "',null,null,4,sysdate,'" + guid + "',null,null, '" + actionBy + "') ";
            //System.out.println(sql);
            st.execute(sql);
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
        
    }
    
    public void updateToviazLaunchManagerCertification(String emplid, String actionBy){
        
         ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        // Get the P2L code for has attended 
        String p2lCourseCodePostLaunch = getP2LCourseCode("Toviaz Manager Confirmation Post Launch Meeting Training");
      //  System.out.println("Course code for attendance is  " + p2lCourseCode);
        String completionDate = getTodaysDate();
        String guid = getEmplNumber(emplid);
        //System.out.println("#############Course code for attendance is  " + p2lCourseCodePostLaunch + "Completion Date " + completionDate + "GUID ########## " + guid) ;
        // Insert into Toviaz attendance table
         try {
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();
            String sql2 = "INSERT INTO TOVIAZ_LAUNCH_ATTENDANCE( EMPLID, TOVIAZ_LAUNCH_CODE, REGISTRATION_DATE, COMPLETION_DATE, SCORE, PASSED," +
                         " STATUS, CREATED_DATE, EMP_NUMBER, CANCELLATION_DATE, NOTES,ACTION_BY )" + 
                         " VALUES( "+
                         "'" + emplid + "', '" + p2lCourseCodePostLaunch + "', null,'" + completionDate + "',null,null,4,sysdate,'" + guid + "',null,null,'" + actionBy + "') ";
            //System.out.println(sql2);
            st.execute(sql2);
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
        
    }
    
    public void updateRegistrationForBreezeCompliance(String emplid, String actionBy){
        
         ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        // Get the P2L code for has attended 
        String p2lCourseCodeCompliance = getP2LCourseCode("Toviaz National Launch Meeting Compliance Breeze");
      //  System.out.println("Course code for attendance is  " + p2lCourseCode);
        String completionDate = getTodaysDate();
        String guid = getEmplNumber(emplid);
        // Insert into Toviaz attendance table
         try {
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();
            String sql3 = "INSERT INTO TOVIAZ_LAUNCH_ATTENDANCE( EMPLID, TOVIAZ_LAUNCH_CODE, REGISTRATION_DATE, COMPLETION_DATE, SCORE, PASSED," +
                         " STATUS, CREATED_DATE, EMP_NUMBER, CANCELLATION_DATE, NOTES, ACTION_BY)" + 
                         " VALUES( "+
                         "'" + emplid + "', '" + p2lCourseCodeCompliance + "','" + completionDate + "',null,null,null,0,sysdate,'" + guid + "',null,null,'" + actionBy + "') ";
            //System.out.println(sql3);
            st.execute(sql3);
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
        
    }
    
    private String getTodaysDate(){
        
        String today = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        today = dateFormat.format(now);
        return today;
    }
    
    public String getP2LCourseCode(String activityName){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "SELECT P2L_COURSE_CODE from TOVIAZ_LAUNCH_P2L_CODES where ACTIVITY_NAME= '" + activityName + "'";
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = rs.getString("P2L_COURSE_CODE");
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
        return result;
        
    }
    
     public String getComplianceStatus(String emplid){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "SELECT STATUS from V_TOVIAZ_COMPLIANCE_STATUS where emplid= '" + emplid + "'";
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = rs.getString("STATUS");
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
        return result;
        
    }
    
    public String getEmployeeStatus(String emplId){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "SELECT STATUS from V_RBU_FIELD_EMPLOYEE where emplid= '" + emplId + "'";
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = rs.getString("STATUS");
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
        return result;
        
    }
    
     public String getEmployeeTrainingStatus(String emplId){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        boolean found = false;
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();
          /*  String sqlQuery1 = "SELECT DISTINCT data.EMPLID as EMPLID"+
                             " FROM   MV_RBU_PED_SCE_DATA data, V_TOVIAZ_ATTENDANCE_DATA att "+
                             " WHERE data.STATUS = 'C' "+
                             " and data.product_cd in('HSLTOVZ','OABTOVZ') and data.EXAM_TYPE= 'PED' " +
                             " AND 0 = (SELECT COUNT (*)  FROM mv_rbu_ped_sce_data  WHERE emplid = data.emplid"+
                             " AND product_cd = data.product_cd  and data.EXAM_TYPE= 'PED' AND status IN ('NC', 'L')) "+
                             " and att.OVERALL_STATUS='C' and att.EMPLID = data.EMPLID and data.emplid ='"+emplId+"'";*/
            String sqlQuery1 = "SELECT OVERALL from MV_TOVIAZ_LAUNCH_STATUS where emplid = '"+emplId+"'";
            //System.out.println(sql);
            rs = st.executeQuery(sqlQuery1);
            
            while(rs.next()){
                 result = rs.getString("OVERALL");
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
        return result;
        
    }
    
    public String getEmplNumber(String emplId){
        //TODO talbe id is not used, delete from class
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "SELECT GUID from V_RBU_LIVE_FEED where EMPLID= '" + emplId + "'";
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                result = rs.getString("GUID");
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
        return result;
        
    }
    
     public List getToviazExams(String activityName){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        List result = new ArrayList();
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "select distinct EXAM_DISPLAY_NAME from RBU_P2L_CODES where product_cd in('HSLTOVZ','OABTOVZ') and ACTIVITY_TYPE='PED'";
            String whereClause = "";
            if(!activityName.equals("")){
                whereClause = "and ACTIVITY_NAME = '"+activityName+"'";
            }
            String orderBy = "order by EXAM_DISPLAY_NAME asc ";           
            sql  = sql + whereClause + orderBy;
            //System.out.println("Query to get exams names   " + sql);
           
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
               // String code = rs.getString("P2L_COURSE_CODE");
                String activity = rs.getString("EXAM_DISPLAY_NAME");
                //if(!result.containsKey(code)){
                    result.add(activity);
                //}
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
        return result;
        
    }
    
     public String getExamName(String activityName){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "select distinct ACTIVITY_NAME from RBU_P2L_CODES where product_cd in('HSLTOVZ','OABTOVZ') and EXAM_DISPLAY_NAME ='"+activityName+"'";
            String whereClause = "";
           //System.out.println("Query to get exams names   " + sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                 result = rs.getString("ACTIVITY_NAME");
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
        return result;
        
    }
    
     public List getToviazExamsForEDP(String activityName){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        List result = new ArrayList();
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "select DISTINCT EXAM_DISPLAY_NAME  from RBU_P2L_CODES where product_cd in('HSLTOVZ','OABTOVZ') and ACTIVITY_TYPE in 'PED'";
            String whereClause = "";
            if(!activityName.equals("")){
                whereClause = "and EXAM_DISPLAY_NAME = '"+activityName+"'";
            }
            String orderBy = "order by EXAM_DISPLAY_NAME asc ";           
            sql  = sql + whereClause + orderBy;
            System.out.println("Query to get exams names   " + sql);
           
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                //String code = rs.getString("P2L_COURSE_CODE");
                String activity = rs.getString("EXAM_DISPLAY_NAME");
                //if(!result.containsKey(code)){
                    result.add(activity);
                //}
            }
            if(activityName.equals("")){
                // Add Attendance column for overall employee list
                result.add("Attendance");
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
        return result;
        
    }
    
    
     public List getLinksForProducts(String productCd){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        List result = new ArrayList();
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "select DISPLAY_NAME,URL, to_char(DISPLAY_ORDER) as DISPLAY_ORDER from RBU_PRODUCT_DOCS where PRODUCT_CD = '"+productCd+"' order BY DISPLAY_ORDER ASC";
            //System.out.println("Query to get exams names   " + sql);
           
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                RBUProductDocsLinks links = new RBUProductDocsLinks();
                if(rs.getString("DISPLAY_NAME") != null){
                    String displayName = rs.getString("DISPLAY_NAME");
                    links.setDisplayName(displayName);
                }
                if(rs.getString("URL") != null){
                    String url = rs.getString("URL");
                    links.setUrl(url);
                }
                if(rs.getString("DISPLAY_ORDER") != null){
                    String displayOrder = rs.getString("DISPLAY_ORDER");
                    links.setDisplayOrder(displayOrder);
                }
                links.setProductCd(productCd);
                result.add(links);
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
        return result;
        
    }
    public List getToviazExamsStatus(String emplId, String type, String section, String examType){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        List result = new ArrayList();
        if(section.equalsIgnoreCase("Not Complete")) {
            section="NC";
        }
        else
        if(section.equalsIgnoreCase("On Leave")) {
            section="L";
        }
        else
        if(section.equalsIgnoreCase("Complete")) {
            section="C";
        }
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();
            String sql = "";
           /* if(type.equals("Attendance")){
                if(examType.equals("conferenceCompleted")){	
                 sql = " select distinct codes.P2L_COURSE_CODE as COURSE_CODE, codes.ACTIVITY_NAME as EXAM_NAME, "+
                       " DECODE(att.STATUS, 'C' , 'Complete', 'NC', 'Registered', 'L', 'On-Leave', att.STATUS) as STATUS from TOVIAZ_LAUNCH_P2L_CODES codes,"+
                       " V_TOVIAZ_ATTENDANCE_DATA att where att.emplid = '"+emplId+"' and att.STATUS='"+section+"'"+
                       " and att.activity_name = codes.activity_name and att.activity_name ='Toviaz Manager Confirmation Post Launch Meeting Training'"+
                       " UNION"+
                       " select distinct codes.P2L_COURSE_CODE as COURSE_CODE, codes.ACTIVITY_NAME as EXAM_NAME, "+
                       " CASE"+
                       " WHEN 0 < (select count(*) from V_TOVIAZ_COMPLIANCE_STATUS where emplid = att.emplid) "+
                       " THEN 'Completed'"+
                       " WHEN att.OVERALL_STATUS='L'"+
                       " THEN 'On-Leave'"+
                       " WHEN att.OVERALL_STATUS='NC'"+
                       " THEN 'Registered'"+
                       " END"+
                       " as STATUS "+
                       " from TOVIAZ_LAUNCH_P2L_CODES codes, V_TOVIAZ_ATTENDANCE_DATA att where att.emplid = '"+emplId+"' and att.OVERALL_STATUS='"+section+"'"+
                       " and att.activity_name = codes.activity_name and att.activity_name ='Toviaz National Launch Meeting Compliance Breeze'";
                }
                else{
                sql=  "  select distinct codes.P2L_COURSE_CODE as COURSE_CODE, codes.ACTIVITY_NAME as EXAM_NAME, "+
                       " CASE"+
                       " WHEN 0 < (select count(*) from V_TOVIAZ_COMPLIANCE_STATUS where emplid = att.emplid) "+
                       " THEN 'Completed'"+
                       " WHEN att.OVERALL_STATUS='L'"+
                       " THEN 'On-Leave'"+
                       " WHEN att.OVERALL_STATUS='NC'"+
                       " THEN 'Registered'"+
                       " END"+
                       " as STATUS "+
                       " from TOVIAZ_LAUNCH_P2L_CODES codes, V_TOVIAZ_ATTENDANCE_DATA att where att.emplid = '"+emplId+"' and att.OVERALL_STATUS='"+section+"'"+
                       " and att.activity_name = codes.activity_name and att.activity_name ='Toviaz National Launch Meeting Compliance Breeze'";
                }
            }*/
             sql = "select distinct p2l.P2L_COURSE_CODE AS COURSE_CODE, rbu.EXAM_NAME as EXAM_NAME," +  
             " DECODE(rbu.STATUS, 'C' , 'Complete', 'NC', 'Not Complete', 'L', 'On-Leave', rbu.STATUS) as STATUS "+
             " from MV_RBU_PED_SCE_DATA rbu, RBU_P2L_CODES p2l  where rbu.product_cd in('HSLTOVZ','OABTOVZ') and rbu.EXAM_TYPE='PED'"+
             " and rbu.emplid = '"+emplId+"' and rbu.EXAM_NAME = p2l.ACTIVITY_NAME and rbu.product_cd  = p2l.product_cd";
            
            //System.out.println("Query to get exams names   " + sql);
           
            //System.out.println(sql);
            rs = st.executeQuery(sql);
            while(rs.next()){
                ToviazLaunchExamStatus exam = new ToviazLaunchExamStatus();
                String code = rs.getString("COURSE_CODE");
                String activity = rs.getString("EXAM_NAME");
                if(activity.equals("Toviaz (Pedagogue) Exam 1")){
                    activity = "Pedagogue Exam 1";
                }
                if(activity.equals("Toviaz (Pedagogue) Exam 2")){
                    activity = "Pedagogue Exam 2";
                }
                String status = rs.getString("STATUS");
                exam.setExamCode(code);
                exam.setExamName(activity);
                exam.setExamStatus(status);
                result.add(exam);
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
        return result;
        
    }
    
     public String getNTIdExistance(String emplid){
        ResultSet rs = null;
		Statement st = null;
		Connection conn = null;
        String result = "";
        try {
        	/*Infosys - Weblogic to Jboss migration changes start here*/
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
            conn =   ds.getConnection();*/
        	conn = JdbcConnectionUtil.getJdbcConnection();
        	/*Infosys - Weblogic to Jboss migration changes end here*/                     
            st = conn.createStatement();	
            String sql = "select EMPLID from V_RBU_LIVE_FEED where emplid ='"+emplid+"'";
           // System.out.println("Query togetNTIdExistance from live feed ##############   " + sql);
            rs = st.executeQuery(sql);
            boolean found = false;
            while(rs.next()){
                found = true;
                 result = rs.getString("EMPLID");
            }
           if(!found){
                // Check in the user access
                sql = "select EMPLID from USER_ACCESS where emplid ='"+emplid+"'";
               // System.out.println("Query togetNTIdExistance from user access ##############   " + sql);
                 rs = st.executeQuery(sql);
                 while(rs.next()){
                 found = true;
                 result = "";
            }
           } 
           if(!found){
                // The user is not present anywhere !!!
                result = "NF";
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
        return result;
        
    }    

     public String getHeaderString(){
         
         String text = "";
  
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
        	 /*Infosys - Weblogic to Jboss migration changes start here*/
             /*Context ctx = new InitialContext();
 			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
             conn =   ds.getConnection();*/
         	conn = JdbcConnectionUtil.getJdbcConnection();
         	/*Infosys - Weblogic to Jboss migration changes end here*/  
            st = conn.prepareCall("select PARAM_VALUE from configuration where param_name = 'PSCPT Banner Text'");	
			
            rs = st.executeQuery();
            
            while (rs.next()) {
                text = rs.getString("PARAM_VALUE");
                if (Util.isEmpty(text)) {
                    text = "";
                }
            }   
            
            st.close();
            conn.close();

        }catch(Exception e){
                log.error(e,e);
                e.printStackTrace();
        }finally {
			if ( rs != null) {
				try {
					rs.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( st != null) {
				try {
					st.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
			if ( conn != null) {
				try {
					conn.close();
				} catch ( Exception e2) {
					log.error(e2,e2);
				}
			}
		}   
        return text;
        }
   
} 
