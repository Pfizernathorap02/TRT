package com.tgix.rbu; 

import com.pfizer.db.BatchJob;
import com.pfizer.db.Employee;
import com.pfizer.db.P2LRegistration;
import com.pfizer.db.Product;
import com.pfizer.hander.AuditHandler;
import com.pfizer.hander.EmployeeHandler;
import com.pfizer.hander.TerritoryHandler;
import com.pfizer.processor.AttendanceProcessor;
import com.pfizer.processor.OverallProcessor;
import com.pfizer.service.Service;
import com.pfizer.service.ServiceFactory;
import com.pfizer.webapp.AppConst;

import java.sql.CallableStatement;
import java.sql.Connection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


///////////////////////////









import com.pfizer.webapp.user.TerritoryFilterForm;
import com.pfizer.webapp.user.User;
import com.pfizer.webapp.user.UserFilter;
import com.pfizer.webapp.user.UserSession;
import com.tgix.Utils.JNDIUtil;
import com.tgix.Utils.LoggerHelper;
import com.tgix.Utils.MailUtil;
import com.tgix.Utils.Util;
import com.tgix.printing.PrintingConstants;
import com.tgix.printing.VelocityConvertor;
//import com.tgix.Utils.MailUtil;


import java.util.ArrayList;
import java.util.Calendar;
/*import weblogic.management.timer.Timer;*/
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Session;
import javax.management.InstanceNotFoundException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.naming.Context;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;










/* Infosys migrated code weblogic to jboss changes start here
 * import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.runtime.ServerRuntimeMBean;
import java.sql.CallableStatement;
Infosys migrated code weblogic to jboss changes end here
*/
import java.sql.Types;

//Webligic Server admin libs
import java.util.Set;
import java.util.Iterator;

import javax.naming.Context;

/* Infosys migrated code weblogic to jboss changes start here
 * import weblogic.jndi.Environment;
import weblogic.management.MBeanHome;
import weblogic.management.runtime.ServerRuntimeMBean;
Infosys migrated code weblogic to jboss changes end here
*/









import com.pfizer.utils.DBUtil;
import com.pfizer.utils.JdbcConnectionUtil;
import com.pfizer.utils.ReadProperties;
import com.pfizer.webapp.report.ReportBuilder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;


//
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.naming.NamingException;
//////////////////////////////////////////

public class RBUHandlers 
{ 
     protected static final Log log = LogFactory.getLog( RBUHandlers.class );
    
     SimpleDateFormat formatFileName = new SimpleDateFormat("MM-dd-yyyy"); 
    /**
     * This method processes the P2L Registration process by calling the procedure.
     */
    public static void callP2LRegistrationProcess()
    {
//      Connection conn = null;
    	Connection conn = JdbcConnectionUtil.getJdbcConnection();
      try
      {
        /*Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn =   ds.getConnection();  */
        CallableStatement proc = conn.prepareCall("{ call RBU_P2L_REGISTRATION_PROCESS()}");     
        proc.executeUpdate();        
      } catch (Exception e) 
      {
			log.error(e,e);
      }
      finally 
      {
         if ( conn != null) {
            try 
             {
                conn.close();
			} catch ( Exception e2) 
            {
				log.error(e2,e2);
			}
        }
      }
     
    }//end of the Method 
        /**
     * This method processes the Table Assignment.
     */
    public static void rbuEnrollment(int week_id)
    {
      Connection conn = null;
      try
      {
        Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn =   ds.getConnection();  
        CallableStatement proc = conn.prepareCall("{ call rbu_table_assignment(?)}");  
        proc.setInt(1, week_id);   
        proc.executeUpdate();  
        proc.close();      
      } catch (Exception e) 
      {
			log.error(e,e);
      }
      finally 
      {
         if ( conn != null) {
            try 
             {
                conn.close();
			} catch ( Exception e2) 
            {
				log.error(e2,e2);
			}
        }
      }
     
    }
    
        /**
     * This method processes the RBUEnrollment.
     */
    public void rbuSCEGT()
    {
      /*Connection conn = null;*/
    	Connection conn = JdbcConnectionUtil.getJdbcConnection();
      try
      {
       /* Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn =   ds.getConnection();  */
        CallableStatement proc = conn.prepareCall("{ call RBU_GT_SCE_ACCESS()}");     
        proc.executeUpdate();        
        proc.close();
      } catch (Exception e) 
      {
			log.error(e,e);
      }
      finally 
      {
         if ( conn != null) {
            try 
             {
                conn.close();
			} catch ( Exception e2) 
            {
				log.error(e2,e2);
			}
        }
      }
     
    }
    /**
     * This method processes the RBUEnrollment.
     */
    public static void rbuEnrollment()
    {
      /*Connection conn = null;*/
    	Connection conn = JdbcConnectionUtil.getJdbcConnection();
      try
      {
       /* Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn =   ds.getConnection();  */
        CallableStatement proc = conn.prepareCall("{ call RBU_ENROLLMENT_PROCESS()}");     
        proc.executeUpdate();        
      } catch (Exception e) 
      {
			log.error(e,e);
      }
      finally 
      {
         if ( conn != null) {
            try 
             {
                conn.close();
			} catch ( Exception e2) 
            {
				log.error(e2,e2);
			}
        }
      }
     
    }//end of the Method 
    //////////////////////////////////////////////////////////////////////////////

    /**
     * This method processes the rbuSandboxRefresh.
     */
    public static void rbuSandboxRefresh()
    {
    /*  Connection conn = null;*/
    	  Connection conn = JdbcConnectionUtil.getJdbcConnection();
      try
      {
        /*Context ctx = new InitialContext();
        DataSource ds = (DataSource)ctx.lookup(AppConst.APP_DATASOURCE);
        conn =   ds.getConnection();  */
        CallableStatement proc = conn.prepareCall("{ call Daily_Sandbox_Refresh_Task()}");     
        proc.executeUpdate();        
      } catch (Exception e) 
      {
			log.error(e,e);
      }
      finally 
      {
         if ( conn != null) {
            try 
             {
                conn.close();
			} catch ( Exception e2) 
            {
				log.error(e2,e2);
			}
        }
      }
     
    }//end of the Method 
    //////////////////////////////////////////////////////////////////////////////

    //private String fileName = "TRT_TRAINING.txt";
    public void handleNotification(String requestURL) {
         String filename = "";
        try{              
            String output = queryData();
            //Don't write output file
            if(output==null){
                return;    
            } 
          //System.out.println("########### Query" + output);                        
         ByteArrayInputStream str = new ByteArrayInputStream(output.getBytes()); 
            File tfile;
            String p2lFolder = getP2LFolder(PrintingConstants.env_type);
            filename = p2lFolder + File.separator + "P2L_Registration" + formatFileName.format(new Date()) + ".txt";   
            // This is added to get the complete path on different servers
             if(requestURL != null && requestURL.indexOf("wlsdev1.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_INT  + filename;
            }
             else if(requestURL != null && requestURL.indexOf("wlsstg5.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_STG  + filename;
            }
             else if(requestURL != null && requestURL.indexOf("wlsprd4.pfizer.com") != -1){
            filename = PrintingConstants.APPLICATION_PATH_PROD  + filename;
            }
            //System.out.println("########## File name " + filename);
            //tfile=new File(filename);
            FileOutputStream fos = new FileOutputStream(filename);
            int data;
            while((data=str.read())!=-1)
            {
            char ch = (char)data;
            fos.write(ch);
            }
            fos.flush();
            fos.close();
            String fileAttachment = filename;
            Session theSession = (Session)JNDIUtil.lookupS("trMailSession");
        theSession.setDebug(false);
      
        Message theMail = new MimeMessage(theSession);

        theMail.setFrom(new InternetAddress("traininglogistics@pfizer.com"));
       ReadProperties readProperties = new ReadProperties("EmailHandlingProperties.propeties");
       String[] email_to = new String[1]; 
       // In case of P2L registration emails will not be sent . THis is only for checking the validity of data 
       if(requestURL != null && requestURL.indexOf("wlsdev1.pfizer.com") != -1
              || requestURL != null && requestURL.indexOf("wlsstg5.pfizer.com") != -1
              || requestURL != null && requestURL.indexOf("localhost") != -1
              || requestURL != null && requestURL.indexOf("wlsprd4.pfizer.com") != -1){
       LoggerHelper.logSystemDebug("#### Request URL IF for all the environments for P2L registration email sending #####");
        String value = readProperties.getValue("EMAIL_P2L_REGISTRATION_TO_DEV_INT_STG_PROD");
        email_to[0] = value; 
      }
      theMail.setRecipients(Message.RecipientType.TO,
                          new InternetAddress[] { new InternetAddress(email_to[0]) });
	
    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    String currentDate = dateFormat.format(new Date());
	
    theMail.setSubject("P2L Registration File");
    theMail.setContent("Test message", "text/html");
    
    MimeBodyPart  messageBodyPart = new MimeBodyPart();
    messageBodyPart.setText("This is P2L Registration file generated on   " + currentDate );
    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(messageBodyPart);
    messageBodyPart = new MimeBodyPart();
    javax.activation.DataSource source =  new javax.activation.FileDataSource(fileAttachment);
    messageBodyPart.setDataHandler(new DataHandler(source));
    messageBodyPart.setFileName(fileAttachment);
    multipart.addBodyPart(messageBodyPart);

    // Put parts in message
    theMail.setContent(multipart);


    Transport.send(theMail);
        }catch(Exception e){                      
            log.info("TMUUploadListener.handleNotification Error:"+e.toString());
        }       
	}
    private String queryData() throws SQLException,NamingException{
        StringBuffer output = new StringBuffer("");
        String delimiter = "|";
        java.sql.Connection conn = null;
		java.sql.ResultSet  rs = null;
		java.sql.Statement stmt = null;    
        int resultCount = 0;
        conn = DBUtil.getConn("java:jboss/trt_ds");                
        //1. CHECK ACTIVE STATUS        
        stmt = conn.createStatement();        
        rs = stmt.executeQuery("select count(*) from tr_job_schedule where JOB_NAME='TMUUPLOAD' and JOB_STATUS = 'A'");
        if(rs.next()){
            int count = rs.getInt(1);    
            if(count<=0){
                DBUtil.closeDBObjects( conn, rs, stmt);
                return null;                    
            }
        }        
        //2.CALL STORED PROCEDURE        
        CallableStatement proc = conn.prepareCall("{ call TMU_DAILY_UPLOAD_PROCESS()}");		
        proc.execute();
        //3. QUERY DATA
        String sql = "SELECT * FROM TMU_UPLOAD_DATA where status = 0";        
        stmt = conn.createStatement();        
        rs = stmt.executeQuery(sql);   
                
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnLength = rsmd.getColumnCount();
        for(int i=1;i<=columnLength;i++){            
            output.append(rsmd.getColumnName(i));        
            if(i!=columnLength){ 
                output.append(delimiter);            
            } 
        }     
        output.append("\n"); 
        while(rs.next()){
            resultCount = resultCount+1;
            for(int i=1;i<=columnLength;i++){            
                output.append(Util.ifNull(rs.getString(i),""));        
                if(i!=columnLength){ 
                    output.append(delimiter);            
                }                
            }     
            output.append("\n"); 
        }        
        DBUtil.closeDBObjects( conn, rs, stmt);
        if(resultCount==0){
            //Don't write output file id there are no data.    
            return null;
        }else{
            return output.toString();        
        }                
    }
  
     private String getP2LFolder(String sEnv)
    {
        String sDeploymentRoot = VelocityConvertor.getDeploymentRoot(sEnv);
        String sTemplateRoot = sDeploymentRoot + File.separator + "TrainingReports" + File.separator +
                               "P2LFiles" + File.separator + "CreatedFiles";
        return sTemplateRoot;                
    }
  /////////////////////////////////////////////////////////////////////////////  
  
  public static void testEmail(){
    
    //Session theSession = (Session)JNDIUtil.lookupS("trMailSession");
    String emailTo = "yogen.sanghani@pfizer.com";
    String emailBCC = "yogen.sanghani@pfizer.com";
     String sSubject = "Testing the email functionality";
     String sEmailContent = "This email is to confirm that email set up is working correctly.";
    /* Properties props = new Properties();
     props.put("mail.transport.protocol", "smtp");
     props.put("mail.smtp.host", "mailhub.pfizer.com");
     props.put("mail.from", "traininglogistics@pfizer.com");*/
     try{
     MailUtil.sendMessage("traininglogistics@pfizer.com", emailTo, null,emailBCC, sSubject, sEmailContent, "text/html", "trMailSession");
     }
     catch(Exception e)
     {
         LoggerHelper.logSystemDebug("Email sending error >>>>>>>>>>>>"+ e.getMessage());
     }
  }
    
} 
