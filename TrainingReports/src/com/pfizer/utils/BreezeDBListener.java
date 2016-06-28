package com.pfizer.utils; 

import com.pfizer.db.BatchJob;
import com.pfizer.db.Employee;
import com.pfizer.db.Product;
import com.pfizer.dao.TransactionDB;
import com.pfizer.hander.AuditHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.TerritoryHandler;
import com.pfizer.processor.AttendanceProcessor;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.webapp.report.ReportBuilder;
import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
//import com.tgix.Utils.MailUtil;
import com.tgix.Utils.JNDIUtil;
import com.tgix.Utils.LoggerHelper;
import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;

//import weblogic.management.timer.Timer;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



import javax.mail.Session;
import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.timer.Timer;
import javax.naming.Context;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.runtime.ServerRuntimeMBean;*/



import java.sql.CallableStatement;
import java.sql.Types;

//Webligic Server admin libs
import java.util.Set;
import java.util.Iterator;

import javax.naming.Context;

/*import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.runtime.ServerRuntimeMBean;*/

// Data Base
/*import db.BreezeDB;
import db.TrDB;*/

public class BreezeDBListener implements ServletContextListener, NotificationListener { 
	protected static final Log log = LogFactory.getLog( BreezeDBListener.class );
	private Timer[] timer;
    private int timerCount = 0;
	private Integer notificationId;
	private static final long PERIOD = Timer.ONE_DAY;
	public static boolean runFlag = true;
   // private BreezeDB breezeDB;
    private TransactionDB trDB;
    private String[] strBreezeData = null;
    
    java.sql.Connection connBreeze = null;
    java.sql.ResultSet  rs = null;
    java.sql.PreparedStatement  stmt = null;   
    
    java.sql.ResultSet  rsSCO = null;
    java.sql.PreparedStatement  stmtSCO = null;   

    
    java.sql.Connection connTRT = null;
    java.sql.Statement batch=null;
    CallableStatement cs = null;
    CallableStatement csBreezeTimer = null;
	
    boolean transactionSucess = false;
    
    public void handleNotification(Notification arg0, Object arg1) {

       try{

            connTRT = DBUtil.getConn("java:jboss/trt_ds"); // Insert the data into TRT Database
            

            csBreezeTimer = connTRT.prepareCall("{call POWERS_POA_breeze_job_timer(?)}");
            csBreezeTimer.registerOutParameter (1, Types.VARCHAR);
            csBreezeTimer.setString (1, "START_JOB");
            csBreezeTimer.execute();
            String jobStatus = csBreezeTimer.getString(1);
            log.info(" ^^^POWERS_POA_breeze_job_timer START JOB^^^:" +jobStatus );

            
            if (jobStatus.equalsIgnoreCase("C")){ // When deployed in Clustered environment this logic will avoid multiple execuetion of JOB
            
                    connBreeze = DBUtil.getConn("BREEZE_DB_DATASOURCE"); // Get the Data From Breeze Database
                    
                    //*************************************************************************************
                    // Findin Relevant SCO ID's for Lyrica POA project
                    //*************************************************************************************
                    String SQLsco_ids = "select distinct to_char(sco_id) as sco_id from POA_BREEZE_EXAM_PRODUCT_MAP";
                    String sSCOIds = "";
                    stmtSCO = connTRT.prepareStatement(SQLsco_ids);   
                    rsSCO   = stmtSCO.executeQuery(); 
                     if(rsSCO!=null){
                        while(rsSCO.next())         
                        {
                            if (sSCOIds.equalsIgnoreCase("")){
                                sSCOIds = rsSCO.getString("sco_id");
                            }else{
                                sSCOIds = sSCOIds + "," + rsSCO.getString("sco_id");
                            }
                        }
                     }
                    
        
                    //*************************************************************************************
                    // Original Query Breeze Databases
                   // String sql = " select p.login as email,t.sco_id,t.status,t.date_created from (SELECT * FROM PPS_TRANSCRIPTS ";
                   //        sql += " where sco_id in(" + sSCOIds +"))T,PPS_PRINCIPALS p ";
                   //        sql += " where t.principal_id = p.principal_id and t.status in ('C','P','R')";
                   //        sql += " order by p.principal_id,t.sco_id";
                    
                    //*************************************************************************************
                    // Powers MID POA1 Breeze Query
                    String sqlMIDPOA = " select p.login as email,t.sco_id,'P' as status,t.date_created from ";
                        sqlMIDPOA += " (select tm.*,td.score as answer,td.response,td.interaction_id from ";
                        sqlMIDPOA += " (select * from PPS_TRANSCRIPT_DETAILS) TD, ";
                        sqlMIDPOA += " (SELECT * FROM PPS_TRANSCRIPTS  where sco_id in(" +sSCOIds +")) TM ";
                        sqlMIDPOA += " where td.TRANSCRIPT_ID = tm.TRANSCRIPT_ID) T,PPS_PRINCIPALS p ";
                        sqlMIDPOA += " where t.principal_id = p.principal_id ";
                        sqlMIDPOA += " and t.interaction_id='1418497' and t.response in ('A') ";
                        sqlMIDPOA += " order by p.principal_id,t.sco_id";
        
                    System.out.println(" ^^^ POWERS MID POA1  Breeze Query: ^^^" + sqlMIDPOA);
                    log.info(" ^^^ POWERS MID POA1  Breeze Query: ^^^" +sqlMIDPOA);
                    stmt = connBreeze.prepareStatement(sqlMIDPOA);   
                    rs   = stmt.executeQuery(); 
                    String emailId = "";
                    if(rs!=null)
                       {
                        batch = connTRT.createStatement();
                           while(rs.next())         
                           {
                                emailId = DBUtil.replace(rs.getString(1),"'","''");
                                String insertSQL =  "insert into poa_breeze_data_stg (EMAIL_ID, SCO_ID, STATUS, DATE_CREATED)";
                                     insertSQL += "values('"+emailId +"','" + rs.getInt(2) +"','" +rs.getString(3) +"',to_date('" + rs.getDate(4) + "','YYYY-MM-DD'))";
                                //System.out.println("INSERT SQL :" +insertSQL );
                                //Batch Record Update
                                batch.addBatch(insertSQL);
                                // Individual Record Update
                                //batch.execute(insertSQL);
                                
                           }
                        //*************************************************************************************
                        // Update TRT database with the data retrived from breeze database
                        // NOTE : We are doing batch execute to improve performance
                        //*************************************************************************************
                         int[] results  = batch.executeBatch();
                         transactionSucess = true;
        
                        //*************************************************************************************
                        // Update Refresh TRT Main Breeze data Table only if the job ran sucessful 
                        // NOTE : Refresh logic resides in DB Stored Procudure "SP_update_breeze_data"
                        //*************************************************************************************
                        cs = connTRT.prepareCall("{call POWERS_POA_update_breeze_data}");
                        boolean hasResult = cs.execute();
                         //"SP_update_breeze_data"
                       }
                csBreezeTimer.setString (1, "END_JOB");
                csBreezeTimer.execute();
                jobStatus = csBreezeTimer.getString(1);
                log.info(" ^^^POWERS_POA_breeze_job_timer END JOB^^^:" +jobStatus );
                 // Breeze Connection only get opened if the Job runs (Close Breeze DB connections)
                 try{
                    DBUtil.closeDBObjects( connBreeze, rs, stmt, transactionSucess );
                    System.out.println("Breeze Connection Closed Sucessfully!");
                    log.info("Breeze Connection Closed Sucessfully!");
                 }catch(Exception e){
                    System.out.println("DB Error while closing Breeze DB Connection");                    
                    log.info("DB Error while closing Breeze DB Connection:"+ e);                    
                 }

            }// End if
             
             // Close TRT DB connections
             try{
                csBreezeTimer.close();
                DBUtil.closeDBObjects(connTRT,rsSCO,stmtSCO,transactionSucess);
                //System.out.println("TRT Connection Closed Sucessfully!");
                log.info("TRT Connection Closed Sucessfully!");
             } catch (Exception e){
                //System.out.println("DB Error while closing TRT DB Connection");
                log.info("DB Error while closing TRT DB Connection:" + e);
             }
       } catch(Exception e){
                System.out.println("Database error occured while performing DB operations." + e);
                log.info("Database error occured while performing DB operations." + e);
                try{
                    String jobStatus ="";
                    csBreezeTimer.setString (1, "END_JOB");
                    csBreezeTimer.execute();
                    jobStatus = csBreezeTimer.getString(1);
                    log.info(" ^^^POWERS_POA_breeze_job_timer END JOB^^^:" +jobStatus );

                    DBUtil.closeDBObjects( connBreeze, rs, stmt, transactionSucess );
                    //System.out.println("Breeze Connection Closed Sucessfully!");
                    log.info("Breeze Connection Closed Sucessfully!");
                }catch(Exception dbError){
                    //System.out.println("DB Error while closing Breeze DB Connection");
                   log.info("DB Error while closing Breeze DB Connection:" +dbError);
                }

                try{
                    DBUtil.closeDBObjects(connTRT,rsSCO,stmtSCO,transactionSucess);
                    //System.out.println("TRT Connection Closed Sucessfully!");
                   log.info("TRT Connection Closed Sucessfully!");
                }catch(Exception dbError){
                    //System.out.println("DB Error while closing TRT DB Connection");
                    log.info("DB Error while closing TRT DB Connection:" + dbError);
                }
                
       } finally{
         try{
            log.info("Exception Hendeling=> Finally Loop of Breeze Connection");
            connBreeze.close();
         } catch(Exception e){}
         try{
            log.info("Exception Hendeling=> Finally Loop of TRT Connection");
            connTRT.close();
         } catch(Exception e){}
       }
       
	}


    public void contextDestroyed(ServletContextEvent sce) {
		log.info("BreezeDBListener.contextDestroyed");
		try {
            for (int i=0; i<=timerCount;i++){
                timer[i].stop();
                timer[i].removeNotification(notificationId);
            }
            
            log.info(">>> Breeze timer stopped.");
		} catch (InstanceNotFoundException e) {
           log.error(e,e);
		}
    }
    public void contextInitialized(ServletContextEvent sce) {
		log.info("BreezeDBListener.contextInitialized");
		
		// Instantiating the Timer MBean
		//timer = new Timer();

        //*********************************************************************************
        java.sql.Connection conn = null;
		java.sql.ResultSet  rs = null;
		java.sql.PreparedStatement  stmt = null;    
        boolean blnSuccess = false;  
        List dateList = new ArrayList();
        //Date[] startDates = null;
        java.util.Date startDate = new java.util.Date();                    
        try{
            conn = DBUtil.getConn("java:jboss/trt_ds");
            String  sql =" select PROCESS_RUN_HOURS,PROCESS_RUN_MINUTES,PROCESS_RUN_SECONDS,AM_PM from tr_job_schedule where JOB_NAME='BREEZE' ";                    
                 
                       
            stmt = conn.prepareStatement(sql);      
            rs   = stmt.executeQuery();    

            if(rs!=null){
                // Max Three Schedule available
                Timer[] timer = new Timer[3];
                while(rs.next())
                {
                    
                    // Instantiating the Timer MBean
                    timer[timerCount] = new Timer();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR, new Integer(rs.getString("PROCESS_RUN_HOURS")).intValue());
                    calendar.set(Calendar.MINUTE, new Integer(rs.getString("PROCESS_RUN_MINUTES")).intValue());
                    calendar.set(Calendar.SECOND, new Integer(rs.getString("PROCESS_RUN_SECONDS")).intValue());
                    String startTimeAmPm = rs.getString("AM_PM");
                    calendar.set(Calendar.AM_PM, startTimeAmPm.equals("AM") ? Calendar.AM : Calendar.PM);
                    startDate = calendar.getTime();
                    dateList.add(startDate);
                    System.out.println("BREEZE JOB Timer:" + timerCount + " Starts at:" + startDate);
                    log.info("BREEZE JOB Timer:" + timerCount + " Starts at:" + startDate);
                    //*********************************************************************************
                    // Registering this class as a listener
                    timer[timerCount].addNotificationListener(this, null, "some handback object");
                    
                    //notificationId = timer.addNotification("oneMinuteTimer", "a recurring call", this, timerTriggerAt, PERIOD);
                    notificationId = timer[timerCount].addNotification("oneDayTimer", "a recurring call", this, startDate, PERIOD);
                    timer[timerCount].start();                    
                    timerCount = timerCount +1;
                }
            }
        }catch (Exception e){
             log.error("Breeze contextInitialized error" + e);
             e.printStackTrace();
		
        }finally{
            try{
                DBUtil.closeDBObjects( conn, rs, stmt, blnSuccess );
            }catch (Exception e) {
                log.error("Exception in Closing DB Objects" + e);
            }
		}//end of finally block



	}
	
    
    
    
    }