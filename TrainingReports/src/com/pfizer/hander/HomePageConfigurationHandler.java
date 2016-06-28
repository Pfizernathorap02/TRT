package com.pfizer.hander; 

import com.pfizer.utils.DBUtil;
import com.pfizer.utils.HibernateUtils;
import com.pfizer.webapp.AppConst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;


public class HomePageConfigurationHandler 
{ 
    protected static final Log log = LogFactory.getLog(HomePageConfigurationHandler.class );
    
    public HomePageConfigurationHandler(){
     //   System.out.println("Inside mgt reports");
    }
    
    public List getTrainingReportConfiguration()
    {
        String sql =" SELECT * FROM TRAINING_REPORT WHERE DELETE_FLAG='N' and PARENT IS NULL order by sort_order asc";
        List result = executeSql(sql);//retrieve columns in a order
      //  System.out.println("result in query"+result);
        log.info(sql);
        return result;
    }
    
   /* public void updateDeleteFlag(String id){
      //  String sql =" Update TRAINING_REPORT set DELETE_FLAG='N' where training_report_id=?"; 
        String sql =" Update TRAINING_REPORT set DELETE_FLAG='Y' where training_report_id=?"; 
        log.info(sql);
        ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        
        try{
            Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(sql);
            statement.setString(1,id);
            int num = statement.executeUpdate();
            
            
           if ( num > 0 ) {
               // System.out.println("updation query"+sql);
            }
        }
        catch(Exception e){
            log.error(e,e);
            e.printStackTrace();
            System.out.println("In exception");
        }
        finally{
                if ( statement != null){
                     try {
                         statement.close();
                         } 
                     catch ( Exception e2){
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
    }*/
// changes made by Infosys
    
    public void updateDeleteFlag(String id){
    	Session session = HibernateUtils.getHibernateSession();
    try{
    	System.out.println(id+"updateDeleteFlag");
    	String sql =" Update TRAINING_REPORT set DELETE_FLAG='Y' where training_report_id= :id"; 
    	Transaction tx= session.beginTransaction();
    	Query query = session.createSQLQuery(sql);
		
		System.out.println("Inside updateDeleteFlag");
		query.setParameter("id", id);
		
		int result = query.executeUpdate();

		tx.commit();
		System.out.println("Rows affected updateDeleteFlag: " + result);
   
    }
    catch (HibernateException e) {
		// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
		// "getDistributionListFilters --> HibernateException : ", e);
		System.out.println();
		e.printStackTrace();

		// log.error("HibernateException in getUserByNTIdAndDomain", e);
		System.out.println("updateUserAccess Hibernatate Exception");
	} finally {
		HibernateUtils.closeHibernateSession(session);
	}
    
    
    
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /*  public void updateTrainingReportConfiguration(List labelList,List idList){
        String sql;
        int num;
        
        try{
            Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(sql);
                for(int i=0;i<labelList.size();i++){
                    sql = " update Training_report set Training_report_label='"+labelList.get(i)+"' where training_report_id='"+idList.get(i)+"' ";   
                    num
                }
        }
            
    } */
    
    
     /*public void updateTrainingReportTable(String[] sectionNames,String[] trackId,String[] minimize){
        for(int i=0;i<trackId.length;i++){
           // String sql = " UPDATE TRAINING_REPORT SET TRAINING_REPORT_LABEL='"+sectionNames[i]+ "' WHERE TRAINING_REPORT_ID='"+trackId[i]+"' ";
           String sql = " UPDATE TRAINING_REPORT SET TRAINING_REPORT_LABEL='"+sectionNames[i]+ "', SORT_ORDER='"+ (i+1) + "', MINIMIZE='"+minimize[i]+"' WHERE TRAINING_REPORT_ID='"+trackId[i]+"' ";
            log.info(sql);
        ResultSet rs = null;
		PreparedStatement statement = null;
		Connection conn = null;
        
        try{
            Context ctx = new InitialContext();
        	DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();
			statement = conn.prepareStatement(sql);
            
            int num = statement.executeUpdate();
            
            
           if ( num > 0 ) {
            //    System.out.println("updation query"+sql);
            }
        }
        catch(Exception e){
            log.error(e,e);
            e.printStackTrace();
          //  System.out.println("In exception");
        }
        finally{
                if ( statement != null){
                     try {
                         statement.close();
                         } 
                     catch ( Exception e2){
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
     }*/
     
  // added by jhansi  
    public void updateTrainingReportTable(String[] sectionNames,String[] trackId,String[] minimize){
        for(int i=0;i<trackId.length;i++){
           // String sql = " UPDATE TRAINING_REPORT SET TRAINING_REPORT_LABEL='"+sectionNames[i]+ "' WHERE TRAINING_REPORT_ID='"+trackId[i]+"' ";
          
            Session session = HibernateUtils.getHibernateSession();
            try{
            	Transaction tx= session.beginTransaction();
            	String sql = " UPDATE TRAINING_REPORT SET TRAINING_REPORT_LABEL='"+sectionNames[i]+ "', SORT_ORDER='"+ (i+1) + "', MINIMIZE='"+minimize[i]+"' WHERE TRAINING_REPORT_ID='"+trackId[i]+"' ";
                //log.info(sql);
    			Query query = session.createSQLQuery(sql);
    			int result=query.executeUpdate();
     
    			tx.commit();
			}
			catch (HibernateException e) {
				// getServiceBean().logError(SystemConstants.TMSCORE_SERVICE_LOGNAME_KEY,
				// "getDistributionListFilters --> HibernateException : ", e);
				System.out.println();
				e.printStackTrace();

				// log.error("HibernateException in getUserByNTIdAndDomain", e);
				System.out.println("updateTrainingReportTable Hibernatate Exception");
			} finally {
				HibernateUtils.closeHibernateSession(session);
			}
		}
    }
     public List executeSql( String sql ) {
        
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE,"ordered");
        
        return result;
    }
} 
