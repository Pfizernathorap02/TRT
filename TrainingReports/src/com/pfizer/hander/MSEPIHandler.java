package com.pfizer.hander; 

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pfizer.utils.HibernateUtils;
import com.pfizer.webapp.wc.POA.POAChartBean;

public class MSEPIHandler {     
    protected static final Log log = LogFactory.getLog(PLCHandler.class);
    
    public int getOverAllTotalCount(String productCd,String areaCd,String regionCd,String districtId,String teamCd) {
        String sqlQuery1=" SELECT COUNT(DISTINCT EMPLID) FROM V_MSEPI_NSM_DATA WHERE STATUS IN ('L','I','P') ";
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
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
        /*private int getCount(String query){
         int total=0;
         ResultSet rs = null;
         PreparedStatement st = null;
		 Connection conn = null;      
            
         try{
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =  ds.getConnection();  
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
    }*/
    
    private int getCount(String query){
    	Session session = HibernateUtils.getHibernateSession();
        int total=0;
        
        try{
        	System.out.println("Query in getCount is "+query);
        	Transaction tx= session.beginTransaction();
			Query queryTemp= session.createSQLQuery("alter session set NLS_DATE_FORMAT = 'DD-MON-RR'");
			
			queryTemp.executeUpdate();
			
			queryTemp=null;
			
			queryTemp = session.createSQLQuery(query);
			if(queryTemp.list().get(0)!=null)
			total = Integer.valueOf(queryTemp.list().get(0).toString());
			
        }catch (Exception e) {
			log.error(e,e);
		} finally {
			HibernateUtils.closeHibernateSession(session);
		}
        
        return total; 
    }
    /*Infosys - Weblogic to Jboss Migrations changes end here*/
    
    public POAChartBean[] getFilteredChart(String trtCourseCd,String areaCd,String regionCd,String districtId,String teamCd){
        
        String tableName = " V_MSEPI_NSM_DATA ";
        
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
    /*Infosys - Weblogic to Jboss Migrations changes start here*/
    /*private POAChartBean[] executeStatement(String query){
         
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
    }        */
    
    @SuppressWarnings("null")
	private POAChartBean[] executeStatement(String query){
    	Session session = HibernateUtils.getHibernateSession();
    	
        POAChartBean[] poaChartBean=null;
        POAChartBean thisPOAChartBean = new POAChartBean();
        List tempList = new ArrayList();	
        boolean flagComplete = false;
        boolean flagInComplete = false;
        boolean flagOnLeave = false;
                
        try{
        	System.out.println("Query in executeStatement is "+query);
        	Transaction tx= session.beginTransaction();
			Query queryTemp= session.createSQLQuery("alter session set NLS_DATE_FORMAT = 'DD-MON-RR'");
			queryTemp.executeUpdate();
			
			queryTemp = null;
			
			queryTemp = session.createSQLQuery(query);
			
			List result = queryTemp.list();
			Iterator it = result.iterator();
			
			while(it.hasNext()){
				thisPOAChartBean=new POAChartBean();
				Object[] field = (Object[]) it.next();
				if(field[0]!=null)
				thisPOAChartBean.setCourseStatus(field[0].toString());
				if(field[1]!=null)
				thisPOAChartBean.setTotal(Integer.parseInt(field[1].toString()));
				
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
            if(tempList!=null)
            for(int i=0;i<tempList.size();i++){
               poaChartBean[i]=(POAChartBean)tempList.get(i);
            }
        }catch(Exception e){
                log.error(e,e);
        }finally {
        	HibernateUtils.closeHibernateSession(session);
        }
        return poaChartBean;
    }
}
