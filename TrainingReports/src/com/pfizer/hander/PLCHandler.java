package com.pfizer.hander; 

import com.pfizer.utils.HibernateUtils;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.wc.POA.POAChartBean;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class PLCHandler 
{ 
    protected static final Log log = LogFactory.getLog(PLCHandler.class);
                
    public PLCHandler(){
    }
    //Compatible with old version PDF-PLC    
    public int getOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
        return getOverAllTotalCount(productCd,areaCd,regionCd,districtId,teamCd,"V_PWRA_PLC_DATA_OVERALL");        
    }
    //Compatible with old version SPF-PLC
    public int getOverAllTotalCountSPF(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
        return getOverAllTotalCount(productCd,areaCd,regionCd,districtId,teamCd,"V_SPF_PLC_DATA_OVERALL");        
        //return getOverAllTotalCount(productCd,areaCd,regionCd,districtId,teamCd,"V_SPF_PLC_DATA_OVERALL");        
    }    
    private int getOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId,String teamCd,String tableName) {
        String sqlQuery1="SELECT COUNT(DISTINCT EMPLID) COUNT"+
                             " FROM "+tableName+
                             " WHERE OVERALL_STATUS IN ('L','I','P') ";                
        //ConditionQuery
        String condQuery="";
            if(!"ALL".equalsIgnoreCase(areaCd)){
             condQuery=condQuery+ "AND AREA_CD='"+areaCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(teamCd)  || "".equalsIgnoreCase(teamCd.trim()) ){
             condQuery=condQuery+ "AND TEAM_CD='"+teamCd+"' ";
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
        
       /* private int getCount(String query){
         int total=0;
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  
            conn.createStatement().executeUpdate("alter session set NLS_DATE_FORMAT = 'DD-MON-RR'");                         
            st = conn.prepareCall(query);			
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                total=rs.getInt("COUNT");        
            }
        }catch(Exception e){
            e.printStackTrace();
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
       
        */
    private int getCount(String query){
        int total=0;
        ResultSet rs = null;
        PreparedStatement st = null;
		 Connection conn = JdbcConnectionUtil.getJdbcConnection();        
           
        try{
            
           conn.createStatement().executeUpdate("alter session set NLS_DATE_FORMAT = 'DD-MON-RR'");                         
           st = conn.prepareStatement(query);			
			st.setFetchSize(5000);
           rs = st.executeQuery();
           while (rs.next()) {
               total=rs.getInt("COUNT");        
           }
       }catch(Exception e){
           e.printStackTrace();
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
        
        
        
        public POAChartBean[] getFilteredChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            return getFilteredChart(productCd,areaCd,regionCd,districtId,teamCd,"V_PWRA_PLC_DATA");
        }
        public POAChartBean[] getFilteredChartSPF(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            return getFilteredChart(productCd,areaCd,regionCd,districtId,teamCd,"V_SPF_PLC_DATA");
        }        
        
        private POAChartBean[] getFilteredChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd,String tableName){
            String sqlCompleteQuery ="SELECT 'Complete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS = 'P' AND PRODUCT_CD='"+productCd+"' ";
            String sqlInCompleteQuery ="SELECT 'InComplete' COURSESTATUS,COUNT(DISTINCT EMPLID) TOTAL FROM  "+tableName+" WHERE STATUS='I' AND PRODUCT_CD='"+productCd+"' ";
            String sqlQueryOnLeave ="SELECT 'OnLeave' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS='L'  AND PRODUCT_CD='"+productCd+"' ";
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
            sqlCompleteQuery = sqlCompleteQuery+condQuery;
            sqlInCompleteQuery = sqlInCompleteQuery+condQuery; 
            sqlQueryOnLeave = sqlQueryOnLeave+condQuery;
            String finalQuery=sqlCompleteQuery+ " UNION " + sqlInCompleteQuery + " UNION "  + sqlQueryOnLeave;
            return executeStatement(finalQuery);
        }
     
        public POAChartBean[] getFilteredChartWithExamType(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            return getFilteredChartWithExamType(productCd,areaCd,regionCd,districtId,teamCd,"V_PWRA_PLC_DATA");
        }
        
        public POAChartBean[] getFilteredChartWithExamTypeSPF(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            return getFilteredChartWithExamType(productCd,areaCd,regionCd,districtId,teamCd,"V_SPF_PLC_DATA");
        }
        
        private POAChartBean[] getFilteredChartWithExamType(String productCd,String areaCd,String regionCd,String districtId,String teamCd,String tableName){
            
            String sqlCompleteQueryMain="SELECT 'Complete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "  ;
            String sqlCompleteQueryPED=" SELECT EMPLID FROM "+tableName+" WHERE STATUS = 'P' AND EXAM_TYPE='PED' AND PRODUCT_CD='"+productCd+"' ";
            String sqlCompleteQuerySCE=" SELECT EMPLID FROM "+tableName+" WHERE STATUS = 'P' AND EXAM_TYPE='SCE' AND PRODUCT_CD='"+productCd+"' ";
            String sqlCompleteQueryINTERSECT=" INTERSECT ";
            
            String sqlInCompleteQueryI=" SELECT 'InComplete' COURSESTATUS,COUNT(DISTINCT EMPLID) TOTAL FROM  "+tableName+" WHERE STATUS='I' AND PRODUCT_CD='"+productCd+"' ";
            String sqlInCompleteQueryMINUS=" MINUS ";
            String sqlInCompleteQueryL=" SELECT 'InComplete' COURSESTATUS,COUNT(DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS='L'AND PRODUCT_CD='"+productCd+"' ";
            
            String sqlQueryOnLeave="SELECT 'OnLeave' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS='L'  AND PRODUCT_CD='"+productCd+"' ";
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
            sqlCompleteQueryPED = sqlCompleteQueryPED+condQuery;
            sqlCompleteQuerySCE = sqlCompleteQuerySCE+condQuery;
            String sqlCompleteQuery = sqlCompleteQueryMain+"("+sqlCompleteQueryPED+sqlCompleteQueryINTERSECT+sqlCompleteQuerySCE+")";
            sqlInCompleteQueryI=sqlInCompleteQueryI+condQuery;
            sqlInCompleteQueryL=sqlInCompleteQueryL+condQuery;
            String sqlInCompleteQuery = sqlInCompleteQueryI+sqlInCompleteQueryMINUS+sqlInCompleteQueryL;
            sqlQueryOnLeave = sqlQueryOnLeave+condQuery;
            String finalQuery=sqlCompleteQuery+ " UNION " + sqlInCompleteQuery + " UNION "  + sqlQueryOnLeave;            
            return executeStatement(finalQuery);
        }
        
        public POAChartBean[] getFilteredPLCOverallChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            return getFilteredPLCOverallChart(productCd,areaCd,regionCd,districtId,teamCd,"V_PWRA_PLC_DATA_OVERALL");    
        }
        public POAChartBean[] getFilteredPLCOverallChartSPF(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            return getFilteredPLCOverallChart(productCd,areaCd,regionCd,districtId,teamCd,"V_SPF_PLC_DATA_OVERALL");    
        }

        private POAChartBean[] getFilteredPLCOverallChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd,String tableName){
            String sqlQuery1="SELECT 'Complete' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   "+tableName+
                             " WHERE  OVERALL_STATUS = 'P' ";
                             
            String sqlQuery2="SELECT 'InComplete' COURSESTATUS,"+ 
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   "+tableName+
                             " WHERE  OVERALL_STATUS = 'I' ";
                             
            String sqlQuery3="SELECT 'OnLeave' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   "+tableName+
                             " WHERE  OVERALL_STATUS = 'L' ";
                             
            //ConditionQuery
            String condQuery="";
            if(!"ALL".equalsIgnoreCase(areaCd)){
             condQuery=condQuery+ "AND AREA_CD='"+areaCd+"' ";
            }
            if(!"ALL".equalsIgnoreCase(teamCd)  || "".equalsIgnoreCase(teamCd.trim()) ){
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
            return executeStatement(finalQuery); 
        }
        
       /* Infosys code changes starts here
        * private POAChartBean[] executeStatement(String query){
         
         POAChartBean[] poaChartBean=null;
         POAChartBean thisPOAChartBean;
         List tempList = new ArrayList();	
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
         boolean flagComplete = false;
         boolean flagInComplete = false;
         boolean flagOnLeave = false;
            
         try{
            Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();             
            conn.createStatement().executeUpdate("alter session set NLS_DATE_FORMAT = 'DD-MON-RR'");                         
            st = conn.prepareCall(query);			
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                thisPOAChartBean=new POAChartBean();
                thisPOAChartBean.setCourseStatus(rs.getString("COURSESTATUS"));                
                thisPOAChartBean.setTotal(rs.getInt("TOTAL"));
                if(thisPOAChartBean.getCourseStatus().equalsIgnoreCase("Complete")){
                    flagComplete = true;
                }
                if(thisPOAChartBean.getCourseStatus().equalsIgnoreCase("InComplete")){
                    flagInComplete = true;
                }
                if(thisPOAChartBean.getCourseStatus().equalsIgnoreCase("OnLeave")){
                    flagOnLeave = true;
                }
                tempList.add(thisPOAChartBean);                        
            }
            if(!flagComplete){
                thisPOAChartBean=new POAChartBean();
                thisPOAChartBean.setCourseStatus("Complete");                
                thisPOAChartBean.setTotal(0);
                tempList.add(thisPOAChartBean);                                        
            }
            if(!flagInComplete){
                thisPOAChartBean=new POAChartBean();
                thisPOAChartBean.setCourseStatus("InComplete");                
                thisPOAChartBean.setTotal(0);
                tempList.add(thisPOAChartBean);                                                        
            }
            if(!flagOnLeave){                
                thisPOAChartBean=new POAChartBean();
                thisPOAChartBean.setCourseStatus("OnLeave");                
                thisPOAChartBean.setTotal(0);
                tempList.add(thisPOAChartBean);                                                                        
            }
            poaChartBean=new POAChartBean[tempList.size()];
            for(int i=0;i<tempList.size();i++){
               poaChartBean[i]=(POAChartBean)tempList.get(i);
            }
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
            return poaChartBean;
        }

*/
        private POAChartBean[] executeStatement(String query){
            
            POAChartBean[] poaChartBean=null;
            POAChartBean thisPOAChartBean;
            List tempList = new ArrayList();	
            ResultSet rs = null;
            PreparedStatement st = null;
   		 Connection conn = JdbcConnectionUtil.getJdbcConnection();      
            boolean flagComplete = false;
            boolean flagInComplete = false;
            boolean flagOnLeave = false;
               
            try{
                         
               conn.createStatement().executeUpdate("alter session set NLS_DATE_FORMAT = 'DD-MON-RR'");                         
               st = conn.prepareCall(query);			
   			st.setFetchSize(5000);
               rs = st.executeQuery();
               while (rs.next()) {
                   thisPOAChartBean=new POAChartBean();
                   thisPOAChartBean.setCourseStatus(rs.getString("COURSESTATUS"));                
                   thisPOAChartBean.setTotal(rs.getInt("TOTAL"));
                   if(thisPOAChartBean.getCourseStatus().equalsIgnoreCase("Complete")){
                       flagComplete = true;
                   }
                   if(thisPOAChartBean.getCourseStatus().equalsIgnoreCase("InComplete")){
                       flagInComplete = true;
                   }
                   if(thisPOAChartBean.getCourseStatus().equalsIgnoreCase("OnLeave")){
                       flagOnLeave = true;
                   }
                   tempList.add(thisPOAChartBean);                        
               }
               if(!flagComplete){
                   thisPOAChartBean=new POAChartBean();
                   thisPOAChartBean.setCourseStatus("Complete");                
                   thisPOAChartBean.setTotal(0);
                   tempList.add(thisPOAChartBean);                                        
               }
               if(!flagInComplete){
                   thisPOAChartBean=new POAChartBean();
                   thisPOAChartBean.setCourseStatus("InComplete");                
                   thisPOAChartBean.setTotal(0);
                   tempList.add(thisPOAChartBean);                                                        
               }
               if(!flagOnLeave){                
                   thisPOAChartBean=new POAChartBean();
                   thisPOAChartBean.setCourseStatus("OnLeave");                
                   thisPOAChartBean.setTotal(0);
                   tempList.add(thisPOAChartBean);                                                                        
               }
               poaChartBean=new POAChartBean[tempList.size()];
               for(int i=0;i<tempList.size();i++){
                  poaChartBean[i]=(POAChartBean)tempList.get(i);
               }
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
               return poaChartBean;
           }
        // Infosys code changes ends here
   } 

