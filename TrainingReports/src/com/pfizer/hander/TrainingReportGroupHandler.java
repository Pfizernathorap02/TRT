package com.pfizer.hander;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pfizer.db.P2lTrack;
import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.webapp.AppConst;

//import weblogic.webservice.tools.pagegen.result; 

public class TrainingReportGroupHandler 
{ 
    
           protected static final Log log = LogFactory.getLog(ForecastFilterHandler.class );

    public TrainingReportGroupHandler () {

    }
      public boolean insertTrainingReports(P2lTrack track, String menuId) {

        String retString = null;
		String insertSql = "insert into  training_report  " + 
                        "   (training_report_id, training_report_label,training_report_url,parent,active,allow_group, track_id,delete_flag) " +
                        "   values (TRAINING_REPORT_ID_SEQ.nextval,?,?,?,1,null,?,'N') ";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */	
   		Connection conn = JdbcConnectionUtil.getJdbcConnection(); 		
   		/* Infosys - Weblogic to Jboss migration changes end here */ 
		
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			statement = conn.prepareStatement(insertSql);
			
			statement.setString( 1, track.getTrackLabel() );			
			statement.setString( 2, "#");		
			statement.setBigDecimal( 3, new BigDecimal(menuId) );		
			statement.setString( 4, track.getTrackId() );			
					
			log.info(insertSql);
			int num = statement.executeUpdate();
            if ( num > 0 ) {
                return true;
            }      
            
        } catch (Exception e) {
			log.error(e,e);
		} finally {        
            if ( statement != null) {
                try {
                    statement.close();
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
        return false;
    }    
    
     public P2lTrack getTrack(String trackId) {
        String sql = "Select track_id as trackid,  training_report_label as trackLabel from training_report where track_id='" + trackId+"'";
        
        List result = executeSql2(sql);
        // only expect 1
        P2lTrack track = null;
        if ( result != null && result.size() > 0 ) {
            System.out.println("result.size " + result.size());
            HashMap trackDetails = (HashMap)result.get(0);
            track = new P2lTrack();
            if(trackDetails.get("TRACKLABEL")!=null && trackDetails.get("TRACKID")!=null){
            track.setTrackLabel(trackDetails.get("TRACKLABEL").toString());
            track.setTrackId(trackDetails.get("TRACKID").toString());
            
              System.out.println("TRACKLABEL " + trackDetails.get("TRACKLABEL").toString());                  
              System.out.println("TRACKID " + trackDetails.get("TRACKID").toString());
            }
        }
        return track;
    }    
    
        public List executeSql2( String sql ) {
        
        List result = DBUtil.executeSql(sql,AppConst.APP_DATASOURCE);
        
        return result;
    }
    
     public boolean updateGroup(String trackId,String trackLabel)
     {
            String retString = null;
		String updateSql = "update training_report  " + 
                        " set training_report_label = '" + trackLabel + "'" +
                        " where track_id='" + trackId+"'";
		ResultSet rs = null;
		PreparedStatement statement = null;
		/*Connection conn = null;*/
		/* Infosys - Weblogic to Jboss migration changes start here */	
   		Connection conn = JdbcConnectionUtil.getJdbcConnection(); 		
   		/* Infosys - Weblogic to Jboss migration changes end here */ 
		
		
		try {

			/*Context ctx = new InitialContext();

			DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
			conn =   ds.getConnection();  */
			statement = conn.prepareStatement(updateSql);
			
			
			log.info(updateSql);
			int num = statement.executeUpdate();
            if ( num > 0 ) {
                return true;
            }      
            
        } catch (Exception e) {
			log.error(e,e);
		} finally {        
            if ( statement != null) {
                try {
                    statement.close();
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
        return false;
     }
    
 
} 
