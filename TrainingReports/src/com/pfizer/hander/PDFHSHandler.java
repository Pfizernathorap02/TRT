package com.pfizer.hander; 

import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.wc.PDFHS.PDFHSChartBean;

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

public class PDFHSHandler 
{ 
    	protected static final Log log = LogFactory.getLog( PDFHSHandler.class );
        
        
        public PDFHSHandler(){
        }
        
        //View Changed from V_POWERS_POA_DATA to V_PWRA_HS_DATA
        public PDFHSChartBean[] getFilteredPDFHSChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
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
        
        
        
         public PDFHSChartBean[] getFilteredPDFHSOverallChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
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
            log.debug("Query for PDFHS Handler with filter:"+finalQuery.toString());
            return executeStatement(finalQuery);
        }
        
       
        
       public int  getOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId,String teamCd) {
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
			conn =   ds.getConnection();  */
            st = conn.prepareCall(query);			
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                total=rs.getInt("COUNT");
        
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
           return total;   
           }
         
        
        
        
        
        
        
        private PDFHSChartBean[] executeStatement(String query){
         
         PDFHSChartBean[] pdfhsChartBean=null;
         PDFHSChartBean thisPOAChartBean;
         List tempList = new ArrayList();	
         ResultSet rs = null;
         PreparedStatement st = null;
		 /*Connection conn = null;*/      
         /* Infosys - Weblogic to Jboss migration changes start here */
  		Connection conn = JdbcConnectionUtil.getJdbcConnection();
  		/* Infosys - Weblogic to Jboss migration changes end here */  
         try{
            /*Context ctx = new InitialContext();
			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
            st = conn.prepareCall(query);			
			st.setFetchSize(5000);
            rs = st.executeQuery();
            while (rs.next()) {
                thisPOAChartBean=new PDFHSChartBean();
                thisPOAChartBean.setCourseStatus(rs.getString("COURSESTATUS"));
                thisPOAChartBean.setTotal(rs.getInt("TOTAL"));
                tempList.add(thisPOAChartBean);                        
            }
            pdfhsChartBean=new PDFHSChartBean[tempList.size()];
            for(int i=0;i<tempList.size();i++){
                pdfhsChartBean[i]=(PDFHSChartBean)tempList.get(i);
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
        return pdfhsChartBean;
        }
    
} 
