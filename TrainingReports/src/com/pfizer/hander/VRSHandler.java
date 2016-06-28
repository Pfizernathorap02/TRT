package com.pfizer.hander; 

import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.wc.POA.POAChartBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class VRSHandler { 
    protected static final Log log = LogFactory.getLog(PLCHandler.class);
    
    public int getOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId,String teamCd) {
        String sqlQuery1=" SELECT COUNT(DISTINCT EMPLID) COUNT FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS IN ('L','I','P') ";
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
        
        private int getCount(String query){
         int total=0;
         ResultSet rs = null;
         PreparedStatement st = null;
		/* Connection conn = null;*/      
         /* Infosys - Weblogic to Jboss migration changes start here */	
    		Connection conn = JdbcConnectionUtil.getJdbcConnection(); 		
    		/* Infosys - Weblogic to Jboss migration changes end here */ 
         
         try{
            /*Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =  ds.getConnection();  */
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
    
    
    public POAChartBean[] getFilteredChart(String trtCourseCd,String areaCd,String regionCd,String districtId,String teamCd){
        
        String tableName = " V_VISTASPIRIVA_DATA ";
        
        String sqlCompleteQuery= "SELECT 'Complete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS = 'P' AND TRT_COURSE_CODE = '"+trtCourseCd+"' ";        
        String sqlInCompleteQuery= "SELECT 'InComplete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS = 'I' AND TRT_COURSE_CODE = '"+trtCourseCd+"' ";
        String sqlQueryOnLeave = "SELECT 'OnLeave' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM "+tableName+" WHERE STATUS = 'L' AND TRT_COURSE_CODE = '"+trtCourseCd+"' ";
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
        sqlCompleteQuery = sqlCompleteQuery+condQuery;
        sqlInCompleteQuery = sqlInCompleteQuery+condQuery;
        sqlQueryOnLeave = sqlQueryOnLeave+condQuery;
        String finalQuery=sqlCompleteQuery+ " UNION " + sqlInCompleteQuery + " UNION "  + sqlQueryOnLeave; 
        
        return executeStatement(finalQuery);
    }
    
    public POAChartBean[] getFilteredChartAttendance(String trtCourseCd,String areaCd,String regionCd,String districtId,String teamCd){
        

        /*
        String sqlCompleteQueryM= " SELECT 'Complete' COURSESTATUS, COUNT (*) TOTAL FROM ";        
        String sqlCompleteQueryC= " SELECT DISTINCT EMPLID TOTAL FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'P' ";        
        String sqlCompleteQueryI= " SELECT DISTINCT EMPLID TOTAL FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' ";        
        String sqlCompleteQueryL= " SELECT DISTINCT EMPLID TOTAL FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'L' ";        
        String sqlInCompleteQuery= " SELECT 'InComplete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' ";
        String sqlQueryOnLeaveM= " SELECT  'OnLeave' COURSESTATUS, COUNT (*) TOTAL FROM ";
        String sqlQueryOnLeaveL = " SELECT DISTINCT EMPLID TOTAL FROM V_GNSM_ATTENDANCE_DATA WHERE STATUS = 'L' ";
        String sqlQueryOnLeaveI = " SELECT DISTINCT EMPLID TOTAL FROM V_GNSM_ATTENDANCE_DATA WHERE  STATUS = 'I' ";
        */
        
        String sqlCompleteQueryM= " SELECT 'Complete' COURSESTATUS, COUNT (*) TOTAL FROM V_SPIRIVA_ATTENDANCE_DATA WHERE STATUS = 'P' ";        
        String sqlOnLeaveQueryM= " SELECT  'OnLeave' COURSESTATUS, COUNT (*) TOTAL FROM ";
        String sqlOnLeaveQueryL = " SELECT DISTINCT EMPLID TOTAL FROM V_SPIRIVA_ATTENDANCE_DATA WHERE STATUS = 'L' ";
        String sqlOnLeaveQueryP = " SELECT DISTINCT EMPLID TOTAL FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'P' ";
        String sqlInCompleteQueryM = " SELECT 'InComplete' COURSESTATUS, COUNT (*) TOTAL FROM ";
        String sqlInCompleteQueryI = " SELECT DISTINCT EMPLID TOTAL FROM V_SPIRIVA_ATTENDANCE_DATA WHERE STATUS = 'I' ";
        String sqlInCompleteQueryP = " SELECT DISTINCT EMPLID TOTAL FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'P' ";
        String sqlInCompleteQueryL = " SELECT DISTINCT EMPLID TOTAL FROM V_SPIRIVA_ATTENDANCE_DATA WHERE  STATUS = 'L' ";
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
        /*
        String sqlCompleteQuery = sqlCompleteQueryM+"( "+sqlCompleteQueryC+condQuery+" MINUS ( "+sqlCompleteQueryI+condQuery+" UNION "+sqlCompleteQueryL+condQuery+" )) ";
        String sqlQueryOnLeave = sqlQueryOnLeaveM+"( "+sqlQueryOnLeaveL+condQuery+" MINUS "+sqlQueryOnLeaveI+condQuery+")";
        sqlInCompleteQuery = sqlInCompleteQuery+condQuery;
        */
        String sqlCompleteQuery = sqlCompleteQueryM+condQuery;
        String sqlOnLeaveQuery = sqlOnLeaveQueryM+"( "+sqlOnLeaveQueryL+condQuery+" MINUS "+sqlOnLeaveQueryP+condQuery+")";
        String sqlInCompleteQuery = sqlInCompleteQueryM+"( "+sqlInCompleteQueryI+condQuery+" MINUS ( "+sqlInCompleteQueryP+condQuery+" UNION "+sqlInCompleteQueryL+condQuery+" )) ";
        
        String finalQuery=sqlCompleteQuery+ " UNION " + sqlInCompleteQuery + " UNION "  + sqlOnLeaveQuery; 

        return executeStatement(finalQuery);
    }
    

    private POAChartBean[] executeStatement(String query){
         
         POAChartBean[] poaChartBean=null;
         POAChartBean thisPOAChartBean;
         List tempList = new ArrayList();	
         ResultSet rs = null;
         PreparedStatement st = null;
		/* Connection conn = null;*/
         /* Infosys - Weblogic to Jboss migration changes start here */	
    		Connection conn = JdbcConnectionUtil.getJdbcConnection(); 		
    		/* Infosys - Weblogic to Jboss migration changes end here */ 
         
         boolean flagComplete = false;
         boolean flagInComplete = false;
         boolean flagOnLeave = false;
            
         try{
           /* Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection(); */      
        	 
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
    
    public POAChartBean[] getFilteredOverallChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){

        String sqlQueryComplete = "SELECT 'Complete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS = 'P' ";
        String sqlQueryInComplete = "SELECT 'InComplete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS = 'I' ";
        String sqlQueryOnLeave = "SELECT 'OnLeave' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS = 'L' ";
                         
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
        sqlQueryComplete=sqlQueryComplete+condQuery;
        sqlQueryInComplete=sqlQueryInComplete+condQuery;
        sqlQueryOnLeave=sqlQueryOnLeave+condQuery;
        String finalQuery=sqlQueryComplete+ " UNION " + sqlQueryInComplete + " UNION "  + sqlQueryOnLeave;

        return executeStatement(finalQuery); 
    }
    
    /* Method added for VistaRx  Spiriva enhancement
     * Author: Meenakshi
     * Date:12-Sep-2008
    */
     public POAChartBean[] getFilteredOverallChartForVRS(String productCd,String areaCd,String regionCd,String districtId,String teamCd)
     {

        String sqlQueryComplete = "SELECT 'Complete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS = 'P' ";
        String sqlQueryInComplete = "SELECT 'InComplete' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS = 'I' ";
        String sqlQueryOnLeave = "SELECT 'OnLeave' COURSESTATUS, COUNT (DISTINCT EMPLID) TOTAL FROM V_SPIRIVA_DATA_OVERALL WHERE OVERALL_STATUS = 'L' ";
                         
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
        sqlQueryComplete=sqlQueryComplete+condQuery;
        sqlQueryInComplete=sqlQueryInComplete+condQuery;
        sqlQueryOnLeave=sqlQueryOnLeave+condQuery;
        String finalQuery=sqlQueryComplete+ " UNION " + sqlQueryInComplete + " UNION "  + sqlQueryOnLeave;

        return executeStatement(finalQuery); 
    }
    
    /* End of addition */
    
} 
