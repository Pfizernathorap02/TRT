package com.pfizer.hander; 

import com.pfizer.utils.HibernateUtils;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;
import com.pfizer.webapp.wc.global.ChartBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SPFHandler 
{ 
    	protected static final Log log = LogFactory.getLog( PDFHSHandler.class );
        
        
        public SPFHandler(){
        }
        
        //View Changed from V_POWERS_POA_DATA to V_SPF_HS_DATA
        public ChartBean[] getFilteredChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            String sqlQuery1="SELECT 'Complete' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_SPF_HS_DATA"+
                             " WHERE  STATUS = 'P' "+
                             "        AND PRODUCT_CD = '"+productCd+"' "  ;
            String sqlQuery2="SELECT 'InComplete' COURSESTATUS,"+ 
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_SPF_HS_DATA"+
                             " WHERE  STATUS = 'I' "+
                             "        AND PRODUCT_CD = '"+productCd+"' "  ;
            String sqlQuery3="SELECT 'OnLeave' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_SPF_HS_DATA"+
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
        
        
        
         public ChartBean[] getFilteredOverallChart(String productCd,String areaCd,String regionCd,String districtId,String teamCd){
            String sqlQuery1="SELECT 'Complete' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_SPF_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS = 'P' ";
                             
            String sqlQuery2="SELECT 'InComplete' COURSESTATUS,"+ 
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_SPF_HS_DATA_OVERALL"+
                             " WHERE  OVERALL_STATUS = 'I' ";
                             
            String sqlQuery3="SELECT 'OnLeave' COURSESTATUS,"+
                             "        COUNT(DISTINCT EMPLID) TOTAL"+
                             " FROM   V_SPF_HS_DATA_OVERALL"+
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
                             " FROM   V_SPF_HS_DATA_OVERALL"+
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
        
      /*  private int getCount(String query){
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
              st = conn.prepareStatement(query)	;	;	
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
  
       /* private ChartBean[] executeStatement(String query){
         
         ChartBean[] pdfhsChartBean=null;
         ChartBean thisPOAChartBean;
         List tempList = new ArrayList();	
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
                thisPOAChartBean=new ChartBean();
                thisPOAChartBean.setCourseStatus(rs.getString("COURSESTATUS"));
                thisPOAChartBean.setTotal(rs.getInt("TOTAL"));
                tempList.add(thisPOAChartBean);                        
            }
            pdfhsChartBean=new ChartBean[tempList.size()];
            for(int i=0;i<tempList.size();i++){
                pdfhsChartBean[i]=(ChartBean)tempList.get(i);
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
    */
       
       private ChartBean[] executeStatement(String query){
           
           ChartBean[] pdfhsChartBean=null;
           ChartBean thisPOAChartBean;
           List tempList = new ArrayList();	
           Session session = HibernateUtils.getHibernateSession();   
              
           try{
             String s="alter session set NLS_DATE_FORMAT = 'DD-MON-RR'";
             Transaction tx= session.beginTransaction();
 				Query q = session.createSQLQuery(s);
 			
 				int result = q.executeUpdate();
 				tx.commit();
 				System.out.println("Rows affected: " + result);
 				List list=q.list();
 				Iterator it = list.iterator();
 				while(it.hasNext())
 				{
 					thisPOAChartBean=new ChartBean();
 					Object[] field = (Object[]) it.next();
 					thisPOAChartBean.setCourseStatus(field[0].toString());
 	                  thisPOAChartBean.setTotal((Integer)field[1]);
 	                 tempList.add(thisPOAChartBean);  
 				}
 				pdfhsChartBean=new ChartBean[tempList.size()];
 	              for(int i=0;i<tempList.size();i++){
 	                  pdfhsChartBean[i]=(ChartBean)tempList.get(i);
 	              }
           }
 				catch (HibernateException e) {
 					// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
 					// "getDistributionListFilters --> HibernateException : ", e);
 					System.out.println();
 					e.printStackTrace();

 					// log.error("HibernateException in getUserByNTIdAndDomain", e);
 					System.out.println("insertUserAccess Hibernatate Exception");
 				} finally {
 					HibernateUtils.closeHibernateSession(session);
 				}
          return pdfhsChartBean;
          }
      
} 
